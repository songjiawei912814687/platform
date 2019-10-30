package com.knowledge.service;

import com.common.Enum.MessageTypeEnum;
import com.common.model.PageData;
import com.common.request.ServiceCall;
import com.common.response.ResponseResult;
import com.common.utils.ExportExcel;
import com.common.utils.GetMessageTemplate;
import com.google.common.collect.Lists;
import com.knowledge.core.base.BaseMapper;
import com.knowledge.core.base.BaseService;
import com.knowledge.core.base.MybatisBaseMapper;
import com.knowledge.domain.output.QuestionOutput;
import com.knowledge.enums.QuestionTypeEnum;
import com.knowledge.mapper.jpa.OrganizationRepository;
import com.knowledge.mapper.jpa.QuestionRepository;
import com.knowledge.mapper.mybatis.QuestionMapper;
import com.knowledge.model.Organization;
import com.knowledge.model.Question;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService extends BaseService<QuestionOutput,Question,Integer> {

    @Autowired
    private QuestionRepository repository;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private OrganizationRepository organizationRepository;


    @Override
    public BaseMapper<Question, Integer> getMapper() {
        return repository;
    }

    @Override
    public MybatisBaseMapper<QuestionOutput> getMybatisBaseMapper() {
        return questionMapper;
    }

    public List<QuestionOutput> getAllByTitle(String title) {
        PageData pageData=new PageData();
        pageData.put("title",title);
        return questionMapper.selectAll(pageData);
    }

    public int updateIsTop(Integer id,Integer isTop) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
      Question question=getById(id);
      Integer result=0;
      if(question!=null){
          question.setIsTop(isTop);
          result=update(id,question);
      }
      return result;
    }

    public int updateIsOpen(Integer id,Integer isOpen) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        Question question=getById(id);
        Integer result=0;
        if(question!=null){
            question.setIsOpen(isOpen);
            result=update(id,question);
        }
        return result;
    }


    public String findTemplateByKey(String type) {
        PageData pageData = new PageData();
        pageData.put("type", type);
        var result = ServiceCall.getResult(loadBalancerClient, "/smstemplate/findByType", "message", pageData);
        if (result.getCode() != 200) {
            return null;
        }
        return (String) result.getData();
    }


    public String getContent(String type, String name) {
        String content = findTemplateByKey(type);
        if (content != null) {
            var map = GetMessageTemplate.getKey(content);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                switch (entry.getKey()) {
                    case "bmfzr"://部门管理员
                        entry.setValue(name);
                        break;
                    case "twr"://提问人
                        entry.setValue(name);
                        break;
                }
            }
            return GetMessageTemplate.getContent(content, map);
        }
        return null;
    }
    public String getContent1(String type, String name) {
        String content = findTemplateByKey(type);
        if (content != null) {
            var map = GetMessageTemplate.getKey(content);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                switch (entry.getKey()) {
                    case "twr"://部门管理员
                        entry.setValue(name);
                        break;
                }
            }
            return GetMessageTemplate.getContent(content, map);
        }
        return null;
    }



    public void sendMassage(List<Map<String,String>> result, String type) {
        PageData pageData=new PageData();
        if(result!=null&&result.size()>0){
            for(Map output:result){
                List<String> list = new ArrayList<>();
                list.add((String)output.get("phoneNumber") + "/" + (String)output.get("name"));
                String description=getContent(type, (String)output.get("name"));
                if(description!=null&&!description.equals("")){
                    pageData.put("description", description);
                    pageData.put("isTiming", 0);
                    pageData.put("type", MessageTypeEnum.INFO_DEPART_MANAGER.getCode());
                    pageData.put("sendList", list);
                    try {
                        var r = ServiceCall.postResult(loadBalancerClient, "/smsSend/form", "message", pageData);
                    } catch (Exception e) {

                    }
                }
            }
        }

    }


    public ResponseResult selectByAnswerState(PageData pageData) {
        List<QuestionOutput> list = questionMapper.findAllByAnswerState(pageData);
        return ResponseResult.success(list);
    }

    public boolean verificationOrg(Question question) {
        List<Integer> list =  questionMapper.selectOrgByParentId(question.getOrganizationId());
        if(list!=null&&list.size()>0){
            return false;
        }else{
            return true;
        }
    }

    public String export(HttpServletResponse response, HttpServletRequest request) throws Exception {

        String title = "问题解答详情表";
        String excelName = "问题解答详情表";
        String[] rowsName = new String[]{"序列","部门","问题","回答"};
        List<Object[]> dataList = new ArrayList<>();
        Object[] objs = null;
        PageData pageData = new PageData(request);

        List<QuestionOutput> questionOutputList =  questionMapper.selectAll(pageData);
        if(!CollectionUtils.isEmpty(questionOutputList)){
            int i=1;

            for(QuestionOutput questionOutput:questionOutputList){
                objs = new Object[rowsName.length];
                objs[0] = i;
                objs[1] = questionOutput.getOrganizationName();
                objs[2] = questionOutput.getTitle();
                objs[3] = questionOutput.getDescription();
                dataList.add(objs);
                i++;
            }
        }
        ExportExcel ex = new ExportExcel(title, rowsName, dataList, excelName);
        return ex.export(response, request);
    }

    public ResponseResult selectQustionOrganizations(PageData pageData) {
        return  ResponseResult.success(questionMapper.selectQustionOrganizations(pageData).stream().distinct().collect(Collectors.toList()));
    }

    @Transactional
    public ResponseResult checkedFile(MultipartFile file) throws IOException, IntrospectionException, IllegalAccessException, InvocationTargetException {
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf(".")).toLowerCase().trim();
        if(!suffixName.equals(".xls")&&!suffixName.equals(".xlsx")){
            return ResponseResult.error("数据表格式不正确");
        }
        int index = fileName.lastIndexOf("\\");
        if (index != -1) {
            fileName = fileName.substring(index + 1);
        }
        int hashCode = fileName.hashCode();
        //把hash值转换为十六进制
        String hex = Integer.toHexString(hashCode);
        String pathHeader = "C:";
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
        return importQuestion(wb);
    }

    @Transactional(rollbackFor = Exception.class)
     ResponseResult importQuestion(Workbook wb) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        // 得到第一个sheet
        Sheet sheet = wb.getSheetAt(0);
        // 得到Excel的行数
        int rows = sheet.getPhysicalNumberOfRows();
        int totalCells = 0;
        // 得到Excel的列数(前提是有行数)
        if (rows > 1 && sheet.getRow(0) != null) {
            totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        List<Question>questionList = Lists.newArrayList();
        for (int r = 1; r <rows; r++) {
            Row row = sheet.getRow(r);
            Question question = new Question();
            if (StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
                continue;
            }
            // 循环Excel的列
            for (int c = 0; c < totalCells; c++) {
                Cell cell = row.getCell(c);
                if(cell != null) {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String value = String.valueOf(cell.getStringCellValue()).trim();
                    if (StringUtils.isNotBlank(value)) {
                        Organization organization = null;
                        if (c == 0) {
                            //先去数据库查，如果存在就不导入
                            List<Question> allByTitle = questionRepository.findAllByTitleAndAmputated(value, 0);
                            if (!CollectionUtils.isEmpty(allByTitle)) {
                                continue;
                            }
                            //设置问题标题
                            question.setTitle(value);
                        } else if (c == 1) {
                            //设置问题类型
                            question.setType(QuestionTypeEnum.getCode(value));
                        } else if (c == 2) {
                            //设置答案
                            question.setDescription(value);
                        } else if (c == 3) {
                            //设置部门
                            organization = organizationRepository.findByName(value);
                            if (null == organization) {
                                return ResponseResult.error("第" + r + "行,第四列所属部门填写错误");
                            }
                            question.setOrganizationId(organization.getId());
                        } else if (c == 4) {
                            //提问人姓名
                            question.setAskName(value);
                        } else if (c == 5) {
                            //提问人手机号码
                            question.setAskTel(value);
                        }
                    }
                }
            }
            //不开放
            question.setIsOpen(0);
            //不置顶
            question.setIsTop(0);
            //未发布
            question.setState(0);
            if(StringUtils.isBlank(question.getDescription())){
                //没有答案未回答
                question.setAnswerState(0);
            }else {
                //有答案已经回答
                question.setAnswerState(1);
            }
            setProperty(question, "createdUserId", getUsers().getId());
            setProperty(question, "createdUserName", getUsers().getUsername());
            setProperty(question, "createdDateTime", new Date());
            setProperty(question, "lastUpdateUserId", getUsers().getId());
            setProperty(question, "lastUpdateUserName", getUsers().getUsername());
            setProperty(question, "lastUpdateDateTime", new Date());
            setProperty(question,"amputated",0);
            questionList.add(question);
        }
        Integer size = questionRepository.saveAll(questionList).size();
        if(size<=0){
            return ResponseResult.error("导入失败");
        }
        return ResponseResult.success();
    }
}
