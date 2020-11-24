package com.teacher.scholat.controller;


import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.service.SchoolService;
import com.teacher.scholat.service.UnitService;
import com.teacher.scholat.util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/school")
public class SchoolController {
    @Autowired
    private SchoolService schoolService;

    /**
     * 查询学校教师列表
     */
    @RequiresPermissions("school:list")
    @GetMapping("/listTeacher")
    public JSONObject listTeacher(HttpServletRequest request) {
        System.out.println("......开始查询教师成员列表");
        System.out.println(CommonUtil.request2Json(request));
        return schoolService.listTeacher(CommonUtil.request2Json(request));
    }
    /**
     * 学校主页查询学校教师列表
     */
    @GetMapping("/listAllTeacher")
    public JSONObject listAllTeacher(HttpServletRequest request) {
        System.out.println("......开始查询学校主页教师成员列表");
        System.out.println(CommonUtil.request2Json(request));
        return schoolService.listAllTeacher(CommonUtil.request2Json(request));
    }
    /**
     * 查询学院教师列表
     */
    @RequiresPermissions("school:list")
    @GetMapping("/listTeacherByUnit")
    public JSONObject listTeacherByUnit(HttpServletRequest request) {
        System.out.println("......开始查询教师成员列表");
        System.out.println(CommonUtil.request2Json(request));
        return schoolService.listTeacherByUnit(CommonUtil.request2Json(request));
    }

    @GetMapping("/getSchoolInfo")
    public JSONObject getSchoolInfo(HttpServletRequest request) {
        JSONObject jsonObject = CommonUtil.request2Json(request);
        System.out.println("requestJson-----"+jsonObject);
        return schoolService.getSchoolInfo(jsonObject);
    }
    @GetMapping("/getSchoolInfo2")
    public JSONObject getSchoolInfo2(HttpServletRequest request) {
        JSONObject jsonObject = CommonUtil.request2Json(request);
        System.out.println("requestJson-----"+jsonObject);
        return schoolService.getSchoolInfo2(jsonObject);
    }
    /**
     * 模糊搜索教师
     */
    @GetMapping("/searchTeacher")
    public JSONObject searchTeacher(HttpServletRequest request) {

        return schoolService.searchTeacher(CommonUtil.request2Json(request));
    }

    @PostMapping("/updateSchoolInfo")
    public JSONObject updateSchoolInfo(@RequestBody JSONObject requestJson){
        return schoolService.updateSchoolInfo(requestJson);
    }
    /**
     * 学院列表
     */
    @RequiresPermissions("school:list")
    @GetMapping("/getUnitList")
    public JSONObject getUnitList(HttpServletRequest request) {
        System.out.println("......开始查询学院列表");
        System.out.println(CommonUtil.request2Json(request));
        return schoolService.getUnitList(CommonUtil.request2Json(request));
    }
    /**
     * 学校管理员删除教师
     */
    @RequiresPermissions("school:delete")
    @PostMapping("/deleteTeacher")
    public JSONObject deleteTeacher(@RequestBody JSONObject requestJson) {
        System.out.println("----------------- 开始请求：学校管理员删除教师 ------------------");
        schoolService.deleteTeacher(requestJson);
        return CommonUtil.successJson();
    }

}
