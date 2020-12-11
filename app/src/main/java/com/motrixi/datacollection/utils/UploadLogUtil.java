package com.motrixi.datacollection.utils;

import android.provider.Settings;

import com.adobe.fre.FREContext;
import com.motrixi.datacollection.content.Contants;

/**
 * author : Jason
 * date   : 2020/12/8 12:33 PM
 * desc   :
 */
public class UploadLogUtil {

    public static void uploadLogData(FREContext context, String value) {

        String val = MessageUtil.uploadLog(value);
        String appKey = Contants.APP_KEY;
        String androidID = Settings.System.getString(context.getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        /*Call<JsonObject> call = HttpClient.uploadLog(context, val, appKey, androidID);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }

        });*/
    }
}
