package com.motrixi.datacollection.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.icu.util.Freezable;

import com.adobe.fre.FREContext;

/**
 * author : Jason
 * date   : 2020/12/8 10:11 AM
 * desc   :
 */
public class CustomStyle {

    /**
     * custom the style of the view
     */
    public static GradientDrawable getGradientDrawable(FREContext context){

        int strokeWidth = DisplayUtil.dp2px(context, 3); // 3dp width
        int roundRadius = DisplayUtil.dp2px(context, 15); // 15dp radius
        int strokeColor = Color.parseColor("#000000"); // stroke color
        int fillColor = Color.parseColor("#ffffff"); // fill color
        GradientDrawable gradientDrawable = new GradientDrawable(); //创建drawable
        gradientDrawable.setColor(fillColor);
        gradientDrawable.setCornerRadius((float)roundRadius);
        gradientDrawable.setStroke(strokeWidth, strokeColor);

        return gradientDrawable;
    }

    /**
     * custom the style of the view
     */
    public static GradientDrawable getGradientDrawable(Context context){

        int strokeWidth = DisplayUtil.dp2px(context, 3); // 3dp width
        int roundRadius = DisplayUtil.dp2px(context, 15); // 15dp radius
        int strokeColor = Color.parseColor("#000000"); // stroke color
        int fillColor = Color.parseColor("#ffffff"); // fill color
        GradientDrawable gradientDrawable = new GradientDrawable(); //创建drawable
        gradientDrawable.setColor(fillColor);
        gradientDrawable.setCornerRadius((float)roundRadius);
        gradientDrawable.setStroke(strokeWidth, strokeColor);

        return gradientDrawable;
    }
}
