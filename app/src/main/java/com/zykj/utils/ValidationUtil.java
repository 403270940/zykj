package com.zykj.utils;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yli on 2015/7/9.
 */
public class ValidationUtil {

    private static String IMEIReg = "^\\d{15}$";
    private static String MACReg = "^[0-9a-fA-F]{2}(:[0-9a-fA-F]{2}){5}$";
    private static String IMSIReg = "^4600[0-3]{1}\\d{10}$";
    private static String CSReg = "";
    private static String XHReg = "";
    private static String IDReg = "^[0-9a-z]{16}$";
    private static String GPSReg = "";


    public static boolean check(String key,String result){
        Pattern pattern = null;
        if(key.equals("IMEI")){
            pattern = Pattern.compile(IMEIReg);
        }
        if(key.equals("MAC")){
            pattern = Pattern.compile(MACReg);
        }
        if(key.equals("IMSI")){
            pattern = Pattern.compile(IMSIReg);
        }
        if(key.equals("IMSI")){
           return true;
        }
        if(key.equals("VERSION")){
            return true;
        }
        if(key.equals("MANU")){
            return true;
        }
        if(key.equals("MODEL")){
            return true;
        }
        if(key.equals("ANDROIDID")){
            pattern = Pattern.compile(IDReg);
        }
        if(key.equals("GPS")) {
            return isGPS(result);
        }
        if(pattern == null)
            return false;
        Matcher matcher = pattern.matcher(result);
        if(matcher.find())return true;
        else return false;
    }

    public static boolean isGPS(String gps){
        try{
            String[] gpss = gps.split(",");
            if(gpss.length!= 2) return false;
            float lat = Float.valueOf(gpss[0]);
            float lon = Float.valueOf(gpss[1]);
        }catch (Exception e){
            Log.e("input","gps error",e);
            return false;
        }
        return true;
    }

    public static boolean isMAC(String mac){
        int len = mac.length();
        if(len != 17)return false;
        String[] macs = mac.split(":");
        if(macs.length != 6) return false;

        for(int i = 0; i < 6; i ++){
            if(!GlobalValue.hex.contains(macs[i].charAt(0)+""))return false;
            if(!GlobalValue.hex.contains(macs[i].charAt(1)+""))return false;
        }

        return true;
    }

}
