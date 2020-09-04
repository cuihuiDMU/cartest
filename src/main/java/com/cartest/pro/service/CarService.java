package com.cartest.pro.service;

import com.cartest.pro.pojo.Car;
import com.cartest.pro.pojo.ResultInfo;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public interface CarService {

    List<Car> getAllInfo(String carNumber,String phone, String testStartDate,String testEndDate,String nextStartDate, String nextEndDate,String commonLog,String state,int snum, int sum);

    List<Car> getAllCar(String carNumber,String phone, String testStartDate,String testEndDate,String nextStartDate, String nextEndDate,String commonLog,String state);

    int getAllInfoSize(String carNumber,String phone, String testStartDate,String testEndDate,String nextStartDate, String nextEndDate,String commonLog,String state);

    int updateCarInfo(Car car);

    int addCar(Car car);

    int deleteCarInfo(int id);

    void importCarExcel(Sheet craftSheet, ResultInfo resultInfo);
}
