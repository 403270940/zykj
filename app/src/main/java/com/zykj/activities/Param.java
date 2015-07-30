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
import android.widget.EditText;
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
    String InitialParam = "InitialParam";
    String UploadedParam = "UploadedParam";
    String RandomParam = "RandomParam";
    String ServerParam = "ServerParam";
    String ResotreParam = "ResotreParam";

    public final int ExceptionAction = -1;
    public final int GetParamAction = 0;
    public final int ConfirmParamAction = 1;
    public final int UploadRandomParamAction = 2;
    public final int GetRestoreParamAction = 3;
    public final int ConfirmRestoreParamAction = 4;

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
    String DATE = "";
    String curParamStatus = InitialParam;//initialParam  uploadedParam   randomParam  serverParam  restoreParam
    int uploadType = 0;//0 表示当前参数为随机参数 1 表示当前参数为从服务器获取参数  2 代表当前参数是从服务器获取的以前的参数
    boolean ifUploaded = false;
    private TextView showTextView  = null;
    private EditText dateEditText = null;
    private EditText taskNameEditText = null;
    private Button OKButton = null;
    private Button CancelButton = null;
    private Button AllRandomButton = null;
    private Button ServerGetButton = null;
    private Button RestoreButton = null;
    private String[] areas = new String[]{"全部","玉兰香苑", "张江地铁站", "金科路", "张江路", "紫薇路", "香楠小区" };

    private Handler responseHandler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GetParamAction:
                    Response response = (Response)msg.obj;
                    if(response.getResultCode() == 0){
                        showParameter(response);
                        Toast.makeText(getApplicationContext(), "参数保存成功", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), response.getMSG(), Toast.LENGTH_LONG).show();
                    }
                    break;
                case ConfirmParamAction:
                    Response confirmParamResponse = (Response)msg.obj;
                    Toast.makeText(getApplicationContext(), confirmParamResponse.getMSG(), Toast.LENGTH_LONG).show();
                    break;
                case UploadRandomParamAction:
                    Response randomResponse = (Response)msg.obj;
                    Toast.makeText(getApplicationContext(), randomResponse.getMSG(), Toast.LENGTH_LONG).show();
                    break;
                case GetRestoreParamAction:
                    RestoreResponse restoreResponse = (RestoreResponse)msg.obj;
                    if(restoreResponse.getCode()==0){

                    }else{
                        Toast.makeText(getApplicationContext(), restoreResponse.getMsg(), Toast.LENGTH_LONG).show();
                    }
                    break;
                case ConfirmRestoreParamAction:
                    Response confirmRestoreResponse = (Response)msg.obj;
                    Toast.makeText(getApplicationContext(), confirmRestoreResponse.getMSG(), Toast.LENGTH_LONG).show();
                    break;
                case ExceptionAction:
                    Exception e = (Exception)msg.obj;
                    Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };


//    private Handler handler =new Handler(){
//        @Override
//        public void handleMessage(Message msg){
//            super.handleMessage(msg);
//            if(msg.what == 0) {
//                Parameter parameter = (Parameter) msg.obj;
//                if (parameter == null) {
//                    Toast.makeText(getApplicationContext(), "获取服务器参数失败", Toast.LENGTH_LONG).show();
//                    return;
//                }
//                showParameter(parameter);
//                uploadType = 1;
//                Toast.makeText(getApplicationContext(), "获取服务器参数成功", Toast.LENGTH_LONG).show();
//                ifUploaded = false;
//            }else if(msg.what == 1){
//
//                Response response = (Response)msg.obj;
//                if(response != null){
//                    Log.e("input","result String " + response);
//                    if(response.getResultCode() == 0){
//                        Toast.makeText(getApplicationContext(), response.getResultString(), Toast.LENGTH_LONG).show();
//                        ifUploaded = true;
//                    }else if(response.getResultCode() == 1){
//                        Toast.makeText(getApplicationContext(), response.getResultString(), Toast.LENGTH_LONG).show();
//                    }else if(response.getResultCode() == 2){
//                        Toast.makeText(getApplicationContext(), response.getResultString(), Toast.LENGTH_LONG).show();
//                    } else{
//                        Toast.makeText(getApplicationContext(), "上传服务器失败，未知错误", Toast.LENGTH_LONG).show();
//                    }
//                } else
//                    Toast.makeText(getApplicationContext(), "网络请求失败", Toast.LENGTH_LONG).show();
//            }else if(msg.what == 2){
//
//            } else if(msg.what == -1){
//                Exception e = (Exception)msg.obj;
//                Toast.makeText(getApplicationContext(),e.getMessage() , Toast.LENGTH_LONG).show();
//            }
//
//        }
//    };


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.OKButton:
                    setAndUploadParam();
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
                    getServerParam();
                    break;


                case R.id.restoreButton:
                    getRestoreParam();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_param);

        showTextView = (TextView)findViewById(R.id.ShowTextView);
        dateEditText = (EditText)findViewById(R.id.dateEditText);
        taskNameEditText = (EditText)findViewById(R.id.taskEditText);

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
    private void setAndUploadParam(){
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
    }


    private void getRandomInfo(){
        TelephonyManager tm = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
        IMSI = tm.getSubscriberId();
        Parameter parameter = RandomUtil.getRandomParameter();
        parameter.setIMSI(IMSI);
        showParameter(parameter);
        uploadType = 0;
        ifUploaded = false;
    }

    private void getServerParam(){
        new AlertDialog.Builder(Param.this).setTitle("确认从服务获取参数吗？")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestThread requestThread = new RequestThread(GetParamAction);
                        requestThread.start();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    private void getRestoreParam(){
        new AlertDialog.Builder(Param.this).setTitle("确认从服务获取参数吗？")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DATE = dateEditText.getText().toString().trim();
                        TASKNAME = taskNameEditText.getText().toString().trim();
                        RequestThread requestThread = new RequestThread(IMSI,DATE,TASKNAME);
                        requestThread.start();

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();

        new AlertDialog.Builder(Param.this).setTitle("选择区域").setItems(areas, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Param.this, "您已经选择了: " + which + ":" + areas[which], Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }).show();
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
        showParameter();
    }
    private boolean saveAllParam(){
        Parameter parameter = new Parameter(IMEI,MAC,IMSI,MANU,MODEL,VERSION,PHONE,ID,GPS,IP,TASKNAME);
        boolean result = ConfigUtil.addParams(parameter);
        result &= XMLUtil.addParameter(parameter);
        if(curParamStatus.equals(ServerParam) ) {
            RequestThread requestThread = new RequestThread(ConfirmParamAction,parameter);
            requestThread.start();
        }else if(curParamStatus.equals(RandomParam) ) {
            RequestThread requestThread = new RequestThread(UploadRandomParamAction,parameter);
            requestThread.start();
        }else if(curParamStatus.equals(ResotreParam) ) {
            RequestThread requestThread = new RequestThread(ConfirmRestoreParamAction,ID);
            requestThread.start();
        }
        return result;
    }

    public void showParameter(Parameter parameter){
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
        TASKNAME = parameter.getTASKNAME();
        showParameter();
    }

    public void showParameter(Response response){
        IMEI = response.getIMEI();
        MAC = response.getMAC();
        IMSI = response.getIMSI();
        VERSION = response.getVERSION();
        MANU = response.getMODEL().split(" ")[0];
        MODEL = response.getMODEL().split(" ")[1];
        ID = response.getANDROIDID();
        GPS = response.getGPS();
        IP = response.getIP();
        PHONE = response.getPHONE();
        TASKNAME = response.getTASKNAME();
        showParameter();
    }

    public void showParameter(){
        if(!TASKNAME.equals(""))
            taskNameEditText.setText(TASKNAME);
        String info = "";
        info += "IMEI:"+ IMEI +"\n";
        info += "MAC:"+ MAC +"\n";
        info += "IMSI:"+ IMSI +"\n";
        info += "VERSION:"+ VERSION +"\n";
        info += "MANU:"+ MANU +"\n";
        info += "MODEL:"+ MODEL +"\n";
        info += "ID:"+ ID +"\n";
        info += "GPS:"+ GPS +"\n";
        info += "IP:"+ IP +"\n";
        info += "PHONE:"+ PHONE;
        showTextView.setText(info);
    }

    class RequestThread extends Thread{
        int requestAction;//0 代表从服务器获取参数  1 代表将当前设置参数上传给服务器  2 代表获取以前的参数
        Parameter uploadParameter;
        String restoreID;
        String IMSI;
        String date;
        String taskname;
        public RequestThread(int requestAction){
            this.requestAction = requestAction;
        }
        public RequestThread(int requestAction,String restoreID){
            this.requestAction = requestAction;
            this.restoreID = restoreID;
        }
        public RequestThread(int requestAction, Parameter uploadParameter){
            this.requestAction = requestAction;
            this.uploadParameter = uploadParameter;
        }
        public RequestThread(String IMSI,String date,String taskname){
            this.IMSI = IMSI;
            this.date = date;
            this.taskname = taskname;
        }

        @Override
        public void run(){
            try{
                Message msg = new Message();
                msg.what = requestAction;
                switch (requestAction){
                    case GetParamAction:
                        msg.obj =ConfigUtil.getServerParam(IMSI);
                        break;
                    case ConfirmParamAction:
                        msg.obj =  ConfigUtil.confirmParam(uploadParameter);
                        break;
                    case UploadRandomParamAction:
                        msg.obj =  ConfigUtil.uploadRandomParam(uploadParameter);
                        break;
                    case GetRestoreParamAction:
                        msg.obj =   ConfigUtil.getRestoreResponse(IMSI,date, taskname);
                        break;
                    case ConfirmRestoreParamAction:
                        msg.obj =  ConfigUtil.confirmRestoreParam(restoreID);
                        break;
                    default:
                        break;
                }
                responseHandler.sendMessage(msg);
            }catch (Exception e){
                Message msg = new Message();
                msg.what = -1;
                msg.obj = e;
                responseHandler.sendMessage(msg);
            }
        }
    }

}
