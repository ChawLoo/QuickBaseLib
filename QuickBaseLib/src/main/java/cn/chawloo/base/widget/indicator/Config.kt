package cn.chawloo.base.widget.indicator

import android.view.Gravity
import android.widget.LinearLayout
import androidx.annotation.AnimatorRes
import androidx.annotation.DrawableRes
import cn.chawloo.base.R

class Config private constructor() {
    var width = -1
    var height = -1
    var margin = -1

    @AnimatorRes
    var animatorResId: Int = R.animator.scale_with_alpha

    @AnimatorRes
    var animatorReverseResId = 0

    @DrawableRes
    var backgroundResId: Int = R.drawable.theme_color_radius

    @DrawableRes
    var unselectedBackgroundId = 0
    var orientation = LinearLayout.HORIZONTAL
    var gravity = Gravity.CENTER

    class Builder {
        private var width = -1
        private var height = -1
        private var margin = -1

        @AnimatorRes
        private var animatorResId: Int = R.animator.scale_with_alpha

        @AnimatorRes
        private var animatorReverseResId = 0

        @DrawableRes
        private var backgroundResId: Int = R.drawable.theme_color_radius

        @DrawableRes
        private var unselectedBackgroundId = 0
        private var orientation = LinearLayout.HORIZONTAL
        private var gravity = Gravity.CENTER

        fun width(width: Int): Builder {
            this.width = width
            return this
        }

        fun height(height: Int): Builder {
            this.height = height
            return this
        }

        fun margin(margin: Int): Builder {
            this.margin = margin
            return this
        }

        fun animator(@AnimatorRes animatorResId: Int): Builder {
            this.animatorResId = animatorResId
            return this
        }

        fun animatorReverse(@AnimatorRes animatorReverseResId: Int): Builder {
            this.animatorReverseResId = animatorReverseResId
            return this
        }

        fun drawable(@DrawableRes backgroundResId: Int): Builder {
            this.backgroundResId = backgroundResId
            return this
        }

        fun drawableUnselected(@DrawableRes unselectedBackgroundId: Int): Builder {
            this.unselectedBackgroundId = unselectedBackgroundId
            return this
        }

        fun orientation(orientation: Int): Builder {
            this.orientation = orientation
            return this
        }

        fun gravity(gravity: Int): Builder {
            this.gravity = gravity
            return this
        }

        fun build(): Config {
            return Config().apply {
                width = this@Builder.width
                height = this@Builder.height
                margin = this@Builder.margin
                animatorResId = this@Builder.animatorResId
                animatorReverseResId = this@Builder.animatorReverseResId
                backgroundResId = this@Builder.backgroundResId
                unselectedBackgroundId = this@Builder.unselectedBackgroundId
                orientation = this@Builder.orientation
                gravity = this@Builder.gravity
            }
        }
    }
}