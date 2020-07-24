package com.teacher.scholat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.dao.SchoolDao;
import com.teacher.scholat.dao.UnitDao;
import com.teacher.scholat.service.SchoolService;
import com.teacher.scholat.util.CommonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SchoolServiceImpl implements SchoolService {

    @Resource
    private SchoolDao schoolDao;

    @Override
    public JSONObject getSchoolInfo(JSONObject object) {
        JSONObject school = schoolDao.getSchoolInfo(object);
        System.out.println("获取到的school------"+school);
        return CommonUtil.successJson(school);
    }
}
