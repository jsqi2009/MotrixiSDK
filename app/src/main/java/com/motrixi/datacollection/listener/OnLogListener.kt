package com.motrixi.datacollection.listener

import com.motrixi.datacollection.network.models.LogInfo

/**
 * author : Jason
 *  date   : 2020/11/3 4:21 PM
 *  desc   :
 */
interface OnLogListener {

    fun onLogListener(info: String)

}