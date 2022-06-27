package cn.chawloo.basedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.chawloo.base.ext.toast
import cn.chawloo.base.popup.CommonConfirmPopupWindow

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CommonConfirmPopupWindow.Builder(this)
            .title("温馨提示")
            .content("测试文本内容")
            .cancel("取消") {
                toast("点击了取消")
            }
            .confirm("确定") {
                toast("点击了确定")
            }
            .build()
    }
}