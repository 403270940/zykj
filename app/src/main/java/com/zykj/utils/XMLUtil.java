package com.zykj.utils;

import android.os.Environment;
import android.util.Log;

import com.zykj.entities.Parameter;

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
import java.util.List;

/**
 * Created by yli on 2015/7/17.
 */
public class XMLUtil {

//    public static List<Parameter> getTaskList(){
//        List<Parameter> taskParameters = null;
//        String fileName = Environment.getDataDirectory() + "/task.xml";
//        try {
//            InputStream in = new FileInputStream(fileName);
//            Document doc = Jsoup.parse(in, "utf-8", "", Parser.xmlParser());
//            Element phones = doc.select("phones").first();
//            if(phones == null)
//                return null;
//            Elements phoneElements = phones.select("phone");
//            if(phoneElements==null || phoneElements.size()<=0)return null;
//            taskParameters = new ArrayList<Parameter>();
//            for(Element phone : phoneElements){
//                String IMEI = phone.select("IMEI").first().text();
//                String MAC = phone.select("MAC").first().text();
//                String IMSI = phone.select("IMSI").first().text();
//                String MANU = phone.select("IMEI").first().text();
//                String MODEL = phone.select("MODEL").first().text();
//                String VERSION = phone.select("VERSION").first().text();
//                String PHONE = phone.select("PHONE").first().text();
//                String ANDROIDID = phone.select("ANDROIDID").first().text();
//                String GPS = phone.select("GPS").first().text();
//                String IP = phone.select("IP").first().text();
////                (String IMEI, String MAC, String IMSI, String MANU, String MODEL, String VERSION, String PHONE,String ANDROIDID, String GPS,String IP)
//                Parameter parameter = new Parameter(IMEI,MAC,IMSI,MANU,MODEL,VERSION,PHONE,ANDROIDID,GPS,IP);
//                taskParameters.add(parameter);
//            }
//            in.close();
//        } catch (Exception e) {
//            Log.e("input", "", e);
//
//        }
//        return taskParameters;
//    }

    public static boolean addParameter(Parameter parameter){
        List<Parameter> taskParameters = null;
        String fileName = Environment.getDataDirectory() + "/task.xml";
        File file = new File(fileName);
        if(!file.exists()){

        }
        try {
            InputStream in = new FileInputStream(fileName);
            Document doc = Jsoup.parse(in, "utf-8", "", Parser.xmlParser());
            in.close();
            Element phones = doc.select("phones").first();
            Element phone = doc.createElement("phone");
// (String IMEI, String MAC, String IMSI, String MANU, String MODEL, String VERSION, String PHONE,String ANDROIDID, String GPS,String IP)
            Element IMEI = phone.appendElement("IMEI");
            IMEI.appendText(parameter.getIMEI());
            Element MAC = phone.appendElement("MAC");
            MAC.appendText(parameter.getMAC());
            Element IMSI = phone.appendElement("IMSI");
            IMSI.appendText(parameter.getIMSI());
            Element MANU = phone.appendElement("MANU");
            MANU.appendText(parameter.getMANU());
            Element MODEL = phone.appendElement("MODEL");
            MODEL.appendText(parameter.getMODEL());
            Element VERSION = phone.appendElement("VERSION");
            VERSION.appendText(parameter.getVERSION());
            Element PHONENUM = phone.appendElement("PHONENUM");
            PHONENUM.appendText(parameter.getPHONE());
            Element ANDROIDID = phone.appendElement("ANDROIDID");
            ANDROIDID.appendText(parameter.getANDROIDID());
            Element GPS = phone.appendElement("GPS");
            GPS.appendText(parameter.getGPS());
            Element IP = phone.appendElement("IP");
            IP.appendText(parameter.getIP());

            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(doc.toString().getBytes());
        } catch (Exception e) {
            Log.e("input", "", e);
        }
        return true;
    }

    private static boolean loadMobile(List<String> modelList){
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
            Log.e("input xml", doc.outerHtml());
        } catch (IOException e) {
            Log.e("input", "", e);
        }
        return true;
    }

}
