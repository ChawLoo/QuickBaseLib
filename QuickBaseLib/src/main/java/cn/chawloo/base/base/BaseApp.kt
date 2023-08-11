package cn.chawloo.base.base

import androidx.multidex.MultiDexApplication
import cn.chawloo.base.ext.OnAppStatusChangedListener
import cn.chawloo.base.ext.activityCache
import cn.chawloo.base.ext.application
import cn.chawloo.base.ext.doOnActivityLifecycle

/**
 * Application基类
 * @author Create by 鲁超 on 2022/6/7 0007 14:17:06
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
open class BaseApp : MultiDexApplication() {
    private var started = 0

    companion object {
        internal var onAppStatusChangedListener: OnAppStatusChangedListener? = null
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        application.doOnActivityLifecycle(
            onActivityCreated = { activity, _ ->
                activityCache.add(activity)
            },
            onActivityStarted = { activity ->
                started++
                if (started == 1) {
                    onAppStatusChangedListener?.onForeground(activity)
                }
            },
            onActivityStopped = { activity ->
                started--
                if (started == 0) {
                    onAppStatusChangedListener?.onBackground(activity)
                }
            },
            onActivityDestroyed = { activity ->
                activityCache.remove(activity)
            }
        )
    }
}