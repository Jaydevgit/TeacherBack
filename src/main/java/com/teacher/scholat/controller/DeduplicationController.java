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

    @RequestMapping("/deduplicationProject")
    public void deduplicationProject(){
        System.out.println("项目去重标记启动...");
        academicService.deduplicationProject();
    }

    @RequestMapping("/deduplicationPatent")
    public void deduplicationPatent(){
        System.out.println("专利去重标记启动...");
        academicService.deduplicationPatent();
    }

    @RequestMapping("/deduplicationPublication")
    public void deduplicationPublication(){
        System.out.println("著作去重标记启动...");
        academicService.deduplicationPublication();
    }

}
