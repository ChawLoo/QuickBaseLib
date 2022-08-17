package cn.chawloo.base.ext

import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.math.roundToInt

/**
 * TODO
 * @author Create by 鲁超 on 2022/6/11 0011 13:37:35
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
fun TextView.startCountDown(
    lifecycleOwner: LifecycleOwner,
    secondInFuture: Long = 60,
    onTick: TextView.(secondUntilFinished: Int) -> Unit,
    onFinish: TextView.() -> Unit
): CountDownTimer = object : CountDownTimer(secondInFuture * 1000, 1000) {
    override fun onTick(millisUntilFinished: Long) {
        isEnabled = false
        onTick((millisUntilFinished / 1000F).roundToInt())
    }

    override fun onFinish() {
        isEnabled = true
        this@startCountDown.onFinish()
    }
}.also {
    it.start()
    lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                it.cancel()
            }
        }
    })
}

inline val TextView.textString: String get() = text.toString()
fun TextView.isBlank(): Boolean = textString.isBlank()
fun TextView.isNotBlank(): Boolean = textString.isNotBlank()
inline var TextView.isPasswordVisible: Boolean
    get() = transformationMethod != PasswordTransformationMethod.getInstance()
    set(value) {
        transformationMethod = if (value) {
            HideReturnsTransformationMethod.getInstance()
        } else {
            PasswordTransformationMethod.getInstance()
        }
    }

fun TextView.addUnderline() {
    paint.flags = Paint.UNDERLINE_TEXT_FLAG
}

fun TextView.addDeleteLine() {
    paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
}

fun TextView.transparentHighlightColor() {
    highlightColor = Color.TRANSPARENT
}

fun TextView.enableWhenOtherTextNotEmpty(vararg textViews: TextView) = enableWhenOtherTextChanged(*textViews) { textViews.all { it.isNotBlank() } }

inline fun TextView.enableWhenOtherTextChanged(
    vararg textViews: TextView,
    crossinline block: (Array<out TextView>) -> Boolean
) {
    isEnabled = block(textViews)
    textViews.forEach { tv ->
        tv.doAfterTextChanged {
            isEnabled = block(textViews)
        }
    }
}

enum class DrawableLocation {
    START, END, TOP, BOTTOM
}

fun TextView.drawable(location: DrawableLocation, @DrawableRes drawableId: Int): TextView {
    val drawable = ContextCompat.getDrawable(context, drawableId)?.apply {
        setBounds(0, 0, minimumWidth, minimumHeight)
    }
    return when (location) {
        DrawableLocation.START -> this.also { setCompoundDrawables(drawable, null, null, null) }
        DrawableLocation.END -> this.also { setCompoundDrawables(null, null, drawable, null) }
        DrawableLocation.TOP -> this.also { setCompoundDrawables(null, drawable, null, null) }
        DrawableLocation.BOTTOM -> this.also { setCompoundDrawables(null, null, null, drawable) }
    }
}

fun TextView.drawable(location: DrawableLocation, drawable: Drawable): TextView {
    drawable.apply { setBounds(0, 0, minimumWidth, minimumHeight) }
    return when (location) {
        DrawableLocation.START -> this.also { setCompoundDrawables(drawable, null, null, null) }
        DrawableLocation.END -> this.also { setCompoundDrawables(null, null, drawable, null) }
        DrawableLocation.TOP -> this.also { setCompoundDrawables(null, drawable, null, null) }
        DrawableLocation.BOTTOM -> this.also { setCompoundDrawables(null, null, null, drawable) }
    }
}