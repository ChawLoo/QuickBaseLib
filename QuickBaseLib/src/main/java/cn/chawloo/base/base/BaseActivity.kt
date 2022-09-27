package cn.chawloo.base.base

import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.content.res.TypedArray
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import cn.chawloo.base.R
import cn.chawloo.base.event.AntiShake
import cn.chawloo.base.popup.LoadingPop
import cn.chawloo.base.router.Router
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
    private var mStateSaved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initGlobalUIConfig()
        Router.inject(this)
        initialize()
        onClick()
        mStateSaved = false
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

    override fun onResume() {
        super.onResume()
        mStateSaved = false
    }

    protected val act: BaseActivity get() = this

    fun showProgress(var1: CharSequence = "") = showProgressDialog(var1)

    fun hideProgress() = dismissProgressDialog()

    @Volatile
    var mCount = 0
    private var mLoadingDialog: LoadingPop? = null

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

    @Synchronized
    private fun clearProgressDialog() {
        if (mCount > 0) {
            mCount = 0
            mLoadingDialog?.dismiss()
            mLoadingDialog = null
        }
    }

    override fun onBackPressed() {
        overridePendingTransition(R.anim.anim_activity_slide_left_in, R.anim.anim_activity_slide_right_out)
        if (!mStateSaved) super.onBackPressed()
    }

    override fun onStart() {
        super.onStart()
        mStateSaved = false
    }

    override fun onStop() {
        super.onStop()
        mStateSaved = true
    }

    override fun onDestroy() {
        super.onDestroy()
        clearProgressDialog()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (!mStateSaved) {
            super.onKeyDown(keyCode, event)
        } else {
            true
        }
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mStateSaved = true
    }
}