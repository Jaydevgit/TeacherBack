package com.teacher.scholat.controller;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.service.AcademicService;
import com.teacher.scholat.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/academic")
public class AcademicController {

    @Autowired
    AcademicService academicService;

//    @GetMapping("/identifyTeacher")
//    public JSONObject identifyTeacher(HttpServletRequest request){
//
//        return academicService.identifyTeacher(CommonUtil.request2Json(request));
//    }

    @GetMapping("/paperInfo/{id}")
    public JSONObject getPaperInfo(@PathVariable("id") long id){
        // howHttpRequestContent(request);
        System.out.println("传来来的参数id为。。。。。。"+id);
        return CommonUtil.successJson(academicService.getPaper(id));
    }

    @GetMapping("/projectInfo/{id}")
    public JSONObject getProjectInfo(@PathVariable("id") long id){
        // howHttpRequestContent(request);
        System.out.println("传来来的参数id为。。。。。。"+id);
        return CommonUtil.successJson(academicService.getProject(id));
    }

    @GetMapping("/patentInfo/{id}")
    public JSONObject getPatentInfo(@PathVariable("id") long id){
        // howHttpRequestContent(request);
        System.out.println("传来来的参数id为。。。。。。"+id);
        return CommonUtil.successJson(academicService.getPatent(id));
    }

    @RequestMapping("/getAcademicFromScholat")
    public JSONObject getResearchFromScholat(@RequestBody JSONObject requestJson){
        CommonUtil.hasAllRequired(requestJson,"scholat_username");
        System.out.println("------scholat_username------" + requestJson.getString("scholat_username"));
        return CommonUtil.successJson();
    }

    @RequestMapping("/getPaperFromScholat")
    public JSONObject getPaperFromScholat(@RequestBody JSONObject requestJson){
        CommonUtil.hasAllRequired(requestJson,"scholat_username");
        System.out.println("------scholat_username------" + requestJson.getString("scholat_username"));
        return academicService.getPaperFromScholat(requestJson);
    }

    @RequestMapping("/getPatentFromScholat")
    public JSONObject getPatentFromScholat(@RequestBody JSONObject requestJson){
        CommonUtil.hasAllRequired(requestJson,"scholat_username");
        System.out.println("------scholat_username------" + requestJson.getString("scholat_username"));
        return academicService.getPatentFromScholat(requestJson);
    }

    @RequestMapping("/getProjectFromScholat")
    public JSONObject getProjectFromScholat(@RequestBody JSONObject requestJson){
        CommonUtil.hasAllRequired(requestJson,"scholat_username");
        System.out.println("------scholat_username------" + requestJson);
        return academicService.getProjectFromScholat(requestJson);
    }

    @RequestMapping("/getPublicationFromScholat")
    public JSONObject getPublicationFromScholat(@RequestBody JSONObject requestJson){
        CommonUtil.hasAllRequired(requestJson,"scholat_username");
        System.out.println("------scholat_username------" + requestJson);
        return academicService.getPublicationFromScholat(requestJson);
    }
    //根据教师名、科研类别、日期查询论文信息
    @RequestMapping("/getAchievement")
    public JSONObject getAchievement(@RequestBody JSONObject requestJson) {
       // System.out.println("查询科研信息请求参数为:" + requestJson+requestJson.getIntValue("type"));
        if(requestJson.getIntValue("type")==0){
            return  academicService.getPaperteacher(requestJson);
        }else if(requestJson.getIntValue("type")==1){
            return  academicService.getProjectteacher(requestJson);
        }else if(requestJson.getIntValue("type")==2){
            return  academicService.getPatentteacher(requestJson);
        }
        return CommonUtil.successJson();
    }
    @RequestMapping("/listPaper")
    public JSONObject listPaper(HttpServletRequest request){
        return academicService.listPaper(CommonUtil.request2Json(request));
    }

    @GetMapping("/listPaperAll")
    public JSONObject listPaperAll(HttpServletRequest request) {
        System.out.println("......开始查询所有论文列表");
        System.out.println(CommonUtil.request2Json(request));
        return academicService.listPaperAll(CommonUtil.request2Json(request));
    }


    @RequestMapping("/deletePaper")
    public JSONObject deletePaper(@RequestBody JSONObject requestJson) {
        System.out.println("----------------- 开始请求：删除论文 ------------------");
        return academicService.deletePaper(requestJson);
    }

    @RequestMapping("/addPaper")
    public JSONObject addPaper(@RequestBody JSONObject requestJson) {
        System.out.println("----------------- 开始请求：增加论文 ------------------");
        System.out.println("--------获取到的要求：---------" + requestJson);
        return academicService.addPaper(requestJson);
    }
    @RequestMapping("/addAllPaper")
    public JSONObject addAllPaper(@RequestBody JSONObject requestJson) {
        System.out.println("----------------- 开始请求：增加所有论文 ------------------");
      //  System.out.println("--------获取到的要求：---------" + requestJson);
        return academicService.addAllPaper(requestJson);
    }
    @RequestMapping("/addAllProject")
    public JSONObject addAllProject(@RequestBody JSONObject requestJson) {
        System.out.println("----------------- 开始请求：增加所有项目 ------------------");
        //  System.out.println("--------获取到的要求：---------" + requestJson);
        return academicService.addAllProject(requestJson);
    }
    @RequestMapping("/addAllPatent")
    public JSONObject addAllPatent(@RequestBody JSONObject requestJson) {
        System.out.println("----------------- 开始请求：增加所有专利 ------------------");
        //  System.out.println("--------获取到的要求：---------" + requestJson);
        return academicService.addAllPatent(requestJson);
    }
    @RequestMapping("/addAllPublication")
    public JSONObject addAllPublication(@RequestBody JSONObject requestJson) {
        System.out.println("----------------- 开始请求：增加所有著作------------------");
        //  System.out.println("--------获取到的要求：---------" + requestJson);
        return academicService.addAllPublication(requestJson);
    }

    @RequestMapping("/updatePaper")
    public JSONObject updatePaper(@RequestBody JSONObject requestJson) {
        System.out.println("----------------- 开始请求：更新论文 ------------------");
        System.out.println("--------获取到的要求：---------" + requestJson);
        return academicService.updatePaper(requestJson);
    }

    @RequestMapping("/updateProject")
    public JSONObject updateProject(@RequestBody JSONObject requestJson) {
        System.out.println("----------------- 开始请求：更新项目 ------------------");
        System.out.println("--------获取到的要求：---------" + requestJson);
        return academicService.updateProject(requestJson);
    }

    @RequestMapping("/updatePatent")
    public JSONObject updatePatent(@RequestBody JSONObject requestJson) {
        System.out.println("----------------- 开始请求：更新专利 ------------------");
        System.out.println("--------获取到的要求：---------" + requestJson);
        return academicService.updatePatent(requestJson);
    }

    @RequestMapping("/listProject")
    public JSONObject listProject(HttpServletRequest request){
        return academicService.listProject(CommonUtil.request2Json(request));
    }

    @GetMapping("/listProjectAll")
    public JSONObject listProjectAll(HttpServletRequest request) {
        System.out.println("......开始查询所有项目列表");
        System.out.println(CommonUtil.request2Json(request));
        return academicService.listProjectAll(CommonUtil.request2Json(request));
    }

    @RequestMapping("/deleteProject")
    public JSONObject deleteProject(@RequestBody JSONObject requestJson) {
        System.out.println("----------------- 开始请求：删除论文 ------------------");
        return academicService.deleteProject(requestJson);
    }

    @RequestMapping("/addProject")
    public JSONObject addProject(@RequestBody JSONObject requestJson) {
        System.out.println("----------------- 开始请求：增加项目 ------------------");
       // System.out.println("--------获取到的要求：---------" + requestJson);
        return academicService.addProject(requestJson);
    }


    @RequestMapping("/listPatent")
    public JSONObject listPatent(HttpServletRequest request){
        return academicService.listPatent(CommonUtil.request2Json(request));
    }

    @GetMapping("/listPatentAll")
    public JSONObject listPatentAll(HttpServletRequest request) {
        System.out.println("......开始查询所有专利列表");
        System.out.println(CommonUtil.request2Json(request));
        return academicService.listPatentAll(CommonUtil.request2Json(request));
    }

    @RequestMapping("/deletePatent")
    public JSONObject deletePatent(@RequestBody JSONObject requestJson) {
        System.out.println("----------------- 开始请求：删除专利 ------------------");
        return academicService.deletePatent(requestJson);
    }

    @RequestMapping("/addPatent")
    public JSONObject addPatent(@RequestBody JSONObject requestJson) {
        System.out.println("----------------- 开始请求：增加专利 ------------------");
        System.out.println("--------获取到的要求：---------" + requestJson);
        return academicService.addPatent(requestJson);
    }
    @RequestMapping("/addPublication")
    public JSONObject addPublication(@RequestBody JSONObject requestJson) {
        System.out.println("----------------- 开始请求：增加著作 ------------------");
        System.out.println("--------获取到的要求：---------" + requestJson);
        return academicService.addPublication(requestJson);
    }

    @GetMapping("/getPaperInYears")
    public JSONObject getPaperInYears(HttpServletRequest request) {
        System.out.println("......开始查询所有专利列表");
        return academicService.getPaperInYears(CommonUtil.request2Json(request));
    }

    @GetMapping("/getProjectInYears")
    public JSONObject getProjectInYears(HttpServletRequest request) {
        System.out.println("......开始查询所有专利列表");
        return academicService.getProjectInYears(CommonUtil.request2Json(request));
    }

    @GetMapping("/getPatentInYears")
    public JSONObject getPatentInYears(HttpServletRequest request) {
        System.out.println("......开始查询所有专利列表");
        return academicService.getPatentInYears(CommonUtil.request2Json(request));
    }

    @RequestMapping("/deletePaperTeacher")
    public JSONObject deletePaperTeacher(@RequestBody JSONObject requestJson){
        return academicService.deletePaperTeacher(requestJson);
    }

    @RequestMapping("/deletePatentTeacher")
    public JSONObject deletePatentTeacher(@RequestBody JSONObject requestJson){
        return academicService.deletePatentTeacher(requestJson);
    }

    @RequestMapping("/deleteProjectTeacher")
    public JSONObject deleteProjectTeacher(@RequestBody JSONObject requestJson){
        return academicService.deleteProjectTeacher(requestJson);
    }

    @RequestMapping("/addPaperTeacher")
    public JSONObject addPaperTeacher(@RequestBody JSONObject requestJson){
        return academicService.addPaperTeacher(requestJson);
    }
    @RequestMapping("/addPatentTeacher")
    public JSONObject addPatentTeacher(@RequestBody JSONObject requestJson){
        return academicService.addPatentTeacher(requestJson);
    }
    @RequestMapping("/addProjectTeacher")
    public JSONObject addProjectTeacher(@RequestBody JSONObject requestJson){
        return academicService.addProjectTeacher(requestJson);
    }

//    @GetMapping("/aiPaper")
//    public JSONObject aiPaper(HttpServletRequest request) throws IOException {
//        return academicService.aiPaper(CommonUtil.request2Json(request).getLongValue("id"));
//    }

    @GetMapping("/getPaperByTeacher")
    public JSONObject getPaperByTeacher(HttpServletRequest request) {
        return academicService.getPaperByTeacher(CommonUtil.request2Json(request).getLongValue("id"));
    }

    @GetMapping("/getProjectByTeacher")
    public JSONObject getProjectByTeacher(HttpServletRequest request) {
        return academicService.getProjectByTeacher(CommonUtil.request2Json(request).getLongValue("id"));
    }

    @GetMapping("/getPatentByTeacher")
    public JSONObject getPatentByTeacher(HttpServletRequest request) {
        return academicService.getPatentByTeacher(CommonUtil.request2Json(request).getLongValue("id"));
    }

//    @GetMapping("/aiUnitPaper")
//    public JSONObject aiUnitPaper(HttpServletRequest request){
//        return academicService.aiUnitPaper(CommonUtil.request2Json(request));
//    }
//
//    @GetMapping("/aiCatalogue")
//    public JSONObject aiCatalogue(HttpServletRequest request) throws IOException {
//        return academicService.aiCatalogue(CommonUtil.request2Json(request));
//    }
//
//    @GetMapping("/recommendPaper")
//    public JSONObject recommendPaper(HttpServletRequest request) throws IOException {
//        return academicService.recommendPaper(CommonUtil.request2Json(request));
//    }
//
//    @RequestMapping("/searchAcademic")
//    public JSONObject searchAcademic(@RequestBody JSONObject requestJson) {
//        System.out.println("猜猜我是谁"+requestJson);
//        return academicService.addSearch(requestJson);
//    }
}
