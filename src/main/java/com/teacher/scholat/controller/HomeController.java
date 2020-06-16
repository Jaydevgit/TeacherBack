package com.teacher.scholat.controller;
import com.alibaba.fastjson.JSONObject;
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
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private UnitService unitService;

    @Autowired
    private TeacherService teacherService;

    //根据学校域名获取学院信息
    @GetMapping("/{schoolDomain}")
    public JSONObject getUnitBySchoolDomain(@PathVariable("schoolDomain") String schoolDomain){
        System.out.println("传来来的学校域名为。。。。。。"+schoolDomain);
        List<JSONObject> unitList = unitService.getUnitBySchoolDomain(schoolDomain);
        System.out.println("unitList=。。。。。。"+unitList);
        return CommonUtil.successJson(unitList);
    }


    @GetMapping("/listTeacherByUnitDomain")
    public JSONObject listTeacherByUnitDomain(HttpServletRequest request){
        System.out.println("......开始查询教师成员列表");
        System.out.println(CommonUtil.request2Json(request));
        return teacherService.listTeacherByUnitDomain(CommonUtil.request2Json(request));
    }

}
