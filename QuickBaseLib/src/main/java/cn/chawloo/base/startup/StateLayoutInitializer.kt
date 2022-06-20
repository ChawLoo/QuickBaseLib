package cn.chawloo.base.startup

import android.content.Context
import androidx.startup.Initializer
import cn.chawloo.base.R
import com.drake.statelayout.StateConfig

class StateLayoutInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        StateConfig.apply {
            emptyLayout = R.layout.custom_error_view
            loadingLayout = R.layout.custom_loading_view
            errorLayout = R.layout.custom_error_view
            setRetryIds(R.id.btn_retry)
        }
    }

    override fun dependencies() = emptyList<Class<Initializer<*>>>()
}