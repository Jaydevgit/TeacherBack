package com.teacher.scholat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.dao.RegisterDao;
import com.teacher.scholat.model.Apply;
import com.teacher.scholat.service.RegisterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Resource
    private RegisterDao registerDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addApply(Apply applay) {
        System.out.println("准备去加入申请信息到--> 申请表中");
        int id = registerDao.addApply(applay);
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
}
