package com.motrixi.datacollection.utils

import android.accounts.Account
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Address
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.motrixi.datacollection.network.models.DataInfo
import com.google.gson.JsonObject
import com.motrixi.datacollection.content.Contants
import com.motrixi.datacollection.content.Session
import com.motrixi.datacollection.listener.OnLogListener
import com.motrixi.datacollection.network.HttpClient
import com.motrixi.datacollection.network.ManifestMetaReader
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors
import java.util.regex.Pattern

/**
 * author : Jason
 *  date   : 2020/10/23 11:20 AM
 *  desc   :
 */
object UploadCollectedData {


    var advertisingId = ""
    var mSession: Session? = null

    fun formatData(context: Context) {

        var info: DataInfo = DataInfo()

        info.appKey = getAppkey(context)
        mSession = Session(context)

        getAdvertisingId(context)
        Log.e("ad id", Contants.advertisingID)
        info.advertisingId = Contants.advertisingID

        info.email = getEmailAccount(context).toString()
        info.imei = getIMEI(context!!)
        info.operationSystem = getSystemInfo(context)
        info.language = getLanguage(context)
        info.installedApplication = getInstalledApp(context)
//        info.installedApplication = ""
//        info.location = ""
        info.location = getLocationInfo(context)
        info.userAgent = getUserAgent(context)
        info.deviceMake = getDeviceMake(context)
        info.deviceModel = getDeviceModel(context)
        info.ipAddress = getCurrentIP(context)
        info.mcc  = getMCCAndMNC(context)
        info.deviceID  = getDeviceId(context)
        info.serial  = getSerial(context)
        info.androidID  = getAndroidId(context)
        info.privacyConsent = 1
        info.consentFormID = mSession!!.consentFormID

        Log.d("info", info.toString())

        if (Contants.onLogListener != null) {
            Contants.onLogListener!!.onLogListener(info.toString())
        }

        //Toast.makeText(context, "Uploading..", Toast.LENGTH_LONG).show()
        uploadData(context, info)
    }

    private fun uploadData(context: Context, info: DataInfo) {

        var call = HttpClient.uploadData(context!!, info)
        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: retrofit2.Call<JsonObject>, t: Throwable) {

            }

            override fun onResponse(call: retrofit2.Call<JsonObject>, response: Response<JsonObject>) {

                if (response.isSuccessful) {
                    val responseObject: JSONObject = JSONObject(response.body().toString())
                    if (Contants.onLogListener != null) {
                        Contants.onLogListener!!.onLogListener(
                            MessageUtil.logMessage(
                                Contants.UPLOAD_DATA_CODE,
                                true,
                                responseObject.optJSONObject("result").toString()
                            )
                        )
                    }
                } else {
                    var error = JSONObject(response.errorBody()!!.string())
                    Contants.onLogListener!!.onLogListener(
                        MessageUtil.logMessage(
                            Contants.UPLOAD_DATA_CODE,
                            false,
                            error.optString("message")
                        )
                    )
                }
            }
        })
    }


    private fun getAppkey(context: Context): String? {
        if (!TextUtils.isEmpty(Contants.APP_KEY)) {
            return Contants.APP_KEY
        }

        val value = ManifestMetaReader.getMetaValue(context, "MOTRIXI_SDK_APPID")
        return value
    }

    private fun getAdvertisingId(context: Context) {
        Executors.newSingleThreadExecutor().execute(object : Runnable{
            override fun run() {
                val googleId: String = AdvertisingIdUtil.getGoogleAdId(context.applicationContext)!!
                Log.d("google Id:", googleId)
                advertisingId = googleId
            }
        })
    }

    private fun getEmailAccount(context: Context): ArrayList<String> {
        try {
            val accounts: Array<Account> = EmailAccountUtil.getEmailAccount(context)
            if (accounts == null || accounts.isEmpty()) {

                return ArrayList()
            }
            val emailAccount: ArrayList<String> = ArrayList()
            //Log.e("email list", emailAccount.toString())
            val emailPattern: Pattern = Patterns.EMAIL_ADDRESS
            if (accounts.isNotEmpty()) {
                for (account in accounts) {
                    if (emailPattern.matcher(account.name).matches()) {
                        Log.e("email account", account.name)
                        emailAccount.add(account.name)
                    }
                }
            }
            UploadLogUtil.uploadLogData(context, "emailAccount:$emailAccount")
            return emailAccount
        } catch (e: Exception) {
            return ArrayList()
        }
    }

    private fun getIMEI(context: Context): String {

        try {
            Log.e("version", Build.VERSION_CODES.Q.toString())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                return ""
            }
            val imeiInfo = DeviceIMInfoUtil.getDeviceIMEI(context)

            UploadLogUtil.uploadLogData(context, "imeiInfo:$imeiInfo")
            return imeiInfo
        } catch (e: Exception) {
            return ""
        }
    }

    private fun getSystemInfo(context: Context): String? {

        var systemVersion = SystemInfoUtils.getDeviceAndroidVersion()
        return systemVersion
    }

    private fun getLanguage(context: Context): String {

        val languageInfo = LanguageUtil.getLanguageInfo()
        UploadLogUtil.uploadLogData(context, "languageInfo:$languageInfo")
        return languageInfo
    }

    private fun getInstalledApp(context: Context): String {

        var jsonObject: JSONObject = JSONObject()
        var applicationList: List<String>? = InstalledPackagesUtil.getInstalledPackageList(context!!)
        for (i in applicationList!!.indices) {
            jsonObject.put(i.toString(), applicationList[i])
        }
        return jsonObject.toString()
    }

    private fun getLocationInfo(context: Context): String {
        try {
            var jsonObject: JSONObject = JSONObject()
            val gpsLocation = GPSLocationUtil.getLocation(context)
            if (gpsLocation != null) {

                jsonObject.put("longitude",gpsLocation!!.longitude)
                jsonObject.put("latitude",gpsLocation!!.latitude)

                var result: List<Address>? = GPSLocationUtil.getAddress(gpsLocation)
                if (result != null && result.isNotEmpty()) {

                    jsonObject.put("countryName", result[0].countryName)
                    jsonObject.put("locality", result[0].locality)
                    if (result[0].countryCode != "0") {
                        jsonObject.put("countryCode", result[0].countryCode)
                    }
                }

            }
            Log.d("location info", jsonObject.toString())
            UploadLogUtil.uploadLogData(context, "location:$jsonObject")
            return jsonObject.toString()
        } catch (e: Exception) {
            return ""
        }
    }

    private fun getUserAgent(context: Context): String {

        val userAgent = UserAgentUtil.getUserAgent(context)
        return userAgent
    }

    private fun getDeviceMake(context: Context): String? {

        var deviceMake = SystemInfoUtils.getDeviceManufacturer()
        return deviceMake
    }

    private fun getDeviceModel(context: Context): String? {

        var deviceModel = SystemInfoUtils.getDeviceModel()
        return deviceModel
    }

    private fun getCurrentIP(context: Context): String? {

        var ipAddress = IPAddressUtils.getCurrentIPAddress(context!!)
        return ipAddress
    }

    private fun getMCCAndMNC(context: Context): String? {

        var jsonObject: JSONObject = JSONObject()
        val mcc = SimInfoUtil.getMCC(context)
        val mnc = SimInfoUtil.getMNC(context)
        jsonObject.put("MCC", mcc)
        jsonObject.put("MNC", mnc)

        return jsonObject.toString()
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceId(context: Context): String {

        try {
            var deviceId = ""
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                return deviceId
            }
            val manager: TelephonyManager = context.getSystemService(Activity.TELEPHONY_SERVICE) as TelephonyManager
            if (manager == null) {
                return ""
            }
            deviceId = manager.deviceId
            return deviceId
        } catch (e: Exception) {
            return ""
        }
    }

    @SuppressLint("MissingPermission")
    private fun getSerial(context: Context): String {

        try {
            var serial = ""
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                return serial
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                serial = Build.getSerial()
            }else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                serial = Build.SERIAL
            }
            return serial
        } catch (e: Exception) {
            return ""
        }
    }

    private fun getAndroidId(context: Context): String {
        val androidId: String = Settings.System.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        UploadLogUtil.uploadLogData(context, "androidId:$androidId")
        return androidId
    }


}