package cn.chawloo.basedemo

import cn.chawloo.base.base.BaseActivity
import cn.chawloo.basedemo.databinding.ActivityMainBinding
import com.dylanc.viewbinding.binding
import com.safframework.log.L

class MainActivity : BaseActivity() {
    private val vb by binding<ActivityMainBinding>()

    override fun initialize() {
        vb.root
    }

    override fun backPressed() {
        println("返回键回调了")
    }
}