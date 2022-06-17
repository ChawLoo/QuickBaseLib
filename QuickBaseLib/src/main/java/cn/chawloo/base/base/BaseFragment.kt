package cn.chawloo.base.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

/**
 * Fragment基类（不包含业务）
 * @author Create by 鲁超 on 2022/6/7 0007 14:28:27
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
abstract class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId) {
    protected lateinit var mActivity: BaseActivity
    protected lateinit var mContext: Context

    override fun onAttach(context: Context) {
        mActivity = context as BaseActivity
        mContext = context
        super.onAttach(mContext)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    abstract fun initialize()

    protected fun showProgress(msg: CharSequence = "") = mActivity.showProgress(msg)

    protected fun hideProgress() = mActivity.hideProgress()
}