package com.motrixi.datacollection;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.motrixi.datacollection.content.Contants;
import com.motrixi.datacollection.utils.DisplayUtil;
import com.motrixi.datacollection.utils.SystemInfoUtils;

public class TestActivity extends FragmentActivity {

    private RelativeLayout rootLayout;
    private TextView tvContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initLayout();
        setContentView(rootLayout);

        initView();
    }

    private void initView() {
        tvContent.setText(SystemInfoUtils.getDeviceAllInfo(Contants.mFREContext));
    }


    @SuppressLint("ResourceType")
    private void initLayout() {

        rootLayout = new  RelativeLayout(this);
        RelativeLayout.LayoutParams rootParams = new  RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        rootLayout.setLayoutParams(rootParams);

        ScrollView scrollView = new ScrollView(this);
        RelativeLayout.LayoutParams scrollParams = new  RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        scrollView.setLayoutParams(scrollParams);

        rootLayout.addView(scrollView);

        LinearLayout contentLayout = new  LinearLayout(this);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams contentParams = new  RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        contentParams.setMargins(30,30, 30, 30);
        contentLayout.setLayoutParams(contentParams);
        scrollView.addView(contentLayout);

        tvContent = new  TextView(this);
        RelativeLayout.LayoutParams textParams = new  RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        tvContent.setLayoutParams(textParams);
        tvContent.setPadding(0, 0, 0, 50);
        contentLayout.addView(tvContent);

    }


}