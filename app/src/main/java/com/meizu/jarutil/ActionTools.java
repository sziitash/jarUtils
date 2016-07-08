package com.meizu.jarutil;

import android.content.Context;
import android.content.res.Configuration;
import android.media.MediaScannerConnection;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiManager;
import android.os.SystemClock;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.Until;

import junit.framework.Assert;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Locale;

//import android.support.test.uiautomator.By;
//import android.support.test.uiautomator.UiDevice;
//import android.support.test.uiautomator.Until;

/**
 * Created by libinhui on 2016/5/10.
 */
public class ActionTools {

    private static void findObjTextSwipe(UiDevice xDevice, String xtext, int xcount, boolean xtype){
        int xnum = xDevice.getDisplayWidth()/2;
        int ynum = xDevice.getDisplayHeight()/2;
        int countnum = 0;
        while (true) {
            boolean res = xDevice.wait(Until.hasObject(By.text(xtext)),2000);
            if (!res){
                if(xtype){
                    xDevice.swipe(xnum,ynum+ynum/2,xnum,ynum-ynum/2,50);
                }
                else {
                    xDevice.swipe(xnum,ynum-ynum/2,xnum,ynum+ynum/2,50);
                }
                if (countnum >= xcount){
                    Assert.assertTrue(false);
                    break;
                }
                SystemClock.sleep(1000);
                countnum++;
            }
            else{
                break;
            }
        }
    }

    public static void findObjTextSwipeUp(UiDevice xDevice, String xtext, int xcount){
        findObjTextSwipe(xDevice,xtext,xcount,true);
    }

    public static void findObjTextSwipeDown(UiDevice xDevice, String xtext, int xcount){
        findObjTextSwipe(xDevice,xtext,xcount,false);
    }

    //切换系统语言
    public static void updateSystemLanguage(String language,String country) {
        Locale updateLocale = getLocale(language,country);
        try {
            Object objIActMag, objActMagNative;
            Class clzIActMag = Class.forName("android.app.IActivityManager");
            Class clzActMagNative = Class.forName("android.app.ActivityManagerNative");
            Method mtdActMagNative$getDefault = clzActMagNative.getDeclaredMethod("getDefault");
            // IActivityManager iActMag = ActivityManagerNative.getDefault();
            objIActMag = mtdActMagNative$getDefault.invoke(clzActMagNative);
            // Configuration config = iActMag.getConfiguration();
            Method mtdIActMag$getConfiguration = clzIActMag.getDeclaredMethod("getConfiguration");
            Configuration config = (Configuration) mtdIActMag$getConfiguration.invoke(objIActMag);
            config.locale = updateLocale;
            // iActMag.updateConfiguration(config);
            // 此处需要声明权限:android.permission.CHANGE_CONFIGURATION
            // 会重新调用 onCreate();
            Class[] clzParams = { Configuration.class };
            Method mtdIActMag$updateConfiguration = clzIActMag.getDeclaredMethod(
                    "updateConfiguration", clzParams);
            mtdIActMag$updateConfiguration.invoke(objIActMag, config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Locale getLocale(String language, String country) {
        Locale[] x = getSystemLanguageList();
        for (Locale locale : x) {
            if (locale.getLanguage().equals(language) && locale.getCountry().equals(country)) {
                return locale;
            }
        }
        return null;
    }

    private static Locale[] getSystemLanguageList(){
        //获取Android系统上的语言列表
        Locale mLanguagelist[] = Locale.getAvailableLocales();
        return mLanguagelist;
    }

//    public static void updateMediaScanner(Context cn,String filePathAndName){
//        MediaScannerConnection.scanFile(cn,
//                new String[]{filePathAndName}, null, null);
//    }

    public void pressBackToFileManager(UiDevice mDevice,String packagename,int counts){
        int count = 0;
        while (true){
            mDevice.pressBack();
            SystemClock.sleep(500);
            if(mDevice.getCurrentPackageName().equals(packagename)||count > counts){
                break;
            }
            count++;
        }
    }

    public static void copyFile(Context appContext,String filename) throws IOException {
        OutputStream fos = null;
        InputStream is = null;
        String[] files = {filename};
        byte bits[] = new byte[1024];
        int length;
        for (String afile : files) {
            is = appContext.getResources().getAssets().open(filename);
            fos = new FileOutputStream("/mnt/sdcard/"+afile);
            while ((length = is.read(bits)) != -1) {
                fos.write(bits, 0, length);
            }
            is.close();
            fos.flush();
            fos.close();
        }
        MediaScannerConnection.scanFile(appContext,
                new String[]{"/mnt/sdcard/"+filename}, null, null);
    }

    public static void newSsidConnect(Context cn,String ssid){
        WifiManager wfm = (WifiManager) cn.getSystemService(cn.WIFI_SERVICE);
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = ssid;
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.IEEE8021X);
        WifiEnterpriseConfig enterpriseConfig = new WifiEnterpriseConfig();
        enterpriseConfig.setIdentity("atsms");
        enterpriseConfig.setPassword("autotest123.123");
        enterpriseConfig.setEapMethod(WifiEnterpriseConfig.Eap.PEAP);
        enterpriseConfig.setPhase2Method(WifiEnterpriseConfig.Phase2.MSCHAPV2);
        config.enterpriseConfig = enterpriseConfig;
        Integer networkId = wfm.addNetwork(config);
        wfm.enableNetwork(networkId, true); // this initiates the connection
    }
}
