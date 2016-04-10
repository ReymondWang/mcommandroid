package com.purplelight.mcommunity.result;

/**
 * 从网络上返回消息的基类
 * Created by wangyn on 16/4/10.
 */
public class WebResult {
    private String code = "";

    private String msg = "";

    public String getCode(){
        return code;
    }

    public void setCode(String code){
        this.code = code;
    }

    public String getMsg(){
        return msg;
    }

    public void setMsg(String msg){
        this.msg = msg;
    }
}
