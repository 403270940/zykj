package com.zykj.utils;

import android.util.Log;

import com.zykj.entities.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yli on 2015/7/17.
 */
public class JSONUtil {

    public static Response getResponseFromJSON(String json){
        Response response = null;
        try {

            JSONObject joj = new JSONObject(json);
//            Log.e("input","jsonObject:" + joj.toString());
            int resultcode = joj.getInt("error");

            if(resultcode==0){
            String IMEI = joj.getString("imei");
            String MAC = joj.getString("mac");
            String ANDROIDID = joj.getString("androidid");
            String MODEL = joj.getString("model");
            String GPS = joj.getString("gps");
            String VERSION = joj.getString("version");
            String IMSI = joj.getString("imsi");
            String IP = joj.getString("ip");
            String PHONE = joj.getString("phone");
            response = new Response(resultcode,IMEI,MAC,ANDROIDID,MODEL,GPS,VERSION,IMSI,IP,PHONE);
            }else{
                String msg = joj.getString("msg");
                if(msg == null)return null;
                response = new Response(resultcode,msg);
            }
        }catch (Exception e){
            Log.e("input","",e);
            return null;
        }
        return response;
    }
}
