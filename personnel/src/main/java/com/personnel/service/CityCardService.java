package com.personnel.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.common.utils.HttpRequestUtil;
import com.google.common.collect.Maps;
import com.personnel.core.base.BaseMapper;
import com.personnel.core.base.BaseService;
import com.personnel.core.base.MybatisBaseMapper;
import com.personnel.domian.output.EmployeesOutput;
import com.personnel.mapper.jpa.CityCardRepository;
import com.personnel.mapper.mybatis.CityCardMapper;
import com.personnel.mapper.mybatis.EmployeesMapper;
import com.personnel.model.CityCard;
import com.personnel.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.transaction.Transactional;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CityCardService extends BaseService<CityCard,CityCard,Integer> {
    @Autowired
    private CityCardRepository repository;

    @Autowired
    private CityCardMapper cityCardMapper;

    @Autowired
    private EmployeesMapper employeesMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${hik.addCard_URL}")
    private String addCard_URL;

    @Value("${hik.openCard_URL}")
    private String openCard_URL;

    @Autowired
    private PersonService personService;
    @Override
    public BaseMapper<CityCard, Integer> getMapper() {
        return repository;
    }

    @Override
    public MybatisBaseMapper<CityCard> getMybatisBaseMapper() {
        return cityCardMapper;
    }

    @Transactional
    public void addCityCard() throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        PageData pageData = new PageData();
        //TODO 为了避免employeesService 的bean的重复注入报错，所以我把empService的注入换成了employeesMapper的注入

        List<EmployeesOutput> employeesOutputList=employeesMapper.selectAll(pageData);
        if(employeesOutputList!=null&&employeesOutputList.size()>0){
            for(EmployeesOutput employeesOutput:employeesOutputList){
                Person person=personService.getByEmployeeId(employeesOutput.getId());
                if(employeesOutput.getCitizenCardPhysicalAddress()!=null
                        &&!employeesOutput.getCitizenCardPhysicalAddress().equals("")
                        &&person!=null&&!employeesOutput.getCitizenCardPhysicalAddress().equals("0")){
                    CityCard cityCard=new CityCard();
                    String s=employeesOutput.getCitizenCardPhysicalAddress();
                    String s1=null;
                    if(isOctNumber(s)){
                        s1=s;
                    }else {
                        s1=Long.parseLong(s,16)+"";
                    }
                    cityCard.setState(0);
                    cityCard.setPhoneNo(employeesOutput.getPhoneNumber());
                    cityCard.setPersonId(person.getPersonId());
                    cityCard.setPersonNo(person.getPersonNo());
                    cityCard.setName(person.getName());
                    cityCard.setCreatedDateTime(new Date());
//                    cityCard.setLastUpdateDateTime(new Date());
                    StringBuilder sb=new StringBuilder();
                    if(s1.length()<10){
                        int a=s1.length();
                        for(int i=0;i<10-a;i++){
                            sb.append("0");
                        }
                    }
                    sb.append(s1);
                    s1=sb.toString();
                    List<CityCard> cityCards=cityCardMapper.selectByityCardNo(s1);
                    if(cityCards==null||cityCards.size()<1){
                        cityCard.setCityCardNo(s1);
                        add(cityCard);
                    }
                }
            }
        }
    }



    public void addCityCards(){
        List<CityCard> list=cityCardMapper.selectByState(0);
        if(list!=null&&list.size()>0){
            for (CityCard cityCard:list){
                try {
                    Map<String, Object> param = Maps.newHashMap();
                    param.put("personNo", cityCard.getCityCardNo());
                    HttpRequestUtil.sendPostRequest(addCard_URL,param);
                    param.put("personId", cityCard.getPersonId());
                    param.put("personNo", cityCard.getCityCardNo());
                    ResponseResult responseResult = HttpRequestUtil.sendPostRequest(openCard_URL,param);

                    if(responseResult.getCode() == 200){
                        cityCard.setState(1);
                        cityCard.setCreatedDateTime(new Date());
                        cityCard.setLastUpdateDateTime(new Date());
                        update(cityCard.getId(),cityCard);
                        logger.info("添加市民卡到海康成功");
                    }
                }catch (Exception e){
                    logger.error("添加市民卡号到海康失败");
                    e.printStackTrace();
                }
            }
        }
    }


    public List<CityCard> getByPersonId(Integer personId){
        return cityCardMapper.selectByPersonId(personId);
    }


    private static boolean isOctNumber(String str) {
        boolean flag = false;
        for(int i=0,n=str.length();i<n;i++){
            char c = str.charAt(i);
            if(c=='0'||c=='1'||c=='2'||c=='3'||c=='4'||c=='5'||c=='6'||c=='7'||c=='8'||c=='9'){
                flag=true;
            }else {
                flag=false;
                break;
            }
        }
        return flag;
    }




}
