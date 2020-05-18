package com.teacher.scholat.dao;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public interface CatalogueDao {


    List<JSONObject> getTeacherByCatalogue(JSONObject object);
    List<JSONObject> findCatalogueByUnit(JSONObject jsonObject);
    void addCatalogue(JSONObject jsonObject);
    void deleteCatalogue(JSONObject jsonObject);
    void addSubCatalogue(JSONObject jsonObject);
    void removeTeacher(JSONObject jsonObject);
    void topTeacher(JSONObject jsonObject);
    void topCatalogue(JSONObject jsonObject);
    void addSingleTeacher(JSONObject object);
    void addCatalogueTeacher(JSONObject jsonObject);
    void sortUnitTeacher(JSONObject jsonObject);
    void updateCatalogueTeacher(JSONObject jsonObject);
    List<JSONObject> getDepartSubjectByUnit(JSONObject object);
    Integer countTeacher(JSONObject object);
    Integer countLetterTeacher(JSONObject object);
    List<JSONObject> getTeacherByCatalogueAndPage(JSONObject object);
    Integer judgeIsParentById(JSONObject object);
    List<Integer> getSubIdByCatalogue(Integer parentId);
    List<JSONObject> getByCatalogueAndLetterAndPage(JSONObject object);

    List<JSONObject> getTeacherByCatalogueId(JSONObject object);

    List<JSONObject> getTeacherAllCatalogues(JSONObject object);


    List<JSONObject> getCatalogueNameByCatalogueId(JSONObject object);
}
