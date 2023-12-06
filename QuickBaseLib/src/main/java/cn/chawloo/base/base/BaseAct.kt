package cn.chawloo.base.base

import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.content.res.TypedArray
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.window.OnBackInvokedCallback
import android.window.OnBackInvokedDispatcher
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import cn.chawloo.base.R
import cn.chawloo.base.delegate.IUpdate
import cn.chawloo.base.delegate.UpdateDelegate
import cn.chawloo.base.router.Router
import cn.chawloo.base.utils.DeviceUtils
import cn.chawloo.base.viewbinding.ActivityBinding
import cn.chawloo.base.viewbinding.ActivityBindingDelegate
import com.gyf.immersionbar.ktx.immersionBar
import me.jessyan.autosize.AutoSizeCompat

/**
 * TODO
 * @author Create by 鲁超 on 2023/8/31 14:53
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
abstract class BaseAct<VB : ViewBinding> : AppCompatActivity(), IUpdate by UpdateDelegate, ActivityBinding<VB> by ActivityBindingDelegate() {

    private lateinit var onBackInvokedCallback: OnBackInvokedCallback
    private var isForcePortrait = true

    protected open fun isForcePortrait(): Boolean {
        return true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentViewWithBinding()
        isForcePortrait = isForcePortrait()
        initGlobalUIConfig()
        Router.inject(this)
        if (DeviceUtils.isLatestT()) {
            onBackInvokedCallback = OnBackInvokedCallback { backPressed() }.also {
                onBackInvokedDispatcher.registerOnBackInvokedCallback(OnBackInvokedDispatcher.PRIORITY_DEFAULT, it)
            }
        } else {
            onBackPressedDispatcher.addCallback(owner = this) {
                backPressed()
            }
        }
        initialize()
        onClick()
    }

    /**
     * 设置一些沉浸式   横竖屏等全局UI参数
     */
    private fun initGlobalUIConfig() {
        if (isForcePortrait) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        immersionBar {
            fitsSystemWindows(true)
            transparentStatusBar()
            statusBarColor(R.color.bg_color)
            navigationBarColor(android.R.color.transparent)
            navigationBarDarkIcon(true)
            statusBarDarkFont(true)
        }
    }

    abstract fun initialize()
    open fun onClick() {}
    open fun backPressed() {
        println("触发返回键拦截了")
        overridePendingTransition(R.anim.anim_activity_slide_left_in, R.anim.anim_activity_slide_right_out)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (DeviceUtils.isLatestT()) {
            onBackInvokedDispatcher.unregisterOnBackInvokedCallback(onBackInvokedCallback)
        }
    }

    override fun getResources(): Resources {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources())
        }
        return super.getResources()
    }

    override fun setRequestedOrientation(requestedOrientation: Int) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) return
        super.setRequestedOrientation(requestedOrientation)
    }

    private fun isTranslucentOrFloating(): Boolean {
        var isTranslucentOrFloating = false
        try {
            val styleableRes = Class.forName("com.android.internal.R\$styleable").getField("Window")[null] as IntArray
            val ta = obtainStyledAttributes(styleableRes)
            val m = ActivityInfo::class.java.getMethod("isTranslucentOrFloating", TypedArray::class.java)
            m.isAccessible = true
            isTranslucentOrFloating = m.invoke(null, ta) as Boolean
            m.isAccessible = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isTranslucentOrFloating
    }
}