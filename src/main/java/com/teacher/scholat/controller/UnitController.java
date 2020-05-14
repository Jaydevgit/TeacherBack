package com.teacher.scholat.controller;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.service.CatalogueService;
import com.teacher.scholat.service.TeacherService;
import com.teacher.scholat.service.UnitService;
import com.teacher.scholat.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/unit")
public class UnitController {
    @Autowired
    private UnitService unitService;


    @Autowired
    private CatalogueService catalogueService;

    @GetMapping("/listDepartment")
    public JSONObject listDepartment (HttpServletRequest request) {
        JSONObject jsonObject = CommonUtil.request2Json(request);
        System.out.println("---department---" + jsonObject);
        jsonObject.put("state",0);
        return catalogueService.getDepartSubjectByUnit(jsonObject);
    }


    @GetMapping("/listSubject")
    public JSONObject listSubject (HttpServletRequest request) {
        JSONObject jsonObject = CommonUtil.request2Json(request);
        System.out.println("---subjectList---" + jsonObject);
        jsonObject.put("state",1);
        return catalogueService.getDepartSubjectByUnit(jsonObject);
    }

    @GetMapping("/getUnitInfo")
    public JSONObject getUnitInfo(HttpServletRequest request) {

        JSONObject jsonObject = CommonUtil.request2Json(request);
        System.out.println("requestJson-----"+jsonObject);
        return unitService.getUnitInfo(jsonObject);
    }

    @GetMapping("/getUnitInfoByUnitId")
    public JSONObject getUnitInfoByUnitId(HttpServletRequest request) {

        JSONObject jsonObject = CommonUtil.request2Json(request);
        System.out.println("requestJson-----"+jsonObject);
        List<JSONObject> list =unitService.getUnitInfoByUnitId(jsonObject);

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("listSubName",list);
        System.out.println("看看所有用户账号：" + jsonObject2);
        return CommonUtil.successJson(jsonObject2);
    }

    @GetMapping("/getUnitBytId")
    public JSONObject getUnitBytId(HttpServletRequest request){
        System.out.println("获取UnitBytId");
        return unitService.getUnitBytId(CommonUtil.request2Json(request));
    }

    @GetMapping("getUnitBytDomain_name")
    public JSONObject getUnitBytDomain_name(HttpServletRequest request){
        System.out.println("获取UnitBytDomain_name");
        return unitService.getUnitBytDomain_name(CommonUtil.request2Json(request));
    }


    @PostMapping("/updateUnitInfo")
    public JSONObject updateUnitInfo(@RequestBody JSONObject requestJson){
        return unitService.updateUnitInfo(requestJson);
    }

    @PostMapping("/updateUnitTagstate")
    public JSONObject updateUnitTagstate(@RequestBody JSONObject requestJson){
        return unitService.updateUnitTagstate(requestJson);
    }
}
