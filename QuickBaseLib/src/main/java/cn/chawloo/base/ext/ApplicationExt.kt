@file:Suppress("unused")

package cn.chawloo.base.ext

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.net.Uri
import android.os.Process
import android.provider.Settings
import androidx.core.content.getSystemService
import androidx.core.content.pm.PackageInfoCompat
import cn.chawloo.base.base.BaseApp

/**
 * TODO
 * @author Create by 鲁超 on 2022/8/17 17:05
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
lateinit var application: Application
    internal set

inline val packageName: String get() = application.packageName

inline val packageInfo: PackageInfo
    get() = application.packageManager.getPackageInfo(packageName, 0)

inline val appName: String
    get() = application.applicationInfo.loadLabel(application.packageManager).toString()

inline val appIcon: Drawable? get() = packageInfo.applicationInfo?.loadIcon(application.packageManager)

inline val appVersionName: String get() = packageInfo.versionName ?: "1.0"

inline val appVersionCode: Long get() = PackageInfoCompat.getLongVersionCode(packageInfo)

inline val isAppDebug: Boolean get() = application.isAppDebug

inline val Application.isAppDebug: Boolean
    get() = packageManager.getApplicationInfo(packageName, 0).flags and ApplicationInfo.FLAG_DEBUGGABLE != 0

inline val isAppDarkMode: Boolean
    get() = (application.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

inline val isLocationEnabled: Boolean
    get() = application.getSystemService<LocationManager>()?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true

fun launchAppSettings(): Boolean =
    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        .apply { data = Uri.fromParts("package", packageName, null) }
        .startForActivity()

fun relaunchApp(killProcess: Boolean = true) =
    application.packageManager.getLaunchIntentForPackage(packageName)?.let {
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(it)
        if (killProcess) Process.killProcess(Process.myPid())
    }

fun doOnAppStatusChanged(onForeground: ((Activity) -> Unit)? = null, onBackground: ((Activity) -> Unit)? = null) =
    doOnAppStatusChanged(object : OnAppStatusChangedListener {
        override fun onForeground(activity: Activity) {
            onForeground?.invoke(activity)
        }

        override fun onBackground(activity: Activity) {
            onBackground?.invoke(activity)
        }
    })

fun doOnAppStatusChanged(listener: OnAppStatusChangedListener) {
    BaseApp.onAppStatusChangedListener = listener
}

interface OnAppStatusChangedListener {
    fun onForeground(activity: Activity)
    fun onBackground(activity: Activity)
}
