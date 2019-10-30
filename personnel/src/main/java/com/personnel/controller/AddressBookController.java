package com.personnel.controller;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.personnel.core.base.BaseController;
import com.personnel.core.base.BaseService;
import com.personnel.domian.output.AddressBookOutput;
import com.personnel.domian.output.EmployeesOutput;
import com.personnel.model.AddressBook;
import com.personnel.model.Plate;
import com.personnel.service.AddressBookService;
import com.personnel.service.EmployeesService;
import com.personnel.service.PlateService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping(value = "/addressbook")
@Api(value = "通讯录controller",description = "通讯录操作",tags = {"通讯录操作接口"})
public class AddressBookController extends BaseController<AddressBookOutput, AddressBook,Integer> {
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private PlateService plateService;
    @Autowired
    private EmployeesService employeesService;

    @Override
    public BaseService<AddressBookOutput, AddressBook, Integer> getService() {
        return addressBookService;
    }


    @Override
    @ApiOperation("新增或修改信息")
    @RequestMapping(value = "form", method = RequestMethod.POST)
    public ResponseResult formPost(Integer id,@RequestBody @ApiParam(name="通讯录信息",value="传入json格式",required=true) AddressBook addressBook) throws Exception {
        if(addressBook.getPlateNoList()!=null&&addressBook.getPlateNoList().size()>0){
            StringBuilder a=new StringBuilder();
            for(String s:addressBook.getPlateNoList()){
                a.append(s+",");
            }
            addressBook.setPlateNo(a.toString().substring(0,a.toString().length()-1));
        }else {
            addressBook.setPlateNo("");
        }
        if(addressBook.getName()==null||addressBook.getName().equals("")){
            return ResponseResult.error("姓名不能为空");
        }
        if(addressBook.getSex()==null||addressBook.getSex().equals("")){
            return ResponseResult.error("性别不能为空");
        }
        if(addressBook.getPhoneNumber()==null||addressBook.getPhoneNumber().equals("")){
            return ResponseResult.error("手机号不能为空");
        }
        if(addressBook.getPlateNo()!=null&&!addressBook.getPlateNo().equals("")){
            PageData pageData=new PageData();
            String[] strings=addressBook.getPlateNo().split(",");
            if(strings!=null&&strings.length>0){
                for(String s:strings){
                    pageData.clear();
                    pageData.put("plateNo",s);
                    List<EmployeesOutput> list=employeesService.findByplateNo(pageData);
                    if(list!=null&&list.size()>0){
                        return ResponseResult.error("车牌号重复");
                    }
                    pageData.put("id",id);
                    AddressBookOutput output=addressBookService.getByPlateNo(pageData);
                    if(output!=null){
                        return ResponseResult.error("车牌号重复");
                    }
                }
            }
        }
        AddressBook addressBook1=new AddressBook();//原数据
        if(id!=null){
            addressBookService.getById(id);
        }
        String[] string1=null;//原数据的车牌号数据
        String[] string2=null;//编辑过后的数据的车牌号数组
        if(addressBook1!=null){
            if(addressBook1.getPlateNo()!=null&&!addressBook1.getPlateNo().equals("")){
                string1=addressBook1.getPlateNo().split(",");
            }
        }
        if(addressBook.getPlateNo()!=null&&!addressBook.getPlateNo().equals("")){
            string2=addressBook.getPlateNo().split(",");
        }
        if(string1!=null&&string1.length>0){
            for(String string:string1){
                if(addressBook.getPlateNo()==null||addressBook.getPlateNo().indexOf(string)<0){//判断是否删除车牌
                    List<Plate> plateList=plateService.getByPlateNo(string);
                    if(plateList!=null&&plateList.size()>0){
                        Plate plate=plateList.get(0);
                        plate.setState(3);
                        if(plateService.update(plate.getId(),plate)<0){
                            return ResponseResult.error("编辑车牌号失败");
                        }
                    }
                }
            }
        }
        if(string2!=null&&string2.length>0){
            for(String string:string2){
                if(addressBook1==null||addressBook1.getPlateNo()==null||addressBook1.getPlateNo().indexOf(string)<0){//判断是否新增车牌
                    Plate plate=new Plate();
                    plate.setState(0);
                    plate.setPersonId(0);
                    plate.setPlateNo(string);
                    plate.setPhoneNo(addressBook.getPhoneNumber());
                    plate.setPersonNo("临时车牌");
                    plate.setName(addressBook.getName());
                    if(plateService.add(plate)<0){
                        return ResponseResult.error("新增车牌号失败");
                    }
                }
            }
        }
        return super.formPost(id,addressBook);
    }


    @Override
    @ApiOperation("删除通讯录信息")
    @GetMapping(value = "logicDelete")
    public ResponseResult logicDelete(String idList) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        return super.logicDelete(idList);
    }


    @Override
    @GetMapping(value = "get")
    public ResponseResult get(Integer id){
        if( id==null ){
            ResponseResult.error(PARAM_EORRO);
        }
        return ResponseResult.success(addressBookService.getByAddressBookId(id));
    }


    @GetMapping(value = "findPageList")
    @ApiOperation("获取分页的员工列表")
    @ApiImplicitParams({@ApiImplicitParam(name="name",value="员工姓名",required=false,dataType="string", paramType = "query")})
    public ResponseResult selectPageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        pageData.put("createdUserId",getService().getUsers().getId()+"");
        return super.selectPageList(pageData);
    }

    @GetMapping(value = "findList")
    @ApiImplicitParams({@ApiImplicitParam(name="name",value="员工姓名",required=false,dataType="string", paramType = "query")})
    public ResponseResult findList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        return super.selectPageList(pageData);
    }




}
