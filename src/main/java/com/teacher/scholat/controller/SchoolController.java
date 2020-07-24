package com.teacher.scholat.controller;


import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.service.SchoolService;
import com.teacher.scholat.service.UnitService;
import com.teacher.scholat.util.CommonUtil;
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

    @GetMapping("/getSchoolInfo")
    public JSONObject getSchoolInfo(HttpServletRequest request) {

        JSONObject jsonObject = CommonUtil.request2Json(request);
        System.out.println("requestJson-----"+jsonObject);
        return schoolService.getSchoolInfo(jsonObject);
    }
}
