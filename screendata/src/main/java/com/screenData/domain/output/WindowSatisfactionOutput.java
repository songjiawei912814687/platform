package com.screenData.domain.output;/** * @author: Administrator */public class WindowSatisfactionOutput {  private String month;//当月  private String satisfactionRate;//满意率  private String implementRate;//实现率  private Integer count;//办结数  private Integer runOnce;//跑一次  private Integer satisfactionCount;//满意数  private String windowName;//窗口名称  public Integer getSatisfactionCount() {    return satisfactionCount;  }  public void setSatisfactionCount(Integer satisfactionCount) {    this.satisfactionCount = satisfactionCount;  }  public Integer getCount() {    return count;  }  public void setCount(Integer count) {    this.count = count;  }  public Integer getRunOnce() {    return runOnce;  }  public void setRunOnce(Integer runOnce) {    this.runOnce = runOnce;  }  public String getWindowName() {    return windowName;  }  public void setWindowName(String windowName) {    this.windowName = windowName;  }  public WindowSatisfactionOutput() {  }  public String getMonth() {    return month;  }  public void setMonth(String month) {    this.month = month;  }  public String getSatisfactionRate() {    return satisfactionRate;  }  public void setSatisfactionRate(String satisfactionRate) {    this.satisfactionRate = satisfactionRate;  }  public String getImplementRate() {    return implementRate;  }  public void setImplementRate(String implementRate) {    this.implementRate = implementRate;  }}