package com.gzak.entities;

/**
 * Created by yli on 2015/7/17.
 */
public class Parameter {

    //需要模拟信息有：IMEI号，手机号，SIM卡串口号，符合iso标注的国家码，
    // SIM卡提供商的移动国家码，服务运营商的名称，
    // 手机型号，SDK版本号，操作系统版本，手机MAC地址
    private String IMEI;
    private String MAC;
    private String IMSI;
    private String MANU;
    private String MODEL;
    private String GPS;
    private String VERSION;
    private String ANDROIDID;
    private String IP;
    private String PHONE;

    public Parameter(String IMEI, String MAC, String IMSI, String MANU, String MODEL, String VERSION, String PHONE,String ANDROIDID, String GPS,String IP) {
        this.IMEI = IMEI;
        this.MAC = MAC;
        this.IMSI = IMSI;
        this.MANU = MANU;
        this.MODEL = MODEL;
        this.PHONE = PHONE;
        this.VERSION = VERSION;
        this.ANDROIDID = ANDROIDID;
        this.GPS = GPS;
        this.IP = IP;
    }


    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public String getIMSI() {
        return IMSI;
    }

    public void setIMSI(String IMSI) {
        this.IMSI = IMSI;
    }

    public String getMANU() {
        return MANU;
    }

    public void setMANU(String MANU) {
        this.MANU = MANU;
    }

    public String getMODEL() {
        return MODEL;
    }

    public void setMODEL(String MODEL) {
        this.MODEL = MODEL;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getANDROIDID() {
        return ANDROIDID;
    }

    public void setANDROIDID(String ANDROIDID) {
        this.ANDROIDID = ANDROIDID;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getGPS() {
        return GPS;
    }

    public void setGPS(String GPS) {
        this.GPS = GPS;
    }

    public String getVERSION() {
        return VERSION;
    }

    public void setVERSION(String VERSION) {
        this.VERSION = VERSION;
    }
}
