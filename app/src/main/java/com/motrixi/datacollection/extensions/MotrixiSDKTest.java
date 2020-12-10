package com.motrixi.datacollection.extensions;

import android.content.Intent;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREInvalidObjectException;
import com.adobe.fre.FREObject;
import com.adobe.fre.FRETypeMismatchException;
import com.adobe.fre.FREWrongThreadException;
import com.motrixi.datacollection.TestActivity;

/**
 * author : Jason
 * date   : 2020/12/8 6:34 PM
 * desc   :
 */
public class MotrixiSDKTest implements FREFunction {
    @Override
    public FREObject call(FREContext mContext, FREObject[] objectAry) {
        FREObject result = null;
        try {
            String msg = String.valueOf(objectAry[0]);

            Intent intent = new Intent(mContext.getActivity(), TestActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.getActivity().startActivity(intent);

            // 返回值
            result = FREObject.newObject(msg);
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FREWrongThreadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
}