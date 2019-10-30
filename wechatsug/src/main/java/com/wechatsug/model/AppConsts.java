package com.wechatsug.model;

public class AppConsts {

	//投诉建议数据来源 0-电话 1-现场 2-上级下发 3-微信  4-二次回复 5-短信
	public final static Integer Resource_Phone =0;
	public final static Integer Resource_OnSite =1;
	public final static Integer Resource_Up =2;
	public final static Integer Resource_Wechat =3;
	public final static Integer Resource_Second =4;
	public final static Integer Resource_Message =5;

	/**处理不满详情的状态0-未处理。1-已经处理*/
	public final static Integer Reply_Un = 0;
	public final static Integer Reply_is = 1;

	//发布状态
	public final static Integer Publish_No =0;
	public final static Integer Publish_Yes =1;
	public final static Integer Publish_Invalid =2;

	//逾期状态
	public final static Integer OutOfDate_No =0;
	public final static Integer OutOfDate_Yes =1;

	//处理状态
	public final static Integer DealState_No =0;
	public final static Integer DealState_Yes =1;

}
