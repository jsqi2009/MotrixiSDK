package com.motrixi.datacollection.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.motrixi.datacollection.DataCollectionActivity
import com.motrixi.datacollection.R
import com.motrixi.datacollection.content.Contants
import com.motrixi.datacollection.utils.DisplayUtil
import com.motrixi.datacollection.utils.NetworkUtil
import kotlinx.android.synthetic.main.fragment_show_more.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShowMoreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShowMoreFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var rootView: View? = null
    private var parentActivity: DataCollectionActivity? = null

    lateinit var moreLayout: RelativeLayout
    lateinit var webView: WebView
    private var actionBarLayout: LinearLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        /*rootView = inflater.inflate(R.layout.fragment_show_more, container, false)
        ViewCompat.setElevation(rootView!!, 50f)*/

        initLayout()

        rootView = moreLayout
        return rootView
    }

    @SuppressLint("ResourceType")
    private fun initLayout() {
        moreLayout = RelativeLayout(activity)
        val rootParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        moreLayout.layoutParams = rootParams

        var topLayout = RelativeLayout(activity)
        val topParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            DisplayUtil.dp2px(activity!!, 50)
        )
        topLayout.id = Contants.MORE_TOP_ID
        topLayout.layoutParams = topParams
        topLayout.visibility = View.GONE
        moreLayout.addView(topLayout)

        var ivBack = ImageView(activity)
        val imageParams = RelativeLayout.LayoutParams(
            DisplayUtil.dp2px(activity!!, 25),
            DisplayUtil.dp2px(activity!!, 25)
        )
        imageParams.leftMargin = DisplayUtil.dp2px(activity!!, 20)
        imageParams.addRule(RelativeLayout.CENTER_VERTICAL)
        //ivBack.setImageResource(R.drawable.ic_back)
        ivBack.layoutParams = imageParams
        topLayout.addView(ivBack)

        var tvTitle = TextView(activity)
        val titleParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT)
        tvTitle.text = "Detailed Terms"
        tvTitle.textSize = 20F
        tvTitle.setTextColor(Color.BLACK)
        tvTitle.layoutParams = titleParams
        topLayout.addView(tvTitle)

        webView = WebView(activity!!)
        val webParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        webParams.addRule(RelativeLayout.BELOW, topLayout.id)
        webView.layoutParams  = webParams
        moreLayout.addView(webView)

        ivBack.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                activity!!.onBackPressed()
            }
        })

        //initView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        parentActivity = activity as DataCollectionActivity?

        customActionBarView()
        parentActivity!!.actionBar!!.customView = actionBarLayout
        parentActivity!!.actionBar!!.setDisplayShowCustomEnabled(true)

        initView()
    }

    private fun initView() {

        initWebView()
        webView.loadUrl(Contants.WEB_URL)

        //iv_back.setOnClickListener(this)
    }

    private fun initWebView() {
        var webSettings = webView!!.settings

        webSettings.javaScriptEnabled = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.allowFileAccess = true
        webSettings.setSupportZoom(true)
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.databaseEnabled = true
        webSettings.domStorageEnabled = true
        if (NetworkUtil.iConnected(activity!!)) {
            Log.d("network status", "online")
            webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        } else {
            Log.d("network status", "offline")
            webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        }

        webSettings.domStorageEnabled = true
        webSettings.databaseEnabled = true
        webView!!.webViewClient = webClient
    }

    private fun customActionBarView() {

        actionBarLayout = LinearLayout(activity)
        val rootParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        actionBarLayout!!.layoutParams = rootParams
        actionBarLayout!!.gravity = Gravity.CENTER

        val tvTitle = TextView(activity)
        tvTitle.textSize = 22F
        tvTitle.text = "Detailed Terms"
        tvTitle.setTextColor(Color.WHITE)
        val titleParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        titleParams.rightMargin = DisplayUtil.dp2px(activity!!, 50)
        tvTitle.layoutParams = titleParams
//        tvCancel.setBackgroundColor(Color.rgb(0, 150, 182))  //#0096B6
        tvTitle.gravity = Gravity.CENTER

        actionBarLayout!!.addView(tvTitle)
    }

    private val webClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            Log.d(TAG, url!!)
            return false
        }
    }

    override fun onClick(view: View?) {
        /*when (view!!.id) {
            R.id.iv_back -> {
                activity!!.onBackPressed()
            }
            else -> {
            }
        }*/
    }

    companion object {

        val TAG=ShowMoreFragment::class.simpleName  //定义Log的TAG

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShowMoreFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}