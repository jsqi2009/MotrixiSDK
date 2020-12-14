package com.motrixi.datacollection.network;

import android.util.Log;
import android.util.Pair;

import com.motrixi.datacollection.content.Contants;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * author : Jason
 * date   : 2020/12/10 1:26 PM
 * desc   :
 */
public class GetMethodUtils {


    public static String httpGet(String urlString, String language) {

        return httpRequest(urlString, "GET", language);
    }

    private static String httpRequest(String requestURL, String requestMethod, String language) {
        String msg = "error";
        try{
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    System.out.println("Warning: URL Host: " + urlHostName + " vs. "
                            + session.getPeerHost());
                    return true;
                }
            };
            trustAllHttpsCertificates();
            HttpsURLConnection.setDefaultHostnameVerifier(hv);

            String url = Contants.BASE_SERVER_URL + requestURL + "?language=" + URLEncoder.encode(language, "utf-8");
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod(requestMethod);
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);

            Log.e("get url", conn.getURL().getPath());

            conn.connect();

            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                ByteArrayOutputStream message = new ByteArrayOutputStream();
                int len = 0;
                byte buffer[] = new byte[1024];
                String data = "";
                while ((len = is.read(buffer)) != -1) {
                    String str = new String(buffer,0,len);
                    data += str;
                }
                return data;
            }
        } catch(Exception e){
            e.printStackTrace();
            return e.getMessage();
        }

        return msg;
    }

    private static String getQueryStringFromValues(List<Pair<String, String>> params) {
        try {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Pair pair : params) {
                if (first) {
                    first = false;
                } else {
                    result.append("&");
                }

                try {
                    result.append(URLEncoder.encode(pair.first.toString(), "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(pair.second.toString(), "UTF-8"));
                } catch (Exception e) {
                }
            }

            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
                .getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
                .getSocketFactory());
    }

    static class miTM implements javax.net.ssl.TrustManager,
            javax.net.ssl.X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
    }
}
