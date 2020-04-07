package com.teacher.scholat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.dao.PermissionDao;
import com.teacher.scholat.service.PermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author:
 * @date:
 */
@Service
public class PermissionServiceImpl implements PermissionService {

	@Resource
	private PermissionDao permissionDao;

	/**
	 * 查询某用户的 角色  菜单列表   权限列表
	 */
	@Override
	public JSONObject getUserPermission(String username) {
		JSONObject userPermission = getUserPermissionFromDB(username);
		return userPermission;
	}


	/*
	* 查询学者网管理员的信息，菜单列表， 权限列表
	* */
	@Override
	public JSONObject getScholatPermission(String username) {
		System.out.println("permisionImpl准备去数据库查询 "+username+" 的权限");
		// 获取到的登录用户的用户信息
		JSONObject userPermission = permissionDao.getScholatPermission(username);
		System.out.println("该学者网管理员的权限为："+userPermission);
		return userPermission;
	}

	/**
	 * 从数据库查询用户权限信息
	 */
	private JSONObject getUserPermissionFromDB(String username) {
		System.out.println("准备去数据库查询 "+username+" 的权限");
		// 获取到的登录用户的用户信息
		JSONObject userPermission = permissionDao.getUserPermission(username);
		/*//管理员角色ID为1
		int adminRoleId = 1;
		//如果是管理员
		String roleIdKey = "roleId";
		if (adminRoleId == userPermission.getIntValue(roleIdKey)) {
			//查询所有菜单  所有权限
			Set<String> menuList = permissionDao.getAllMenu();
			Set<String> permissionList = permissionDao.getAllPermission();
			userPermission.put("menuList", menuList);
			userPermission.put("permissionList", permissionList);
		}*/
		System.out.println("找到的学院id为："+userPermission.get("unitId"));
		System.out.println("该学院用户的权限为："+userPermission);
		return userPermission;
	}
}
