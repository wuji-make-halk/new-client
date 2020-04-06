package com.weimingfj.utils;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * alibaba fastjson 工具类
 * 
 * @author lihw
 * @created 2015年8月19日 上午11:13:40
 *
 */
public class FastJsonUtils {
    
    /**
     * 格式化JSON字符串的设置
     */
    static final SerializerFeature[] DEFAULT_SERIALIZER = new SerializerFeature[]{
            SerializerFeature.WriteEnumUsingName, // 使用枚举的名称
            SerializerFeature.WriteMapNullValue, // 输出空的字段
            SerializerFeature.WriteNullListAsEmpty, // 空list输出为[]
            SerializerFeature.WriteNullBooleanAsFalse, // 空boolean输出为false
            SerializerFeature.WriteNullNumberAsZero, // 空数字输出为0
            SerializerFeature.WriteNullStringAsEmpty, // 空String输出为""
            SerializerFeature.WriteDateUseDateFormat // 日期格式化为yyyy-MM-dd HH:mm:ss
    };
    
    /**
     * Object转为JSON格式的字符串
     * 
     * @author lihw
     * @created 2015年8月19日 上午11:35:37
     *
     * @param obj
     * @return
     */
    public static String toJsonString(Object obj) {
        return JSON.toJSONString(obj, DEFAULT_SERIALIZER);
    }
    
    /**
     * JSON字符串转换为Object
     * 
     * @author lihw
     * @created 2015年8月19日 上午11:37:18
     *
     * @param string
     * @param clazz
     * @return
     */
    public static <T> T parse(String string, Class<T> clazz) {
        return JSON.parseObject(string, clazz);
    }
    
    /**
     * JSON字符串转换为List
     * 
     * @author lihw
     * @created 2015年8月19日 上午11:37:24
     *
     * @param string
     * @param clazz
     * @return
     */
    public static <T> List<T> parseArray(String string, Class<T> clazz) {
        return JSON.parseArray(string, clazz);
    }
}
