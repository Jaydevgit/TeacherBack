package com.teacher.scholat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.common.UserToken;
import com.teacher.scholat.dao.ScholatDao;
import com.teacher.scholat.model.Apply;
import com.teacher.scholat.service.PermissionService;
import com.teacher.scholat.service.ScholatService;
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
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScholatServiceImpl implements ScholatService {
    @Resource
    private ScholatDao scholatDao;
    @Autowired
    private PermissionService permissionService;

    @Override
    public JSONObject authLogin(JSONObject jsonObject) {
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        String host = jsonObject.getString("host");
        System.out.println("前台传过来的host（登录角色）值为：" + host);
        // 进行密码加密处理
        password = MD5Util.inputCompareWithDb(password);
        System.out.println("学者网用户加密后的密码为: " + password);

        JSONObject info = new JSONObject();
        Subject currentUser = SecurityUtils.getSubject();
//        UsernamePasswordToken tokenScholat = new UsernamePasswordToken(username, password,host);
        UserToken token = new UserToken(username, password, "Scholat");
        try {
            currentUser.login(token);
            info.put("result", "success");
        } catch (AuthenticationException e) {
            info.put("result", "fail");
        }
        return CommonUtil.successJson(info);
    }

    /**
     * 查询当前登录用户基本信息和权限等信息
     */
    @Override
    public JSONObject getInfo() {
        System.out.println("--------------------- 进入到scholatmpl获取用户信息 -----------------");
        //从session获取用户信息
        Session session = SecurityUtils.getSubject().getSession();
        JSONObject userInfo = (JSONObject) session.getAttribute(Constants.SESSION_SCHOLAT_INFO);
        System.out.println("登录的用户信息为：" + userInfo);
        if (userInfo != null) {
            String username = userInfo.getString("username");
            System.out.println("需要查询的用户名为： " + username);
            JSONObject info = new JSONObject();
            JSONObject userPermission = permissionService.getScholatPermission(username);
            System.out.println("查询到的权限为：" + userPermission);
            session.setAttribute(Constants.SESSION_SCHOLAT_PERMISSION, userPermission);
            info.put("userPermission", userPermission);
            System.out.println("=======================================================");
            return CommonUtil.successJson(info);
        } else {
            return CommonUtil.errorJson(ErrorEnum.E_20011);
        }

    }

    /**
     * 管理列表获取，具体看state参数是什么
     */
    @Override
    public JSONObject listApply(JSONObject jsonObject) {
        System.out.println("准备获取列表，前端传过来的列表要求为: ");
        CommonUtil.fillPageParam(jsonObject);
        long state = jsonObject.getLongValue("state");
        List<JSONObject> list = new ArrayList<>();
        int count = 0;
        if (state == 1 || state == 2 || state == -1) {
            // 申请的三种状态：待处理，待修改，黑名单
            System.out.println("state为" + state + "，表示申请的三种获取之一");
            count = scholatDao.countApply(state);
            System.out.println("........有" + count + "个申请学院");
            list = scholatDao.listApply(jsonObject);
            System.out.println("后台查询到的申请数据为: " + list);
        } else if (state == 3) {
            // 获取全部学院
            System.out.println("state为3，表示准备查询所有学院，且不包括黑名单（state!=-1）");
            count = scholatDao.countUnit(state);
            System.out.println("........全部共有" + count + "个学院");
            list = scholatDao.listUnitAll(jsonObject);
            System.out.println("后台查询到的所有学院数据为: " + list);
        } else if (state == -2) {
            // 获取黑名单学院
            System.out.println("state为-2，表示准备查询所有黑名单");
            count = scholatDao.countUnitBlack(state);
            System.out.println("........全部共有" + count + "个学院黑名单用户");
            list = scholatDao.listUnitBlack(jsonObject);
            System.out.println("后台查询到的所有学院黑名单数据为: " + list);
        } else {
            // 其他
            System.out.println("没有找到state,所以不知道你到底要找什么?");
        }
        return CommonUtil.successPage(jsonObject, list, count);
    }

    @Override
    public JSONObject listApplySchool(JSONObject jsonObject) {
        System.out.println("准备获取列表，前端传过来的列表要求为: ");
        CommonUtil.fillPageParam(jsonObject);
        long state = jsonObject.getLongValue("state");
        List<JSONObject> list = new ArrayList<>();
        int count = 0;
        if (state == 1 || state == 2 || state == -1) {
            // 申请的三种状态：待处理，待修改，黑名单
            System.out.println("state为" + state + "，表示申请的三种获取之一");
            count = scholatDao.countApplySchool(state);
            System.out.println("........有" + count + "个申请学院");
            list = scholatDao.listApplySchool(jsonObject);
            System.out.println("后台查询到的申请数据为: " + list);
        } else if (state == 3) {
            // 获取全部学院
            System.out.println("state为3，表示准备查询所有学院，且不包括黑名单（state!=-1）");
            count = scholatDao.countUnit(state);
            System.out.println("........全部共有" + count + "个学院");
            list = scholatDao.listUnitAll(jsonObject);
            System.out.println("后台查询到的所有学院数据为: " + list);
        } else if (state == -2) {
            // 获取黑名单学院
            System.out.println("state为-2，表示准备查询所有黑名单");
            count = scholatDao.countUnitBlack(state);
            System.out.println("........全部共有" + count + "个学院黑名单用户");
            list = scholatDao.listUnitBlack(jsonObject);
            System.out.println("后台查询到的所有学院黑名单数据为: " + list);
        } else {
            // 其他
            System.out.println("没有找到state,所以不知道你到底要找什么?");
        }
        return CommonUtil.successPage(jsonObject, list, count);
    }

    /**
     * 模糊搜索学院列表
     */
    @Override
    public JSONObject search(JSONObject jsonObject) {
        System.out.println("准备获取列表，前端传过来的列表要求为: ");
        CommonUtil.fillPageParam(jsonObject);
        long state = jsonObject.getLongValue("state");
        List<JSONObject> list = new ArrayList<>();
        int count = 0;
        count = scholatDao.countUnitNoBlack(jsonObject);
        System.out.println("........全部共有" + count + "个学院非黑名单用户");
        list = scholatDao.listUnitNoBlack(jsonObject);
        System.out.println("后台查询到的所有非黑名单数据为: " + list);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    @Override
    public JSONObject changePassword(JSONObject requestJson) {
        System.out.println("改密:updateUser........"+requestJson);
        scholatDao.updateUser(requestJson);
        System.out.println("改密:现在准备更新信息到登录表");
        scholatDao.updateUserToLogin(requestJson);
        return CommonUtil.successJson();
    }

    @Override
    public int deleteUnit(Long id) {
        System.out.println("删除该学院表信息");
        scholatDao.deleteSchool(id);
        System.out.println("删除该学院所有教师");
        scholatDao.deleteTeacher(id);
        System.out.println("删除该学院权限系统角色");
        scholatDao.deleteRole(id);
        List<JSONObject> jsonObjects = scholatDao.selectIds(id);
        System.out.println("查询到unit的删除ids"+jsonObjects);
        for(int i=0;i<jsonObjects.size();i++){
            int tId= jsonObjects.get(i).getInteger("id");
            System.out.println("tId="+tId);
            scholatDao.deleteUnitIds(tId);
            scholatDao.deleteLoginIds(tId);
        }
        System.out.println("查询所有栏目");
        List<JSONObject> jsonObjects2 = scholatDao.selectCatalogueIds(id);
        System.out.println("删除所有栏目");
        scholatDao.deleteCatalogueIds(id);
        System.out.println("删除该学院所有栏目");
        for(int i=0;i<jsonObjects2.size();i++) {
            int cId = jsonObjects2.get(i).getInteger("id");
            System.out.println("cId=" + cId);

            System.out.println("删除所有栏目关联教师表信息");
            scholatDao.deleteCatalogueTeaacherIds(cId);
        }

        return 0;
    }


    /**
     * 退出登录
     */
    @Override
    public JSONObject logout() {
        try {
            /*Subject currentUser = SecurityUtils.getSubject();
            currentUser.logout();*/
            // 重置session
            Session session = SecurityUtils.getSubject().getSession();
            session.setAttribute(Constants.SESSION_SCHOLAT_INFO, "scholatInfo");
            session.setAttribute(Constants.SESSION_SCHOLAT_PERMISSION, "scholatPermission");
        } catch (Exception e) {
        }
        return CommonUtil.successJson();
    }

    /*
     * 允许通过申请
     * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addApplyToSchoolUnit(Apply applay) {
        System.out.println("准备去加入申请信息到--> school_unit表中");
        int id = scholatDao.addApplyToSchoolUnit(applay);
        return id;
    }

    @Override
    public int addApplyToSchool(Apply applay) {
        System.out.println("准备去加入申请信息到--> school表中");
        int id = scholatDao.addApplyToSchool(applay);
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addApplyToUnitProfile(Apply applay) {
        System.out.println("准备去加入申请信息到--> 学院信息表中");
        int id = scholatDao.addApplyToUnitProfile(applay);
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addApplyToSchoolProfile(Apply apply) {
        System.out.println("准备去加入申请信息到--> 学院信息表中");
        int id = scholatDao.addApplyToSchoolProfile(apply);
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addApplyToLogin(Apply applay) {
        System.out.println("准备去加入申请信息到--> 登录表中");
        int id = scholatDao.addApplyToLogin(applay);
        return id;
    }

    @Override
    public int addApplySchoolToLogin(Apply applay) {
        System.out.println("准备去加入申请信息到--> 登录表中");
        int id = scholatDao.addApplySchoolToLogin(applay);
        return id;
    }

    @Override
    public int applyValidate(JSONObject jsonObject) {

        int applyValidateUnitApply = scholatDao.applyValidateUnitApply(jsonObject);
        int applyValidateUnitProfile = scholatDao.applyValidateUnitProfile(jsonObject);
        System.out.println("验证找到的username数量为：" + applyValidateUnitApply + applyValidateUnitProfile);
        int toltalNum = 0;
        toltalNum = applyValidateUnitApply + applyValidateUnitProfile;
        return toltalNum;
    }

    @Override
    public JSONObject getApplyInfo(JSONObject jsonObject) {
        return scholatDao.getApplyInfo(jsonObject);
    }
    @Override
    public JSONObject getApplySchoolInfo(JSONObject jsonObject) {
        return scholatDao.getApplySchoolInfo(jsonObject);
    }

    @Override
    public int updateApplySuccess(Apply apply) {
        return scholatDao.updateApplySuccess(apply);
    }@Override
    public int updateApplySchoolSuccess(Apply apply) {
        return scholatDao.updateApplySchoolSuccess(apply);
    }
    @Override
    public int updateApplyModify(Apply apply) {
        return scholatDao.updateApplyModify(apply);
    }

    @Override
    public int updateApplySchoolModify(Apply apply) {
        return scholatDao.updateApplySchoolModify(apply);
    }

    @Override
    public int updateApplyBlack(Apply apply) {
        return scholatDao.updateApplyBlack(apply);
    }
    @Override
    public int updateApplySchoolBlack(Apply apply) {
        return scholatDao.updateApplySchoolBlack(apply);
    }
    @Override
    public int updateAllBlack(JSONObject jsonObject) {
        return scholatDao.updateAllBlack(jsonObject);
    }
    @Override
    public int updateCancelBlackApply(Apply apply) {
        return scholatDao.updateCancelBlackApply(apply);
    }
    public int updateCancelBlackApplySchool(Apply apply) {
        return scholatDao.updateCancelBlackApplySchool(apply);
    }

}
