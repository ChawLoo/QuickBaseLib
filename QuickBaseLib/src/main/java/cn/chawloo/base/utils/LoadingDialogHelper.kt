package cn.chawloo.base.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import cn.chawloo.base.base.BaseFragment
import cn.chawloo.base.ext.lifecycleOwner
import cn.chawloo.base.popup.LoadingDialog
import com.safframework.log.L

/**
 * TODO
 * @author Create by 鲁超 on 2022/9/27 08:59
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
object LoadingDialogHelper {
    private val dialogQueen = LinkedHashMap<LifecycleOwner, LoadingDialog>()
    private val countQueen = LinkedHashMap<LifecycleOwner, Int>()

    @Synchronized
    fun AppCompatActivity.showLoading(msg: String = "") {
        this.lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    dialogQueen[this@showLoading]?.dismiss()
                    dialogQueen.remove(this@showLoading)
                    countQueen.remove(this@showLoading)
                }
            }
        })
        dialogQueen[this]?.run {
            countQueen[this@showLoading]?.takeIf { it > 0 }?.run {
                countQueen[this@showLoading] = this + 1
            }
        } ?: run {
            val loading = LoadingDialog(this, msg)
            dialogQueen[this] = loading
            loading.show()
            countQueen[this] = 1
        }
    }

    @Synchronized
    fun <B : ViewDataBinding> BaseFragment<B>.showLoading(msg: String = "") {
        this.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    dialogQueen[this@showLoading]?.dismiss()
                    dialogQueen.remove(this@showLoading)
                    countQueen.remove(this@showLoading)
                }
            }
        })
        dialogQueen[this]?.run {
            countQueen[this@showLoading]?.takeIf { it > 0 }?.run {
                countQueen[this@showLoading] = this + 1
            }
        } ?: run {
            this.activity?.run {
                val loading = LoadingDialog(this, msg)
                dialogQueen[this@showLoading] = loading
                loading.show()
                countQueen[this@showLoading] = 1
            } ?: L.e("宿主不存在")
        }
    }

    /**
     * 判断是否存在Dialog
     * 如果不存在则忽略
     * 如果存在  则判断count是否大于0
     * 如果count大于0，则自减1
     * 判断count<1,如果是，则从队列中移除该activity对应的dialog和count，并且dismiss dialog
     */
    @Synchronized
    fun AppCompatActivity.hideLoading() {
        dialogQueen[this]?.run {
            countQueen[this@hideLoading]?.takeIf { it > 0 }?.run {
                countQueen[this@hideLoading] = this - 1
            }
            if ((countQueen[this@hideLoading] ?: 0) < 1) {
                countQueen.remove(this@hideLoading)
                dialogQueen[this@hideLoading]?.dismiss()
                dialogQueen.remove(this@hideLoading)
                this.dismiss()
            }
        }
    }

    @Synchronized
    fun <B : ViewDataBinding> BaseFragment<B>.hideLoading() {
        dialogQueen[this]?.run {
            countQueen[this@hideLoading]?.takeIf { it > 0 }?.run {
                countQueen[this@hideLoading] = this - 1
            }
            if ((countQueen[this@hideLoading] ?: 0) < 1) {
                countQueen.remove(this@hideLoading)
                dialogQueen[this@hideLoading]?.dismiss()
                dialogQueen.remove(this@hideLoading)
                this.dismiss()
            }
        }
    }
}