package com.meizu.jarutil;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by libinhui on 2016/6/29.
 */
public class GetInfoUtils {
    private static ConnectivityManager cm;
    private static WifiManager wifi;

    /*
    获取应用版本号
     */
    public static String getAppVersion(Context cn, String packagename){
        List<PackageInfo> packages = cn.getPackageManager().getInstalledPackages(0);
        for(int i=0;i<packages.size();i++) {
            PackageInfo packageInfo = packages.get(i);
            if(packageInfo.packageName.equals(packagename)){
                return packageInfo.versionName;
            }
        }
        return "";
    }

    public static String getCurrentTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }

    public static boolean isWifiIsConnected(Context cn){
        cm = (ConnectivityManager) cn.getSystemService(cn.CONNECTIVITY_SERVICE);
        NetworkInfo res = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(res != null){
            return res.isConnected();
        }
        return false;
    }

    public static boolean getWifiSsid(String ssidname){
        WifiInfo wii = wifi.getConnectionInfo();
        String res = wii.getSSID().replace("\"","");
        if(res.equals(ssidname)){
            return true;
        }
        else{
            return false;
        }
    }
}
