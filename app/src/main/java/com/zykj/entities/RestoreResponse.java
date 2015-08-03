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
    private List<Parameter> parameterList = new ArrayList<Parameter>();

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

    public void addParameter(Parameter parameter){
        parameterList.add(parameter);
    }

    public List<Parameter> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<Parameter> parameterList) {
        this.parameterList = parameterList;
    }
}
