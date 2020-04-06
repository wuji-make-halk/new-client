package com.weimingfj.liner.service;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service("openService")
public class OpenService {
    
    Logger log = LoggerFactory.getLogger(OpenService.class);
    
    public static Charset UTF8 = Charset.forName("UTF-8");
    
    /**
     * 参数加密
     * 
     * <pre>
     *   加密规则： 
     *      1. 全部请求参数按字典序排序后，以参数名+参数值的方式（中间不加任何字符）全部连成一个字符串。
     *      2. 在步骤1生成的字符串的前后各加上appsecret
     *      3. 计算步骤2的字符串的MD5值，即为此次请求签名。
     *      备注： a. 名称为sign的参数， 以及内部参数（参数名以__开始）不参与加密计算   
     *            b. 在请求时，务必不要把appsecret作为参数上传！
     * </pre>
     * 
     * @author lihw
     * @created 2016年10月13日 上午10:34:14
     *
     * @param reqParams 全部请求参数
     * @param appkey    appkey
     * @param appsecret appsecret
     * @return
     */
    public String signParams(Map<String, String> reqParams, String appkey, String appsecret) {
        
        String[] keys = reqParams.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        
        StringBuffer str = new StringBuffer();
        str.append(appsecret);
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            String value = reqParams.get(key);
            if ("sign".equals(key) || key.startsWith("__")) {
                continue;
            }
            str.append(key).append(StringUtils.isEmpty(value) ? "" : value);
        }
        str.append(appsecret);
        log.debug("[openapi] --> str [ " + str.toString()  + " ] ");
        
        String calcSign = DigestUtils.md5Hex(str.toString().getBytes(UTF8));
        log.debug("[openapi] --> calcSign [ " + calcSign  + " ] ");
        
        return calcSign;
    }

    /**
     * 判断公共参数
     * @author lihw
     * @created 2016年7月23日 上午11:43:27
     *
     * @param reqParams
     * @return
     */
    public boolean hasAllCommonParams(Map<String, String> reqParams) {
        if (reqParams == null || reqParams.isEmpty()
                || !reqParams.containsKey("method") 
                || !reqParams.containsKey("time")
                || !reqParams.containsKey("appkey")
                || !reqParams.containsKey("sign")
                || !reqParams.containsKey("token")) {
            return false;
        }
        return true;
    }
    
    
    
    
    /**
     * 获取表单请求参数
     * 
     * @author lihw
     * @created 2016年7月22日 下午2:32:25
     *
     * @param req
     * @return
     */
    public Map<String, String> getRequestParams(HttpServletRequest req) {
        Map<String, String> params = new HashMap<String, String>();
        Enumeration<?> pnames = req.getParameterNames();
        while (pnames.hasMoreElements()) {
            String pname = (String) pnames.nextElement();
            params.put(pname, req.getParameter(pname));
        }
        return params;
    }
    
    /**
     * 获取上传文件表单请求参数
     * 
     * @author lihw
     * @created 2016年7月25日 下午3:11:18
     *
     * @param req
     * @return
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getMutilpartRequestParams(HttpServletRequest req) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        
        DiskFileItemFactory dfif = new DiskFileItemFactory();
        dfif.setSizeThreshold(4 * 1024 * 1024);
        File tempDir = new File("/open/tmp/");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        dfif.setRepository(tempDir);
        ServletFileUpload fileUpload = new ServletFileUpload(dfif);
        List<FileItem> items = fileUpload.parseRequest(req);
        for (FileItem item : items) {
            if (item.isFormField()) {
                String fieldName = item.getFieldName();
                params.put(fieldName, item.getString());
            } else {
                String fileName = item.getName();
                String fileType = "";
                if (fileName.lastIndexOf(".") > -1) {
                    fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
                }
                File file = new File(tempDir.getPath() + File.separator + UUID.randomUUID() + "." + fileType);
                item.write(file);
                params.put("img", file);
            }
        }
        
        return params;
    }
    
    /**
     * 获取请求IP
     * @author lihw
     * @created 2016年7月20日 下午5:07:02
     *
     * @param req
     * @return
     */
    public String getRequestIp(HttpServletRequest req) {
        String ip = "";
        ip = req.getHeader("X-Forwarded-For");
        log.debug("getRequestIp --> X-Forwarded-For : " + ip);
        if (!StringUtils.isEmpty(ip)) {
            ip = getRemoteIpFromForward(ip);
            if (!StringUtils.isEmpty(ip)) {
                return ip;
            }
        }
        ip = req.getHeader("X-Real-IP");
        log.debug("getRequestIp --> X-Real-IP : " + ip);
        if (!StringUtils.isEmpty(ip)) {
            ip = getRemoteIpFromForward(ip);
            if (!StringUtils.isEmpty(ip)) {
                return ip;
            }
        }
        ip = req.getRemoteAddr();
        log.debug("getRequestIp --> X-Real-IP : " + ip);
        return ip;
    }
    
    /** 
     * <p> 
     * 从 HTTP Header 中截取客户端连接 IP 地址。如果经过多次反向代理， 
     * 在请求头中获得的是以“,&lt;SP&gt;”分隔 IP 地址链，第一段为客户端 IP 地址。 
     * </p> 
     * 
     * @param xforwardIp 从 HTTP 请求头中获取转发过来的 IP 地址链 
     * @return 客户端源 IP 地址 
     */  
    private String getRemoteIpFromForward(String xforwardIp) {  
        int commaOffset = xforwardIp.indexOf(',');  
        if (commaOffset < 0) {  
            return xforwardIp;  
        }  
        return xforwardIp.substring(0, commaOffset);  
    }
    
}
