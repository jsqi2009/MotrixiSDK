package com.motrixi.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.motrixi.datacollection.utils.MotrixiSDK;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MotrixiSDK.INSTANCE.init(this, "6ebc1d80-7762-11eb-8446-d1713a3f35b9");
    }
}