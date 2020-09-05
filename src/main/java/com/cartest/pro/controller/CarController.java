package com.cartest.pro.controller;

import com.alibaba.fastjson.JSON;
import com.cartest.pro.pojo.Car;
import com.cartest.pro.pojo.PageHelper;
import com.cartest.pro.pojo.ResultInfo;
import com.cartest.pro.service.CarService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/car")
public class CarController {

    private static Logger logger = LoggerFactory.getLogger(CarController.class);

    @Autowired
    CarService carService;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 搜索
     * */
    @RequestMapping("/searchInfo")
    public ResultInfo searchInfo(@RequestParam(value = "carNumber", required = false) String carNumber,
                                 @RequestParam(value = "phone", required = false) String phone,
                                 @RequestParam(value = "testStartDate", required = false) String testStartDate,
                                 @RequestParam(value = "testEndDate", required = false) String testEndDate,
                                 @RequestParam(value = "nextStartDate", required = false) String nextStartDate,
                                 @RequestParam(value = "nextEndDate", required = false) String nextEndDate,
                                 @RequestParam(value = "commonLog", required = false) String commonLog,
                                 @RequestParam(value = "state", required = false) String state,
                                 @RequestParam(value = "pageNum") int pageNum,
                                 @RequestParam(value = "pageSize") int pageSize){
        ResultInfo resultInfo = new ResultInfo();
        try {
            List<Car> carList = new ArrayList<>();
           // carList.stream().forEach(item->item.setNextDate(Objects.isNull(item.getTestDate())?null:sdf.format(item.getNextDate())));
            carList = carService.getAllInfo(carNumber,phone,testStartDate,testEndDate,nextStartDate,nextEndDate,commonLog,state,(pageNum - 1) * pageSize, pageSize);
            carList.stream().forEach(item->item.setTestDate(Objects.isNull(item.getTestDate())?null:item.getTestDate()));
            carList.stream().forEach(item->item.setNextDate(Objects.isNull(item.getNextDate())?null:item.getNextDate()));

            if (Objects.isNull(carList)||carList.size()<1){
                resultInfo.setValue(new PageHelper<Car>(
                        new ArrayList<Car>()));
                resultInfo.setSuccess(true);
                resultInfo.setMessage("暂无数据！");
                return resultInfo;
            }
            int total = carService.getAllInfoSize(carNumber,phone,testStartDate,testEndDate,nextStartDate,nextEndDate,commonLog,state);
            PageHelper<Car> pageHelper = new PageHelper(total,
                    pageNum, pageSize, carList);
            resultInfo.setSuccess(true);
            resultInfo.setMessage("查询成功!");
            resultInfo.setValue(pageHelper);

        }catch (Exception e){
            logger.error("searchInfo exists error!",e);
            resultInfo.setSuccess(false);
            resultInfo.setMessage("查询错误!");
        }
        return resultInfo;
    }

    /**
     * 新增车辆信息
     * */
    @RequestMapping("/addCarInfo")
    public ResultInfo addCarInfo(@RequestParam(value = "param") String param){
        ResultInfo resultInfo = new ResultInfo();
        try {
            Car car = JSON.parseObject(param,Car.class);
            logger.debug("add car:{}",car.toString());
            int num = carService.addCar(car);
            resultInfo.setSuccess(true);
            resultInfo.setMessage("新增成功!");
        }catch (Exception e){
            logger.error("addCarInfo exists error!",e);
            resultInfo.setSuccess(false);
            resultInfo.setMessage("新增错误!");
        }
        return resultInfo;
    }

    /**
     * 修改车辆信息
     * */
    @RequestMapping("/modifyInfo")
    public ResultInfo modifyInfo(@RequestParam(value = "param") String param){
        ResultInfo resultInfo = new ResultInfo();
        try {
            Car car = JSON.parseObject(param,Car.class);
            logger.debug("modifyInfo car:{}",car.toString());
            int num = carService.updateCarInfo(car);
            resultInfo.setSuccess(true);
            resultInfo.setMessage("更新成功!");
        }catch (Exception e){
            logger.error("modifyInfo exists error!",e);
            resultInfo.setSuccess(false);
            resultInfo.setMessage("更新错误!");
        }
        return resultInfo;
    }

    /**
     * 删除车辆信息
     * */
    @RequestMapping("/deleteCarInfo")
    public ResultInfo deleteCarInfo(@RequestParam(value = "id") int id){
        ResultInfo resultInfo = new ResultInfo();
        try {
            logger.debug("delete car:{}",id);
            int num = carService.deleteCarInfo(id);
            resultInfo.setSuccess(true);
            resultInfo.setMessage("删除成功!");
        }catch (Exception e){
            logger.error("deleteCarInfo exists error!",e);
            resultInfo.setSuccess(false);
            resultInfo.setMessage("删除失败!");
        }
        return resultInfo;
    }

    /**
     * 导入excel
     * */
    @RequestMapping("/batchImportCarInfo")
    public ResultInfo batchImportCarInfo(@RequestParam("excelFile") MultipartFile excelFile){
        ResultInfo resultInfo = new ResultInfo();
        //zip包校验
        if (Objects.isNull(excelFile) || excelFile.isEmpty()) {
            logger.error("import nameList is null.");
            return resultInfo.failResult("导入文件不允许为空");
        }
        String fileName = excelFile.getOriginalFilename();
        if (!StringUtils.hasText(fileName) || !checkExcelName(fileName)) {
            logger.error("importFile is not xlsx or xls.");
            return resultInfo.failResult("导入文件要求必须是excel格式");
        }
        try {
            // 上传excel
            InputStream is = excelFile.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            int sheetNums = workbook.getNumberOfSheets();
            if (sheetNums==0){
                return resultInfo.failResult("excel文件没有表数据");
            }
            XSSFSheet craftSheet = workbook.getSheetAt(0);
            if (Objects.isNull(craftSheet)) {
                return resultInfo.failResult("导入的excel内容为空");
            }

            carService.importCarExcel(craftSheet,resultInfo);

        }catch (IOException ioe) {
            logger.error("batchImportCarInfo exist IOException", ioe);
            return resultInfo.failResult("后台数据流读取错误");
        }catch (Exception e){
            logger.error("batchImportCarInfo in carController exists error!", e);
            return resultInfo.failResult("导入文件失败");
        }
        return resultInfo;
    }

    /**
     * 下载汽车信息
     * */
    @RequestMapping("/downloadAllCar")
    public ResultInfo downloadAllCar(@RequestParam(value = "carNumber", required = false) String carNumber,
                                     @RequestParam(value = "phone", required = false) String phone,
                                     @RequestParam(value = "testStartDate", required = false) String testStartDate,
                                     @RequestParam(value = "testEndDate", required = false) String testEndDate,
                                     @RequestParam(value = "nextStartDate", required = false) String nextStartDate,
                                     @RequestParam(value = "nextEndDate", required = false) String nextEndDate,
                                     @RequestParam(value = "commonLog", required = false) String commonLog,
                                     @RequestParam(value = "state", required = false) String state,
                                     HttpServletResponse response){
        ResultInfo resultInfo = new ResultInfo();
        try(ServletOutputStream outputStream = response.getOutputStream()) {
            Workbook infoBook = new XSSFWorkbook();
            List<Car> carList = new ArrayList<>();
            carList = carService.getAllCar(carNumber,phone,testStartDate,testEndDate,nextStartDate,nextEndDate,commonLog,state);

            if (Objects.isNull(carList)||carList.size()<1){
                createExcel(infoBook,new ArrayList<>());
            }else {
                List<List<Object>> dataList = new ArrayList<>();
                for (Car car:carList){
                    List<Object> rowList = new ArrayList<>();
                    rowList.add(car.getCarNumber());
                    rowList.add(car.getPhone());
                    rowList.add(Objects.isNull(car.getTestDate())?"":car.getTestDate());
                    rowList.add(Objects.isNull(car.getNextDate())?"":car.getNextDate());
                    rowList.add(verseStateCode(car.getState()));
                    rowList.add(car.getCommonLog());
                    dataList.add(rowList);
                }
                createExcel(infoBook,dataList);
            }
            String excelName = String.valueOf(
                    URLEncoder.encode("检测车辆信息.xlsx", "UTF-8"));
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(
                            excelName.getBytes("gb2312"), "ISO8859-1"));
            response.setContentType("application/ms-excel;charset=UTF-8");
            infoBook.write(outputStream);
            outputStream.flush();
        }catch (Exception e){
            logger.error("downloadAllCar exists error!",e);
            resultInfo.setSuccess(false);
            resultInfo.setMessage("下载错误!");
            return resultInfo;
        }
        return null;
    }

    /**
     * 下载excel模板
    * */
    @RequestMapping("/downloadDemo")
    public ResultInfo downloadDemo(HttpServletResponse response){
        ResultInfo resultInfo = new ResultInfo();
        try(ServletOutputStream outputStream = response.getOutputStream()) {
            Workbook infoBook = new XSSFWorkbook();

            createExcel(infoBook,new ArrayList<>());

            String excelName = String.valueOf(
                    URLEncoder.encode("模板.xlsx", "UTF-8"));
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(
                            excelName.getBytes("gb2312"), "ISO8859-1"));
            response.setContentType("application/ms-excel;charset=UTF-8");

            infoBook.write(outputStream);
            outputStream.flush();
        }catch (Exception e){
            logger.error("downloadDemo exists error!",e);
            resultInfo.setSuccess(false);
            resultInfo.setMessage("下载错误!");
            return resultInfo;
        }
        return null;
    }

    public void createExcel(Workbook infoBook,List<List<Object>> dataList) {
        try {
            Sheet sheet = infoBook.createSheet();
            String[] titleInfo = new String[]{"车辆号牌","联系电话","检验日期","下次年审日期","状态","备注"};
            for (int i=0;i<titleInfo.length;i++){
                sheet.setColumnWidth(i, 16 * 256);
            }
            // 设置样式
            CellStyle titleStyle = createTitleStyle(infoBook);

            // 创建表头
            int rowNum = 0;
            Row firstRow = sheet.createRow(rowNum++);
            int firstRowCell = 0;

            // 设置状态为下拉框
            DataValidationHelper helper = sheet.getDataValidationHelper();
            String[] stateTypes = new String[] { "今年已来", "没有来" };
            CellRangeAddressList typeRegions = new CellRangeAddressList(1,100,4,4);
            DataValidationConstraint typeConstraint = helper.createExplicitListConstraint(stateTypes);
            DataValidation typeValidation = helper.createValidation(typeConstraint, typeRegions);
            sheet.addValidationData(typeValidation);


            //用户信息列
            for (String title : titleInfo) {
                Cell titleCell = firstRow.createCell(firstRowCell++);
                createRow(infoBook,title,titleCell,titleStyle);
            }

            CellStyle dataStyle = createDataStyle(infoBook);

            for (List<Object> rowData:dataList){
                Row dataRow = sheet.createRow(rowNum++);
                int cellNum = 0;
                for (Object data:rowData){
                    Cell titleCell = dataRow.createCell(cellNum++);
                    createRow(infoBook,data,titleCell,dataStyle);
                }
            }

        } catch (Exception e) {
            logger.error("createUserInfoExcel exists exception", e);
        }
    }

    private CellStyle createTitleStyle(Workbook workBook) {
        CellStyle cellStyle = workBook.createCellStyle();
        // 设置样式
        Font font = workBook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 13);
        font.setColor((short) 44);
        font.setBold(true);
        cellStyle.setFont(font);
        return cellStyle;
    }

    private CellStyle createDataStyle(Workbook workBook) {
        CellStyle cellStyle = workBook.createCellStyle();
        // 设置样式
        Font font = workBook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 12);
        font.setBold(false);
        cellStyle.setFont(font);
        return cellStyle;
    }

    private void createRow(Workbook workbook,Object cellValue, Cell cell,
                           CellStyle cellStyle) {
        // 设置单元格内容
        if (cellValue instanceof Integer){
            cell.setCellValue((Integer)cellValue);
        }else if (cellValue instanceof String){
            cell.setCellValue((String)cellValue);
        }else if (cellValue instanceof Double){
            cell.setCellValue((Double)cellValue);
        }else if (cellValue instanceof Float) {
            cell.setCellValue((Float)cellValue);
        } else if (cellValue instanceof Long) {
            cell.setCellValue((Long)cellValue);
        } else if (cellValue instanceof Boolean) {
            cell.setCellValue((Boolean)cellValue);
        } else if (cellValue instanceof Date) {
            cellStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            short dateFormat = createHelper.createDataFormat().getFormat("yyyy-MM-dd");
            cellStyle.setDataFormat(dateFormat);
            cell.setCellValue((Date)cellValue);
        }

            // 设置样式
        cell.setCellStyle(cellStyle);
    }

    private boolean checkExcelName(String excelName) {
        String s = StringUtils.getFilenameExtension(excelName);
        return "xls".equals(s) || "xlsx".equals(s);
    }

    /**
     * 设置返回信息
     * */
    public void setReturnMessage(ResultInfo resultInfo,boolean isSuccess,String message){
        resultInfo.setSuccess(isSuccess);
        resultInfo.setMessage(message);
    }

    public String verseStateCode(int code){
        if (1==code){
            return "今年已来";
        }else if (2==code){
            return "没有来";
        }else {
            return "-";
        }
    }
}
