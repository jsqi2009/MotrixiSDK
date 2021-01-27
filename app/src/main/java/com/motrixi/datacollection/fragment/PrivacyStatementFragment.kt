package com.motrixi.datacollection.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.URLSpan
import android.util.Log
import android.view.Gravity
import android.view.Gravity.CENTER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM
import android.widget.RelativeLayout.ALIGN_PARENT_RIGHT
import com.motrixi.datacollection.DataCollectionActivity
import com.motrixi.datacollection.R
import com.motrixi.datacollection.adapter.LanguageAdapter
import com.motrixi.datacollection.content.Contants
import com.motrixi.datacollection.content.Session
import com.motrixi.datacollection.network.models.LanguageInfo
import com.motrixi.datacollection.service.MotrixiService
import com.motrixi.datacollection.utils.CustomStyle
import com.motrixi.datacollection.utils.DisplayUtil


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PrivacyStatementFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PrivacyStatementFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var rootView: View? = null
    private var parentActivity: DataCollectionActivity? = null

    lateinit var privateLayout: RelativeLayout
    private var actionBarLayout: RelativeLayout? = null
    private var mSession: Session? = null
    lateinit var tvTitle: TextView
    lateinit var tvContent1: TextView
    lateinit var tvCancel: TextView
    lateinit var tvOption: TextView
    lateinit var tvConfirm: TextView

    lateinit var contentLayout: LinearLayout
    lateinit var bottomLayout: LinearLayout
    lateinit var tvLanguage: TextView
    lateinit var listView: ListView


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
//        rootView = inflater.inflate(R.layout.fragment_privacy_statement, container, false)
//        ViewCompat.setElevation(rootView!!, 50f)

        initLayout()
        rootView = privateLayout

        return rootView
    }

    @SuppressLint("ResourceType", "NewApi")
    private fun initLayout() {
        privateLayout = RelativeLayout(activity)
        val rootParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        var topLayout = RelativeLayout(activity)
        val topParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            DisplayUtil.dp2px(activity!!, 50)
        )
        topLayout.id = Contants.PRIVACY_TOP_ID
        topLayout.layoutParams = topParams
        topLayout.setBackgroundColor(Color.rgb(0, 150, 182))
        privateLayout.addView(topLayout)

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
        //tvTitle.text = "Consent"
        tvTitle.textSize = 20F
//        tvTitle.setTextColor(activity!!.resources.getColor(R.color.black))
        tvTitle.setTextColor(Color.BLACK)
        tvTitle.layoutParams = titleParams

        tvLanguage = TextView(activity)
        tvLanguage.textSize = 17F
        tvLanguage.text = "Language"
        tvLanguage.setTextColor(Color.BLACK)
        val languageParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        languageParams.addRule(ALIGN_PARENT_RIGHT)
        languageParams.rightMargin = DisplayUtil.dp2px(activity!!, 10)
        tvLanguage.layoutParams = languageParams
        tvLanguage.gravity = Gravity.CENTER

        topLayout.addView(tvTitle)
        topLayout.addView(tvLanguage)


        var scrollView: ScrollView = ScrollView(activity!!)
        val scrollParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        scrollParams.bottomMargin = DisplayUtil.dp2px(activity!!, 70)
        scrollParams.addRule(RelativeLayout.BELOW, topLayout.id)
        scrollView.layoutParams  =scrollParams
        privateLayout.addView(scrollView)

        contentLayout = LinearLayout(activity)
        contentLayout.orientation = LinearLayout.VERTICAL
        val contentParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        contentParams.setMargins(DisplayUtil.dp2px(activity!!, 15),DisplayUtil.dp2px(activity!!, 15),DisplayUtil.dp2px(activity!!, 15),DisplayUtil.dp2px(activity!!, 15))
        contentLayout.layoutParams = contentParams
        scrollView.addView(contentLayout)

        val style = SpannableStringBuilder()
        style.append(Contants.PRIVATE_STATEMENT_1)
        //设置部分文字点击事件
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                activity!!.supportFragmentManager.beginTransaction()
                    .addToBackStack("show_more")
                    .replace(Contants.HOME_CONTAINER_ID, ShowMoreFragment())
                    .commit()
            }
        }
        style.setSpan(clickableSpan, Contants.PRIVATE_STATEMENT_1.length - 5,
            Contants.PRIVATE_STATEMENT_1.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

//        val colorSpan = ForegroundColorSpan(Color.parseColor(R.color.app_color.toString()))
        val colorSpan = ForegroundColorSpan(Color.rgb(0, 150, 182))
        style.setSpan(colorSpan, Contants.PRIVATE_STATEMENT_1.length - 5,
            Contants.PRIVATE_STATEMENT_1.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)


        tvContent1 = TextView(activity)
        //tvContent1.text = style
        tvContent1.textSize = 18F
        tvContent1.movementMethod = LinkMovementMethod.getInstance()

        val tvContent2 = TextView(activity)
        tvContent2.textSize = 18F
        tvContent2.text = Contants.PRIVATE_STATEMENT_2

        contentLayout.addView(tvContent1)
        //contentLayout.addView(tvContent2)

        bottomLayout = LinearLayout(activity)
        bottomLayout.orientation = LinearLayout.HORIZONTAL
        val bottomParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            DisplayUtil.dp2px(activity!!, 70)
        )
        bottomParams.addRule(ALIGN_PARENT_BOTTOM)
        bottomLayout.layoutParams = bottomParams
        bottomLayout.gravity = CENTER

        tvCancel = TextView(activity)
        tvCancel.textSize = 18F
        //tvCancel.text = "Cancel"
        tvCancel.setTextColor(Color.BLACK)
//        tvCancel.setTextColor(Color.rgb(0, 150, 182))  //#0096B6
//        tvCancel.setTextColor(Color.WHITE)
        val cancelParams = LinearLayout.LayoutParams(
            0,
            DisplayUtil.dp2px(activity!!, 50), 1F
        )
        cancelParams.leftMargin = DisplayUtil.dp2px(activity!!, 20)
        cancelParams.rightMargin = DisplayUtil.dp2px(activity!!, 20)
        tvCancel.layoutParams = cancelParams
//        tvCancel.background = activity!!.resources.getDrawable(R.drawable.constant_button_bg)
//        tvCancel.setBackgroundColor(Color.rgb(0, 150, 182))  //#0096B6
        tvCancel.gravity = Gravity.CENTER
        tvCancel.background = CustomStyle.getGradientDrawable(activity!!)

        tvOption = TextView(activity)
        tvOption.textSize = 18F
        //tvOption.text = "Options"
        tvOption.setTextColor(Color.BLACK)
//        tvOption.setTextColor(Color.rgb(0, 150, 182))  //#0096B6
//        tvOption.setTextColor(Color.WHITE)
        val confirmParams = LinearLayout.LayoutParams(
            0,
            DisplayUtil.dp2px(activity!!, 50), 1F
        )
        tvOption.layoutParams = confirmParams
        tvOption.gravity = Gravity.CENTER
        tvOption.background = CustomStyle.getGradientDrawable(activity!!)
//        tvOption.background = activity!!.resources.getDrawable(R.drawable.constant_button_bg)
//        tvOption.setBackgroundColor(Color.rgb(0, 150, 182))  //#0096B6

        tvConfirm = TextView(activity)
        tvConfirm.textSize = 18F
        //tvConfirm.text = "Confirm"
        tvConfirm.setTextColor(Color.BLACK)
//        tvConfirm.setTextColor(Color.rgb(0, 150, 182))  //#0096B6
//        tvConfirm.setTextColor(Color.WHITE)
        val more = LinearLayout.LayoutParams(
            0,
            DisplayUtil.dp2px(activity!!, 50), 1F
        )
        more.leftMargin = DisplayUtil.dp2px(activity!!, 20)
        more.rightMargin = DisplayUtil.dp2px(activity!!, 20)
        tvConfirm.layoutParams = more
        tvConfirm.gravity = Gravity.CENTER
//        tvConfirm.background = activity!!.resources.getDrawable(R.drawable.constant_button_bg)
        tvConfirm.background = CustomStyle.getGradientDrawable(activity!!)
//        tvConfirm.setBackgroundColor(Color.rgb(0, 150, 182))  //#0096B6

        //set style
//        val strokeWidth = DisplayUtil.dp2px(activity!!, 3) // 3dp 边框宽度
//        val roundRadius = DisplayUtil.dp2px(activity!!, 15) // 8dp 圆角半径
//        val strokeColor = Color.parseColor("#2E3135") //边框颜色
//        val fillColor = Color.parseColor("#0096B6") //内部填充颜色
//        val gd = GradientDrawable() //创建drawable
//        gd.setColor(fillColor)
//        gd.cornerRadius = roundRadius.toFloat()
//        gd.setStroke(strokeWidth, strokeColor)
//        tvConfirm.background = gd

        bottomLayout.addView(tvCancel)
        bottomLayout.addView(tvOption)
        bottomLayout.addView(tvConfirm)

        privateLayout!!.layoutParams = rootParams
        privateLayout.addView(bottomLayout)

        listView = ListView(activity)
        val listParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        listParams.addRule(RelativeLayout.BELOW, topLayout.id)
        listView.layoutParams = listParams
        listView.visibility = View.GONE
        privateLayout.addView(listView)

        ivBack.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {

                activity!!.finish()
            }
        })

        tvCancel.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                mSession!!.agreeFlag = true
                Log.e("stop service", "stop service")
                var startService: Intent = Intent(activity, MotrixiService::class.java)
                activity!!.stopService(startService)
                mSession!!.isCollecting = false

                parentActivity!!.rejectCollect()
                activity!!.finish()
            }
        })
        tvConfirm.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                //parentActivity!!.initPermission()
                mSession!!.agreeFlag = true
                var formValue = ""
                if (!mSession!!.isCollecting) {
                    Log.e("start service", "start service")
                    var startService: Intent = Intent(activity, MotrixiService::class.java)
                    if (Build.VERSION.SDK_INT >= 26) {
                        activity!!.startForegroundService(startService)
                    } else {
                        activity!!.stopService(startService)
                    }

                }
                mSession!!.isCollecting = true


                if (parentActivity!!.optionArray.size > 0) {

                    for (index in parentActivity!!.selectedOptionList.indices) {
                        if (index == parentActivity!!.selectedOptionList.size -1) {
                            formValue = formValue + parentActivity!!.selectedOptionList[index]
                        } else {
                            formValue = formValue + parentActivity!!.selectedOptionList[index] + "|"
                        }
                    }

                    parentActivity!!.submitConsentFormData(formValue)
                }
            }
        })
        tvOption.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                activity!!.supportFragmentManager.beginTransaction()
                    .addToBackStack("option")
                    .replace(Contants.HOME_CONTAINER_ID, OptionFragment())
                    .commit()
            }
        })

        tvLanguage.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {

                if (parentActivity!!.lanList != null && parentActivity!!.lanList.size > 0) {
                    setListViewData()
                }
            }
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        parentActivity = activity as DataCollectionActivity?
        mSession = Session(activity!!)
        //initView()

        customActionBarView()
        //parentActivity!!.actionBar!!.customView = actionBarLayout
        //parentActivity!!.actionBar!!.setDisplayShowCustomEnabled(true)

        Log.d("fragment", mSession!!.consentDataInfo.toString())
        setData()

    }


    private fun customActionBarView() {

        actionBarLayout = RelativeLayout(activity)
        val rootParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        actionBarLayout!!.layoutParams = rootParams
        actionBarLayout!!.gravity = Gravity.CENTER

        val tvTitle = TextView(activity)
        tvTitle.textSize = 22F
//        tvTitle.text = "Consent"
        tvTitle.text = parentActivity!!.info!!.value!!.terms_page_title
        tvTitle.setTextColor(Color.WHITE)
        val titleParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        titleParams.rightMargin = DisplayUtil.dp2px(activity!!, 50)
        tvTitle.layoutParams = titleParams
//        tvCancel.setBackgroundColor(Color.rgb(0, 150, 182))  //#0096B6
        tvTitle.gravity = Gravity.CENTER

        val tvLanguage = TextView(activity)
        tvLanguage.textSize = 17F
        tvLanguage.text = ""
        tvLanguage.setTextColor(Color.BLACK)
        val languageParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        languageParams.addRule(ALIGN_PARENT_RIGHT)
        tvLanguage.layoutParams = languageParams
        tvLanguage.gravity = Gravity.CENTER

        actionBarLayout!!.addView(tvTitle)
        actionBarLayout!!.addView(tvLanguage)

        tvLanguage.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {

                if (parentActivity!!.lanList != null && parentActivity!!.lanList.size > 0) {
                    setListViewData()
                }
            }
        })
    }

    private fun setData() {

        tvTitle.text = parentActivity!!.info!!.value!!.terms_page_title
        tvCancel.text = parentActivity!!.info!!.value!!.cancel_button_text
        tvOption.text = parentActivity!!.info!!.value!!.option_button_text
        tvConfirm.text = parentActivity!!.info!!.value!!.confirm_button_text
        if (!TextUtils.isEmpty(parentActivity!!.info!!.value!!.language_button_text)) {
            tvLanguage.text = parentActivity!!.info!!.value!!.language_button_text
        } else {
            tvLanguage.text = "Language"
        }

        tvContent1.text = Html.fromHtml(parentActivity!!.info!!.value!!.terms_content)
        val str: CharSequence = tvContent1.text
        if (str is Spannable) {
            val end = str.length
            val sp = tvContent1.text as Spannable
            val urls = sp.getSpans(0, end, URLSpan::class.java) //find a tag of the text
            val style = SpannableStringBuilder(str)
            style.clearSpans() //should clear old spans
            for (url in urls) {
                //set click event
                val clickableSpan: ClickableSpan = object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        activity!!.supportFragmentManager.beginTransaction()
                            .addToBackStack("show_more")
                            .replace(Contants.HOME_CONTAINER_ID, ShowMoreFragment.newInstance(url.url.toString()))
                            .commit()
                    }
                }
                style.setSpan(clickableSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            tvContent1.text = style
        }
    }

    private fun setListViewData() {
        val lanList: ArrayList<LanguageInfo> = ArrayList()
        lanList.add(LanguageInfo("jan", "Japanese"))
        lanList.add(LanguageInfo("zh", "Chinese"))
        lanList.add(LanguageInfo("en", "English"))

        contentLayout.visibility = View.GONE
        bottomLayout.visibility = View.GONE
        listView.visibility = View.VISIBLE

        listView.adapter = LanguageAdapter(activity, 0, parentActivity!!.lanList)
        listView.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                var code = parentActivity!!.lanList[position].code.toString()
                Log.e("code", code)
                contentLayout.visibility = View.VISIBLE
                bottomLayout.visibility = View.VISIBLE
                listView.visibility = View.GONE

                parentActivity!!.getConsentDataList(activity!!, code, 1)
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun initView() {

        val style = SpannableStringBuilder()
        style.append(getString(R.string.fragment_private_statement))
        //设置部分文字点击事件
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                activity!!.supportFragmentManager.beginTransaction()
                    .addToBackStack("show_more")
                    .replace(Contants.HOME_CONTAINER_ID, ShowMoreFragment())
                    .commit()
            }
        }
        style.setSpan(clickableSpan, getString(R.string.fragment_private_statement).length - 5,
            getString(R.string.fragment_private_statement).length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

//        val colorSpan = ForegroundColorSpan(Color.parseColor(R.color.app_color.toString()))
        val colorSpan = ForegroundColorSpan(activity!!.resources.getColor(R.color.app_color))
        style.setSpan(colorSpan, getString(R.string.fragment_private_statement).length - 5,
            getString(R.string.fragment_private_statement).length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)

        /*tv_content.text = style
        tv_content.movementMethod = LinkMovementMethod.getInstance();


        tv_cancel.setOnClickListener(this)
        tv_confirm_all.setOnClickListener(this)
        tv_option.setOnClickListener(this)
        iv_back.setOnClickListener(this)*/
    }

    override fun onClick(view: View?) {
        /*when (view!!.id) {
            R.id.tv_cancel -> {
                activity!!.finish()
            }
            R.id.iv_back -> {
                activity!!.finish()
            }
            R.id.tv_confirm_all -> {

                parentActivity!!.initPermission()

                //activity!!.finish()

            }
            R.id.tv_option -> {
                activity!!.supportFragmentManager.beginTransaction()
                    .addToBackStack("option")
                    .replace(Contants.HOME_CONTAINER_ID, OptionFragment())
                    .commit()
            }
            else -> {
            }
        }*/
    }




    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PrivacyStatementFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}