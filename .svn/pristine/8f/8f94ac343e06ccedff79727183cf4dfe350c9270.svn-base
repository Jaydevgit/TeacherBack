package com.teacher.scholat.controller;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.model.Apply;
import com.teacher.scholat.service.MailService;
import com.teacher.scholat.service.ScholatService;
import com.teacher.scholat.util.constants.ErrorEnum;
import com.teacher.scholat.util.CommonUtil;
import com.teacher.scholat.util.InviteToScholat;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;

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
        Context context = new Context();
        context.setVariable("token", apply.getToken());
        String emailContent = templateEngine.process("unitApplyModify", context);
        mailService.sendHtmlMail("413459865@qq.com", "学院师资队伍栏目申请修改", emailContent);
        // ===============================================================
        return CommonUtil.successJson();
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
        // 将申请者的state改为黑名单,即为-1
        scholatService.updateApplyBlack(apply);
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
        if(callback.equals("error")){
            System.out.println("发送邀请注册邮件失败");
            return CommonUtil.errorJson(ErrorEnum.E_400);
        }else{
            System.out.println("发送邀请注册邮件成功");
            return CommonUtil.successJson();
        }
    }

}
