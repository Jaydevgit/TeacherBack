package com.teacher.scholat.controller;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.service.ManagerService;
import com.teacher.scholat.service.TeacherService;
import com.teacher.scholat.service.UnitService;
import com.teacher.scholat.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/homepage")
public class HomepageController {
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private UnitService unitService;
    //获取最新更新教师名单
    @GetMapping("/updateList/{id}")
    public JSONObject getUpdatedTeacher(@PathVariable("id") Long unitId){
        System.out.println("传来来的参数id为。。。。。。"+unitId);
        List<JSONObject> teacherList =
                teacherService.getUpdatedTeacherList(unitId);
        System.out.println("controller-update--"+ teacherList);
        return CommonUtil.successJson(teacherList);
    }


    //获取最热门教师名单
    @GetMapping("/hostList/{id}")
    public JSONObject getHostTeacher(@PathVariable("id") Long unitId){
        System.out.println("传来来的参数id为。。。。。。"+unitId);
        List<JSONObject> teacherList = teacherService.getHotTeacherList(unitId);
        System.out.println("controller--hot--"+ teacherList);
        return CommonUtil.successJson(teacherList);
    }

    /**
     * 查询教师列表
     */
    @GetMapping("/listTeacher")
    public JSONObject listTeacher(HttpServletRequest request) {
        System.out.println("......开始查询教师成员列表");
        return teacherService.listTeacher(CommonUtil.request2Json(request));
    }

    /**
     * 查询教师列表
     */
    @GetMapping("/listTeacherAll")
    public JSONObject listTeacherAll(HttpServletRequest request) {
        System.out.println("......开始查询教师成员列表");
        System.out.println(CommonUtil.request2Json(request));
        return teacherService.listTeacherAll(CommonUtil.request2Json(request));
    }
    /**
     * 查询教师个人主页推荐教师
     */
    @GetMapping("/listTeacherRecommend")
    public JSONObject listTeacherRecommend(HttpServletRequest request) {
        return teacherService.listTeacherRecommend(CommonUtil.request2Json(request));
    }

    @GetMapping("/letterTeacher")
    public JSONObject letterTeacher(HttpServletRequest request) {
        System.out.println("......开始查询教师成员列表");
        System.out.println(CommonUtil.request2Json(request));
        return teacherService.letterTeacher(CommonUtil.request2Json(request));
    }
    /**
     * 查询学院基础信息
     */
    @GetMapping("/getUnitInfo")
    public JSONObject getUnit(HttpServletRequest request){
        System.out.println("...开始查询学院基础信息");
        JSONObject object = unitService.getUnitInfo(CommonUtil.request2Json(request));
        System.out.println("object....." + object);
        return object;
    }

    /**
     * 依据域名查询学院基础信息
     */
    @GetMapping("/getUnitInfo2")
    public JSONObject getUnit2(HttpServletRequest request){
        System.out.println("...开始查询学院基础信息");
        JSONObject object = unitService.getUnitInfo2(CommonUtil.request2Json(request));
        System.out.println("object....." + object);
        return object;
    }


}
