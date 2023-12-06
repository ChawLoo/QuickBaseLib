package cn.chawloo.base.viewbinding

import android.app.Activity
import android.view.View
import androidx.viewbinding.ViewBinding

/**
 * TODO
 * @author Create by 鲁超 on 2023/12/6 15:26
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
interface ActivityBinding<VB : ViewBinding> {
    val vb: VB
    fun Activity.setContentViewWithBinding()
}

class ActivityBindingDelegate<VB : ViewBinding> : ActivityBinding<VB> {
    private lateinit var _binding: VB
    override val vb: VB
        get() = _binding

    override fun Activity.setContentViewWithBinding() {
        _binding = ViewBindingUtil.inflateWithGeneric(this, layoutInflater)
        setContentView(_binding.root)
    }

}