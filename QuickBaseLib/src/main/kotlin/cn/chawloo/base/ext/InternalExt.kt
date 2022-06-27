package cn.chawloo.base.ext

import android.view.View
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import cn.chawloo.base.R

internal const val NO_GETTER: String = "Property does not have a getter"

internal fun noGetter(): Nothing = throw NotImplementedError(NO_GETTER)

internal var View.isAddedMarginTop: Boolean? by viewTags(R.id.tag_is_added_margin_top)
internal var View.isAddedPaddingTop: Boolean? by viewTags(R.id.tag_is_added_padding_top)
internal var View.isAddedMarginBottom: Boolean? by viewTags(R.id.tag_is_added_margin_bottom)
internal var View.lastClickTime: Long? by viewTags(R.id.tag_last_click_time)
internal var View.rootWindowInsetsCompatCache: WindowInsetsCompat? by viewTags(R.id.tag_root_window_insets)
internal var View.windowInsetsControllerCompatCache: WindowInsetsControllerCompat? by viewTags(R.id.tag_window_insets_controller)
