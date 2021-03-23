package com.motrixi.datacollection

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.*
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
import com.motrixi.datacollection.network.models.ConsentDetailInfo
import com.motrixi.datacollection.network.models.LanguageInfo
import com.motrixi.datacollection.utils.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
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
        Manifest.permission.FOREGROUND_SERVICE,
        Manifest.permission.GET_ACCOUNTS
    )
    private val request_code = 1
    var mPermissionList: ArrayList<String> = ArrayList()
    var mSession: Session? = null
    var actionBar: ActionBar? = null

    private var rootLayout: RelativeLayout? = null
    private var frameLayout: FrameLayout? = null
    private var onRequestListener: OnRequestPermissionListener? = null
    private var actionBarLayout: LinearLayout? = null
    var info: ConsentDetailInfo.ResultInfo? = null
    var optionArray: ArrayList<String> = ArrayList()
    var lanList: ArrayList<LanguageInfo> = ArrayList()
    var selectedOptionList: ArrayList<String> = ArrayList()
    private var mLocationManager: LocationManager? = null
    var checkedList: ArrayList<Int> = ArrayList()


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


        mSession = Session(this)
        getConsentDataList(this, "en", 0)
        //initView()

        checkGPSStatus()
    }

    override fun onResume() {
        super.onResume()
        //mSession!!.agreeFlag = true

        getLanguageList(this)

        uploadPresent(this)
    }

    private fun checkGPSStatus() {
        mLocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!mLocationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (Contants.onLogListener != null) {
                Contants.onLogListener!!.onLogListener(
                    MessageUtil.logMessage(Contants.SET_GPS, false, "The GPS is disabled, please set the GPS to enable")
                )
            }
        }
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
    private fun initView(tag: Int) {
        //mSession = Session(this)

        Contants.STATEMENT = ""
        checkedList.clear()
        info = mSession!!.consentDataInfo
        Log.e("option",info!!.value!!.options!!)
        if (info!!.value!!.options!!.contains("|")) {
            optionArray = info!!.value!!.options!!.replace("|", "=").split("=") as ArrayList<String>
            Log.d("option array", optionArray.size.toString())

            selectedOptionList = info!!.value!!.options!!.replace("|", "=").split("=") as ArrayList<String>

            fetchStatement()
        } else {
            optionArray.clear()
            optionArray.add(info!!.value!!.options!!)

            selectedOptionList.clear()
            selectedOptionList.add(info!!.value!!.options!!)

            checkedList.add(0)
        }



        // 获取碎片管理器
        val fm: FragmentManager = supportFragmentManager
//        fm.beginTransaction().add(R.id.home_container, PrivacyStatementFragment()).commit()

//        fm.beginTransaction().add(Contants.HOME_CONTAINER_ID, PrivacyStatementFragment()).commit()
        //fm.beginTransaction().add(Contants.HOME_CONTAINER_ID, PrivacyStatementFragment()).commit()

        /*if (!fm.beginTransaction().isEmpty) {
            fm.beginTransaction().replace(Contants.HOME_CONTAINER_ID, PrivacyStatementFragment()).commit()
        } else {
            fm.beginTransaction().add(Contants.HOME_CONTAINER_ID, PrivacyStatementFragment()).commit()
        }*/

        if (tag == 1) {
            fm.beginTransaction().replace(Contants.HOME_CONTAINER_ID, PrivacyStatementFragment()).commit()
        } else {
            fm.beginTransaction().add(Contants.HOME_CONTAINER_ID, PrivacyStatementFragment()).commit()
        }

    }

    private fun fetchStatement(){
        for (str in optionArray) {
            if (str.contains(Contants.SPECIAL_VALUE)) {
                Contants.STATEMENT = str
                break
            }
        }
        if (Contants.STATEMENT.isNotEmpty()) {
            optionArray.remove(Contants.STATEMENT)
            selectedOptionList.remove(Contants.STATEMENT)
        }

        for (index in optionArray.indices) {
            checkedList.add(index)
        }
        Log.e("checked list", checkedList.toString())
    }

    fun submitConsentFormData(value: String) {

        Log.e("form data", value)
        var call = HttpClient.submitConsentForm(this, value, mSession!!.appID, checkedList.toString())
        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: retrofit2.Call<JsonObject>, t: Throwable) {
                Log.d("submit status", "failure")

                UploadLogUtil.uploadLogData(this@DataCollectionActivity, t.message.toString())
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

    fun rejectCollect() {

        var appKey = Contants.APP_KEY
        var androidID = Settings.System.getString(this.contentResolver, Settings.Secure.ANDROID_ID)

        var call = HttpClient.rejectCollectionData(this, appKey, androidID, Contants.advertisingID)
        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: retrofit2.Call<JsonObject>, t: Throwable) {
                Log.d("cancel status", "network failure")

                UploadLogUtil.uploadLogData(this@DataCollectionActivity, t.message.toString())
            }

            override fun onResponse(call: retrofit2.Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseObject: JSONObject = JSONObject(response.body().toString())

                    UploadLogUtil.uploadLogData(this@DataCollectionActivity, "cancel:" + responseObject.optString("message"))
                    Log.d("cancel status", "success")
                    if (Contants.onLogListener != null) {
                        Contants.onLogListener!!.onLogListener(
                            MessageUtil.logMessage(Contants.CANCEL_COLLECT_DATA, true,
                                responseObject.optString("message")
                            )
                        )
                    }

                    //stopService(Contants.startService)
                } else {
                    var errorObject = JSONObject(response.errorBody()!!.string())

                    UploadLogUtil.uploadLogData(this@DataCollectionActivity, "cancel:" + errorObject.optString("message"))
                    Log.d("cancel status", "failure")
                    if (Contants.onLogListener != null) {
                        Contants.onLogListener!!.onLogListener(
                            MessageUtil.logMessage(Contants.CANCEL_COLLECT_DATA, true,
                                errorObject.optString("message")
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

    fun getOptionValue(): String {
        var formValue = ""
        for (index in optionArray.indices) {
            if (index == optionArray.size - 1) {
                formValue += optionArray[index]
            } else {
                formValue = formValue + optionArray[index] + "|"
            }
        }
        return formValue
    }

    fun getOptionSelectedValue(): String {
        var formValue = ""
        if (selectedOptionList.size == 0) {
            return formValue
        }
        for (index in selectedOptionList.indices) {
            if (index == selectedOptionList.size - 1) {
                formValue += selectedOptionList[index]
            } else {
                formValue = formValue + selectedOptionList[index] + "|"
            }
        }
        return formValue
    }

    fun getConsentDataList(context: Context, code: String, flag: Int) {

        var call = HttpClient.fetchConsentData(context, code)
        call.enqueue(object : Callback<ConsentDetailInfo> {
            override fun onFailure(call: retrofit2.Call<ConsentDetailInfo>, t: Throwable) {
                Log.d("fetch consent", "failure")

                UploadLogUtil.uploadLogData(context, t.message.toString())
            }

            override fun onResponse(call: retrofit2.Call<ConsentDetailInfo>, response: Response<ConsentDetailInfo>) {

                try {
                    if (response.isSuccessful) {
                        Log.d("result", response.body()!!.result.toString())
                        mSession!!.consentDataInfo = response.body()!!.result!!

                        if (Contants.onLogListener != null) {
                            Contants.onLogListener!!.onLogListener(MessageUtil.logMessage(Contants.FETCH_CONSENT_DATA, true, response.body()!!.result!!.toString()))
                        }

                        UploadLogUtil.uploadLogData(context, "get consent form data success ")

                        initView(flag)

                    } else {

                        UploadLogUtil.uploadLogData(context, "get consent form data failure ")
                        if (Contants.onLogListener != null) {
                            Contants.onLogListener!!.onLogListener(MessageUtil.logMessage(Contants.FETCH_CONSENT_DATA, false, response.body()!!.result!!.toString()))
                        }
                    }
                } catch (e: Exception) {
                    UploadLogUtil.uploadLogData(context, e.message.toString())
                }
            }
        })
    }

    private fun getLanguageList(context: Context) {

        lanList.clear()
        var call = HttpClient.getLanguageList(context)
        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: retrofit2.Call<JsonObject>, t: Throwable) {
                Log.d("fetch language", "failure")

                UploadLogUtil.uploadLogData(context, t.message.toString())
            }

            override fun onResponse(call: retrofit2.Call<JsonObject>, response: Response<JsonObject>) {

                try {
                    if (response.isSuccessful) {
                        Log.d("result", response.body().toString())
                        var responseObject:JSONObject = JSONObject(response.body().toString())
                        var resultArray: JSONArray = responseObject.optJSONArray("result")

                        for (i in 0 until resultArray.length()){
                            val item:JSONObject = resultArray.get(i) as JSONObject
                            val valueObj:JSONObject = item.optJSONObject("value")
                            val languageObj:JSONObject = valueObj.optJSONObject("select_language")

                            if (languageObj.has("code") && languageObj.has("language")) {
                                val code = languageObj.optString("code")
                                val language = languageObj.optString("language")
                                lanList.add(LanguageInfo(code, language))
                                Log.e("language info", "$code-$language")
                            }

                        }
                    } else {
                        UploadLogUtil.uploadLogData(context, "get consent list failure ")
                    }
                } catch (e: Exception) {
                    UploadLogUtil.uploadLogData(context, e.message.toString())
                }
            }
        })
    }

    private fun uploadPresent(context: Context) {

        val appKey = Contants.APP_KEY
        val androidID = Settings.System.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
        val country = LanguageUtil.getCountryCode(this)

        val call = HttpClient.uploadPresent(context, appKey, androidID, Contants.advertisingID, country)
        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: retrofit2.Call<JsonObject>, t: Throwable) {
                Log.d("upload present", "failure")
                UploadLogUtil.uploadLogData(context, t.message.toString())
            }

            override fun onResponse(call: retrofit2.Call<JsonObject>, response: Response<JsonObject>) {
                try {
                    if (response.isSuccessful) {
                        UploadLogUtil.uploadLogData(context, "upload present success ")
                    } else {
                        UploadLogUtil.uploadLogData(context, "upload present failure ")
                    }
                } catch (e: Exception) {
                    UploadLogUtil.uploadLogData(context, e.message.toString())
                }
            }
        })
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