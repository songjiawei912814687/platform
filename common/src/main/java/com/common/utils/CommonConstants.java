package com.common.utils;

public class CommonConstants {
	public interface OSS {
		public static final String bucketName = "hr-platform";
		public static final String accessKeyId = "LTAIYoiRhRCHqBwH";
		public static final String accessKeySecret = "zZf54crK7LOsYjXfq8cZCWVFzidby3";
		public static final String endpoint = "http://oss-cn-beijing.aliyuncs.com/";
		public static final String host = "http://hr-platform.oss-cn-beijing.aliyuncs.com/";

		public static final int MAX_KEYS = 1000;// 获取列表的数量个数 如果不指定个数 默认是100
												// 最大不能超过1000 有超过1000的需求 需要分页来处理
	}



}
