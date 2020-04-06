package com.weimingfj.liner.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.conn.HttpHostConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.weimingfj.common.cache.GlobalCache;
import com.weimingfj.common.form.ResponseData;
import com.weimingfj.common.form.ResponseDataForm;
import com.weimingfj.common.utils.JsonUtil;
import com.weimingfj.liner.cache.SystemPropertiesCache;
import com.weimingfj.liner.service.OpenService;
import com.weimingfj.utils.HttpTools;

/**
 * 专线版 运营 app
 * @author lihw
 * @created 2016年10月12日 上午10:27:20
 *
 */
@Controller
@RequestMapping("/liner/android")
public class LinerAppAndroidController {
    
    Logger log = LoggerFactory.getLogger(LinerAppAndroidController.class);
    
    /** 专线版 api接口服务 地址 */
    static String LINER_API_URL = "";
    
    /** 专线版APPKEY */
    static String APPKEY = "";
    
    /** 专线版APPSECRET */
    static String APPSECRET = "";
    
    @Resource(name = "systemPropertiesCache")
    SystemPropertiesCache systemPropertiesCache;
    
    @Resource(name = "openService")
    OpenService openService;
    
    @PostConstruct
    public void init() {
        @SuppressWarnings("unchecked")
        Map<String, String> propCache = GlobalCache.getCache(SystemPropertiesCache.class, Map.class);
        if (null == propCache || propCache.isEmpty()) {
            propCache = systemPropertiesCache.getProps();
        }
        LINER_API_URL = propCache.get("linerapp.api.url");
        APPKEY = propCache.get("linerapp.api.appkey");
        APPSECRET = propCache.get("linerapp.api.appsecret");
        log.debug("android LINER_API_URL = " + LINER_API_URL);
        log.debug("android APPKEY = " + APPKEY);
        log.debug("android APPSECRET = " + APPSECRET);
    }
    
    /**
     * 入口
     * @author lihw
     * @created 2016年10月13日 上午11:00:33
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("")
    public @ResponseBody Object service(HttpServletRequest request, HttpServletResponse response) {
        
        String reqIp = openService.getRequestIp(request); // 请求IP
        Map<String, String> reqParams = openService.getRequestParams(request); // 请求参数
        log.debug("[client] /liner/android --> reqIp = " + reqIp + ", reqParams = " + reqParams);
        
        try {
            if(null == reqParams || !reqParams.containsKey("method")) {
                Map<String, Object> res = new HashMap<String, Object>();
                res.put("result", ResponseDataForm.FAULAIE);
                res.put("resultInfo", "请求参数错误。参数为空，或是未包含必要参数");
                return res;
            }
            
            reqParams.put("__reqIp", reqIp);    // 请求ip
            reqParams.put("__device_type", "android");
            reqParams.put("appkey", APPKEY);   // appkey 必要公共参数
            reqParams.put("time", String.valueOf(System.currentTimeMillis()));  // time  请求时间 必要公共参数
            
            reqParams.put("sign", openService.signParams(reqParams, APPKEY, APPSECRET));
            log.debug("[client] /liner/android --> reqParams = " + reqParams);
            
            String result = HttpTools.postCommonForm(LINER_API_URL, reqParams);
            log.debug("[client] /liner/android --> redirect to open-liner, posted result = " + result  + " ");
            
            Map<String, Object> res = JsonUtil.jsonToMapObject(result);
            if (null != res && res.containsKey("errorList")) {
                res.remove("errorList");
            }
            if (!StringUtils.isEmpty(reqParams.get("__showdoc"))) {
                res.put("__showdoc", res.get("page"));
            }
            if (null != res && res.containsKey("page")) {
                res.remove("page");
            }
            
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            ResponseData rd = new ResponseData();
            if (e instanceof HttpHostConnectException) {
                rd.setResult(ResponseData.FAULAIE);
                rd.setInfo("访问接口服务器失败。");
            } else {
                rd.setResult(ResponseData.FAULAIE);
                rd.setInfo("服务器异常。");
            }
            return rd;
        }
    }
    
}
