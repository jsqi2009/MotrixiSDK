package com.motrixi.datacollection.network.models

import java.io.Serializable

/**
 * @author jsqi
 * @time 2019/12/4 14:54
 */
class LanguageInfo: Serializable {

    var code: String? = null
    var language: String? = null

    constructor(code: String?, language: String?) {
        this.code = code
        this.language = language
    }


    override fun toString(): String {
        return "LanguageInfo(code=$code, language=$language)"
    }


}