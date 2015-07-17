package com.gzak.hook;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import com.gzak.utils.ConfigUtil;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by yli on 2015/6/29.
 */
public class WZUtil implements IXposedHookLoadPackage{
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if ( loadPackageParam.packageName.equals("com.liyongyue.getinfo") ){
            Log.e("input hook","enter hook");
        try{
            Class<?> classBuild = XposedHelpers.findClass("android.os.Build", loadPackageParam.classLoader);
            String saveModel        = ConfigUtil.get("MODEL");
            String saveManufacutrer = ConfigUtil.get("MANU");
            String saveVersion = ConfigUtil.get("VERSION");
            if (!saveManufacutrer.equals("null") && !saveManufacutrer.equals("")) XposedHelpers.setStaticObjectField(classBuild, "MANUFACTURER", saveManufacutrer);
            if (!saveModel.equals("null") && !saveModel.equals("")) {
                XposedHelpers.setStaticObjectField(classBuild, "MODEL", saveModel);
                XposedHelpers.setStaticObjectField(classBuild, "BRAND", saveModel);
            }
            if (!saveVersion.equals("null") && !saveVersion.equals("")) XposedHelpers.setStaticObjectField(classBuild, "DISPLAY", saveVersion);
//            Class<?> classSetting = XposedHelpers.findClass("android.provider.Setting.System",loadPackageParam.classLoader);
//            String saveAndroidId = "1234567890abcdef";//len is 16
//            if (!saveAndroidId.equals("null") && !saveAndroidId.equals("")) XposedHelpers.setStaticObjectField(classSetting, "ANDROID_ID", saveAndroidId);

//            Class<?> classVERSION = XposedHelpers.findClass("android.os.Build.VERSION",loadPackageParam.classLoader);
//            String saveSDK = ConfigUtil.get("SDK");
//            String saveVersion = ConfigUtil.get("VERSION");
//            if (!saveSDK.equals("null") && !saveSDK.equals("")) XposedHelpers.setStaticObjectField(classVERSION, "SDK", saveSDK);
//            if (!saveVersion.equals("null") && !saveVersion.equals("")) XposedHelpers.setStaticObjectField(classVERSION, "RELEASE", saveVersion);

            Class<?> classTelephonyManager = XposedHelpers.findClass("android.telephony.TelephonyManager", loadPackageParam.classLoader);
            Class<?> classWifiInfo         = XposedHelpers.findClass("android.net.wifi.WifiInfo", loadPackageParam.classLoader);
            Class<?> classSettingsSecure   = XposedHelpers.findClass("android.provider.Settings.Secure", loadPackageParam.classLoader);
            Class<?> classLocationManager = XposedHelpers.findClass("android.location.LocationManager", loadPackageParam.classLoader);
            XposedHelpers.findAndHookMethod(classTelephonyManager, "getDeviceId", new XC_MethodHook()
            {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    String saveValue = ConfigUtil.get("IMEI");
                    if (!saveValue.equals("null") && !saveValue.equals("")) param.setResult(saveValue);
                    super.afterHookedMethod(param);
                }
            });

            XposedHelpers.findAndHookMethod(classTelephonyManager, "getSubscriberId", new XC_MethodHook()
            {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    String saveValue = ConfigUtil.get("IMSI");
                    if (!saveValue.equals("null") && !saveValue.equals("")) param.setResult(saveValue);
                    super.afterHookedMethod(param);
                }
            });

            XposedHelpers.findAndHookMethod(classWifiInfo, "getMacAddress", new XC_MethodHook()
            {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    String saveValue = ConfigUtil.get("MAC");
                    if (!saveValue.equals("null") && !saveValue.equals("")) param.setResult(saveValue);
                    super.afterHookedMethod(param);
                }
            });

            XposedHelpers.findAndHookMethod(classLocationManager, "getLastKnownLocation" , String.class, new XC_MethodHook()
            {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    if(param.getResult() != null&&param!=null)
                        Log.e("input loc before",param.getResult().toString());
                    else
                        Log.e("input loc before","location is null");
                    String saveValue = ConfigUtil.get("GPS");
                    if (!saveValue.equals("null")  && !saveValue.equals("")){
                        String[] gps = saveValue.split(",");
                        double lat = Double.valueOf(gps[0]);
                        double lon = Double.valueOf(gps[1]);
                        Location location = new Location(LocationManager.GPS_PROVIDER);
                        location.setLatitude(lat);
                        location.setLongitude(lon);
                        location.setAccuracy(70);
                        location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                        param.setResult(location);
                    }
                    if(param.getResult() != null&&param!=null)
                        Log.e("input loc after",param.getResult().toString());
                    super.afterHookedMethod(param);
                }
            });

            XposedHelpers.findAndHookMethod(classSettingsSecure, "getString", android.content.ContentResolver.class, String.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    String saveValue = ConfigUtil.get("ANDROIDID");
                    if (param.args[1].equals("android_id") && !saveValue.equals("null") && !saveValue.equals(""))
                        param.setResult(saveValue);
                    Log.e("input after hook", param.getResult().toString());
                }
            });
//            Class<?> classLocationListener = XposedHelpers.findClass("android.location.LocationListener", loadPackageParam.classLoader);
//            XposedHelpers.findAndHookMethod(classLocationListener, "onLocationChanged", android.location.Location.class,new XC_MethodHook() {
//                @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//                @Override
//                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                    Log.e("input loc before",param.args[0].toString());
//                    String saveValue = ConfigUtil.get("GPS");
//                    if (!saveValue.equals("null")  && !saveValue.equals("")){
//                        String[] gps = saveValue.split(",");
//                        double lat = Double.valueOf(gps[0]);
//                        double lon = Double.valueOf(gps[1]);
//                        Location location = new Location(LocationManager.GPS_PROVIDER);
//                        location.setLatitude(lat);
//                        location.setLongitude(lon);
//                        location.setAccuracy(70);
//                        location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
//                        param.args[0] = location;
//                    }
//                    Log.e("input loc after",param.args[0].toString());
//                }
//
//            });
        }catch (Exception e){
            Log.e("input","HOOK Error",e);
        }
    }
    }
}
