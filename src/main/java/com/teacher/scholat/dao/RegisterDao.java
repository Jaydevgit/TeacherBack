package com.teacher.scholat.dao;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.model.Apply;

public interface RegisterDao {
    int addApply(Apply apply);
    JSONObject getApplyInfo(JSONObject jsonObject);
    int judgeUnitExist(JSONObject jsonObject);
    int judgeSchoolUnitExist(JSONObject jsonObject);

    int judgeUserNameExist(JSONObject jsonObject);
    int judgeDomainNameExist(JSONObject jsonObject);
}
