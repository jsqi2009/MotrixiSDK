package com.motrixi.datacollection.extensions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREInvalidObjectException;
import com.adobe.fre.FREObject;
import com.adobe.fre.FRETypeMismatchException;
import com.adobe.fre.FREWrongThreadException;
import com.motrixi.datacollection.utils.MotrixiSDK;

/**
 * author : Jason
 * date   : 2020/12/8 6:34 PM
 * desc   :
 */
public class MotrixiSDKInit implements FREFunction {
    @Override
    public FREObject call(FREContext mContext, FREObject[] objectAry) {
        FREObject result = null;
        try {
            String msg = objectAry[0].getAsString();

            //MotrixiSDK.init(mContext, msg);

            // 返回值
            result = FREObject.newObject(msg);
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FREWrongThreadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FREInvalidObjectException e) {
            e.printStackTrace();
        } catch (FRETypeMismatchException e) {
            e.printStackTrace();
        }
        return result;
    }
}
