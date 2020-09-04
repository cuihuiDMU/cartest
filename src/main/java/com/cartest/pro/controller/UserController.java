package com.cartest.pro.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cartest.pro.pojo.Car;
import com.cartest.pro.pojo.PageHelper;
import com.cartest.pro.pojo.ResultInfo;
import com.cartest.pro.pojo.User;
import com.cartest.pro.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @RequestMapping("permission")
    public String permission(){
        return "permission";
    }

    @RequestMapping("/login")
    public ResultInfo login(@RequestParam(value = "info") String info){
        ResultInfo responseResult = new ResultInfo();
        User user = JSON.parseObject(info,User.class);
        System.out.println(user.getName()+user.getPassword());
        //shiro用户认证
        //获取subject
        Subject subject = SecurityUtils.getSubject();
        //封装用户数据
        UsernamePasswordToken userToken = new UsernamePasswordToken(user.getName(),DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        //UsernamePasswordToken userToken = new UsernamePasswordToken(user.getName(),user.getPassword());
        //执行登录方法,用捕捉异常去判断是否登录成功
        try {
            subject.login(userToken);
            //request.setAttribute("loginUser","test");
            responseResult.setSuccess(true);
            return responseResult;
        } catch (UnknownAccountException e) {
            //用户名不存在
            responseResult.setSuccess(false);
            responseResult.setMessage("用户或密码错误!");
            return responseResult;
        }catch (IncorrectCredentialsException e) {
            //密码错误
            responseResult.setSuccess(false);
            responseResult.setMessage("密码错误!");
            return responseResult;
        }

    }

    @RequestMapping("/logout")
    public ResultInfo logout(){
        ResultInfo responseResult = new ResultInfo();
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
            responseResult.setSuccess(true);
        } catch (UnknownAccountException e) {
            //用户名不存在
            responseResult.setSuccess(false);
            responseResult.setMessage("用户或密码错误!");
            return responseResult;
        }catch (IncorrectCredentialsException e) {
            //密码错误
            responseResult.setSuccess(false);
            responseResult.setMessage("密码错误!");
            return responseResult;
        }
        return responseResult;
    }
    @RequestMapping("/addUser")
    public ResultInfo addUser(@RequestParam(value = "info") String info){
        ResultInfo resultInfo = new ResultInfo();
        try {
            User user = JSON.parseObject(info,User.class);
            System.out.println(user.getName()+user.getPassword());
            //shiro用户认证
            //获取subject
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            int num = userService.add(user);
            resultInfo.setSuccess(true);
            resultInfo.setMessage("用户创建成功!");
        }catch (Exception e){
            logger.error("用户创建错误!",e);
            resultInfo.setSuccess(false);
            resultInfo.setMessage("用户创建错误!");
        }
        return resultInfo;
        }

    @RequestMapping("/checkType")
    public ResultInfo checkType(){
        ResultInfo resultInfo = new ResultInfo();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        User user1 = userService.findUser(user);
        if(Objects.isNull(user1)){
            resultInfo.setSuccess(false);
            return resultInfo;
        }
        JSONObject object = new JSONObject();
        object.put("name",user1.getName());
        object.put("type",user1.getUserType());
        resultInfo.setValue(object);
        resultInfo.setSuccess(true);
        return resultInfo;
    }

    @RequestMapping("/modifyUser")
    public ResultInfo modifyUser(@RequestParam(value = "info") String info){
        ResultInfo resultInfo = new ResultInfo();
        try {
            User user = JSON.parseObject(info,User.class);
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            int num = userService.updateUser(user);
            resultInfo.setSuccess(true);
            resultInfo.setMessage("用户编辑成功!");
        }catch (Exception e){
            logger.error("用户编辑错误!",e);
            resultInfo.setSuccess(false);
            resultInfo.setMessage("用户编辑错误!");
        }
        return resultInfo;
    }

    @RequestMapping("/deleteUser")
    public ResultInfo deleteUser(@RequestParam(value = "id") int id){
        ResultInfo resultInfo = new ResultInfo();
        try {
            int num = userService.deleteUser(id);
            resultInfo.setSuccess(true);
            resultInfo.setMessage("用户成功删除!");
        }catch (Exception e){
            logger.error("用户删除错误!",e);
            resultInfo.setSuccess(false);
            resultInfo.setMessage("用户删除错误!");
        }
        return resultInfo;
    }

    @RequestMapping("/getAllUserInfo")
    public ResultInfo getAllUserInfo(@RequestParam(value = "pageNum") int pageNum,@RequestParam(value = "pageSize") int pageSize){
        ResultInfo resultInfo = new ResultInfo();
        try {
            List<User> userList = userService.getAllUser((pageNum - 1) * pageSize, pageSize);
            int total = userService.getAllUserSize();
            PageHelper<User> pageHelper = new PageHelper(total,
                    pageNum, pageSize, userList);
            resultInfo.setValue(pageHelper);
            resultInfo.setSuccess(true);
            resultInfo.setMessage("用户搜索成功!");
        }catch (Exception e){
            logger.error("用户搜索错误!",e);
            resultInfo.setSuccess(false);
            resultInfo.setMessage("用户搜索错误!");
        }
        return resultInfo;
    }
}
