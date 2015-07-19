package com.gzak.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gzak.entities.Parameter;
import com.gzak.utils.ConfigUtil;
import com.gzak.utils.HttpUtil;
import com.gzak.utils.ValidationUtil;


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

    private TextView showTextView  = null;
    private Button OKButton = null;
    private Button CancelButton = null;
    private Button AllRandomButton = null;
    private Button ServerGetButton = null;

    private Handler handler =new Handler(){
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            Parameter parameter = (Parameter)msg.obj;
            if(parameter == null) {
                showTextView.setText("get failed");
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

//处理UI
        }
    };

    Thread thread =  new Thread(){
        @Override
        public void run(){
            Parameter parameter = ConfigUtil.getServerInfo();
//            handler.sendEmptyMessage(0);
            Message msg = new Message();
            msg.obj = parameter;
            handler.sendMessage(msg);
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
                        saveAllParam();
                        break;

                    case R.id.CancelButton:
                        initParam();
                        break;

                    case R.id.ALLRandomButton:
                       getRandomInfo();
                        break;

                    case R.id.ServerGetButton:
//                        ConfigUtil.getServerInfo();
                        thread.start();
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
        IMSI = ConfigUtil.getRandomIMSI();
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
    }
    private void getServerInfo(){
        Parameter parameter = ConfigUtil.getServerInfo();
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
        Parameter parameter = new Parameter(IMEI,MAC,IMSI,MANU,MODEL,VERSION,PHONE,ID,GPS,IP);
        boolean result = ConfigUtil.addParams(parameter);
        return result;
    }


}
