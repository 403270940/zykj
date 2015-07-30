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
import com.zykj.entities.RestoreResponse;
import com.zykj.utils.ConfigUtil;
import com.zykj.utils.RandomUtil;
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
    String TASKNAME = "";
    int actionType = 0;//0 代表从服务器获取参数  1 代表将当前设置参数上传给服务器  2 代表获取以前的参数
    Object obj = null;
    int uploadType = 0;//0 表示当前参数为随机参数 1 表示当前参数为从服务器获取参数  2 代表当前参数是获取的以前的参数
    private TextView showTextView  = null;
    private Button OKButton = null;
    private Button CancelButton = null;
    private Button AllRandomButton = null;
    private Button ServerGetButton = null;
    private Button RestoreButton = null;
    private String[] areas = new String[]{"全部","玉兰香苑", "张江地铁站", "金科路", "张江路", "紫薇路", "香楠小区" };

    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.what == 0) {
                Parameter parameter = (Parameter) msg.obj;
                if (parameter == null) {
                    Toast.makeText(getApplicationContext(), "获取服务器参数失败", Toast.LENGTH_LONG).show();
                    return;
                }
                showParameter(parameter);
                uploadType = 1;
                Toast.makeText(getApplicationContext(), "获取服务器参数成功", Toast.LENGTH_LONG).show();
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

            } else if(msg.what == -1){
                Exception e = (Exception)msg.obj;
                Toast.makeText(getApplicationContext(),e.getMessage() , Toast.LENGTH_LONG).show();
            }

        }
    };


    Thread thread =  new Thread(){
        @Override
        public void run(){
            try{
            if(actionType == 0){
                Parameter parameter = ConfigUtil.getServerInfo(IMSI);
                //handler.sendEmptyMessage(0);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = parameter;
                handler.sendMessage(msg);

            }else if(actionType == 1){
                Response result = ConfigUtil.updateServerInfo((Parameter)obj, uploadType);
//            handler.sendEmptyMessage(0);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                handler.sendMessage(msg);
            }else if(actionType == 2){
                RestoreResponse restoreResponse = ConfigUtil.getRestoreResponse();
            }
            }catch (Exception e){
                Message msg = new Message();
                msg.what = -1;
                msg.obj = e;
                handler.sendMessage(msg);
            }
        }
    };
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
                                    initParam();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
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
                                    actionType = 0;
                                    new Thread(thread).start();

                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
                    break;


                case R.id.restoreButton:
                    new AlertDialog.Builder(Param.this).setTitle("选择区域").setItems(areas,new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which){
                            Toast.makeText(Param.this, "您已经选择了: " + which + ":" + areas[which],Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }).show();
                    break;
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
        RestoreButton = (Button)findViewById(R.id.restoreButton);

        OKButton.setOnClickListener(onClickListener);
        CancelButton.setOnClickListener(onClickListener);
        AllRandomButton.setOnClickListener(onClickListener);
        ServerGetButton.setOnClickListener(onClickListener);
        RestoreButton.setOnClickListener(onClickListener);

        initParam();
    }

    private void getRandomInfo(){
        TelephonyManager tm = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
        IMSI = tm.getSubscriberId();
        Parameter parameter = RandomUtil.getRandomParameter();
        parameter.setIMSI(IMSI);
        showParameter(parameter);
        uploadType = 0;
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
        TASKNAME = ConfigUtil.get("TASKNAME");
        Parameter parameter =  new Parameter(IMEI,MAC,IMSI,MANU,MODEL,VERSION,PHONE,ID,GPS,IP,TASKNAME);
        showParameter(parameter);
    }
    private boolean saveAllParam(){
        Parameter parameter = new Parameter(IMEI,MAC,IMSI,MANU,MODEL,VERSION,PHONE,ID,GPS,IP,TASKNAME);
        boolean result = ConfigUtil.addParams(parameter);
        result &= XMLUtil.addParameter(parameter);
        actionType = 1;
        obj = parameter;
        new Thread(thread).start();
        return result;
    }

    public void showParameter(Parameter parameter){

        String info = "";
        info += "IMEI:"+parameter.getIMEI() +"\n";
        info += "MAC:"+parameter.getMAC() +"\n";
        info += "IMSI:"+parameter.getIMSI() +"\n";
        info += "VERSION:"+parameter.getVERSION() +"\n";
        info += "MANU:"+parameter.getMANU() +"\n";
        info += "MODEL:"+parameter.getMODEL() +"\n";
        info += "ID:"+parameter.getANDROIDID() +"\n";
        info += "GPS:"+parameter.getGPS() +"\n";
        info += "IP:"+parameter.getIP() +"\n";
        info += "PHONE:"+parameter.getPHONE();
        showTextView.setText(info);
    }



}
