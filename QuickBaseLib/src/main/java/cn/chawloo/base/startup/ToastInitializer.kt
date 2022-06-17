package cn.chawloo.base.startup

import android.app.Application
import android.content.Context
import android.view.Gravity
import androidx.startup.Initializer
import cn.chawloo.base.toast.ToastInterceptor
import com.hjq.toast.ToastUtils
import com.hjq.toast.style.BlackToastStyle

/**
 * TODO
 * @author Create by 鲁超 on 2022/1/24 0024 13:55:23
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
class ToastInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        ToastUtils.init(context as Application?, BlackToastStyle())
        ToastUtils.setGravity(Gravity.BOTTOM, 0, 100)
        ToastUtils.setInterceptor(ToastInterceptor())
    }

    override fun dependencies() = emptyList<Class<Initializer<*>>>()
}