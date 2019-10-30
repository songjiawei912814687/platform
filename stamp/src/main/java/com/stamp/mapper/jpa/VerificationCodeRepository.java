package com.stamp.mapper.jpa;


import com.stamp.core.base.BaseRepository;
import com.stamp.model.VerificationCode;

import java.util.List;


public interface VerificationCodeRepository extends BaseRepository<VerificationCode, Integer> {

    List<VerificationCode> findByUserNameAndCode(String userName, String password);
}
