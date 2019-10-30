package com.personnel.mapper.mybatis;

import com.common.model.PageData;
import com.github.pagehelper.Page;
import com.personnel.core.base.MybatisBaseMapper;
import com.personnel.domian.output.WindowOutput;
import com.personnel.model.Window;
import org.springframework.stereotype.Repository;


@Repository
public interface WindowMapper extends MybatisBaseMapper<WindowOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(Window record);

    int insertSelective(Window record);

    @Override
    WindowOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Window record);

    int updateByPrimaryKey(Window record);

    Page<WindowOutput> selectPageListWithinAuthority(PageData pageData);

    void updateOrgId(PageData pageData);

    void updateAttendanceDataOrgId(PageData pageData);

    void updateAttendanceDataStaticOrgId(PageData pageData);

    void updateOverTimeApplication(PageData pageData);

    void updateOffApplication(PageData pageData);

    void updateLeaveApplication(PageData pageData);

    void updateQuestion(PageData pageData);

    void updateComplain(PageData pageData);

    void updateEmployeeRecord(PageData pageData);

    void updatePlan(PageData pageData);

    void updateDepartSheet(PageData pageData);

    void updateFeedBackInfo(PageData pageData);

    void updateSuggestion(PageData pageData);

    void updateSuggestion1(PageData pageData);
}
