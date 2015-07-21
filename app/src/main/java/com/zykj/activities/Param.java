package com.zykj.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.liyongyue.zykj.R;
import com.zykj.entities.Parameter;
import com.zykj.entities.Response;
import com.zykj.utils.ConfigUtil;
import com.zykj.utils.XMLUtil;


public class Param extends ActionBarActivity {

    String IMEI = "";
    String MAC = "";
    String IMSI = "";
    String VERSION = "";
    String MANU = "";
    String MODEL = "";
    String ID = "";
    String GPS = "";
    String IP = "0.0.0.0";
    String PHONE = "000000000";
    int code = 0;
    Object obj = null;
    int type = 0;
    private TextView showTextView  = null;
    private Button OKButton = null;
    private Button CancelButton = null;
    private Button AllRandomButton = null;
    private Button ServerGetButton = null;

    private Handler handler =new Handler(){
        @Override
        //������Ϣ���ͳ�����ʱ���ִ��Handler���������
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.what == 0) {
                Parameter parameter = (Parameter) msg.obj;
                if (parameter == null) {
                    Toast.makeText(getApplicationContext(), "获取服务器参数失败", Toast.LENGTH_LONG).show();
                    return;
                }
                IMEI = parameter.getIMEI();
                MAC = parameter.getMAC();
                IMSI = parameter.getIMSI();
                VERSION = parameter.getVERSION();
                MANU = parameter.getMANU();
                MODEL = parameter.getMODEL();
                ID = parameter.getANDROIDID();
                GPS = parameter.getGPS();
                IP = parameter.getIP();
                PHONE = parameter.getPHONE();
                String info = "";
                info += "IMEI:" + IMEI + "\n";
                info += "MAC:" + MAC + "\n";
                info += "IMSI:" + IMSI + "\n";
                info += "VERSION:" + VERSION + "\n";
                info += "MANU:" + MANU + "\n";
                info += "MODEL:" + MODEL + "\n";
                info += "ID:" + ID + "\n";
                info += "GPS:" + GPS + "\n";
                info += "IP:" + IP + "\n";
                info += "PHONE:" + PHONE;
                type = 1;
                Toast.makeText(getApplicationContext(), "获取服务器参数成功", Toast.LENGTH_LONG).show();
                showTextView.setText(info);

            }else if(msg.what == 1){

                Response response = (Response)msg.obj;
                if(response != null){
                    Log.e("input","result String " + response);
                    if(response.getResultCode() == 0){
                        Toast.makeText(getApplicationContext(), response.getResultString(), Toast.LENGTH_LONG).show();

                    }else if(response.getResultCode() == 1){
                        Toast.makeText(getApplicationContext(), response.getResultString(), Toast.LENGTH_LONG).show();
                    }else if(response.getResultCode() == 2){
                        Toast.makeText(getApplicationContext(), response.getResultString(), Toast.LENGTH_LONG).show();
                    } else{
                        Toast.makeText(getApplicationContext(), "上传服务器失败，未知错误", Toast.LENGTH_LONG).show();
                    }
                } else
                    Toast.makeText(getApplicationContext(), "网络请求失败", Toast.LENGTH_LONG).show();
            }else if(msg.what == 2){
                Exception e = (Exception)msg.obj;
                Toast.makeText(getApplicationContext(),e.getMessage() , Toast.LENGTH_LONG).show();
            }

        }
    };


    Thread thread =  new Thread(){
        @Override
        public void run(){
            try{
            if(code == 0){
                Parameter parameter = ConfigUtil.getServerInfo();
                //handler.sendEmptyMessage(0);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = parameter;
                handler.sendMessage(msg);

            }else if(code == 1){
                Response result = ConfigUtil.updateServerInfo((Parameter)obj,type);
//            handler.sendEmptyMessage(0);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                handler.sendMessage(msg);
            }
            }catch (Exception e){
                Message msg = new Message();
                msg.what = 2;
                msg.obj = e;
                handler.sendMessage(msg);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_param);


        showTextView = (TextView)findViewById(R.id.ShowTextView);
        OKButton = (Button)findViewById(R.id.OKButton);
        CancelButton = (Button)findViewById(R.id.CancelButton);
        AllRandomButton = (Button)findViewById(R.id.ALLRandomButton);
        ServerGetButton = (Button)findViewById(R.id.ServerGetButton);


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){

                    case R.id.OKButton:
                        new AlertDialog.Builder(Param.this).setTitle("确认修改吗？")
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // �����ȷ�ϡ���Ĳ���
                                        saveAllParam();
                                        Param.this.finish();

                                    }
                                })
                                .setNegativeButton("返回", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();


                        break;

                    case R.id.CancelButton:
                        new AlertDialog.Builder(Param.this).setTitle("确认重置吗")
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // �����ȷ�ϡ���Ĳ���
                                        initParam();

                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // ��������ء���Ĳ���,���ﲻ����û���κβ���
                                    }
                                }).show();

                        break;

                    case R.id.ALLRandomButton:
                       getRandomInfo();
                        break;

                    case R.id.ServerGetButton:

                        new AlertDialog.Builder(Param.this).setTitle("确认从服务获取参数吗？")
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // �����ȷ�ϡ���Ĳ���
                                        code = 0;
                                        new Thread(thread).start();

                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // ��������ء���Ĳ���,���ﲻ����û���κβ���
                                    }
                                }).show();

                        break;

                }
            }
        };

        initParam();

        AllRandomButton.setOnClickListener(onClickListener);
        OKButton.setOnClickListener(onClickListener);
        CancelButton.setOnClickListener(onClickListener);
        ServerGetButton.setOnClickListener(onClickListener);

    }

    private void getRandomInfo(){
        IMEI = ConfigUtil.getRandomIMEI();
        MAC = ConfigUtil.getRandomMAC();
        TelephonyManager tm = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
        IMSI = tm.getSubscriberId();
        VERSION = ConfigUtil.getRandomVersion();
        String sj = ConfigUtil.getRandomModel();
        MANU = sj.split(" ")[0].trim();
        MODEL = sj.split(" ")[1].trim();
        ID = ConfigUtil.getRandomID();
        GPS = ConfigUtil.getRandomGPS();
        IP = "127.0.0.1";
        PHONE = "13888888888";

        String info = "";
        info += "IMEI:"+IMEI +"\n";
        info += "MAC:"+MAC +"\n";
        info += "IMSI:"+IMSI +"\n";
        info += "VERSION:"+VERSION +"\n";
        info += "MANU:"+MANU +"\n";
        info += "MODEL:"+MODEL +"\n";
        info += "ID:"+ID +"\n";
        info += "GPS:"+GPS +"\n";
        info += "IP:"+IP +"\n";
        info += "PHONE:"+PHONE;

        showTextView.setText(info);
        type = 0;
    }



    private void initParam(){
        IMEI = ConfigUtil.get("IMEI");
        MAC = ConfigUtil.get("MAC");
        IMSI = ConfigUtil.get("IMSI");
        VERSION = ConfigUtil.get("VERSION");
        MANU = ConfigUtil.get("MANU");
        MODEL = ConfigUtil.get("MODEL");
        ID = ConfigUtil.get("ANDROIDID");
        GPS = ConfigUtil.get("GPS");
        IP = ConfigUtil.get("IP");
        PHONE = ConfigUtil.get("PHONE");
        String info = "";
        info += "IMEI:"+IMEI +"\n";
        info += "MAC:"+MAC +"\n";
        info += "IMSI:"+IMSI +"\n";
        info += "VERSION:"+VERSION +"\n";
        info += "MANU:"+MANU +"\n";
        info += "MODEL:"+MODEL +"\n";
        info += "ID:"+ID +"\n";
        info += "GPS:"+GPS +"\n";
        info += "IP:"+IP +"\n";
        info += "PHONE:"+PHONE;

        showTextView.setText(info);
    }
    private boolean saveAllParam(){
        if(IMEI == null||IMEI.equals("")||IMEI.equals("null")){
            Toast.makeText(getApplicationContext(), "IMEI 不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if(MAC == null||MAC.equals("")||MAC.equals("null")){
            Toast.makeText(getApplicationContext(), "MAC 不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if(IMSI == null||IMSI.equals("")||IMSI.equals("null")){
            Toast.makeText(getApplicationContext(), "IMSI 不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if(MANU == null||MANU.equals("")||MANU.equals("null")){
            Toast.makeText(getApplicationContext(), "MANU 不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if(MODEL == null||MODEL.equals("")||MODEL.equals("null")){
            Toast.makeText(getApplicationContext(), "MODEL 不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if(VERSION == null||VERSION.equals("")||VERSION.equals("null")){
            Toast.makeText(getApplicationContext(), "VERSION 不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if(PHONE == null||PHONE.equals("")||PHONE.equals("null")){
            Toast.makeText(getApplicationContext(), "PHONE 不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if(ID == null||ID.equals("")||ID.equals("null")){
            Toast.makeText(getApplicationContext(), "ID 不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if(GPS == null||GPS.equals("")||GPS.equals("null")){
            Toast.makeText(getApplicationContext(), "GPS 不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if(IP == null||IP.equals("")||IP.equals("null")){
            Toast.makeText(getApplicationContext(), "IP 不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        Parameter parameter = new Parameter(IMEI,MAC,IMSI,MANU,MODEL,VERSION,PHONE,ID,GPS,IP);
        boolean result = ConfigUtil.addParams(parameter);
        result &= XMLUtil.addParameter(parameter);
        code = 1;
        obj = parameter;
        new Thread(thread).start();
        return result;
    }


}
