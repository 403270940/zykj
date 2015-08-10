package com.zykj.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yli on 2015/7/30.
 * 该类是获取恢复参数结果的实体类
 */
public class RestoreResponse {

    private int code;
    private String msg;
    private List<RestoreParameter> parameterList = new ArrayList<RestoreParameter>();

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void addParameter(RestoreParameter parameter){
        parameterList.add(parameter);
    }

    public List<RestoreParameter> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<RestoreParameter> parameterList) {
        this.parameterList = parameterList;
    }
}
