package com.zykj.utils;

import android.util.Log;

import com.zykj.entities.Response;
import com.zykj.entities.RestoreResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yli on 2015/7/17.
 */
public class JSONUtil {

    public static Response getResponseFromJSON(String json)throws Exception{
        Response response = null;
        try {

            JSONObject joj = new JSONObject(json);
//            Log.e("input","jsonObject:" + joj.toString());
            int resultcode = joj.getInt("error");

            if(resultcode==0){
                if(json.contains("msg")){
                    String msg = joj.getString("msg");
                    response = new Response(resultcode,msg);
                    return response;
                }
            String IMEI = joj.getString("imei");
            String MAC = joj.getString("mac");
            String ANDROIDID = joj.getString("androidid");
            String MODEL = joj.getString("model");
            String GPS = joj.getString("gps");
            String VERSION = joj.getString("version");
            String IMSI = joj.getString("imsi");
            String IP = joj.getString("ip");
            String PHONE = joj.getString("phone");
            String TASKNAME = joj.getString("taskname");
            response = new Response(resultcode,IMEI,MAC,ANDROIDID,MODEL,GPS,VERSION,IMSI,IP,PHONE,TASKNAME);
            }else{
                String msg = joj.getString("msg");
                if(msg == null)return null;
                response = new Response(resultcode,msg);
            }
        }catch (Exception e){
            Log.e("input","",e);
            throw  new Exception("网络请求返回格式错误，返回结果：" + json);
        }
        return response;
    }

    public static RestoreResponse getRestoreResponseFromJSON(String json)throws Exception{
        RestoreResponse response = null;
        JSONObject joj = new JSONObject(json);
        Log.e("input",joj.toString());
        return response;
    }

}
