package cn.chawloo.base.ext

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import cn.chawloo.base.base.BaseLibConfig
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 视图相关扩展函数
 * @author Create by 鲁超 on 2021/3/9 0009 16:57
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
fun <T : View> T.gone() {
    if (this.visibility != View.GONE) {
        this.visibility = View.GONE
    }
}

fun <T : View> T.visible(): T {
    if (this.visibility != View.VISIBLE) {
        this.visibility = View.VISIBLE
    }
    return this
}

/**
 * 如果visible 则Visible  否则 Gone
 */
fun View.if2Visible(isVisible: Boolean): View {
    if (isVisible) {
        this.visible()
    } else {
        this.gone()
    }
    return this
}

/**
 * 根据传入条件判断隐藏，否则展示
 * @param isGone 是否隐藏
 */
fun View.if2Gone(isGone: Boolean): View {
    if (isGone) {
        this.gone()
    } else {
        this.visible()
    }
    return this
}

/**
 * 把显示状态反转
 */
fun View.reverseVisible() {
    if (this.isShown) {
        gone()
    } else {
        visible()
    }
}

/**
 * 把选择状态反转
 */
fun View.reverseSelected() {
    this.isSelected = !this.isSelected
}

fun CheckBox.reverseChecked() {
    this.isChecked = !this.isChecked
}

fun TextView.clear() {
    this.text = ""
}

fun <T : View> List<T>.doClick(clickIntervals: Int = BaseLibConfig.clickIntervals, isSharingIntervals: Boolean = false, block: T.() -> Unit) =
    forEach { it.doClick(clickIntervals, isSharingIntervals, block) }

fun <T : View> T.doClick(clickIntervals: Int = BaseLibConfig.clickIntervals, isSharingIntervals: Boolean = false, block: T.() -> Unit) = setOnClickListener {
    this.context.asActivity()?.hideSoftKeyboard()
    val view = if (isSharingIntervals) context.asActivity()?.window?.decorView ?: this else this
    val currentTime = System.currentTimeMillis()
    val lastTime = view.lastClickTime ?: 0
    if (currentTime - lastTime > clickIntervals) {
        view.lastClickTime = currentTime
        block()
    }
}

fun <T> viewTags(key: Int) = object : ReadWriteProperty<View, T?> {
    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: View, property: KProperty<*>): T? = thisRef.getTag(key) as? T
    override fun setValue(thisRef: View, property: KProperty<*>, value: T?) = thisRef.setTag(key, value)
}

val View.rootWindowInsetsCompat: WindowInsetsCompat?
    get() {
        if (rootWindowInsetsCompatCache == null) {
            rootWindowInsetsCompatCache = ViewCompat.getRootWindowInsets(this)
        }
        return rootWindowInsetsCompatCache
    }
val View.windowInsetsControllerCompat: WindowInsetsControllerCompat?
    get() {
        if (windowInsetsControllerCompatCache == null) {
            windowInsetsControllerCompatCache = ViewCompat.getWindowInsetsController(this)
        }
        return windowInsetsControllerCompatCache
    }