package com.teacher.scholat.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.dao.ManagerDao;
import com.teacher.scholat.dao.UnitDao;
import com.teacher.scholat.model.Teacher;
import com.teacher.scholat.model.excel.importTeacher;
import com.teacher.scholat.model.excel.listener.teacherExcelListen;
import com.teacher.scholat.model.excel.teacherData;
import com.teacher.scholat.service.ManagerService;
import com.teacher.scholat.util.CommonUtil;
import com.teacher.scholat.util.GetScholatProfile;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.tags.Param;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: sansen
 * @date: 2017/10/24 16:07
 */
@Service
public class ManagerServiceImpl implements ManagerService {

    @Resource
    private ManagerDao managerDao;

    @Resource
    private UnitDao unitDao;

    /**
     * 新增教师
     */
    @CacheEvict(value = "FacultyTeacherAll",key = "'Faculty'+#teacher.getUnit_id()")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addTeacher(Teacher teacher) {
        int id = managerDao.addTeacher(teacher);
        System.out.println("teacher.getUnit_id()="+teacher.getUnit_id());
        return id;
    }

    @CacheEvict(value = "FacultyTeacherAll",key = "'Faculty'+#jsonObject.getLongValue(\"unit_id\")")
    @Override
    public int addImportTeacher(JSONObject jsonObject) {
        int id = managerDao.addImportTeacher(jsonObject);
        System.out.println(id);
        return id;
    }

    @CacheEvict(value = "FacultyTeacherAll",key = "'Faculty'+#jsonObject.getLongValue(\"unitId\")")
    @Override
    public int deleteTeacher(JSONObject jsonObject) {
        return managerDao.deleteTeacher(jsonObject);
    }

    @Override
    public List<JSONObject> selectAll() {

        return managerDao.selectAllTeacher();
    }

    @Override
    public JSONObject judgeEmailExist(JSONObject jsonObject) {

        return   managerDao.judgeEmailExist(jsonObject);
    }

    @Override
    public List<JSONObject> judgeDomainExist(JSONObject jsonObject) {

        return managerDao.judgeDomainExist(jsonObject);
    }
    @Override
    public JSONObject judgeDomainExist2(JSONObject jsonObject) {

        return managerDao.judgeDomainExist2(jsonObject);
    }
    /**
     * 统计有多少用户的学者网简介更新了信息
     */
    @Override
    public JSONObject listTeacherUpdateScholat(JSONObject jsonObject) {
        int countScholat=0;
        List<JSONObject> listLocal = managerDao.listTeacherLocalAll(jsonObject);
        for (JSONObject jsonObject1 : listLocal) {
            String username = jsonObject1.getString("scholat_username");
            String email = jsonObject1.getString("email"); // 通过email搜索
            String updateTime = jsonObject1.getString("update_time");
            JSONArray jsonArray = GetScholatProfile.getScholatProfileByUserName(username);
            JSONObject jsonObject2 = new JSONObject();
            if(jsonArray!= null && jsonArray.size()>0){
                jsonObject2= jsonArray.getJSONObject(0);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                SimpleDateFormat strFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                String str = jsonObject2.getString("introUpdateTime");
                Date date = new Date();
                String time;
                try {
                    date = strFormat.parse(str);
                    time = dateFormat.format(date); //可以把日期转换转指定格式的字符串
                    int compareTo = time.compareTo(updateTime);
                    if(compareTo==1) {
                        countScholat++;
                    }
                } catch (ParseException e) {
                    time = str;
                    // e.printStackTrace();
                }
                jsonObject1.put("scholat_update_time",time);
                //jsonObject1.put("real_scholat_username", jsonObject2.getString("acc_name"));
            }else {
                jsonObject1.put("scholat_update_time", "");
                jsonObject1.put("real_scholat_username", "");
            }
        }
        System.out.println("后台查询到的更新教师数据总为: " + countScholat);
        JSONObject r=new JSONObject();
        r.put("countScholat",countScholat);
        return r;
    }
    /**
     * 教师列表
     *
     * @TODO: 加入一个统计有多少用户的学者网简介更新了信息
     */
    @Override
    public JSONObject listTeacher(JSONObject jsonObject) {
        System.out.println("前端传过来的教师列表要求为: ");
        CommonUtil.fillPageParam(jsonObject);
        //long unitId = jsonObject.getLongValue("unitId");
        int count = managerDao.countTeacher(jsonObject);
        //System.out.println("........有"+count+"位教师");
//        int countScholat = managerDao.countUpdateTeacher(jsonObject);
        // 搜索列表, 对加入进来的信息加入在线搜索的user_profile: introUpdateTime信息
        //List<JSONObject> list = managerDao.listTeacher(jsonObject);
        List<JSONObject> listLocal = managerDao.listTeacherLocal(jsonObject);
        for (JSONObject jsonObject1 : listLocal) {
            String username = jsonObject1.getString("scholat_username");
            String email = jsonObject1.getString("email"); // 通过email搜索
            String updateTime = jsonObject1.getString("update_time");
            JSONArray jsonArray = GetScholatProfile.getScholatProfileByUserName(username);
            JSONObject jsonObject2 = new JSONObject();
            if(jsonArray!= null && jsonArray.size()>0){
                jsonObject2= jsonArray.getJSONObject(0);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                SimpleDateFormat strFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                String str = jsonObject2.getString("introUpdateTime");
                Date date = new Date();
                String time;
                try {
                    date = strFormat.parse(str);
                    time = dateFormat.format(date); //可以把日期转换转指定格式的字符串
                    int compareTo = time.compareTo(updateTime);
                } catch (ParseException e) {
                    time = str;
                    // e.printStackTrace();
                }
                jsonObject1.put("scholat_update_time",time);
                //jsonObject1.put("real_scholat_username", jsonObject2.getString("acc_name"));
            }else {
                jsonObject1.put("scholat_update_time", "");
                jsonObject1.put("real_scholat_username", "");
            }
        }
        // 需要 u.introUpdateTime 和 u.acc_name	当 (t.scholat_username = u.acc_name) or (t.email=u.work_email)
        System.out.println("后台查询到的教师数据为: " + listLocal);

//        System.out.println("看下是JOSN信息：" + stLocal.get(0).getString(("email")));
        //managerDao.whetherScholatUser(jsonObject);
        //managerDao.insertScholatName(listLocal);
        return CommonUtil.successPage(jsonObject, listLocal, count);
    }

//    /**
//     * 未在岗教师列表
//     *
//     * @TODO: 加入一个统计有多少用户的学者网简介更新了信息
//     */
//    @Override
//    public JSONObject listTeacherNoState(JSONObject jsonObject) {
//        System.out.println("前端传过来的教师列表要求为: ");
//        CommonUtil.fillPageParam(jsonObject);
//        long unitId = jsonObject.getLongValue("unitId");
//        int count = managerDao.countTeacher(unitId);
//        //System.out.println("........有"+count+"位教师");
//
//
//        // 搜索列表, 对加入进来的信息加入在线搜索的user_profile: introUpdateTime信息
//        //List<JSONObject> list = managerDao.listTeacher(jsonObject);
//        List<JSONObject> listNoLocal = managerDao.listTeacherNoLocal(jsonObject);
//        for (JSONObject jsonObject1 : listNoLocal) {
//            String username = jsonObject1.getString("scholat_username");
//            String email = jsonObject1.getString("email"); // 通过email搜索
//            JSONArray jsonArray = GetScholatProfile.getScholatProfileByUserName(username);
//            JSONObject jsonObject2 = new JSONObject();
//            if(jsonArray!= null && jsonArray.size()>0){
//                jsonObject2= jsonArray.getJSONObject(0);
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                SimpleDateFormat strFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//                String str = jsonObject2.getString("introUpdateTime");
//                Date date = new Date();
//                String time;
//                try {
//                    date = strFormat.parse(str);
//                    time = dateFormat.format(date); //可以把日期转换转指定格式的字符串
//                } catch (ParseException e) {
//                    time = str;
//                    // e.printStackTrace();
//                }
//                jsonObject1.put("scholat_update_time",time);
//                //jsonObject1.put("real_scholat_username", jsonObject2.getString("acc_name"));
//            }else {
//                jsonObject1.put("scholat_update_time", "");
//                jsonObject1.put("real_scholat_username", "");
//            }
//        }
//        // 需要 u.introUpdateTime 和 u.acc_name	当 (t.scholat_username = u.acc_name) or (t.email=u.work_email)
//        System.out.println("后台查询到的教师数据为: " + listNoLocal);
//
////        System.out.println("看下是JOSN信息：" + listLocal.get(0).getString(("email")));
//        //managerDao.whetherScholatUser(jsonObject);
//        //managerDao.insertScholatName(listLocal);
//        return CommonUtil.successPage(jsonObject, listNoLocal, count);
//    }

    /**
     * 更新教师
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject updateTeacher(JSONObject jsonObject) {
        managerDao.updateTeacher(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject getTeacherByKey(JSONObject object) {
        List<JSONObject> teacherByName = managerDao.getTeacherByKey(object);
        return CommonUtil.successPage(teacherByName);
    }

    @Override
    public JSONObject searchTeacher(JSONObject jsonObject) {
        System.out.println("搜索 ==========> 前端传过来的教师列表要求为: ");
        System.out.println(jsonObject);
        CommonUtil.fillPageParam(jsonObject);
		/*String key = jsonObject.getString("key");

		String result = "";
		String temp = "";
		for (int i=0; i<key.length(); i++) {
			temp = String.valueOf(key.charAt(i));
			if (!temp.equals(" ")) {
				result += "%" + temp;
			}
		}
		result += "%";
		jsonObject.put("key",result);*/

        int count = managerDao.countKeyTeacher(jsonObject);
        System.out.println("........有" + count + "位教师");


        List<JSONObject> list = managerDao.searchTeacher(jsonObject);
        for(JSONObject jsonObject1:list){
            String username = jsonObject1.getString("scholat_username");
            String email = jsonObject1.getString("email");
            JSONArray jsonArray = GetScholatProfile.getScholatProfileByUserName(username);
            JSONObject jsonObject2 = new JSONObject();
            if(jsonArray!=null && jsonArray.size()>0){
                jsonObject2 = jsonArray.getJSONObject(0);
                System.out.println(jsonObject2);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                SimpleDateFormat strFormat = new SimpleDateFormat("yyyyMMddHHmm");
                String str = jsonObject2.getString("introUpdateTime");
                Date date = new Date();
                String time;
                try {
                    date = strFormat.parse(str);
                    time = dateFormat.format(date); //可以把日期转换转指定格式的字符串
                } catch (ParseException e) {
                    time = str;
                    // e.printStackTrace();
                }
                jsonObject1.put("scholat_update_time",time);
                jsonObject1.put("real_scholat_username", jsonObject2.getString("acc_name"));
            }else {
                jsonObject1.put("scholat_update_time", "");
                jsonObject1.put("real_scholat_username", "");
            }
        }
        return CommonUtil.successPage(jsonObject, list, count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject searchScholatList(JSONObject jsonObject) {
        System.out.println("=============准备获取学者网数据==============");
        CommonUtil.fillPageParam(jsonObject);
        int count = 0;
        String chineseName = jsonObject.getString("name");
        String username = jsonObject.getString("username");
        String email = jsonObject.getString("email");
        System.out.println(chineseName + " " + username + " " + email);
        JSONArray jsonArray = new JSONArray();
        if (email != null && email != "") {
            jsonArray = GetScholatProfile.getScholatProfileByEmail(email);
        } else if (chineseName != null && chineseName != "") {
            jsonArray = GetScholatProfile.getScholatProfileByChineseName(chineseName);
        }else if(username != null && username != ""){
            jsonArray = GetScholatProfile.getScholatProfileByUserName(username);
        }
        count = jsonArray.size();
        List<JSONObject> list = new ArrayList<JSONObject>();
        // 读取url传回来的学者网数据
        for (int i = 0; i < count; i++) {
            JSONObject jsonObject1 = new JSONObject();
            JSONObject tempJSONObject = jsonArray.getJSONObject(i);
            jsonObject1.put("scholat_username", tempJSONObject.getString("user"));
            jsonObject1.put("username", tempJSONObject.getString("userChineseName"));
            jsonObject1.put("unit_name", tempJSONObject.getString("workUnit"));
            jsonObject1.put("department_name", tempJSONObject.getString("workDepartment"));
            jsonObject1.put("avatar", tempJSONObject.getString("userPictureUrl"));
            jsonObject1.put("research_direction", tempJSONObject.getString("researchInterest"));
            String scholat_email = tempJSONObject.getString("workEmail");
            // 判断是否已经存在该邮箱用户
            JSONObject requestJson = new JSONObject();
            requestJson.put("email",scholat_email);
            JSONObject jsonObject2 = managerDao.judgeEmailExist(requestJson);
            String flag = jsonObject2.getString("flag");
            int whetherHasEmail = Integer.parseInt(flag);
            if(whetherHasEmail!=0){
                jsonObject1.put("email_exist", true);
            }else{
                jsonObject1.put("email_exist", false);
            }
            jsonObject1.put("email", tempJSONObject.getString("workEmail"));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat strFormat = new SimpleDateFormat("yyyyMMddHHmm");
            String str = tempJSONObject.getString("introUpdateTime");
            Date date = new Date();
            try {
                date = strFormat.parse(str);
            } catch (ParseException e) {
                //e.printStackTrace();
            }
            String time = dateFormat.format(date); //可以把日期转换转指定格式的字符串
            jsonObject1.put("update_time", time);
            String scholatIntro = tempJSONObject.getString("introduction");
            jsonObject1.put("intro", scholatIntro);
            jsonObject1.put("degree", tempJSONObject.getString("degree"));
            jsonObject1.put("post", tempJSONObject.getString("scholarTitle"));
            jsonObject1.put("qrcode", tempJSONObject.getString("qrcodeUrl"));
            list.add(jsonObject1);
        }
        //count =managerDao.countScholat(jsonObject);
        System.out.println("编辑界面根据邮箱或姓名查询学者网账号结果: 有" + count + "位教师");

		if(count!=0){
			list = managerDao.searchScholat(jsonObject);
			System.out.println("后台查询到的教师数据为: "+list);
		}else{
			System.out.println("并没有找到学者网对应的学者数据");
		}
        return CommonUtil.successPage(jsonObject, list, count);
    }

    @Override
    public void unBindScholat(String id) {
        managerDao.unBindScholat(id);
    }
    @Override
    public void bindScholat(String id,String scholat_username) {
        managerDao.bindScholat(id,scholat_username);
    }

    @Override
    public void exportTeacher(HttpServletRequest request,HttpServletResponse response) throws IOException {
        System.out.println( "CommonUtil.request2Json(request)="+CommonUtil.request2Json(request));
        JSONObject json=CommonUtil.request2Json(request);
        String json1 = json.getString("json");
        String str1=json1.substring(0, json1.indexOf(":"));
        String unitId=json1.substring(str1.length()+1,json1.length()-1);
        System.out.println("unitId="+unitId);
        JSONObject J=new JSONObject();
        J.put("unitId",unitId);
        JSONObject unitInfo = unitDao.getUnitInfo(J);
        String SchoolDomain=unitInfo.getString("schoolDomain");
        List<JSONObject> teacherList = managerDao.listTeacherLocalAll(J);
        List<teacherData> list = new ArrayList<teacherData>();
        for (int i = 0; i <teacherList.size() ; i++) {
            JSONObject teacher=teacherList.get(i);
            teacherData data = new teacherData();
            data.setUsername(teacher.getString("name"));
            if((teacher.getIntValue("sex"))==1){
                data.setSex("女");
            }else{
                data.setSex("男");
            }
            data.setEmail(teacher.getString("email"));
            data.setPost(teacher.getString("post"));
            data.setDuty(teacher.getString("duty"));
            data.setLabel(teacher.getString("label"));
            data.setDepartment_name(teacher.getString("departName"));
            data.setDomain_name("http://faculty.scholat.com/teacher/"+SchoolDomain+"/"+teacher.getString("domainName"));
//            data.setSubject(teacher.getString("subject"));
            data.setDegree(teacher.getString("degree"));
//            data.setGraduateFrom(teacher.getString("graduateFrom"));
            data.setResearch_direction(teacher.getString("research_direction"));
            data.setWork_place(teacher.getString("work_place"));
            data.setPhone(teacher.getString("phone"));
            list.add(data);
        }

        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("教师信息", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), teacherData.class).autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(list);
        }  catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
    }
    @Override
    public void exportTeacher2(HttpServletRequest request,HttpServletResponse response) throws IOException {
        List<importTeacher> list = new ArrayList<importTeacher>();

        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("教师填写模板信息", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), importTeacher.class).autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(list);
        }  catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
    }

    @Override
    public void importTeacher(MultipartFile file, ManagerService managerService,Long unitId,String editName) {
        try {
            InputStream in=file.getInputStream();
            EasyExcel.read(in, importTeacher.class,new teacherExcelListen(managerService,unitId,editName)).sheet().doRead();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

