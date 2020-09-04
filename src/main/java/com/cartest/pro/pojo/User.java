package com.cartest.pro.pojo;
import com.alibaba.fastjson.JSON;

import java.io.Serializable;
/**
*
*  @author author
*/
public class User implements Serializable {

    private static final long serialVersionUID = 1594566591943L;


    /**
    * 主键
    * 
    * isNullAble:0
    */
    private Integer id;

    /**
    * 
    * isNullAble:0
    */
    private String name;

    /**
    * 
    * isNullAble:0
    */
    private String password;

    /**
    * 0：普通用户；1：管理员
    * isNullAble:0
    */
    private Integer userType;

    /**
    * 
    * isNullAble:1
    */
    private String roleId;


    public void setId(Integer id){this.id = id;}

    public Integer getId(){return this.id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserType(Integer userType){this.userType = userType;}

    public Integer getUserType(){return this.userType;}

    public void setRoleId(String roleId){this.roleId = roleId;}

    public String getRoleId(){return this.roleId;}
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
