package cn.chawloo.base.event

import com.safframework.log.L

/**
 * 单词点击检测
 * @author Create by 鲁超 on 2020/12/21 0021 17:15
 */
class OneClickUtil(val methodName: String) {
    companion object {
        const val MIN_CLICK_DELAY_TIME = 300
    }

    private var lastClickTime: Long = 0

    fun check(): Boolean {
        val currentTime = System.currentTimeMillis()
        return if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime
            false
        } else {
            L.e("防止用户快速点击")
            true
        }
    }
}