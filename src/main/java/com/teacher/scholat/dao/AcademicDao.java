package com.teacher.scholat.dao;


import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface AcademicDao {


    List<JSONObject> listPaper(JSONObject jsonObject);
    List<JSONObject> listPublication(JSONObject jsonObject);
    List<JSONObject> listPaperAll(JSONObject jsonObject);
    List<JSONObject> getPaperteacher(JSONObject jsonObject);
    List<JSONObject> getSearchPaper(JSONObject jsonObject);
    List<JSONObject> searchPaper(JSONObject jsonObject);

    List<JSONObject> getProjectteacher(JSONObject jsonObject);
    List<JSONObject> getSearchProject(JSONObject jsonObject);
    List<JSONObject> searchProject(JSONObject jsonObject);

    List<JSONObject> getPatentteacher(JSONObject jsonObject);
    List<JSONObject> getSearchPatent(JSONObject jsonObject);
    List<JSONObject> searchPatent(JSONObject jsonObject);

    List<JSONObject> getPublicationteacher(JSONObject jsonObject);
    List<JSONObject> listPatent(JSONObject jsonObject);
    List<JSONObject> listPatentAll(JSONObject jsonObject);

    List<JSONObject> listProject(JSONObject jsonObject);
    List<JSONObject> listProjectAll(JSONObject jsonObject);

    void addPaper(JSONObject jsonObject);
    void NoDeletePaper(long scholat_paper_id);
    void NoDeletePublication(long scholat_publication_id);
    void NoDeleteProject(long scholat_paper_id);
    void NoDeletePatent(long scholat_paper_id);

    void addPublication(JSONObject jsonObject);
    int paperExitIf(long scholat_paper_id);
    int paperDeleteExitIf(long scholat_paper_id);
    int publicationDeleteExitIf(long scholat_publication_id);


    int projectExitIf(long scholat_project_id);
    int projectDeleteExitIf(long scholat_project_id);

    int patentExitIf(long scholat_paper_id);
    int patentDeleteExitIf(long scholat_project_id);

    int publicationExitIf(long scholat_paper_id);
    JSONObject getPaper(Long id);
    void removePaper(JSONObject jsonObject);
    void removePubilcation(JSONObject jsonObject);
    void updatePaper(JSONObject jsonObject);
    int countPaper(long unitId);
    int countSearchPaper(JSONObject jsonObject);
    int countPublication(long unitId);
    void updatePaperRecommend(JSONObject jsonObject);

    void addProject(JSONObject jsonObject);
    JSONObject getProject(Long id);
    JSONObject getPublication(Long id);
    void removeProject(JSONObject jsonObject);
    void updateProject(JSONObject jsonObject);
    void updatePublication(JSONObject jsonObject);
    int countProject(long unitId);
    int countSearchProject(JSONObject jsonObject);

    void addPatent(JSONObject jsonObject);
    JSONObject getPatent(Long id);
    void removePatent(JSONObject jsonObject);
    void updatePatent(JSONObject jsonObject);
    int countPatent(long unitId);
    int countSearchPatent(JSONObject jsonObject);

    List<Long> paperIdsByScholatname(JSONObject jsonObject);
    List<Long> projectIdsByScholatname(JSONObject jsonObject);
    List<Long> patentIdsByScholatname(JSONObject jsonObject);
    List<Long> publicationIdsByScholatname(JSONObject jsonObject);

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


    void addAllStatistic(JSONObject jsonObject);

    void deleteAllStatistic(int unitId);

    JSONObject getTotal(Long unitId);
}
