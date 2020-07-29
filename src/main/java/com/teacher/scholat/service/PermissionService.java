package com.teacher.scholat.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author:
 * @date:
 */
public interface PermissionService {
	/**
	 * 查询某学院用户的 角色  菜单列表   权限列表
	 */
	JSONObject getUserPermission(String username);

	/**
	 * 查询某学校用户的 角色  菜单列表   权限列表
	 */
	JSONObject getSchoolUserPermission(String username);

	/**
	 * 查询学者网管理员的 角色  菜单列表   权限列表
	 */
	JSONObject getScholatPermission(String username);
}
