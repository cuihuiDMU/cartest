package com.cartest.pro.impl;

import com.cartest.pro.mapper.CarMapper;
import com.cartest.pro.pojo.Car;
import com.cartest.pro.pojo.ResultInfo;
import com.cartest.pro.service.CarService;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    CarMapper carMapper;

    private Logger logger = LoggerFactory.getLogger(CarServiceImpl.class);

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<Car> getAllInfo(String carNumber, String phone, String testStartDate,String testEndDate,String nextStartDate, String nextEndDate, String commonLog, String state, int snum, int sum) {
        return carMapper.getAllInfo(carNumber,phone,testStartDate,testEndDate,nextStartDate,nextEndDate,commonLog,state,snum, sum);
    }

    @Override
    public int getAllInfoSize(String carNumber, String phone, String testStartDate,String testEndDate,String nextStartDate, String nextEndDate, String commonLog, String state) {
        return carMapper.getAllInfoSize(carNumber,phone,testStartDate,testEndDate,nextStartDate,nextEndDate,commonLog,state);
    }

    @Override
    public List<Car> getAllCar(String carNumber,String phone, String testStartDate,String testEndDate,String nextStartDate, String nextEndDate,String commonLog,String state){
        return carMapper.getAllCar(carNumber,phone,testStartDate,testEndDate,nextStartDate,nextEndDate,commonLog,state);
    }
    @Override
    public int updateCarInfo(Car car){
        return carMapper.updateCarInfo(car);
    }

    @Override
    public int addCar(Car car){
        return carMapper.addCar(car);
    }

    @Override
    public int deleteCarInfo(int id){
        return carMapper.deleteCarInfo(id);
    }

    @Override
    public void importCarExcel(Sheet craftSheet, ResultInfo resultInfo) {
        try {
            int totalRows = craftSheet.getPhysicalNumberOfRows();
            if (totalRows<2){
                resultInfo.failResult("文件内容有误");
                return ;
            }
            Row firstRow = craftSheet.getRow(0);
            int totalCols = firstRow.getLastCellNum();
            if (totalCols!=6){
                resultInfo.failResult("文件内容有误");
                return ;
            }
            List<Car> carList = new ArrayList<>();
            for (int rowNum=1;rowNum<totalRows;rowNum++){
                Row row = craftSheet.getRow(rowNum);
                Car car = new Car();
                if (Objects.nonNull(row.getCell(0))){
                    row.getCell(0).setCellType(CellType.STRING);
                    String carNumber = row.getCell(0).getStringCellValue()==null?"":row.getCell(0).getStringCellValue().replaceAll(" ","");
                    car.setCarNumber(carNumber);
                }
                if (Objects.nonNull(row.getCell(1))){
                    row.getCell(1).setCellType(CellType.STRING);
                    String phone = row.getCell(1).getStringCellValue()==null?"":row.getCell(1).getStringCellValue().replaceAll(" ","");
                    car.setPhone(phone);
                }
                if (Objects.nonNull(row.getCell(2))){
                    //row.getCell(2).setCellType(CellType.NUMERIC);
                    boolean isDate = DateUtil.isCellDateFormatted(row.getCell(2));
                    if (isDate){
                        car.setTestDate(row.getCell(2).getDateCellValue());
                    }
                }
                if (Objects.nonNull(row.getCell(3))){
                    boolean isDate = DateUtil.isCellDateFormatted(row.getCell(3));
                    if (isDate){
                        car.setNextDate(row.getCell(3).getDateCellValue());
                    }
                }
                if (Objects.nonNull(row.getCell(4))){
                    row.getCell(4).setCellType(CellType.STRING);
                    String stateDesc = row.getCell(4).getStringCellValue()==null?"":row.getCell(4).getStringCellValue().replaceAll(" ","");
                    if("今年已来".equals(stateDesc)){
                        car.setState(1);
                    }
                    if ("没有来".equals(stateDesc)){
                        car.setState(2);
                    }
                    car.setStateDesc(stateDesc);
                }
                if (Objects.nonNull(row.getCell(5))){
                    row.getCell(5).setCellType(CellType.STRING);
                    String commonLog = row.getCell(5).getStringCellValue()==null?"":row.getCell(5).getStringCellValue().replaceAll(" ","");
                    car.setCommonLog(commonLog);
                }
                carList.add(car);
            }
            // 号码入库
            int num = carMapper.batchInsert(carList);
            logger.info("上传{}个车辆信息",num);
            resultInfo.setSuccess(true);
            resultInfo.setMessage("上传"+num+"个车辆信息！");
            return ;
        }catch (IllegalStateException ir){
            logger.error("importCarExcel carServiceImpl exists IllegalStateException!",ir);
            resultInfo.failResult("导入文件数据类型错误，请核对日期类型等是否正确!");
        } catch (Exception e){
            logger.error("importCarExcel carServiceImpl exists error!",e);
            resultInfo.failResult("导入文件错误，请核对日期等数据类型是否正确!");
        }
    }
}
