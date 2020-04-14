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

	List<JSONObject> getUnitInfoByUnitId(JSONObject jsonObject);
}
