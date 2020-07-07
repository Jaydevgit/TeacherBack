package com.teacher.scholat.dao;


import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface AcademicDao {


    List<JSONObject> listPaper(JSONObject jsonObject);
    List<JSONObject> listPaperAll(JSONObject jsonObject);
    List<JSONObject> getPaperteacher(JSONObject jsonObject);

    List<JSONObject> listPatent(JSONObject jsonObject);
    List<JSONObject> listPatentAll(JSONObject jsonObject);

    List<JSONObject> listProject(JSONObject jsonObject);
    List<JSONObject> listProjectAll(JSONObject jsonObject);

    void addPaper(JSONObject jsonObject);
    int paperExitIf(long scholat_paper_id);
    JSONObject getPaper(Long id);
    void removePaper(JSONObject jsonObject);
    void updatePaper(JSONObject jsonObject);
    int countPaper(long unitId);
    void updatePaperRecommend(JSONObject jsonObject);

    void addProject(JSONObject jsonObject);
    JSONObject getProject(Long id);
    void removeProject(JSONObject jsonObject);
    void updateProject(JSONObject jsonObject);
    int countProject(long unitId);

    void addPatent(JSONObject jsonObject);
    JSONObject getPatent(Long id);
    void removePatent(JSONObject jsonObject);
    void updatePatent(JSONObject jsonObject);
    int countPatent(long unitId);

    List<Long> paperIdsByScholatname(JSONObject jsonObject);
    List<Long> projectIdsByScholatname(JSONObject jsonObject);
    List<Long> patentIdsByScholatname(JSONObject jsonObject);

    void sortUnitPaper(JSONObject jsonObject);
    List<JSONObject> getPaperByKey(JSONObject jsonObject);
    List<JSONObject> getProjectByKey(JSONObject jsonObject);
    List<JSONObject> getPatentByKey(JSONObject jsonObject);

    List<JSONObject> getPaperInYears(JSONObject jsonObject);
    List<JSONObject> getProjectInYears(JSONObject jsonObject);
    List<JSONObject> getPatentInYears(JSONObject jsonObject);

    List<JSONObject> getPaperByTeacher(Long id);
    List<JSONObject> getProjectByTeacher(Long id);
    List<JSONObject> getPatentByTeacher(Long id);

    void addPaperTeacher(Long id, List<Long> list);
    void deletePaperTeacher(JSONObject jsonObject);

    void addPatentTeacher(Long id, List<Long> list);
    void deletePatentTeacher(JSONObject jsonObject);

    void addProjectTeacher(Long id, List<Long> list);
    void deleteProjectTeacher(JSONObject jsonObject);



    List<Long> getPapersByTeacher(Long id);
}