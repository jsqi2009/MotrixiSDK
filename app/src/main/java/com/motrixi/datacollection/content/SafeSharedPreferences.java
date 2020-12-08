package com.motrixi.datacollection.content;


import android.content.Context;

import com.adobe.fre.FREContext;

public final class SafeSharedPreferences {
    private BufferedSharedPreferences mBufferedSharedPreferences;

    public SafeSharedPreferences(Context paramContext, String paramString) {
        this.mBufferedSharedPreferences = new BufferedSharedPreferences(paramContext, paramString);
    }

    public void apply() {
        this.mBufferedSharedPreferences.apply();
    }

    public void clear() {
        this.mBufferedSharedPreferences.clear();
    }

    public SafeSharedPreferences clearBuffers() {
        this.mBufferedSharedPreferences.clearBuffers();
        return this;
    }

    public SafeSharedPreferences clearRemoveBuffer() {
        this.mBufferedSharedPreferences.clearRemoveBuffer();
        return this;
    }

    public SafeSharedPreferences clearWriteBuffer() {
        this.mBufferedSharedPreferences.clearWriteBuffer();
        return this;
    }

    public boolean getBoolean(String paramString, boolean paramBoolean) {
        return Boolean.valueOf(getString(paramString, Boolean.toString(paramBoolean))).booleanValue();
    }

    public double getDouble(String paramString, double paramDouble) {
        return Double.valueOf(getString(paramString, Double.toString(paramDouble))).doubleValue();
    }

    public float getFloat(String paramString, float paramFloat) {
        return Float.valueOf(getString(paramString, Float.toString(paramFloat))).floatValue();
    }

    public int getInt(String paramString, int paramInt) {
        return Integer.valueOf(getString(paramString, Integer.toString(paramInt))).intValue();
    }

    public long getLong(String paramString, long paramLong) {
        return Long.valueOf(getString(paramString, Long.toString(paramLong))).longValue();
    }

    public String getString(String paramString1, String paramString2) {
        return this.mBufferedSharedPreferences.getString(paramString1, paramString2);
    }

    public SafeSharedPreferences put(String paramString, double paramDouble) {
        this.mBufferedSharedPreferences.put(paramString, Double.toString(paramDouble));
        return this;
    }

    public SafeSharedPreferences put(String paramString, float paramFloat) {
        this.mBufferedSharedPreferences.put(paramString, Float.toString(paramFloat));
        return this;
    }

    public SafeSharedPreferences put(String paramString, int paramInt) {
        this.mBufferedSharedPreferences.put(paramString, Integer.toString(paramInt));
        return this;
    }

    public SafeSharedPreferences put(String paramString, long paramLong) {
        this.mBufferedSharedPreferences.put(paramString, Long.toString(paramLong));
        return this;
    }

    public SafeSharedPreferences put(String paramString1, String paramString2) {
        this.mBufferedSharedPreferences.put(paramString1, paramString2);
        return this;
    }

    public SafeSharedPreferences put(String paramString, boolean paramBoolean) {
        this.mBufferedSharedPreferences.put(paramString, Boolean.toString(paramBoolean));
        return this;
    }

    public SafeSharedPreferences remove(String paramString) {
        this.mBufferedSharedPreferences.remove(paramString);
        return this;
    }
}