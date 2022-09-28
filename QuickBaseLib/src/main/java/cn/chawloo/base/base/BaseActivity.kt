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
import cn.chawloo.base.R
import cn.chawloo.base.event.AntiShake
import cn.chawloo.base.popup.LoadingPop
import cn.chawloo.base.router.Router
import cn.chawloo.base.utils.DeviceUtils
import com.gyf.immersionbar.ktx.immersionBar
import razerdp.basepopup.BasePopupWindow

/**
 * Activity基类（不含业务）
 * @author Create by 鲁超 on 2022/6/7 0007 11:22:04
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
const val BUNDLE_NAME = "bundle_name"

abstract class BaseActivity : AppCompatActivity() {
    protected val util = AntiShake()
    private lateinit var onBackInvokedCallback: OnBackInvokedCallback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
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

    protected val act: BaseActivity get() = this

    override fun onBackPressed() {
        overridePendingTransition(R.anim.anim_activity_slide_left_in, R.anim.anim_activity_slide_right_out)
        super.onBackPressed()
    }

    /**
     * 新版本的返回重写该方法
     */
    open fun backPressed() {
        overridePendingTransition(R.anim.anim_activity_slide_left_in, R.anim.anim_activity_slide_right_out)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (DeviceUtils.isLatestT()) {
            onBackInvokedDispatcher.unregisterOnBackInvokedCallback(onBackInvokedCallback)
        }
        clearProgressDialog()
    }

    override fun getResources(): Resources {
        if (Looper.myLooper() == Looper.getMainLooper()) {
//            AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources())
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

    @Deprecated("废弃了，采用LoadingDialogHelper的扩展函数，过度几个版本就删了")
    fun showProgress(var1: CharSequence = "") = showProgressDialog(var1)

    @Deprecated("废弃了，采用LoadingDialogHelper的扩展函数，过度几个版本就删了")
    fun hideProgress() = dismissProgressDialog()

    @Deprecated("废弃了，采用LoadingDialogHelper的扩展函数，过度几个版本就删了")
    @Volatile
    var mCount = 0

    @Deprecated("废弃了，采用LoadingDialogHelper的扩展函数，过度几个版本就删了")
    private var mLoadingDialog: LoadingPop? = null

    @Deprecated("废弃了，采用LoadingDialogHelper的扩展函数，过度几个版本就删了")
    @Synchronized
    private fun showProgressDialog(msg: CharSequence = "") {
        if (mCount == 0) {
            mLoadingDialog = LoadingPop(this, msg).apply {
                onDismissListener = object : BasePopupWindow.OnDismissListener() {
                    override fun onDismiss() {
                        mCount = 0
                    }
                }
            }
            mLoadingDialog?.showPopupWindow()
        }
        mCount++
    }

    @Deprecated("废弃了，采用LoadingDialogHelper的扩展函数，过度几个版本就删了")
    @Synchronized
    private fun dismissProgressDialog() {
        if (mCount > 0) {
            mCount--
            if (mCount == 0) {
                mLoadingDialog?.dismiss()
                mLoadingDialog = null
            }
        }
    }

    @Deprecated("废弃了，采用LoadingDialogHelper的扩展函数，过度几个版本就删了")
    @Synchronized
    private fun clearProgressDialog() {
        if (mCount > 0) {
            mCount = 0
            mLoadingDialog?.dismiss()
            mLoadingDialog = null
        }
    }
}