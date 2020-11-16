package com.motrixi.datacollection.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable

/**
 * author : Jason
 *  date   : 2020/11/11 3:57 PM
 *  desc   :
 */
object CustomStyle {

    /**
     * custom the style of the view
     */
    fun getGradientDrawable(context: Context): GradientDrawable {

        val strokeWidth = DisplayUtil.dp2px(context, 3) // 3dp width
        val roundRadius = DisplayUtil.dp2px(context, 15) // 15dp radius
        val strokeColor = Color.parseColor("#000000") // stroke color
        val fillColor = Color.parseColor("#ffffff") // fill color
        val gradientDrawable = GradientDrawable() //创建drawable
        gradientDrawable.setColor(fillColor)
        gradientDrawable.cornerRadius = roundRadius.toFloat()
        gradientDrawable.setStroke(strokeWidth, strokeColor)

        return gradientDrawable
    }
}