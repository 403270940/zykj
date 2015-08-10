package com.zykj.entities;

/**
 * Created by yli on 2015/8/3.
 */
public class RestoreParameter {
    private int id;
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

    public RestoreParameter(int id, String IMEI, String MAC, String IMSI, String MANU, String MODEL, String GPS, String VERSION, String ANDROIDID, String IP, String PHONE, String TASKNAME) {
        this.id = id;
        this.IMEI = IMEI;
        this.MAC = MAC;
        this.IMSI = IMSI;
        this.MANU = MANU;
        this.MODEL = MODEL;
        this.GPS = GPS;
        this.VERSION = VERSION;
        this.ANDROIDID = ANDROIDID;
        this.IP = IP;
        this.PHONE = PHONE;
        this.TASKNAME = TASKNAME;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getTASKNAME() {
        return TASKNAME;
    }

    public void setTASKNAME(String TASKNAME) {
        this.TASKNAME = TASKNAME;
    }
}
