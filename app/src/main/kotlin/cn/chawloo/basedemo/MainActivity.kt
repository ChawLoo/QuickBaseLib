package cn.chawloo.basedemo

import cn.chawloo.base.base.BaseAct
import cn.chawloo.base.base.BaseActivity
import cn.chawloo.base.ext.doClick
import cn.chawloo.base.ext.longToast
import cn.chawloo.base.popup.showBottomListPopup
import cn.chawloo.base.popup.showConfirmWindow
import cn.chawloo.basedemo.databinding.ActivityMainBinding
import com.dylanc.viewbinding.binding
import com.therouter.router.Route

@Route(path = "MainActivity", description = "首页")
class MainActivity : BaseAct<ActivityMainBinding>(R.layout.activity_main) {
    override fun initialize() {
        vb.btnShowToast.doClick {
            val telNo = "010-12345678"
            val telNumber = telNo.replace("tel:", "").replace("/".toRegex(), "")
            var telList = telNumber.takeIf { it.isNotBlank() }?.split(",")
            telList = telList?.takeIf { it.size > 3 }?.subList(0, 3) ?: telList
            showBottomListPopup(this@MainActivity, telList?.takeIf { it.isNotEmpty() } ?: listOf("400-600-3067")) { }
        }
        vb.btnShowLongToast.doClick {
            showConfirmWindow(this@MainActivity, content = "不可取消模态框测试文案", cancelable = false, leftStr = "取消", cancel = {}, rightStr = "好的") {
                longToast("测试长气泡消息")
            }
        }
    }
}