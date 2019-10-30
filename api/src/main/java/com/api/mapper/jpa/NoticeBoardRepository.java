package com.api.mapper.jpa;

import com.api.model.NoticeBoard;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: young
 * @project_name: svn
 * @description: 公示栏
 * @date: Created in 2018-12-24  10:26
 * @modified by:
 */
public interface NoticeBoardRepository extends JpaRepository<NoticeBoard,Integer> {


}
