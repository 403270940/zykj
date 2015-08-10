package com.zykj.utils;

import android.os.Environment;
import android.util.Log;

import com.zykj.entities.Parameter;
import com.zykj.entities.Response;

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
 * Created by yli on 2015/7/30.
 * 该类是用于产生随机参数的工具类
 */
public class RandomUtil {

    private static final String propertyFileName = Environment.getExternalStorageDirectory() + "/shua/config.properties";
    private static Properties properties = null;
    private static HashMap<String,String> modelMap = null;
    private static ArrayList<String> modelList = null;
    private static final String IMEI = "";
    private static final String MAC = "";
    private static final String IMSI = "";
    private static final String MANU = "";
    private static final String MODEl = "";
    private static final String GPS = "";

    static{
        try {
            modelList = new ArrayList<String>();
            loadMobile();
        } catch (Exception e) {
            Log.e("input", "", e);
        }
    }


    private static boolean loadMobile(){
        String modelFileName = "/assets/models.xml";
        InputStream in = ConfigUtil.class.getResourceAsStream(modelFileName);
        try {
            Document doc = Jsoup.parse(in, "UTF-8", "", Parser.xmlParser());
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


    public static Parameter getRandomParameter(){
        String IMEI = getRandomIMEI();
        String MAC = getRandomMAC();
        String IMSI = "";
        String[] tmpModel = getRandomModel().split(" ");
        String MANU = tmpModel[0];
        String MODEL = "";
        for(int i = 1;i < tmpModel.length;i ++ ){
            MODEL += tmpModel[i] + " ";
        }
        MODEL = MODEL.trim();
        String VERSION = getRandomVersion();
        String PHONE = "13888888888";
        String ANDROIDID = getRandomID();
        String GPS = getRandomGPS();
        String IP = "1.1.1.1";
        String TASKNAME = "";
        Parameter parameter =  new Parameter(IMEI,MAC,IMSI,MANU,MODEL,VERSION,PHONE,ANDROIDID,GPS,IP,TASKNAME);
        return  parameter;
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

    private static String IMEICalc(String head){
        if(head.length() != 14)return  "";
        char[] imeiChar=head.toCharArray();
        int resultInt=0;
        for (int i = 0; i < imeiChar.length; i++) {
            int a=Integer.parseInt(String.valueOf(imeiChar[i]));
            i++;
            final int temp=Integer.parseInt(String.valueOf(imeiChar[i]))*2;
            final int b=temp<10?temp:temp-9;
            resultInt+=a+b;
        }
        resultInt%=10;
        resultInt=resultInt==0?0:10-resultInt;

        return head + resultInt;
    }

    public static String getRandomIMEI(){
        String result = "";

        String pre = "35563705";
        String random = getRandomString(GlobalValue.num, 6);
        result = pre + random;
        result = IMEICalc(result);
        return result;
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

    public static String getRandomVersion(){
        List<String> versionList= new ArrayList<String>();
        versionList.add("KOT49H.ZMUGNH6");
        versionList.add("V100R001CHNC01B127");
        versionList.add("H430aR011MCB01B8bc");
        versionList.add("1003020hct.001");
        versionList.add("uhi1000opq0003");
        Random random = new Random();
        int index = random.nextInt(versionList.size());
        return versionList.get(index);
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
        String lat = "1" + getRandomString(GlobalValue.num,2) + "." + getRandomString(GlobalValue.num,6);
        String lon = getRandomString(GlobalValue.num,2) + "." + getRandomString(GlobalValue.num, 6);
        return lat + "," + lon;
    }

}
