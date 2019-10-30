package com.repository;

import com.model.AttachmentConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author: Young
 * @description:
 * @date: Created in 17:26 2018/9/7
 * @modified by:
 */
public interface AttachmentConfigRepository extends JpaRepository<AttachmentConfig, Integer> {

    AttachmentConfig findByConfigTypeAndState(Integer configType,Integer state);

    @Query(value = "select a.id,a.allowFiles,a.compressMaxWidth,a.configName,a.configType,a.maxSize,a.minSize,a.urlPrefix from AttachmentConfig as a")
    List<AttachmentConfig> selectAll();


    AttachmentConfig findAttachmentConfigByAllowFiles(String suffixName);
}
