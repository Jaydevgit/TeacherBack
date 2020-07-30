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
    JSONObject getSchoolInfo(JSONObject jsonObject);

    /**
     * 模糊搜索教师
     */
    JSONObject searchTeacher(JSONObject request2Json);

    JSONObject updateSchoolInfo(JSONObject requestJson);
}
