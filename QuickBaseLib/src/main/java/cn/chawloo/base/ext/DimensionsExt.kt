package cn.chawloo.base.ext

import me.jessyan.autosize.utils.AutoSizeUtils

/**
 * Dp换算
 * @author Create by 鲁超 on 2020/12/21 0021 16:55
 */
val Int.dp: Int
    get() {
        return AutoSizeUtils.dp2px(application, this.toFloat())
    }
val Float.dp: Float
    get() {
        return AutoSizeUtils.dp2px(application, this).toFloat()
    }
val Int.sp: Int
    get() {
        return AutoSizeUtils.sp2px(application, this.toFloat())
    }
val Float.sp: Float
    get() {
        return AutoSizeUtils.sp2px(application, this).toFloat()
    }