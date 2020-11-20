package com.teacher.scholat.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author: sansen
 * @description: 用户/角色/权限
 * @date: 2017/11/2 10:18
 */
public interface UserService {
	/**
	 * 用户列表
	 */
	JSONObject listUser(JSONObject jsonObject);
	JSONObject listSchoolUser(JSONObject request2Json);
	JSONObject listAllUnitUser(JSONObject request2Json);

	/**
	 * 查询所有的角色
	 * 在添加/修改用户的时候要使用此方法
	 */
	JSONObject getAllRoles();

	/**
	 *
	 * @return
	 */
	JSONObject getRolesByUnitId(long unitId);
	JSONObject getRolesBySchoolId(long schoolId);

	/**
	 * 添加用户
	 */
	JSONObject addUser(JSONObject jsonObject);
	JSONObject addSchoolUser(JSONObject requestJson);

	/**
	 * 修改用户,包括删除
	 */
	JSONObject updateUser(JSONObject jsonObject);
    JSONObject updateSchoolUser(JSONObject requestJson);

	/**
	 * 本用户,自己修改密码
	 */
	JSONObject updateSelfPass(JSONObject jsonObject);
	JSONObject updateSchoolSelfPass(JSONObject requestJson);

	/**
	 * 角色列表
	 */
	JSONObject listRole(JSONObject jsonObject);

	/**
	 * 查询所有权限, 给角色分配权限时调用
	 */
	JSONObject listAllPermission();

	/**
	 * 添加角色
	 */
	JSONObject addRole(JSONObject jsonObject);


	/**
	 * 修改角色
	 */
	JSONObject updateRole(JSONObject jsonObject);

	/**
	 * 删除角色
	 */
	JSONObject deleteRole(JSONObject jsonObject);

	JSONObject getInfo(JSONObject jsonObject);
	JSONObject getSchoolUserInfo(JSONObject requestJson);
	JSONObject updateInfo(JSONObject jsonObject);
	JSONObject updateSchoolUserInfo(JSONObject requestJson);



}
