package com.liyongyue.zykj;

import android.content.res.XmlResourceParser;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.IOException;
import java.io.InputStream;
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

    private static final String IMEI = "";
    private static final String MAC = "";
    private static final String IMSI = "";
    private static final String MANU = "";
    private static final String MODEl = "";
    private static final String GPS = "";

    static{
        String fileName = "/assets/config.properties";
        try {
            properties.load(ConfigUtil.class.getResourceAsStream(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static boolean loadMobile(){
        String modelFileName = "/assets/models.xml";
        InputStream in = ConfigUtil.class.getResourceAsStream(modelFileName);
        try {
            Document doc = Jsoup.parse(in, "UTF-8","", Parser.xmlParser());
            Log.e("input xml",doc.outerHtml());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean clearProperties(){
        properties.setProperty("IMEI","");
        properties.setProperty("MAC","");
        properties.setProperty("IMSI","");
        properties.setProperty("MANU","");
        properties.setProperty("MODEL","");
        properties.setProperty("GPS","");
        return true;
    }

    public static String getServerInfo(){
        //if get false return false
        //if format error return false
        loadMobile();
        return null;
    }



    public static String get(String key){
        String result = "";
        try {
            result = (String)properties.get(key);
            if(result == null || result.equals("null")){
                Log.e("input null", key);
                Log.e("input null",result);
                return "";
            }
            if(!ValidationUtil.check(key,result))
                result = "";
        }catch (Exception e){
            e.printStackTrace();
            Log.e("input e", "error",e);
            Log.e("input e", key);
            Log.e("input e",result);
        }


        return result;
    }


    public static boolean addParams(String IMEI,String MAC,String IMSI,String CS,String XH,String ID,String GPS){
        Log.e("input","IMEI:" + IMEI);
        Log.e("input","MAC:" + MAC);
        Log.e("input","IMSI:" + IMSI);
        Log.e("input","CS:" + CS);
        Log.e("input","XH:" + XH);
        Log.e("input","ANDROIDID:" + ID);
        Log.e("input","GPS:" + GPS);

        return true;
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

    public static String getRandomIMEI(){
        return getRandomString(num, 15);
    }

    public static String getRandomMAC(){
        String result = "";
        String mac = getRandomString(hex,12).toUpperCase();
        result += mac.substring(0,2) + ":";
        result += mac.substring(2,4) + ":";
        result += mac.substring(4,6) + ":";
        result += mac.substring(6,8) + ":";
        result += mac.substring(8,10) + ":";
        result += mac.substring(10,12);
        return result;
    }

    public static String getRandomIMSI(){
        String pre = "4600";
//        String preYD1 = "46000";
//        String preYD2 = "46002";
//        String preLT = "46001";
//        String preDX = "46003";
        String result = pre+getRandomString("0123",1) + getRandomString(num,12);
        return result;
    }


    public static String getRandomCH(){
        String result = "SUMSUNG";
        return result;
    }

    public static String getRandomXH(){
        String result = "GT-I9508";
        return result;
    }

    public static String getRandomID(){
        return getRandomString(lowerCase+num,16);
    }
    public static String getRandomGPS(){
        String result = "120.104776,30.290787";
        return result;
    }

}
