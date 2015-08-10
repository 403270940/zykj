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

import java.net.URLEncoder;

/**
 * Created by yli on 2015/7/9.
 * 该类是处理网络请求的工具类
 */
public class HttpUtil {
    private static HttpClient httpClient = new DefaultHttpClient();

    static {
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,10000);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,10000);
    }

    public static String requestModiParam(String IMSI) throws Exception{
        String result = null;
        String url = GlobalValue.modiParamURL + "&imsi=" + IMSI;
        Log.e("input","requestURL:"+url);
        try {
            HttpGet get = new HttpGet(url);
            HttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);
            Log.e("input","get end:"+result);
        } catch (Exception e) {
            Log.e("input","",e);
            result = null;
            throw new Exception("联网失败");
        }
        return result;
    }

    public static String confirmModiParam(String IMSI,String PHONE) throws  Exception{
        String result = null;
        String updateurl = GlobalValue.confirmModiParamURL;
        updateurl +=  "&imsi="+IMSI;
        updateurl +=  "&phone="+PHONE;
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
            throw new Exception("联网失败");
        }
        return result;
    }

    public static String randomMobileParam(Parameter parameter) throws  Exception{
        String result = null;
        String updateurl = GlobalValue.randomURL;
        updateurl +=  "&imei="+parameter.getIMEI();
        updateurl +=  "&mac="+parameter.getMAC();
        updateurl +=  "&imsi="+parameter.getIMSI();
        //updateurl +=  "&imsi="+parameter.getIMSI();
        updateurl +=  "&phone="+parameter.getPHONE();
        updateurl +=  "&model="+parameter.getMANU() + "%20" + parameter.getMODEL();
        updateurl +=  "&androidid="+parameter.getANDROIDID();
        updateurl +=  "&version="+parameter.getVERSION();
        updateurl +=  "&ip="+parameter.getIP();
        updateurl +=  "&gps="+parameter.getGPS();
        updateurl +=  "&taskname="+URLEncoder.encode(parameter.getTASKNAME(), "utf-8");

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
            throw new Exception("联网失败");
        }
        return result;
    }

    public static String getRestoreParam(String IMSI, String date, String taskName) throws  Exception{
        String result = null;
        String updateurl = GlobalValue.restoreParamURL;
        updateurl +=  "&imsi="+IMSI;
        updateurl +=  "&date="+date;
        updateurl +=  "&taskname="+taskName;

        Log.e("input","requestRestoreurl:" + updateurl);
        HttpGet get = new HttpGet(updateurl);
        try {
            HttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);
            Log.e("input","get end:"+result);
        } catch (Exception e) {
            Log.e("input","",e);
            result = null;
            throw new Exception("联网失败");
        }
        return result;
    }

    public static String confirmRestoreParam(int ID) throws  Exception{
        String result = null;
        String updateurl = GlobalValue.confirmRestoreParamURL;
        updateurl +=  "&id="+ID;

        Log.e("input","confirmrestoreurl:" + updateurl);
        HttpGet get = new HttpGet(updateurl);
        try {
            HttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);
            Log.e("input","get end:"+result);
        } catch (Exception e) {
            Log.e("input","",e);
            result = null;
            throw new Exception("联网失败");
        }
        return result;
    }


    public static String getPhone(String IMSI) throws  Exception{
        String result = null;
        String updateurl = GlobalValue.getPhone;
        updateurl +=  "&imsi="+IMSI;

        Log.e("input","getPhone url:" + updateurl);
        HttpGet get = new HttpGet(updateurl);
        try {
            HttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);
            Log.e("input","get end:"+result);
        } catch (Exception e) {
            Log.e("input","",e);
            result = null;
            throw new Exception("联网失败");
        }
        return result;
    }


}
