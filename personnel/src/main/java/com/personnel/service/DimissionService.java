package com.personnel.service;

import com.common.Enum.AuditStateEnum;
import com.personnel.core.base.BaseMapper;
import com.personnel.core.base.BaseService;
import com.personnel.core.base.MybatisBaseMapper;
import com.personnel.domian.input.EmployeesInput;
import com.personnel.domian.output.DimissionOutput;
import com.personnel.mapper.jpa.DimissionRepository;
import com.personnel.mapper.mybatis.AttachmentMapper;
import com.personnel.mapper.mybatis.DimissionMapper;
import com.personnel.model.Dimission;
import com.personnel.model.Employees;
import com.personnel.model.Person;
import com.personnel.model.Plate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;


@Service
public class DimissionService extends BaseService<DimissionOutput,Dimission,Integer> {

    @Autowired
    private DimissionRepository repository;

    @Autowired
    private DimissionMapper dimissionMapper;

    @Autowired
    private EmployeesService employeesService;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private PersonService personService;

    @Autowired
    private FoodSystemService foodSystemService;

    @Autowired
    private PlateService plateService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public BaseMapper<Dimission, Integer> getMapper() {
        return repository;
    }

    @Override
    public MybatisBaseMapper<DimissionOutput> getMybatisBaseMapper() {
        return dimissionMapper;
    }


    public DimissionOutput selectByEmployeeId(Integer id) {
        return dimissionMapper.selectByEmployeeId(id);
    }

    @Override
    public int add(Dimission dimission) throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        dimission.setState(AuditStateEnum.AUDIT.getCode());
        return super.add(dimission);
    }

    public DimissionOutput getById(Integer id) {
        DimissionOutput dimissionOutput=dimissionMapper.selectByPrimaryKey(id);
        dimissionOutput.setAttachmentList(attachmentMapper.selectByDimissionId(id));
        return dimissionOutput;
    }

    @Transactional
    public int updateState(EmployeesInput employeesInput) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        Dimission dimission= getMapper().getById(employeesInput.getId());
        if(employeesInput.getState()==1){
            Employees employees=employeesService.getById(dimission.getEmployeeId());
            employees.setId(dimission.getEmployeeId());
            employees.setWorkingState(3);
            //如果员工的离职时间为空，设置离职时间
            if(employees.getDepartureDateTime()==null){
                employees.setDepartureDateTime(new Date());
            }
            if(employeesService.update(dimission.getEmployeeId(),employees)<=0){
                return ERROR;
            }
            Person person=personService.findByEmployeeId(dimission.getEmployeeId());
            if(person!=null){
                person.setState(3);
                if(personService.update(person.getId(),person)<0){
                    return ERROR;
                }
            }
            Employees employees1=employeesService.getById(dimission.getEmployeeId());
            if(employees1!=null){
                List<Plate> plateList=plateService.getByPersonNo(employees1.getEmployeeNo());
                if(plateList!=null&&plateList.size()>0){
                    for(Plate plate:plateList){
                        plate.setState(3);
                        plateService.update(plate.getId(),plate);
                    }
                }
            }

            //1.把取号叫号下发的人员的state改成1
            employeesService.leave(employees);
            //调用删除餐盘人员的方法
            foodSystemService.delFoodEmp(employees.getEmployeeNo());
        }
        dimission.setState(employeesInput.getState());
        if(super.update(employeesInput.getId(),dimission)<=0){
            return ERROR;
        }

        return SUCCESS;
    }
}
