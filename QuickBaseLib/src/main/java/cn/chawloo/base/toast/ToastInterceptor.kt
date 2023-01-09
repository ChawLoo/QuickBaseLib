package cn.chawloo.base.toast

import com.hjq.toast.ToastParams
import com.hjq.toast.config.IToastInterceptor

/**
 * 吐司拦截器
 * @author Create by 鲁超 on 2021/5/11 0011 10:36:35
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
class ToastInterceptor : IToastInterceptor {
    private var toastText: CharSequence = ""
    private var lastShowTime = 0L
    override fun intercept(params: ToastParams?): Boolean {
        val showTime = System.currentTimeMillis()
        return if (params?.text == toastText && showTime - lastShowTime <= 300L) {
            lastShowTime = showTime
            toastText = params.text ?: ""
            true
        } else {
            false
        }
    }
}