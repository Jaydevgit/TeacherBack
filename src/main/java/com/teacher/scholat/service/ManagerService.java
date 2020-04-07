package com.teacher.scholat.service;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.model.Teacher;

import java.util.List;

/**
 * @author:
 * @date:
 */
public interface ManagerService {
	/**
	 * 新增教师
	 */
	public int addTeacher(Teacher teacher);
	/**
	 * 新增教师
	 */
	public int deleteTeacher(JSONObject jsonObject);

	public List<JSONObject> selectAll();

	/*
	* 查询邮箱是否已经存在
	* */
	int judgeEmailExist(JSONObject jsonObject);

	/**
	 * 教师列表
	 */
	JSONObject listTeacher(JSONObject jsonObject);

	/**
	 * 未在岗教师列表
	 */
	JSONObject listTeacherNoState(JSONObject jsonObject);

	/**
	 * 更新教师
	 */
	JSONObject updateTeacher(JSONObject jsonObject);


	/**
	 * 通过名字获取教师
	 * @param object
	 * @return
	 */
	JSONObject getTeacherByKey(JSONObject object);

	//模糊搜索教师
	JSONObject searchTeacher(JSONObject jsonObject);

	JSONObject searchScholatList(JSONObject jsonObject);
}
