package com.screen.domain.output;

import java.util.List;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2018-12-20  10:17
 * @modified by:
 */
public class DoingOutput {

    private List<DoConditionOutput> doConditionOutputList;
    private Integer todayCount;
    private Integer allCount;

    public List<DoConditionOutput> getDoConditionOutputList() {
        return doConditionOutputList;
    }

    public void setDoConditionOutputList(List<DoConditionOutput> doConditionOutputList) {
        this.doConditionOutputList = doConditionOutputList;
    }

    public Integer getTodayCount() {
        return todayCount;
    }

    public void setTodayCount(Integer todayCount) {
        this.todayCount = todayCount;
    }

    public Integer getAllCount() {
        return allCount;
    }

    public void setAllCount(Integer allCount) {
        this.allCount = allCount;
    }
}
