package com.teacher.scholat.controller;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.service.LoginService;
import com.teacher.scholat.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:
 * @description: 登录相关Controller
 * @date:
 */
@RestController
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private LoginService loginService;

	/**
	 * 学院用户登录, 设置token
	 */
	@PostMapping("/auth")
	public JSONObject authLogin(@RequestBody JSONObject requestJson) {
		CommonUtil.hasAllRequired(requestJson, "username,password");
		return loginService.authLogin(requestJson);
	}
	/**
	 * 学校用户登录, 设置token
	 */
	@PostMapping("/schoolAuth")
	public JSONObject authSchoolLogin(@RequestBody JSONObject requestJson) {
		CommonUtil.hasAllRequired(requestJson, "username,password");
		return loginService.authSchoolLogin(requestJson);
	}

	/**
	 * 查询当前登录学院用户的信息
	 */
	@PostMapping("/getInfo")
	public JSONObject getInfo() {
		return loginService.getInfo();
	}
	/**
	 * 查询当前登录学校用户的信息
	 */
	@PostMapping("/getSchoolInfo")
	public JSONObject getSchoolInfo() {
		return loginService.getSchoolInfo();
	}

	/**
	 * 学院用户登出
	 */
	@PostMapping("/logout")
	public JSONObject logout() {
		return loginService.logout();
	}

	/**
	 * 学校用户登出
	 */
	@PostMapping("/schoolLogout")
	public JSONObject schoolLogout() {
		return loginService.schoolLogout();
	}
}
