package com.teacher.scholat.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.Set;

/**
 * @author:
 * @date:
 */
public interface PermissionDao {
	/**
	 * 查询用户的角色 菜单 权限
	 */
	JSONObject getUserPermission(String username);

	/**
	 * 查询学者网用户的基本信息 菜单 权限
	 */
	JSONObject getScholatPermission(String username);

	/**
	 * 查询所有的菜单
	 */
	Set<String> getAllMenu();

	/**
	 * 查询所有的权限
	 */
	Set<String> getAllPermission();
}
