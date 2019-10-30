package com.personnel.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.common.model.PageData;
import com.personnel.mapper.jpa.DeptRepository;
import com.personnel.mapper.mybatis.DeptMapper;
import com.personnel.model.Dept;
import com.common.response.ResponseResult;
import com.common.utils.HttpRequestUtil;
import com.google.common.collect.Maps;
import com.personnel.core.base.BaseMapper;
import com.personnel.core.base.BaseService;
import com.personnel.core.base.MybatisBaseMapper;
import com.personnel.domian.output.PersonOutput;
import com.personnel.mapper.jpa.PersonRepository;
import com.personnel.mapper.mybatis.PersonMapper;
import com.personnel.mapper.mybatis.PlateMapper;
import com.personnel.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PersonService extends BaseService<Person, Person,Integer> {

    @Autowired
    private DeptRepository deptRepository;

    @Autowired
    private PersonRepository repository;

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private PlateMapper plateMapper;

    @Autowired
    private PlateService plateService;

    @Autowired
    private CityCardService cityCardService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${hik.addPerson_URL}")
    private String addPerson_URL;

    @Value("${hik.addCard_URL}")
    private String addCard_URL;

    @Value("${hik.openCard_URL}")
    private String openCard_URL;

    @Value("${hik.returnCard_URL}")
    private String returnCard_URL;

    @Value("${hik.returnCard2_URL}")
    private String returnCard2_URL;

    @Value("${hik.deletePerson_URL}")
    private String deletePerson_URL;

    @Override
    public BaseMapper<Person, Integer> getMapper() {
        return repository;
    }
    @Override
    public MybatisBaseMapper<Person> getMybatisBaseMapper() {
        return null;
    }

    public Person findByEmployeeId(Integer id){
        return  repository.findByEmployeeId(id);
    }

    public void addPerson(){
        List<PersonOutput> people=personMapper.selectByState1(0);
        if(people!=null&&people.size()>0){
            for(Person person:people) {
                Map<String, Object> param = Maps.newHashMap();
                param.put("cardNo", person.getPersonNo());
                param.put("name", person.getName());
                param.put("personNo", person.getPersonNo());
                param.put("gender", person.getGender());
                param.put("phoneNo", person.getPhoneNo());
                param.put("path",person.getPath());
                Dept dept = deptRepository.findByDeptNo(person.getDeptNo());
                if(dept != null){
                    person.setDeptUuid(dept.getDeptUuid());
                    if(person.getDeptNo().equals(dept.getDeptNo())){
                        param.put("deptUuid",person.getDeptUuid());
                    }
                }
                try {
//
//                    strs = getDeptInfosByName(person);
//                    Map<String, Object> jsonObject = JSONObject.parseObject(strs);
//                    Map<String, Object> str = (Map<String, Object>) jsonObject.get("data");
//                    try {
//                        com.alibaba.fastjson.JSONArray lists = (JSONArray) str.get("list");
//                        StringBuilder stringBuilder = new StringBuilder();
//                        for (int i = 0; i < lists.size(); i++) {
//                            //address.get(int i)是获取json格式”[]”中的值，i是下标
//                            Map<String, Object> map = (Map<String, Object>) lists.get(i);
//                            if (map.get("deptUuid") != null) {
//                                stringBuilder.append(map.get("deptUuid")).append(" ");
//                            }
//                        }
//                        dept_Uuid = stringBuilder.toString();


                    //海康添加人员
                    ResponseResult responseResult = HttpRequestUtil.sendPostRequest(addPerson_URL, param);

                    net.sf.json.JSONObject object = net.sf.json.JSONObject.fromObject(responseResult.getData());
                    Integer code = responseResult.getCode();
                    String errorMessage = object.getString("errorMessage");

                    Map <String ,Object> map =(Map <String ,Object>)object.get("data");
                    Integer personId =null;
                    if(map!=null){
                        personId=(Integer) map.get("personId");
                    }
//                       HttpRequestUtil.sendPostRequest(addCard_URL,param);

                    if(personId==null||personId.equals("")){
                        person.setState(2);
                        person.setDescription(errorMessage);
                        person.setCreatedDateTime(new Date());
                        person.setLastUpdateDateTime(new Date());
                        logger.error("人员下发海康平台失败");
                        return;
                    }else {
                        person.setPersonId(personId);
                        person.setState(1);
                        person.setDescription(errorMessage);
                        person.setCreatedDateTime(new Date());
                        person.setLastUpdateDateTime(new Date());
                        if(update(person.getId(),person)<=0){
                            logger.error("更新关联表失败");
                        }else {
                            List<Plate> list=plateMapper.selectByPersonNo(person.getPersonNo());
                            if(list!=null&&list.size()>0){
                                for(Plate plate:list){
                                    plate.setPersonId(personId);
                                    plateService.update(plate.getId(),plate);
                                }
                            }
                            if(code == 200){
                                //添加一张新的以员工工号作为卡号的卡片
                                HttpRequestUtil.sendPostRequest(addCard_URL,param);
                                param.put("personId",personId);
                                param.put("personNo",person.getPersonNo());
                                //海康卡片开卡(将添加的卡片绑定到添加的人员)
                                HttpRequestUtil.sendPostRequest(openCard_URL,param);
                            }else {
                                person.setState(2);
                                person.setDescription("人员"+personId+"添加失败");
                                person.setCreatedDateTime(new Date());
                                person.setLastUpdateDateTime(new Date());
                            }
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    logger.info("人员下发海康平台失败");
                }
            }
        }
    }

    public void deletePerson() {
        List<Person> people=personMapper.selectByState(3);
        if(people!=null&&people.size()>0){
            for(Person person:people) {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("personId",person.getPersonId());//员工工号作为用户登录名传入,不能重复
                param.put("personNo",person.getPersonNo());//员工工号作为卡号传入,不能重复
                try{
                    //退卡
                    HttpRequestUtil.sendPostRequest(returnCard_URL,param);

                    List<CityCard> cityCard=cityCardService.getByPersonId(person.getPersonId());
                    if(cityCard!=null&&cityCard.size()>0){
//                    param.put("personId",cityCard.get(0).getPersonId());
                        for(int i =0;i < cityCard.size();i++){
                            param.put("cityCardNo",cityCard.get(i).getCityCardNo());
                            HttpRequestUtil.sendPostRequest(returnCard2_URL,param);
                        }
//                    param.put("cityCardNo",cityCard.get(0).getCityCardNo());
//                    HttpRequestUtil.sendPostRequest(returnCard2_URL,param);
                    }
                    //删除海康平台人员
                    ResponseResult responseResult = HttpRequestUtil.sendPostRequest(deletePerson_URL,param);
                    net.sf.json.JSONObject object = net.sf.json.JSONObject.fromObject(responseResult.getData());
                    Integer code = responseResult.getCode();
                    String errorMessage = object.getString("data");
                    if(code == 200){
                        person.setState(5);
                        person.setDescription(errorMessage);
                        person.setCreatedDateTime(new Date());
                        person.setLastUpdateDateTime(new Date());
                        logger.info("从海康平台删除人员成功");
                    }else{
                        person.setDescription(errorMessage);
                        person.setState(2);
                        person.setCreatedDateTime(new Date());
                        person.setLastUpdateDateTime(new Date());
                    }
                    if(update(person.getId(),person)<=0){
                        logger.error("更新关联表失败");
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    logger.info("从海康平台删除人员失败");
                }
            }
        }
    }

    public Person getByEmployeeId(Integer employeeId){
        return personMapper.selectByEmployeeId(employeeId);
    }
}
