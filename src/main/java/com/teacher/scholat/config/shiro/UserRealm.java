package com.teacher.scholat.config.shiro;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.service.LoginService;
import com.teacher.scholat.util.constants.Constants;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 * @author:
 * @description: 自定义Realm
 * @date:
 */
public class UserRealm extends AuthorizingRealm {
    private Logger logger = LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    private LoginService loginService;

    @Override
    @SuppressWarnings("unchecked")
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("*****************进入unit权限查询*****************");
        System.out.println("查询的用户名为：" + principals);
        Session session = SecurityUtils.getSubject().getSession();
        //查询用户的权限
        // 获取角色
        //System.out.println((JSONObject) session.getAttribute(Constants.SESSION_USER_PERMISSION));
        //System.out.println((JSONObject) session.getAttribute(Constants.SESSION_SCHOLAT_PERMISSION));
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        JSONObject permission = new JSONObject();
        if (session.getAttribute(Constants.SESSION_USER_INFO) == null
        ||session.getAttribute(Constants.SESSION_USER_INFO).toString().equals("userInfo")) {
            System.out.println("用户表信息为空，说明没有用户登录");
            permission = null;
        } else {
            permission = (JSONObject) session.getAttribute(Constants.SESSION_USER_PERMISSION);
        }
        if (permission != null) {
            logger.info("permission的值为:" + permission);
            logger.info("本用户权限为:" + permission.get("permissionList"));
            //为当前用户设置角色和权限
            authorizationInfo.addStringPermissions((Collection<String>) permission.get("permissionList"));
            System.out.println("权限查询返回的最后结果为：" + authorizationInfo.getStringPermissions());
        }
        System.out.println("**********************************");
        return authorizationInfo;
    }

    /**
     * 验证当前登录的Subject
     * LoginController.login()方法中执行Subject.login()时 执行此方法
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        System.out.println("进到了userRealm获取登录验证");
        String loginName = (String) authcToken.getPrincipal();
        // 获取用户密码
        String password = new String((char[]) authcToken.getCredentials());
        // 角色是学院用户
        String host = "unit";
        // 去找数据库中的数据是否有该账户
        JSONObject user = loginService.getUser(loginName, password, host);
        if (user == null) {
            //没找到帐号
            System.out.println("对不起，没有找到 " + loginName + " 账号");
            throw new UnknownAccountException();
        }
        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getString("username"),
                user.getString("password"),
                //ByteSource.Util.bytes("salt"), salt=username+salt,采用明文访问时，不需要此句
                getName()
        );
        System.out.println("登录后找到的数据库账号内容：" + user);
        //session中不需要保存密码
        user.remove("password");
        //将用户信息放入session中
        SecurityUtils.getSubject().getSession().setAttribute(Constants.SESSION_USER_INFO, user);
        System.out.println("Shiro登录验证返回结果为：" + authenticationInfo);
        return authenticationInfo;
    }
}
