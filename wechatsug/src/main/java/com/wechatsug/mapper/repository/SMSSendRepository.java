package com.wechatsug.mapper.repository;


import com.wechatsug.model.SMSSend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SMSSendRepository extends JpaRepository<SMSSend,Integer> {
    List<SMSSend> findAllByState(Integer state);

    List<SMSSend> findAllByType(Integer type);

    SMSSend findSMSSendById(Integer id);

}
