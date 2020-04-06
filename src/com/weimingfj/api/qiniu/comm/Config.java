package com.weimingfj.api.qiniu.comm;

import java.nio.charset.Charset;

// CHECKSTYLE:OFF

public final class Config {

    public static final String VERSION = "7.0.7";
    /**
     * 断点上传时的分块大小(默认的分块大小, 不允许改变)
     */
    public static final int BLOCK_SIZE = 4 * 1024 * 1024;

    public static final Charset UTF_8 = Charset.forName("UTF-8");
    /**
     * 默认API服务器
     */
    public static String API_HOST = "http://api.qiniu.com";
    /**
     * 默认文件列表服务器
     */
    public static String RSF_HOST = "http://rsf.qbox.me";
    /**
     * 默认文件管理服务器
     */
    public static String RS_HOST = "http://rs.qbox.me";
    /**
     * 默认文件服务器
     */
    public static String IO_HOST = "http://iovip.qbox.me";
    /**
     * 图片下载地址前缀测试地址
     */
//    public static String IMAGE_URL="http://7xpgb1.com1.z0.glb.clouddn.com/";
    /**
     * 图片下载地址前缀正式地址
     */
//    public static String IMAGE_URL="http://7xphzd.com2.z0.glb.qiniucdn.com/";
    public static String IMAGE_URL="http://static02.yunba.com";
    /**
     * 默认Zone
     */
    public static Zone zone = Zone.zone0();
    /**
     * 如果文件大小大于此值则使用断点上传, 否则使用Form上传
     */
    public static int PUT_THRESHOLD = BLOCK_SIZE;
    /**
     * 连接超时时间 单位秒(默认10s)
     */
    public static int CONNECT_TIMEOUT = 10;
    /**
     * 回复超时时间 单位秒(默认30s)
     */
    public static int RESPONSE_TIMEOUT = 30;
    /**
     * 上传失败重试次数
     */
    public static int RETRY_MAX = 5;

    private Config() {
    }
}
// CHECKSTYLE:ON
