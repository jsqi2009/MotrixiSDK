package com.motrixi.datacollection.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.motrixi.datacollection.DataCollectionActivity;
import com.motrixi.datacollection.content.Contants;
import com.motrixi.datacollection.content.Session;
import com.motrixi.datacollection.utils.CustomStyle;
import com.motrixi.datacollection.utils.DisplayUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OptionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;
    private DataCollectionActivity parentActivity;

    private RelativeLayout optionLayout;
    private LinearLayout actionBarLayout;
    private LinearLayout contentLayout;
    private TextView tvBack;
    private TextView tvConfirm;
    private TextView tvTitle;

    public OptionFragment() {
        // Required empty public constructor

        parentActivity = DataCollectionActivity.getSharedMainActivity();
    }

    // TODO: Rename and change types and number of parameters
    public static OptionFragment newInstance(String param1, String param2) {
        OptionFragment fragment = new OptionFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_option2, container, false);

        initLayout();
        rootView = optionLayout;
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //parentActivity = (DataCollectionActivity) getActivity();
        customActionBarView();
        setData();
    }

    private void initLayout() {
        optionLayout = new  RelativeLayout(getActivity());
        RelativeLayout.LayoutParams rootParams = new  RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        optionLayout.setLayoutParams(rootParams);

        RelativeLayout topLayout = new  RelativeLayout(getActivity());
        RelativeLayout.LayoutParams topParams = new  RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                DisplayUtil.dp2px(getActivity(), 50)
        );
        topLayout.setId(Contants.OPTION_TOP_ID);
        topLayout.setLayoutParams(topParams);
//        topLayout.visibility = View.GONE
        topLayout.setBackgroundColor(Color.rgb(0, 150, 182));
        optionLayout.addView(topLayout);

        tvTitle = new TextView(getActivity());
        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        tvTitle.setTextSize(20F);
        tvTitle.setTextColor(Color.BLACK);
        tvTitle.setLayoutParams(titleParams);
        topLayout.addView(tvTitle);

        ScrollView scrollView = new ScrollView(getActivity());
        RelativeLayout.LayoutParams scrollParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        scrollParams.bottomMargin = DisplayUtil.dp2px(getActivity(), 70);
        scrollParams.addRule(RelativeLayout.BELOW, topLayout.getId());
        scrollView.setLayoutParams(scrollParams);
        scrollView.setBackgroundColor(Color.rgb(255, 255, 255));
        optionLayout.addView(scrollView);

        contentLayout = new  LinearLayout(getActivity());
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams contentParams = new  RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        contentParams.setMargins(DisplayUtil.dp2px(getActivity(), 15),DisplayUtil.dp2px(getActivity(), 15),
        DisplayUtil.dp2px(getActivity(), 15),DisplayUtil.dp2px(getActivity(), 15));
        contentLayout.setLayoutParams(contentParams);
        scrollView.addView(contentLayout);

        LinearLayout bottomLayout = new LinearLayout(getActivity());
        bottomLayout.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams bottomParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                DisplayUtil.dp2px(getActivity(), 70)
        );
        bottomParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        bottomLayout.setLayoutParams(bottomParams);
        bottomLayout.setBackgroundColor(Color.rgb(255, 255, 255));
        bottomLayout.setGravity(Gravity.CENTER);

        tvBack = new TextView(getActivity());
        tvBack.setTextSize(18F);
        tvBack.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams backParams = new LinearLayout.LayoutParams(
                0,
                DisplayUtil.dp2px(getActivity(), 50), 1F
        );
        backParams.leftMargin = DisplayUtil.dp2px(getActivity(), 40);
        backParams.rightMargin = DisplayUtil.dp2px(getActivity(), 20);
        tvBack.setLayoutParams(backParams);
        tvBack.setBackground(CustomStyle.getGradientDrawable(getActivity()));
        tvBack.setGravity(Gravity.CENTER);

        tvConfirm = new TextView(getActivity());
        tvConfirm.setTextSize(18F);
        tvConfirm.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams more = new LinearLayout.LayoutParams(
                0,
                DisplayUtil.dp2px(getActivity(), 50), 1F
        );
        more.leftMargin = DisplayUtil.dp2px(getActivity(), 20);
        more.rightMargin = DisplayUtil.dp2px(getActivity(), 40);
        tvConfirm.setLayoutParams(more);
        tvConfirm.setGravity(Gravity.CENTER);
        tvConfirm.setBackground(CustomStyle.getGradientDrawable(getActivity()));

        bottomLayout.addView(tvBack);
        bottomLayout.addView(tvConfirm);
        optionLayout.addView(bottomLayout);

        tvBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Contants.agreeFlag = true;
                parentActivity.mSession.setAgreeFlag(true);
                parentActivity.submitFormData();

                if (Contants.mFREContext != null) {
                    getActivity().finish();
                }

                if (parentActivity.mSession.getPermissionFlag()) {
                    getActivity().finish();
                }
            }
        });
    }

    private void customActionBarView() {

        actionBarLayout = new LinearLayout(getActivity());
        RelativeLayout.LayoutParams rootParams = new  RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        actionBarLayout.setLayoutParams(rootParams);
        actionBarLayout.setGravity(Gravity.CENTER);

        TextView tvTitle = new  TextView(getActivity());
        tvTitle.setTextSize(22F);
//        tvTitle.text = "Options"
        //tvTitle.setText(Contants.option_page_title);
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

    private void setData() {

        tvTitle.setText(parentActivity.mSession.getOptionTitle());
        tvBack.setText(parentActivity.mSession.getBackButton());
        tvConfirm.setText(parentActivity.mSession.getConfirmButton());

        contentLayout.removeAllViews();
        for (int i = 0; i < parentActivity.optionArray.length; i++) {
            addCheckBoxView(i);
        }

    }

    private void addCheckBoxView(int index) {

        CheckBox checkBox = new CheckBox(getActivity());
        LinearLayout.LayoutParams checkBoxParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        checkBoxParams.topMargin = DisplayUtil.dp2px(getActivity(), 15);
        if (index == parentActivity.optionArray.length - 1) {
            checkBoxParams.bottomMargin = DisplayUtil.dp2px(getActivity(), 25);
        }

        checkBox.setText(parentActivity.optionArray[index]);
        checkBox.setTextColor(Color.BLACK);
        checkBox.setTextSize(15F);
        checkBox.setLayoutParams(checkBoxParams);
        checkBox.setHighlightColor(Color.rgb(0, 150, 182));
        checkBox.setClickable(false);
        checkBox.setChecked(true);
        contentLayout.addView(checkBox);
    }

    private String getOptionValue(){
        String formValue = "";
        if (DataCollectionActivity.optionArray.length > 0) {

            for (int i = 0; i < parentActivity.optionArray.length; i++) {
                if (i == parentActivity.optionArray.length -1) {
                    formValue = formValue + parentActivity.optionArray[i];
                } else {
                    formValue = formValue + parentActivity.optionArray[i] + "|";
                }
            }
        }
        return formValue;
    }
}