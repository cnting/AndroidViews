package com.cnting.code4_wechat_floating_window

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

/**
 * Created by cnting on 2020/4/7
 *
 */
object UiUtil {
    fun dpToPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }
}