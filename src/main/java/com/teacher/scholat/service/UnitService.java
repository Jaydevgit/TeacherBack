package com.teacher.scholat.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author:
 * @description: UnitService
 * @date:
 */
public interface UnitService {
	/**
	 * 获取学院信息
	 */

	JSONObject getUnitInfo(JSONObject object);

	JSONObject getUnitInfo2(JSONObject object);

	JSONObject updateUnitInfo(JSONObject object);

	JSONObject getUnitBytId(JSONObject object);

	JSONObject getUnitBytDomain_name(JSONObject object);

	List<JSONObject> getUnitInfoByUnitId(JSONObject jsonObject);

    JSONObject updateUnitTagstate(JSONObject object);

	List<JSONObject> getUnitBySchoolDomain(String schoolDomain);

	List<JSONObject> getAllUnit();
}
