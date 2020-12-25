package com.motrixi.datacollection.network.models

import java.io.Serializable

/**
 * @author jsqi
 * @time 2019/12/4 14:54
 */
class ConsentDetailInfo: Serializable {

    var success: Boolean? = null
    var code: Int? = null
    var message: String? = null
    var result: ResultInfo? = null

    class ResultInfo : Serializable {
        var id: Int? = null
        var key: String? = null
        var value: ListInfo?  = null
        var deleted_at: String? = null
        var created_at: String? = null
        var updated_at: String? = null
        var language: String? = null
        override fun toString(): String {
            return "ResultInfo(id=$id, key=$key, value=$value, deleted_at=$deleted_at, created_at=$created_at, updated_at=$updated_at, language=$language)"
        }
    }


    class ListInfo: Serializable{
        var terms_content: String? = null
        var options: String? = null
        var cancel_button_text: String? = null
        var confirm_button_text: String? = null
        var option_button_text: String? = null
        var back_button_text: String? = null
        var terms_page_title: String? = null
        var link_page_title: String? = null
        var option_page_title: String? = null
        var language_button_text: String? = null
        var terms_link: String? = null
        override fun toString(): String {
            return "ListInfo(terms_content=$terms_content, options=$options, cancel_button_text=$cancel_button_text, confirm_button_text=$confirm_button_text, option_button_text=$option_button_text, back_button_text=$back_button_text, terms_page_title=$terms_page_title, link_page_title=$link_page_title, option_page_title=$option_page_title, language_button_text=$language_button_text, terms_link=$terms_link)"
        }
    }

    override fun toString(): String {
        return "ConsentDataInfo(success=$success, code=$code, message=$message, result=$result)"
    }


}