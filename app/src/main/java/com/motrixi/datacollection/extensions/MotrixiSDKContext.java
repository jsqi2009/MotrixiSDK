package com.motrixi.datacollection.extensions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;

import java.util.HashMap;
import java.util.Map;

/**
 * author : Jason
 * date   : 2020/12/8 6:38 PM
 * desc   :
 */
public class MotrixiSDKContext extends FREContext {

    @Override
    public Map<String, FREFunction> getFunctions() {
        Map<String, FREFunction> functions = new HashMap<String, FREFunction> ();
        functions.put("init", new MotrixiSDKInit());
        functions.put("reset", new MotrixiSDKReset());

        return functions;
    }

    @Override
    public void dispose() {

    }
}
