package tv.niuwa.live.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.smart.androidutils.utils.LogUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/8/30.
 * Author: XuDeLong
 */
public class LocationUtile {

    private static final String TAG = "LocationUtile";

    public static Location getLocation(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,this);
        //获取所有可用的位置提供器
        String locationProvider;
        List<String> providers = locationManager.getProviders(true);
//        if (providers.contains(LocationManager.GPS_PROVIDER)) {
//            LogUtils.i(TAG, "getLocation====gps");
//            //如果是GPS
//            locationProvider = LocationManager.GPS_PROVIDER;
//
//        } else
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            LogUtils.i(TAG, "getLocation====network");
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            LogUtils.i(TAG, "getLocation====provider==null");
            return null;
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            LogUtils.i(TAG, "getLocation====check");
            Location location = locationManager.getLastKnownLocation(locationProvider);
            return location;
        } else {
            Location location = locationManager.getLastKnownLocation(locationProvider);
            LogUtils.i(TAG, "getLocation====check=null");
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LogUtils.i(TAG, "getLocation====check=null==latitude" + latitude);
            LogUtils.i(TAG, "getLocation====check=null==longitude" + longitude);
            return null;
        }
    }


}
