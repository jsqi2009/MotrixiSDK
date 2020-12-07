package com.motrixi.datacollection.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Gravity
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
import com.motrixi.datacollection.DataCollectionActivity
import com.motrixi.datacollection.content.Contants
import com.motrixi.datacollection.content.Session
import com.motrixi.datacollection.utils.DisplayUtil
import com.motrixi.datacollection.utils.NetworkUtil

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
    private var linkURL: String? = null

    private var rootView: View? = null
    private var parentActivity: DataCollectionActivity? = null

    lateinit var moreLayout: RelativeLayout
    lateinit var webView: WebView
    private var actionBarLayout: LinearLayout? = null
    private var mSession: Session? = null
    lateinit var tvTitle: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            linkURL = it.getString(ARG_PARAM1)
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
        topLayout.setBackgroundColor(Color.rgb(0, 150, 182))
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
        //topLayout.addView(ivBack)

        tvTitle = TextView(activity)
        val titleParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT)
        //tvTitle.text = "Detailed Terms"
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
        mSession = Session(Contants.mFREContext!!)

        tvTitle.text = parentActivity!!.info!!.value!!.link_page_title

        customActionBarView()
        //parentActivity!!.actionBar!!.customView = actionBarLayout
        //parentActivity!!.actionBar!!.setDisplayShowCustomEnabled(true)

        initView()
    }

    private fun initView() {

        initWebView()
        //webView.loadUrl(Contants.WEB_URL)
//        webView.loadUrl(parentActivity!!.info!!.value!!.terms_link!!)
        Log.e("link", linkURL!!)
        webView.loadUrl(linkURL!!)

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
//        tvTitle.text = "Detailed Terms"
        tvTitle.text = mSession!!.consentDataInfo.value!!.link_page_title
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
        fun newInstance(param1: String) =
            ShowMoreFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }


}