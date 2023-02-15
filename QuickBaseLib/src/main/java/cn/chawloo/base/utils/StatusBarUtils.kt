package cn.chawloo.base.utils

import android.graphics.Color
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity

object StatusBarUtils {
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
}