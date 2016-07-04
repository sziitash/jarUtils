package com.meizu.jarutil;

/**
 * Created by libinhui on 2016/5/24.
 */
public class ShellTools {
    private static String res = ShellUtils.execCommand("dumpsys activity top|grep ACTIVITY",true).successMsg;

    public static String getCurrentPkgName(){
        String[] ress = res.split("/")[0].split(" ");
        int count = ress.length;
        String currentPackageName = ress[count-1];
        return currentPackageName;
    }

    public static String getCurrentActivity(){
//        String currentActivity = res.split(" ")[3];
        String[] ress = res.split("/")[1].split(" ");
        String currentActivity = ress[0];
        return currentActivity;
    }
}
