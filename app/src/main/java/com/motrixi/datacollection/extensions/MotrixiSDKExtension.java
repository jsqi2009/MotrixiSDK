package com.motrixi.datacollection.extensions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREExtension;

/**
 * author : Jason
 * date   : 2020/12/8 6:41 PM
 * desc   :
 */
public class MotrixiSDKExtension implements FREExtension {
    @Override
    public void initialize() {

    }

    @Override
    public FREContext createContext(String s) {
        return new MotrixiSDKContext();
    }

    @Override
    public void dispose() {

    }
}
