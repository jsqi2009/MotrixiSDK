package com.motrixi.datacollection.content;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.adobe.fre.FREContext;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class BufferedSharedPreferences {
    private Set<String> mRemoveBuffer;
    private SharedPreferences mSharedPreferences;
    private Map<String, Object> mWriteBuffer;

    public BufferedSharedPreferences(Context paramContext, String paramString) {
        if (!TextUtils.isEmpty(paramString)) {
            this.mWriteBuffer = new HashMap();
            this.mRemoveBuffer = new HashSet<>();
            this.mSharedPreferences = paramContext.getSharedPreferences(paramString, 0);
        }
    }

    public void apply() {
        Editor editor;
        try {
            editor = this.mSharedPreferences.edit();
            Iterator localIterator1 = this.mRemoveBuffer.iterator();
            while (localIterator1.hasNext()) {
                String str = (String) localIterator1.next();
                this.mWriteBuffer.remove(str);
                editor.remove(str);
            }
        } finally {
        }
        Iterator iterator = this.mWriteBuffer.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry localEntry = (Entry) iterator.next();
            Object value = localEntry.getValue();
            if ((value instanceof String))
                editor.putString((String) localEntry.getKey(), (String) value);
            else if ((value instanceof Integer))
                editor.putInt((String) localEntry.getKey(), ((Integer) value).intValue());
            else if ((value instanceof Long))
                editor.putLong((String) localEntry.getKey(), ((Long) value).longValue());
            else if ((value instanceof Float))
                editor.putFloat((String) localEntry.getKey(), ((Float) value).floatValue());
            else if ((value instanceof Boolean))
                editor.putBoolean((String) localEntry.getKey(), ((Boolean) value).booleanValue());
        }
        editor.apply();
        this.mWriteBuffer.clear();
        this.mRemoveBuffer.clear();
    }

    public void clear() {
        Editor localEditor = this.mSharedPreferences.edit();
        localEditor.clear();
        localEditor.apply();
        this.mWriteBuffer.clear();
    }

    public BufferedSharedPreferences clearBuffers() {
        BufferedSharedPreferences localBufferedSharedPreferences = clearWriteBuffer().clearRemoveBuffer();
        return localBufferedSharedPreferences;

    }

    public BufferedSharedPreferences clearRemoveBuffer() {
        this.mRemoveBuffer.clear();
        return this;

    }

    public BufferedSharedPreferences clearWriteBuffer() {

        this.mWriteBuffer.clear();
        return this;

    }

    public boolean getBoolean(String paramString, boolean paramBoolean) {
        boolean bool = this.mSharedPreferences.getBoolean(paramString, paramBoolean);
        return bool;

    }

    public float getFloat(String paramString, float paramFloat) {
        float f = this.mSharedPreferences.getFloat(paramString, paramFloat);
        return f;

    }

    public int getInt(String paramString, int paramInt) {
        int i = this.mSharedPreferences.getInt(paramString, paramInt);
        return i;

    }

    public long getLong(String paramString, long paramLong) {
        long l = this.mSharedPreferences.getLong(paramString, paramLong);
        return l;

    }

    public String getString(String paramString1, String paramString2) {
        String str = this.mSharedPreferences.getString(paramString1, paramString2);
        return str;

    }

    public BufferedSharedPreferences put(String paramString, float paramFloat) {
        this.mWriteBuffer.put(paramString, Float.valueOf(paramFloat));
        return this;

    }

    public BufferedSharedPreferences put(String paramString, int paramInt) {

        this.mWriteBuffer.put(paramString, Integer.valueOf(paramInt));
        return this;

    }

    public BufferedSharedPreferences put(String paramString, long paramLong) {

        this.mWriteBuffer.put(paramString, Long.valueOf(paramLong));
        return this;

    }

    public BufferedSharedPreferences put(String paramString1, String paramString2) {

        this.mWriteBuffer.put(paramString1, paramString2);
        return this;

    }

    public BufferedSharedPreferences put(String paramString, boolean paramBoolean) {

        this.mWriteBuffer.put(paramString, Boolean.valueOf(paramBoolean));
        return this;

    }

    public BufferedSharedPreferences remove(String paramString) {

        this.mRemoveBuffer.add(paramString);
        return this;

    }
}