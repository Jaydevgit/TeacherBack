package com.teacher.scholat.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author:
 * @description: 登录Service
 * @date:
 */
public interface LoginService {
	/**
	 * 登录表单提交
	 */
	JSONObject authLogin(JSONObject jsonObject);
	JSONObject authSchoolLogin(JSONObject requestJson);

	/**
	 * 根据用户名和密码查询对应的用户
	 *
	 * @param username 用户名
	 * @param password 密码
	 */
	JSONObject getUser(String username, String password,String host);

	/**
	 * 查询当前登录用户的权限等信息
	 */
	JSONObject getInfo();
	JSONObject getSchoolInfo();

	/**
	 * 退出登录
	 */
	JSONObject logout();
	JSONObject schoolLogout();
}
