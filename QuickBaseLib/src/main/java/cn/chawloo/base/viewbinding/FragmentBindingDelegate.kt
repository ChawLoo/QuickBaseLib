package cn.chawloo.base.viewbinding

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

/**
 * TODO
 * @author Create by 鲁超 on 2023/12/6 15:30
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
interface FragmentBinding<VB : ViewBinding> {
    val vb: VB
    fun Fragment.createViewWithBinding(inflater: LayoutInflater, container: ViewGroup?): View
}

class FragmentBindingDelegate<VB : ViewBinding> : FragmentBinding<VB> {
    private var _binding: VB? = null
    private val handler by lazy { Handler(Looper.getMainLooper()) }

    override val vb: VB
        get() = requireNotNull(_binding) { "The property of binding has been destroyed." }

    override fun Fragment.createViewWithBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        if (_binding == null) {
            _binding = ViewBindingUtil.inflateWithGeneric(this, inflater, container, false)
            viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    handler.post { _binding = null }
                }
            })
        }
        return _binding!!.root
    }
}