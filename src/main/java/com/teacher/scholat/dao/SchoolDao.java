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
    int countUnitTeacher(JSONObject jsonObject);
    /**
     * 学校教师列表
     */
    List<JSONObject> listTeacherLocal(JSONObject jsonObject);
    List<JSONObject> listAllTeacher(JSONObject jsonObject);
    /**
     * 学院教师列表
     */
    List<JSONObject> listTeacherByUnit(JSONObject jsonObject);
    //获取学院基本信息
    JSONObject getSchoolInfo(JSONObject object);
    /**
     * 统计搜索结果
     * @param object
     * @return
     */
    int countKeyTeacher(JSONObject object);
    /**
     * 模糊搜索教师
     */
    List<JSONObject> searchTeacher(JSONObject jsonObject);

    void updateSchoolInfo(JSONObject object);
    /**
     * 学院列表
     */
    List<JSONObject> getUnitList(JSONObject jsonObject);
    /**
     * 学校管理员删除教师
     */
    int deleteTeacher(JSONObject jsonObject);


}
