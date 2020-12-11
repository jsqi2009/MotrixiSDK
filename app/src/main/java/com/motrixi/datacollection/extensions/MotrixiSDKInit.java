package com.motrixi.datacollection.extensions;

import android.content.Intent;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREInvalidObjectException;
import com.adobe.fre.FREObject;
import com.adobe.fre.FRETypeMismatchException;
import com.adobe.fre.FREWrongThreadException;
import com.motrixi.datacollection.MotrixiActivity;
import com.motrixi.datacollection.content.Contants;
import com.motrixi.datacollection.utils.MotrixiSDK;

/**
 * author : Jason
 * date   : 2020/12/8 6:34 PM
 * desc   :
 */
public class MotrixiSDKInit implements FREFunction {

    public static MotrixiSDKContext sdkContext;

    @Override
    public FREObject call(FREContext mContext, FREObject[] objectAry) {
        FREObject result = null;
        try {
            String msg = objectAry[0].getAsString();

            //MotrixiSDK.init(mContext, msg);
            sdkContext = (MotrixiSDKContext) mContext;

            Contants.mFREContext = mContext;
            Intent intent = new Intent(mContext.getActivity(), MotrixiActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("key", msg);
            mContext.getActivity().startActivity(intent);

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
