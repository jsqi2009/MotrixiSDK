package com.motrixi.datacollection.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.adobe.fre.FREContext;

import java.util.ArrayList;
import java.util.List;

/**
 * author : Jason
 * date   : 2020/12/8 2:46 PM
 * desc   :
 */
public class InstalledPackagesUtil {

    public static ArrayList<String> getInstalledPackageList(FREContext context){
        ArrayList<String> packages = new ArrayList();
        try {
            List<PackageInfo> packageInfos = context.getActivity().getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);

            for (PackageInfo info : packageInfos) {


                if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    String pkg = info.packageName;
                    String appName = (String) info.applicationInfo.loadLabel(context.getActivity().getPackageManager());
                    packages.add(pkg);
                } else {
                    //system app
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return packages;
    }

    public static ArrayList<String> getInstalledPackageList(Context context){
        ArrayList<String> packages = new ArrayList();
        try {
            List<PackageInfo> packageInfos = context.getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);

            for (PackageInfo info : packageInfos) {


                if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    String pkg = info.packageName;
                    String appName = (String) info.applicationInfo.loadLabel(context.getPackageManager());
                    packages.add(pkg);
                } else {
                    //system app
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return packages;
    }
}
