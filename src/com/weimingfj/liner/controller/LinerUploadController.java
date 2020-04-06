package com.weimingfj.liner.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.weimingfj.common.form.ResponseDataForm;
import com.weimingfj.liner.service.AliUoloadService;
import com.weimingfj.liner.service.OpenService;

/**
 * 开放平台 - 上传
 * 
 * @author lihw
 * @created 2016年7月25日 下午4:24:15
 *
 */
@Controller
@RequestMapping("/liner/upload")
public class LinerUploadController {
    
    Logger log = LoggerFactory.getLogger(LinerUploadController.class);
    
    @Resource(name = "aliUoloadService")
    AliUoloadService aliUoloadService;
    
    @Resource(name = "openService")
    OpenService openService;
    
    @RequestMapping("/img/file")
    @ResponseBody
    public Object uploadImgFile(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        ResponseDataForm rdf = new ResponseDataForm();
        
        String reqIp = openService.getRequestIp(req); // 请求IP
        Map<String, Object> reqParams = openService.getMutilpartRequestParams(req);
        
        File img = (File) reqParams.get("img");
        String imgname = (String) reqParams.get("imgname");
        if (null == imgname) {
            imgname = img.getName();
        }
        if (StringUtils.isEmpty(imgname)) {
            rdf.setResult(ResponseDataForm.FAILURE);
            rdf.setResultInfo("图片上传业务必要参数为空");
            return rdf;
        }
        
        Map<String, String> params = new HashMap<String, String>();
        for (Iterator<String> it = reqParams.keySet().iterator(); it.hasNext();) {
            String key = it.next();
            if ("img".equals(key)) {
                continue;
            }
            params.put(key, (String) reqParams.get(key));
        }
        log.debug("[client] /open/upload/img/file --> reqIp = " + reqIp + ", reqParams = " + params);
        
        rdf = aliUoloadService.uploadFile(imgname, img);
        return rdf;
    }
    
    @RequestMapping("/img/base64")
    @ResponseBody
    public Object uploadImgBase64(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        ResponseDataForm rdf = new ResponseDataForm();
        
        String reqIp = openService.getRequestIp(req); // 请求IP
        Map<String, String> reqParams = openService.getRequestParams(req);
        log.debug("[client] /open/upload/img/file --> reqIp = " + reqIp + ", reqParams = " + reqParams);
        
        String img = reqParams.get("img");
        String imgname = reqParams.get("imgname");
        if (StringUtils.isEmpty(img) || StringUtils.isEmpty(imgname)) {
            rdf.setResult(ResponseDataForm.FAILURE);
            rdf.setResultInfo("图片上传业务必要参数为空");
            return rdf;
        }
        
        rdf = aliUoloadService.uploadBase64(imgname, img);
        return rdf;
    }
    
    
}
