package com.teacher.scholat.service;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.model.Apply;

public interface RegisterService {
    int addApply(Apply applay);
    JSONObject getApplyInfo(JSONObject jsonObject);
    int judgeUnitExist(JSONObject jsonObject);
    int judgeUserNameExist(JSONObject jsonObject);
    int judgeDomainNameExist(JSONObject jsonObject);

    JSONObject getSchoolDomain(JSONObject requestJson);

    int judgeSchoolDomainNameExist(JSONObject requestJson);

    int judgeSchoolUserNameExist(JSONObject requestJson);

    int addApplySchool(Apply apply);
}
