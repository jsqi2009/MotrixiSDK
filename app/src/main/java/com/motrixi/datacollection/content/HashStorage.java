package com.motrixi.datacollection.content;


import android.content.Context;

import com.adobe.fre.FREContext;

public class HashStorage {
    private SafeSharedPreferences mSafeSharedPreferences;

    public HashStorage(Context paramContext, String paramString) {
        this(new SafeSharedPreferences(paramContext, paramString));
    }

    HashStorage(SafeSharedPreferences paramSafeSharedPreferences) {
        this.mSafeSharedPreferences = paramSafeSharedPreferences;
    }

    public Buffer buffer() {
        return new Buffer();
    }

    public void clear() {
        this.mSafeSharedPreferences.clear();
    }

    public boolean getBoolean(String paramString) {
        return this.mSafeSharedPreferences.getBoolean(paramString, false);
    }

    public boolean getBoolean(String paramString, boolean paramBoolean) {
        return this.mSafeSharedPreferences.getBoolean(paramString, paramBoolean);
    }

    public double getDouble(String paramString) {
        return this.mSafeSharedPreferences.getDouble(paramString, 0.0D);
    }

    public double getDouble(String paramString, double paramDouble) {
        return this.mSafeSharedPreferences.getDouble(paramString, paramDouble);
    }

    public float getFloat(String paramString) {
        return this.mSafeSharedPreferences.getFloat(paramString, 0.0F);
    }

    public float getFloat(String paramString, float paramFloat) {
        return this.mSafeSharedPreferences.getFloat(paramString, paramFloat);
    }

    public int getInt(String paramString) {
        return this.mSafeSharedPreferences.getInt(paramString, 0);
    }

    public int getInt(String paramString, int paramInt) {
        return this.mSafeSharedPreferences.getInt(paramString, paramInt);
    }

    public long getLong(String paramString) {
        return this.mSafeSharedPreferences.getLong(paramString, 0L);
    }

    public long getLong(String paramString, long paramLong) {
        return this.mSafeSharedPreferences.getLong(paramString, paramLong);
    }

    public String getString(String paramString) {
        return this.mSafeSharedPreferences.getString(paramString, "");
    }

    public void put(String paramString, double paramDouble) {
        this.mSafeSharedPreferences.put(paramString, paramDouble);
        this.mSafeSharedPreferences.apply();
    }

    public void put(String paramString, float paramFloat) {
        this.mSafeSharedPreferences.put(paramString, paramFloat);
        this.mSafeSharedPreferences.apply();
    }

    public void put(String paramString, int paramInt) {
        this.mSafeSharedPreferences.put(paramString, paramInt);
        this.mSafeSharedPreferences.apply();
    }

    public void put(String paramString, long paramLong) {
        this.mSafeSharedPreferences.put(paramString, paramLong);
        this.mSafeSharedPreferences.apply();
    }

    public void put(String paramString1, String paramString2) {
        this.mSafeSharedPreferences.put(paramString1, paramString2);
        this.mSafeSharedPreferences.apply();
    }

    public void put(String paramString, boolean paramBoolean) {
        this.mSafeSharedPreferences.put(paramString, paramBoolean);
        this.mSafeSharedPreferences.apply();
    }

    public void remove(String paramString) {
        this.mSafeSharedPreferences.remove(paramString);
        this.mSafeSharedPreferences.apply();
    }

    public class Buffer {
        public Buffer() {
        }

        public void apply() {
            HashStorage.this.mSafeSharedPreferences.apply();
        }

        public void clear() {
            HashStorage.this.mSafeSharedPreferences.clearBuffers();
        }

        public Buffer put(String paramString, double paramDouble) {
            HashStorage.this.mSafeSharedPreferences.put(paramString, paramDouble);
            return this;
        }

        public Buffer put(String paramString, float paramFloat) {
            HashStorage.this.mSafeSharedPreferences.put(paramString, paramFloat);
            return this;
        }

        public Buffer put(String paramString, int paramInt) {
            HashStorage.this.mSafeSharedPreferences.put(paramString, paramInt);
            return this;
        }

        public Buffer put(String paramString, long paramLong) {
            HashStorage.this.mSafeSharedPreferences.put(paramString, paramLong);
            return this;
        }

        public Buffer put(String paramString1, String paramString2) {
            HashStorage.this.mSafeSharedPreferences.put(paramString1, paramString2);
            return this;
        }

        public Buffer put(String paramString, boolean paramBoolean) {
            HashStorage.this.mSafeSharedPreferences.put(paramString, paramBoolean);
            return this;
        }

        public Buffer remove(String paramString) {
            HashStorage.this.mSafeSharedPreferences.remove(paramString);
            return this;
        }
    }
}