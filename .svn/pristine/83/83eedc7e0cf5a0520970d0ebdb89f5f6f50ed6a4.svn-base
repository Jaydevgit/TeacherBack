package com.teacher.scholat.service;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.model.Apply;

public interface ScholatService {
    /**
     * 申请列表
     */
    JSONObject listApply(JSONObject jsonObject);

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

    int updateApplySuccess(Apply apply);
    int updateApplyModify(Apply apply);
    int updateApplyBlack(Apply apply);
    int updateCancelBlackApply(Apply apply);

    // 通过申请请求
    int addApplyToSchoolUnit(Apply applay);
    int addApplyToUnitProfile(Apply applay);
    int addApplyToLogin(Apply applay);

    int applyValidate(JSONObject jsonObject);

}
