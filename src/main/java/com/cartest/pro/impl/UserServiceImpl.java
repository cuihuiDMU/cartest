package com.cartest.pro.impl;

import com.cartest.pro.mapper.UserMapper;
import com.cartest.pro.pojo.User;
import com.cartest.pro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    public User findUser(User user){
        return userMapper.findUser(user);
    }

    @Override
    public int add(User user){
        return userMapper.add(user);
    }

    @Override
    public int updateUser(User user){
        return userMapper.updateUser(user);
    }

    @Override
    public int deleteUser(int id){
        return userMapper.deleteUser(id);
    }

    @Override
    public List<User> getAllUser(int snum,int sum){
        return userMapper.getAllUser(snum,sum);
    }

    @Override
    public int getAllUserSize(){
        return userMapper.getAllUserSize();
    }
}
