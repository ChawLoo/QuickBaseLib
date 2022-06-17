package cn.chawloo.base.utils

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*
import kotlin.experimental.and

/**
 * 设备相关工具类
 * @author Create by 鲁超 on 2021/7/15 0015 8:54:34
 *----------Dragon be here!----------/
 *       ┌─┐      ┌─┐
 *     ┌─┘─┴──────┘─┴─┐
 *     │              │
 *     │      ─       │
 *     │  ┬─┘   └─┬   │
 *     │              │
 *     │      ┴       │
 *     │              │
 *     └───┐      ┌───┘
 *         │      │神兽保佑
 *         │      │代码无BUG！
 *         │      └──────┐
 *         │             ├┐
 *         │             ┌┘
 *         └┐ ┐ ┌───┬─┐ ┌┘
 *          │ ┤ ┤   │ ┤ ┤
 *          └─┴─┘   └─┴─┘
 *─────────────神兽出没───────────────/
 */
object DeviceUtils {
    /**
     * 获取版本名称
     */
    fun Context.getVersionName(): String {
        return try {
            packageManager.getPackageInfo(packageName, 0).versionName
        } catch (e: Exception) {
            e.printStackTrace()
            "0.1.0"
        }
    }

    /**
     * 获取版本名称
     */
    fun Context.getVersionCode(): Long {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                packageManager.getPackageInfo(packageName, 0).longVersionCode
            } else {
                packageManager.getPackageInfo(packageName, 0).versionCode.toLong()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            1
        }
    }

    fun Context.getDeviceId(): String {
        val sb = StringBuilder()
        val imei = getIMEI()
        val androidId = getAndroidId()
        val serial = getSERIAL()
        val uuid = getDeviceUUID().replace("-", "")
        if (imei.isNotBlank()) {
            sb.append(imei)
            sb.append("|")
        }
        if (androidId.isNotBlank()) {
            sb.append(androidId)
            sb.append("|")
        }
        if (serial.isNotBlank()) {
            sb.append(serial)
            sb.append("|")
        }
        if (uuid.isNotBlank()) {
            sb.append(uuid)
        }
        if (sb.isNotBlank()) {
            try {
                val hash = sb.toString().toHash()
                val sha1 = bytesToHex(hash)
                if (sha1.isNotBlank()) {
                    return sha1
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return UUID.randomUUID().toString().replace("-", "")
    }

    //需要获得READ_PHONE_STATE权限，>=6.0，默认返回null
    private fun Context.getIMEI(): String {
        return try {
            (getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
        } catch (ex: Exception) {
            ex.printStackTrace()
            ""
        }
    }

    /**
     * 获得设备的AndroidId
     *
     * @return 设备的AndroidId
     */
    private fun Context.getAndroidId(): String {
        return try {
            Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        } catch (ex: Exception) {
            ex.printStackTrace()
            ""
        }
    }

    /**
     * 获得设备序列号（如：WTK7N16923005607）, 个别设备无法获取
     *
     * @return 设备序列号
     */
    private fun getSERIAL(): String {
        return try {
            Build.SERIAL
        } catch (ex: Exception) {
            ex.printStackTrace()
            ""
        }
    }

    /**
     * 获得设备硬件uuid
     * 使用硬件信息，计算出一个随机数
     *
     * @return 设备硬件uuid
     */
    private fun getDeviceUUID(): String {
        return try {
            val dev = "3883756" +
                    Build.BOARD.length % 10 +
                    Build.BRAND.length % 10 +
                    Build.DEVICE.length % 10 +
                    Build.HARDWARE.length % 10 +
                    Build.ID.length % 10 +
                    Build.MODEL.length % 10 +
                    Build.PRODUCT.length % 10 +
                    Build.SERIAL.length % 10
            UUID(dev.hashCode().toLong(), Build.SERIAL.hashCode().toLong()).toString()
        } catch (ex: Exception) {
            ex.printStackTrace()
            ""
        }
    }

    /**
     * 取SHA1
     *
     * @param data 数据
     * @return 对应的hash值
     */
    private fun String.toHash(): ByteArray {
        return try {
            val messageDigest = MessageDigest.getInstance("SHA1")
            messageDigest.reset()
            messageDigest.update(toByteArray(StandardCharsets.UTF_8))
            messageDigest.digest()
        } catch (e: Exception) {
            "".toByteArray()
        }
    }

    private val hexArray = "0123456789ABCDEF".toCharArray()

    /**
     * 转16进制字符串
     *
     * @param bytes 数据
     * @return 16进制字符串
     */
    private fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        for (j in bytes.indices) {
            val v = (bytes[j] and 0xFF.toByte()).toInt()
            hexChars[j * 2] = hexArray[v ushr 4]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }
}