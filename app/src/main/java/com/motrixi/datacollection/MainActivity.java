package com.motrixi.datacollection;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.motrixi.datacollection.extensions.MotrixiSDKExtension;
import com.motrixi.datacollection.listener.OnLogListener;
import com.motrixi.datacollection.utils.MotrixiSDK;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        init();
    }

    private void init() {

       //MotrixiSDK.init(this, "ce13d5d0-1f2e-11eb-b44e-7132fcb9deec");

        textView = findViewById(R.id.tv_reset);
        textView.setOnClickListener(this);

        Intent intent = new Intent(this, MotrixiActivity.class);
        intent.putExtra("key", "86cf1530-88a2-11eb-b5b4-4dd2cbc4e7f1");
        startActivity(intent);

        //MotrixiSDK.init(this, "ce13d5d0-1f2e-11eb-b44e-7132fcb9deec");
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

        if (view.getId() == R.id.tv_reset) {
            MotrixiSDK.resetConsentForm(this);
        }
    }
}