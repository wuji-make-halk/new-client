package com.weimingfj.utils;

import java.nio.charset.Charset;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;


/**
 * 密码加解密
 * 
 * @author lihw
 * @created 2016年8月3日 下午5:43:25
 *
 */
public class PasswordUtils {
    
    static Charset UTF8 = Charset.forName("UTF8");
    
    /**
     * 加密
     * 
     * @author lihw
     * @created 2016年8月3日 下午7:26:28
     *
     * @param source  原始字符串
     * @param salt  加密盐
     * @return
     */
    public static String encode(String source, String salt) {
        if (StringUtils.isEmpty(source)) {
            throw new RuntimeException("source can't be null!");
        }
        // 使用utf8编码，将密码和appsecret字符串编码为byte数组b1, b2
        byte[] b1 = source.getBytes(UTF8);
        byte[] b2 = salt.getBytes(UTF8);
        
        // 将数组b1与b2进行异或运算，并保存计算结果到b1
        for (int i = 0; i < b1.length; i++) {
            b1[i] = (byte) (b1[i] ^ b2[i % b2.length]);
        }
        
        // 将上一步的计算结果b1数组输出为hex字符串
        String str = bytes2Hex(b1);
        return str;
    }
    
    /**
     * 解密
     * 
     * @author lihw
     * @created 2016年8月3日 下午7:26:52
     *
     * @param source 加密后的字符串
     * @param salt 加密盐
     * @return
     */
    public static String decode(String source, String salt) {
        if (StringUtils.isEmpty(source)) {
            throw new RuntimeException("source can't be null!");
        }
        Pattern p = Pattern.compile("^[0-9A-F]+$");
        if (!p.matcher(source).matches()) {
            throw new RuntimeException("invalid source string");
        }
        
        // 使用base64对加密串进行
        byte[] b1 = hex2Bytes(source);
        byte[] b2 = salt.getBytes(UTF8);
        
        // 异或运算，还原
        for (int i = 0; i < b1.length; i++) {
            b1[i] = (byte) (b1[i] ^ b2[i % b2.length]);
        }
        
        // 获得原始字符串
        return new String(b1, UTF8);
    }
    
    /** 
     * byte数组转hex字符串<br/> 
     * 一个byte转为2个hex字符 
     * @param src 
     * @return 
     */
    public static String bytes2Hex(byte[] src) {
        char[] res = new char[src.length * 2];
        final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C',
                'D', 'E', 'F' };
        for (int i = 0, j = 0; i < src.length; i++) {
            res[j++] = hexDigits[src[i] >>> 4 & 0x0f];
            res[j++] = hexDigits[src[i] & 0x0f];
        }
        return new String(res);
    }
    
    /** 
     * hex字符串转byte数组<br/> 
     * 2个hex转为一个byte 
     * @param src 
     * @return 
     */  
    public static byte[] hex2Bytes(String src) {
        byte[] res = new byte[src.length() / 2];
        char[] chs = src.toCharArray();
        for (int i = 0, c = 0; i < chs.length; i += 2, c++) {
            res[c] = (byte) (Integer.parseInt(new String(chs, i, 2), 16));
        }
        return res;
    }
    
}
