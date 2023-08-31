package cn.chawloo.base.delegate

import cn.chawloo.base.ext.topActivity
import cn.chawloo.base.popup.showConfirmWindow
import cn.chawloo.base.update.entity.IUpdateBean
import cn.chawloo.base.update.view.UpdateAppPop
import cn.chawloo.base.utils.AppLauncher
import com.drake.net.utils.TipUtils.toast
import com.safframework.log.L

/**
 * TODO
 * @author Create by 鲁超 on 2023/6/30 16:08
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
object UpdateDelegate : IUpdate {
    private var mAppConfig: IUpdateBean? = null
    override fun update(appConfig: IUpdateBean, isShowToast: Boolean) {
        mAppConfig = appConfig
        L.e("代理处理更新")
        if (appConfig.needUpdate) {
            appConfig.url.takeIf { it.isNotBlank() }?.run {
                UpdateAppPop(topActivity, appConfig).show()
            } ?: showConfirmWindow(topActivity, title = "更新提示", content = appConfig.verInfo, leftStr = "取消", rightStr = "跳转应用市场") {
                AppLauncher.gotoMarket(topActivity)
            }
        } else {
            if (isShowToast) toast("当前为最新版本")
        }
    }
}

interface IUpdate {
    fun update(appConfig: IUpdateBean, isShowToast: Boolean = true)
}