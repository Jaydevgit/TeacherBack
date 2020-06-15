package com.teacher.scholat.dao;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.model.Teacher;

import java.util.List;

/**
 * @author:
 * @description: 管理教师成员Dao层
 * @date:
 */
public interface ManagerDao {
    /**
     * 新增教师
     */
    int addTeacher(Teacher teacher);
	int addImportTeacher(JSONObject jsonObject);
    int deleteTeacher(JSONObject jsonObject);
    /*
     * 查询邮箱是否已经存在
     * */
	JSONObject judgeEmailExist(JSONObject jsonObject);

    /*
     * 查询若是学者网用户就插入到学者网name处
     * */
    void insertScholatName(List<JSONObject> list);


	//统计教师总数
    int countTeacher(JSONObject jsonObject);

	//统计教师更新了学者网信息总数
	int countUpdateTeacher(JSONObject jsonObject);

	int countLetterUnitTeacher(JSONObject jsonObject);
    /**
     * 教师列表
     */
    List<JSONObject> listTeacher(JSONObject jsonObject);
    List<JSONObject> listTeacherLocal(JSONObject jsonObject);
	List<JSONObject> listTeacherLocalAll(JSONObject jsonObject);
//	/**
//	 * 除去在岗教师列表
//	 */
//	List<JSONObject> listTeacherNoLocal(JSONObject jsonObject);

    /**
     * 教师文章
     */
    int updateTeacher(JSONObject jsonObject);

	/**
	 * 栏目方面的搜索
	 * @param jsonObject
	 * @return
	 */
	List<JSONObject> getTeacherByKey(JSONObject jsonObject);

	//模糊搜索教师
	List<JSONObject> searchTeacher(JSONObject jsonObject);

	/**
	 * 学者列表
	 */
	List<JSONObject> searchScholat(JSONObject jsonObject);
	/**
	 * 统计学者总数
	 */
	int countScholat(JSONObject object);

	/**
	 * 统计搜索结果
	 * @param object
	 * @return
	 */
	int countKeyTeacher(JSONObject object);

	List<JSONObject> selectAllTeacher();


	//域名是否存在
	List<JSONObject> judgeDomainExist(JSONObject jsonObject);
	JSONObject judgeDomainExist2(JSONObject jsonObject);

    void unBindScholat(String id);
    void bindScholat(String id,String scholat_username);
}
