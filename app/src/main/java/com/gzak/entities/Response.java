package com.gzak.entities;

/**
 * Created by yli on 2015/7/17.
 */
public class Response {

    private int resultCode;
    private String MSG;
    private String IMEI;
    private String MAC;
    private String ANDROIDID;
    private String MODEL;
    private String GPS;
    private String VERSION;
    private String IMSI;
    private String IP;
    private String PHONE;

    public Response(int resultCode, String resultString){

    }

    public Response(int resultCode, String IMEI, String MAC, String ANDROIDID, String MODEL, String GPS, String VERSION, String IMSI, String IP, String PHONE) {
        this.resultCode = resultCode;
        this.IMEI = IMEI;
        this.MAC = MAC;
        this.ANDROIDID = ANDROIDID;
        this.MODEL = MODEL;
        this.GPS = GPS;
        this.VERSION = VERSION;
        this.IMSI = IMSI;
        this.IP = IP;
        this.PHONE = PHONE;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultString() {
        return MSG;
    }

    public void setResultString(String msg) {
        this.MSG = msg;
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

    public String getANDROIDID() {
        return ANDROIDID;
    }

    public void setANDROIDID(String ANDROIDID) {
        this.ANDROIDID = ANDROIDID;
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

    public String getIMSI() {
        return IMSI;
    }

    public void setIMSI(String IMSI) {
        this.IMSI = IMSI;
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

    @Override
    public String toString() {
        if(resultCode == 0){
            return "Response{" +
                    "resultCode=" + resultCode +
                    ", IMEI='" + IMEI + '\'' +
                    ", MAC='" + MAC + '\'' +
                    ", ANDROIDID='" + ANDROIDID + '\'' +
                    ", MODEL='" + MODEL + '\'' +
                    ", GPS='" + GPS + '\'' +
                    ", VERSION='" + VERSION + '\'' +
                    ", IMSI='" + IMSI + '\'' +
                    ", IP='" + IP + '\'' +
                    ", PHONE='" + PHONE + '\'' +
                    '}';
        }else{
            return "Response{" +
                    "resultCode=" + resultCode +
                    ", MSG='" + MSG +
                    '}';
        }

    }
}
