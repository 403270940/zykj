package com.zykj.entities;

/**
 * Created by yli on 2015/7/17.
 * 该类是参数信息的实体类
 */
public class Parameter {


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
    private String TASKNAME;

    public Parameter(String IMEI, String MAC, String IMSI, String MANU, String MODEL, String VERSION, String PHONE,String ANDROIDID, String GPS,String IP,String TASKNAME) {
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
        this.TASKNAME = TASKNAME;
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

    public String getTASKNAME() {
        return TASKNAME;
    }

    public void setTASKNAME(String TASKNAME) {
        this.TASKNAME = TASKNAME;
    }
}
