package com.motrixi.datacollection.network;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.adobe.fre.FREContext;
import com.motrixi.datacollection.content.Contants;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author : Jason
 * date   : 2020/12/10 1:26 PM
 * desc   :
 */
public class PostMethodUtils {


    public static String httpPost(String urlString, HashMap<String, String> postVars) {

        List<Pair<String, String>> postParams = new ArrayList<>();
        for (Map.Entry<String, String> entry : postVars.entrySet()) {
            postParams.add(new Pair<>(entry.getKey(), entry.getValue()));
        }

        return httpRequest(urlString, "POST", postParams);
    }

    private static String httpRequest(String requestURL, String requestMethod, List<Pair<String, String>> postParams) {
        String msg = "error";
        try{
            HttpURLConnection conn = (HttpURLConnection) new URL(Contants.BASE_SERVER_URL + requestURL).openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);

            String postString = "";

            Log.e("post url",conn.getURL().getPath());

            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            postString = getQueryStringFromValues(postParams);
            writer.write(postString);
            writer.flush();
            writer.close();
            out.close();

            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                ByteArrayOutputStream message = new ByteArrayOutputStream();
                int len = 0;
                byte buffer[] = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    message.write(buffer, 0, len);
                }
                is.close();
                message.close();
                msg = new String(message.toByteArray());
                return msg;
            } else {
                return msg;
            }
        } catch(Exception e){
            e.printStackTrace();
            return msg;
        }
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
}
