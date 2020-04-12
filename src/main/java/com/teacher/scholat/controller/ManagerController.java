package com.teacher.scholat.controller;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.model.Teacher;
import com.teacher.scholat.service.ManagerService;
import com.teacher.scholat.util.CommonUtil;
import com.teacher.scholat.util.DelTagsUtil;
import com.teacher.scholat.util.DiffProcessUtils;
import com.teacher.scholat.util.PinyinUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author:
 * @description: 管理教师相关Controller
 * @date:
 */
@RestController
@RequestMapping("/manager")
public class ManagerController {

	@Autowired
	private ManagerService managerService;



	/**
	 * 查询教师列表
	 */
	@RequiresPermissions("teacher:list")
	@GetMapping("/listTeacher")
	public JSONObject listTeacher(HttpServletRequest request) {
        System.out.println("......开始查询教师成员列表");
		System.out.println(CommonUtil.request2Json(request));
		return managerService.listTeacher(CommonUtil.request2Json(request));
	}

	/**
	 * 查询教师域名是否存在
	 */
	@RequestMapping("/judgeDomainExist")
	public int judgeDomainExist(@RequestBody JSONObject requestJson) {
		System.out.println("......开始查询教师域名是否存在");
		System.out.println(requestJson);
		return managerService.judgeDomainExist(requestJson);
	}

//	@GetMapping("/listTeacherNoState")
//	public JSONObject listTeacherNoState(HttpServletRequest request) {
//		System.out.println("......开始查询未在岗教师成员列表");
//		System.out.println(CommonUtil.request2Json(request));
//		return managerService.listTeacherNoState(CommonUtil.request2Json(request));
//	}

	@GetMapping("/getTeacherByKey")
	public JSONObject getTeacherByName(HttpServletRequest request) {

		return managerService.getTeacherByKey(CommonUtil.request2Json(request));
	}

	@GetMapping("/searchTeacher")
	public JSONObject searchTeacher(HttpServletRequest request) {

		return managerService.searchTeacher(CommonUtil.request2Json(request));
	}

	/**
	 * 新增教师
	 */
	@RequiresPermissions("teacher:add")
	@PostMapping("/addTeacher")
	public JSONObject addTeacher(@RequestBody JSONObject requestJson) {
        System.out.println("........新增中");
        System.out.println("前台传过来的新增数据为: "+requestJson);
        CommonUtil.hasAllRequired(requestJson, "avatar,username,email,post,create_time,state");
		String username = (String)requestJson.get("username");
		String pinyin = PinyinUtil.getPinyinString(username);
		System.out.println("----------pinyin-------------"+pinyin);
		requestJson.put("pinyin",pinyin);
		System.out.println("修改后的的新增数据为: "+requestJson);
		System.out.println("........验证完毕, 有必填字段");

        Teacher teacher = JSONObject.toJavaObject(requestJson,Teacher.class);
        System.out.println("********"+teacher.getScholat_update_time());

        //managerService.addTeacher(teacher);
        managerService.addTeacher(teacher);

		/*List<JSONObject> jsonObject = managerService.selectAll();

		System.out.println("++++++++所有教师++++++++" + jsonObject);
		for (int i = 0;i < jsonObject.size();i++){
			JSONObject jsonObject1 = jsonObject.get(i);
			String username1 = (String) jsonObject1.get("username");
			String py = PinyinUtil.getPinyinString(username1);
			jsonObject1.put("pinyin",py);
			System.out.println("--------单个教师-------"+jsonObject1);
			managerService.updateTeacher(jsonObject1);
		}*/

		System.out.println("___________________________"+teacher.getId());
        requestJson.put("teacher_id",teacher.getId());



        return CommonUtil.successJson();
	}

	/**
	 * 修改教师信息
	 */
	@RequiresPermissions("teacher:update")
	@PostMapping("/updateTeacher")
	public JSONObject updateTeacher(@RequestBody JSONObject requestJson) {
		CommonUtil.hasAllRequired(requestJson, "id,username");
		//更新 姓名的拼音
		String username = (String)requestJson.get("username");
		String pinyin = PinyinUtil.getPinyinString(username);
		System.out.println("----------pinyin-------------"+pinyin);
		requestJson.put("pinyin",pinyin);
		System.out.println("修改后的的新增数据为: "+requestJson);

		return managerService.updateTeacher(requestJson);
	}
	/**
	 * 删除教师教师
	 */
	@RequiresPermissions("teacher:delete")
	@PostMapping("/deleteTeacher")
	public JSONObject deleteTeacher(@RequestBody JSONObject requestJson) {
		System.out.println("----------------- 开始请求：删除教师 ------------------");
		managerService.deleteTeacher(requestJson);
		return CommonUtil.successJson();
	}



	// 验证学者网信息
	@RequestMapping("/validate")
	public JSONObject searchScholatUser(@RequestBody JSONObject requestJson){
		System.out.println(".......搜索是否有学者网的账号");
		System.out.println("前台传过来的新增数据为: "+requestJson);
        /*
        前端输入传过来的验证参数主要由: 名字, 邮箱, 单位名称, pageRow
        现在就主要根据, 名字和邮箱来判断
        主要根据邮箱, 邮箱为空就判断名字
        */
		/*
		 * 学者网返回的信息应该有
		 * username                  唯一的用户名    acc_name
		 * name                      中文名字    chinese_name
		 * unit                      工作单位    work_unit
		 * post                      职称        scholar_title
		 * degree                    学历        degree
		 * department                部门  work_department
		 * avatar                    头像  picture_url
		 * email                     邮箱  work_email
		 * update_time               更新时间    curEditDate
		 * intro                     个人简介    introduction
		 * research_direction        研究兴趣    scholar_field
		 * */


		// 判断邮箱是否已经存在
		System.out.println(">>>>>>>现在："+requestJson.getString("email"));
		String email = requestJson.getString("email");
		String username = requestJson.getString("username");
		if(email!=null){
			// 去判断下有没有重复的学者网邮箱用户
			int whetherHasEmail = managerService.judgeEmailExist(requestJson);
			if(whetherHasEmail!=0){
				JSONObject r =managerService.searchScholatList((requestJson));
				r.put("err","该邮箱已存在");
				return r;
			}else{
				return managerService.searchScholatList((requestJson));
			}
		}else if(username!=null){
			// 根据姓名去进行验证
			return managerService.searchScholatList((requestJson));
		}else{
			return managerService.searchScholatList((requestJson));
		}
	}

	@RequestMapping("/compare")
	public JSONObject teacherCompareScholat(@RequestBody JSONObject jsonObject) {
		System.out.println("-------------------------进入到信息比对----------------------------------");
		JSONObject teacherJSON = jsonObject.getJSONObject("teacher");
		JSONObject scholatJSON = jsonObject.getJSONObject("scholat");
		Teacher teacher = JSONObject.toJavaObject(teacherJSON, Teacher.class);
		Teacher scholat = JSONObject.toJavaObject(scholatJSON, Teacher.class);

		System.out.println(scholat.getIntro());
		System.out.println("学者网去标签开始");
		System.out.println(DelTagsUtil.delHtmlTags(scholat.getIntro()));
		System.out.println("教师去标签开始");
		System.out.println(DelTagsUtil.delHtmlTags(teacher.getIntro()));

   /*     scholat.setIntro(DelTagsUtil.delHtmlTags(scholat.getIntro()));
        teacher.setIntro(DelTagsUtil.delHtmlTags(teacher.getIntro()));*/


		List<Teacher> res = DiffProcessUtils.compareVersion(teacher,scholat);


		JSONObject resp = new JSONObject();
		System.out.println(res.get(0));
		System.out.println(res.get(1));
		resp.put("teacher",res.get(0));
		resp.put("scholat",res.get(1));
		System.out.println("---------------對比完畢-----------------"+resp+"???");
		return CommonUtil.successJson(resp);

//        ver1.setIntroduction(res.get(0));
//        ver2.setIntroduction(res.get(1));
	}
}
