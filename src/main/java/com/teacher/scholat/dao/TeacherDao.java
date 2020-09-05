package com.teacher.scholat.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TeacherDao {
    // 获取教师信息
    JSONObject getTeacherInfo(Long id);

    List<JSONObject> getInfoByName(JSONObject jsonObject);

    // 通过域名获取教师信息
    JSONObject getTeacherInfoByDomainName(String tDomainName);

    // 通过学者名获取教师信息
    JSONObject getTeacherInfoByScholatName(String scholatName);

    // 获取最新更新教师列表
    List<JSONObject> getUpdatedTeacherList(Long unitId);

    // 获取热门教师列表
    List<JSONObject> getHotTeacherList(Long unitId);

    //模糊搜索教师
    List<JSONObject> searchTeacher(JSONObject jsonObject);

    //获取所有学院教师(分页)
    List<JSONObject> listTeacher(JSONObject jsonObject);

    //获取首字母学院教师（分页）
    List<JSONObject> letterTeacher(JSONObject jsonObject);

    //获取所有学院教师(不分页)
    List<JSONObject> listTeacherAll(JSONObject jsonObject);

    //获取所有学院教师(不分页，没有学者网关联账号)
    List<JSONObject> listTeacherAllNoScholat(JSONObject jsonObject);

    //获取所有教师(不分学院)
    List<JSONObject> listTeacherAllUnit();

    //通过域名获取学院所有教师
    List<JSONObject> listTeacherByUnitDomain(JSONObject jsonObject);

    //注：通用搜索
    List<JSONObject> getTeacherByKey(JSONObject jsonObject);
    //注：学校范围内的搜索
    List<JSONObject> getTeacherByKeyInSchoolDomain(JSONObject jsonObject);
    // 统计返回的教师数量
    Integer countKeyTeacher(JSONObject jsonObject);
    Integer countKeyTeacherInSchoolDomain(JSONObject jsonObject);


    //获取学院ID
    Integer getUnitIdBytId(JSONObject object);
    // 获取学者网用户名
    String getScholatUername(Long  id);
    // 获取学者网账号信息
    JSONObject getScholatInfo(String username);
    // 获取学者网账号ID
    Long getScholatID(String username);

    // 获取学者画像中的好友列表 -------------------------------------
    List<JSONObject> getLocalScholatFriends(Long scholatID);
    List<JSONObject> getLocalScholatFans(Long scholatID);
    List<JSONObject> getLocalScholatWatchs(Long scholatID);

    List<JSONObject> getLocalScholatPaper(Long scholatID);

    JSONObject getScholatPersona_keywords(String username);

    JSONObject getLocalInfo(@Param("username")String username,@Param("user_id")String user_id);

    JSONObject getLocalKeywords(String user_id);

    JSONObject getPersonaRecommendUsers(String username);

    JSONObject getScholatInfoByID(int i);

    String getScholatUernameByScholatID(long l);

    JSONObject getScholatInfoSimple(String scholat_username);

    JSONObject getLocalRecommendUsers(String local_id);

    JSONObject getUnionRecommendUsers(String local_id);

    JSONObject getLocalScholatFriends_trust1(@Param("scholatID")Long scholatID, @Param("scholat_id")String scholat_id);

    JSONObject getLocalScholatFriends_trust2(@Param("scholatID")Long scholatID, @Param("scholat_id")String scholat_id);

    JSONObject getLocalScholatFans_trust(@Param("scholatID")Long scholatID, @Param("scholat_id")String scholat_id);

    JSONObject getLocalScholatWatchs_trust(@Param("scholatID")Long scholatID, @Param("scholat_id")String scholat_id);

    List<JSONObject> listTeacherByletter(JSONObject jsonObject);

    JSONObject getFirstLetterFromTeacher(JSONObject jsonObject);

    List<JSONObject> listScholatTeacherRecommend(JSONObject jsonObject);

    JSONObject getLocalInfoByID(String s);

    List<JSONObject> getRecommendTeacher(JSONObject jsonObject);

    List<JSONObject> getRecentUpdateTeacher(JSONObject jsonObject);

    Integer countTeacher(JSONObject jsonObject);


    //  -------------------------------------
}
