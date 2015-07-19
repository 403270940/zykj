package com.zykj.utils;

import android.util.Log;

import com.zykj.entities.Parameter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

/**
 * Created by yli on 2015/7/9.
 */
public class HttpUtil {
    private static HttpClient httpClient = new DefaultHttpClient();

    static {
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,10000);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,10000);
    }
    public static String getTime(){
        return "";
    }


    public static String get(){
        String result = null;
        HttpGet get = new HttpGet(GlobalValue.modiParamURL);
        try {
            HttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);
            Log.e("input","get end:"+result);
        } catch (Exception e) {
            Log.e("input","",e);
            result = null;
        }
        return result;
    }

    public static String updateRandom(Parameter parameter){
        String result = null;
        String updateurl = GlobalValue.randomURL;
        updateurl +=  "&imei="+parameter.getIMEI();
        updateurl +=  "&mac="+parameter.getMAC();
        updateurl +=  "&imsi="+parameter.getIMSI();
        updateurl +=  "&androidid="+parameter.getANDROIDID();
        updateurl +=  "&version="+parameter.getVERSION();
        updateurl +=  "&ip="+parameter.getIP();
        updateurl +=  "&gps="+parameter.getGPS();
        Log.e("input","url:" + updateurl);
        HttpGet get = new HttpGet(updateurl);
        try {
            HttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);
            Log.e("input","get end:"+result);
        } catch (Exception e) {
            Log.e("input","",e);
            result = null;
        }
        return result;
    }

    public static String verify(Parameter parameter){
        String result = null;
        String updateurl = GlobalValue.updateURL;
        updateurl +=  "&imsi="+parameter.getIMSI();
        updateurl +=  "&phone="+parameter.getPHONE();
        Log.e("input","url:" + updateurl);
        HttpGet get = new HttpGet(updateurl);
        try {
            HttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);
            Log.e("input","get end:"+result);
        } catch (Exception e) {
            Log.e("input","",e);
            result = null;
        }
        return result;
    }

    public static String getIP(){
        HttpGet get = new HttpGet("http://www.liyongyue.com/getip.php");
        Log.e("input", "get");
        try {
            HttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            Log.e("input","get end");
            return result.trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
