package com.teacher.scholat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.common.UserToken;
import com.teacher.scholat.dao.LoginDao;
import com.teacher.scholat.service.LoginService;
import com.teacher.scholat.service.PermissionService;
import com.teacher.scholat.util.CommonUtil;
import com.teacher.scholat.util.MD5Util;
import com.teacher.scholat.util.constants.Constants;
import com.teacher.scholat.util.constants.ErrorEnum;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author:
 * @description: 登录service实现类
 * @date:
 */
@Service
public class LoginServiceImpl implements LoginService {

	@Resource
	private LoginDao loginDao;
	@Autowired
	private PermissionService permissionService;

	/**
	 * 登录表单提交
	 */
	@Override
	public JSONObject authLogin(JSONObject jsonObject) {
		String username = jsonObject.getString("username");
		String password = jsonObject.getString("password");
		String host = jsonObject.getString("host");
		System.out.println("------------------ LoginImpl:开始准备登录验证 ----------------------");
		System.out.println("前台传过来的host（登录角色）值为："+host);
		// 进行密码加密处理
		password = MD5Util.inputCompareWithDb(password);
		System.out.println("学院用户加密后的密码为: "+password);

		JSONObject info = new JSONObject();
		Subject currentUser = SecurityUtils.getSubject();

//		UsernamePasswordToken token = new UsernamePasswordToken(username, password,host);
		UserToken token = new UserToken(username, password,"User"); // 说明传入的是学院用户
		try {
			currentUser.login(token);
			info.put("result", "success");
		} catch (AuthenticationException e) {
			info.put("result", "fail");
		}
		System.out.println("看看我发现了什么哈哈哈哈："+SecurityUtils.getSubject().getSession().getAttribute(Constants.SESSION_USER_INFO));
		return CommonUtil.successJson(info);
	}

	/**
	 * 根据用户名和密码查询对应的用户
	 */
	@Override
	public JSONObject getUser(String username, String password,String host) {
		return loginDao.getUser(username, password,host);
	}

	/**
	 * 查询当前登录用户的权限等信息
	 */
	@Override
	public JSONObject getInfo() {
		System.out.println("------------- LoginImpl：验证密码成功 ---------------");
		System.out.println("准备查询权限");
		//从session获取用户信息
		Session session = SecurityUtils.getSubject().getSession();
		JSONObject userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);
		System.out.println("登录的用户信息为："+userInfo);
		if(userInfo!=null){
			String username = userInfo.getString("username");
			System.out.println("需要查询权限的用户名为： "+username);
			JSONObject info = new JSONObject();
			JSONObject userPermission = permissionService.getUserPermission(username);
			System.out.println("查询到的权限为："+userPermission);
			session.setAttribute(Constants.SESSION_USER_PERMISSION, userPermission);
			info.put("userPermission", userPermission);
			return CommonUtil.successJson(info);
		}else {
			return CommonUtil.errorJson(ErrorEnum.E_20011);
		}
	}

	/**
	 * 退出登录
	 */
	@Override
	public JSONObject logout() {
		try {
			/*Subject currentUser = SecurityUtils.getSubject();
			currentUser.logout();*/
			// 清除session中的信息
			Session session = SecurityUtils.getSubject().getSession();
			session.setAttribute(Constants.SESSION_USER_INFO, "userInfo");
			session.setAttribute(Constants.SESSION_USER_PERMISSION, "userPermission");
		} catch (Exception e) {
		}
		return CommonUtil.successJson();
	}
}
