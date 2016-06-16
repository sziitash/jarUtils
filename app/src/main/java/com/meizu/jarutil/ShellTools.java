package com.meizu.jarutil;

/**
 * Created by libinhui on 2016/5/24.
 */
public class ShellTools {
    private static String res = ShellUtils.execCommand("dumpsys activity top|grep ACTIVITY",true).successMsg;

    public static String getCurrentPkgName(){
        String currentPackageName = res.split(" ")[3].split("/")[0];
        return currentPackageName;
    }

    public static String getCurrentActivity(){
        String currentActivity = res.split(" ")[3];
        return currentActivity;
    }
}
