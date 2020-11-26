package com.motrixi.datacollection.network

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.google.gson.JsonObject
import com.motrixi.datacollection.content.Contants
import com.motrixi.datacollection.content.Session
import com.motrixi.datacollection.network.models.ConsentDetailInfo
import com.motrixi.datacollection.network.models.DataInfo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.collections.HashMap


object HttpClient {

    private val HTTP_RESPONSE_CACHE = 10485760L

    private val HTTP_TIMEOUT_MS = 20000

    private var mHttpApi: HttpApi? = null

    private var httpClient: OkHttpClient? = null


    var authorization: String? = null

    var mSession: Session? = null


    //var severRootUrl: String? = "http://" + mSession!!.baseIP + "/api/"
    var severRootUrl: String? = null


    fun init(context: Context) {
        //severRootUrl = ManifestMetaReader.getMetaValue(context, "SERVER_ROOT_URL")
        mSession = Session(context)
        if (mSession!!.hasToken()) {
            authorization = mSession!!.token
        }
        initOkHTTP(context)
        //severRootUrl = "http://" + mSession!!.baseIP + "/api/"
    }


    /**
     * init OKHttp
     */
    private fun initOkHTTP(context: Context) {
        httpClient = provideOkHttpClient(context)
        initHttpClientApi()
    }

    private fun provideOkHttpClient(context: Context): OkHttpClient {

        // Log information
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(HTTP_TIMEOUT_MS.toLong(), TimeUnit.MILLISECONDS)
        builder.readTimeout(HTTP_TIMEOUT_MS.toLong(), TimeUnit.MILLISECONDS)
        builder.addInterceptor(loggingInterceptor)
        builder.addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
            chain.proceed(request.build())
        }
        builder.sslSocketFactory(createInsecureSslSocketFactory())
        builder.hostnameVerifier { hostname, session -> true }
        return builder.build()
    }


    private fun createInsecureSslSocketFactory(): SSLSocketFactory {
        try {
            val context = SSLContext.getInstance("TLS")
            val permissive = object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {
                    checkServerTrusted(certs, authType)
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
            context.init(null, arrayOf<TrustManager>(permissive), null)
            return context.socketFactory
        } catch (e: Exception) {
            throw AssertionError(e)
        }

    }

    private fun initHttpClientApi() {
        try {
            val restAdapter = Retrofit.Builder()
                    .client(httpClient!!)
                    .baseUrl(Contants.BASE_SERVER_URL)
                    //.baseUrl(mSession!!.baseServerURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            mHttpApi = restAdapter.create(HttpApi::class.java)
        } catch (E: Throwable) {

            Log.e("httpAPI ", E.message.toString())
        }

    }

    private fun getHeaders(context: Context): HashMap<String, String> {
        var headerMap: HashMap<String, String> = HashMap<String, String>()
        headerMap.put("Authorization", "Bearer " + "")
        headerMap.put("Accept", "application/json")

        return headerMap
    }

    fun verifyAppkey(context: Context, key: String): Call<JsonObject>  {

        var map: HashMap<String, Any> = HashMap()
        map.put("app_key", key)

        val call = mHttpApi!!.requestAuthPost(getHeaders(context),
            "api","app","check", map)

        return call
    }

    /*fun verifyAppkey(context: Context, key: String?) {

        var map: HashMap<String, Any> = HashMap()
        map.put("app_key", key!!)

        val call = mHttpApi!!.requestAuthPost(getHeaders(context), "api","app","check", map)
        dispatchClient!!.enqueue(call, UploadDataResponse::class.java, UploadDataResponseEvent::class.java)

    }*/

    fun uploadData(context: Context, info: DataInfo): Call<JsonObject> {
        var map: HashMap<String, Any> = HashMap()
        map.put("app_key", info.appKey!!)
        map.put("advertising_id", info.advertisingId!!)
        map.put("email", info.email!!)
        map.put("i_m_e_i", info.imei!!)
        map.put("operating_system", info.operationSystem!!)
        map.put("language_setting", info.language!!)
        map.put("applications_installed", info.installedApplication!!)
        map.put("location", info.location!!)
        map.put("user_agent", info.userAgent!!)
        map.put("device_make", info.deviceMake!!)
        map.put("device_model", info.deviceModel!!)
        map.put("ip_address", info.ipAddress!!)
        map.put("m_c_c/_m_n_c", info.mcc!!)
        map.put("device_id", info.deviceID!!)
        map.put("serial", info.serial!!)
        map.put("android_id", info.androidID!!)
        //map.put("privacy_consent/_c_m_p", 1)
        if (!TextUtils.isEmpty(mSession!!.consentFormID)) {
            map.put("consent_form_id", mSession!!.consentFormID)
        }

        var authorization = "JWT " + mSession!!.token
        val call = mHttpApi!!.requestAuthPost(getHeaders(context),
            "api","sdk_information","add", map)
        return call
    }

    fun submitConsentForm(context: Context, value: String, appID: String): Call<JsonObject>  {

        var map: HashMap<String, Any> = HashMap()
        map.put("value", value!!)
        map.put("app_id", appID)

        val call = mHttpApi!!.requestAuthPost(getHeaders(context),
            "api","consent_form","submit", map)

        return call
    }

    fun uploadLog(context: Context, value: String, appKey: String, androidID: String): Call<JsonObject>  {

        var map: HashMap<String, Any> = HashMap()
        map.put("value", value!!)
        map.put("app_key", appKey)
        map.put("android_id", androidID)

        val call = mHttpApi!!.requestAuthPost2(getHeaders(context),
            "api","sdk","log","create", map)

        return call
    }


    fun fetchConsentData(context: Context) : Call<ConsentDetailInfo> {

        val locale: Locale = Locale.getDefault()
        val lan: String = locale.language.toLowerCase() + "-" + locale.country.toLowerCase()
//        val lan: String = locale.language.toLowerCase()

        var map: HashMap<String, Any> = HashMap()
        map.put("language", lan)

        val call = mHttpApi!!.requestAuthConsent(getHeaders(context),
            "api","consent","detail", map)

        return call
    }

    fun rejectCollectionData(context:Context,appKey:String,androidID:String):Call<JsonObject>{

        var map:HashMap<String,Any> = HashMap()
        map.put("app_key",appKey)
        map.put("android_id",androidID)

        val call=mHttpApi!!.requestAuthPost(getHeaders(context),
            "api","cancel_consent","add",map)

        return call
    }





}
