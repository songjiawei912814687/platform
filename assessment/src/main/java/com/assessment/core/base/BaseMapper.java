package com.assessment.core.base;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;


/**
 * 基类
 * leeoohoo@gmail.com
 */
@NoRepositoryBean
public interface BaseMapper<T,ID extends Serializable> extends JpaRepository<T,ID>{

    Page<T> findAll(Specification mySpec, Pageable pageable);

    List<T> findAll(Specification mySpec);

    T getById(Integer id);

    void deleteById(Integer id);

}
