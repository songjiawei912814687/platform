package com.assistdecision.core.util;

public class AppConsts {

	//职位状态0-动态效能指标 1-常态效能指标 3-工作人员指标
	public final static Integer Dynamic_Index =0;
	public final static Integer Normal_Index =1;
	public final static Integer StaffMember_Index =3;

	//0-窗口 1-工作人员
	public final static Integer Window =0;
	public final static Integer StaffMember =1;

	//启动0 停用1
	public final static Integer start =0;
	public final static Integer stop =1;

	//计分公式   直接加分：0   直接减分：1
	public final static Integer Add_Points =0;
	public final static Integer Reduce_Points =1;

	//打分设置  系统默认值：0  手动调整 1  自动计算 3
	public final static Integer System_Default =0;
	public final static Integer Manual_Adjustment =1;
	public final static Integer Automatic_Calculation =3;

	//数据接口  迟到：1  早退 2  未打卡 3  办件量 5  网上申报率 7  资源精简率 9 群众满意度 11
	public final static Integer Belate_Interface =1;
	public final static Integer LeaveEarly_Interface =2;
	public final static Integer NotPunch_Interface =3;
	public final static Integer Amount_Interface =5;
	public final static Integer OnlineReportingRate_Interface =7;
	public final static Integer ResourceReductionRate_Interface =9;
	public final static Integer MassSatisfaction_Interface =11;

	//模板类型 月度考核 0  年度考核 1
	public final static Integer Monthly_Assessment =0;
	public final static Integer Annual_Assessment =1;

	//组织是否考核  考核 1
	public final static Integer Appraisal =1;

	//指标和考核规则树的类型
	public final static Integer Index_Type =0;
	public final static Integer Rule_type =1;

	//审核 未审核 0  审核通过 1  审核未通过 3
	public final static Integer  Unreviewed =0;
	public final static Integer  ReviewePassed =1;
	public final static Integer  RevieweUnPassed =3;

   //0-已生成 1-已同步 2-已提交 3-已确认

	public final static Integer created=0;
	public final static Integer synchronize=1;
	public final static Integer adjusted=2;
	public final static Integer confirm=3;


	public final static  Integer personAvg = 32;

	public final static  Integer jointRate = 36;


}
