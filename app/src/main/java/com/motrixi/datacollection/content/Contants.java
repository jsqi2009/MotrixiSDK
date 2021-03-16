package com.motrixi.datacollection.content;

import com.adobe.fre.FREContext;
import com.motrixi.datacollection.listener.OnLogListener;
import com.motrixi.datacollection.network.models.ConsentDetailInfo;

/**
 * author : Jason
 * date   : 2020/12/7 5:26 PM
 * desc   :
 */
public class Contants {

    public static final String BASE_SERVER_URL = "https://api.motrixi.osvlabs.com";
//    public static final String BASE_SERVER_URL = "https://platform.motrixi.com";
    public static final String WEB_URL = "https://www.motrixi.com/index.php/privacy-policy-2/";
    public static final String RESPONSE_ERROR = "error";

    public static final String CONSENT_DETAIL_API = "/api/consent/detail";
    public static final String VERIFY_KEY_API = "/api/app/check";
    public static final String CANCEL_CONSENT_API = "/api/cancel_consent/add";
    public static final String SUBMIT_FORM_API = "/api/consent_form/submit";
    public static final String SUBMIT_INFO_API = "/api/sdk_information/add";
    public static final String FETCH_LANGUAGE_API = "/api/consent/list";

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
    public static int GET_LANGUAGE_LIST = 1006;    // cancel collect data
    public static int SET_GPS = 1007;    // SET_GPS

    public static FREContext mFREContext = null;

    public static String terms_content = "";
    public static String options = "";
    public static String cancel_button_text = "";
    public static String confirm_button_text = "";
    public static String option_button_text = "";
    public static String back_button_text = "";
    public static String terms_page_title = "";
    public static String link_page_title = "";
    public static String option_page_title = "";

    public static boolean agreeFlag = false;
    public static ConsentDetailInfo.ResultInfo formInfo = null;
    public static String consentFormID = "";
    public static long lastSyncTime = 0;

    public static String STATEMENT = "";
    public static final String SPECIAL_VALUE = "**##**";

}
