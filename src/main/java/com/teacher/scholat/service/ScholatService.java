package com.teacher.scholat.service;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.model.Apply;

public interface ScholatService {
    /**
     * 学院申请列表
     */
    JSONObject listApply(JSONObject jsonObject);
    /**
     * 学校申请列表
     */
    JSONObject listApplySchool(JSONObject jsonObject);
    /**
     * 登录表单提交
     */
    JSONObject authLogin(JSONObject jsonObject);

    /**
     * 查询当前登录用户基本信息和权限等信息
     */
    JSONObject getInfo();

    /**
     * 退出登录
     */
    JSONObject logout();
    // 获取申请的信息
    JSONObject getApplyInfo(JSONObject jsonObject);
    JSONObject getApplySchoolInfo(JSONObject requestJson);

    int updateApplySuccess(Apply apply);
    int updateApplySchoolSuccess(Apply apply);
    int updateApplyModify(Apply apply);
    int updateApplySchoolModify(Apply apply);
    int updateApplyBlack(Apply apply);
    int updateApplySchoolBlack(Apply apply);
    int updateCancelBlackApply(Apply apply);
    int updateCancelBlackApplySchool(Apply apply);

    // 通过申请请求
    int addApplyToSchoolUnit(Apply applay);
    int addApplyToSchool(Apply applay);
    int addApplyToUnitProfile(Apply applay);
    int addApplyToSchoolProfile(Apply apply);
    int addApplyToLogin(Apply applay);
    int addApplySchoolToLogin(Apply applay);

    int applyValidate(JSONObject jsonObject);

    int updateAllBlack(JSONObject jsonObject);

    JSONObject search(JSONObject requestJson);

    JSONObject changePassword(JSONObject requestJson);

    int deleteUnit(Long id);


}
