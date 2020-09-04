package com.cartest.pro.mapper;

import java.util.List;

import com.cartest.pro.pojo.Car;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
*  @author author
*/
@Mapper
public interface CarMapper{

    List<Car> getAllInfo(@Param(value = "carNumber") String carNumber, @Param(value = "phone") String phone,
                         @Param(value = "testStartDate") String testStartDate, @Param(value = "testEndDate") String testEndDate,
                         @Param(value = "nextStartDate") String nextStartDate, @Param(value = "nextEndDate") String nextEndDate,
                         @Param(value = "commonLog") String commonLog,@Param(value = "state")  String state,
                         @Param(value = "snum") int snum,@Param(value = "sum")  int sum);

    List<Car> getAllCar(@Param(value = "carNumber") String carNumber, @Param(value = "phone") String phone,
                         @Param(value = "testStartDate") String testStartDate, @Param(value = "testEndDate") String testEndDate,
                         @Param(value = "nextStartDate") String nextStartDate, @Param(value = "nextEndDate") String nextEndDate,
                         @Param(value = "commonLog") String commonLog,@Param(value = "state")  String state);

    int getAllInfoSize(@Param(value = "carNumber") String carNumber, @Param(value = "phone") String phone,
                       @Param(value = "testStartDate") String testStartDate, @Param(value = "testEndDate") String testEndDate,
                       @Param(value = "nextStartDate") String nextStartDate, @Param(value = "nextEndDate") String nextEndDate,
                       @Param(value = "commonLog") String commonLog,@Param(value = "state")  String state);
    int updateCarInfo(Car car);

    int addCar(Car car);

    int deleteCarInfo(int id);

    int batchInsert(List<Car> carList);

/*    int insertCar(Car object);

    int updateCar(Car object);

    int update(Car.UpdateBuilder object);

    List<Car> queryCar(Car object);

    Car queryCarLimit1(Car object);*/


}