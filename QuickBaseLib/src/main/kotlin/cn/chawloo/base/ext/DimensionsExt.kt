package cn.chawloo.base.ext

import android.app.UiModeManager
import android.content.Context
import cn.chawloo.base.base.BaseApp
import com.safframework.log.L
import me.jessyan.autosize.utils.AutoSizeUtils
import org.koin.java.KoinJavaComponent.get

/**
 * Dp换算
 * @author Create by 鲁超 on 2020/12/21 0021 16:55
 */
val Int.dp: Int
    get() {
        return AutoSizeUtils.dp2px(get<BaseApp>(BaseApp::class.java), this.toFloat())
    }
val Float.dp: Float
    get() {
        return AutoSizeUtils.dp2px(get<BaseApp>(BaseApp::class.java), this).toFloat()
    }
val Int.sp: Int
    get() {
        return AutoSizeUtils.sp2px(get<BaseApp>(BaseApp::class.java), this.toFloat())
    }
val Float.sp: Float
    get() {
        return AutoSizeUtils.sp2px(get<BaseApp>(BaseApp::class.java), this).toFloat()
    }

fun Context.isDarkMode(): Boolean {
    val uiModeManager: UiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
    val isNight = uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES
    L.e("是否是黑夜模式:$isNight")
    return isNight
}