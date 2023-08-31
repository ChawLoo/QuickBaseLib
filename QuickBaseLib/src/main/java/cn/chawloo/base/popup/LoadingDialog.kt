package cn.chawloo.base.popup

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.databinding.DataBindingUtil
import cn.chawloo.base.R
import cn.chawloo.base.databinding.PopLoadingBinding
import cn.chawloo.base.ext.if2Visible
import com.drake.net.utils.runMain

/**
 * TODO
 * @author Create by 鲁超 on 2023/5/23 16:23
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
class LoadingDialog @JvmOverloads constructor(
    context: Context,
    var title: String = context.getString(R.string.loading),
    @StyleRes themeResId: Int = R.style.LoadingDialog,
) : Dialog(context, themeResId) {
    private lateinit var vb: PopLoadingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = DataBindingUtil.inflate(layoutInflater, R.layout.pop_loading, null, true)
        setContentView(vb.root)
        vb.mLoadingTextView.text = title
        vb.mLoadingTextView.if2Visible(title.isNotBlank())
    }

    override fun show() {
        runMain {
            super.show()
        }
    }

    fun updateTitle(text: String) {
        if (isShowing) {
            runMain {
                vb.mLoadingTextView.text = text
            }
        } else {
            title = text
        }
    }
}