package com.motrixi.datacollection.network.models

import java.io.Serializable

/**
 * @author jsqi
 * @time 2019/12/4 14:54
 */
class ResponseInfo: Serializable {

    var app_key: String? = null
    override fun toString(): String {
        return "ResponseInfo(app_key=$app_key)"
    }


}