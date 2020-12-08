package com.motrixi.datacollection.content;

import com.adobe.fre.FREContext;
import com.motrixi.datacollection.listener.OnLogListener;

/**
 * author : Jason
 * date   : 2020/12/7 5:26 PM
 * desc   :
 */
public class Contants {

    public static final String BASE_SERVER_URL = "https://api.motrixi.osvlabs.com";
    public static final String WEB_URL = "https://www.motrixi.com/index.php/privacy-policy-2/";
    public static String advertisingID = "";
    public static String APP_ID = "";
    public static String APP_KEY = "";

    public static int HOME_CONTAINER_ID = 123456789;
    public static int PRIVACY_TOP_ID = 1234567;
    public static int MORE_TOP_ID = 1234568;
    public static int OPTION_TOP_ID = 1234569;

    public static OnLogListener onLogListener;

    public static int APP_KEY_CODE = 1001;   //verify app key
    public static int CONSENT_FORM_CODE = 1002;   //upload the consent form
    public static int UPLOAD_DATA_CODE = 1003;    // upload the collected data
    public static int FETCH_CONSENT_DATA = 1004;   // fetch consent form data
    public static int CANCEL_COLLECT_DATA = 1005;    // cancel collect data

    public static FREContext mFREContext;
}
