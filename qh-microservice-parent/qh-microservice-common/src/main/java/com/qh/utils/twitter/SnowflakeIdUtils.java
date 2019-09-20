package com.qh.utils.twitter;

/**
 * 雪花算法
 * Created by qiaozhonghuai on 2019/8/26.
 */
public class SnowflakeIdUtils {
	private static SnowflakeIdWorker idWorker;
	static {
		// 使用静态代码块初始化 SnowflakeIdWorker
		idWorker = new SnowflakeIdWorker(1, 1);
	}

	public static String nextId() {
		return idWorker.nextId() + "";
	}

}
