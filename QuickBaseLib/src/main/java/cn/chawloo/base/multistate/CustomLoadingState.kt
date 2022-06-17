package cn.chawloo.base.multistate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import cn.chawloo.base.R
import cn.chawloo.base.ext.gone
import cn.chawloo.base.ext.visible
import com.zy.multistatepage.MultiState
import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.MultiStatePage

/**
 * 自定义加载中缺省页
 * @author Create by 鲁超 on 2021/8/24 0024 16:25:20
 *----------Dragon be here!----------/
 *       ┌─┐      ┌─┐
 *     ┌─┘─┴──────┘─┴─┐
 *     │              │
 *     │      ─       │z
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
class CustomLoadingState : MultiState() {

    private lateinit var tvLoadingMsg: TextView
    override fun onCreateMultiStateView(context: Context, inflater: LayoutInflater, container: MultiStateContainer): View {
        return inflater.inflate(R.layout.custom_loading_view, container, false)
    }

    override fun onMultiStateViewCreate(view: View) {
        tvLoadingMsg = view.findViewById(R.id.tv_loading_msg)
        setLoadingMsg(MultiStatePage.config.loadingMsg)
    }

    fun setLoadingMsg(msg: String) {
        if (msg.isNotBlank()) {
            tvLoadingMsg.text = msg
            tvLoadingMsg.visible()
        } else {
            tvLoadingMsg.gone()
        }
    }
}