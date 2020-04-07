package com.teacher.scholat.controller;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.model.Apply;
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

    @PostMapping("/apply")
    public JSONObject apply(@RequestBody JSONObject requestJson) {
        System.out.println("进入到了申请。。。。。。");
        System.out.println(requestJson);
       /* CommonUtil.hasAllRequired(requestJson, "username");
        CommonUtil.hasAllRequired(requestJson, "email");
        CommonUtil.hasAllRequired(requestJson, "post");
        CommonUtil.hasAllRequired(requestJson, "create_time");
        CommonUtil.hasAllRequired(requestJson, "state");*/

        // 去看下有没有重复的申请信息, 主要是去校验有没有相同的学校和学院
        int exist = registerService.judgeUnitExist(requestJson);
        System.out.println("看看该学院是否已经发起过申请：" + exist);
        if (exist != 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("exist", "该学院已发起过申请");
            return CommonUtil.successJson(jsonObject);
        } else {
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