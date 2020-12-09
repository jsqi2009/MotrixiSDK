package com.motrixi.datacollection.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.adobe.fre.FREContext;

import java.util.List;
import java.util.Locale;

/**
 * author : Jason
 * date   : 2020/12/8 3:34 PM
 * desc   :
 */
public class GPSLocationUtil {

    private static LocationManager mLocationManager;
    private static final String TAG = "GPSLocationUtil";
    private static Location mLocation = null;
    private static FREContext mContext;


    public static void init(FREContext context) {

        mContext = context;
        mLocationManager = (LocationManager) context.getActivity().getSystemService(Context.LOCATION_SERVICE);
        // check the gps whether on
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Toast.makeText(context, "Please set the GPS on...", Toast.LENGTH_SHORT).show();
            return;
        }

        String bestProvider = mLocationManager.getBestProvider(getCriteria(), true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mContext.getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && mContext.getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
        }
        Location location = mLocationManager.getLastKnownLocation(bestProvider);
        mLocation = location;

        //mLocationManager.requestLocationUpdates(bestProvider, 2000, 0, locationListener);
        //mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, locationListener);
        mLocationManager.requestLocationUpdates(bestProvider, 2000, 1, locationListener);
    }

    /**
     *
     * @return
     */
    private static Criteria getCriteria() {
        Criteria criteria = new Criteria();
        //Criteria.ACCURACY_COARSE is Roughly ，Criteria.ACCURACY_FINE is Fine
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(true);
        criteria.setBearingRequired(true);
        criteria.setAltitudeRequired(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    private static LocationListener locationListener = new LocationListener() {

        //location changed
        public void onLocationChanged(Location location) {
            mLocation = location;
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d(TAG, "GPS available");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d(TAG, "GPS out of service");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d(TAG, "GPS temp unavailable");
                    break;
            }
        }

        public void onProviderEnabled(String provider) {
            @SuppressLint("MissingPermission")
            Location location = mLocationManager.getLastKnownLocation(provider);
            mLocation = location;
        }

        public void onProviderDisabled(String provider) {
            mLocation = null;
        }
    };

    /**
     * @return Location--->getLongitude()/getLatitude()
     */
    public static Location getLocation(FREContext context) {
        init(context);
        if (mLocation == null) {
            Log.e("GPSLocationUtil", "setLocationData: current location is null");
            //Toast.makeText(mContext, "current location is null", Toast.LENGTH_SHORT).show();
            return null;
        }
        return mLocation;
    }

    public static List<Address> getAddress(Location location) {
        List<Address> result = null;
        try {
            if (location != null) {
                Geocoder gc = new Geocoder(mContext.getActivity(), Locale.getDefault());
                result = gc.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
