package com.zykj.utils;

/**
 * Created by yli on 2015/7/9.
 */
public class GlobalValue {
        //&imei=355065153311231&mac=F2:DD:EF:E8:13:F6&androidid=hche0ifej5hj3e2e&model=HUAWEIG610-T11&gps=120.104776,30.290787&version=G610-T11V100R001CHNC01B127&ip=183.128.174.125&imsi=460077577717831&phone=15757784615
    public static final String lowerCase = "abcdefghijklmnopqrstuvwxyz";
    public static final String hex = "0123456789abcdef";
    public static final String upCase = lowerCase.toUpperCase();
    public static final String num = "0123456789";
    public static String modiParamURL = "http://115.236.65.83:8080/topup/mobileParameterServlet?method=requestModiParam";
    public static String confirmModiParamURL =  "http://115.236.65.83:8080/topup/mobileParameterServlet?method=confirmModiParam";
    public static String randomURL = "http://115.236.65.83:8080/topup/mobileParameterServlet?method=randomMobileParam";
    public static String restoreParamURL ="http://115.236.65.83:8080/topup/mobileParameterServlet?method=requestRestoreParam";
    public static String confirmRestoreParamURL = "http://115.236.65.83:8080/topup/mobileParameterServlet?method=confirmRestoreParam";
}
