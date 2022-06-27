package cn.chawloo.base.utils

import android.app.Activity
import androidx.fragment.app.Fragment
import kotlin.reflect.KProperty

/**
 * TODO
 * @author Create by 鲁超 on 2022/6/8 0008 16:09:31
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
class Extras<out T>(private val key: String, private val default: T) {
    operator fun getValue(thisRef: Any, kProperty: KProperty<*>): T {
        return when (thisRef) {
            is Activity -> thisRef.intent?.extras?.get(key) as? T ?: default
            is Fragment -> thisRef.arguments?.get(key) as? T ?: default
            else -> default
        }
    }
}