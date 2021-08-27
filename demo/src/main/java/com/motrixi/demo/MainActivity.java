package com.motrixi.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.motrixi.datacollection.utils.MotrixiSDK;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MotrixiSDK.INSTANCE.init(this, "ee1a6dc0-889c-11eb-b03a-9379ae93e248");
    }
}