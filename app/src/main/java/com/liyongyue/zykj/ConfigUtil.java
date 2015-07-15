package com.liyongyue.zykj;

import android.util.Log;

import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Random;

/**
 * Created by Administrator on 2015/7/6.
 */
public class ConfigUtil {
    private static Properties properties = new Properties();
    private static final String lowerCase = "abcdefghijklmnopqrstuvwxyz";
    private static final String hex = "0123456789abcdef";
    private static final String upCase = lowerCase.toUpperCase();
    private static final String num = "0123456789";
    public static void init(){


        String fileName = "/assets/config.properties";
        try {
            properties.load(ConfigUtil.class.getResourceAsStream(fileName));
            Enumeration enu2=properties.propertyNames();
            while(enu2.hasMoreElements()){
                String key = (String)enu2.nextElement();
                Log.e("input", "properties key:" + key + "result : " + properties.get(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String get(String key){

        String result = (String)properties.get(key);
        Log.e("input", "get key:" + key + "result:" + result);
        return result;
    }

    public static String getMAC(){
        String result = (String)properties.get("MAC");
        if(result==null){

        }else if(result == "random"){
            String mac = getRandomString(hex,12).toUpperCase();
            result = mac.substring(0,2) + ":";
            result = mac.substring(2,4) + ":";
            result = mac.substring(4,6) + ":";
            result = mac.substring(6,8) + ":";
            result = mac.substring(8,10) + ":";
            result = mac.substring(10,12);
        }
        return result;
    }

    public static String getIMSI(){
        String result = (String)properties.get("IMSI");
        String preYD1 = "46000";
        String preYD2 = "46002";
        String preLT = "46001";
        String preDX = "46003";
        if(result==null){

        }else if(result == "random"){
            String mac = getRandomString(hex,12).toUpperCase();
            result = mac.substring(0,2) + ":";
            result = mac.substring(2,4) + ":";
            result = mac.substring(4,6) + ":";
            result = mac.substring(6,8) + ":";
            result = mac.substring(8,10) + ":";
            result = mac.substring(10,12);
        }
        return result;
    }


    private static String getRandomString(int length) {
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static String getRandomString(String base, int length){
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}
