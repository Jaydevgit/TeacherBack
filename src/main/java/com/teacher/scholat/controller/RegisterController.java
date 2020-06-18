package com.teacher.scholat.controller;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.model.Apply;
import com.teacher.scholat.service.MailService;
import com.teacher.scholat.service.RegisterService;
import com.teacher.scholat.util.CommonUtil;
import com.teacher.scholat.util.MD5Util;
import com.teacher.scholat.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;



    @Autowired
    private MailService mailService;



    @PostMapping("/getApplyInfo")
    public JSONObject getApplyInfo(@RequestBody JSONObject requestJson) {
        System.out.println("---------------- 开始请求：根据token获取申请表信息 ---------------------");
        System.out.println("传入的信息为：");
        System.out.println(requestJson);
        //  根据token搜索信息
        JSONObject jsonObject = registerService.getApplyInfo(requestJson);
        System.out.println("根据token找到的申请表信息为:");
        System.out.println(jsonObject);
        return CommonUtil.successJson(jsonObject);
    }
    @PostMapping("/judgeDomainNameExist")
    public JSONObject judgeDomainNameExist(@RequestBody JSONObject requestJson) {
        System.out.println("domain==="+requestJson);
        int existDomainName = registerService.judgeDomainNameExist(requestJson);
        if(existDomainName!=0) {
            System.out.println("看看该域名是注册：" + existDomainName);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("existDomain",true);
            return CommonUtil.successJson(jsonObject);
        }else{
            System.out.println("看看该域名否注册：" + existDomainName);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("existDomain",false);
            return CommonUtil.successJson(jsonObject);
        }
    }
    @PostMapping("/getSchoolDomain")
    public JSONObject getSchoolDomain(@RequestBody JSONObject requestJson) {
        System.out.println("根据域名查找"+requestJson);
        JSONObject jsonObject= registerService.getSchoolDomain(requestJson);
        return CommonUtil.successJson(jsonObject);
    }
    @PostMapping("/judgeUserNameExist")
    public JSONObject judgeUserNameExist(@RequestBody JSONObject requestJson) {
        System.out.println("UserName==="+requestJson);
        int UserName = registerService.judgeUserNameExist(requestJson);
        if(UserName!=0) {
            System.out.println("看看该用户名是注册：" + UserName);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UserName",true);
            return CommonUtil.successJson(jsonObject);
        }else{
            System.out.println("看看该用户名否注册：" + UserName);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UserName",false);
            return CommonUtil.successJson(jsonObject);
        }
    }

    @PostMapping("/apply")
    public JSONObject apply(@RequestBody JSONObject requestJson) {
        System.out.println("进入到了申请。。。。。。");
        System.out.println(requestJson);
       /* CommonUtil.hasAllRequired(requestJson, "username");
        CommonUtil.hasAllRequired(requestJson, "email");
        CommonUtil.hasAllRequired(requestJson, "post");
        CommonUtil.hasAllRequired(requestJson, "create_time");
        CommonUtil.hasAllRequired(requestJson, "state");*/
        int existUsername = registerService.judgeUserNameExist(requestJson);
        int existDomainName = registerService.judgeDomainNameExist(requestJson);
        // 去看下有没有重复的申请信息, 主要是去校验有没有相同的学校和学院
//        int exist = registerService.judgeUnitExist(requestJson);
        System.out.println("看看该学院是否已经发起过申请：exist:" +"---existUsername:"+existUsername+"---existDomainName:"+existDomainName);
//        if (exist != 0) {
//            System.out.println("进入到if (exist != 0)");
//            /*-----------------开始发送email----------------------*/
//            /*String userEmail=requestJson.getString("email");
//            mailService.sendHtmlMail(userEmail,"注册失败","<p>该学院已经发起过申请，请勿重复注册。<br>\n" +
//                    "                                此为系统邮件，请勿回复。<br>\n" +
//                    "                                请保管好您的邮箱，避免账号被他人盗用！\n" +
//                    "                            </p>");
//            System.out.println("成功了");
//            System.out.println("发送email结束");*/
//            /*--------------发送email结束-------------------*/
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("exist", "该学院已发起过申请");
//            return CommonUtil.successJson(jsonObject);
//        } else
            if(existUsername!=0){
            System.out.println("看看该用户是否注册：" + existUsername);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("existName", "该用户名已存在");
            /*String userEmail=requestJson.getString("email");
            mailService.sendHtmlMail(userEmail,"注册失败","<p>该用户名已存在，请勿重复注册。<br>\n" +
                    "                                此为系统邮件，请勿回复。<br>\n" +
                    "                                请保管好您的邮箱，避免账号被他人盗用！\n" +
                    "                            </p>");*/
            return CommonUtil.successJson(jsonObject);
        } else if(existDomainName!=0){
            System.out.println("看看该域名是否注册：" + existDomainName);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("existDomainName", "该域名已存在");
            /*String userEmail=requestJson.getString("email");
            mailService.sendHtmlMail(userEmail,"注册失败","<p>该域名已存在，请勿重复注册。<br>\n" +
                    "                                此为系统邮件，请勿回复。<br>\n" +
                    "                                请保管好您的邮箱，避免账号被他人盗用！\n" +
                    "                            </p>"); */
            return CommonUtil.successJson(jsonObject);
        }else{
            Apply apply = JSONObject.toJavaObject(requestJson, Apply.class);
            System.out.println("发起申请的学院为：" + apply.getSchool_name() + " " + apply.getUnit_name() + ".......");
            // -----------------------对输入的数据进行处理------------------------------
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
            apply.setUpdate_time(df.format(new Date()));
            apply.setState(1); // 状态改为申请状态

            System.out.println("加密前" + apply.getPassword());
            String newPassword = MD5Util.toDb(apply.getPassword());
            System.out.println("加密后：" + newPassword);
            apply.setPassword(newPassword);
            // 设置token
            String token = TokenUtils.getToken(apply.getUsername());
            System.out.println("该申请学院的token为：" + token);
            apply.setToken(token);
            // ------------------------处理数据结束---------------------------
            registerService.addApply(apply);
            System.out.println("插入申请表的id为：" + apply.getId());
            return CommonUtil.successJson();
        }

    }
}