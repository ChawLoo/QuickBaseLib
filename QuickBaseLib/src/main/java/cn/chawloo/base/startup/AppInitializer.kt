@file:Suppress("unused")

package cn.chawloo.base.startup

import android.app.Application
import android.content.Context
import androidx.startup.Initializer
import cn.chawloo.base.ext.activityCache
import cn.chawloo.base.ext.application
import cn.chawloo.base.ext.doOnActivityLifecycle

class AppInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        application = context as Application
        application.doOnActivityLifecycle(
            onActivityCreated = { activity, _ ->
                activityCache.add(activity)
            },
            onActivityDestroyed = { activity ->
                activityCache.remove(activity)
            }
        )
    }

    override fun dependencies() = emptyList<Class<Initializer<*>>>()
}
