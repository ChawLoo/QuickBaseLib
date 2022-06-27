package cn.chawloo.base.utils

import android.os.Parcelable
import com.tencent.mmkv.MMKV

object MK {
    private var mmkv = MMKV.defaultMMKV()

    fun encode(key: String, value: Any?) {
        when (value) {
            is String -> mmkv?.encode(key, value)
            is Float -> mmkv?.encode(key, value)
            is Boolean -> mmkv?.encode(key, value)
            is Int -> mmkv?.encode(key, value)
            is Long -> mmkv?.encode(key, value)
            is Double -> mmkv?.encode(key, value)
            is ByteArray -> mmkv?.encode(key, value)
            is Parcelable -> mmkv?.encode(key, value)
            else -> return
        }
    }

    fun encode(key: String, sets: Set<String>) {
        mmkv?.encode(key, sets)
    }

    fun decodeInt(key: String, defaultValue: Int = 0): Int {
        return mmkv?.decodeInt(key, defaultValue) ?: defaultValue
    }

    fun decodeDouble(key: String, defaultValue: Double = 0.00): Double {
        return mmkv?.decodeDouble(key, defaultValue) ?: defaultValue
    }

    fun decodeLong(key: String, defaultValue: Long = 0L): Long {
        return mmkv?.decodeLong(key, defaultValue) ?: defaultValue
    }

    fun decodeBool(key: String, defaultValue: Boolean = false): Boolean {
        return mmkv?.decodeBool(key, defaultValue) ?: defaultValue
    }

    fun decodeFloat(key: String, defaultValue: Float = 0F): Float {
        return mmkv?.decodeFloat(key, defaultValue) ?: defaultValue
    }

    fun decodeByteArray(key: String, defaultValue: ByteArray = byteArrayOf()): ByteArray {
        return mmkv?.decodeBytes(key, defaultValue) ?: defaultValue
    }

    fun decodeString(key: String, defaultValue: String = ""): String {
        return mmkv?.decodeString(key, defaultValue) ?: defaultValue
    }

    fun <T : Parcelable> decodeParcelable(key: String, tClass: Class<T>): T? {
        return mmkv?.decodeParcelable(key, tClass)
    }

    fun decodeStringSet(key: String, defaultValue: Set<String> = setOf()): Set<String> {
        return mmkv?.decodeStringSet(key, defaultValue) ?: defaultValue
    }

    fun removeKeys(vararg key: String) {
        mmkv?.removeValuesForKeys(key)
    }

    fun clearAll() {
        mmkv?.clearAll()
    }
}