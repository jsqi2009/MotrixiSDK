package com.motrixi.datacollection

import android.accounts.Account
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Address
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.FragmentActivity
import android.telephony.TelephonyManager
import android.util.Log
import android.util.Patterns
import android.view.View
import com.motrixi.datacollection.feature.SystemInfoActivity
import com.motrixi.datacollection.listener.OnLogListener
import com.motrixi.datacollection.network.ManifestMetaReader
import com.motrixi.datacollection.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors
import java.util.regex.Pattern


class MainActivity : FragmentActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        initView()
    }

    private fun initView() {

//初始化
        MotrixiSDK.init(this, "6ebc1d80-7762-11eb-8446-d1713a3f35b9")

        //MotrixiSDK.init(上下文对象, "appKey")


        //CrashReport.testJavaCrash();

        tv_get_imei.setOnClickListener(this)
        tv_get_install_package.setOnClickListener(this)
        tv_get_language.setOnClickListener(this)
        tv_system_info.setOnClickListener(this)
        tv_get_IP.setOnClickListener(this)
        tv_sim_info.setOnClickListener(this)
        tv_user_agent.setOnClickListener(this)
        tv_get_location.setOnClickListener(this)
        tv_email_account.setOnClickListener(this)
        tv_google_ad_id.setOnClickListener(this)
        tv_consent.setOnClickListener(this)
        tv_app_key.setOnClickListener(this)
        tv_deviceId.setOnClickListener(this)
        tv_serial.setOnClickListener(this)
        tv_android_id.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()


        MotrixiSDK.setOnLogListener(object : OnLogListener{
            override fun onLogListener(info: String) {
                Log.e("listener", info)

                //info 内容为json字符串，格式如{"type":1001,"success":true,"info":"App key available"}
            }
        })

        //MotrixiSDK.resetConsentForm(this)


        //UploadCollectedData.formatData(this)


    }

    @SuppressLint("MissingPermission")
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.tv_consent -> {

                val intent: Intent = Intent()
                 intent.setClass(this, DataCollectionActivity::class.java)
                 startActivity(intent)
            }
            R.id.tv_get_imei -> {

                imeiInfo()

            }
            R.id.tv_get_install_package -> {

                installPackageInfo()
            }
            R.id.tv_get_language -> {

                languageInfo()
            }
            R.id.tv_system_info -> {

                val intent: Intent = Intent()
                intent.setClass(this, SystemInfoActivity::class.java)
                startActivity(intent)
            }
            R.id.tv_get_IP -> {

                ipAddressInfo()
            }
            R.id.tv_sim_info -> {

                simInfo()
            }
            R.id.tv_user_agent -> {

                userAgent()
            }
            R.id.tv_get_location -> {

                locationInfo()
            }
            R.id.tv_email_account -> {

                var accounts: Array<Account> = EmailAccountUtil.getEmailAccount(this)
                val emailPattern: Pattern = Patterns.EMAIL_ADDRESS
                val emailAccount: ArrayList<String> = ArrayList()
                if (accounts.isNotEmpty()) {
                    for (account in accounts) {
                        if (emailPattern.matcher(account.name).matches()) {
                            Log.e("account", account.name)
                            emailAccount.add(account.name)
                        }
                    }
                    email_value.text = emailAccount.toString()
                }

            }
            R.id.tv_google_ad_id -> {

                Executors.newSingleThreadExecutor().execute(object : Runnable{
                    override fun run() {
                        val adid: String = AdvertisingIdUtil.getGoogleAdId(applicationContext)!!
                        Log.e("MainActivity", "adid:  $adid")

                        runOnUiThread(object : Runnable{
                            override fun run() {
                                google_ad_id.text = adid
                            }
                        })

                    }
                })
            }
            R.id.tv_app_key -> {

                val app_key_value = ManifestMetaReader.getMetaValue(this, "MOTRIXI_SDK_APPID")
                app_key.text = app_key_value
            }
            R.id.tv_deviceId -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    return

                }
                val tm: TelephonyManager = this.getSystemService(Activity.TELEPHONY_SERVICE) as TelephonyManager
                deviceId.text = tm.deviceId

            }
            R.id.tv_serial -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    return

                }
                var serial = ""
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    serial = Build.getSerial()
                }else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                    serial = Build.SERIAL
                }
                serial_value.text = serial
            }
            R.id.tv_android_id -> {
                val id: String = Settings.System.getString(contentResolver, Settings.Secure.ANDROID_ID)
                android_id.text = id
            }
            else -> {
            }
        }
    }

    private fun imeiInfo() {

        val imeiInfo = DeviceIMInfoUtil.getDeviceIMEI(this)
        tv_imei_value.text = imeiInfo
        show_imei.text = imeiInfo
    }

    private fun installPackageInfo() {

        val packageList = InstalledPackagesUtil.getInstalledPackageList(this)
        Log.e("package list", packageList.toString())
        tv_package_value.text = packageList.toString()
    }

    private fun languageInfo() {

        val languageInfo = LanguageUtil.getLanguageInfo()
        tv_language_value.text = languageInfo
    }

    private fun ipAddressInfo() {
        val ipAddress = IPAddressUtils.getCurrentIPAddress(this)
        tv_IP_value.text = ipAddress
    }

    private fun simInfo() {
        val mcc = SimInfoUtil.getMCC(this)
        val mnc = SimInfoUtil.getMNC(this)
        tv_sim_value.text = "$mcc/$mnc"
    }

    private fun userAgent() {
        val userAgent = UserAgentUtil.getUserAgent(this)
        tv_user_agent_value.text = userAgent
    }

    private fun locationInfo() {
        val gpsLocation = GPSLocationUtil.getLocation(this)
        var cityInfo = ""
        var locationInfo = ""
        if (gpsLocation != null) {
            locationInfo = gpsLocation!!.longitude.toString() + "/" + gpsLocation.latitude.toString()
            var result: List<Address>? = GPSLocationUtil.getAddress(gpsLocation)
            Log.d("address info", result.toString())
            if (result != null && result.isNotEmpty()) {
                cityInfo = result[0].countryName + "/" + result[0].locality+ "/" + result[0].countryCode
                Log.d("countryCode", result[0].countryCode)
            }

            tv_location_value.text = "$cityInfo  $locationInfo"
        }


    }
}