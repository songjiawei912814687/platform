package com.personnel.mapper.jpa;

import com.personnel.core.base.BaseMapper;
import com.personnel.model.Window;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WindowRepository extends BaseMapper<Window,Integer> {
    /**
     * 根据窗口名称查询
     * @param name
     * @return
     */
    List<Window> findByName(String name);

    /**
     * 根据窗口编号
     * @param windowNo
     * @return
     */
    Window findByWindowNo(String windowNo);

}
