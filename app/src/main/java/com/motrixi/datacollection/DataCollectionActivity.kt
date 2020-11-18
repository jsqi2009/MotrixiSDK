package com.motrixi.datacollection

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.gson.JsonObject
import com.motrixi.datacollection.content.Contants
import com.motrixi.datacollection.content.Session
import com.motrixi.datacollection.fragment.PrivacyStatementFragment
import com.motrixi.datacollection.listener.OnRequestPermissionListener
import com.motrixi.datacollection.network.HttpClient
import com.motrixi.datacollection.network.event.UploadDataResponseEvent
import com.motrixi.datacollection.utils.DisplayUtil
import com.motrixi.datacollection.utils.MessageUtil
import com.motrixi.datacollection.utils.UploadCollectedData
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class DataCollectionActivity : AppCompatActivity() {

    val OPTION_VALUE_1 = "Data Customization: to custom data with demographics, behavioral, contextual or other information for personalized targeted advertisement"
    val OPTION_VALUE_2 = "Measurement: measure key point indicators to evaluate marketin"
    val OPTION_VALUE_3 = "Analytics: Identify and analyze behavioral data and patterns, and/or make more-informed business decisions and verify or disprove scientific models, theories and hypotheses"
    val OPTION_VALUE_4 = "Modeling: To pinpoint key shared attributions or look alike audiences"
    val OPTION_VALUE_5 = "Research and Development: allowing parties to process information to create and/or enhance the quality of products"
    val OPTION_VALUE_6 = "Data Management Platform: to create better audiences to target specific users to increase performance"


    private lateinit var mFragmensts: Array<Fragment?>
    private val permissions = arrayOf<String>(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.FOREGROUND_SERVICE
    )
    private val request_code = 1
    var mPermissionList: ArrayList<String> = ArrayList()
    var mSession: Session? = null
    var actionBar: ActionBar? = null

    private var rootLayout: RelativeLayout? = null
    private var frameLayout: FrameLayout? = null
    private var onRequestListener: OnRequestPermissionListener? = null
    private var actionBarLayout: LinearLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_data_collection)



        initLayout()
        setContentView(rootLayout)

        actionBar = this.supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
//        customActionBarView()
//        actionBar!!.customView = actionBarLayout
//        actionBar!!.setDisplayShowCustomEnabled(true)



        //set statusBar color
        /*val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.statusBarColor = this.resources.getColor(R.color.app_color)
            window.statusBarColor = Color.rgb(0, 150, 182)  //#0096B6
        }*/


        initView()
    }

    override fun onResume() {
        super.onResume()
        mSession!!.agreeFlag = true
    }

    private fun customActionBarView() {

        actionBarLayout = LinearLayout(this)
        val rootParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        actionBarLayout!!.layoutParams = rootParams
        actionBarLayout!!.gravity = Gravity.CENTER

        val tvTitle = TextView(this)
        tvTitle.textSize = 22F
        tvTitle.text = "Consent"
        tvTitle.setTextColor(Color.WHITE)
        val titleParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        titleParams.rightMargin = DisplayUtil.dp2px(this, 50)
        tvTitle.layoutParams = titleParams
//        tvCancel.setBackgroundColor(Color.rgb(0, 150, 182))  //#0096B6
        tvTitle.gravity = Gravity.CENTER

        actionBarLayout!!.addView(tvTitle)
    }

    @SuppressLint("ResourceType")
    private fun initLayout() {

        rootLayout = RelativeLayout(this)
        val rootParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        rootLayout!!.layoutParams = rootParams


        frameLayout  =FrameLayout(this)
        val itemParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        frameLayout!!.id = Contants.HOME_CONTAINER_ID
        frameLayout!!.layoutParams = itemParams

        rootLayout!!.addView(frameLayout)

    }

    @SuppressLint("ResourceType")
    private fun initView() {
        mSession = Session(this)

        // 获取碎片管理器
        val fm: FragmentManager = supportFragmentManager
//        fm.beginTransaction().add(R.id.home_container, PrivacyStatementFragment()).commit()

        fm.beginTransaction().add(Contants.HOME_CONTAINER_ID, PrivacyStatementFragment()).commit()
    }

    fun submitConsentFormData(value: String) {

        var call = HttpClient.submitConsentForm(this, value, mSession!!.appID)
        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: retrofit2.Call<JsonObject>, t: Throwable) {
                Log.d("submit status", "failure")
            }

            override fun onResponse(call: retrofit2.Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseObject: JSONObject = JSONObject(response.body().toString())
                    val resultObject: JSONObject = responseObject.optJSONObject("result")
                    val consentFormID = resultObject.optString("id")
                    mSession!!.consentFormID = consentFormID

                    if (Contants.onLogListener != null) {
                        Contants.onLogListener!!.onLogListener(
                            MessageUtil.logMessage(
                                Contants.CONSENT_FORM_CODE,
                                true,
                                responseObject.optString("message")
                            )
                        )
                    }

                    initPermission()
                } else {
                    var error = JSONObject(response.errorBody()!!.string())
                    //Log.d("verify failure", error.optString("message"))
                    if (Contants.onLogListener != null) {
                        Contants.onLogListener!!.onLogListener(
                            MessageUtil.logMessage(
                                Contants.CONSENT_FORM_CODE,
                                false,
                                error.optString("message")
                            )
                        )
                    }
                }
            }
        })
    }

    fun initPermission() {
        //mSession!!.agreeFlag = true
        mSession!!.syncTime = Date().time


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission()
        }
    }

    private fun requestPermission() {

        mPermissionList.clear()
        for (requestPermission in permissions) {
            if (ContextCompat.checkSelfPermission(this, requestPermission) != PackageManager.PERMISSION_GRANTED){
                mPermissionList.add(requestPermission)
            }
        }

        if (mPermissionList.size > 0) {
            ActivityCompat.requestPermissions(this, permissions, request_code)
        } else {

            /*var service: Intent = Intent(this, UploadService::class.java)
            startService(service)*/
            //upload data
            UploadCollectedData.formatData(this)
            finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        var hasPermission = false
        if (request_code == requestCode) {
            for (grantResult in grantResults) {
                if (grantResult == -1) {
                    hasPermission = true
                }
            }
        }

        if (hasPermission) {
            if (onRequestListener != null) {
                onRequestListener!!.onRequestResult(true)
            }
        }

        UploadCollectedData.formatData(this)
        finish()

        /*if (hasPermission) {

        } else{
            //have all permission
            //upload data
        }*/
    }


    fun setOnRequestPermissionListener(listener: OnRequestPermissionListener) {
        this.onRequestListener = listener
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            this.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    fun getCheckedValue(): String {
        var formValue = ""
        if (mSession!!.option_1_flag) {
            formValue += OPTION_VALUE_1
        }
        if (mSession!!.option_2_flag) {
            if (TextUtils.isEmpty(formValue)) {
                formValue += OPTION_VALUE_2
            } else {
                formValue = formValue + "|" + OPTION_VALUE_2
            }
        }
        if (mSession!!.option_3_flag) {
            if (TextUtils.isEmpty(formValue)) {
                formValue += OPTION_VALUE_3
            } else {
                formValue = formValue + "|" + OPTION_VALUE_3
            }
        }
        if (mSession!!.option_4_flag) {
            if (TextUtils.isEmpty(formValue)) {
                formValue += OPTION_VALUE_4
            } else {
                formValue = formValue + "|" + OPTION_VALUE_4
            }
        }
        if (mSession!!.option_5_flag) {
            if (TextUtils.isEmpty(formValue)) {
                formValue += OPTION_VALUE_5
            } else {
                formValue = formValue + "|" + OPTION_VALUE_5
            }
        }
        if (mSession!!.option_6_flag) {
            if (TextUtils.isEmpty(formValue)) {
                formValue += OPTION_VALUE_6
            } else {
                formValue = formValue + "|" + OPTION_VALUE_6
            }
        }
        return formValue
    }


}