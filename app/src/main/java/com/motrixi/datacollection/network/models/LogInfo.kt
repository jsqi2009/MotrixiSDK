package com.motrixi.datacollection.network.models

import java.io.Serializable

/**
 * @author jsqi
 * @time 2019/12/4 14:54
 */
class LogInfo: Serializable {

    var type: Int? = null
    var success: Boolean? = null
    var info: String? = null
    override fun toString(): String {
        return "LogInfo(type=$type, success=$success, info=$info)"
    }


}