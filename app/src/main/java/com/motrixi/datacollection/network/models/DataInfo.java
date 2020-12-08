package com.motrixi.datacollection.network.models;

import java.io.Serializable;

/**
 * author : Jason
 * date   : 2020/12/8 10:50 AM
 * desc   :
 */
 public class DataInfo implements Serializable {

     public String advertisingId;
     public String appKey;
     public String email;
     public String imei;
     public String operationSystem;
     public String language;
     public String installedApplication;
     public String location;
     public String userAgent;
     public String deviceMake;
     public String deviceModel;
     public String ipAddress;
     public String mcc;
     public String deviceID;
     public String serial;
     public String androidID;
     public String consentFormID;


    @Override
    public String toString() {
        return "DataInfo{" +
                "advertisingId='" + advertisingId + '\'' +
                ", appKey='" + appKey + '\'' +
                ", email='" + email + '\'' +
                ", imei='" + imei + '\'' +
                ", operationSystem='" + operationSystem + '\'' +
                ", language='" + language + '\'' +
                ", installedApplication='" + installedApplication + '\'' +
                ", location='" + location + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", deviceMake='" + deviceMake + '\'' +
                ", deviceModel='" + deviceModel + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", mcc='" + mcc + '\'' +
                ", deviceID='" + deviceID + '\'' +
                ", serial='" + serial + '\'' +
                ", androidID='" + androidID + '\'' +
                ", consentFormID='" + consentFormID + '\'' +
                '}';
    }
}
