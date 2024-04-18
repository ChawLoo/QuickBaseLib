package cn.chawloo.base.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import cn.chawloo.base.delegate.IUpdate
import cn.chawloo.base.delegate.UpdateDelegate
import cn.chawloo.base.viewbinding.FragmentBinding
import cn.chawloo.base.viewbinding.FragmentBindingDelegate

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
abstract class BaseFragment<VB : ViewBinding>(@LayoutRes layoutId: Int) : Fragment(layoutId), IUpdate by UpdateDelegate, FragmentBinding<VB> by FragmentBindingDelegate<VB>() {
    protected lateinit var mContext: Context

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(mContext)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewWithBinding(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    abstract fun initialize()
}