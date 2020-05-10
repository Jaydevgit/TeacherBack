package com.teacher.scholat.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author:
 * @description: UnitService
 * @date:
 */
public interface CatalogueService {
	/**
	 * 获取栏目信息
	 */
	JSONObject getCatalogues(JSONObject object);

	/**
	 * 获取栏目教师
	 */
	JSONObject getTeacherByCatalogue(JSONObject object);

	/**
	 * 获取栏目与教师关联id
	 */
	JSONObject getTeacherByCatalogueId(JSONObject object);

	/**
	 * 获取该教师所有栏目
	 */
	JSONObject getTeacherAllCatalogues(JSONObject object);

	JSONObject getTeacherByCatalogueAndPage(JSONObject object);

	/**
	 * 获取开头首字母的栏目教师
	 */
	JSONObject getByCatalogueAndLetterAndPage(JSONObject object);

	/**
	 * 添加主栏目信息
	 */
	JSONObject addCatalogue(JSONObject object);

	/**
	 * 删除主栏目信息
	 */
	JSONObject deleteCatalogue(JSONObject object);

	/**
	 * 更新主栏目信息
	 */
	JSONObject updateCatalogue(JSONObject object);

	/**
	 * 添加子栏目信息
	 */
	JSONObject addSubCatalogue(JSONObject object);

	/**
	 * 移除栏目中的教师
	 */
	JSONObject removeTeacher(JSONObject Object);

	/**
	 * 置顶栏目中的教师
	 */
	JSONObject topTeacher(JSONObject Object);

	/**
	 * 置顶主栏目
	 */
	JSONObject topCatalogue(JSONObject Object);

	/**
	 * 排序栏目
	 * @param object
	 * @return
	 */
	JSONObject sortCatalogue(JSONObject object);


	/**
	 * 排序教师
	 */
	JSONObject sortTeacher(JSONObject object);

	/**
	 * 单个添加栏目教师
	 * @param object
	 * @return
	 */
	JSONObject addSingleTeacher(JSONObject object);
	/**
	 * 添加栏目教师
	 * @param object
	 * @return
	 */
	JSONObject addCatalogueTeacher(JSONObject object);

	/**
	 * 更改栏目教师
	 * @param object
	 * @return
	 */
	JSONObject updateCatalogueTeacher(JSONObject object);

	/**
	 * 获取部门/学科
	 * @param object
	 * @return
	 */
	JSONObject getDepartSubjectByUnit(JSONObject object);

	/**
	 * 排序学院的教师
	 */
	JSONObject sortUnitTeacher(JSONObject object);


}
