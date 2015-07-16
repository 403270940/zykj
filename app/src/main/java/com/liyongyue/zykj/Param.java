package com.liyongyue.zykj;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Param extends ActionBarActivity {

    String IMEI = "";
    String MAC = "";
    String IMSI = "";
    String VERSION = "";
    String MODEL = "";
    String ID = "";
    String GPS = "";
    private Button IMEIButton = null;
    private Button MACButton = null;
    private Button IMSIButton = null;
    private Button VersionButton = null;
    private Button ModelButton = null;
    private Button IDButton = null;
    private Button GPSButton = null;
    private Button OKButton = null;
    private Button CancelButton = null;
    private Button AllRandomButton = null;
    private Button ServerGetButton = null;
    private EditText IMEIEditText = null;
    private EditText MACEditText = null;
    private EditText IMSIEditText = null;
    private EditText VersionEditText = null;
    private EditText ModelEditText = null;
    private EditText IDEditText = null;
    private EditText GPSEditText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_param);
        IMEIButton = (Button)findViewById(R.id.IMEIButton);
        MACButton = (Button)findViewById(R.id.MACButton);
        IMSIButton = (Button)findViewById(R.id.IMSIButton);
        VersionButton = (Button)findViewById(R.id.VersionButton);
        ModelButton = (Button)findViewById(R.id.ModelButton);
        IDButton = (Button)findViewById(R.id.IDButton);
        GPSButton = (Button)findViewById(R.id.GPSButton);


        OKButton = (Button)findViewById(R.id.OKButton);
        CancelButton = (Button)findViewById(R.id.CancelButton);
        AllRandomButton = (Button)findViewById(R.id.ALLRandomButton);
        ServerGetButton = (Button)findViewById(R.id.ServerGetButton);

        IMEIEditText = (EditText)findViewById(R.id.IMEIEditText);
        MACEditText = (EditText)findViewById(R.id.MACEditText);
        IMSIEditText = (EditText)findViewById(R.id.IMSIEditText);
        VersionEditText = (EditText)findViewById(R.id.VersionEditText);
        ModelEditText = (EditText)findViewById(R.id.ModelEditText);
        IDEditText = (EditText)findViewById(R.id.IDEditText);
        GPSEditText = (EditText)findViewById(R.id.GPSEditText);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.IMEIButton:
                        IMEI = ConfigUtil.getRandomIMEI();
                        IMEIEditText.setText(IMEI);
                        break;

                    case R.id.MACButton:
                        MAC = ConfigUtil.getRandomMAC();
                        MACEditText.setText(MAC);
                        break;

                    case R.id.IMSIButton:
                        IMSI = ConfigUtil.getRandomIMSI();
                        IMSIEditText.setText(IMSI);
                        break;

                    case R.id.VersionButton:
                        VERSION = ConfigUtil.getRandomVersion();
                        VersionEditText.setText(VERSION);
                        break;

                    case R.id.ModelButton:
                        MODEL = ConfigUtil.getRandomModel();
                        ModelEditText.setText(MODEL);
                        break;

                    case R.id.IDButton:
                        ID = ConfigUtil.getRandomID();
                        IDEditText.setText(ID);
                        break;

                    case R.id.GPSButton:
                        GPS = ConfigUtil.getRandomGPS();
                        GPSEditText.setText(GPS);
                        break;

                    case R.id.OKButton:
                        saveAllParam();
                        break;

                    case R.id.CancelButton:
                        initParam();
                        break;

                    case R.id.ALLRandomButton:
                        IMEI = ConfigUtil.getRandomIMEI();
                        MAC = ConfigUtil.getRandomMAC();
                        IMSI = ConfigUtil.getRandomIMSI();
                        VERSION = ConfigUtil.getRandomVersion();
                        MODEL = ConfigUtil.getRandomModel();
                        ID = ConfigUtil.getRandomID();
                        IDEditText.setText(ID);
                        ModelEditText.setText(MODEL);
                        VersionEditText.setText(VERSION);
                        IMSIEditText.setText(IMSI);
                        MACEditText.setText(MAC);
                        IMEIEditText.setText(IMEI);
                        break;

                    case R.id.ServerGetButton:
                        ConfigUtil.getServerInfo();
                        break;

                }
            }
        };

        initParam();


        IMEIButton.setOnClickListener(onClickListener);
        MACButton.setOnClickListener(onClickListener);
        IMSIButton.setOnClickListener(onClickListener);
        VersionButton.setOnClickListener(onClickListener);
        ModelButton.setOnClickListener(onClickListener);
        GPSButton.setOnClickListener(onClickListener);
        IDButton.setOnClickListener(onClickListener);
        AllRandomButton.setOnClickListener(onClickListener);
        OKButton.setOnClickListener(onClickListener);
        CancelButton.setOnClickListener(onClickListener);
        ServerGetButton.setOnClickListener(onClickListener);



    }

    private void initParam(){
        IMEIEditText.setText(ConfigUtil.get("IMEI"));
        MACEditText.setText(ConfigUtil.get("MAC"));
        IMSIEditText.setText(ConfigUtil.get("IMSI"));
        VersionEditText.setText(ConfigUtil.get("VERSION"));
        ModelEditText.setText(ConfigUtil.get("MANU") + " " +ConfigUtil.get("MODEL"));
        IDEditText.setText(ConfigUtil.get("ANDROIDID"));
        GPSEditText.setText(ConfigUtil.get("GPS"));
    }
    private boolean saveAllParam(){
        IMEI = IMEIEditText.getText().toString();
        MAC = MACEditText.getText().toString();
        IMSI = IMSIEditText.getText().toString();
        VERSION = VersionEditText.getText().toString();
        MODEL = ModelEditText.getText().toString();
        GPS = GPSEditText.getText().toString();
        ID = IDEditText.getText().toString();
        if(!ValidationUtil.check("IMEI",IMEI)){
            Toast.makeText(this, "IMEI格式错误", Toast.LENGTH_SHORT);
            return false;
        }
        if(!ValidationUtil.check("MAC",MAC)){
            Toast.makeText(this,"MAC格式错误",Toast.LENGTH_SHORT);
            return false;
        }
        if(!ValidationUtil.check("IMSI",IMSI)){
            Toast.makeText(this,"IMSI格式错误",Toast.LENGTH_SHORT);
            return false;
        }
        if(!ValidationUtil.check("VERSION", VERSION)){
            Toast.makeText(this,"厂商格式错误",Toast.LENGTH_SHORT);
            return false;
        }
        if(!ValidationUtil.check("MODEL", MODEL)){
            Toast.makeText(this,"型号格式错误",Toast.LENGTH_SHORT);
            return false;
        }
        if(!ValidationUtil.check("ANDROIDID",ID)){
            Toast.makeText(this,"ID格式错误",Toast.LENGTH_SHORT);
            return false;
        }
        if(!ValidationUtil.check("GPS",GPS)){
            Toast.makeText(this,"GPS格式错误",Toast.LENGTH_SHORT);
            return false;
        }

        boolean result = ConfigUtil.addParams(IMEI,MAC,IMSI, VERSION, MODEL,ID,GPS);
        return result;
    }



    private boolean resetAllParam(){
        IMEI = ConfigUtil.get("IMEI");
        return true;
    }
}
