package com.personnel.controller;


import com.common.Enum.ApprovalTypeEnum;
import com.common.model.PageData;
import com.common.request.Audit;
import com.common.response.ResponseResult;
import com.github.pagehelper.PageInfo;
import com.personnel.core.base.BaseController;
import com.personnel.core.base.BaseService;
import com.personnel.domian.input.EmployeesInput;
import com.personnel.domian.input.PhysicalAddressInput;
import com.personnel.domian.input.PlateNoInput;
import com.personnel.domian.output.EmployeesOutput;
import com.personnel.model.Employees;
import com.personnel.model.Organization;
import com.personnel.model.Person;
import com.personnel.model.Plate;
import com.personnel.service.*;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping(value = "/employees")
@Api(value = "人员controller",description = "人员操作",tags = {"人员操作接口"})
public class EmployeesController extends BaseController<EmployeesOutput, Employees,Integer> {

    @Autowired
    private EmployeesService employeesService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private PlateService plateService;

    @Autowired
    private PersonService personService;
    @Autowired
    private FoodSystemService foodSystemService;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public BaseService<EmployeesOutput,Employees, Integer> getService() {
        return employeesService;
    }

    @Override
    @ApiOperation("新增或修改人员信息")
    @RequestMapping(value = "form", method = RequestMethod.POST)
    public ResponseResult formPost(Integer id, @Validated @RequestBody @ApiParam(name="人员信息",value="传入json格式",required=true) Employees employees) throws Exception {
        if(!employeesService.verificationOrg(employees.getOrganizationId(),null)){
            return ResponseResult.error("必须将员工关联末级组织");
        }
        if(employees.getPlateNoList()!=null&&employees.getPlateNoList().size()>0){
            StringBuilder a=new StringBuilder();
            for(String s:employees.getPlateNoList()){
                a.append(s+",");
            }
            employees.setPlateNo(a.toString().substring(0,a.toString().length()-1));
        }else {
            employees.setPlateNo("");
        }
        if(employees.getPartyMemberState()<3){
            if(employees.getPartyBranch()==null||employees.getPartyBranch().equals("")||employees.getJoinPartyDate()==null
                    ||employees.getJoinPartyDate().equals("")){
                return ResponseResult.error("所在党支部或入党时间不能为空");
            }
        }
        if(id!=null){
            if(employees.getEmployeeNo()==null||employees.getEmployeeNo().length()<=0||employees.getEmployeeNo().length()>55){
                return ResponseResult.error("工号不能为null且长度不超过55");
            }
        }
        if(employeesService.isRepeatCitizenCards(id,employees)){
            return ResponseResult.error("市民卡号重复");
        }
        if(employeesService.isRepeatIdCard(id,employees)){
            return ResponseResult.error("身份证号码重复");
        }
        if(employeesService.isRepeatPhoneNumber(id,employees)){
            return ResponseResult.error("手机号码重复");
        }
        if(id==null){
            String t=employeesService.getMaxNo(employees.getOrganizationId());
            employees.setEmployeeNo(t);
            employees.setActivationId(0);
            employees.setInductionDateTime(new Date());
            employees.setWorkingState(0);
            id=employeesService.add(employees);
            //同步到员工信息中间表
            employees.setId(id);
            employeesService.saveStationEmployees(employees,0);
            if(id<0){
                return  ResponseResult.error(SYS_EORRO);
            }
            if(Audit.apply(loadBalancerClient,id,getService().getUsers().getId(), ApprovalTypeEnum.EMPLOYEE_INDUCTION_TYPE.getCode())){
                return ResponseResult.success();
            }else {
                this.delete(id.toString());
                //从员工信息中间表删除这条数据
                employeesService.deleteStationEmployees(employees);
                return ResponseResult.error("提交审批失败");
            }
        }else {

            if(employeesService.update(id,employees)<0){
                return  ResponseResult.error(SYS_EORRO);
            }
            employees.setId(id);
            //如果人员跟新成功,1.排队叫号跟新
            employeesService.updateQue(employees);
            //如果人员更新成功，人员中间表更新
            employeesService.saveStationEmployees(employees,0);
        }
        return ResponseResult.success();

    }


    private int plateNo(Integer id, String plateNo) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        Employees employees1=employeesService.getById(id);//原数据
        String[] string1=null;//原数据的车牌号数据
        String[] string2=null;//编辑过后的数据的车牌号数组
        if(employees1.getPlateNo()!=null&&!employees1.getPlateNo().equals("")){
            string1=employees1.getPlateNo().split(",");
        }
        if(plateNo!=null&&!plateNo.equals("")){
            string2=plateNo.split(",");
        }
        if(string1!=null&&string1.length>0){
            for(String string:string1){
                if(plateNo==null||plateNo.indexOf(string)<0){//判断是否删除车牌
                    List<Plate> plateList=plateService.getByPlateNo(string);
                    if(plateList!=null&&plateList.size()>0){
                        Plate plate=plateList.get(0);
                        plate.setState(3);
                        if(plateService.update(plate.getId(),plate)<0){
                            return 1;
                        }
                    }
                }
            }
        }
        if(string2!=null&&string2.length>0){
            for(String string:string2){
                if(employees1.getPlateNo()==null||employees1.getPlateNo().indexOf(string)<0){//判断是否新增车牌
                    Person person=personService.getByEmployeeId(id);
                    if(person!=null&&!person.equals("")){
                        Plate plate=new Plate();
                        plate.setState(0);
                        plate.setPersonId(person.getPersonId());
                        plate.setPlateNo(string);
                        plate.setPhoneNo(employees1.getPhoneNumber());
                        plate.setPersonNo(employees1.getEmployeeNo());
                        plate.setName(employees1.getName());
                        plate.setCreatedDateTime(new Date());
//                        plate.setLastUpdateDateTime(new Date());
                        if(plateService.add(plate)<0){
                            return 1;
                        }
                    }
                }
            }

        }
        return 0;
    }



    @PostMapping(value = "plateNo")
    public ResponseResult plateNo(@RequestBody PlateNoInput plateNo) throws Exception {
        if(plateNo == null || plateNo.getId() == null){
            return ResponseResult.error(PARAM_EORRO);
        }
        if(plateNo.getPlateNoList()!=null&&plateNo.getPlateNoList().size()>0){
            StringBuilder a=new StringBuilder();
            for(String s:plateNo.getPlateNoList()){
                a.append(s.trim()+",");
            }
            plateNo.setPlateNo(a.toString().substring(0,a.toString().length()-1));
        }else {
            plateNo.setPlateNo("");
        }

        //重复查询
        if(plateNo.getPlateNo()!=null&&!plateNo.getPlateNo().equals("")){
            PageData pageData=new PageData();
            String[] strings=plateNo.getPlateNo().split(",");
            if(strings!=null&&strings.length>0){
                for(String s:strings){
                    pageData.put("id",plateNo.getId());
                    pageData.put("plateNo",s);
                    List<EmployeesOutput> list=employeesService.findByplateNo(pageData);
                    if(list!=null&&list.size()>0){
                        return ResponseResult.error("车牌号重复");
                    }
                }
            }
        }

        var emp = employeesService.getById(plateNo.getId());

        var result = plateNo(plateNo.getId(), plateNo.getPlateNo());
        emp.setPlateNo(plateNo.getPlateNo());
        emp.setId(plateNo.getId());
        emp.setPlateNoList(plateNo.getPlateNoList());
        if(result > 0){
            return ResponseResult.error("操作失败");
        }
        var id = employeesService.update(plateNo.getId(), emp);
        if(id <= 0){
            return ResponseResult.error("操作失败");
        }
        return ResponseResult.success();
    }

    @PostMapping(value = "physicalAddress")
    public ResponseResult physicalAddress(@RequestBody PhysicalAddressInput physicalAddressInput) throws Exception {
        if(physicalAddressInput == null || physicalAddressInput.getId() == null){
            return ResponseResult.error(PARAM_EORRO);
        }
        var emp = employeesService.getById(physicalAddressInput.getId());
        emp.setId(physicalAddressInput.getId());
        emp.setCitizenCardPhysicalAddress(physicalAddressInput.getPhysicalAddress().trim());
        //2.餐盘系统更新
        foodSystemService.addFoodEmployees(emp);
        return formPost(physicalAddressInput.getId(), emp);
    }


    @Override
    @ApiOperation("删除人员信息")
    @GetMapping(value = "logicDelete")
    public ResponseResult logicDelete(String idList) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException{

        Employees employees ;
        if(idList.split(",").length>1){
            return ResponseResult.error("人员数据只能单个删除，请选择单条数据后做删除");
        }else{
            employees=employeesService.getById(Integer.parseInt(idList.split(",")[0]));
            if(employees.getWorkingState()!=0){
                return ResponseResult.error("不是待入职状态的人员不可以删除");
            }
        }

        ResponseResult result = super.logicDelete(idList);
        //如果人员删除成功
        if(result.getCode()==200){
            String[] idArray = idList.split(",");
            for(String id:idArray){
                if(Audit.deleteByResourceId(loadBalancerClient,id, ApprovalTypeEnum.EMPLOYEE_INDUCTION_TYPE.getCode())){
                    return ResponseResult.success();
                }
                return ResponseResult.error("删除入职审批失败");
            }

            return ResponseResult.success("删除人员成功");
        }
        return ResponseResult.error("删除人员失败");
    }


    @ApiOperation("获取所有不分页人员信息")
    @GetMapping(value = "selectAll")
    public ResponseResult selectAll(){
      return   employeesService.selectAll();
    }

    @GetMapping(value = "findPageList")
    @ApiOperation("获取分页的员工列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="employeeNo",value="员工工号",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="name",value="员工姓名",required=false,dataType="string", paramType = "query")})
    public ResponseResult selectPageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        pageData.put("amputated",0);
        return super.selectPageList(pageData);
    }

    @Override
    @GetMapping(value = "selectById")
    @ApiOperation(value="根据id获取单个窗口")
    public ResponseResult selectById(Integer id) {
        if( id==null ){
            ResponseResult.error(PARAM_EORRO);
        }
        return ResponseResult.success(employeesService.getByEmployeeId(id));
    }

    /**
     * 导入人员信息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "importemployees", method = RequestMethod.POST)
    public ResponseResult importemployees(MultipartFile files) throws Exception {
        ResponseResult result = new ResponseResult();
        if (files == null ) {
            result.setMessage("上传文件为空");
            result.setSuccess(false);
            result.setCode(500);
            return result;
        }
        String a=employeesService.checkedFile(files);
        if(!a.equals("操作成功")){
            result.setSuccess(false);
            result.setCode(500);
            result.setMessage(a);
            return result;
        }
        result.setSuccess(true);
        result.setCode(200);
        result.setMessage("导入成功");
        return result;
    }

    /**
     * 导出人员信息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ResponseResult export(HttpServletResponse response, HttpServletRequest request) {
        try {
            String str = employeesService.export(response,request);
            return  ResponseResult.success(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 改变人员状态
     *
     * @param
     * @return
     */
    @RequestMapping(value = "updateState", method = RequestMethod.POST)
    public ResponseResult updateState(@RequestBody EmployeesInput employeesInput) throws Exception {
        ResponseResult result = new ResponseResult();
        result.setSuccess(true);
        result.setCode(200);
        Employees employees;
        if(employeesInput.getId()==null||employeesInput.getId().equals("")||employeesInput.getState()==null||employeesInput.getState().equals("")){
            ResponseResult.error(PARAM_EORRO);
        }
        employees=employeesService.getById(employeesInput.getId());
        if(employeesInput.getState()==1){
            employees.setWorkingState(employeesInput.getState());
//            employees.setInductionDateTime(new Date());
        }else {
            if(!logicDelete(employeesInput.getId().toString()).isSuccess()){
                result.setCode(500);
                result.setSuccess(false);
            }
            return result;
        }
        Person person=new Person();
        person.setEmployeeId(employees.getId());
        person.setName(employees.getName());
        person.setPersonId(0);
        person.setState(0);
        person.setPersonNo(employees.getEmployeeNo());
        person.setGender(employees.getSex());
        person.setPhoneNo(employees.getPhoneNumber());

        Organization organization = organizationService.getById(employees.getOrganizationId());
        person.setDeptNo(organization.getOrganizationNo());
        person.setDeptName(organization.getName());

        person.setCreatedDateTime(new Date());
//        person.setLastUpdateDateTime(new Date());

        if(!employeesService.updateState(employees,person)){
            result.setSuccess(false);
            result.setCode(500);
            result.setMessage("更新失败");
        }
        if(result.getCode() == 200){
            //查询人员是否是属于窗口人员或者后台以及是否是中心窗口下
            if((employees.getWindowState() == 1 || employees.getWindowState() == 0 )&& employeesService.checkCondition(employees.getOrganizationId())){
                //1.人员下发到取号叫号系
                employeesService.queueUpdateState(employeesInput);
            }
            //2.下发人员到餐盘系统
            foodSystemService.addFoodEmployees(employees);

        }
        return result;
    }


    @ApiOperation("根据窗口id获得该窗口下的人员")
    @GetMapping(value = "selectByWindowId")
    public ResponseResult selectByWindowId(Integer windowsId){
        return employeesService.selectByWindowId(windowsId);
    }


    /**
     * 导入人员信息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "importcitizenCardPhysicalAddress", method = RequestMethod.POST)
    public ResponseResult importcitizenCardPhysicalAddress(MultipartFile files) throws Exception {
        ResponseResult result = new ResponseResult();
        if (files == null ) {
            result.setMessage("上传文件为空");
            result.setSuccess(false);
            result.setCode(500);
            return result;
        }
        String a=employeesService.checkedFiles(files);
        if(!a.equals("操作成功")){
            result.setSuccess(false);
            result.setCode(500);
            result.setMessage(a);
            return result;
        }
        result.setSuccess(true);
        result.setCode(200);
        result.setMessage("导入成功");
        return result;
    }

    @GetMapping(value = "get_by_organ_id")
    public ResponseResult getByOrganCode(Integer organId){
        if(organId == null){
            return ResponseResult.error(PARAM_EORRO);
        }
        return ResponseResult.success(employeesService.getByOrganCode(organId));
    }


    @GetMapping(value = "findList")
    @ApiOperation("获取分页的员工列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="employeeNo",value="员工工号",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="name",value="员工姓名",required=false,dataType="string", paramType = "query")})
    public ResponseResult findList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        var level = employeesService.getUsers().getAdministratorLevel();
        if(level!=9){
            if(getService().getUsers().getUserType()==0){
                pageData.put("employeeId",getService().getUsers().getEmployeeId());
            }else {
                pageData.put("organId",getService().getUsers().getOrganizationId());
            }
        }
        return super.selectPageList(pageData);
    }

    @GetMapping(value = "selectPageListWithinAuthority")
    @ApiOperation("获取带数据权限的分页的员工列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="employeeNo",value="员工工号",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="name",value="员工姓名",required=false,dataType="string", paramType = "query")})
    public ResponseResult selectPageListWithinAuthority(HttpServletRequest request){
        PageData pageData = new PageData(request);
        return ResponseResult.success(new PageInfo<>(employeesService.selectPageListWithinAuthority(pageData)));
    }

    /**
     * 新登分中心人员临时下发
     */
    @PostMapping("QueueXindengPeople")
    public ResponseResult QueueXindengPeople(){
        employeesService.QueueXindengPeople();
        return ResponseResult.success();
    }

    /**
     * 场口分中心人员临时下发
     */
    @PostMapping("QueueCKPeople")
    public ResponseResult QueueCKPeople(){
        employeesService.QueueCKPeople();
        return ResponseResult.success();
    }
}
