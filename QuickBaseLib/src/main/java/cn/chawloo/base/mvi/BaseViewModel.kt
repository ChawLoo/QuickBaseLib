package cn.chawloo.base.mvi

import androidx.lifecycle.ViewModel
import cn.chawloo.base.mvi.event.SingleLiveEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import org.koin.core.component.KoinComponent

/**
 * ViewModel基类（不包含业务，都是通用的）
 * @author Create by 鲁超 on 2021/12/16 0016 11:20:10
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
abstract class BaseViewModel : ViewModel(), KoinComponent {
    private val _viewEvents: SingleLiveEvent<PageEvent> = SingleLiveEvent()
    val event = _viewEvents.asLiveData()
    protected fun showToast(msg: String?) {
        _viewEvents.setEvent(PageEvent.ShowToast(msg))
    }

    protected fun showToast(t: Throwable) {
        _viewEvents.setEvent(PageEvent.ShowToast(t.message))
    }

    protected fun hideLoading() {
        _viewEvents.setEvent(PageEvent.HideLoading)
    }

    protected fun showLoading() {
        _viewEvents.setEvent(PageEvent.ShowLoading)
    }

    abstract fun hideStatus()

    suspend fun Flow<*>.customCollect() {
        this.onCompletion {
            hideStatus()
        }.collect()
    }
}