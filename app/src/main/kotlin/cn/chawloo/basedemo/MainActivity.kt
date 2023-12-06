package cn.chawloo.basedemo

import androidx.lifecycle.lifecycleScope
import cn.chawloo.base.base.BaseAct
import cn.chawloo.base.ext.doClick
import cn.chawloo.base.popup.LoadingDialog
import cn.chawloo.basedemo.databinding.ActivityMainBinding
import com.therouter.router.Route
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Route(path = "MainActivity", description = "首页")
class MainActivity : BaseAct<ActivityMainBinding>() {
    override fun isForcePortrait(): Boolean {
        return false
    }

    override fun initialize() {
        binding.btnShowToast.doClick {
            val loading = LoadingDialog(this@MainActivity)
            lifecycleScope.launch {
                loading.show()
                delay(3000)
                loading.dismiss()
            }
        }
    }
}