package com.motrixi.datacollection.feature

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.*
import com.motrixi.datacollection.R
import com.motrixi.datacollection.content.Contants
import com.motrixi.datacollection.utils.DisplayUtil
import com.motrixi.datacollection.utils.SystemInfoUtils
import kotlinx.android.synthetic.main.activity_main.*


class SystemInfoActivity : Activity() {


    private var rootLayout: RelativeLayout? = null
    private var tvContent: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initLayout()
        setContentView(rootLayout)

        //setContentView(R.layout.activity_system_info)

        initView()
    }

    private fun initView() {
        //tv_system_info.text = SystemInfoUtils.getDeviceAllInfo(this)
        tvContent!!.text = SystemInfoUtils.getDeviceAllInfo(this)
    }


    @SuppressLint("ResourceType")
    private fun initLayout() {

        rootLayout = RelativeLayout(this)
        val rootParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        rootLayout!!.layoutParams = rootParams

        var scrollView: ScrollView = ScrollView(this!!)
        val scrollParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        scrollParams.bottomMargin = DisplayUtil.dp2px(this!!, 70)
        scrollView.layoutParams  =scrollParams
        rootLayout!!.addView(scrollView)

        var contentLayout: LinearLayout = LinearLayout(this)
        contentLayout.orientation = LinearLayout.VERTICAL
        val contentParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        contentParams.setMargins(DisplayUtil.dp2px(this!!, 15),DisplayUtil.dp2px(this!!, 15),DisplayUtil.dp2px(this!!, 15),DisplayUtil.dp2px(this!!, 15))
        contentLayout.layoutParams = contentParams
        scrollView.addView(contentLayout)

        tvContent = TextView(this!!)
        val webParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        tvContent!!.layoutParams  = webParams
        contentLayout.addView(tvContent)

    }
}