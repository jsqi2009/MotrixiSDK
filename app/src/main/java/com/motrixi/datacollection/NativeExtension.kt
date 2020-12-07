package com.motrixi.datacollection

import com.adobe.fre.FREContext
import com.adobe.fre.FREExtension

/**
 * author : Jason
 *  date   : 2020/12/4 1:31 PM
 *  desc   :
 */
class NativeExtension : FREExtension {
    override fun createContext(p0: String?): FREContext {

        return NativeContext()
    }

    override fun initialize() {

    }

    override fun dispose() {

    }
}