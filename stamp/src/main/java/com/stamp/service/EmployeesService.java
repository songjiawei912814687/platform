package com.stamp.service;

import com.common.Enum.MessageTypeEnum;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.common.utils.ExportExcel;
import com.common.utils.HttpRequestUtil;
import com.common.utils.Img2Base64Util;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.stamp.core.base.BaseRepository;
import com.stamp.core.base.BaseService;
import com.stamp.core.base.MybatisBaseMapper;
import com.stamp.domain.output.EmployeesOutput;
import com.stamp.mapper.jpa.EmployeesRepository;
import com.stamp.mapper.mybatis.EmployeesMapper;
import com.stamp.model.Employees;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("employeesService")
public class EmployeesService extends BaseService<EmployeesOutput, Employees, Integer> {

    @Autowired
    private EmployeesRepository repository;

    @Autowired
    private EmployeesMapper employeesMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Override
    public BaseRepository<Employees, Integer> getMapper() {
        return null;
    }

    @Override
    public MybatisBaseMapper<EmployeesOutput> getMybatisBaseMapper() {
        return employeesMapper;
    }



    public List<EmployeesOutput> selectByPath(PageData pageData) {
        Integer pagesize = pageData.getRows();
        Integer page = pageData.getPageIndex();
        PageHelper.startPage(page, pagesize);
        Page<EmployeesOutput> pageList = employeesMapper.selectByPath(pageData);
        return pageList;
    }


    public List<EmployeesOutput> selectByOrgId(PageData pageData){
        Integer pagesize = pageData.getRows();
        Integer page = pageData.getPageIndex();
        PageHelper.startPage(page, pagesize);
        Page<EmployeesOutput> pageList = employeesMapper.selectByOrgId(pageData);
        return pageList;
    }

    public EmployeesOutput getByEmployeeId(Integer id) {
        EmployeesOutput employeesOutput = employeesMapper.selectByPrimaryKey(id);
        if (employeesOutput != null) {
            if (employeesOutput.getPlateNo() != null && !employeesOutput.getPlateNo().equals("")) {
                String[] s = employeesOutput.getPlateNo().split(",");
                List<String> list = new ArrayList<String>();
                if (s != null && s.length > 0) {
                    for (String s1 : s) {
                        list.add(s1);
                    }
                }
                employeesOutput.setPlateNoList(list);
            }
        }
        return employeesOutput;
    }
}
