package com.motrixi.datacollection

import com.adobe.fre.FREContext
import com.adobe.fre.FREFunction

/**
 * author : Jason
 *  date   : 2020/12/4 1:34 PM
 *  desc   :
 */
class NativeContext : FREContext() {


    override fun getFunctions(): MutableMap<String, FREFunction> {

        val functions: MutableMap<String, FREFunction> = HashMap<String, FREFunction>()
        functions.put("init", MotrixiSDKFunction())

        return functions
    }

    override fun dispose() {

    }
}