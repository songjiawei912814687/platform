package com.message.service;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.message.core.base.BaseMapper;
import com.message.core.base.BaseService;
import com.message.core.base.MybatisBaseMapper;
import com.message.mapper.jpa.EmpTelRepository;
import com.message.mapper.mybatis.EmpTelMapper;
import com.message.model.EmpTel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmpTelService extends BaseService<EmpTel,EmpTel,Integer> {

    @Autowired
    private EmpTelRepository empTelRepository;
    @Autowired
    private EmpTelMapper empTelMapper;


    @Override
    public BaseMapper<EmpTel, Integer> getMapper() {
        return null;
    }

    @Override
    public MybatisBaseMapper<EmpTel> getMybatisBaseMapper() {
        return null;
    }



    public ResponseResult selectPageInfo(PageData pageData){
        var emp = empTelMapper.selectPage(pageData);
        return ResponseResult.success(emp);
    }



    //上传excel到服务器
    public String checkedFile(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        if (!suffixName.equals(".xls") && !suffixName.equals(".xlsx")) {
            return "文件类型错误";
        }
        int index = fileName.lastIndexOf("\\");
        if (index != -1) {
            fileName = fileName.substring(index + 1);
        }
        int hashCode = fileName.hashCode();
        //把hash值转换为十六进制
        String hex = Integer.toHexString(hashCode);
        String pathHeader = "D:\\\\HZFY\\\\telPhone";
        StringBuilder sb = new StringBuilder();
        sb.append("/" + hex.charAt(0));
        sb.append("/" + hex.charAt(1));
        sb.append("/" + System.currentTimeMillis() + "_" + fileName);
        String pathEnd = sb.toString();
        File fileInfo = new File(pathHeader + pathEnd);
        if (!fileInfo.getParentFile().exists()) {
            fileInfo.getParentFile().mkdirs();
        }
        file.transferTo(fileInfo);
        Workbook wb = null;
        try {
            wb = new HSSFWorkbook(new FileInputStream(pathHeader + pathEnd));
        } catch (Exception e) {
            wb = new XSSFWorkbook(new FileInputStream(pathHeader + pathEnd));
        }
        return importTEL(wb);

    }

    //把excel数据导入到数据库中
    public String importTEL(Workbook wb) throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        // 得到Excel的行数
        int rows = sheet.getPhysicalNumberOfRows();
        int totalCells = 0;
        // 得到Excel的列数(前提是有行数)
        if (rows > 1 && sheet.getRow(0) != null) {
            totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        List<EmpTel> list = new ArrayList<EmpTel>();
        for (int r = 1; r < rows; r++) {
            EmpTel emp = new EmpTel();
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            var a = row.getCell(0);
            var name = "";
            try {
                name = String.valueOf(a.getStringCellValue()).trim();
            } catch (NullPointerException e) {
                break;
            }
            if (name == null || "".equals(name)) {
                continue;
            }
            // 循环Excel的列
            for (int c = 0; c < totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String value = String.valueOf(cell.getStringCellValue()).trim();
                    if(!value.equals("") && value != null){
                        if(c==0){
                            emp.setKeyWord(value);
                        }else if(c==1) {
                            emp.setMobilPhone(value);
                        }
                    }
                }
                list.add(emp);
            }
            Integer size = empTelRepository.saveAll(list).size();
            if(size<0){
                return "系统错误";

            }
        }
        return "操作成功";
    }

}
