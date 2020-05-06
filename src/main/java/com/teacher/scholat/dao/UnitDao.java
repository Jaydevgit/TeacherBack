package com.teacher.scholat.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author:
 * @description: schoolUnit表相关dao
 * @date:
 */
public interface UnitDao {

	//获取学院基本信息
	JSONObject getUnitInfo(JSONObject object);

	//获取学院基本信息
	JSONObject getUnitInfo2(JSONObject object);

	void updateUnitInfo(JSONObject object);

	JSONObject getUnitBytId(JSONObject object);

	//通过教师域名获取学院基本信息（学校名称等）
	JSONObject getUnitBytDomain_name(JSONObject object);

	List<JSONObject> getUnitInfoByUnitId(JSONObject object);


}
