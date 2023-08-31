package cn.chawloo.base.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import cn.chawloo.base.delegate.IUpdate
import cn.chawloo.base.delegate.UpdateDelegate

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
abstract class BaseFragment<B : ViewDataBinding>(@LayoutRes layoutId: Int) : Fragment(layoutId), IUpdate by UpdateDelegate {
    protected lateinit var mContext: Context

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(mContext)
    }

    val vb: B
        get() = DataBindingUtil.bind(requireView())!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DataBindingUtil.bind<B>(view)
        initialize()
    }

    abstract fun initialize()
}