package com.teacher.scholat.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @Author: Jay
 * @Description:
 * @Date: create in 2020/7/23 15:37
 */
public interface SchoolDao {
    //统计学校教师总数
    int countTeacher(JSONObject jsonObject);
    /**
     * 教师列表
     */
    List<JSONObject> listTeacherLocal(JSONObject jsonObject);
    //获取学院基本信息
    JSONObject getSchoolInfo(JSONObject object);
}
