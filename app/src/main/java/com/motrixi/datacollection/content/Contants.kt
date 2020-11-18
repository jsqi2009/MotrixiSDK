package com.motrixi.datacollection.content

import com.motrixi.datacollection.listener.OnAppkeyListener
import com.motrixi.datacollection.listener.OnLogListener


object Contants {



    val BASE_SERVER_URL = "https://api.motrixi.osvlabs.com"
    val WEB_URL = "https://www.motrixi.com/index.php/privacy-policy-2/"
    var advertisingID = ""
    var APP_ID = ""
    var APP_KEY = ""

    val PRIVATE_STATEMENT_1 = "By entering your email and clicking confirm, you consent to the collection of the use of your data to our trusted partners and us. Our trusted partners whom we share the information with may include storage, analytic providers, agencies, platforms, data providers, and research development. The purpose of sharing the data allows our third parties for the following (a) Data Customization: to custom data with demographics, behavioral, contextual or other information for personalized targeted advertisement (b) Measurement: measure key point indicators to evaluate marketing performance (c) Analytics:" +
            "Identify and analyze behavioral data and patterns, and/or make more-informed business decisions and verify or disprove scientific models, theories and hypotheses (d) Modeling: To pinpoint key shared attributions for look alike audiences (e) Research and Development: allowing parties to process" +
            "information to create and/or enhance the quality of products (f) Data Management Platform: to create better audiences to target specific users to increase performance When you confirm, you not only grant your consent, you acknowledge you are of 16 years of age and older. Please note, if you choose to click cancel, no information will be collected from you. To learn more" +
            "about the terms in its entirety, please click here."
    val PRIVATE_STATEMENT_2 = "\nWe thank you for installing our app and helping us improve the user experience by clicking \"Confirm\"."

    val OPTION_VALUE_1 = "Data Customization: to custom data with demographics, behavioral, contextual or other information for personalized targeted advertisement"
    val OPTION_VALUE_2 = "Measurement: measure key point indicators to evaluate marketin"
    val OPTION_VALUE_3 = "Analytics: Identify and analyze behavioral data and patterns, and/or make more-informed business decisions and verify or disprove scientific models, theories and hypotheses"
    val OPTION_VALUE_4 = "Modeling: To pinpoint key shared attributions or look alike audiences"
    val OPTION_VALUE_5 = "Research and Development: allowing parties to process information to create and/or enhance the quality of products"
    val OPTION_VALUE_6 = "Data Management Platform: to create better audiences to target specific users to increase performance"

    var HOME_CONTAINER_ID = 123456789
    var PRIVACY_TOP_ID = 1234567
    var MORE_TOP_ID = 1234568
    var OPTION_TOP_ID = 1234569

    var onLogListener: OnLogListener? = null

    var APP_KEY_CODE = 1001   //verify app key
    var CONSENT_FORM_CODE = 1002   //upload the consent form
    var UPLOAD_DATA_CODE = 1003    // upload the collected data
    var UPLOAD_LOG_CODE = 1004    // upload log info





}
