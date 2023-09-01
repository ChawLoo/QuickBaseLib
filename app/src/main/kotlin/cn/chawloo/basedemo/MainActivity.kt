package cn.chawloo.basedemo

import androidx.lifecycle.lifecycleScope
import cn.chawloo.base.base.BaseAct
import cn.chawloo.base.popup.showConfirmWindow
import cn.chawloo.base.utils.LoadingDialogHelper.hideLoading
import cn.chawloo.base.utils.LoadingDialogHelper.showLoading
import cn.chawloo.basedemo.databinding.ActivityMainBinding
import com.therouter.router.Route
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Route(path = "MainActivity", description = "首页")
class MainActivity : BaseAct<ActivityMainBinding>(R.layout.activity_main) {
    override fun initialize() {
        showConfirmWindow(this, content = "123123123123123") {

        }
    }
}