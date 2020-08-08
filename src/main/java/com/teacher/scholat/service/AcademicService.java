package com.teacher.scholat.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AcademicService {

    JSONObject getPaperFromScholat(JSONObject jsonObject);
    JSONObject getPatentFromScholat(JSONObject jsonObject);
    JSONObject getProjectFromScholat(JSONObject jsonObject);
    JSONObject getPublicationFromScholat(JSONObject jsonObject);

    JSONObject getPaper(Long id);
    JSONObject deletePaper(JSONObject jsonObject);
    JSONObject deletePubilcation(JSONObject jsonObject);
    JSONObject addPaper(JSONObject jsonObject);
    JSONObject addAllPaper(JSONObject jsonObject);
    JSONObject addAllProject(JSONObject jsonObject);
    JSONObject addAllPatent(JSONObject jsonObject);
    JSONObject addAllPublication(JSONObject jsonObject);
    JSONObject updatePaper(JSONObject jsonObject);

    JSONObject getPatent(Long id);
    JSONObject deletePatent(JSONObject jsonObject);
    JSONObject addPatent(JSONObject jsonObject);
    JSONObject updatePatent(JSONObject jsonObject);

    JSONObject getProject(Long id);
    JSONObject deleteProject(JSONObject jsonObject);
    JSONObject addProject(JSONObject jsonObject);
    JSONObject updateProject(JSONObject jsonObject);
    JSONObject updatePublication(JSONObject jsonObject);

    JSONObject getPublication(Long id);
    JSONObject addPublication(JSONObject jsonObject);

    JSONObject listPatent(JSONObject jsonObject);
    JSONObject listPatentAll(JSONObject jsonObject);

    JSONObject listPaper(JSONObject jsonObject);
    JSONObject listPublication(JSONObject jsonObject);
    JSONObject listPaperAll(JSONObject jsonObject);
    JSONObject getPaperteacher(JSONObject jsonObject);
    JSONObject searchPaper(JSONObject jsonObject);

    JSONObject getProjectteacher(JSONObject jsonObject);
    JSONObject getPatentteacher(JSONObject jsonObject);
    JSONObject getPublicationteacher(JSONObject jsonObject);

    JSONObject listProject(JSONObject jsonObject);
    JSONObject listProjectAll(JSONObject jsonObject);

    JSONObject getPaperInYears(JSONObject jsonObject);
    JSONObject getPatentInYears(JSONObject jsonObject);
    JSONObject getProjectInYears(JSONObject jsonObject);

    JSONObject getPaperByTeacher(Long id);
    JSONObject getPatentByTeacher(Long id);
    JSONObject getProjectByTeacher(Long id);

    JSONObject identifyTeacher(JSONObject jsonObject);
    JSONObject deletePaperTeacher(JSONObject jsonObject);
    JSONObject deletePatentTeacher(JSONObject jsonObject);
    JSONObject deleteProjectTeacher(JSONObject jsonObject);

    JSONObject addPaperTeacher(JSONObject jsonObject);
    JSONObject addPatentTeacher(JSONObject jsonObject);
    JSONObject addProjectTeacher(JSONObject jsonObject);

    void exportPaper(HttpServletRequest request, HttpServletResponse response) throws IOException;
    void exportPaper2(HttpServletRequest request, HttpServletResponse response) throws IOException;
    void exportProject(HttpServletRequest request, HttpServletResponse response) throws IOException;
    void exportPatent(HttpServletRequest request, HttpServletResponse response) throws IOException;
    void  exportPublication(HttpServletRequest request, HttpServletResponse response) throws IOException;

    int getPaperTotal(long id);
    int getProjectTotal(long id);
    int getPatentTotal(long id);
    int getPublicationTotal(long id);

    JSONObject deleteAllStatistic(int unitId);

    JSONObject addAllStatistic(JSONObject jsonObject);

    JSONObject getTotal(long unitId);

    JSONObject getAllCount(long unitId);


//    JSONObject aiPaper(Long id) throws IOException;
//
//    JSONObject aiUnitPaper(JSONObject jsonObject);
//
//    JSONObject aiCatalogue(JSONObject jsonObject) throws IOException;
//    JSONObject recommendPaper(JSONObject jsonObject) throws IOException;
//
//    JSONObject addSearch(JSONObject jsonObject);
}
