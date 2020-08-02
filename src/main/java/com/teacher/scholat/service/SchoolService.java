package com.teacher.scholat.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author:Jay
 * @description: SchoolService
 * @date:
 */
public interface SchoolService {
    /**
     * 学校教师列表
     */
    JSONObject listTeacher(JSONObject request2Json);
    /**
     * 学院教师列表
     */
    JSONObject listTeacherByUnit(JSONObject request2Json);
    JSONObject getSchoolInfo(JSONObject jsonObject);

    /**
     * 模糊搜索教师
     */
    JSONObject searchTeacher(JSONObject request2Json);

    JSONObject updateSchoolInfo(JSONObject requestJson);
    /**
     * 学院列表
     */
    JSONObject getUnitList(JSONObject request2Json);




}
