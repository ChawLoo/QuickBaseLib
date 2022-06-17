package cn.chawloo.base.mvi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * 基础MVI状态
 * @author Create by 鲁超 on 2021/12/15 0015 15:02:39
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
fun <T> MutableLiveData<T>.asLiveData(): LiveData<T> {
    return this
}

sealed class PageState<out T> {
    data class Success<T>(val data: T) : PageState<T>()
    data class Error<T>(val message: String?) : PageState<T>() {
        constructor(t: Throwable) : this(t.message ?: "")
    }
}

sealed class PageEvent {
    data class ShowToast(val msg: String?) : PageEvent()
    object ShowLoading : PageEvent()
    object HideLoading : PageEvent()
}

sealed class PageStatus {
    object Loading : PageStatus()
    object Success : PageStatus()
    data class Error(val throwable: Throwable) : PageStatus()
}

sealed class ListPageEvent {
    object HideSwipeRefresh : ListPageEvent()
    data class RefreshFailed(val msg: String) : ListPageEvent()
    data class LoadMoreFailed(val msg: String) : ListPageEvent()
}