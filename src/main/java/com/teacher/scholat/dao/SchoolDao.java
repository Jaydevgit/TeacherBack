package com.teacher.scholat.dao;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author: Jay
 * @Description:
 * @Date: create in 2020/7/23 15:37
 */
public interface SchoolDao {
    //获取学院基本信息
    JSONObject getSchoolInfo(JSONObject object);
}
