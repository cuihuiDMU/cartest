package com.cartest.pro.mapper;

import java.util.List;

import com.cartest.pro.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
*  @author author
*/
@Mapper
public interface UserMapper {

    User findUser(User user);

    int add(User user);

    int updateUser(User user);

    int deleteUser(int id);

    List<User> getAllUser(int snum,int sum);

    int getAllUserSize();

/*
    int insertUser(User object);

    int updateUser(User object);

    int update(User.UpdateBuilder object);

    List<User> queryUser(User object);

    User queryUserLimit1(User object);
*/


}