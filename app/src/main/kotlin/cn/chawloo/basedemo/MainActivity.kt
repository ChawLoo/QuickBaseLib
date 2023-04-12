package cn.chawloo.basedemo

import cn.chawloo.base.base.BaseActivity
import cn.chawloo.base.ext.doClick
import cn.chawloo.base.ext.longToast
import cn.chawloo.base.ext.toast
import cn.chawloo.base.popup.showConfirmWindow
import cn.chawloo.basedemo.databinding.ActivityMainBinding
import com.dylanc.viewbinding.binding
import com.safframework.log.L

class MainActivity : BaseActivity() {
    private val vb by binding<ActivityMainBinding>()

    override fun initialize() {
        vb.btnShowToast.doClick {
            showConfirmWindow(this@MainActivity, content = "测试文案", leftStr = "取消", rightStr = "好的") {
                toast("点击了好的")
            }
        }
        vb.btnShowLongToast.doClick {
            showConfirmWindow(this@MainActivity, content = "不可取消模态框测试文案", cancelable = false, leftStr = "取消", cancel = {}, rightStr = "好的") {
                longToast("测试长气泡消息")
            }
        }
    }
}