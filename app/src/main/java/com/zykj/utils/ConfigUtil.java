package com.zykj.utils;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;

import android.util.Log;

import com.zykj.activities.Param;
import com.zykj.entities.Parameter;
import com.zykj.entities.PhoneResponse;
import com.zykj.entities.Response;
import com.zykj.entities.RestoreResponse;

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
import java.util.Properties;

/**
 * Created by Administrator on 2015/7/6.
 * 该类是用于处理参数信息的工具类
 */
public class ConfigUtil {


    private static  String propertyFileName;
    private static String phoneFile;
    private static Properties properties = null;
    private static HashMap<String,String> modelMap = null;
    private static ArrayList<String> modelList = null;
    private static final String IMEI = "";
    private static final String MAC = "";
    private static final String IMSI = "";
    private static final String MANU = "";
    private static final String MODEl = "";
    private static final String GPS = "";
    private static final String TASKNAME = "";

    static{
        try {
            propertyFileName = Environment.getExternalStorageDirectory() + "/shua/config.properties";
            phoneFile = Environment.getExternalStorageDirectory() + "/shua/phone.ini";
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

    public static boolean savePhone(String phone){
        File file = new File(phoneFile);
        if(!file.exists())
            file.getParentFile().mkdirs();
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(phone.getBytes());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            Log.e("input","",e);
        }
        return true;
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
        if(in == null){
            Log.e("input", "models can not be found");
        }
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

//    public static Parameter getServerInfo(String IMSI) throws  Exception{
//        //if get false return false
//        //if format error return false
//        Parameter parameter = null;
//
//        String result = HttpUtil.requestModiParam(IMSI);
//        Log.e("input", "http result:" + result);
//        if(result == null || result.equals("null")){
//            throw new Exception("网络请求返回为空");
//        }
//        Response response = JSONUtil.getResponseFromJSON(result);
//        if(response.getResultCode() != 0){
//            return null;
//        }else{
//            String model = response.getMODEL();
//            String []tmp = model.split(" ");
//            int count = tmp.length;
//            if(count < 2) throw  new Exception("model 格式错误");
//
//            String MANU = tmp[0].trim();
//            String XH = "";
//            for(int i = 1;i < count; i ++){
//                XH = tmp[i].trim() + " ";
//            }
//            XH = XH.trim();
//            parameter = new Parameter(response.getIMEI(),response.getMAC(),response.getIMSI(),MANU,XH,response.getVERSION(),response.getPHONE(),response.getANDROIDID(),response.getGPS(),response.getIP(),response.getTASKNAME());
//        }
//        return parameter;
//    }

    public static Response getServerParam(String IMSI) throws Exception{
        Response response = null;
        String result = HttpUtil.requestModiParam(IMSI);
        response = JSONUtil.getResponseFromJSON(result);
        return response;
    }
    public static Response confirmParam(Parameter parameter) throws Exception{
        Response response = null;
        String result = HttpUtil.confirmModiParam(parameter.getIMSI(), parameter.getPHONE());
        response = JSONUtil.getResponseFromJSON(result);
        return response;
    }

    public static Response uploadRandomParam(Parameter parameter) throws Exception{
        Response response = null;
        String result = HttpUtil.randomMobileParam(parameter);
        response = JSONUtil.getResponseFromJSON(result);
        return response;
    }

    public static RestoreResponse getRestoreParam(String IMSI,String date,String taskname) throws Exception{
        RestoreResponse restoreResponse = null;
        String result = HttpUtil.getRestoreParam(IMSI, date, taskname);
        restoreResponse = JSONUtil.getRestoreResponseFromJSON(result);
        return restoreResponse;
    }

    public static Response confirmRestoreParam(int ID) throws Exception{
        Response response = null;
        String result = HttpUtil.confirmRestoreParam(ID);
        response = JSONUtil.getResponseFromJSON(result);
        return response;
    }
    public static PhoneResponse getPhone(String IMSI) throws Exception{
        PhoneResponse response = null;
        String result = HttpUtil.getPhone(IMSI);
        response = JSONUtil.getPhoneResponseFromJSON(result);
        return response;
    }
//    public static Response updateServerInfo(Parameter parameter,int type) throws  Exception{
//        //if get false return false
//        //if format error return false
//        String result = "";
//        if(type == 1){//verify parameter
//            result = HttpUtil.confirmModiParam(parameter.getIMSI(),parameter.getPHONE());
//        }
//        if(type == 0){
//            result = HttpUtil.randomMobileParam(parameter);
//        }
//
//        Log.e("input","http result:"+result);
//        if(result==null||result.equals("")|| result.equals("null")){
//            throw new Exception("网络请求返回为空");
//        }
//
//        //{error:"0",msg:"����ɹ�"}
//        Response response = JSONUtil.getResponseFromJSON(result);
//
//        return response;
////        outModel();
//
//    }

    public static String get(String key){

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

        }catch (Exception e){
            Log.e("input e", "error",e);
        }
        Log.e("input get","key:"+key + "result :" + result);
        return result;
    }

    public static boolean saveProperties(){
        File file = new File(propertyFileName);
        if(!file.exists())
            file.getParentFile().mkdirs();
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
        if(properties==null)
            properties = new Properties();
        properties.setProperty("IMEI", parameter.getIMEI());
        properties.setProperty("MAC",parameter.getMAC());
        properties.setProperty("IMSI",parameter.getIMSI());
        properties.setProperty("MANU",parameter.getMANU());
        properties.setProperty("MODEL",parameter.getMODEL());
        properties.setProperty("ANDROIDID",parameter.getANDROIDID());
        properties.setProperty("VERSION",parameter.getVERSION());
        properties.setProperty("IP",parameter.getIP());
        properties.setProperty("PHONE",parameter.getPHONE());
        properties.setProperty("GPS", parameter.getGPS());
        properties.setProperty("TASKNAME", parameter.getTASKNAME());
        saveProperties();
        return true;
    }


}
