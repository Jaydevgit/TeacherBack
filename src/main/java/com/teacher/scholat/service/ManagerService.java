package com.teacher.scholat.service;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.model.Teacher;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.tags.Param;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author:
 * @date:
 */
public interface ManagerService {
	/**
	 * 新增教师
	 */
	public int addTeacher(Teacher teacher);
	public int addImportTeacher(JSONObject jsonObject);

	/**
	 * 新增教师
	 */
	public int deleteTeacher(JSONObject jsonObject);

	public List<JSONObject> selectAll();

	/*
	* 查询邮箱是否已经存在
	* */
	JSONObject  judgeEmailExist(JSONObject jsonObject);

	/*
	 * 查询教师域名是否已经存在
	 * */
	List<JSONObject> judgeDomainExist(JSONObject jsonObject);
	//生成相同域名个数
	JSONObject judgeDomainExist2(JSONObject jsonObject);
	/**
	 * 教师列表
	 */
	JSONObject listTeacher(JSONObject jsonObject);
	/**
	 * 统计有多少用户的学者网简介更新了信息
	 */
	JSONObject listTeacherUpdateScholat(JSONObject jsonObject);

//	/**
//	 * 未在岗教师列表
//	 */
//	JSONObject listTeacherNoState(JSONObject jsonObject);

	/**
	 * 更新教师
	 */
	JSONObject updateTeacher(JSONObject jsonObject);


	/**
	 * 通过名字获取教师
	 * @param object
	 * @return
	 */
	JSONObject getTeacherByKey(JSONObject object);

	//模糊搜索教师
	JSONObject searchTeacher(JSONObject jsonObject);

	JSONObject searchScholatList(JSONObject jsonObject);

    void unBindScholat(String id);
    void bindScholat(String id,String scholat_username);

    void exportTeacher(HttpServletRequest request,HttpServletResponse response) throws IOException;
	void exportTeacher2(HttpServletRequest request,HttpServletResponse response) throws IOException;

	void importTeacher(MultipartFile file, ManagerService managerService,int unitId,String editName);
}
