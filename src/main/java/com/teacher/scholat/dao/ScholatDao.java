package com.teacher.scholat.dao;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.model.Apply;

import java.util.List;

public interface ScholatDao {
    /**
     * 统计申请总数
     */
    int countApply(long state);
    /**
     * 统计待修改数量
     */
    int countApplyProcessed(long state);
    /**
     * 统计学院总数
     */
    int countUnit(long state);
    /**
     * 统计学院黑名单
     */
    int countUnitBlack(long state);
    /**
     * 统计学院非黑名单
     */
    int countUnitNoBlack(JSONObject jsonObject);
    /**
     * 申请列表
     */
    List<JSONObject> listApply(JSONObject jsonObject);
    /**
     * 申请列表
     */
    List<JSONObject> listApplyProcessed(JSONObject jsonObject);
    /**
     * 全部学院
     */
    List<JSONObject> listUnitAll(JSONObject jsonObject);
    /**
     * 全部学院黑名单
     */
    List<JSONObject> listUnitBlack(JSONObject jsonObject);
    // 查看申请的学院信息
    List<JSONObject> listUnitNoBlack(JSONObject jsonObject);
    // 查看申请的学院信息
    JSONObject getApplyInfo(JSONObject jsonObject);

    int updateApplySuccess(Apply apply);
    int updateApplyModify(Apply apply);
    int updateApplyBlack(Apply apply);
    int updateCancelBlackApply(Apply apply);

    // 允许通过申请
    int addApplyToSchoolUnit(Apply apply);
    int addApplyToUnitProfile(Apply apply);
    int addApplyToLogin(Apply apply);

    int applyValidateUnitApply(JSONObject jsonObject);
    int applyValidateUnitProfile(JSONObject jsonObject);

    int updateAllBlack(JSONObject jsonObject);

    int updateUser(JSONObject requestJson);

    int updateUserToLogin(JSONObject requestJson);

    List<JSONObject> selectIds(Long id);

    int deleteIds(int tId);
}
