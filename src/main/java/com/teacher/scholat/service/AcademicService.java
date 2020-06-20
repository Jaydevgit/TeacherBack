package com.teacher.scholat.service;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

public interface AcademicService {

    JSONObject getPaperFromScholat(JSONObject jsonObject);
    JSONObject getPatentFromScholat(JSONObject jsonObject);
    JSONObject getProjectFromScholat(JSONObject jsonObject);
    JSONObject getPublicationFromScholat(JSONObject jsonObject);

    JSONObject getPaper(Long id);
    JSONObject deletePaper(JSONObject jsonObject);
    JSONObject addPaper(JSONObject jsonObject);
    JSONObject updatePaper(JSONObject jsonObject);

    JSONObject getPatent(Long id);
    JSONObject deletePatent(JSONObject jsonObject);
    JSONObject addPatent(JSONObject jsonObject);
    JSONObject updatePatent(JSONObject jsonObject);

    JSONObject getProject(Long id);
    JSONObject deleteProject(JSONObject jsonObject);
    JSONObject addProject(JSONObject jsonObject);
    JSONObject updateProject(JSONObject jsonObject);

    JSONObject listPatent(JSONObject jsonObject);
    JSONObject listPatentAll(JSONObject jsonObject);

    JSONObject listPaper(JSONObject jsonObject);
    JSONObject listPaperAll(JSONObject jsonObject);

    JSONObject listProject(JSONObject jsonObject);
    JSONObject listProjectAll(JSONObject jsonObject);

    JSONObject getPaperInYears(JSONObject jsonObject);
    JSONObject getPatentInYears(JSONObject jsonObject);
    JSONObject getProjectInYears(JSONObject jsonObject);

    JSONObject getPaperByTeacher(Long id);
    JSONObject getPatentByTeacher(Long id);
    JSONObject getProjectByTeacher(Long id);

//    JSONObject identifyTeacher(JSONObject jsonObject);
    JSONObject deletePaperTeacher(JSONObject jsonObject);
    JSONObject deletePatentTeacher(JSONObject jsonObject);
    JSONObject deleteProjectTeacher(JSONObject jsonObject);

    JSONObject addPaperTeacher(JSONObject jsonObject);
    JSONObject addPatentTeacher(JSONObject jsonObject);
    JSONObject addProjectTeacher(JSONObject jsonObject);

//    JSONObject aiPaper(Long id) throws IOException;
//
//    JSONObject aiUnitPaper(JSONObject jsonObject);
//
//    JSONObject aiCatalogue(JSONObject jsonObject) throws IOException;
//    JSONObject recommendPaper(JSONObject jsonObject) throws IOException;
//
//    JSONObject addSearch(JSONObject jsonObject);
}
