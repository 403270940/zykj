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
        JSONObject joj = new JSONObject(json);
        int resultcode = getIntFromObject(joj, "error");
        if(resultcode==0){
            if(json.contains("msg")){
                String msg = getStringFromObject(joj, "msg");
                response = new Response(resultcode,msg);
                return response;
            }
            String IMEI = getStringFromObject(joj, "imei");
            String MAC = getStringFromObject(joj, "mac");
            String ANDROIDID = getStringFromObject(joj, "androidid");
            String MODEL = getStringFromObject(joj, "model");
            String GPS = getStringFromObject(joj, "gps");
            String VERSION = getStringFromObject(joj, "version");
            String IMSI = getStringFromObject(joj, "imsi");
            String IP = getStringFromObject(joj, "ip");
            String PHONE = getStringFromObject(joj, "phone");
            String TASKNAME = getStringFromObject(joj, "taskname");
            response = new Response(resultcode,IMEI,MAC,ANDROIDID,MODEL,GPS,VERSION,IMSI,IP,PHONE,TASKNAME);
        }else{
            String msg = getStringFromObject(joj,"msg");
            if(msg == null)return null;
            response = new Response(resultcode,msg);
        }
        return response;
    }

    private static int getIntFromObject(JSONObject obj,String key) throws  Exception{
        int result = 0;
        try {
            result = obj.getInt(key);
        } catch (JSONException e) {
            throw new Exception("数据格式错误： key为：" + key);
        }
        return result;
    }

    private static String getStringFromObject(JSONObject obj,String key) throws  Exception{
        String result = "";
        try {
            result = obj.getString(key);
        } catch (JSONException e) {
            throw new Exception("数据格式错误： key为：" + key);
        }
        return result;
    }

    public static RestoreResponse getRestoreResponseFromJSON(String json)throws Exception{
        RestoreResponse response = new RestoreResponse();
        List<Parameter> parameterList = new ArrayList<Parameter>();
        JSONObject joj = new JSONObject(json);
        int code = getIntFromObject(joj,"error");
        response.setCode(code);
        if(code == 0){
            JSONArray dataArray = joj.getJSONArray("data");
            for(int i = 0; i < dataArray.length();i++){
                JSONObject dataObject = (JSONObject)dataArray.get(i);
                int id = getIntFromObject(dataObject, "id");
                String IMEI = getStringFromObject(dataObject, "imei");
                String MAC = getStringFromObject(dataObject, "mac");
                String ANDROIDID = getStringFromObject(dataObject, "androidid");
                String TMP = getStringFromObject(dataObject, "model");
                String MANU = ConfigUtil.getManu(TMP);
                String MODEL = MODEL = ConfigUtil.getModel(TMP);

                String GPS = getStringFromObject(dataObject,"gps");
                String VERSION = getStringFromObject(dataObject, "version");
                String IMSI = getStringFromObject(dataObject, "imsi");
                String IP = getStringFromObject(dataObject, "ip");
                String PHONE = getStringFromObject(dataObject, "phone");
                String TASKNAME = getStringFromObject(dataObject,"taskname");
                RestoreParameter parameter = new RestoreParameter(id, IMEI, MAC, IMSI, MANU,  MODEL, GPS, VERSION,  ANDROIDID,  IP,  PHONE,TASKNAME);
                response.addParameter(parameter);
            }
        }else{
            String MSG = getStringFromObject(joj, "msg");
            response.setMsg(MSG);
        }

        return response;
    }

}
