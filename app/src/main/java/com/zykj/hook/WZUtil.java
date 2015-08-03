package com.zykj.hook;

import android.annotation.TargetApi;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import com.zykj.utils.ConfigUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by yli on 2015/6/29.
 * 该类是使用xposed框架修改手机参数类
 */
public class WZUtil implements IXposedHookLoadPackage{

    private final String TAG = "Xposed";
    private XC_LoadPackage.LoadPackageParam mLpp;


    public void log(String s){
        Log.d(TAG, s);
        XposedBridge.log(s);
    }

    //不带参数的方法拦截
    private void hook_method(Class<?> clazz, String methodName, Object... parameterTypesAndCallback)
    {
        try {
            XposedHelpers.findAndHookMethod(clazz, methodName, parameterTypesAndCallback);
        } catch (Exception e) {
            XposedBridge.log(e);
        }
    }

    //不带参数的方法拦截
    private void hook_method(String className, ClassLoader classLoader, String methodName,
                             Object... parameterTypesAndCallback)
    {
        try {
            XposedHelpers.findAndHookMethod(className, classLoader, methodName, parameterTypesAndCallback);
        } catch (Exception e) {
            XposedBridge.log(e);
        }
    }

    //带参数的方法拦截
    private void hook_methods(String className, String methodName, XC_MethodHook xmh)
    {
        try {
            Class<?> clazz = Class.forName(className);

            for (Method method : clazz.getDeclaredMethods())
                if (method.getName().equals(methodName)
                        && !Modifier.isAbstract(method.getModifiers())
                        && Modifier.isPublic(method.getModifiers())) {
                    XposedBridge.hookMethod(method, xmh);
                }
        } catch (Exception e) {
            XposedBridge.log(e);
        }
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        mLpp = loadPackageParam;
//        if(!loadPackageParam.equals("com.liyongyue.getinfo"))return;
        try{
            Class<?> classBuild = XposedHelpers.findClass("android.os.Build", loadPackageParam.classLoader);
            String saveModel        = ConfigUtil.get("MODEL");
            String saveManufacutrer = ConfigUtil.get("MANU");

            if (!saveManufacutrer.equals("null") && !saveManufacutrer.equals("")) XposedHelpers.setStaticObjectField(classBuild, "MANUFACTURER", saveManufacutrer);
            if (!saveModel.equals("null") && !saveModel.equals("")) {
                XposedHelpers.setStaticObjectField(classBuild, "MODEL", saveModel);
                XposedHelpers.setStaticObjectField(classBuild, "BRAND", saveModel);
            }
            String saveVersion = ConfigUtil.get("VERSION");
            if (!saveVersion.equals("null") && !saveVersion.equals("")) XposedHelpers.setStaticObjectField(classBuild, "DISPLAY", saveVersion);

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
                        Log.e("input loc after", param.getResult().toString());
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
            XposedBridge.log("-----------没有保护，所以获取是假地址：" + mLpp.packageName);


            hook_method("android.net.wifi.WifiManager", mLpp.classLoader, "getScanResults",
                    new XC_MethodHook() {
                        /**
                         * Android提供了基于网络的定位服务和基于卫星的定位服务两种
                         * android.net.wifi.WifiManager的getScanResults方法
                         * Return the results of the latest access point scan.
                         * @return the list of access points found in the most recent scan.
                         */
                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            // TODO Auto-generated method stub
                            //super.afterHookedMethod(param);
                            param.setResult(null);//return empty ap list, force apps using gps information
                        }
                    });

            hook_method("android.telephony.TelephonyManager", mLpp.classLoader, "getCellLocation",
                    new XC_MethodHook() {
                        /**
                         * android.telephony.TelephonyManager的getCellLocation方法
                         * Returns the current location of the device.
                         * Return null if current location is not available.
                         */
                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            // TODO Auto-generated method stub
                            //super.afterHookedMethod(param);
                            param.setResult(null);//return empty cell id list
                        }
                    });

            hook_method("android.telephony.TelephonyManager", mLpp.classLoader, "getNeighboringCellInfo",
                    new XC_MethodHook() {
                        /**
                         * android.telephony.TelephonyManager类的getNeighboringCellInfo方法
                         * Returns the neighboring cell information of the device.
                         */
                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            // TODO Auto-generated method stub
                            //super.afterHookedMethod(param);
                            param.setResult(null);//// return empty neighboring cell info list
                        }
                    });

            hook_methods("android.location.LocationManager", "requestLocationUpdates",
                    new XC_MethodHook() {
                        /**
                         * android.location.LocationManager类的requestLocationUpdates方法
                         * 其参数有4个：
                         * String provider, long minTime, float minDistance,LocationListener listener
                         * Register for location updates using the named provider, and a pending intent
                         */
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                            if (param.args.length == 4 && (param.args[0] instanceof String)) {
                                //位置监听器,当位置改变时会触发onLocationChanged方法
                                LocationListener ll = (LocationListener) param.args[3];

                                Class<?> clazz = LocationListener.class;
                                Method m = null;
                                for (Method method : clazz.getDeclaredMethods()) {
                                    if (method.getName().equals("onLocationChanged")) {
                                        m = method;
                                        break;
                                    }
                                }

                                try {
                                    if (m != null) {
                                        //  mSettings.reload();

                                        Object[] args = new Object[1];
                                        Location l = new Location(LocationManager.GPS_PROVIDER);
                                        String saveValue = ConfigUtil.get("GPS");
                                        String[] gps = saveValue.split(",");
                                        double lat = Double.valueOf(gps[0]);
                                        double lon = Double.valueOf(gps[1]);
                                        l.setLatitude(lat);
                                        l.setLongitude(lon);
                                        args[0] = l;
                                        //invoke onLocationChanged directly to pass location infomation
                                        m.invoke(ll, args);
                                        log("fake location: " + lat + ", " + lon);
                                        }
                                    }catch(Exception e){
                                        XposedBridge.log(e);
                                    }
                                }
                            }
                        }

                        );


                        hook_methods("android.location.LocationManager","getGpsStatus",
                                             new XC_MethodHook() {
                            /**
                             * android.location.LocationManager类的getGpsStatus方法
                             * 其参数只有1个：GpsStatus status
                             * Retrieves information about the current status of the GPS engine.
                             * This should only be called from the {@link GpsStatus.Listener#onGpsStatusChanged}
                             * callback to ensure that the data is copied atomically.
                             *
                             */
                            @Override
                            protected void afterHookedMethod (MethodHookParam param)throws Throwable
                            {
                                GpsStatus gss = (GpsStatus) param.getResult();
                                if (gss == null)
                                    return;

                                Class<?> clazz = GpsStatus.class;
                                Method m = null;
                                for (Method method : clazz.getDeclaredMethods()) {
                                    if (method.getName().equals("setStatus")) {
                                        if (method.getParameterTypes().length > 1) {
                                            m = method;
                                            break;
                                        }
                                    }
                                }

                                //access the private setStatus function of GpsStatus
                                m.setAccessible(true);

                                //make the apps belive GPS works fine now
                                int svCount = 5;
                                int[] prns = {1, 2, 3, 4, 5};
                                float[] snrs = {0, 0, 0, 0, 0};
                                float[] elevations = {0, 0, 0, 0, 0};
                                float[] azimuths = {0, 0, 0, 0, 0};
                                int ephemerisMask = 0x1f;
                                int almanacMask = 0x1f;

                                //5 satellites are fixed
                                int usedInFixMask = 0x1f;

                                try {
                                    if (m != null) {
                                        m.invoke(gss, svCount, prns, snrs, elevations, azimuths, ephemerisMask, almanacMask, usedInFixMask);
                                        param.setResult(gss);
                                    }
                                } catch (Exception e) {
                                    XposedBridge.log(e);
                                }
                            }
                        }

                        );
                    }catch (Exception e){
            Log.e("input","HOOK Error",e);
        }

    }






}
