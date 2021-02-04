package com.motrixi.datacollection.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Picture
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebView.GONE
import android.webkit.WebView.PictureListener
import android.webkit.WebViewClient
import android.widget.*
import androidx.fragment.app.Fragment
import com.motrixi.datacollection.DataCollectionActivity
import com.motrixi.datacollection.content.Contants
import com.motrixi.datacollection.content.Session
import com.motrixi.datacollection.utils.DisplayUtil
import com.motrixi.datacollection.utils.NetworkUtil
import java.util.function.LongFunction


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
    lateinit var progressBar: ProgressBar


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


        /*var progressBarLayout = RelativeLayout(activity)
        val barLayoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        barLayoutParams.addRule(RelativeLayout.BELOW, topLayout.id)
        progressBarLayout.layoutParams = barLayoutParams
        moreLayout.addView(progressBarLayout)*/

        progressBar = ProgressBar(activity!!)
        val barParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        barParams.addRule(RelativeLayout.CENTER_IN_PARENT)
        progressBar.layoutParams = barParams
        progressBar.isIndeterminate  = true
        moreLayout.addView(progressBar)


        webView = WebView(activity!!)
        val webParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
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
        mSession = Session(activity!!)

        customActionBarView()
        parentActivity!!.actionBar!!.customView = actionBarLayout
        parentActivity!!.actionBar!!.setDisplayShowCustomEnabled(true)

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
        //webSettings.cacheMode = WebSettings.LOAD_NO_CACHE

        webSettings.domStorageEnabled = true
        webSettings.databaseEnabled = true
        //webView.webViewClient = webClient

        webView.setPictureListener(pictureListener)

        webView.webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.e("finish", "onPageFinished")
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.e("start load", "onPageStarted")
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return super.shouldOverrideUrlLoading(view, url)
            }


        }

        webView.webChromeClient = object : WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100) {
                    Log.e("load done", "Finished")
                }
            }
        }
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


    private val pictureListener : PictureListener = object : PictureListener {
        override fun onNewPicture(p0: WebView?, p1: Picture?) {
            Log.e("picture", "listener")
            if (progressBar != null) {
                if (progressBar.isShown) {
                    progressBar.visibility = View.GONE
                }
            }
        }

    }

    private val webClient = object : WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            Log.e("start load", "onPageStarted")

        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            Log.e("finish", "onPageFinished")
        }

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