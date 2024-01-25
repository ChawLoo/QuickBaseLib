package cn.chawloo.base.ext

import android.os.Bundle
import android.os.Parcelable
import cn.chawloo.base.utils.DeviceUtils

/**
 * TODO
 * @author Create by 鲁超 on 2023/12/28 11:52
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
fun Bundle?.string(key: String, default: String = ""): String = this?.getString(key, default) ?: default
fun Bundle?.int(key: String, default: Int = 0): Int = this?.getInt(key, default) ?: default
fun Bundle?.long(key: String, default: Long = 0): Long = this?.getLong(key, default) ?: default
fun Bundle?.float(key: String, default: Float = 0F): Float = this?.getFloat(key, default) ?: default
fun Bundle?.bool(key: String, default: Boolean = false): Boolean = this?.getBoolean(key, default) ?: default
fun Bundle?.stringArrayList(key: String): ArrayList<String> = this?.getStringArrayList(key) ?: arrayListOf()

@Suppress("DEPRECATION")
inline fun <reified T : Parcelable> Bundle?.parcelable(key: String): T? {
    return if (DeviceUtils.isLatestT()) {
        this?.getParcelable(key, T::class.java)
    } else {
        this?.getParcelable(key)
    }
}

@Suppress("DEPRECATION")
inline fun <reified T : Parcelable> Bundle?.parcelableArrayList(key: String): ArrayList<T> {
    return if (DeviceUtils.isLatestT()) {
        this?.getParcelableArrayList(key, T::class.java)
    } else {
        this?.getParcelableArrayList(key)
    } ?: arrayListOf()
}