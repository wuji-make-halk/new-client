package com.weimingfj.liner.controller;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.weimingfj.common.cache.GlobalCache;
import com.weimingfj.liner.cache.SystemPropertiesCache;
import com.weimingfj.utils.PasswordUtils;

@Controller
@RequestMapping("/linerapp/tool")
public class LinerAppToolController {
    
    Logger log = LoggerFactory.getLogger(LinerAppAndroidController.class);
    
    /** 专线版 api接口服务 地址 */
    static String LINER_API_URL = "";
    
    /** 专线版APPKEY */
    static String APPKEY = "";
    
    /** 专线版APPSECRET */
    static String APPSECRET = "";
    
    @Resource(name = "systemPropertiesCache")
    SystemPropertiesCache systemPropertiesCache;
    
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
        log.debug("tool LINER_API_URL = " + LINER_API_URL);
        log.debug("tool APPKEY = " + APPKEY);
        log.debug("tool APPSECRET = " + APPSECRET);
    }
    
    @RequestMapping("/pwd/encode")
    @ResponseBody
    public String encode(HttpServletRequest req) {
        String pwd = req.getParameter("PWD");
        return PasswordUtils.encode(pwd, APPSECRET);
    }
    
    @RequestMapping("/pwd/decode")
    @ResponseBody
    public String decode(HttpServletRequest req) {
        String pwd = req.getParameter("PWD");
        return PasswordUtils.decode(pwd, APPSECRET);
    }
}
