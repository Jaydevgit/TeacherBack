package com.teacher.scholat.controller;


import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.service.AcademicService;
import com.teacher.scholat.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deduplication")
public class DeduplicationController {
    @Autowired
    AcademicService academicService;

    @RequestMapping("/deduplicationPaper")
    public void deduplicationPaper(){
        System.out.println("论文去重标记启动...");
        academicService.deduplicationPaper();
    }

}
