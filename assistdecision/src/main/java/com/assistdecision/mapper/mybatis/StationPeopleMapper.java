package com.assistdecision.mapper.mybatis;

import com.assistdecision.domain.output.StationPeopleOutput;
import com.common.model.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StationPeopleMapper {

    List<StationPeopleOutput> selectAverageService();

    List<StationPeopleOutput> selectAllDeptTake(PageData pageData);

    List<StationPeopleOutput> selectAllDeptComp(PageData pageData);

    List<StationPeopleOutput> mattersAverageMinutes();

    List<StationPeopleOutput> deptAverageMinutes();
}
