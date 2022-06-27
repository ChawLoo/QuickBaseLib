package cn.chawloo.base.event

import android.view.View
import kotlin.math.abs

/**
 * 点击防抖工具
 * @author Create by 鲁超 on 2021/2/24 0024 10:41
 */
object AopClickUtil {
    /**
     * 最近一次点击的时间
     */
    private var mLastClickTime: Long = 0

    /**
     * 最近一次点击的控件ID
     */
    private var mLastClickViewId = 0

    /**
     * 是否是快速点击
     *
     * @param v  点击的控件
     * @param intervalMillis  时间间期（毫秒）
     * @return  true:是，false:不是
     */
    fun isFastDoubleClick(v: View, intervalMillis: Long): Boolean {
        val viewId: Int = v.id
        val time = System.currentTimeMillis()
        val timeInterval = abs(time - mLastClickTime)
        return if (timeInterval < intervalMillis && viewId == mLastClickViewId) {
            true
        } else {
            mLastClickTime = time
            mLastClickViewId = viewId
            false
        }
    }
}