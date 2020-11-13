package com.motrixi.datacollection.utils

import android.content.Context

/**
 * author : Jason
 *  date   : 2020/10/30 4:03 PM
 *  desc   :
 */
object DisplayUtil {

    /**
     * Convert px value to dip or dp value
     *
     * @param pxValue
     * @param scale
     * @return
     */
    fun px2dp(context: Context, pxValue: Float): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * Convert dip or dp value to px value
     *
     * @param dipValue
     * @param scale
     * @return
     */
    fun dp2px(context: Context, dipValue: Int): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * Convert px value to sp value
     *
     * @param pxValue
     * @param fontScale
     * @return
     */
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale: Float = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * Convert sp value to px value
     *
     * @param spValue
     * @param fontScale
     * @return
     */
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale: Float = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }
}