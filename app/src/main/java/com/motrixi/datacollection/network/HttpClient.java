package com.motrixi.datacollection.network;

import android.content.Context;
import android.text.TextUtils;

import com.adobe.fre.FREContext;
import com.google.gson.JsonObject;
import com.motrixi.datacollection.content.Contants;
import com.motrixi.datacollection.content.Session;
import com.motrixi.datacollection.network.models.ConsentDetailInfo;
import com.motrixi.datacollection.network.models.DataInfo;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author : Jason
 * date   : 2020/12/8 10:28 AM
 * desc   :
 */
public class HttpClient {


    private static final long HTTP_RESPONSE_CACHE = 10485760L;
    private static final int HTTP_TIMEOUT_MS = 20000;
    private static HttpApi mHttpApi;
    private static OkHttpClient httpClient;
    private static String authorization;
    static Session mSession;

    public static void init(Context context) {
        mSession = new Session(context);
        initOkHTTP(context);
    }

    /**
     * 初始化OKHttp
     */
    private static void initOkHTTP(Context context) {
        httpClient = provideOkHttpClient(context);
        initHttpClientApi();
    }

    private static OkHttpClient provideOkHttpClient(Context context) {

        // Log信息
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(HTTP_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        builder.readTimeout(HTTP_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        builder.addInterceptor(loggingInterceptor);
        builder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder request = original.newBuilder();
                return chain.proceed(request.build());
            }
        });
        builder.sslSocketFactory(createInsecureSslSocketFactory());
        builder.hostnameVerifier(new HostnameVerifier() {
                                     @Override
                                     public boolean verify(String hostname, SSLSession session) {
                                         return true;
                                     }
                                 }

        );
        OkHttpClient okHttpClient = builder.build();
        return okHttpClient;
    }

    private static SSLSocketFactory createInsecureSslSocketFactory() {
        try {
            SSLContext context = SSLContext.getInstance("TLS");
            TrustManager permissive = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType)
                        throws CertificateException {
                    checkServerTrusted(certs, authType);
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            };
            context.init(null, new TrustManager[]{permissive}, null);
            return context.getSocketFactory();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    private static void initHttpClientApi() {

        try {
            Retrofit restAdapter = new Retrofit.Builder()
                    .client(httpClient)
                    .baseUrl(Contants.BASE_SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            mHttpApi = restAdapter.create(HttpApi.class);
        } catch (Throwable E) {
            Retrofit restAdapter = new Retrofit.Builder()
                    .client(httpClient)
                    .baseUrl(Contants.BASE_SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            mHttpApi = restAdapter.create(HttpApi.class);
        }

    }

    private static HashMap<String, String> getHeaders() {
        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Authorization", "Bearer " + "");
        headerMap.put("Accept", "application/json");

        return headerMap;
    }

    public static Call<JsonObject> verifyAppkey(Context context, String key) {

        HashMap<String, Object> map = new HashMap();
        map.put("app_key", key);

        Call<JsonObject> call = mHttpApi.requestAuthPost(getHeaders(),
                "api","app","check", map);

        return call;
    }

    public static Call<JsonObject> uploadData(Context context, DataInfo info) {
        HashMap<String, Object> map = new HashMap();
        map.put("app_key", info.appKey);
        map.put("advertising_id", info.advertisingId);
        map.put("email", info.email);
        map.put("i_m_e_i", info.imei);
        map.put("operating_system", info.operationSystem);
        map.put("language_setting", info.language);
        map.put("applications_installed", info.installedApplication);
        map.put("location", info.location);
        map.put("user_agent", info.userAgent);
        map.put("device_make", info.deviceMake);
        map.put("device_model", info.deviceModel);
        map.put("ip_address", info.ipAddress);
        map.put("m_c_c/_m_n_c", info.mcc);
        map.put("device_id", info.deviceID);
        map.put("serial", info.serial);
        map.put("android_id", info.androidID);
        if (!TextUtils.isEmpty(mSession.getConsentFormID())) {
            map.put("consent_form_id", mSession.getConsentFormID());
        }

        Call<JsonObject> call = mHttpApi.requestAuthPost(getHeaders(),
                "api","sdk_information","add", map);
        return call;
    }

    public static Call<JsonObject> submitConsentForm(Context context, String value, String appID)  {

        HashMap<String, Object> map = new HashMap();
        map.put("value", value);
        map.put("app_id", appID);

        Call<JsonObject> call = mHttpApi.requestAuthPost(getHeaders(),
                "api","consent_form","submit", map);

        return call;
    }

    public static Call<JsonObject> uploadLog(Context context, String value, String appKey, String androidID){

        HashMap<String, Object> map = new HashMap();
        map.put("value", value);
        map.put("app_key", appKey);
        map.put("android_id", androidID);

        Call<JsonObject> call = mHttpApi.requestAuthPost2(getHeaders(),
                "api","sdk","log","create", map);

        return call;
    }


    public static Call<ConsentDetailInfo> fetchConsentData(Context context){

        Locale locale = Locale.getDefault();
        String lan = locale.getLanguage().toLowerCase() + "-" + locale.getCountry().toLowerCase();

        HashMap<String, Object> map = new HashMap();
        map.put("language", lan);

        Call<ConsentDetailInfo> call = mHttpApi.requestAuthConsent(getHeaders(),
                "api","consent","detail", map);

        return call;
    }

    public static Call<JsonObject> rejectCollectionData(Context context, String appKey, String androidID){

        HashMap<String, Object> map = new HashMap();
        map.put("app_key",appKey);
        map.put("android_id",androidID);

        Call<JsonObject> call=mHttpApi.requestAuthPost(getHeaders(),
                "api","cancel_consent","add",map);

        return call;
    }
}
