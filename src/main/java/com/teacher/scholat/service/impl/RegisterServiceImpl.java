package com.teacher.scholat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.dao.RegisterDao;
import com.teacher.scholat.dao.SchoolDomain;
import com.teacher.scholat.model.Apply;
import com.teacher.scholat.service.RegisterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Resource
    private RegisterDao registerDao;
    @Resource
    private SchoolDomain schoolDomain;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addApply(Apply applay) {
        System.out.println("准备去加入申请信息到--> 申请表中");
        int id = registerDao.addApply(applay);
        return id;
    }
    @Override
    public int addApplySchool(Apply apply) {
        System.out.println("准备去加入申请信息到--> 申请表中");
        int id = registerDao.addApplySchool(apply);
        return id;
    }
    @Override
    public JSONObject getApplyInfo(JSONObject jsonObject) {
        return registerDao.getApplyInfo(jsonObject);
    }


    @Override
    public int judgeUnitExist(JSONObject jsonObject) {
        int exist = registerDao.judgeUnitExist(jsonObject);
        int school_unit_exist = registerDao.judgeSchoolUnitExist(jsonObject);
        if(exist == 0&& school_unit_exist==0){
            // 表示没有找到相同的学院，那就可以添加
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public int judgeUserNameExist(JSONObject jsonObject) {
        int exist = registerDao.judgeUserNameExist(jsonObject);
        if(exist == 0){
            // 表示没有找到相同的用户名，那就可以添加
            return 0;
        }else{
            return 1;
        }
    }
    @Override
    public int judgeSchoolUserNameExist(JSONObject jsonObject) {
        int exist = registerDao.judgeSchoolUserNameExist(jsonObject);
        if(exist == 0){
            // 表示没有找到相同的用户名，那就可以添加
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public int judgeDomainNameExist(JSONObject jsonObject) {
      //  int exist = registerDao.judgeUserNameExist(jsonObject);
       // int school_unit_exist = registerDao.judgeSchoolUnitExist(jsonObject);
        int domain_name_exist = registerDao.judgeDomainNameExist(jsonObject);
        if(domain_name_exist == 0){
            // 表示没有找到相同的学院域名，那就可以添加
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public int judgeSchoolDomainNameExist(JSONObject jsonObject) {
        int school_domain_name_exist = registerDao.judgeSchoolDomainNameExist(jsonObject);
        if(school_domain_name_exist == 0){
            // 表示没有找到相同的学校域名，那就可以添加
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public JSONObject getSchoolDomain(JSONObject requestJson) {
        JSONObject jsonObject= schoolDomain.GetSchoolDomain(requestJson);
        return jsonObject;
    }


}
