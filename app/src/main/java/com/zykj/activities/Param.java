package com.zykj.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.liyongyue.zykj.R;
import com.zykj.entities.Parameter;
import com.zykj.entities.PhoneResponse;
import com.zykj.entities.Response;
import com.zykj.entities.RestoreParameter;
import com.zykj.entities.RestoreResponse;
import com.zykj.utils.ConfigUtil;
import com.zykj.utils.RandomUtil;
import com.zykj.utils.XMLUtil;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;


public class Param extends ActionBarActivity {
    private String InitialParam = "InitialParam";//初始化参数
    private String UploadedParam = "UploadedParam";//已上传参数
    private String RandomParam = "RandomParam";//随机参数
    private String ServerParam = "ServerParam";//服务器获取参数
    private String ResotreParam = "ResotreParam";//恢复的参数
    private boolean IMSIThreadGoOn = true;
    private final int ExceptionAction = -1;
    private final int GetParamAction = 0;//获取服务器参数
    private final int ConfirmParamAction = 1;//确认服务器参数
    private final int UploadRandomParamAction = 2;//上传随机参数
    private final int GetRestoreParamAction = 3;//恢复指定参数
    private final int ConfirmRestoreParamAction = 4;//确认恢复参数
    private final int GetPhoneAction = 5;
    private final int UpdateUIAction = 6;
    private String IMEI = "";
    private String MAC = "";
    private String IMSI = "";
    private String VERSION = "";
    private String MANU = "";
    private String MODEL = "";
    private String ID = "";
    private String GPS = "";
    private String IP = "0.0.0.0";
    private String PHONE = "13888888888";
    private String TASKNAME = "";
    private String DATE = "";
    private String curParamStatus = InitialParam;//用于记录当前显示的参数是何种参数
    private boolean ISGetPhoneSuccess = false;
    private static int curID = 0;//当前选择的恢复参数的ID,如恢复参数 时返回3个参数，用户选择了第2个，则保存为1

    private TextView showTextView  = null;//显示参数TextView
    private EditText dateEditText = null;//日期编辑控件
    private EditText taskNameEditText = null;//任务名控件
    private Button OKButton = null;//确认控件
    private Button CancelButton = null;//重置控件
    private Button AllRandomButton = null;//全部随机控件
    private Button ServerGetButton = null;//获取服务器参数控件
    private Button RestoreButton = null;//恢复指定参数控件

    /*
     *responseHandler 是用来出来网络请求后的返回结果
     * 根据msg.what来判断该结果是调用什么网络接口后的结果，msg.obj是返回的结果
     *  GetParamAction 表示该结果是获取服务器参数返回的参数信息，接口1
     *  ConfirmParamAction表示该结果是确认服务器参数的结果，接口2
     *  UploadRandomParamAction表示该结果是上传随机参数的返回结果，接口3
     *  GetRestoreParamAction 表示该结果是恢复指定参数的返回结果
     *  ConfirmRestoreParamAction 表示该结果是确定恢复参数的返回结果
     *
     */
    private Handler responseHandler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GetParamAction://获取服务器参数的返回结果
                    Response response = (Response)msg.obj;
                    if(response.getResultCode() == 0){
                        showParameter(response);//如果code为0，则说明获取参数成功，显示参数
                        curParamStatus = ServerParam;
                        Toast.makeText(getApplicationContext(), "参数获取成功", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), response.getMSG(), Toast.LENGTH_LONG).show();
                    }
                    break;
                case ConfirmParamAction://确认获取服务器参数的返回结果
                    Response confirmParamResponse = (Response)msg.obj;
                    Toast.makeText(getApplicationContext(), confirmParamResponse.getMSG(), Toast.LENGTH_LONG).show();
                    break;
                case UploadRandomParamAction://上传随机参数的返回结果
                    Response randomResponse = (Response)msg.obj;
                    Toast.makeText(getApplicationContext(), randomResponse.getMSG(), Toast.LENGTH_LONG).show();
                    break;
                case GetRestoreParamAction://获取指定条件的所有恢复参数的返回结果
                    RestoreResponse restoreResponse = (RestoreResponse)msg.obj;
                    if(restoreResponse.getCode()==0){
                        showRestoreParameter(restoreResponse);
                    }else{
                        Toast.makeText(getApplicationContext(), restoreResponse.getMsg(), Toast.LENGTH_LONG).show();
                    }
                    break;
                case ConfirmRestoreParamAction://确认恢复指定参数的返回结果
                    Response confirmRestoreResponse = (Response)msg.obj;
                    Toast.makeText(getApplicationContext(), confirmRestoreResponse.getMSG(), Toast.LENGTH_LONG).show();
                    break;

                case GetPhoneAction:
                    PhoneResponse phoneResponse = (PhoneResponse)msg.obj;
                    if(phoneResponse.getCode() == 0){
                        PHONE = phoneResponse.getPhone();
                        ISGetPhoneSuccess = true;
                        ConfigUtil.savePhone(PHONE);
                        showParameterWithoutTaskName();
                    }else{
                        Toast.makeText(getApplicationContext(), phoneResponse.getMsg(), Toast.LENGTH_LONG).show();
                    }
                    break;
                case UpdateUIAction:
                    showParameterWithoutTaskName();
                    break;
                case ExceptionAction:
                    Exception e = (Exception)msg.obj;
                    Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };


    /*
     * onclickListener 是用于处理按钮事件
     */
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.OKButton://确认按钮事件
                    setAndUploadParam();
                    break;

                case R.id.CancelButton://重置按钮事件
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

                case R.id.ALLRandomButton://随机按钮事件
                    getRandomInfo();
                    break;

                case R.id.ServerGetButton://获取服务器参数按钮事件
                    getServerParam();
                    break;

                case R.id.restoreButton://恢复指定参数按钮事件
                    getRestoreParam();
                    break;
            }
        }
    };


    /*
     *初始化UI组件
     */
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

        initParam();//初始化参数
        IMSICheckThread checkThread = new IMSICheckThread();//检测sim卡变动线程
        checkThread.start();
    }

    private String getPhone(String IMSI){
        String phone = "";
//        IMSI = "460008143139857";
        RequestThread requestThread = new RequestThread(GetPhoneAction,IMSI);
        requestThread.start();
        return phone;
    }
    //设置参数并且将参数上传服务器
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

    /*
     *获取随机信息
     * 因为IMSI不需要修改，所以只是获取本机的IMSI值
     */
    private void getRandomInfo(){
        TelephonyManager tm = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
        IMSI = tm.getSubscriberId();
        Parameter parameter = RandomUtil.getRandomParameter();
        parameter.setIMSI(IMSI);
        parameter.setPHONE(PHONE);
        parameter.setIP(IP);
        curParamStatus = RandomParam;
        showParameter(parameter);

    }
    /*
     *根据IMSI值从服务器获取指定的IMSI的参数
     *
     */
    private void getServerParam(){
        new AlertDialog.Builder(Param.this).setTitle("确认从服务获取参数吗？")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestThread requestThread = new RequestThread(GetParamAction,IMSI);
                        requestThread.start();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }
    /*
    *
   *获取指定条件的所有的恢复结果
   */
    private void getRestoreParam(){
        new AlertDialog.Builder(Param.this).setTitle("确认从服务获取参数吗？")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DATE = dateEditText.getText().toString().trim();//获取设置的日期值
                        TASKNAME = taskNameEditText.getText().toString().trim();//获取设置的任务名
                        RequestThread requestThread = new RequestThread(GetRestoreParamAction,IMSI, DATE, TASKNAME);
                        requestThread.start();

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    /*
     *恢复参数为上次设置成功的参数值
     */
    private void initParam(){
        IMEI = ConfigUtil.get("IMEI");
        MAC = ConfigUtil.get("MAC");
        TelephonyManager tm = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
        IMSI = tm.getSubscriberId();
        if(IMSI == null){
            IMSI = "";
        }
        VERSION = ConfigUtil.get("VERSION");
        MANU = ConfigUtil.get("MANU");
        MODEL = ConfigUtil.get("MODEL");
        ID = ConfigUtil.get("ANDROIDID");
        GPS = ConfigUtil.get("GPS");
//        IP = ConfigUtil.get("IP");
        IP = getIp();
        PHONE = ConfigUtil.get("PHONE");
        TASKNAME = ConfigUtil.get("TASKNAME");
        showParameter();
    }

    /*
     * 保存参数值，使当前的参数值生效
     */
    private boolean saveAllParam(){
        TASKNAME = taskNameEditText.getText().toString();

        Parameter parameter = new Parameter(IMEI,MAC,IMSI,MANU,MODEL,VERSION,PHONE,ID,GPS,IP,TASKNAME);
        boolean result = ConfigUtil.addParams(parameter);
        result &= XMLUtil.addParameter(parameter);
        if(curParamStatus.equals(ServerParam) ) {
            RequestThread requestThread = new RequestThread(ConfirmParamAction,parameter);
            requestThread.start();
        }else if(curParamStatus.equals(RandomParam) ) {
            if(parameter.getTASKNAME().equals("")){
                Toast.makeText(getApplicationContext(), "taskname 不能为空", Toast.LENGTH_LONG).show();
                return false;
            }
            RequestThread requestThread = new RequestThread(UploadRandomParamAction,parameter);
            requestThread.start();
        }else if(curParamStatus.equals(ResotreParam) ) {
            RequestThread requestThread = new RequestThread(ConfirmRestoreParamAction,curID);
            requestThread.start();
        }
        return result;
    }

    private  String getLocalIpAddress() {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        // 获取32位整型IP地址
        int ipAddress = wifiInfo.getIpAddress();

        //返回整型地址转换成“*.*.*.*”地址
        return String.format("%d.%d.%d.%d",
                (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
    }

    private static String getIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        // if (!inetAddress.isLoopbackAddress() && inetAddress
                        // instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public  String getIp(){
        String result = "";
        String tmp = getLocalIpAddress();
        if(tmp !=null && !tmp.equals("0.0.0.0")){
            result = tmp;
            return result;
        }
        tmp = getIpAddress();
        if(tmp !=null && !tmp.equals("")){
            result = tmp;
            return result;
        }
        return "";
    }
    /*
     * parameter 参数信息
     * 将parameter中的参数信息显示在UI上
     */
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
    /*
     * parameter 参数信息
     * 将parameter中的恢复参数信息显示在UI上
     */
    public void showParameter(RestoreParameter parameter){
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

    /*
     * 将获取的restoreresponse的值显示给用户选择，并将选择的值显示在UI上
     *
     */
    public void showRestoreParameter(final RestoreResponse restoreResponse){
        final int size = restoreResponse.getParameterList().size();
        final String[] parameters = new String[10];

        for(int i = 0; i < size;i++){
            RestoreParameter parameter = restoreResponse.getParameterList().get(i);
            parameters[i] = " ID:" + parameter.getId() +"IMEI:" + parameter.getIMEI()+" MAC:" + parameter.getMAC() ;
        }
        new AlertDialog.Builder(Param.this).setTitle("选择要还原参数").setItems(parameters, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Param.this, "您已经选择了: " + which + ":" + parameters[which], Toast.LENGTH_LONG).show();
                if (which < size) {
                    curID = restoreResponse.getParameterList().get(which).getId();//设置当前选择的恢复的结果的ID
                    curParamStatus = ResotreParam;//设置当前的参数类型为恢复结果类型
                    showParameter(restoreResponse.getParameterList().get(which));
                    dialog.dismiss();
                }
            }
        }).show();
    }

    /*
     *将从服务器获取的参数的返回结果在UI上显示
     */
    public void showParameter(Response response){
        IMEI = response.getIMEI();
        MAC = response.getMAC();
        IMSI = response.getIMSI();
        VERSION = response.getVERSION();
        MANU = ConfigUtil.getManu(response.getMODEL());
        MODEL = ConfigUtil.getModel(response.getMODEL());
        ID = response.getANDROIDID();
        GPS = response.getGPS();
        IP = response.getIP();
        PHONE = response.getPHONE();
        TASKNAME = response.getTASKNAME();
        showParameter();
    }


    public void showParameterWithoutTaskName(){
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
    /*
     * 显示参数信息
     */
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

    /*
     * 网络请求的线程因为Android不允许在主线程中进行网络请求，故需要生成一个线程进行网络请求，
     * 获取请求结果成功后，交给handler去处理
     * 当按钮事件触发时，调用对应的构造函数，
     * 线程执行时通过判断requestaction来决定执行什么网络请求
     */
    class RequestThread extends Thread{
        int requestAction;
        Parameter uploadParameter;
        int restoreID;
        String IMSI;
        String date;
        String taskname;
        public RequestThread(int requestAction,String IMSI){//获取服务器参数
            this.IMSI = IMSI;
            this.requestAction = requestAction;
        }
        public RequestThread(int requestAction,int restoreID){//确认恢复参数
            this.requestAction = requestAction;
            this.restoreID = restoreID;
        }
        public RequestThread(int requestAction, Parameter uploadParameter){//确认服务器参数或上传随机参数
            this.requestAction = requestAction;
            this.uploadParameter = uploadParameter;
        }
        public RequestThread(int requestAction,String IMSI,String date,String taskname){//获取指定条件的恢复结果
            this.requestAction = requestAction;
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
                    case GetParamAction://获取服务器参数
                        msg.obj =ConfigUtil.getServerParam(IMSI);
                        break;
                    case ConfirmParamAction://确认服务器参数
                        msg.obj =  ConfigUtil.confirmParam(uploadParameter);
                        break;
                    case UploadRandomParamAction://上传随机参数
                        msg.obj =  ConfigUtil.uploadRandomParam(uploadParameter);
                        break;
                    case GetRestoreParamAction://获取恢复参数
                        msg.obj =   ConfigUtil.getRestoreParam(IMSI,date, taskname);
                        break;
                    case ConfirmRestoreParamAction://确认恢复参数
                        msg.obj =  ConfigUtil.confirmRestoreParam(restoreID);
                        break;
                    case GetPhoneAction:
                        msg.obj = ConfigUtil.getPhone(IMSI);
                    default:
                        break;
                }
                responseHandler.sendMessage(msg);
            }catch (Exception e){
                Message msg = new Message();
                msg.what = ExceptionAction;
                msg.obj = e;
                responseHandler.sendMessage(msg);
            }
        }
    }

    /*
     *该类线程是用于检测sim卡变动情况，如果sim卡变动，则进行网络请求获取到新的手机号码
     */
    class IMSICheckThread extends  Thread{
        public void updateUI(){
            Message msg = new Message();
            msg.what = UpdateUIAction;
            responseHandler.sendMessage(msg);
        }
        @Override
        public void run() {
            while(IMSIThreadGoOn){
                TelephonyManager tm = (TelephonyManager) getSystemService(Param.this.TELEPHONY_SERVICE);
                String tmpIMSI = tm.getSubscriberId();
                if(tmpIMSI == null){
                    tmpIMSI = "";
                }
                if(!tmpIMSI.equals(IMSI) || ISGetPhoneSuccess == false){
                    ISGetPhoneSuccess = false;
                    if(IMSI.equals("")){
                        PHONE = "";
                        IMSI = tmpIMSI;
                        ISGetPhoneSuccess = true;
                    }else{
                        getPhone(IMSI);
                    }
                }
                IP = getIp();
                updateUI();
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        IMSIThreadGoOn = false;
    }
}
