package com.cartest.pro.pojo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

/**
*
*  @author author
*/
@Data
public class Car implements Serializable {

    private static final long serialVersionUID = 1594566513298L;


    /**
    * 主键
    */
    private Integer id;

    /**
    * 车牌号
    */
    private String carNumber;

    /**
    * 手机号
    */
    private String phone;

    /**
    * 年检日期
    */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date testDate;

    /**
    * 下次年检日期
    */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date nextDate;

    /**
    * 备注 1：今年已来；2：没有来
    */
    private String commonLog;

    /**
     * 状态
    * */
    private int state;

    /**
     * 状态描述
     * */
    private String stateDesc;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }

    public Date getNextDate() {
        return nextDate;
    }

    public void setNextDate(Date nextDate) {
        this.nextDate = nextDate;
    }

    public String getCommonLog() {
        return commonLog;
    }

    public void setCommonLog(String commonLog) {
        this.commonLog = commonLog;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStateDesc() {
        return stateDesc;
    }

    public void setStateDesc(String stateDesc) {
        this.stateDesc = stateDesc;
    }
}
