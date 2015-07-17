package com.gzak.activities;

import android.graphics.Bitmap;

import com.gzak.entities.Parameter;
import com.gzak.utils.ConfigUtil;
import com.gzak.utils.XMLUtil;

import java.util.List;

/**
 * Created by yli on 2015/7/17.
 */
public class TaskFlow {

    public static void start(){
//          addParameter();
        List<Parameter> taskList = XMLUtil.getTaskList();
        for(Parameter parameter : taskList){
            ConfigUtil.addParams(parameter);
        }

//        changeparameter();
//        saveparamerter();
//        installAPK();
//        openAPK();
//        uninstallAPK();
    }



}
