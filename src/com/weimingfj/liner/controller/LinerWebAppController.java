package com.weimingfj.liner.controller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.http.conn.HttpHostConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.weimingfj.common.cache.GlobalCache;
import com.weimingfj.common.form.ResponseDataForm;
import com.weimingfj.common.utils.JsonUtil;
import com.weimingfj.liner.cache.SystemPropertiesCache;
import com.weimingfj.liner.service.OpenService;
import com.weimingfj.utils.FastJsonUtils;
import com.weimingfj.utils.HttpTools;
import com.weimingfj.utils.PasswordUtils;


/**
 * 专线版 web 应用入口
 * @author lihw
 * @created 2016年10月12日 上午10:27:20
 *
 */
@Controller
@RequestMapping("/liner/webapp")
public class LinerWebAppController {
    
    Logger log = LoggerFactory.getLogger(LinerWebAppController.class);
    
    /** 专线版 api接口服务 地址 */
    static String LINER_API_URL = "";
    
    /** 专线版APPKEY */
    static String APPKEY = "";
    
    /** 专线版APPSECRET */
    static String APPSECRET = "";
    
    /** session attribute name */
    static final String LINER_SESSION_TOKEN = "LINER_SESSION_TOKEN";
    
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
        LINER_API_URL = propCache.get("api.url");
        APPKEY = propCache.get("api.appkey");
        APPSECRET = propCache.get("api.appsecret");
        log.debug("LINER_API_URL = " + LINER_API_URL);
        log.debug("APPKEY = " + APPKEY);
        log.debug("APPSECRET = " + APPSECRET);
    }
    
    @RequestMapping("/jsonp")
    public void jsonpService(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/plain");
        Map<String, String> reqParams = openService.getRequestParams(request);
        boolean isJsonp = reqParams.containsKey("jsonp_caller");
        String jsonpCaller = isJsonp ? reqParams.get("jsonp_caller") : "alert";
        Object res = service(request, response, true);
        try {
            String result = jsonpCaller + "(" + FastJsonUtils.toJsonString(res) + ");";
            log.debug("jsonp --> result = " + result);
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
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
        return service(request, response, false);
    }
    
    
    private Object service(HttpServletRequest request, HttpServletResponse response, boolean isJsonp) {
        String reqIp = openService.getRequestIp(request); // 请求IP
        Map<String, String> reqParams = openService.getRequestParams(request); // 请求参数
        log.debug("[client] /open/liner --> reqIp = " + reqIp + ", reqParams = " + reqParams);
        
        HttpSession session = request.getSession();
        
        try {
            
            if(null == reqParams || !reqParams.containsKey("method")) {
                Map<String, Object> res = new HashMap<String, Object>();
                res.put("result", ResponseDataForm.FAULAIE);
                res.put("resultInfo", "请求参数错误。参数为空，或是未包含必要参数method(接口方法名)");
                return res;
            }
            
            if (isJsonp) {
                Map<String, String> reqParamsEncoded = new HashMap<String, String>();
                if (null != reqParams && !reqParams.isEmpty()) {
                    for (Iterator<String> it = reqParams.keySet().iterator(); it.hasNext(); ) {
                        String key = it.next();
                        String val = reqParams.get(key);
                        String val2 = new String(val.getBytes(Charset.forName("iso-8859-1")), Charset.forName("utf-8"));
                        reqParamsEncoded.put(key, val2);
                    }
                }
                reqParams = reqParamsEncoded;
                log.debug("jsonp --> reqParams = " + reqParams);
            }
            
            for (Iterator<String> it = reqParams.keySet().iterator(); it.hasNext(); ) {
                String key = it.next();
                String value = reqParams.get(key);
                if (key.contains("password") || key.contains("pwd")) { // 对于密码相关的参数，需进行加密处理
                    if (!StringUtils.isEmpty(value)) {
                        reqParams.put(key, PasswordUtils.encode(value, APPSECRET));
                    }
                } else if ("imgcode".equals(key)) {
                    String sessionImgcode = (String) session.getAttribute(LinerImgcodeController.SESSION_IMGCODE);
                    if (!value.equals(sessionImgcode)) {
                        Map<String, Object> res = new HashMap<String, Object>();
                        res.put("result", ResponseDataForm.FAULAIE);
                        res.put("resultInfo", "图片验证码错误");
                        return res;
                    }
                } else if ("geetest_challenge".equals(key)) {
                    boolean isTrue = LinerGeetestController.validate(request);
                    if(!isTrue){
                        Map<String, Object> res = new HashMap<String, Object>();
                        res.put("result", ResponseDataForm.FAULAIE);
                        res.put("resultInfo", "验证码错误");
                        return res;
                    }
                }
            }
            
            reqParams.put("__reqIp", reqIp);    // 请求ip
            reqParams.put("appkey", APPKEY);   // appkey 必要公共参数
            reqParams.put("time", String.valueOf(System.currentTimeMillis()));  // time  请求时间 必要公共参数
            if (!reqParams.containsKey("token")) {
                reqParams.put("token", (String) session.getAttribute(LINER_SESSION_TOKEN)); // token 会话token 必要公共参数
            }
            // 对全部参数进行签名
            reqParams.put("sign", openService.signParams(reqParams, APPKEY, APPSECRET)); // sign 签名 必要公共参数
            
            String result = HttpTools.postCommonForm(LINER_API_URL, reqParams);
            log.debug("[client] /open/liner --> redirect to open-liner, posted result = " + result  + " ");
            
            Map<String, Object> res = JsonUtil.jsonToMapObject(result);
            if (null != res && res.containsKey("errorList")) {
                res.remove("errorList");
            }
            if (null != res && res.containsKey("page")) {
                if (!StringUtils.isEmpty(reqParams.get("__showdoc")) && !reqParams.containsKey("jsonp_caller")) {
                    res.put("__showdoc", res.get("page"));
                }
                res.remove("page");
            }
            
            // 登录成功后，将会话token缓存到session中
            String method = reqParams.get("method");
            if ("yunba.liner.v1.site.user.login".equals(method)) {
                if (null != res && "1".equals((String) res.get("result"))) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> loginInfo = (Map<String, Object>) res.get("resultObj");
                    if (null != loginInfo && loginInfo.containsKey("token")) {
                        session.setAttribute(LINER_SESSION_TOKEN, loginInfo.get("token"));
                        loginInfo.remove("token");
                    }
                }
            }
            
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            
            Map<String, Object> res = new HashMap<String, Object>();
            if (e instanceof HttpHostConnectException) {
                res.put("result", ResponseDataForm.FAULAIE);
                res.put("resultInfo", "访问接口服务器失败。");
            } else {
                res.put("result", ResponseDataForm.FAULAIE);
                res.put("resultInfo", "服务器异常。");
            }
            return res;
        }
    }
}
