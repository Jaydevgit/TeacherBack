package com.teacher.scholat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.dao.SchoolDao;
import com.teacher.scholat.dao.UnitDao;
import com.teacher.scholat.service.SchoolService;
import com.teacher.scholat.util.CommonUtil;
import com.teacher.scholat.util.GetScholatProfile;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SchoolServiceImpl implements SchoolService {

    @Resource
    private SchoolDao schoolDao;

    /**
     * 学校教师列表
     */
    @Override
    public JSONObject listTeacher(JSONObject jsonObject) {
        System.out.println("前端传过来的学校教师列表要求为: ");
        CommonUtil.fillPageParam(jsonObject);
        //long unitId = jsonObject.getLongValue("unitId");
        int count = schoolDao.countTeacher(jsonObject);
        System.out.println("........有"+count+"位教师");
//        int countScholat = managerDao.countUpdateTeacher(jsonObject);
        // 搜索列表, 对加入进来的信息加入在线搜索的user_profile: introUpdateTime信息
        //List<JSONObject> list = managerDao.listTeacher(jsonObject);
        List<JSONObject> listLocal = schoolDao.listTeacherLocal(jsonObject);
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
        return CommonUtil.successPage(jsonObject, listLocal, count);
    }
    /**
     * 学院教师列表
     */
    @Override
    public JSONObject listTeacherByUnit(JSONObject jsonObject) {
        System.out.println("前端传过来的学校教师列表要求为: ");
        CommonUtil.fillPageParam(jsonObject);
        //long unitId = jsonObject.getLongValue("unitId");
        int count = schoolDao.countUnitTeacher(jsonObject);
        System.out.println("........有"+count+"位教师");
//        int countScholat = managerDao.countUpdateTeacher(jsonObject);
        // 搜索列表, 对加入进来的信息加入在线搜索的user_profile: introUpdateTime信息
        //List<JSONObject> list = managerDao.listTeacher(jsonObject);
        List<JSONObject> listLocal = schoolDao.listTeacherByUnit(jsonObject);
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
        return CommonUtil.successPage(jsonObject, listLocal, count);
    }
    @Override
    public JSONObject getSchoolInfo(JSONObject object) {
        JSONObject school = schoolDao.getSchoolInfo(object);
        System.out.println("获取到的school------"+school);
        return CommonUtil.successJson(school);
    }
    /**
     * 模糊搜索教师
     */
    @Override
    public JSONObject searchTeacher(JSONObject jsonObject) {
        System.out.println("搜索 ==========> 学校前端传过来的教师列表要求为: ");
        System.out.println(jsonObject);
        CommonUtil.fillPageParam(jsonObject);
        int count = schoolDao.countKeyTeacher(jsonObject);
        System.out.println("........有" + count + "位教师");


        List<JSONObject> list = schoolDao.searchTeacher(jsonObject);
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
    public JSONObject updateSchoolInfo(JSONObject object) {
        schoolDao.updateSchoolInfo(object);
        return CommonUtil.successJson();
    }
    /**
     * 学院列表
     */
    @Override
    public JSONObject getUnitList(JSONObject jsonObject) {
        List<JSONObject> unitList = schoolDao.getUnitList(jsonObject);
        System.out.println("后台查询到的学院数据为: " + unitList);
        return CommonUtil.successPage(unitList);
    }

    /**
     * 学校管理员删除教师
     */
    @CacheEvict(value = "FacultyTeacherAll",key = "'Faculty'+#jsonObject.getLongValue(\"unitId\")")
    @Override
    public int deleteTeacher(JSONObject jsonObject) {
        return schoolDao.deleteTeacher(jsonObject);
    }

}
