package com.stamp.service;


import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.stamp.core.base.BaseRepository;
import com.stamp.core.base.BaseService;
import com.stamp.core.base.MybatisBaseMapper;
import com.stamp.domain.input.SMSSendInput;
import com.stamp.domain.output.OrganizationOutput;
import com.stamp.domain.output.StampOrganOutput;
import com.stamp.domain.output.UsersOutput;
import com.stamp.mapper.jpa.EmployeesRepository;
import com.stamp.mapper.jpa.UserRepository;
import com.stamp.mapper.jpa.VerificationCodeRepository;
import com.stamp.mapper.mybatis.OrganizationMapper;
import com.stamp.mapper.mybatis.StampOrganMapper;
import com.stamp.mapper.mybatis.UsersMapper;
import com.stamp.model.Employees;
import com.stamp.model.Users;
import com.stamp.model.VerificationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.stamp.core.base.BaseController.PARAM_EORRO;

@Service
public class UserService extends BaseService<UsersOutput, Users,Integer> {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private StampOrganMapper stampOrganMapper;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private EmployeesRepository employeesRepository;
    @Autowired
    private VerificationCodeRepository verificationCodeRepository;
    @Autowired
    private SMSSendService smsSendService;

    @Override
    public BaseRepository<Users,Integer> getMapper() {
        return userRepository;
    }

    @Override
    public MybatisBaseMapper<UsersOutput> getMybatisBaseMapper() {
        return usersMapper;
    }

    public Users getByUserName(String name){
        return userRepository.findByUsername(name);
    }
    public List<Users> getByEmployeeId(Integer id){
        return userRepository.findAllByEmployeeId(id);
    }

    public UsersOutput selectByEmployeeId(Integer employeeId) {
       return usersMapper.selectByEmployeeId(employeeId);
    }

    public List<Users> findByEmployeeId(Integer employeeId){
        return userRepository.findAllByEmployeeId(employeeId);
    }

    public ResponseResult selectByIdAndType(Integer id){
        if( id==null ){
            ResponseResult.error(PARAM_EORRO);
        }
        UsersOutput result = this.selectById(id);
        if(result.getUserType()==2){
            StampOrganOutput stampOrganOutput = stampOrganMapper.selectByPrimaryKey(result.getOrganizationId());
            result.setOrganizationName(stampOrganOutput.getName());
        }else{
            OrganizationOutput organizationOutput = organizationMapper.selectByPrimaryKey(result.getOrganizationId());
            result.setOrganizationName(organizationOutput.getName());
        }
        return ResponseResult.success(result);
    }


    public List<UsersOutput> getByRoleId(PageData pageData) {
        return usersMapper.findByRoleId(pageData);
    }

    public List<UsersOutput> getByRoleIdNotIn(Integer roleId, PageData pageData) {
        Integer pagesize = pageData.getRows();
        Integer page = pageData.getPageIndex();
        PageHelper.startPage(page, pagesize);

        Page<UsersOutput> pageList = usersMapper.findByRoleIdNotIn(pageData);
        return pageList;
    }

    public boolean updatePassword(Users users){
        return usersMapper.updatePassword(users) > 0;
    }

    public ResponseResult sendVerificationCode(String userName, String phoneNo) throws Exception {
        //验证手机号码是否有效
        PageData pageData = new PageData();
        UsersOutput usersOutput = usersMapper.selectByUserName(userName);
        pageData.put("receiveTelephone",phoneNo);
        pageData.put("isTiming",0);
        ArrayList<String> objects = new ArrayList<>();
        if(usersOutput==null){
            return ResponseResult.error("系统中未找到该账户，请确认");
        }
        if(usersOutput.getUserType()==1){ //部门账号
            StampOrganOutput organizationOutput = stampOrganMapper.selectByPrimaryKey(usersOutput.getOrganizationId());
            if(!phoneNo.equals(organizationOutput.getPhoneNumber())){
                return ResponseResult.error("手机号和系统中部门手机号不匹配请确认手机号是否输入正确或联系管理员核实部门对应的手机号");
            }else {
                pageData.put("receiveEmployeeName",organizationOutput.getName());
                objects.add(phoneNo+ "/" +organizationOutput.getName());
            }
        }else{ //员工账号
            Employees employeesById = employeesRepository.findEmployeesByIdAndAmputated(usersOutput.getEmployeeId(),0);
            if(!phoneNo.equals(employeesById.getPhoneNumber())){
                return ResponseResult.error("手机号未注册到系统，请确认手机号是否输入正确或联系管理员核实手机号");
            }else {
                pageData.put("receiveEmployeeName",employeesById.getName());
                objects.add(phoneNo+ "/" +employeesById.getName());
            }
        }
        String code = String.valueOf(new Random().nextInt(899999) + 100000);
        String message = "您好，您正在使用杭州市富阳区行政服务中心大数据平台的忘记密码找回操作，本次验证码为："+code+", 请勿泄露。";
        SMSSendInput smsSendInput = new SMSSendInput();
        smsSendInput.setDescription(message);
        smsSendInput.setSendList(objects);
        smsSendInput.setCreatedDateTime(new Date());
        smsSendInput.setCreatedUserId(usersOutput.getId());
        smsSendInput.setCreatedUserName(usersOutput.getUsername());
        smsSendInput.setLastUpdateUserId(usersOutput.getId());
        smsSendInput.setLastUpdateUserName(usersOutput.getUsername());
        smsSendInput.setLastUpdateDateTime(new Date());
        //返回1
        if(smsSendService.addMessages(smsSendInput)>0){
            //若成功则将用户的账号和验证码以及有限时间添加到数据库中（当前时间+2分钟）
            VerificationCode verificationCode = new VerificationCode();
            verificationCode.setUserName(userName);
            verificationCode.setCode(code);
            Date now = new Date();
            Date afterDate = new Date(now .getTime() + 120000);
            verificationCode.setOutOfDateTime(afterDate);
            VerificationCode save = verificationCodeRepository.save(verificationCode);
            if(save!=null){
                return ResponseResult.success();
            }else{
                return ResponseResult.error("系统异常");
            }
        }else{
            return ResponseResult.error("系统异常，未成功发送验证码");
        }
    }

    public ResponseResult vCodeAndChangePwd(String userName, String code, String password) {
        List<VerificationCode> list = verificationCodeRepository.findByUserNameAndCode(userName,code);
        if(list==null||list.size()==0){
            return ResponseResult.error("验证码输入错误");
        }else {
            for (VerificationCode v:list) {
                if(v.getOutOfDateTime().after(new Date())){
                    String encode = passwordEncoder.encode(password);
                    Users users = new Users();
                    users.setUsername(userName);
                    users.setPassword(encode);
                    int user = usersMapper.updatePasswordByUserName(users);
                    if(user>0){
                        return ResponseResult.success();
                    }else{
                        return ResponseResult.error("修改密码失败");
                    }
                }
            }
            return ResponseResult.error("验证码已过期，请重新获取验证码");
        }
    }

    public Page<UsersOutput> selectPageListWithinAuthority(PageData pageData) {
        Integer pagesize = pageData.getRows();
        Integer page = pageData.getPageIndex();
        PageHelper.startPage(page, pagesize);
        Page<UsersOutput> pageList = null;
        pageData.put("userId",getUsers().getId());
        if(getUsers().getAdministratorLevel()==9){
            pageList = usersMapper.selectPage(pageData);
        }else{
            if(getUsers().getUserType()==1){
                StampOrganOutput organizationOutput = stampOrganMapper.selectByPrimaryKey(getUsers().getOrganizationId());
                pageData.put("path",organizationOutput.getPath());
                pageData.put("orgId",getUsers().getOrganizationId());
            }else{
                pageData.put("employeeId",getUsers().getEmployeeId());
            }
            pageList = usersMapper.selectPageListWithinAuthority(pageData);
        }
        return pageList;
    }

    public List<Users> getByOrganIdAndUserType(Integer organId){
        return userRepository.findAllByOrganizationIdAndUserType(organId, 1);
    }

    public Integer selectOrganIdByEmpId(Integer empId) {
        return usersMapper.selectOrganIdByEmpId(empId);
    }

    public Integer selectOrganIdByParentId(Integer organId){
        return usersMapper.selectOrganIdByParentId(organId);
    }

    public boolean verificationOrg(Users users) {
        List<StampOrganOutput> list =  stampOrganMapper.selectByParentId(users.getOrganizationId());
        if(list!=null&&list.size()>0){
            return false;
        }else{
            return true;
        }
    }
}
