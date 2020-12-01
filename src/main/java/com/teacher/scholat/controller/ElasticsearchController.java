package com.teacher.scholat.controller;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.service.ElasticsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/elasticsearch")
public class ElasticsearchController {

    @Autowired
    ElasticsearchService elasticsearchService;

    @RequestMapping("/esTeacher")
    public JSONObject esTeacher(@RequestBody JSONObject requestJson){
        System.out.println(requestJson);
        return elasticsearchService.esTeacher(requestJson);
    }
}
