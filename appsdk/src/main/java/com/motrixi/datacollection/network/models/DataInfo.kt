package com.motrixi.datacollection.network.models

import java.io.Serializable

/**
 * @author jsqi
 * @time 2019/12/4 14:54
 */
class DataInfo: Serializable {

    var advertisingId: String? = null
    var appKey: String? = null
    var email: String? = null
    var imei: String? = null
    var operationSystem: String? = null
    var language: String? = null
    var installedApplication: String? = null
    var location: String? = null
    var userAgent: String? = null
    var deviceMake: String? = null
    var deviceModel: String? = null
    var ipAddress: String? = null
    var mcc: String? = null
    var deviceID: String? = null
    var serial: String? = null
    var androidID: String? = null
    var privacyConsent: Int? = 1
    override fun toString(): String {
        return "DataInfo(advertisingId=$advertisingId, appKey=$appKey, email=$email, imei=$imei, operationSystem=$operationSystem, language=$language, installedApplication=$installedApplication, location=$location, userAgent=$userAgent, deviceMake=$deviceMake, deviceModel=$deviceModel, ipAddress=$ipAddress, mcc=$mcc, deviceID=$deviceID, serial=$serial, androidID=$androidID, privacyConsent=$privacyConsent)"
    }


}