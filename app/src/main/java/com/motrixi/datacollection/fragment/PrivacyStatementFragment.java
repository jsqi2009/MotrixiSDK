package com.motrixi.datacollection.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.motrixi.datacollection.DataCollectionActivity;
import com.motrixi.datacollection.content.Contants;
import com.motrixi.datacollection.content.Session;
import com.motrixi.datacollection.utils.CustomStyle;
import com.motrixi.datacollection.utils.DisplayUtil;

import static android.view.Gravity.CENTER;
import static android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrivacyStatementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrivacyStatementFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View rootView;
    private DataCollectionActivity parentActivity;
    private RelativeLayout privateLayout;
    private LinearLayout actionBarLayout;
    private Session mSession;
    private TextView tvTitle;
    private TextView tvContent1;
    private TextView tvCancel;
    private TextView tvOption;
    private TextView tvConfirm;

    public PrivacyStatementFragment() {
        // Required empty public constructor
    }

    public static PrivacyStatementFragment newInstance(String param1, String param2) {
        PrivacyStatementFragment fragment = new PrivacyStatementFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mSession = new Session(Contants.mFREContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_privacy_statement2, container, false);

        initLayout();
        rootView = privateLayout;

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //parentActivity = (DataCollectionActivity) Contants.mFREContext.getActivity();
        //mSession = new Session(Contants.mFREContext);

        customActionBarView();

        //Log.d("fragment", mSession.getConsentDataInfo().toString());
        setData();
    }

    private void initLayout() {
        privateLayout = new  RelativeLayout(Contants.mFREContext.getActivity());
        RelativeLayout.LayoutParams rootParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        RelativeLayout topLayout = new RelativeLayout(Contants.mFREContext.getActivity());
        RelativeLayout.LayoutParams topParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                DisplayUtil.dp2px(Contants.mFREContext, 50)
        );
        topLayout.setId(Contants.PRIVACY_TOP_ID);
        topLayout.setLayoutParams(topParams);
        topLayout.setBackgroundColor(Color.rgb(0, 150, 182));
        privateLayout.addView(topLayout);

        tvTitle = new TextView(Contants.mFREContext.getActivity());
        RelativeLayout.LayoutParams titleParams = new  RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        //tvTitle.text = "Consent"
        tvTitle.setTextSize(20F);
        tvTitle.setTextColor(Color.BLACK);
        tvTitle.setLayoutParams(titleParams);
        topLayout.addView(tvTitle);


        ScrollView scrollView= new ScrollView(Contants.mFREContext.getActivity());
        RelativeLayout.LayoutParams scrollParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        scrollParams.bottomMargin = DisplayUtil.dp2px(Contants.mFREContext, 70);
        scrollParams.addRule(RelativeLayout.BELOW, topLayout.getId());
        scrollView.setLayoutParams(scrollParams);
        privateLayout.addView(scrollView);

        LinearLayout contentLayout = new LinearLayout(Contants.mFREContext.getActivity());
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams contentParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        contentParams.setMargins(DisplayUtil.dp2px(Contants.mFREContext, 15),DisplayUtil.dp2px(Contants.mFREContext, 15),
                DisplayUtil.dp2px(Contants.mFREContext, 15),DisplayUtil.dp2px(Contants.mFREContext, 15));
        contentLayout.setLayoutParams(contentParams);
        scrollView.addView(contentLayout);

        tvContent1 = new TextView(Contants.mFREContext.getActivity());
        //tvContent1.text = style
        tvContent1.setTextSize(18F);
        tvContent1.setMovementMethod(LinkMovementMethod.getInstance());
        contentLayout.addView(tvContent1);

        LinearLayout bottomLayout = new LinearLayout(Contants.mFREContext.getActivity());
        bottomLayout.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams bottomParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                DisplayUtil.dp2px(Contants.mFREContext, 70)
        );
        bottomParams.addRule(ALIGN_PARENT_BOTTOM);
        bottomLayout.setLayoutParams(bottomParams);
        bottomLayout.setGravity(CENTER);

        tvCancel = new TextView(Contants.mFREContext.getActivity());
        tvCancel.setTextSize(18F);
        //tvCancel.text = "Cancel"
        tvCancel.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams cancelParams = new  LinearLayout.LayoutParams(
                0,
                DisplayUtil.dp2px(Contants.mFREContext, 50), 1F
        );
        cancelParams.leftMargin = DisplayUtil.dp2px(Contants.mFREContext, 20);
        cancelParams.rightMargin = DisplayUtil.dp2px(Contants.mFREContext, 20);
        tvCancel.setLayoutParams(cancelParams);
        tvCancel.setGravity(Gravity.CENTER);
        tvCancel.setBackground(CustomStyle.getGradientDrawable(Contants.mFREContext));

        tvOption = new TextView(Contants.mFREContext.getActivity());
        tvOption.setTextSize(18F);
        tvOption.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams confirmParams = new  LinearLayout.LayoutParams(
                0,
                DisplayUtil.dp2px(Contants.mFREContext, 50), 1F
        );
        tvOption.setLayoutParams(confirmParams);
        tvOption.setGravity(Gravity.CENTER);
        tvOption.setBackground(CustomStyle.getGradientDrawable(Contants.mFREContext));

        tvConfirm = new TextView(Contants.mFREContext.getActivity());
        tvConfirm.setTextSize(18F);
        //tvConfirm.text = "Confirm"
        tvConfirm.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams more = new  LinearLayout.LayoutParams(
                0,
                DisplayUtil.dp2px(Contants.mFREContext, 50), 1F
        );
        more.leftMargin = DisplayUtil.dp2px(Contants.mFREContext, 20);
        more.rightMargin = DisplayUtil.dp2px(Contants.mFREContext, 20);
        tvConfirm.setLayoutParams(more);
        tvConfirm.setGravity(Gravity.CENTER);
        tvConfirm.setBackground(CustomStyle.getGradientDrawable(Contants.mFREContext));

        bottomLayout.addView(tvCancel);
        bottomLayout.addView(tvOption);
        bottomLayout.addView(tvConfirm);

        privateLayout.setLayoutParams(rootParams);
        privateLayout.addView(bottomLayout);

        tvCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mSession.setAgreeFlag(true);
                //parentActivity.rejectCollect();
                getActivity().finish();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mSession.setAgreeFlag(true);
                String formValue = "";

                if (parentActivity.optionArray != null && parentActivity.optionArray.length > 0) {

                    for (int i = 0; i < parentActivity.optionArray.length; i++) {
                        if (i == parentActivity.optionArray.length -1) {
                            formValue = formValue + parentActivity.optionArray[i];
                        } else {
                            formValue = formValue + parentActivity.optionArray[i] + "|";
                        }
                    }

                    //parentActivity.submitConsentFormData(formValue);
                }

            }


        });
        tvOption.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack("option")
                        .replace(Contants.HOME_CONTAINER_ID, new OptionFragment())
                        .commit();
            }
        });

    }

    private void customActionBarView() {

        actionBarLayout = new  LinearLayout(Contants.mFREContext.getActivity());
        RelativeLayout.LayoutParams rootParams = new  RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        actionBarLayout.setLayoutParams(rootParams);
        actionBarLayout.setGravity(Gravity.CENTER);

        TextView tvTitle = new TextView(Contants.mFREContext.getActivity());
        tvTitle.setTextSize(22F);
//        tvTitle.text = "Consent"
        tvTitle.setText("test");
//        tvTitle.setText(parentActivity.info.value.terms_page_title);
        tvTitle.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams titleParams = new  LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        titleParams.rightMargin = DisplayUtil.dp2px(Contants.mFREContext, 50);
        tvTitle.setLayoutParams(titleParams);
        tvTitle.setGravity(Gravity.CENTER);

        actionBarLayout.addView(tvTitle);
    }

    private void setData() {

//        tvTitle.setText(parentActivity.info.value.terms_page_title);
//        tvCancel.setText(parentActivity.info.value.cancel_button_text);
//        tvOption.setText(parentActivity.info.value.option_button_text);
//        tvConfirm.setText(parentActivity.info.value.confirm_button_text);

        //tvContent1.setText(Html.fromHtml(parentActivity.info.value.terms_content));
        String content = "<p>By entering your email and clicking confirm, you consent to the collection of the use of your data to our trusted partners and us. Our trusted partners whom we share the information with may include storage, analytic providers, agencies, platforms, data providers, and research development. The purpose of sharing the data allows our third parties for the following (a) Data Customization: to custom data with demographics, behavioral, contextual or other information for personalized targeted advertisement (b) Measurement: measure key point indicators to evaluate marketing performance (c) Analytics: Identify and analyze behavioral data and patterns, and/or make more-informed business decisions and verify or disprove scientific models, theories and hypotheses (d) Modeling: To pinpoint key shared attributions for look alike audiences (e) Research and Development: allowing parties to process information to create and/or enhance the quality of products (f) Data Management Platform: to create better audiences to target specific users to increase performance When you confirm, you not only grant your consent, you acknowledge you are of 16 years of age and older. Please note, if you choose to click cancel, no information will be collected from you. To learn more about the terms in its entirety, please click <a href=\\\"https://www.motrixi.com/index.php/privacy-policy-2/\\\" target=\\\"_blank\\\" rel=\\\"noopener\\\">here</a>.</p>\\n<p>We thank you for installing our app and helping us improve the user experience by clicking 'Confirm'.</p>";
        tvContent1.setText(Html.fromHtml(content));
        //tvContent1.setText(Html.fromHtml(parentActivity.info.value.terms_content));
        //tvContent1.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence str = tvContent1.getText();
        if (str instanceof Spannable) {
            int end = str.length();
            Spannable sp = (android.text.Spannable) tvContent1.getText();
            URLSpan[] urls=sp.getSpans(0, end, URLSpan.class);  //find a tag of the text
            SpannableStringBuilder style=new SpannableStringBuilder(str);
            style.clearSpans(); //should clear old spans
            for(final URLSpan url : urls){

                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View view) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .addToBackStack("show_more")
                                .replace(Contants.HOME_CONTAINER_ID, ShowMoreFragment.newInstance(url.getURL()))
                                .commit();
                    }
                };
                //设置样式其中参数what是具体样式的实现对象，start则是该样式开始的位置，end对应的是样式结束的位置，
                // 参数 flags，定义在Spannable中的常量
                style.setSpan(clickableSpan ,sp.getSpanStart(url),sp.getSpanEnd(url),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            tvContent1.setText(style);
        }
    }
}