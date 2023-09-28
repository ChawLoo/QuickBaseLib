package cn.chawloo.base.utils

import android.content.Context
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager

/**
 * TODO
 * @author Create by 鲁超 on 2023/9/28 15:28
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
object ScreenUtils {
    /**
     * 获取当前的屏幕尺寸
     *
     * @param context [Context]
     * @return 屏幕尺寸
     */
    fun getScreenSize(context: Context): IntArray? {
        val size = IntArray(2)
        val w = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val d = w.defaultDisplay
        val metrics = DisplayMetrics()
        d.getMetrics(metrics)
        size[0] = metrics.widthPixels
        size[1] = metrics.heightPixels
        return size
    }

    /**
     * 获取原始的屏幕尺寸
     *
     * @param context [Context]
     * @return 屏幕尺寸
     */
    fun getRawScreenSize(context: Context): IntArray? {
        val size = IntArray(2)
        val w = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val d = w.defaultDisplay
        val metrics = DisplayMetrics()
        d.getMetrics(metrics)
        // since SDK_INT = 1;
        var widthPixels = metrics.widthPixels
        var heightPixels = metrics.heightPixels

        // includes window decorations (status bar/menu bar)
        try {
            val realSize = Point()
            Display::class.java.getMethod("getRealSize", Point::class.java).invoke(d, realSize)
            widthPixels = realSize.x
            heightPixels = realSize.y
        } catch (ignored: Exception) {
        }
        size[0] = widthPixels
        size[1] = heightPixels
        return size
    }

}