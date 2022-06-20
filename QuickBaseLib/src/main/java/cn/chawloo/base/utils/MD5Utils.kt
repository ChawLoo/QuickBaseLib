package cn.chawloo.base.utils

import com.safframework.log.L.e
import java.security.MessageDigest

/**
 * MD5工具类
 * @author Create by 鲁超 on 2021/1/5 0005 10:43
 */
object MD5Utils {
    fun String.toMD5(): String {
        val hash = MessageDigest.getInstance("MD5").digest(toByteArray())
        val hex = StringBuilder(hash.size * 2)
        for (b in hash) {
            var str = Integer.toHexString(b.toInt())
            if (b < 0x10) {
                str = "0$str"
            }
            hex.append(str.substring(str.length - 2))
        }
        return hex.toString()
    }

    /** md5加密 */
    fun md5(content: String): String {
        val hash = MessageDigest.getInstance("MD5").digest(content.toByteArray())
        val hex = StringBuilder(hash.size * 2)
        for (b in hash) {
            var str = Integer.toHexString(b.toInt())
            if (b < 0x10) {
                str = "0$str"
            }
            hex.append(str.substring(str.length - 2))
        }
        return hex.toString()
    }

    /***
     *
     * @Description 32位MD5 加密,小写
     * @Date 2013-4-10
     * @param inStr
     * @return String
     */
    fun md5ToHex(inStr: String?): String {
        if (inStr == null) {
            return ""
        }
        val byteArray: ByteArray = inStr.toByteArray()
        return md5ToHexWithData(byteArray)
    }

    /***
     *
     * @Description 32位MD5 加密,小写
     * @Date 2013-4-10
     * @return String
     */
    private fun md5ToHexWithData(byteArray: ByteArray): String {
        val md5: MessageDigest = try {
            MessageDigest.getInstance("MD5")
        } catch (e: java.lang.Exception) {
            e(e.toString())
            e.printStackTrace()
            return ""
        }
        val md5Bytes = md5.digest(byteArray)
        val hexValue = StringBuilder()
        for (md5Byte in md5Bytes) {
            val value = md5Byte.toInt() and 0xff
            if (value < 16) {
                hexValue.append("0")
            }
            hexValue.append(Integer.toHexString(value))
        }
        return hexValue.toString()
    }

    //十六进制下数字到字符的映射数组
    private val hexDigits = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "b", "n", "z", "y", "v", "a")

    /**
     * 对字符串进行MD5加密
     */
    fun encode(originString: String?): String? {
        if (originString != null) {
            try {
                //创建具有指定算法名称的信息摘要
                val md = MessageDigest.getInstance("MD5")
                //使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
                val results = md.digest(originString.toByteArray())
                //将得到的字节数组变成字符串返回
                val resultString = byteArrayToHexString(results)
                return resultString.uppercase()
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }
        return null
    }

    /**
     * 转换字节数组为十六进制字符串
     *
     * @param b 字节数组
     * @return 十六进制字符串
     */
    private fun byteArrayToHexString(b: ByteArray): String {
        val resultSb = java.lang.StringBuilder()
        for (value in b) {
            resultSb.append(byteToHexString(value))
        }
        return resultSb.toString()
    }

    /**
     * 将一个字节转化成十六进制形式的字符串
     */
    private fun byteToHexString(b: Byte): String {
        var n = b.toInt()
        if (n < 0) {
            n += 256
        }
        val d1 = n / 16
        val d2 = n % 16
        return hexDigits[d1] + hexDigits[d2]
    }
}