package com.motrixi.datacollection.utils;

import android.content.Context;
import android.content.Intent;

import com.adobe.fre.FREContext;

/**
 * author : Jason
 * date   : 2020/12/8 10:03 AM
 * desc   :
 */
public class DisplayUtil {

    /**
     * Convert px value to dip or dp value
     *
     * @param pxValue
     * @return
     */
    public static int px2dp(FREContext context, Float pxValue){
        Float scale = context.getActivity().getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }

    /**
     * Convert dip or dp value to px value
     *
     * @param dipValue
     * @return
     */
    public static int dp2px(FREContext context, int dipValue) {
        Float scale = context.getActivity().getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }

    /**
     * Convert px value to sp value
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(FREContext context, Float pxValue){
        Float fontScale = context.getActivity().getResources().getDisplayMetrics().scaledDensity;
        return (int)(pxValue / fontScale + 0.5f);
    }

    /**
     * Convert sp value to px value
     *
     * @param spValue
     * @return
     */
    public static int sp2px(FREContext context, Float spValue){
        Float fontScale = context.getActivity().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
