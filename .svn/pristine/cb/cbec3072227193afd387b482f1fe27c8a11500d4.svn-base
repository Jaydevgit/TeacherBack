package com.teacher.scholat.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.teacher.scholat.service.TeacherService;
import com.teacher.scholat.util.CommonUtil;
import com.teacher.scholat.util.constants.ErrorEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/teacher")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @PostMapping("/persona/getScholatRecommendUsers")
    public JSONObject getScholatRecommendUsers(@RequestBody JSONObject requestJson) {
        String username = requestJson.get("username").toString();
        System.out.println("################## "+username+"查找学者推荐用户");
        JSONObject jsonObject =teacherService.getPersonaRecommendUsers(username);
        String users = jsonObject.getString("recommend_user");
        String[] str_arr = users.split("#");
        int len = str_arr.length;
        JSONObject res = new JSONObject();
        // 这里推荐11个人就可以了
        for(int i=1;i<11;i++){
            String scholat_username = teacherService.getScholatUernameByScholatID(Long.parseLong(str_arr[i]));
            JSONObject jsonObject1 = teacherService.getScholatInfoSimple(scholat_username);
            res.put(Integer.toString(i),jsonObject1);
        }
        return res;
    }
    @PostMapping("/persona/getLoacalRecommendUsers")
    public JSONObject getLoacalRecommendUsers(@RequestBody JSONObject requestJson) {
        String local_id = requestJson.get("local_id").toString();
        System.out.println("################## "+local_id+"查找仅本地推荐用户");
        JSONObject jsonObject =teacherService.getLocalRecommendUsers(local_id);
        String users = jsonObject.getString("recommend_user");
        String[] str_arr = users.split("#");
        int len = str_arr.length;
        JSONObject res = new JSONObject();
        // 这里推荐11个人就可以了
        for(int i=1;i<11;i++){
            System.out.println(str_arr[i]);
            JSONObject jsonObject1 = teacherService.getLocalInfo(null,str_arr[i]);
            res.put(Integer.toString(i),jsonObject1);
        }
        return res;
    }
    @PostMapping("/persona/getUnionRecommendUsers")
    public JSONObject getUnionRecommendUsers(@RequestBody JSONObject requestJson) {
        String local_id = requestJson.get("local_id").toString();
        System.out.println("################## "+local_id+"查找仅本地推荐用户");
        JSONObject jsonObject =teacherService.getUnionRecommendUsers(local_id);
        String users = jsonObject.getString("recommend_union");
        String[] str_arr = users.split("#");
        int len = str_arr.length;
        JSONObject res = new JSONObject();
        // 这里推荐11个人就可以了
        for(int i=1;i<11;i++){
            JSONObject jsonObject1 = teacherService.getLocalInfo(null,str_arr[i]);
            res.put(Integer.toString(i),jsonObject1);
        }
        return res;
    }

    @GetMapping("/{id}")
    public JSONObject getTeacherInfo(@PathVariable("id") Long id) {
        // howHttpRequestContent(request);
        System.out.println("传来来的参数id为。。。。。。" + id);
        return CommonUtil.successJson(teacherService.getTeacherInfo(id));
    }

    //  ------------------------------ 学者画像相关(数据来源于本地数据库) ----------------------------------------
    // 根据id检查是否已经绑定了学者网账号
    @GetMapping("/getPersonaProfile")
    public JSONObject getScholatProfile(HttpServletRequest request) {
        System.out.println("------------------- 查找学者画像的教师信息");
        JSONObject jsonObject = CommonUtil.request2Json(request);
        System.out.println(jsonObject);
        String username = jsonObject.getString("username");
        String user_id = jsonObject.getString("user_id");
        JSONObject profile = teacherService.getScholatInfo(username);
        JSONObject local_profile = teacherService.getLocalInfo(username,user_id);
        JSONObject local_profile_keywords = teacherService.getLocalKeywords(username,user_id);
        // 查找学者画像分词结果
        JSONObject keywords = teacherService.getScholatPersona_keywords(username);
        profile.put("intro_keywords",keywords.get("intro_keywords"));
        profile.put("paper_keywords",keywords.get("paper_keywords"));
        profile.put("local_profile",local_profile);
        profile.put("local_profile_keywords",local_profile_keywords.get("keywords"));
        if (profile != null) {
            return CommonUtil.successJson(profile);
        } else return CommonUtil.errorJson(ErrorEnum.E_400);
    }

    // 根据id检查论文
    @GetMapping("/persona/getScholatPaper/{username}")
    public JSONObject getScholatPaper(@PathVariable("username") String username) {
        System.out.println("【学者画像】：查找学者论文");
        Long scholatID = teacherService.getScholatID(username);
        JSONObject scholat = new JSONObject();
        List<JSONObject> paper = teacherService.getLocalScholatPaper(username,scholatID);
        scholat.put("paper", paper);
        return CommonUtil.successJson(scholat);
    }

    // 根据id输出学者关系
    @GetMapping("/persona/getRelation/{username}")
    public JSONObject getPersonaRelation(@PathVariable("username") String username) {
        System.out.println("【学者画像】：查找学者关系");
        Long scholatID = teacherService.getScholatID(username);
        JSONObject relation = new JSONObject();
        List<JSONObject> friends = teacherService.getLocalScholatFriends(username,scholatID);
        List<JSONObject> fans = teacherService.getLocalScholatFans(username,scholatID);
        List<JSONObject> watchs = teacherService.getLocalScholatWatchs(username,scholatID);
        relation.put("friends", friends);
        relation.put("fans", fans);
        relation.put("watchs", watchs);
        return CommonUtil.successJson(relation);
    }

    //  ------------------------------ 学者画像相关_end ----------------------------------------
    @GetMapping("/searchTeacher")
    public JSONObject searchTeacher(HttpServletRequest request) {

        return teacherService.searchTeacher(CommonUtil.request2Json(request));
    }

    @GetMapping("/getUnitIdBytId")
    public JSONObject getUnitIdBytId(HttpServletRequest request) {
        return teacherService.getUnitIdBytId(CommonUtil.request2Json(request));
    }

    // 展示httprequest的url地址内容
    public void showHttpRequestContent(HttpServletRequest request) {

        System.out.println("getRequestURL: " + request.getRequestURL());
        System.out.println("getRequestURI: " + request.getRequestURI());
        System.out.println("getQueryString: " + request.getQueryString());
        System.out.println("getRemoteAddr: " + request.getRemoteAddr());
        System.out.println("getRemoteHost: " + request.getRemoteHost());
        System.out.println("getRemotePort: " + request.getRemotePort());
        System.out.println("getRemoteUser: " + request.getRemoteUser());
        System.out.println("getLocalAddr: " + request.getLocalAddr());
        System.out.println("getLocalName: " + request.getLocalName());
        System.out.println("getLocalPort: " + request.getLocalPort());
        System.out.println("getMethod: " + request.getMethod());
        System.out.println("-------request.getParamterMap()-------");
        //得到请求的参数Map，注意map的value是String数组类型
        Map map = request.getParameterMap();
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            String[] values = (String[]) map.get(key);
            for (String value : values) {
                System.out.println(key + "=" + value);
            }
        }
        System.out.println("--------request.getHeader()--------");
        //得到请求头的name集合
        Enumeration<String> em = request.getHeaderNames();
        while (em.hasMoreElements()) {
            String name = (String) em.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + "=" + value);
        }
    }
}
