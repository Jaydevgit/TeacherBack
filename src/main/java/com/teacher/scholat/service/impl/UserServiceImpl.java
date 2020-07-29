package com.teacher.scholat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.dao.UserDao;
import com.teacher.scholat.service.UserService;
import com.teacher.scholat.util.CommonUtil;
import com.teacher.scholat.util.constants.ErrorEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author:
 * @description: 用户/角色/权限
 * @date:
 */
@Service
public class UserServiceImpl implements UserService {
	@Resource
	private UserDao userDao;

	/**
	 * 用户列表
	 */
	@Override
	public JSONObject listUser(JSONObject jsonObject) {
		CommonUtil.fillPageParam(jsonObject);
		long unitId = jsonObject.getLongValue("unitId");
		System.out.println("unitId="+unitId);
		int count = userDao.countUser(unitId);
		System.out.println("........有"+count+"位子账号");
		List<JSONObject> list = userDao.listUser(jsonObject);
		System.out.println(list);
		return CommonUtil.successPage(jsonObject, list, count);
	}

	/**
	 * 添加用户
	 */
	@Override
	public JSONObject addUser(JSONObject jsonObject) {
		int exist = userDao.queryExistUsername(jsonObject);
		if (exist > 0) {
			return CommonUtil.errorJson(ErrorEnum.E_10009);
		}
		userDao.addUser(jsonObject); // 添加用户到用户表
		int userId = Integer.parseInt(jsonObject.getString("userId"));
		System.out.println("返回的userId为："+userId);
		jsonObject.put("userId",userId);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
		jsonObject.put("updateTime",df.format(new Date()));
		System.out.println("刚刚加入了加入到用户表的userId，看看有没有加入到jsonObject");
		System.out.println("jsonObject："+jsonObject);
		userDao.addUserToLogin(jsonObject); // 添加用户到登录表
		return CommonUtil.successJson();
	}

	/**
	 * 查询所有的角色
	 * 在添加/修改用户的时候要使用此方法
	 */
	@Override
	public JSONObject getAllRoles() {
		List<JSONObject> roles = userDao.getAllRoles();
		return CommonUtil.successPage(roles);
	}

	@Override
	public JSONObject getRolesByUnitId(long unitId) {

		List<JSONObject> roles = userDao.getRolesByUnitId(unitId);
		return CommonUtil.successPage(roles);
	}

	/**
	 * 修改用户,包括删除
	 */
	@Override
	public JSONObject updateUser(JSONObject jsonObject) {
		System.out.println("updateUser........"+jsonObject);
		userDao.updateUser(jsonObject);
		System.out.println("现在准备更新用户信息到登录表");
		userDao.updateUserToLogin(jsonObject);
		return CommonUtil.successJson();
	}

	/**
	 * 本用户,自己修改密码
	 */
	@Override
	public JSONObject updateSelfPass(JSONObject jsonObject) {
		System.out.println("updateUser........"+jsonObject);
		userDao.updateSelfUser(jsonObject);
		System.out.println("现在准备更新用户信息到登录表");
		userDao.updateSelfUserToLogin(jsonObject);
		return CommonUtil.successJson();
	}
	@Override
	public JSONObject updateSchoolSelfPass(JSONObject jsonObject) {
		System.out.println("updateSchoolUser........"+jsonObject);
		userDao.updateSchoolSelfUser(jsonObject);
		System.out.println("现在准备更新用户信息到登录表");
		userDao.updateSchoolSelfUserToLogin(jsonObject);
		return CommonUtil.successJson();
	}

	/**
	 * 角色列表
	 */
	@Override
	public JSONObject listRole(JSONObject jsonObject) {

		System.out.println("前端传过来的角色列表要求为: ");
		CommonUtil.fillPageParam(jsonObject);
		long unitId = jsonObject.getLongValue("unitId");
		int count = userDao.countUser(unitId);
		System.out.println("........有"+count+"个角色");
		List<JSONObject> list = userDao.listRole(jsonObject);
		System.out.println("后台查询到的角色数据为: "+list);
		return CommonUtil.successPage(jsonObject, list, count);
	}

	/**
	 * 查询所有权限, 给角色分配权限时调用
	 */
	@Override
	public JSONObject listAllPermission() {
		List<JSONObject> permissions = userDao.listAllPermission();
		return CommonUtil.successPage(permissions);
	}

	/**
	 * 添加角色
	 */
	@Transactional(rollbackFor = Exception.class)
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject addRole(JSONObject jsonObject) {


		System.out.println("role信息---------"+jsonObject);
		jsonObject = CommonUtil.dealPermissions(jsonObject);
		System.out.println("role信息處理完畢--------"+jsonObject);

		//添加時間
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		jsonObject.put("updateTime", ts);
		jsonObject.put("createTime", ts);

		userDao.insertRole(jsonObject);


		return CommonUtil.successJson();
	}


	/**
	 * 修改角色
	 */
	@Transactional(rollbackFor = Exception.class)
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject updateRole(JSONObject jsonObject) {
		String roleId = jsonObject.getString("roleId");
		System.out.println(jsonObject);

		System.out.println("role信息---------"+jsonObject);
		jsonObject = CommonUtil.dealPermissions(jsonObject);
		System.out.println("role信息處理完畢--------"+jsonObject);

		userDao.updateRole(jsonObject);

		/*List<Integer> newPerms = (List<Integer>) jsonObject.get("permissions");
		JSONObject roleInfo = userDao.getRoleAllInfo(jsonObject);
		Set<Integer> oldPerms = (Set<Integer>) roleInfo.get("permissionIds");
		//修改角色名称
		dealRoleName(jsonObject, roleInfo);
		//添加新权限
		saveNewPermission(roleId, newPerms, oldPerms);
		//移除旧的不再拥有的权限
		removeOldPermission(roleId, newPerms, oldPerms);*/
		return CommonUtil.successJson();
	}

	/**
	 * 修改角色名称
	 */
	private void dealRoleName(JSONObject paramJson, JSONObject roleInfo) {
		String roleName = paramJson.getString("roleName");
		if (!roleName.equals(roleInfo.getString("roleName"))) {
			userDao.updateRoleName(paramJson);
		}
	}

	/**
	 * 为角色添加新权限
	 */
	private void saveNewPermission(String roleId, Collection<Integer> newPerms, Collection<Integer> oldPerms) {
		List<Integer> waitInsert = new ArrayList<>();
		for (Integer newPerm : newPerms) {
			if (!oldPerms.contains(newPerm)) {
				waitInsert.add(newPerm);
			}
		}
		if (waitInsert.size() > 0) {
			userDao.insertRolePermission(roleId, waitInsert);
		}
	}

	/**
	 * 删除角色 旧的 不再拥有的权限
	 */
	private void removeOldPermission(String roleId, Collection<Integer> newPerms, Collection<Integer> oldPerms) {
		List<Integer> waitRemove = new ArrayList<>();
		for (Integer oldPerm : oldPerms) {
			if (!newPerms.contains(oldPerm)) {
				waitRemove.add(oldPerm);
			}
		}
		if (waitRemove.size() > 0) {
			userDao.removeOldPermission(roleId, waitRemove);
		}
	}

	/**
	 * 删除角色
	 */
	@Transactional(rollbackFor = Exception.class)
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject deleteRole(JSONObject jsonObject) {

		long roleId = jsonObject.getLongValue("roleId");
		List<JSONObject> users = userDao.findUsersByRoleId(roleId);
		if (users != null && users.size() > 0)
		{
			return CommonUtil.errorJson(ErrorEnum.E_10008);
		}

		userDao.removeRole(jsonObject);
		/*JSONObject roleInfo = userDao.getRoleAllInfo(jsonObject);
		List<JSONObject> users = (List<JSONObject>) roleInfo.get("users");
		if (users != null && users.size() > 0) {
			return CommonUtil.errorJson(ErrorEnum.E_10008);
		}
		userDao.removeRole(jsonObject);
		userDao.removeRoleAllPermission(jsonObject);*/
		return CommonUtil.successJson();
	}

	@Override
	public JSONObject getInfo(JSONObject jsonObject) {
		System.out.println("查询用户基本信息");

		return CommonUtil.successJson(userDao.getInfo(jsonObject));
	}
	@Override
	public JSONObject getSchoolUserInfo(JSONObject jsonObject) {
		System.out.println("查询学校用户基本信息");

		return CommonUtil.successJson(userDao.getSchoolUserInfo(jsonObject));
	}
	@Override
	public JSONObject updateInfo(JSONObject jsonObject) {
		System.out.println("更新用户基本信息");
		userDao.updateInfo(jsonObject);
		return CommonUtil.successJson();
	}
	@Override
	public JSONObject updateSchoolUserInfo(JSONObject jsonObject) {
		System.out.println("更新学校用户基本信息");
		userDao.updateSchoolUserInfo(jsonObject);
		return CommonUtil.successJson();
	}
}
