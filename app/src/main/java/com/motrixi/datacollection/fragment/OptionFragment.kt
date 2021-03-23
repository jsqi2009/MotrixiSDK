package com.motrixi.datacollection.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.motrixi.datacollection.DataCollectionActivity
import com.motrixi.datacollection.content.Contants
import com.motrixi.datacollection.content.Session
import com.motrixi.datacollection.service.MotrixiService
import com.motrixi.datacollection.utils.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OptionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OptionFragment : Fragment(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var rootView: View? = null
    private var parentActivity: DataCollectionActivity? = null
    private var mSession: Session? = null

    lateinit var optionLayout: RelativeLayout
    lateinit var checkBox1: CheckBox
    lateinit var checkBox2: CheckBox
    lateinit var checkBox3: CheckBox
    lateinit var checkBox4: CheckBox
    lateinit var checkBox5: CheckBox
    lateinit var checkBox6: CheckBox
    private var actionBarLayout: LinearLayout? = null
    lateinit var contentLayout: LinearLayout
    lateinit var tvBack: TextView
    lateinit var tvConfirm: TextView
    lateinit var tvTitle: TextView

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
        /*rootView = inflater.inflate(R.layout.fragment_option, container, false)
        ViewCompat.setElevation(rootView!!, 50f)*/

        initLayout()
        rootView = optionLayout
        return rootView
    }

    private fun initLayout() {
        optionLayout = RelativeLayout(activity)
        val rootParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        optionLayout.layoutParams = rootParams

        var topLayout = RelativeLayout(activity)
        val topParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            DisplayUtil.dp2px(activity!!, 50)
        )
        topLayout.id = Contants.OPTION_TOP_ID
        topLayout.layoutParams = topParams
//        topLayout.visibility = View.GONE
        topLayout.setBackgroundColor(Color.rgb(0, 150, 182))
        optionLayout.addView(topLayout)

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
//        tvTitle.text = "Options"
        tvTitle.textSize = 20F
        tvTitle.setTextColor(Color.BLACK)
        tvTitle.layoutParams = titleParams
        topLayout.addView(tvTitle)

        var scrollView: ScrollView = ScrollView(activity!!)
        val scrollParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        scrollParams.bottomMargin = DisplayUtil.dp2px(activity!!, 70)
        scrollParams.addRule(RelativeLayout.BELOW, topLayout.id)
        scrollView.layoutParams  =scrollParams
        optionLayout.addView(scrollView)

        contentLayout = LinearLayout(activity)
        contentLayout!!.orientation = LinearLayout.VERTICAL
        val contentParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        contentParams.setMargins(DisplayUtil.dp2px(activity!!, 15),DisplayUtil.dp2px(activity!!, 15),DisplayUtil.dp2px(activity!!, 15),DisplayUtil.dp2px(activity!!, 15))
        contentLayout!!.layoutParams = contentParams
        scrollView.addView(contentLayout)

        checkBox1 = CheckBox(activity)
        val checkBoxParams1 = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        checkBoxParams1.topMargin = DisplayUtil.dp2px(activity!!, 15)
        checkBox1.text = Contants.OPTION_VALUE_1
        checkBox1.setTextColor(Color.BLACK)
        checkBox1.textSize = 15F
        checkBox1.layoutParams = checkBoxParams1
        checkBox1.isClickable = false
        //contentLayout.addView(checkBox1)

        checkBox2 = CheckBox(activity)
        val checkBoxParams2 = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        checkBoxParams2.topMargin = DisplayUtil.dp2px(activity!!, 15)
        checkBox2.text = Contants.OPTION_VALUE_2
        checkBox2.setTextColor(Color.BLACK)
        checkBox2.textSize = 15F
        checkBox2.layoutParams = checkBoxParams2
        checkBox2.isClickable = false
        //contentLayout.addView(checkBox2)

        checkBox3 = CheckBox(activity)
        val checkBoxParams3 = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        checkBoxParams3.topMargin = DisplayUtil.dp2px(activity!!, 15)
        checkBox3.text = Contants.OPTION_VALUE_3
        checkBox3.setTextColor(Color.BLACK)
        checkBox3.textSize = 15F
        checkBox3.layoutParams = checkBoxParams3
        checkBox3.isClickable = false
        //contentLayout.addView(checkBox3)

        checkBox4 = CheckBox(activity)
        val checkBoxParams4 = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        checkBoxParams4.topMargin = DisplayUtil.dp2px(activity!!, 15)
        checkBox4.text = Contants.OPTION_VALUE_4
        checkBox4.setTextColor(Color.BLACK)
        checkBox4.textSize = 15F
        checkBox4.layoutParams = checkBoxParams4
        checkBox4.isClickable = false
        //contentLayout.addView(checkBox4)

        checkBox5 = CheckBox(activity)
        val checkBoxParams5 = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        checkBoxParams5.topMargin = DisplayUtil.dp2px(activity!!, 15)
        checkBox5.text = Contants.OPTION_VALUE_5
        checkBox5.setTextColor(Color.BLACK)
        checkBox5.textSize = 15F
        checkBox5.layoutParams = checkBoxParams5
        checkBox5.isClickable = false
        //contentLayout.addView(checkBox5)

        checkBox6 = CheckBox(activity)
        val checkBoxParams6 = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        checkBoxParams6.topMargin = DisplayUtil.dp2px(activity!!, 15)
        checkBox6.text = Contants.OPTION_VALUE_6
        checkBox6.setTextColor(Color.BLACK)
        checkBox6.textSize = 15F
        checkBox6.layoutParams = checkBoxParams6
        checkBox6.highlightColor = Color.rgb(0, 150, 182)
        checkBox6.isClickable = false
        //contentLayout.addView(checkBox6)

        var bottomLayout: LinearLayout = LinearLayout(activity)
        bottomLayout.orientation = LinearLayout.HORIZONTAL
        val bottomParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            DisplayUtil.dp2px(activity!!, 70)
        )
        bottomParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        bottomLayout.layoutParams = bottomParams
        bottomLayout.gravity = Gravity.CENTER

        tvBack = TextView(activity)
        tvBack.textSize = 18F
//        tvBack.text = "Back"
        tvBack.setTextColor(Color.BLACK)
//        tvBack.setTextColor(Color.rgb(0, 150, 182))  //#0096B6
        val backParams = LinearLayout.LayoutParams(
            0,
            DisplayUtil.dp2px(activity!!, 50), 1F
        )
        backParams.leftMargin = DisplayUtil.dp2px(activity!!, 40)
        backParams.rightMargin = DisplayUtil.dp2px(activity!!, 20)
        tvBack.layoutParams = backParams
//        tvBack.background = activity!!.resources.getDrawable(R.drawable.constant_button_bg)
        tvBack.background = CustomStyle.getGradientDrawable(activity!!)
        tvBack.gravity = Gravity.CENTER

        tvConfirm = TextView(activity)
        tvConfirm.textSize = 18F
//        tvConfirm.text = "Confirm"
        tvConfirm.setTextColor(Color.BLACK)
//        tvConfirm.setTextColor(Color.rgb(0, 150, 182))  //#0096B6
        val more = LinearLayout.LayoutParams(
            0,
            DisplayUtil.dp2px(activity!!, 50), 1F
        )
        more.leftMargin = DisplayUtil.dp2px(activity!!, 20)
        more.rightMargin = DisplayUtil.dp2px(activity!!, 40)
        tvConfirm.layoutParams = more
        tvConfirm.gravity = Gravity.CENTER
//        tvConfirm.background = activity!!.resources.getDrawable(R.drawable.constant_button_bg)
        tvConfirm.background = CustomStyle.getGradientDrawable(activity!!)

        bottomLayout.addView(tvBack)
        bottomLayout.addView(tvConfirm)
        optionLayout.addView(bottomLayout)


        ivBack.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                activity!!.onBackPressed()
            }
        })

        tvBack.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                activity!!.onBackPressed()
            }
        })

        tvConfirm.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                //parentActivity!!.initPermission()
                mSession!!.agreeFlag = true

                tvConfirm.isClickable = false
                tvConfirm.setTextColor(Color.GRAY)

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
                //var checkedValue = getCheckedValue()
                //var checkedValue = getOptionValue()
                var checkedValue = getOptionSelectedValue()
                parentActivity!!.submitConsentFormData(checkedValue)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        parentActivity = activity as DataCollectionActivity?
        mSession = Session(activity!!)
        initView()

        customActionBarView()
        //parentActivity!!.actionBar!!.customView = actionBarLayout
        //parentActivity!!.actionBar!!.setDisplayShowCustomEnabled(true)

        setData()
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
//        tvTitle.text = "Options"
        tvTitle.text = parentActivity!!.info!!.value!!.option_page_title
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

    private fun setData() {

        tvTitle.text = parentActivity!!.info!!.value!!.option_page_title
        tvBack.text = parentActivity!!.info!!.value!!.back_button_text
        tvConfirm.text = parentActivity!!.info!!.value!!.confirm_button_text

        contentLayout.removeAllViews()
        for (index in parentActivity!!.optionArray.indices) {
            addCheckBoxView(index)
        }

        val tvStatement = TextView(activity)
        val statementParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        statementParams.topMargin = DisplayUtil.dp2px(activity!!, 10)
        statementParams.bottomMargin = DisplayUtil.dp2px(activity!!, 30)
        tvStatement.layoutParams = statementParams
        tvStatement.textSize = 18F
        tvStatement.text = Contants.STATEMENT.replace(Contants.SPECIAL_VALUE, "")
        tvStatement.setTextColor(Color.GRAY)
        contentLayout.addView(tvStatement)

    }

    private fun addCheckBoxView(index: Int) {

        val checkBox: CheckBox = CheckBox(activity)
        val checkBoxParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        checkBoxParams.topMargin = DisplayUtil.dp2px(activity!!, 15)
        if (index == parentActivity!!.optionArray.size - 1) {
            checkBoxParams.bottomMargin = DisplayUtil.dp2px(activity!!, 25)
        }

        checkBox.text = parentActivity!!.optionArray[index]
        checkBox.setTextColor(Color.BLACK)
        checkBox.textSize = 15F
        checkBox.layoutParams = checkBoxParams
        checkBox.highlightColor = Color.rgb(0, 150, 182)
        for (position in parentActivity!!.selectedOptionList.indices) {
            if (parentActivity!!.selectedOptionList[position] == parentActivity!!.optionArray[index]) {
                checkBox.isChecked = true
                break
            }
        }
        checkBox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {

                val checkText = checkBox.text.toString()
                Log.e("text value", checkBox.text.toString())
                if (isChecked) {
                    Log.e("check status", isChecked.toString())
                    var flag1 = true
                    for (pos in parentActivity!!.selectedOptionList.indices) {
                        if (parentActivity!!.selectedOptionList[pos] == checkText) {
                            flag1 = false
                            break
                        }
                    }
                    if (flag1) {
                        parentActivity!!.selectedOptionList.add(checkText)
                    }

                    parentActivity!!.checkedList.add(index)
                } else {
                    Log.e("check status", isChecked.toString())
                    var flag2 = false
                    for (posi in parentActivity!!.selectedOptionList.indices) {
                        if (parentActivity!!.selectedOptionList[posi] == checkText) {
                            flag2 = true
                            break
                        }
                    }
                    if (flag2) {
                        parentActivity!!.selectedOptionList.remove(checkText)
                    }
                    parentActivity!!.checkedList.remove(index)
                }
                Log.e("selected size", parentActivity!!.selectedOptionList.size.toString())
            }
        })
        //checkBox.isClickable = false
        //checkBox.isChecked = true
        contentLayout.addView(checkBox)
    }

    private fun initView() {

        checkBox1.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, checked: Boolean) {
                mSession!!.option_1_flag = checked
                Log.d("option1 status", checked.toString())
            }
        })
        checkBox2.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, checked: Boolean) {
                mSession!!.option_2_flag = checked
                Log.d("option2 status", checked.toString())
            }
        })
        checkBox3.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, checked: Boolean) {
                mSession!!.option_3_flag = checked
                Log.d("option3 status", checked.toString())
            }
        })
        checkBox4.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, checked: Boolean) {
                mSession!!.option_4_flag = checked
                Log.d("option4 status", checked.toString())
            }
        })
        checkBox5.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, checked: Boolean) {
                mSession!!.option_5_flag = checked
                Log.d("option5 status", checked.toString())
            }
        })
        checkBox6.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, checked: Boolean) {
                mSession!!.option_6_flag = checked
                Log.d("option6 status", checked.toString())
            }
        })

        if (!mSession!!.viewOptionsFlag) {
            Log.d("view status", "true")
            mSession!!.viewOptionsFlag = true


            checkBox1.isChecked = true
            checkBox2.isChecked = true
            checkBox3.isChecked = true
            checkBox4.isChecked = true
            checkBox5.isChecked = true
            checkBox6.isChecked = true
        } else {
            checkBox1.isChecked = mSession!!.option_1_flag
            checkBox2.isChecked = mSession!!.option_2_flag
            checkBox3.isChecked = mSession!!.option_3_flag
            checkBox4.isChecked = mSession!!.option_4_flag
            checkBox5.isChecked = mSession!!.option_5_flag
            checkBox6.isChecked = mSession!!.option_6_flag
        }



        /*tv_back.setOnClickListener(this)
        iv_back.setOnClickListener(this)
        tv_confirm.setOnClickListener(this)
        cb_1.setOnCheckedChangeListener(this)
        cb_2.setOnCheckedChangeListener(this)
        cb_3.setOnCheckedChangeListener(this)
        cb_4.setOnCheckedChangeListener(this)
        cb_5.setOnCheckedChangeListener(this)
        cb_6.setOnCheckedChangeListener(this)

        if (!mSession!!.viewOptionsFlag) {
            Log.d("view status", "true")
            mSession!!.viewOptionsFlag = true

            cb_1.isChecked = true
            cb_2.isChecked = true
            cb_3.isChecked = true
            cb_4.isChecked = true
            cb_5.isChecked = true
            cb_6.isChecked = true
        } else {
            cb_1.isChecked = mSession!!.option_1_flag
            cb_2.isChecked = mSession!!.option_2_flag
            cb_3.isChecked = mSession!!.option_3_flag
            cb_4.isChecked = mSession!!.option_4_flag
            cb_5.isChecked = mSession!!.option_5_flag
            cb_6.isChecked = mSession!!.option_6_flag
        }*/

    }

    private fun getOptionValue(): String {
        var formValue = ""
        if (parentActivity!!.optionArray.size > 0) {

            for (index in parentActivity!!.optionArray.indices) {
                if (index == parentActivity!!.optionArray.size -1) {
                    formValue = formValue + parentActivity!!.optionArray[index]
                } else {
                    formValue = formValue + parentActivity!!.optionArray[index] + "|"
                }
            }
        }
        return formValue
    }

    private fun getOptionSelectedValue(): String {
        var formValue = ""
        if (parentActivity!!.selectedOptionList.size == 0) {
            return formValue
        }
        for (index in parentActivity!!.selectedOptionList.indices) {
            if (index == parentActivity!!.selectedOptionList.size - 1) {
                formValue += parentActivity!!.selectedOptionList[index]
            } else {
                formValue = formValue + parentActivity!!.selectedOptionList[index] + "|"
            }
        }
        return formValue
    }

    private fun getCheckedValue(): String {
        var formValue = ""
        if (mSession!!.option_1_flag) {
            formValue += parentActivity!!.OPTION_VALUE_1
        }
        if (mSession!!.option_2_flag) {
            if (TextUtils.isEmpty(formValue)) {
                formValue += parentActivity!!.OPTION_VALUE_2
            } else {
                formValue = formValue + "|" + parentActivity!!.OPTION_VALUE_2
            }
        }
        if (mSession!!.option_3_flag) {
            if (TextUtils.isEmpty(formValue)) {
                formValue += parentActivity!!.OPTION_VALUE_3
            } else {
                formValue = formValue + "|" + parentActivity!!.OPTION_VALUE_3
            }
        }
        if (mSession!!.option_4_flag) {
            if (TextUtils.isEmpty(formValue)) {
                formValue += parentActivity!!.OPTION_VALUE_4
            } else {
                formValue = formValue + "|" + parentActivity!!.OPTION_VALUE_4
            }
        }
        if (mSession!!.option_5_flag) {
            if (TextUtils.isEmpty(formValue)) {
                formValue += parentActivity!!.OPTION_VALUE_5
            } else {
                formValue = formValue + "|" + parentActivity!!.OPTION_VALUE_5
            }
        }
        if (mSession!!.option_6_flag) {
            if (TextUtils.isEmpty(formValue)) {
                formValue += parentActivity!!.OPTION_VALUE_6
            } else {
                formValue = formValue + "|" + parentActivity!!.OPTION_VALUE_6
            }
        }
        return formValue
    }

    override fun onCheckedChanged(cBox: CompoundButton?, checked: Boolean) {
        /*when (cBox!!.id) {
            R.id.cb_1 -> {
                mSession!!.option_1_flag = checked
                Log.d("option1 status", checked.toString())
            }
            R.id.cb_2 -> {
                mSession!!.option_2_flag = checked
                Log.d("option2 status", checked.toString())
            }
            R.id.cb_3 -> {
                mSession!!.option_3_flag = checked
                Log.d("option3 status", checked.toString())
            }
            R.id.cb_4 -> {
                mSession!!.option_4_flag = checked
                Log.d("option4 status", checked.toString())
            }
            R.id.cb_5 -> {
                mSession!!.option_5_flag = checked
                Log.d("option5 status", checked.toString())
            }
            R.id.cb_6 -> {
                mSession!!.option_6_flag = checked
                Log.d("option6 status", checked.toString())
            }
            else -> {
            }
        }*/

    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            /*R.id.iv_back -> {
                activity!!.onBackPressed()
            }
            R.id.tv_back -> {
                activity!!.onBackPressed()
            }
            R.id.tv_confirm -> {
                parentActivity!!.initPermission()

                //activity!!.finish()
            }
            else -> {
            }*/
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OptionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}