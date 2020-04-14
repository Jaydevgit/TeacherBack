package com.teacher.scholat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.dao.UnitDao;
import com.teacher.scholat.service.UnitService;
import com.teacher.scholat.util.CommonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UnitServiceImpl implements UnitService {

    @Resource
    private UnitDao unitDao;

    /**
     *
     * @param： unitId
     * @return
     */


    @Override
    public JSONObject getUnitInfo(JSONObject object)
    {   JSONObject unit = unitDao.getUnitInfo(object);
        System.out.println("获取到的unit------"+unit);
        return CommonUtil.successJson(unit);
    }

    @Override
    public JSONObject getUnitInfo2(JSONObject object)
    {   JSONObject unit = unitDao.getUnitInfo2(object);
        System.out.println("获取到的unit------"+unit);
        return CommonUtil.successJson(unit);
    }

    @Override
    public JSONObject updateUnitInfo(JSONObject object) {
        unitDao.updateUnitInfo(object);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject getUnitBytId(JSONObject object) {
        System.out.println("查查object" + object);
        JSONObject unitBytId = unitDao.getUnitBytId(object);
        return CommonUtil.successJson(unitBytId);
    }

    @Override
    public List<JSONObject> getUnitInfoByUnitId(JSONObject object) {
        System.out.println("查查UnitId---object" + object);
        List<JSONObject> unitBytIdObject = unitDao.getUnitInfoByUnitId(object);
        System.out.println("查查UnitId---object" + unitBytIdObject);
        return unitBytIdObject;
    }

}
