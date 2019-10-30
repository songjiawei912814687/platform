package com.stamp.mapper.jpa;


import com.stamp.core.base.BaseRepository;
import com.stamp.model.Menu;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends BaseRepository<Menu, Integer> {

}
