package cn.chawloo.base.base

import androidx.annotation.LayoutRes

/**
 * Fragment懒加载基类（不包含业务）
 * @author Create by 鲁超 on 2022/6/7 0007 14:58:43
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
abstract class BaseLazyFragment(@LayoutRes layoutId: Int) : BaseFragment(layoutId) {
    private var isLoaded = false
    override fun initialize() {
        onClick()
    }

    open fun onClick() {}

    override fun onResume() {
        super.onResume()
        if (!isLoaded && !isHidden) {
            lazyLoad()
            isLoaded = true
        }
    }

    abstract fun lazyLoad()

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
    }
}