package com.teacher.scholat.controller;


import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.service.SchoolService;
import com.teacher.scholat.service.UnitService;
import com.teacher.scholat.util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/school")
public class SchoolController {
    @Autowired
    private SchoolService schoolService;

    /**
     * 查询教师列表
     */
    @RequiresPermissions("school:list")
    @GetMapping("/listTeacher")
    public JSONObject listTeacher(HttpServletRequest request) {
        System.out.println("......开始查询教师成员列表");
        System.out.println(CommonUtil.request2Json(request));
        return schoolService.listTeacher(CommonUtil.request2Json(request));
    }

    @GetMapping("/getSchoolInfo")
    public JSONObject getSchoolInfo(HttpServletRequest request) {
        JSONObject jsonObject = CommonUtil.request2Json(request);
        System.out.println("requestJson-----"+jsonObject);
        return schoolService.getSchoolInfo(jsonObject);
    }
    /**
     * 模糊搜索教师
     */
    @GetMapping("/searchTeacher")
    public JSONObject searchTeacher(HttpServletRequest request) {

        return schoolService.searchTeacher(CommonUtil.request2Json(request));
    }
}
