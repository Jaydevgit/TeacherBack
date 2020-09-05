package com.teacher.scholat.controller;


import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.service.CatalogueService;
import com.teacher.scholat.service.TeacherService;
import com.teacher.scholat.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/catalogue")
public class CatalogueController {
    @Autowired
    CatalogueService catalogueService;
    @Autowired
    TeacherService teacherService;

    @GetMapping("/getCatalogues")
    public JSONObject getCatalogues(HttpServletRequest request) {
        return catalogueService.getCatalogues(CommonUtil.request2Json(request));
    }

    @GetMapping("/getCatalogueNameByCatalogueId")
    public JSONObject getCatalogueNameByCatalogueId(HttpServletRequest request){
        System.out.println(CommonUtil.request2Json(request));
        return catalogueService.getCatalogueNameByCatalogueId(CommonUtil.request2Json(request));
    }

    @GetMapping("/getTeacherByCatalogue")
    public JSONObject getTeacherByCatalogue(HttpServletRequest request){
        System.out.println(CommonUtil.request2Json(request));
        return catalogueService.getTeacherByCatalogue(CommonUtil.request2Json(request));
    }

    @GetMapping("/getTeacherByCatalogueId")
    public JSONObject getTeacherByCatalogueId(HttpServletRequest request){
        System.out.println(CommonUtil.request2Json(request));
        return catalogueService.getTeacherByCatalogueId(CommonUtil.request2Json(request));
    }

    @GetMapping("/getTeacherAllCatalogues")
    public JSONObject getTeacherAllCatalogues(HttpServletRequest request){
        System.out.println(CommonUtil.request2Json(request));
        return catalogueService.getTeacherAllCatalogues(CommonUtil.request2Json(request));
    }



    @GetMapping("/getTeacherByCatalogueAndPage")
    public JSONObject getTeacherByCatalogueAndPage(HttpServletRequest request){
        return catalogueService.getTeacherByCatalogueAndPage(CommonUtil.request2Json(request));
    }

    @GetMapping("/getByCatalogueAndLetterAndPage")
    public JSONObject getByCatalogueAndLetterAndPage(HttpServletRequest request){
        return catalogueService.getByCatalogueAndLetterAndPage(CommonUtil.request2Json(request));
    }

    @PostMapping("/addCatalogue")
    public JSONObject addCatalogue(@RequestBody JSONObject requestJson){
        CommonUtil.hasAllRequired(requestJson, "unitId,catalogue");
        return  catalogueService.addCatalogue(requestJson);
    }

    @PostMapping("/deleteCatalogue")
    public JSONObject deleteCatalogue(@RequestBody JSONObject requestJson){
        CommonUtil.hasAllRequired(requestJson, "cId");
        return  catalogueService.deleteCatalogue(requestJson);
    }

    @PostMapping("/updateCatalogue")
    public JSONObject updateCatalogue(@RequestBody JSONObject requestJson){
        System.out.println("修改栏目名为"+(requestJson));
        CommonUtil.hasAllRequired(requestJson,"cId,newName");
        return catalogueService.updateCatalogue(requestJson);
    }

    @PostMapping("/addSubCatalogue")
    public JSONObject addSubCatalogue(@RequestBody JSONObject requestJson){
        CommonUtil.hasAllRequired(requestJson, "unitId,catalogue,parentId");
        return  catalogueService.addSubCatalogue(requestJson);
    }

    @PostMapping("/removeTeacher")
    public JSONObject removeTeacher(@RequestBody JSONObject requestJson){
        CommonUtil.hasAllRequired(requestJson,"ctId");
        return catalogueService.removeTeacher(requestJson);
    }

    @PostMapping("/addSingleTeacher")
    public JSONObject addSingleTeacher(@RequestBody JSONObject requestJson){
        CommonUtil.hasAllRequired(requestJson,"tId,cId");
        System.out.println(requestJson.getString("tId"));
        return catalogueService.addSingleTeacher(requestJson);
    }

    @PostMapping("/addCatalogueTeacher")
    public JSONObject addCatalogueTeacher(@RequestBody JSONObject requestJson){
        CommonUtil.hasAllRequired(requestJson,"tIds,cId");
        System.out.println(requestJson.getString("cId"));
        return catalogueService.addCatalogueTeacher(requestJson);
    }

    @PostMapping("/updateCatalogueTeacher")
    public JSONObject updateCatalogueTeacher(@RequestBody JSONObject requestJson){
        CommonUtil.hasAllRequired(requestJson,"ctId,cId");
        System.out.println(requestJson.getString("ctId"));
        return catalogueService.updateCatalogueTeacher(requestJson);
    }

    @PostMapping("/topTeacher")
    public JSONObject topTeacher(@RequestBody JSONObject requestJson){
        CommonUtil.hasAllRequired(requestJson,"ctId,tSeq");
        return catalogueService.topTeacher(requestJson);
    }

    @PostMapping("/topCatalogue")
    public JSONObject topCatalogue(@RequestBody JSONObject requestJson){
        CommonUtil.hasAllRequired(requestJson,"cId,seq");
        return catalogueService.topCatalogue(requestJson);
    }

    @PostMapping("/sortCatalogue")
    public JSONObject sortCatalogue(@RequestBody JSONObject requestJson){
        CommonUtil.hasAllRequired(requestJson,"cIds,unitId");
        return catalogueService.sortCatalogue(requestJson);
    }

    @PostMapping("/sortTeacher")
    public JSONObject sortTeacher(@RequestBody JSONObject requestJson){
        //id ---- 栏目ID
        CommonUtil.hasAllRequired(requestJson,"ctIds,id");
        return catalogueService.sortTeacher(requestJson);
    }

    @PostMapping("/sortUnitTeacher")
    public JSONObject sortUnitTeacher(@RequestBody JSONObject requestJson){
        CommonUtil.hasAllRequired(requestJson,"tIds,unitId");
        return catalogueService.sortUnitTeacher(requestJson);
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
     * 查询教师列表 没有学者网关联账号
     */
    @GetMapping("/listTeacherAllNoScholat")
    public JSONObject listTeacherAllNoScholat(HttpServletRequest request) {
        System.out.println("......开始查询教师成员列表");
        System.out.println(CommonUtil.request2Json(request));
        return teacherService.listTeacherAllNoScholat(CommonUtil.request2Json(request));
    }
}
