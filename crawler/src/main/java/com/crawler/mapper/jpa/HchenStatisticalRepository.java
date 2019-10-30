package com.crawler.mapper.jpa;

import com.crawler.model.HchenStatistical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  HchenStatisticalRepository extends JpaRepository<HchenStatistical,Integer> {
}
