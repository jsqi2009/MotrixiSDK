package com.motrixi.datacollection.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import java.util.*

/**
 * author : Jason
 *  date   : 2020/10/21 2:13 PM
 *  desc   :
 */
object GPSLocationUtil {

    private var mLocationManager: LocationManager? = null
    private const val TAG = "GPSLocationUtil"
    private var mLocation: Location? = null
    private var mContext: Context? = null

    fun init(context: Context) {
        mContext = context
        mLocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // check the gps whether on
        if (!mLocationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(context, "Please set the GPS on...", Toast.LENGTH_SHORT).show()
            //val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            //mContext.startActivityForResult(intent, 0)
            return
        }
        val bestProvider = mLocationManager!!.getBestProvider(getCriteria()!!, true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mContext!!.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && mContext!!.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }
        val location = mLocationManager!!.getLastKnownLocation(bestProvider!!)
        mLocation = location
        //mLocationManager!!.addGpsStatusListener(listener)

        //mLocationManager.requestLocationUpdates(bestProvider, 2000, 0, locationListener);
        //mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, locationListener);
        mLocationManager!!.requestLocationUpdates(bestProvider, 2000, 1f, locationListener)
    }

    /**
     *
     * @return
     */
    private fun getCriteria(): Criteria? {
        val criteria = Criteria()
        //Criteria.ACCURACY_COARSE is Roughly ，Criteria.ACCURACY_FINE is Fine
        criteria.accuracy = Criteria.ACCURACY_FINE
        criteria.isCostAllowed = true
        criteria.isBearingRequired = true
        criteria.isAltitudeRequired = true
        criteria.powerRequirement = Criteria.POWER_LOW
        return criteria
    }

    private val locationListener: LocationListener = object : LocationListener {
        //location changed
        override fun onLocationChanged(location: Location) {
            mLocation = location
            if (mLocation != null) {
                Log.d(TAG, "time：" + location.time)
                Log.d(TAG, "lon：" + location.longitude)
                Log.d(TAG, "lat：" + location.latitude)
            }

        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            when (status) {
                LocationProvider.AVAILABLE -> Log.d(TAG, "GPS available")
                LocationProvider.OUT_OF_SERVICE -> Log.d(TAG, "GPS out of service")
                LocationProvider.TEMPORARILY_UNAVAILABLE -> Log.d(TAG, "GPS temp unavailable")
            }
        }

        override fun onProviderEnabled(provider: String) {
            @SuppressLint("MissingPermission")
            val location = mLocationManager!!.getLastKnownLocation(provider)
            mLocation = location
        }

        override fun onProviderDisabled(provider: String) {
            mLocation = null
        }
    }

    /**
     * @return Location--->getLongitude()/getLatitude()
     */
    fun getLocation(context: Context): Location? {
        init(context)
        if (mLocation == null) {
            Log.d("GPSLocationUtil", "setLocationData: current location is null")
            //Toast.makeText(mContext, "current location is null", Toast.LENGTH_SHORT).show()
            return null
        }
        return mLocation
    }

    fun getLocalCity(): String? {
        if (mLocation == null) {
            Log.d(TAG, "getLocalCity: city info is null")
            return ""
        }
        val result = getAddress(mLocation)
        var city: String? = ""
        if (result != null && result.size > 0) {
            city = result[0].locality //get city info
        }
        return city
    }

    fun getAddressStr(): String? {
        if (mLocation == null) {
            Log.d(TAG, "getAddressStr: address info is null")
            return ""
        }
        val result = getAddress(mLocation)
        var address: String? = ""
        if (result != null && result.size > 0) {
            address = result[0].getAddressLine(0) //get address info
        }
        return address
    }

    fun getAddress(location: Location?): List<Address>? {
        var result: List<Address>? = null
        try {
            if (location != null) {
                val gc = Geocoder(mContext, Locale.getDefault())
//                val gc = Geocoder(mContext, Locale.ENGLISH)
                result = gc.getFromLocation(location.latitude, location.longitude, 1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }


    /*@SuppressLint("MissingPermission")
    var listener = GpsStatus.Listener { event ->
        when (event) {
            GpsStatus.GPS_EVENT_FIRST_FIX -> Log.d(TAG, "first location")
            GpsStatus.GPS_EVENT_SATELLITE_STATUS -> {
                val gpsStatus = mLocationManager!!.getGpsStatus(null)
                val maxSatellites = gpsStatus!!.maxSatellites
                val iters: Iterator<GpsSatellite> = gpsStatus.satellites.iterator()
                var count = 0
                while (iters.hasNext() && count <= maxSatellites) {
                    val s = iters.next()
                    count++
                }
                //Log.d(TAG, "satellite count: $count")
            }
            GpsStatus.GPS_EVENT_STARTED -> Log.d(TAG, "location start")
            GpsStatus.GPS_EVENT_STOPPED -> Log.d(TAG, "location stop")
        }
    }*/
}