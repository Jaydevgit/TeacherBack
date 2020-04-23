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
        int exist = registerService.judgeUnitExist(requestJson);
        System.out.println("看看该学院是否已经发起过申请：exist:" + exist+"---existUsername:"+existUsername+"---existDomainName:"+existDomainName);
        if (exist != 0) {
            System.out.println("进入到if (exist != 0)");
            /*-----------------开始发送email----------------------*/
            /*String userEmail=requestJson.getString("email");
            mailService.sendHtmlMail(userEmail,"注册失败","<p>该学院已经发起过申请，请勿重复注册。<br>\n" +
                    "                                此为系统邮件，请勿回复。<br>\n" +
                    "                                请保管好您的邮箱，避免账号被他人盗用！\n" +
                    "                            </p>");
            System.out.println("成功了");
            System.out.println("发送email结束");*/
            /*--------------发送email结束-------------------*/
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("exist", "该学院已发起过申请");
            return CommonUtil.successJson(jsonObject);
        } else if(existUsername!=0){
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


            /*-----------------开始发送email----------------------*/
            /*System.out.println("开始发送email");
            String email=requestJson.getString("email");
            String username=requestJson.getString("username");
            String school_name=requestJson.getString("school_name");
            String domain_name=requestJson.getString("domain_name");
            String unit_name=requestJson.getString("unit_name");
            String phone=requestJson.getString("phone");
            String content="<div style=\"background-color:#ECECEC; padding: 35px;\">\n" +
                    "    <table cellpadding=\"0\" align=\"center\"\n" +
                    "           style=\"width: 600px; margin: 0px auto; text-align: left; position: relative; border-top-left-radius: 5px; border-top-right-radius: 5px; border-bottom-right-radius: 5px; border-bottom-left-radius: 5px; font-size: 14px; font-family:微软雅黑, 黑体; line-height: 1.5; box-shadow: rgb(153, 153, 153) 0px 0px 5px; border-collapse: collapse; background-position: initial initial; background-repeat: initial initial;background:#fff;\">\n" +
                    "        <tbody>\n" +
                    "        <tr>\n" +
                    "            <th valign=\"middle\"\n" +
                    "                style=\"height: 25px; line-height: 25px; padding: 15px 35px; border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: #42a3d3; background-color: #49bcff; border-top-left-radius: 5px; border-top-right-radius: 5px; border-bottom-right-radius: 0px; border-bottom-left-radius: 0px;\">\n" +
                    "                <font face=\"微软雅黑\" size=\"5\" style=\"color: rgb(255, 255, 255); \">注册成功! （学者网师资栏目管理系统）</font>\n" +
                    "            </th>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "            <td>\n" +
                    "                <div style=\"padding:25px 35px 40px; background-color:#fff;\">\n" +
                    "                    <h2 style=\"margin: 5px 0px; \">\n" +
                    "                        <font color=\"#333333\" style=\"line-height: 20px; \">\n" +
                    "                            <font style=\"line-height: 22px; \" size=\"4\">\n" +
                    "                                亲爱的 123456</font>\n" +
                    "                        </font>\n" +
                    "                    </h2>\n" +
                    "                    <p>首先感谢您加入本**站！下面是您的账号信息<br>\n" +
                    "                        学校名称：<b>"+school_name+"</b><br>\n" +
                    "                        学院名称：<b>"+unit_name+"</b><br>\n" +
                    "                        学校域名：<b>"+domain_name+"</b><br>\n" +
                    "                        管理员账号：<b>"+username+"</b><br>\n" +
                    "                        管理员邮箱：<b>"+email+"</b><br>\n" +
                    "                        管理员电话：<b>"+phone+"</b><br>\n" +
                    "                        当您在使用本网站时，遵守当地法律法规。<br>\n" +
                    "                    <p align=\"right\">学者网师资栏目管理系统</p>\n" +
                    "                    <div style=\"width:700px;margin:0 auto;\">\n" +
                    "                        <div style=\"padding:10px 10px 0;border-top:1px solid #ccc;color:#747474;margin-bottom:20px;line-height:1.3em;font-size:12px;\">\n" +
                    "                            <p>此为系统邮件，请勿回复<br>\n" +
                    "                                请保管好您的邮箱，避免账号被他人盗用\n" +
                    "                            </p>\n" +
                    "                            <p>©***</p>\n" +
                    "                        </div>\n" +
                    "                    </div>\n" +
                    "                </div>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "        </tbody>\n" +
                    "    </table>\n" +
                    "</div>\n";
            mailService.sendHtmlMail(email,"注册成功",content);
            System.out.println("成功了");
            System.out.println("发送email结束");*/
            /*--------------发送email结束-------------------*/


            return CommonUtil.successJson();
        }

    }
}