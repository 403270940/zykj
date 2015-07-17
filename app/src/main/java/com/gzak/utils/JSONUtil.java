package com.gzak.utils;

import android.util.Log;

import com.gzak.entities.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yli on 2015/7/17.
 */
public class JSONUtil {

    public static void decodeJson(String json){
        try {

            json = "{error:\"0\",imei:\"355065053311003\",mac:\"F2:D2:E5:E8:23:F6\",androidid:\"hche0ifej5hj3e2e\",model:\"HUAWEI G610-T11\",gps:\"120.104776,30.290787\",version:\"G610-T11 V100R001CHNC01B127\",imsi:\"460024577216128\",ip:\"183.128.178.158\",phone:\"18458453750\"}";
            JSONObject joj = new JSONObject(json);
            Log.e("input","jsonObject:" + joj.toString());

            int resultcode = joj.getInt("error");
            String IMEI = joj.getString("imei");
            String MAC = joj.getString("mac");
            String ANDROIDID = joj.getString("androidid");
            String MODEL = joj.getString("model");
            String GPS = joj.getString("gps");
            String VERSION = joj.getString("version");
            String IMSI = joj.getString("imsi");
            String IP = joj.getString("ip");
            String PHONE = joj.getString("phone");
            Log.e("input","resultcode:"+resultcode);
            Log.e("input","IMEI:"+IMEI);
            Log.e("input","MAC:"+MAC);
            Log.e("input","ANDROIDID:"+ANDROIDID);
            Log.e("input","MODEL:"+MODEL);
            Log.e("input","GPS:"+GPS);
            Log.e("input","VERSION:"+VERSION);
            Log.e("input","IMSI:"+IMSI);
            Log.e("input","IP:"+IP);
            Log.e("input","PHONE:"+PHONE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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
            }else if(resultcode == 1){
                String msg = joj.getString("msg");
                response = new Response(resultcode,msg);
            }
        }catch (Exception e){
            Log.e("input","",e);
        }
        return response;
    }
}
