package com.gzak.utils;

import android.os.Environment;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.graphics.AvoidXfermode;
import android.os.Environment;
import android.util.Log;

import com.gzak.entities.Parameter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;

/**
 * Created by Administrator on 2015/7/6.
 */
public class ConfigUtil {

    private static final String propertyFileName = Environment.getExternalStorageDirectory() + "/config.properties";
    private static Properties properties = null;
    private static HashMap<String,String> modelMap = null;
    private static ArrayList<String> modelList = null;
    private static final String IMEI = "";
    private static final String MAC = "";
    private static final String IMSI = "";
    private static final String MANU = "";
    private static final String MODEl = "";
    private static final String GPS = "";

    private static String curChangshang = null;
    private static List<String> xinghaos = null;

    static{
        try {
            modelList = new ArrayList<String>();
            loadMobile();
            initProperties();
        } catch (Exception e) {
            Log.e("input","",e);
        }
    }

    public static void setProperties(Properties properties){
        ConfigUtil.properties = properties;
    }
    private static boolean initProperties(){
        try {
            File  file = new File(propertyFileName);
            if(file.exists()){
                properties = new Properties();
                properties.load(new FileInputStream(file));
                Log.e("input file", "file exist");
                return true;
            }else{
                Log.e("input file","can not find file");
            }
        } catch (IOException e) {
            Log.e("input", "", e);
        }
        return false;
    }


//    public static void bianliProperties(Properties properties){

//
//    public static void bianli(){
//        Enumeration enu2=properties.propertyNames();
//        while(enu2.hasMoreElements()){
//            String key = (String)enu2.nextElement();
//            Log.e("input properties","key:"+key);
//        }
//    }

    private static boolean loadMobile(){
        String modelFileName = "/assets/models.xml";
        InputStream in = ConfigUtil.class.getResourceAsStream(modelFileName);
        try {
            Document doc = Jsoup.parse(in, "UTF-8","", Parser.xmlParser());
            Elements elements = doc.select("model");
            for(Element element : elements){
                String changshang = element.select("changshang").first().text();
                Elements xinghaoElements = element.select("xinghao");
                for(Element xinghaoElement : xinghaoElements){
                    String xinghao = xinghaoElement.text();
                    modelList.add(changshang + " " + xinghao);
                }
            }
            Log.e("input xml",doc.outerHtml());
        } catch (IOException e) {
            Log.e("input", "", e);
        }
        return true;
    }



    public static void outModel(){
        for(String model : modelList){
            Log.e("input","model:"+ model);
        }

    }


    public static boolean clearProperties(){
        properties.setProperty("IMEI","");
        properties.setProperty("MAC","");
        properties.setProperty("IMSI","");
        properties.setProperty("MANU","");
        properties.setProperty("MODEL","");
        properties.setProperty("VERSION","");
        properties.setProperty("GPS", "");
        return true;
    }

    public static String getServerInfo(){
        //if get false return false
        //if format error return false
        outModel();
        return null;
    }



    public static String get(String key){
        Log.e("input get","key:"+key);
        String result = "";
        try {
            if(properties==null||properties.isEmpty()){
                return "";
            }

            result = (String)properties.get(key);
            if(result == null || result.equals("null")){
                Log.e("input null", key);
                return "";
            }
            Log.e("input key", key);
            Log.e("input value", result);
            if(!ValidationUtil.check(key, result))
                result = "";
        }catch (Exception e){
            Log.e("input e", "error",e);
        }

        return result;
    }

    public static boolean saveProperties(){
        File file = new File(propertyFileName);
        try {
            FileOutputStream fos = new FileOutputStream(file,false);
            properties.store(fos, "");
        } catch (Exception e) {
            Log.e("input","",e);
            Log.e("input", "", e);
        }
        return true;
    }


    public static boolean addParams(Parameter parameter){
//        Log.e("input add","IMEI:" + IMEI);
//        Log.e("input add","MAC:" + MAC);
//        Log.e("input add","IMSI:" + IMSI);
//        Log.e("input add","VERSION:" + VERSION);
//        Log.e("input add", "MODEL:" + MODEL);
//        Log.e("input add", "ANDROIDID:" + ID);
//        Log.e("input add", "GPS:" + GPS);
        String[] modelArray = parameter.getMODEL().split(" ");
        String MANU = modelArray[0].trim();
        String XH = modelArray[1].trim();
        if(properties==null)
            properties = new Properties();
        properties.setProperty("IMEI", parameter.getIMEI());
        properties.setProperty("MAC",parameter.getMAC());
        properties.setProperty("IMSI",parameter.getIMSI());
        properties.setProperty("MANU",parameter.getMANU());
        properties.setProperty("MODEL",parameter.getMODEL());
        properties.setProperty("ANDROIDID",parameter.getANDROIDID());
        properties.setProperty("PHONE",parameter.getPHONE());
        properties.setProperty("SDK",parameter.getSDK());
        saveProperties();
        return true;
    }

    public static boolean addParams(String IMEI,String MAC,String IMSI,String VERSION,String MODEL,String ID,String GPS){
//        Log.e("input add","IMEI:" + IMEI);
//        Log.e("input add","MAC:" + MAC);
//        Log.e("input add","IMSI:" + IMSI);
//        Log.e("input add","VERSION:" + VERSION);
//        Log.e("input add", "MODEL:" + MODEL);
//        Log.e("input add", "ANDROIDID:" + ID);
//        Log.e("input add", "GPS:" + GPS);
        String[] modelArray = MODEL.split(" ");
        String MANU = modelArray[0].trim();
        String XH = modelArray[1].trim();
        if(properties==null)
            properties = new Properties();
        properties.setProperty("IMEI", IMEI);
        properties.setProperty("MAC",MAC);
        properties.setProperty("IMSI",IMSI);
        properties.setProperty("MANU",MANU);
        properties.setProperty("MODEL",XH);
        properties.setProperty("ANDROIDID",ID);
        properties.setProperty("VERSION",VERSION);
        properties.setProperty("GPS",GPS);
        saveProperties();
        return true;
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
        return getRandomString(GlobalValue.num, 15);
    }

    public static String getRandomMAC(){
        String result = "";
        String mac = getRandomString(GlobalValue.hex,12).toUpperCase();
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
        String result = pre+getRandomString("0123",1) + getRandomString(GlobalValue.num,10);
        return result;
    }


    public static String getRandomVersion(){
        String result = "KOT49H.I9508ZMUGNH6";
        return result;
    }

    public static String getRandomModel(){
        String result = "SUMSUNG GT-I9508";
        int size = modelList.size();
        Random random = new Random();
        int i = random.nextInt(size);
        result = modelList.get(i);
        Log.e("input","model:"+ result);
        return result;
    }

    public static String getRandomID(){
        return getRandomString(GlobalValue.lowerCase+GlobalValue.num,16);
    }

    public static String getRandomGPS(){
        String result = "120.104776,30.290787";
        return result;
    }

}
