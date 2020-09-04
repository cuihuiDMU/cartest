package com.cartest.pro.service;

import com.cartest.pro.pojo.User;

import java.util.List;

public interface UserService {

    User findUser(User user);

    int add(User user);

    int updateUser(User user);

    int deleteUser(int id);

    List<User> getAllUser(int snum,int sum);

    int getAllUserSize();
}
