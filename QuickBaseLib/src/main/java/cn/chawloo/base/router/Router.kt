package cn.chawloo.base.router

import android.content.Intent
import android.os.Bundle
import cn.chawloo.base.base.BUNDLE_NAME
import com.alibaba.android.arouter.launcher.ARouter

/**
 * 路由器
 * @author Create by 鲁超 on 2022/6/7 0007 11:28:42
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
object Router {
    fun inject(clz: Any) {
        ARouter.getInstance().inject(clz)
    }

    fun goto(path: String, bundle: Bundle.() -> Unit = {}) {
        ARouter.getInstance().build(path).withBundle(BUNDLE_NAME, Bundle().apply(bundle)).navigation()
    }

    fun logout(path: String) {
        ARouter.getInstance().build(path).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }.navigation()
    }
}