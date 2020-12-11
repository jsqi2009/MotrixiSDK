package com.motrixi.datacollection.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.motrixi.datacollection.DataCollectionActivity;
import com.motrixi.datacollection.content.Contants;
import com.motrixi.datacollection.content.Session;
import com.motrixi.datacollection.utils.DisplayUtil;
import com.motrixi.datacollection.utils.NetworkUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowMoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowMoreFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String linkURL;

    private View rootView;
    private DataCollectionActivity parentActivity;

    private RelativeLayout moreLayout;
    private WebView webView;
    private LinearLayout actionBarLayout;
    private TextView tvTitle;

    public ShowMoreFragment() {
        // Required empty public constructor

        parentActivity = DataCollectionActivity.getSharedMainActivity();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ShowMoreFragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowMoreFragment newInstance(String param1) {
        ShowMoreFragment fragment = new ShowMoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            linkURL = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_show_more2, container, false);

        initLayout();

        rootView = moreLayout;
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //parentActivity = (DataCollectionActivity) getActivity();

        //tvTitle.setText(parentActivity.info.value.link_page_title);
        tvTitle.setText(parentActivity.mSession.getLinkTitle());

        customActionBarView();
        initView();
    }

    @SuppressLint("ResourceType")
    private void initLayout() {
        moreLayout = new  RelativeLayout(getActivity());
        RelativeLayout.LayoutParams rootParams = new  RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        moreLayout.setLayoutParams(rootParams);

        RelativeLayout topLayout = new RelativeLayout(getActivity());
        RelativeLayout.LayoutParams topParams = new  RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                DisplayUtil.dp2px(getActivity(), 50)
        );
        topLayout.setId(Contants.MORE_TOP_ID);
        topLayout.setLayoutParams(topParams);
        topLayout.setBackgroundColor(Color.rgb(0, 150, 182));
        moreLayout.addView(topLayout);

        tvTitle = new TextView(getActivity());
        RelativeLayout.LayoutParams titleParams = new  RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        //tvTitle.text = "Detailed Terms"
        tvTitle.setTextSize(20F);
        tvTitle.setTextColor(Color.BLACK);
        tvTitle.setLayoutParams(titleParams);
        topLayout.addView(tvTitle);

        webView = new  WebView(getActivity());
        RelativeLayout.LayoutParams webParams = new  RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        webParams.addRule(RelativeLayout.BELOW, topLayout.getId());
        webView.setLayoutParams(webParams);
        moreLayout.addView(webView);

    }

    private void initView() {

        initWebView();
        Log.e("link", linkURL);
        webView.loadUrl(linkURL);
        //webView.loadUrl("https://www.motrixi.com/index.php/privacy-policy-2/");

    }

    private void initWebView() {
        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        if (NetworkUtil.iConnected(parentActivity)) {
            Log.d("network status", "online");
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            Log.d("network status", "offline");
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        webView.setWebViewClient(webViewClient);
    }

    private void customActionBarView() {

        actionBarLayout = new  LinearLayout(getActivity());
        RelativeLayout.LayoutParams rootParams = new  RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        actionBarLayout.setLayoutParams(rootParams);
        actionBarLayout.setGravity(Gravity.CENTER);

        TextView tvTitle = new TextView(getActivity());
        tvTitle.setTextSize(22F);
        //tvTitle.setText(mSession.getConsentDataInfo().value.link_page_title);
        tvTitle.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams titleParams = new  LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        titleParams.rightMargin = DisplayUtil.dp2px(getActivity(), 50);
        tvTitle.setLayoutParams(titleParams);
        tvTitle.setGravity(Gravity.CENTER);

        actionBarLayout.addView(tvTitle);
    }

    private WebViewClient webViewClient = new WebViewClient(){
        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("ansen","url:"+url);

            return super.shouldOverrideUrlLoading(view, url);
        }

    };

}