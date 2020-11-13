package com.motrixi.datacollection.utils

import java.util.*

/**
 * author : Jason
 *  date   : 2020/10/13 6:47 PM
 *  desc   :
 */
object LanguageUtil {

    fun getLanguageInfo(): String {

        val locale: Locale = Locale.getDefault()
        val lan = locale.language
        val country = locale.country
        return "$lan/$country"

    }
}