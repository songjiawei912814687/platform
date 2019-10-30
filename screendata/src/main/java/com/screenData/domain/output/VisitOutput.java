package com.screenData.domain.output;

import java.util.List;

/**
 * @author: young
 * @project_name: svn
 * @description: 来访人数返回对象
 * @date: Created in 2018-12-20  10:32
 * @modified by:
 */
public class VisitOutput  {

    private List<VisitorsNumberOutput> visitorsNumberOutputList;

    //取号人数的总数
    private Integer takeNumber;

    //办结总数
    private Integer completeNumber;

    public List<VisitorsNumberOutput> getVisitorsNumberOutputList() {
        return visitorsNumberOutputList;
    }

    public void setVisitorsNumberOutputList(List<VisitorsNumberOutput> visitorsNumberOutputList) {
        this.visitorsNumberOutputList = visitorsNumberOutputList;
    }

    public Integer getTakeNumber() {
        return takeNumber;
    }

    public void setTakeNumber(Integer takeNumber) {
        this.takeNumber = takeNumber;
    }

    public Integer getCompleteNumber() {
        return completeNumber;
    }

    public void setCompleteNumber(Integer completeNumber) {
        this.completeNumber = completeNumber;
    }
}
