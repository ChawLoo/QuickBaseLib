package cn.chawloo.base.widget.tablayout.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * 片段切换管理类
 *
 * @author Create by 鲁超 on 2020/4/25 0025 19:32
 */
class FragmentChangeManager(fm: FragmentManager, containerViewId: Int, fragments: List<Fragment>) {
    private var mFragmentManager: FragmentManager = fm
    private var mContainerViewId = containerViewId

    /**
     * Fragment切换数组
     */
    private var mFragments: List<Fragment> = fragments

    /**
     * 当前选中的Tab
     */
    private var currentTab = 0

    init {
        initFragments()
    }

    /**
     * 初始化fragments
     */
    private fun initFragments() {
        for (fragment in mFragments) {
            mFragmentManager.beginTransaction().add(mContainerViewId, fragment).hide(fragment).commit()
        }
        setFragments(0)
    }

    /**
     * 界面切换控制
     */
    fun setFragments(index: Int) {
        for (i in mFragments.indices) {
            val ft = mFragmentManager.beginTransaction()
            val fragment = mFragments[i]
            if (i == index) {
                ft.show(fragment)
            } else {
                ft.hide(fragment)
            }
            ft.commit()
        }
        currentTab = index
    }

    val currentFragment: Fragment get() = mFragments[currentTab]
}