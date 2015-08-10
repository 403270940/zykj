package com.zykj.utils;

import android.util.Log;

import com.zykj.entities.Parameter;
import com.zykj.entities.PhoneResponse;
import com.zykj.entities.Response;
import com.zykj.entities.RestoreParameter;
import com.zykj.entities.RestoreResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yli on 2015/7/17.
 * 该类是处理返回json结果的工具类
 */
public class JSONUtil {

    public static PhoneResponse getPhoneResponseFromJSON(String json)throws Exception{
        PhoneResponse response = new PhoneResponse();
        try {
            JSONObject joj = new JSONObject(json);
//            Log.e("input","jsonObject:" + joj.toString());
            int resultcode = joj.getInt("error");
            if(resultcode == 0){
                String PHONE = joj.getString("phone");
                response.setCode(resultcode);
                response.setPhone(PHONE);
            }else{
                String msg = joj.getString("msg");
                if(msg == null)return null;
                response.setCode(resultcode);
                response.setMsg(msg);
            }
        }catch (Exception e){
            Log.e("input","",e);
            throw  new Exception("网络请求返回格式错误，返回结果：" + json);
        }
        return response;
    }

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
        RestoreResponse response = new RestoreResponse();
        List<Parameter> parameterList = new ArrayList<Parameter>();
        try {
            JSONObject joj = new JSONObject(json);
            int code = joj.getInt("error");
            response.setCode(code);
            if(code == 0){
                JSONArray dataArray = joj.getJSONArray("data");
                for(int i = 0; i < dataArray.length();i++){
                    JSONObject dataObject = (JSONObject)dataArray.get(i);
                    int id = dataObject.getInt("id");
                    String IMEI = dataObject.getString("imei");
                    String MAC = dataObject.getString("mac");
                    String ANDROIDID = dataObject.getString("androidid");
                    String TMP = dataObject.getString("model");
                    String MANU = TMP.split(" ")[0];
                    String MODEL = "";
                    if(TMP.split(" ").length >= 2){
                        MODEL = TMP.split(" ")[1];
                    }
                    String GPS = dataObject.getString("gps");
                    String VERSION = dataObject.getString("version");
                    String IMSI = dataObject.getString("imsi");
                    String IP = dataObject.getString("ip");
                    String PHONE = dataObject.getString("phone");
                    String TASKNAME = dataObject.getString("taskname");
                    RestoreParameter parameter = new RestoreParameter(id,IMEI,MAC,IMSI,MANU,MODEL,VERSION,PHONE,ANDROIDID,GPS,IP,TASKNAME);
                    response.addParameter(parameter);
                }
            }else{
                String MSG = joj.getString("msg");
                response.setMsg(MSG);
            }
            Log.e("input",joj.toString());
        }catch (Exception e){
            throw new Exception("返回结果格式不正确");
        }


        return response;
    }

}
