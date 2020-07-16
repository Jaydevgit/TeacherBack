package com.teacher.scholat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.model.Apply;
import com.teacher.scholat.service.MailService;
import com.teacher.scholat.service.ScholatService;
import com.teacher.scholat.util.MD5Util;
import com.teacher.scholat.util.constants.ErrorEnum;
import com.teacher.scholat.util.CommonUtil;
import com.teacher.scholat.util.InviteToScholat;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/scholat")
public class ScholatController {
    @Autowired
    private ScholatService scholatService;
    @Autowired
    private MailService mailService;
    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 登录, 设置token
     */
    @PostMapping("/login/auth")
    public JSONObject authLogin(@RequestBody JSONObject requestJson) {
        System.out.println("----------------- 开始准备验证学者网管理员账号密码 ------------------");
        System.out.println("前台传过来的信息有：" + requestJson);
        CommonUtil.hasAllRequired(requestJson, "username,password");
        return scholatService.authLogin(requestJson);
    }

    /**
     * 查询当前登录用户的信息
     */
    @PostMapping("/login/getInfo")
    public JSONObject getInfo() {
        return scholatService.getInfo();
    }

    /**
     * 查询管理列表
     */
    @RequiresPermissions("unit:list")
    @GetMapping("/apply/getInfo")
    public JSONObject listTeacher(HttpServletRequest request) {
        System.out.println("开始查询成员列表, 不一定是申请，看前台传过来的是什么state");
        System.out.println(CommonUtil.request2Json(request));
        return scholatService.listApply(CommonUtil.request2Json(request));
    }
    /**
     * 查询学校管理列表
     */
    @RequiresPermissions("unit:list")
    @GetMapping("/apply/getInfoSchool")
    public JSONObject listApplySchool(HttpServletRequest request) {
        System.out.println("开始查询成员列表, 不一定是申请，看前台传过来的是什么state");
        System.out.println(CommonUtil.request2Json(request));
        return scholatService.listApplySchool(CommonUtil.request2Json(request));
    }

    /**
     * 登出
     */
    @PostMapping("/login/logout")
    public JSONObject logout() {
        return scholatService.logout();
    }

    /**
     * 登录, 设置token
     */
    @PostMapping("/apply/allowApply")
    public JSONObject allowApply(@RequestBody JSONObject requestJson) {
        System.out.println("----------------- 请求：允许通过申请 ------------------");
        // 获取申请表信息
        int apply_id = Integer.parseInt(requestJson.get("apply_id").toString());
        System.out.println("查看请求的id为：" + apply_id);
        JSONObject jsonObject = scholatService.getApplyInfo(requestJson);
        System.out.println("找到的申请信息如下：");
        System.out.println(jsonObject);
        Apply apply = JSONObject.toJavaObject(jsonObject, Apply.class);
        apply.setId(apply_id);
        // --------------------------------------------------------
        /*
         * 找完申请信息后就去插入到各种表内
         * */
        // 添加到学院用户表和登录表进去
        scholatService.addApplyToSchoolUnit(apply);
        System.out.println("插入school_unit表的unit_id为：" + apply.getUnit_id());
        scholatService.addApplyToUnitProfile(apply);
        System.out.println("插入unit_profile表的unit_profile_id为：" + apply.getUnit_profile_id());
        scholatService.addApplyToLogin(apply);
        System.out.println("插入login表的login_id为：" + apply.getLogin_id());
        // ------------------------------------------------------------------
        // 插入到其他表后，下一步去修改申请状态为通过，即state为0
        scholatService.updateApplySuccess(apply);
        System.out.println("修改登录表状态为申请成功");
        System.out.println("----------------- 结束请求：允许通过申请 ------------------");
        /*-------------------开始发送邮件---------------------------------------*/
        String email=jsonObject.getString("email");
        String username=jsonObject.getString("username");
        String school_name=jsonObject.getString("school_name");
        String school_domain=jsonObject.getString("school_domain");
        String domain_name=jsonObject.getString("domain_name");
        String unit_name=jsonObject.getString("unit_name");
        String phone=jsonObject.getString("phone");
        String chinese_name=jsonObject.getString("chinese_name");
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
                "                                亲爱的 "+chinese_name+"</font>\n" +
                "                        </font>\n" +
                "                    </h2>\n" +
                "                    <p>首先感谢您加入学者网师资栏目管理系统！下面是您的账号信息<br>\n" +
                "                        学校名称：<b>"+school_name+"</b><br>\n" +
                "                        学校域名：<b>"+school_domain+"</b><br>\n" +
                "                        学院名称：<b>"+unit_name+"</b><br>\n" +
                "                        学院域名：<b>"+domain_name+"</b><br>\n" +
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
        mailService.sendHtmlMail(email,"申请注册成功",content);
        System.out.println("成功了");
        System.out.println("发送email结束");
        return CommonUtil.successJson();
    }
    @PostMapping("/apply/allowApplySchool")
    public JSONObject allowApplySchool(@RequestBody JSONObject requestJson) {
        System.out.println("----------------- 请求：允许通过申请 ------------------");
        // 获取申请表信息
        int apply_id = Integer.parseInt(requestJson.get("apply_id").toString());
        System.out.println("查看请求的id为：" + apply_id);
        JSONObject jsonObject = scholatService.getApplySchoolInfo(requestJson);
        System.out.println("找到的申请信息如下：");
        System.out.println(jsonObject);
        Apply apply = JSONObject.toJavaObject(jsonObject, Apply.class);
        apply.setSchool_id(apply_id);
        // --------------------------------------------------------
        /*
         * 找完申请信息后就去插入到各种表内
         * */
        // 添加到学院用户表和登录表进去
        scholatService.addApplyToSchool(apply);
        System.out.println("插入school表的school_id为：" + apply.getSchool_id());
        scholatService.addApplyToSchoolProfile(apply);
        System.out.println("插入school_profile表的unit_profile_id为：" + apply.getSchool_profile_id());
        scholatService.addApplySchoolToLogin(apply);
        System.out.println("插入login表的login_id为：" + apply.getLogin_id());
        // ------------------------------------------------------------------
        // 插入到其他表后，下一步去修改申请状态为通过，即state为0
        scholatService.updateApplySchoolSuccess(apply);
        System.out.println("修改登录表状态为申请成功");
        System.out.println("----------------- 结束请求：允许通过申请 ------------------");
        /*-------------------开始发送邮件---------------------------------------*/
        String email=jsonObject.getString("email");
        String username=jsonObject.getString("username");
        String school_name=jsonObject.getString("school_name");
        String school_domain=jsonObject.getString("school_domain");
        String phone=jsonObject.getString("phone");
        String chinese_name=jsonObject.getString("chinese_name");
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
                "                                亲爱的 "+chinese_name+"</font>\n" +
                "                        </font>\n" +
                "                    </h2>\n" +
                "                    <p>首先感谢您加入学者网师资栏目管理系统！下面是您的账号信息<br>\n" +
                "                        学校名称：<b>"+school_name+"</b><br>\n" +
                "                        学校域名：<b>"+school_domain+"</b><br>\n" +
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
        mailService.sendHtmlMail(email,"申请注册成功",content);
        System.out.println("成功了");
        System.out.println("发送email结束");
        return CommonUtil.successJson();
    }
    /**
     * 拒绝申请
     */
    @PostMapping("/apply/rejectApply")
    public JSONObject rejectApply(@RequestBody JSONObject requestJson) {
        System.out.println("----------------- 请求：拒绝申请 ------------------");
        // 获取申请表信息
        int apply_id = Integer.parseInt(requestJson.get("apply_id").toString());
        System.out.println("查看拒绝请求的id为：" + apply_id);
        JSONObject jsonObject = scholatService.getApplyInfo(requestJson);
        System.out.println("找到的申请信息如下：");
        System.out.println(jsonObject);
        Apply apply = JSONObject.toJavaObject(jsonObject, Apply.class);
        apply.setId(apply_id);
        // --------------------------------------------------------
        // 将申请者的state改为待修改状态,即为2
        scholatService.updateApplyModify(apply);
        System.out.println("该学院申请修改为了待修改状态,该学院token为:"+apply.getToken()
        +" email为:"+apply.getEmail());
        // ---------------------------------------------------------

        System.out.println("--------------- 动作开始:发送模板邮件 ---------------------");
        //向Thymeleaf模板传值，并解析成字符串
        String email=jsonObject.getString("email");
        Context context = new Context();
        context.setVariable("token", apply.getToken());
        String emailContent = templateEngine.process("unitApplyModify", context);
        mailService.sendHtmlMail(email, "学院师资队伍栏目申请修改", emailContent);
        // ===============================================================
        return CommonUtil.successJson();
    }
    @PostMapping("/apply/rejectApplySchool")
    public JSONObject rejectApplySchool(@RequestBody JSONObject requestJson) {
        System.out.println("----------------- 请求：拒绝申请 ------------------");
        // 获取申请表信息
        int apply_id = Integer.parseInt(requestJson.get("apply_id").toString());
        System.out.println("查看拒绝请求的id为：" + apply_id);
        JSONObject jsonObject = scholatService.getApplySchoolInfo(requestJson);
        System.out.println("找到的申请信息如下：");
        System.out.println(jsonObject);
        Apply apply = JSONObject.toJavaObject(jsonObject, Apply.class);
        apply.setId(apply_id);
        // --------------------------------------------------------
        // 将申请者的state改为待修改状态,即为2
        scholatService.updateApplySchoolModify(apply);
        System.out.println("该学院申请修改为了待修改状态,该学校token为:"+apply.getToken()
                +" email为:"+apply.getEmail());
        // ---------------------------------------------------------

        System.out.println("--------------- 动作开始:发送模板邮件 ---------------------");
        //向Thymeleaf模板传值，并解析成字符串
        String email=jsonObject.getString("email");
        Context context = new Context();
        context.setVariable("token", apply.getToken());
        String emailContent = templateEngine.process("unitApplyModify", context);
        mailService.sendHtmlMail(email, "学校师资队伍栏目申请修改", emailContent);
        // ===============================================================
        return CommonUtil.successJson();
    }
    /**
     * 已通过申请的学院加入到黑名单
     */
    @PostMapping("/apply/blackAll")
    public JSONObject blackAll(@RequestBody JSONObject requestJson) {
        System.out.println("----------------- 请求：加入到黑名单 ------------------");

        // --------------------------------------------------------
        // 将申请者的state改为黑名单,即为-1  申请完成后表的state的也要变为-1,登录账号state变为-1
        scholatService.updateAllBlack(requestJson);
        // ===============================================================
        return CommonUtil.successJson();
    }

    @RequiresPermissions("unit:list")
    @GetMapping("/apply/search")
    public JSONObject search(HttpServletRequest request) {
        System.out.println("----------------- 请求：搜索名单 ------------------");
        System.out.println(CommonUtil.request2Json(request));
        // --------------------------------------------------------
        // 将申请者的state改为黑名单,即为-1  申请完成后表的state的也要变为-1,登录账号state变为-1
        return scholatService.search(CommonUtil.request2Json(request));
    }
    /**
     * 加入到黑名单
     */
    @PostMapping("/apply/blackApply")
    public JSONObject blackApply(@RequestBody JSONObject requestJson) {
        System.out.println("----------------- 请求：加入到黑名单 ------------------");
        // 获取申请表信息
        int apply_id = Integer.parseInt(requestJson.get("apply_id").toString());
        System.out.println("查看拒绝请求的id为：" + apply_id);
        JSONObject jsonObject = scholatService.getApplyInfo(requestJson);
        System.out.println("找到的申请信息如下：");
        System.out.println(jsonObject);
        Apply apply = JSONObject.toJavaObject(jsonObject, Apply.class);
        apply.setId(apply_id);
        // --------------------------------------------------------
        // 将申请者的state改为黑名单,即为-1  申请完成后表的state的也要变为-1,登录账号state变为-1
        scholatService.updateApplyBlack(apply);
        // ===============================================================
        return CommonUtil.successJson();
    }
    @PostMapping("/apply/blackApplySchool")
    public JSONObject blackApplySchool(@RequestBody JSONObject requestJson) {
        System.out.println("----------------- 请求：加入到黑名单 ------------------");
        // 获取申请表信息
        int apply_id = Integer.parseInt(requestJson.get("apply_id").toString());
        System.out.println("查看拒绝请求的id为：" + apply_id);
        JSONObject jsonObject = scholatService.getApplySchoolInfo(requestJson);
        System.out.println("找到的申请信息如下：");
        System.out.println(jsonObject);
        Apply apply = JSONObject.toJavaObject(jsonObject, Apply.class);
        apply.setId(apply_id);
        // --------------------------------------------------------
        // 将申请者的state改为黑名单,即为-1  申请完成后表的state的也要变为-1,登录账号state变为-1
        scholatService.updateApplySchoolBlack(apply);
        // ===============================================================
        return CommonUtil.successJson();
    }
    /**
     * 从黑名单中移除
     */
    @PostMapping("/apply/cancelBlackApply")
    public JSONObject cancelBlackApply(@RequestBody JSONObject requestJson) {
        System.out.println("----------------- 请求：从黑名单中移除 ------------------");
        // 获取申请表信息
        int apply_id = Integer.parseInt(requestJson.get("apply_id").toString());
        System.out.println("查看拒绝请求的id为：" + apply_id);
        JSONObject jsonObject = scholatService.getApplyInfo(requestJson);
        System.out.println("找到的申请信息如下：");
        System.out.println(jsonObject);
        Apply apply = JSONObject.toJavaObject(jsonObject, Apply.class);
        apply.setId(apply_id);
        // --------------------------------------------------------
        // 将申请者的state改为黑名单,即为-1
        scholatService.updateCancelBlackApply(apply);
        // ===============================================================
        return CommonUtil.successJson();
    }
    @PostMapping("/apply/cancelBlackApplySchool")
    public JSONObject cancelBlackApplySchool(@RequestBody JSONObject requestJson) {
        System.out.println("----------------- 请求：从黑名单中移除 ------------------");
        // 获取申请表信息
        int apply_id = Integer.parseInt(requestJson.get("apply_id").toString());
        System.out.println("查看拒绝请求的id为：" + apply_id);
        JSONObject jsonObject = scholatService.getApplySchoolInfo(requestJson);
        System.out.println("找到的申请信息如下：");
        System.out.println(jsonObject);
        Apply apply = JSONObject.toJavaObject(jsonObject, Apply.class);
        apply.setId(apply_id);
        // --------------------------------------------------------
        // 将申请者的state改为黑名单,即为-1
        scholatService.updateCancelBlackApplySchool(apply);
        // ===============================================================
        return CommonUtil.successJson();
    }

    @PostMapping("/apply/validate")
    public JSONObject validate(@RequestBody JSONObject requestJson) {
        System.out.println("----------------- 验证：验证申请表信息 ------------------");
        System.out.println("需要验证的信息为：" + requestJson.get("username").toString());
        int toltalNum = scholatService.applyValidate(requestJson);
        System.out.println("数据库中找到的相同username数量为：" + toltalNum);
        if (toltalNum != 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("exist", "找到了相同的username");
            return CommonUtil.successJson(jsonObject);
        } else {
            return CommonUtil.successJson();
        }
    }

    /*
     * @Function： 邀请成为学者网用户
     * @description： 通过发送已有的数据来进行邀请注册学者网用户
     * */
    @PostMapping("/invite")
    public JSONObject invite(@RequestBody JSONObject requestJson) {
        System.out.println("----------------- 开始请求：邀请成为学者网用户 ------------------");
        System.out.println("传入的信息为：");
        System.out.println(requestJson);
        // 开始进行邮箱邀请发送
        String callback = InviteToScholat.inviteToScholat(requestJson);
        System.out.println("发送邀请注册为学者网的邮件回调信息为:"+callback);
        if(callback.equals("{\"state\":\"email_lock\"}")||callback.equals("{\"state\":\"email_exists\"}")){
            System.out.println("邮件已发送");
            return CommonUtil.errorJson(ErrorEnum.E_10009);
        }else if(callback.equals("{\"state\":\"empty_fail\"}")){
            System.out.println("发送邀请注册邮件失败,缺少必填参数邮箱");
            return CommonUtil.errorJson(ErrorEnum.E_90003);
        }else if(callback.equals("{\"state\":\"success\"}")){
            System.out.println("发送邀请注册邮件成功");
            return CommonUtil.successJson();
        }else{
            System.out.println("发送邀请注册邮件失败");
            return CommonUtil.errorJson(ErrorEnum.E_400);
        }
    }
    @PostMapping("/changePassword")
    public JSONObject changePassword(@RequestBody JSONObject requestJson) {
        System.out.println("requestJson="+requestJson);
        CommonUtil.hasAllRequired(requestJson, "newPassword,changePasswordId");
        System.out.println("加密前"+requestJson.get("newPassword").toString());
        String newPassword = MD5Util.toDb(requestJson.get("newPassword").toString());
        requestJson.put("newPassword",newPassword);
        System.out.println("加密后："+requestJson.get("newPassword").toString());
        return scholatService.changePassword(requestJson);
    }
    @GetMapping("delete/{id}")
    public JSONObject delete(@PathVariable("id") Long id){
        System.out.println("删除unit_profile中信息");
        int i = scholatService.deleteUnit(id);
        return CommonUtil.successJson();
    }
}
