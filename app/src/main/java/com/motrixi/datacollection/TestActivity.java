package com.motrixi.datacollection;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.motrixi.datacollection.listener.OnLogListener;
import com.motrixi.datacollection.utils.MotrixiSDK;

public class TestActivity extends FragmentActivity implements View.OnClickListener {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        init();
    }

    private void init() {

        MotrixiSDK.init(this, "7536f220-27b5-11eb-96a9-6d9cb391fb34");

        textView = findViewById(R.id.tv_reset);
        textView.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        MotrixiSDK.setOnLogListener(new OnLogListener() {
            @Override
            public void onLogListener(String info) {
                Log.e("listener log", info);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_reset:
                MotrixiSDK.resetConsentForm(this);
                break;
        }
    }
}