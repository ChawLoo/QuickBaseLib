package cn.chawloo.base.ext

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import com.hjq.toast.Toaster
import java.io.Serializable

/**
 * 扩展函数，放一些不知道怎么分类的扩展函数  知道后可以迁移
 * @author Create by 鲁超 on 2022/6/7 0007 14:16:32
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
fun String?.insertClipboard(context: Context, title: String = "标题已复制") {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboardManager.setPrimaryClip(ClipData.newPlainText(null, this))
    Toaster.show(title)
}

fun <T> Bundle.put(key: String, value: T) {
    when (value) {
        is Boolean -> putBoolean(key, value)
        is BooleanArray -> putBooleanArray(key, value)
        is String -> putString(key, value)
        is Int -> putInt(key, value)
        is IntArray -> putIntArray(key, value)
        is Short -> putShort(key, value)
        is ShortArray -> putShortArray(key, value)
        is Long -> putLong(key, value)
        is LongArray -> putLongArray(key, value)
        is Byte -> putByte(key, value)
        is ByteArray -> putByteArray(key, value)
        is Char -> putChar(key, value)
        is CharArray -> putCharArray(key, value)
        is CharSequence -> putCharSequence(key, value)
        is Float -> putFloat(key, value)
        is FloatArray -> putFloatArray(key, value)
        is Bundle -> putBundle(key, value)
        is Parcelable -> putParcelable(key, value)
        is Serializable -> putSerializable(key, value)
        else -> throw IllegalArgumentException("Type of property $key is not supported")
    }
}

fun <T> Bundle.get(key: String, default: T): T {
    return get(key) as? T ?: default
}