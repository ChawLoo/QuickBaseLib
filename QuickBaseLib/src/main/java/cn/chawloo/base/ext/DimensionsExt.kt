package cn.chawloo.base.ext

import android.util.TypedValue

/**
 * Dp换算
 * @author Create by 鲁超 on 2020/12/21 0021 16:55
 */
val Int.dp: Int
    get() {
        return (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), application.resources.displayMetrics) + 0.5f).toInt()
    }
val Float.dp: Float
    get() {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, application.resources.displayMetrics) + 0.5f

    }
val Int.sp: Int
    get() {
        return (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), application.resources.displayMetrics) + 0.5f).toInt()
    }
val Float.sp: Float
    get() {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, application.resources.displayMetrics) + 0.5f
    }