package cn.chawloo.base.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import com.hjq.toast.Toaster

const val KEY_FIRST_RUN = "key_first_run"

object AppLauncher {
    /**
     * 启动当前应用设置页面
     */
    fun Context.startAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:$packageName")
        startActivity(intent)
    }

    /**
     * 启动当前应用的权限设置界面
     */
    fun Context.gotoPermissionSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        try {
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 重启Activity
     * 此方法会比 recreate() 效果更好
     */
    fun Activity.reload() {
        overridePendingTransition(0, 0)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        System.gc()
    }

    fun gotoMarket(context: Context) {
        val sb = StringBuilder("market://details?id=")
        val uri = Uri.parse(sb.append(context.packageName).toString())
        val intent = Intent("android.intent.action.VIEW", uri)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val localList = context.packageManager.queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER)
        localList.takeIf { it.isNotEmpty() }?.run {
            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } ?: run {
            Toaster.show("未找到应用市场")
        }
    }
}