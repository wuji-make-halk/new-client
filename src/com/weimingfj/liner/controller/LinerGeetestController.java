package com.weimingfj.liner.controller;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.weimingfj.utils.FastJsonUtils;
import com.weimingfj.utils.geetest.GeetestConfig;
import com.weimingfj.utils.geetest.GeetestLib;

/**
 * 图片验证码
 * @author wangyb
 * @created 2016年11月18日 上午10:01:48
 *
 */
@Controller
@RequestMapping("/liner/geetest")
public class LinerGeetestController {
    
    
    @RequestMapping("/register")
    public void register(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String jsonpCaller = request.getParameter("jsonp_caller");
        Boolean isJsonp = !StringUtils.isEmpty(jsonpCaller);
        if(isJsonp){
            response.setContentType("text/plain");
        }
        
        
        GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key());

        String resStr = "{}";
        
        //自定义userid
        //String userid = "test";

        //进行验证预处理
        int gtServerStatus = gtSdk.preProcess();
        
        //将服务器状态设置到session中
        request.getSession().setAttribute(gtSdk.gtServerStatusSessionKey, gtServerStatus);
        //将userid设置到session中
        //request.getSession().setAttribute("userid", userid);
        
        resStr = gtSdk.getResponseStr();

        if(isJsonp){
            resStr = jsonpCaller + "(" + resStr + ");";
        }

        PrintWriter out = response.getWriter();
        out.println(resStr);
    }
    
    
    public static boolean validate(HttpServletRequest request){
        GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key());
        
        String challenge = request.getParameter(GeetestLib.fn_geetest_challenge);
        String validate = request.getParameter(GeetestLib.fn_geetest_validate);
        String seccode = request.getParameter(GeetestLib.fn_geetest_seccode);
        
        if(StringUtils.isEmpty(challenge)){
            return false;
        }
        if(StringUtils.isEmpty(validate)){
            return false;
        }
        if(StringUtils.isEmpty(seccode)){
            return false;
        }
        
        //从session中获取gt-server状态
        int gt_server_status_code = (Integer) request.getSession().getAttribute(gtSdk.gtServerStatusSessionKey);
        
        //从session中获取userid
        //String userid = (String)request.getSession().getAttribute("userid");
        
        int gtResult = 0;

        if (gt_server_status_code == 1) {
            //gt-server正常，向gt-server进行二次验证
            gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode);
            //gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode, userid);
            System.out.println(gtResult);
        } else {
            // gt-server非正常情况下，进行failback模式验证
                
            System.out.println("failback:use your own server captcha validate");
            gtResult = gtSdk.failbackValidateRequest(challenge, validate, seccode);
            System.out.println(gtResult);
        }

        return gtResult == 1;
        /*if (gtResult == 1) {
            // 验证成功
            PrintWriter out = response.getWriter();
            JSONObject data = new JSONObject();
            try {
                data.put("status", "success");
                data.put("version", gtSdk.getVersionInfo());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            out.println(data.toString());
        }
        else {
            // 验证失败
            JSONObject data = new JSONObject();
            try {
                data.put("status", "fail");
                data.put("version", gtSdk.getVersionInfo());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PrintWriter out = response.getWriter();
            out.println(data.toString());
        }*/
    }
    
}
