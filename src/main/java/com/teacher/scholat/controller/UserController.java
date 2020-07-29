package com.teacher.scholat.controller;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.service.UserService;
import com.teacher.scholat.util.CommonUtil;
import com.teacher.scholat.util.MD5Util;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author:
 * @description: 用户/角色/权限相关controller
 * @date:
 */
@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	/**
	 * 查询用户列表
	 */
	@RequiresPermissions("user:list")
	@GetMapping("/list")
	public JSONObject listUser(HttpServletRequest request) {
		return userService.listUser(CommonUtil.request2Json(request));
	}


	@RequiresPermissions("user:add")
	@PostMapping("/addUser")
	public JSONObject addUser(@RequestBody JSONObject requestJson) {
		CommonUtil.hasAllRequired(requestJson, "username,nickname,password, roleId, unitId");
		System.out.println("加密前"+requestJson.get("password").toString());
		String newPassword = MD5Util.toDb(requestJson.get("password").toString());
		requestJson.put("password",newPassword);
		System.out.println("加密后："+requestJson.get("password").toString());
		return userService.addUser(requestJson);
	}

	@RequiresPermissions("user:update")
	@PostMapping("/updateUser")
	public JSONObject updateUser(@RequestBody JSONObject requestJson) {
		System.out.println("requestJson....."+requestJson);
		CommonUtil.hasAllRequired(requestJson, " roleId, state, userId");
		System.out.println("加密前"+requestJson.get("password").toString());
		String newPassword = MD5Util.toDb(requestJson.get("password").toString());
		requestJson.put("password",newPassword);
		System.out.println("加密后："+requestJson.get("password").toString());
		return userService.updateUser(requestJson);
	}
	@PostMapping("/updateSelfPass")
	public JSONObject updateSelfPass(@RequestBody JSONObject requestJson) {
		System.out.println("加密前"+requestJson.get("password").toString());
		String newPassword = MD5Util.toDb(requestJson.get("password").toString());
		requestJson.put("password",newPassword);
		System.out.println("加密后："+requestJson.get("password").toString());
		return userService.updateSelfPass(requestJson);
	}
	@PostMapping("/updateSchoolSelfPass")
	public JSONObject updateSchoolSelfPass(@RequestBody JSONObject requestJson) {
		System.out.println("加密前"+requestJson.get("password").toString());
		String newPassword = MD5Util.toDb(requestJson.get("password").toString());
		requestJson.put("password",newPassword);
		System.out.println("加密后："+requestJson.get("password").toString());
		return userService.updateSchoolSelfPass(requestJson);
	}
	@PostMapping("/getInfo")
	public JSONObject getInfo(@RequestBody JSONObject requestJson) {
		return userService.getInfo(requestJson);
	}
	@PostMapping("/getSchoolUserInfo")
	public JSONObject getSchoolUserInfo(@RequestBody JSONObject requestJson) {
		return userService.getSchoolUserInfo(requestJson);
	}
	@PostMapping("/updateInfo")
	public JSONObject updateInfo(@RequestBody JSONObject requestJson) {
		return userService.updateInfo(requestJson);
	}
	@PostMapping("/updateSchoolUserInfo")
	public JSONObject updateSchoolUserInfo(@RequestBody JSONObject requestJson) {
		return userService.updateSchoolUserInfo(requestJson);
	}

	@RequiresPermissions(value = {"user:add", "user:update"}, logical = Logical.OR)
	@GetMapping("/getAllRoles")
	public JSONObject getAllRoles() {
		return userService.getAllRoles();
	}

	@RequiresPermissions(value = {"user:add", "user:update"}, logical = Logical.OR)
	@PostMapping("/getRolesByUnitId")
	public JSONObject getRolesByUnitId(@RequestBody JSONObject requestJson) {

		CommonUtil.hasAllRequired(requestJson, "unitId");
		long unitId = requestJson.getLongValue("unitId");
		System.out.println(unitId);
		return userService.getRolesByUnitId(unitId);
	}

	/**
	 * 角色列表
	 */
	@RequiresPermissions("role:list")
	@GetMapping("/listRole")
	public JSONObject listRole(HttpServletRequest request) {
		System.out.println("......开始查询角色列表");
		System.out.println(CommonUtil.request2Json(request));
		return userService.listRole(CommonUtil.request2Json(request));
	}

	/**
	 * 查询所有权限, 给角色分配权限时调用
	 */
	@RequiresPermissions("role:list")
	@GetMapping("/listAllPermission")
	public JSONObject listAllPermission() {

		return userService.listAllPermission();
	}

	/**
	 * 新增角色
	 */
	@RequiresPermissions("role:add")
	@PostMapping("/addRole")
	public JSONObject addRole(@RequestBody JSONObject requestJson) {
		CommonUtil.hasAllRequired(requestJson, "roleName,permissions,unitId");
		return userService.addRole(requestJson);
	}

	/**
	 * 修改角色
	 */
	@RequiresPermissions("role:update")
	@PostMapping("/updateRole")
	public JSONObject updateRole(@RequestBody JSONObject requestJson) {
		CommonUtil.hasAllRequired(requestJson, "roleId,roleName,permissions");
		return userService.updateRole(requestJson);
	}

	/**
	 * 删除角色
	 */
	@RequiresPermissions("role:delete")
	@PostMapping("/deleteRole")
	public JSONObject deleteRole(@RequestBody JSONObject requestJson) {
		CommonUtil.hasAllRequired(requestJson, "roleId");
		return userService.deleteRole(requestJson);
	}
}
