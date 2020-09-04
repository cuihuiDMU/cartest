package com.cartest.pro.pojo;

import lombok.Data;

@Data
public class ResultInfo {

    private boolean success;

    private String message;

    private Object value;

    public static ResultInfo newInstance() {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setSuccess(true);
        return resultInfo;
    }

    public static ResultInfo successResult(String message) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setSuccess(true);
        resultInfo.setMessage(message);
        return resultInfo;
    }

    public static ResultInfo successResult(Object value) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setSuccess(true);
        resultInfo.setValue(value);
        return resultInfo;
    }

    public static ResultInfo failResult(String message) {
        return failResult(message, null);
    }

    public static ResultInfo failResult(String message, Object value) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setSuccess(false);
        resultInfo.setMessage(message);
        resultInfo.setValue(value);
        return resultInfo;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setResult(ResultInfo result,boolean success,String message){
        result.setSuccess(success);
        result.setMessage(message);
    }
}
