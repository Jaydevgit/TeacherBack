package com.teacher.scholat.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author:
 * @description: 用户/角色/权限
 * @date: 2
 */
public interface UserDao {
	/**
	 * 查询用户数量
	 */
	int countUser(Long unitId);

	/**
	 * 查询用户列表
	 */
	List<JSONObject> listUser(JSONObject jsonObject);

	/**
	 * 查询所有的角色
	 * 在添加/修改用户的时候要使用此方法
	 */
	List<JSONObject> getAllRoles();

	/**
	 * 查询学院所有的角色
	 * 在添加/修改用户的时候要使用此方法
	 */

	List<JSONObject> getRolesByUnitId(long unitId);

	/**
	 * 校验用户名是否已存在
	 */
	int queryExistUsername(JSONObject jsonObject);

	/**
	 * 新增用户
	 */
	int addUser(JSONObject jsonObject);	/**
	 * 新增用户到登录表
	 */
	int addUserToLogin(JSONObject jsonObject);

	/**
	 * 修改用户
	 */
	int updateUser(JSONObject jsonObject);
	/*
	* 修改用户的登录表
	* */
	int updateUserToLogin(JSONObject jsonObject);


	/**
	 * 角色列表
	 */
	List<JSONObject> listRole(JSONObject jsonObject);

	List<JSONObject> findUsersByRoleId(Long roleId);

	/**
	 * 查询所有权限, 给角色分配权限时调用
	 */
	List<JSONObject> listAllPermission();

	/**
	 * 新增角色
	 */
	int insertRole(JSONObject jsonObject);
	/*
	* 添加角色到登录表中
	* */



	/**
	 * 批量插入角色的权限
	 *
	 * @param roleId      角色ID
	 * @param permissions 权限
	 */
	int insertRolePermission(@Param("roleId") String roleId, @Param("permissions") List<Integer> permissions);

	/**
	 * 将角色曾经拥有而修改为不再拥有的权限 delete_status改为'2'
	 */
	int removeOldPermission(@Param("roleId") String roleId, @Param("permissions") List<Integer> permissions);

	/**
	 * 修改角色名称
	 */
	int updateRoleName(JSONObject jsonObject);

	/**
	 * 查询某角色的全部数据
	 * 在删除和修改角色时调用
	 */
	JSONObject getRoleAllInfo(JSONObject jsonObject);

	/**
	 * 删除角色
	 */
	int removeRole(JSONObject jsonObject);

	/**
	 * 删除本角色全部权限
	 */
	int removeRoleAllPermission(JSONObject jsonObject);

	int updateRole(JSONObject jsonObject);
}
