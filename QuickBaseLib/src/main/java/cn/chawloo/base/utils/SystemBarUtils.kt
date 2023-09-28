package cn.chawloo.base.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.provider.Settings
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity

object SystemBarUtils {
    fun AppCompatActivity.transparentStatusBar() {
        window.statusBarColor = Color.TRANSPARENT
    }

    fun AppCompatActivity.statusBarColorInt(@ColorInt color: Int) {
        window.statusBarColor = color
    }

    fun AppCompatActivity.statusBarColor(@ColorRes color: Int) {
        statusBarColorInt(getColor(color))
    }

    fun AppCompatActivity.statusBarColor(color: String) {
        statusBarColorInt(Color.parseColor(color))
    }

    fun AppCompatActivity.transparentNavigationBar() {
        window.navigationBarColor = Color.TRANSPARENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.navigationBarDividerColor = Color.TRANSPARENT
        }
    }

    fun getStatusBarHeight(): Int {
        var result = 0
        try {
            val resourceId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = Resources.getSystem().getDimensionPixelSize(resourceId)
            }
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }
        return result
    }

    fun getHeightOfNavigationBar(context: Context): Int {
        //如果小米手机开启了全面屏手势隐藏了导航栏则返回 0
        if (Settings.Global.getInt(context.contentResolver, "force_fsg_nav_bar", 0) != 0) {
            return 0
        }
        val realHeight = ScreenUtils.getRawScreenSize(context)[1]
        val displayHeight = ScreenUtils.getScreenSize(context)[1]
        return realHeight - displayHeight
    }

}