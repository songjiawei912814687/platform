package com.screen.mapper.jpa;import com.screen.core.base.BaseMapper;import com.screen.model.ScreenConfig;import org.springframework.stereotype.Repository;import java.util.List;/** * @author: XiGuoQing * @description: * @date: Created in 下午 2:05 2018/10/30 0030 * @modified by: */@Repositorypublic interface ScreenConfigRepository extends BaseMapper<ScreenConfig,Integer> {    ScreenConfig findByConfigKey(String key);    List<ScreenConfig> findByParentId(Integer id);}