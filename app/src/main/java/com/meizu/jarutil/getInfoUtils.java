package com.meizu.jarutil;

import android.content.Context;
import android.content.pm.PackageInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by libinhui on 2016/6/29.
 */
public class GetInfoUtils {

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
}
