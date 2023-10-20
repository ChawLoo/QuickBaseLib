package cn.chawloo.basedemo

import cn.chawloo.base.base.BaseAct
import cn.chawloo.basedemo.databinding.ActivityMainBinding
import com.therouter.router.Route

@Route(path = "MainActivity", description = "首页")
class MainActivity : BaseAct<ActivityMainBinding>(R.layout.activity_main) {
    override fun isForcePortrait(): Boolean {
        return false
    }

    override fun initialize() {

    }
}