package com.teacher.scholat.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;


public interface TeacherService {
    // 询询教师信息
    JSONObject getTeacherInfo(Long id);

    // 通过域名查询教师信息
    JSONObject getTeacherInfoByDomainName(String tDomainName);

    // 通过域名查询教师信息
    JSONObject getTeacherInfoByScholatName(String scholatName);

    // 获取最新更新教师List
    List<JSONObject> getUpdatedTeacherList(Long unitId);

    // 获取热门教师List
    List<JSONObject> getHotTeacherList(Long unitId);


    //获取学院教师(分页)
    JSONObject listTeacher(JSONObject jsonObject);

    //获取学院教师(不分页)
    JSONObject listTeacherAll(JSONObject jsonObject);

    //获取首字母学院教师（分页）
    JSONObject letterTeacher(JSONObject jsonObject);

    //搜索教师
    JSONObject searchTeacher(JSONObject jsonObject);
    JSONObject searchTeacherBySchoolDomain(JSONObject jsonObject);

    JSONObject getUnitIdBytId(JSONObject jsonObject);
    // 根据id查找学者网用户账号
    String getScholatUername(Long  id);

    JSONObject getScholatInfo(String username);

    // ------------------- [学者画像]   获取本地数据库学者关系 ----------------
    List<JSONObject> getLocalScholatFriends(String username,Long scholatID); // 获取学者画像互相关注用户信息
    List<JSONObject> getLocalScholatFans(String username,Long scholatID); // 获取学者画像粉丝用户信息
    List<JSONObject> getLocalScholatWatchs(String username,Long scholatID); // 获取学者画像关注用户
    Long getScholatID(String username); // 获取学者网ID
    List<JSONObject> getLocalScholatPaper(String username, Long scholatID); // 获取学者网论文信息

    JSONObject getScholatPersona_keywords(String username); // 获取学者网信息关键词云

    JSONObject getLocalInfo(String username,String user_id); // 获取本地教师信息

    JSONObject getLocalKeywords(String username, String user_id); // 获取本地教师关键词云

    JSONObject getPersonaRecommendUsers(String username); // 获取学者网学者推荐用户

    JSONObject getScholatInfoByID(int i);

    String getScholatUernameByScholatID(long l);

    JSONObject getScholatInfoSimple(String scholat_username);

    JSONObject getLocalRecommendUsers(String local_id); // 获取本地教师推荐用户

    JSONObject getUnionRecommendUsers(String local_id); // 获取结合学者网和本地教师的推荐用户

    JSONObject listTeacherRecommend(JSONObject jsonObject);

    JSONObject listTeacherByUnitDomain(JSONObject jsonObject);

    JSONObject getRecommendTeacher(JSONObject request2Json);

    JSONObject getRecentUpdateTeacher(JSONObject request2Json);


}
