package com.teacher.scholat.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.teacher.scholat.dao.AcademicDao;
import com.teacher.scholat.dao.ScholatDao;
import com.teacher.scholat.dao.TeacherDao;
import com.teacher.scholat.dao.UnitDao;
//import com.teacher.scholat.repository.PaperRepository;
//import com.teacher.scholat.repository.PatentRepository;
//import com.teacher.scholat.repository.ProjectRepository;
import com.teacher.scholat.model.excel.*;
import com.teacher.scholat.service.AcademicService;
import com.teacher.scholat.util.CommonUtil;
//import com.teacher.scholat.util.EditDistance;
//import com.teacher.scholat.util.TextUtil;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import com.teacher.scholat.util.GetScholatAcademic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AcademicServiceImpl implements AcademicService {

    @Resource
     ScholatDao scholatDao;
    @Resource
    AcademicDao academicDao;
    @Resource
    TeacherDao teacherDao;
    @Resource
    UnitDao unitDao;
//    @Autowired
//    private ElasticsearchTemplate esTemplate;
//    @Autowired
//    private PaperRepository paperRepository;
//    @Autowired
//    private PatentRepository patentRepository;
//    @Autowired
//    private ProjectRepository projectRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject getPaperFromScholat(JSONObject jsonObject) {
        String scholatUsername = jsonObject.getString("scholat_username");
        JSONArray jsonArray = GetScholatAcademic.getScholatPaperByUserName(scholatUsername);
        List<Long> list1 = academicDao.paperIdsByScholatname(jsonObject);
        System.out.println("----------list1--------"+list1);

        List<JSONObject> paperAll = academicDao.listPaperAll(jsonObject);

        List<JSONObject> list = new ArrayList<>();
        if (jsonArray.toString().equals("[null]"))
            return CommonUtil.successJson(list);
        //每一篇论文
        for (int i = 0;i < jsonArray.size();i++){
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            String authors = jsonObject1.getString("authors");
            //需要处理：后面格式为"authors":"[{\"authorName\":\"Jianguo Li\",\"isCorresponding\":0,\"isSelf\":0,
            // \"paperCreatror\":\"\"}
            if (authors.indexOf("isCorresponding") != -1){
                System.out.println("-------authorsTest-----" + authors);
                JSONArray jsonArray1 = JSONArray.parseArray(authors);
                StringBuffer sum = new StringBuffer();
                for (int j = 0 ; j < jsonArray1.size();j++){
                    JSONObject author1 = jsonArray1.getJSONObject(j);
                    sum.append(author1.getString("authorName"));
                    if (j != jsonArray1.size()-1)
                        sum.append(", ");
                }
                jsonObject1.put("authors" , sum.toString());
            }

            jsonObject1.put("exist",0);
            //需要处理：id是否已经存在于
            Long id = jsonObject1.getLong("id");
            for (int k = 0;k < list1.size();k++){
                //Long必须要用longValue作比较，否则只是比较地址
                if (id.longValue() == list1.get(k).longValue())
                {
                    jsonObject1.put("exist",1);
                    break;
                }
            }
//            if (jsonObject1.getInteger("exist").equals(0)) {
//                for (int z = 0; z < paperAll.size(); z++) {
//                    JSONObject paper = paperAll.get(z);
//                    if (EditDistance.getsimilarity(paper.getString("title"), jsonObject1.getString("title")) > 0.8) {
//                        jsonObject1.put("exist", 2);
//                        jsonObject1.put("similarId" ,paper.getInteger("id"));
//                        break;
//                    }
//                }
//            }
            list.add(jsonObject1);
        }
        return CommonUtil.successJson(list);
    }

    //论文模糊去重
    @Override
    public void deduplicationPaper(){
        List<JSONObject> allUnit = scholatDao.getAllUnit();
        System.out.println("去重论文学院集合为:"+allUnit);
        for (int i = 0; i < allUnit.size(); i++) {
            int UnitId=allUnit.get(i).getInteger("id");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("unitId",UnitId);
            List<JSONObject> paperAll = academicDao.listPaperAll(jsonObject);
            //存储所有论文
            HashMap<Integer,String> map=new HashMap<>();
            //存储无重复论文
            HashMap<Integer,String> map2=new HashMap<>();
            for (int j = 0; j < paperAll.size(); j++) {
                //论文题目 论文id
                String title=paperAll.get(j).getString("title");
                Integer id=paperAll.get(j).getIntValue("id");
                if(map.containsValue(title)){
                    String scholatName=paperAll.get(j).getString("scholatUsername");
                   // System.out.println("重复论文id为:"+id);
                    //标记重复论文
                    academicDao.siguDeduplicationPaper(id);
                    //获取保留论文id
                    Integer svaeId=getKey(map2,title);
                    System.out.println("保留论文id为:"+getKey(map2,title)+"学者网用户名:"+scholatName);
                    //查询共同作者的学者网用户名集合
                    JSONObject coAuScholatNameList = academicDao.getCoAuScholatName(svaeId);
                    Set<String> set = new HashSet<>();
                    if(coAuScholatNameList!=null){
                        //保存无重复学者网共同用户集合
                    String coAuScholatName=coAuScholatNameList.getString("coAuScholatName").replace(" ","");;
                        if(!coAuScholatName.isEmpty()){
                            String[] scholat_usernames = coAuScholatName.split(",");
                            for(String name:scholat_usernames){
                                System.out.println("共同作者的学者网用户名集合为:"+name);
                                set.add(name);
                            }
                        }
                    }
                    set.add(scholatName);
                    System.out.println("set=="+set.toString().substring(1,set.toString().length()-1).replace(" ",""));
                    String scholatNameList=set.toString().substring(1,set.toString().length()-1);

                    //共用关联论文Json数据
                    JSONObject json = new JSONObject();
                    json.put("id",svaeId);
                    json.put("scholatNameList",scholatNameList);
                    //添加共同作者的学者网用户名集合
                    academicDao.updateCoAuScholatName(json);
                }else{
                    map2.put(id,title);
                }
                map.put(id,title);
            }
        }

    }

    //根据获取vaule得到key
    public static Integer getKey(Map map, String value){
        Integer keyList = null;
      //  List<Object> keyList = new ArrayList<>();
        for(Object key: map.keySet()){
            if(map.get(key).equals(value)){
                keyList= (Integer) key;
            }
        }
        return keyList;
    }

    @Override
    public JSONObject getPatentFromScholat(JSONObject jsonObject) {
        String scholatUsername = jsonObject.getString("scholat_username");
        JSONArray jsonArray = GetScholatAcademic.getScholatPatentByUserName(scholatUsername);
        List<Long> list1 = academicDao.patentIdsByScholatname(jsonObject);
        System.out.println("----------list1--------"+list1);
        List<JSONObject> patentAll = academicDao.listPatentAll(jsonObject);

        List<JSONObject> list = new ArrayList<>();
        if (jsonArray.toString().equals("[null]"))
            return CommonUtil.successJson(list);
        //每一篇论文
        for (int i = 0;i < jsonArray.size();i++){
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            jsonObject1.put("exist",0);
            //需要处理：id是否已经存在于
            Long id = jsonObject1.getLong("id");
            for (int k = 0;k < list1.size();k++){
                //Long必须要用longValue作比较，否则只是比较地址
                if (id.longValue() == list1.get(k).longValue())
                {
                    jsonObject1.put("exist",1);
                    break;
                }
            }
//            if (jsonObject1.getInteger("exist").equals(0)) {
//                for (int z = 0; z < patentAll.size(); z++) {
//                    JSONObject patent = patentAll.get(z);
//                    if (EditDistance.getsimilarity(patent.getString("title"), jsonObject1.getString("name")) > 0.8) {
//                        jsonObject1.put("exist", 2);
//                        jsonObject1.put("similarId" ,patent.getInteger("id"));
//                        break;
//                    }
//                }
//            }
            list.add(jsonObject1);
        }
        return CommonUtil.successJson(list);
    }

    @Override
    public JSONObject getProjectFromScholat(JSONObject jsonObject) {
        String scholatUsername = jsonObject.getString("scholat_username");
        JSONArray jsonArray = GetScholatAcademic.getScholatProjectByUserName(scholatUsername);
        System.out.println(jsonArray);
        List<Long> list1 = academicDao.projectIdsByScholatname(jsonObject);
        System.out.println("----------list1--------"+list1);
        List<JSONObject> projectAll = academicDao.listProjectAll(jsonObject);

        List<JSONObject> list = new ArrayList<>();
        if (jsonArray.toString().equals("[null]"))
            return CommonUtil.successJson(list);
        //每一篇论文
        for (int i = 0;i < jsonArray.size();i++){
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            jsonObject1.put("exist",0);
            //需要处理：id是否已经存在于
            Long id = jsonObject1.getLong("id");
         //   if (list.size()!=0){
            for (int k = 0;k < list1.size();k++){
                    //Long必须要用longValue作比较，否则只是比较地址
                    if (id.longValue() == list1.get(k).longValue())
                    {
                        jsonObject1.put("exist",1);
                        break;
                    }
            }
         //   }
//            if (jsonObject1.getInteger("exist").equals(0)) {
//                for (int z = 0; z < projectAll.size(); z++) {
//                    JSONObject project = projectAll.get(z);
//                    if (EditDistance.getsimilarity(project.getString("title"), jsonObject1.getString("name")) > 0.8) {
//                        jsonObject1.put("exist", 2);
//                        jsonObject1.put("similarId" ,project.getInteger("id"));
//                        break;
//                    }
//                }
//            }
            list.add(jsonObject1);
        }
        return CommonUtil.successJson(list);
    }

    @Override
    public JSONObject getPublicationFromScholat(JSONObject jsonObject) {
        String scholatUsername = jsonObject.getString("scholat_username");
        JSONArray jsonArray = GetScholatAcademic.getScholatPublicationByUserName(scholatUsername);
        System.out.println(jsonArray);
        List<Long> list1 = academicDao.publicationIdsByScholatname(jsonObject);
        System.out.println("----------list1--------"+list1);
        List<JSONObject> list = new ArrayList<>();
        if (jsonArray.toString().equals("[null]"))
            return CommonUtil.successJson(list);
        //每一篇论文
        for (int i = 0;i < jsonArray.size();i++){
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            jsonObject1.put("exist",0);
            //需要处理：id是否已经存在于
            Long id = jsonObject1.getLong("id");
            //   if (list.size()!=0){
            for (int k = 0;k < list1.size();k++){
                //Long必须要用longValue作比较，否则只是比较地址
                if (id.longValue() == list1.get(k).longValue())
                {
                    jsonObject1.put("exist",1);
                    break;
                }
            }
            //   }
//            if (jsonObject1.getInteger("exist").equals(0)) {
//                for (int z = 0; z < projectAll.size(); z++) {
//                    JSONObject project = projectAll.get(z);
//                    if (EditDistance.getsimilarity(project.getString("title"), jsonObject1.getString("name")) > 0.8) {
//                        jsonObject1.put("exist", 2);
//                        jsonObject1.put("similarId" ,project.getInteger("id"));
//                        break;
//                    }
//                }
//            }
            list.add(jsonObject1);
        }
        return CommonUtil.successJson(list);
    }

    @Override
    public JSONObject getPaper(Long id) {
        JSONObject paper = academicDao.getPaper(id);
        System.out.println("paper="+paper);
        ArrayList<JSONObject> teacherList = (ArrayList<JSONObject>) paper.get("teacherList");
        System.out.println("teacherList="+teacherList);
        if (teacherList.get(0).getString("tCount").equals("0"))
            paper.put("teacherList" ,new ArrayList<>());
//        System.out.println("paper" + paper);
//        String similarPaper = paper.getString("similarPaper");
//        System.out.println("similarPaper" + similarPaper);
//        List<JSONObject> similarList = new ArrayList<>();
//        if (similarPaper != null && similarPaper.indexOf(",")!= -1){
//        String[] split = similarPaper.split(",");
//        for (int i = 0;i < split.length;i++){
//            Long ad = Long.valueOf(split[i]);
//            System.out.println(ad);
//            JSONObject paper1 = academicDao.getPaper(ad);
//            similarList.add(paper1);
//            }
//        }
//        else if (similarPaper != null )
//        {       Long ad = Long.valueOf(similarPaper);
//                System.out.println(ad);
//                JSONObject paper1 = academicDao.getPaper(Long.valueOf(ad));
//                similarList.add(paper1);
//
//        }
//        paper.put("similarList" , similarList);

        //改成在前端修改格式 这边就先注释了
/*        String datetime = paper.getString("datetime");
        System.out.println("-----datetime----" + datetime);
        String[] split = datetime.split("\\.");
        //"datetime" -> "2018-12-31T16:00:00.000Z"
        System.out.println(split);
        StringBuilder sb = new StringBuilder();
        sb.append(split[0]);
        sb.append("-");
        //第二位 为数字
        if (split[1] != "-"){
        sb.append(split[1]);
        sb.append("-01");
        }
        else {
            sb.append("-01-01");
        }
        paper.put("datetime" , sb.toString());*/
        return paper;
    }

    @Override
    public JSONObject listPaper(JSONObject jsonObject) {
        System.out.println("前端传过来的论文列表要求为: " + jsonObject);
        CommonUtil.fillPageParam(jsonObject);
        long unitId = jsonObject.getLongValue("unitId");
        int count = academicDao.countPaper(unitId);
        List<JSONObject> list = academicDao.listPaper(jsonObject);
        return CommonUtil.successPage(jsonObject, list , count);
    }
    @Override
    public JSONObject listPublication(JSONObject jsonObject) {
        System.out.println("前端传过来的著作列表要求为: " + jsonObject);
        CommonUtil.fillPageParam(jsonObject);
        long unitId = jsonObject.getLongValue("unitId");
        int count = academicDao.countPublication(unitId);
        List<JSONObject> list = academicDao.listPublication(jsonObject);
        return CommonUtil.successPage(jsonObject, list , count);
    }

    @Override
    public JSONObject listPaperAll(JSONObject jsonObject) {
        Long unitId = jsonObject.getLongValue("unitId");
        jsonObject.put("unitId", unitId);
        List<JSONObject> list = academicDao.listPaperAll(jsonObject);
        return CommonUtil.successPage(list);
    }
    @Override
    public JSONObject getPaperteacher(JSONObject jsonObject) {

        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        String beginTime=jsonObject.getString("valueStart");
        String endTime=jsonObject.getString("valueEnd");
       // System.out.println("beginTime="+beginTime+endTime);
        if(beginTime!=null&&beginTime.length()!=0){
            beginTime = beginTime.replace("-", ".").substring(0,10);
            jsonObject.put("beginTime", beginTime);
        }
        if(endTime!=null&&endTime.length()!=0){
            endTime = endTime.replace("-", ".").substring(0,10);
            jsonObject.put("endTime", endTime);
        }
        Long unitId = jsonObject.getLongValue("unitId");
        jsonObject.put("unitId", unitId);
       List<JSONObject> list = academicDao.getPaperteacher(jsonObject);
        return CommonUtil.successPage(list);
    }

    @Override
    public JSONObject searchPaper(JSONObject jsonObject) {

        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        String beginTime=jsonObject.getString("valueStart");
        String endTime=jsonObject.getString("valueEnd");
        // System.out.println("beginTime="+beginTime+endTime);
        if(beginTime!=null&&beginTime.length()!=0){
            beginTime = beginTime.replace("-", ".").substring(0,10);
            jsonObject.put("beginTime", beginTime);
        }
        if(endTime!=null&&endTime.length()!=0){
            endTime = endTime.replace("-", ".").substring(0,10);
            jsonObject.put("endTime", endTime);
        }
        Long unitId = jsonObject.getLongValue("unitId");
        jsonObject.put("unitId", unitId);
        CommonUtil.fillPageParam(jsonObject);
        int count = academicDao.countSearchPaper(jsonObject);
        List<JSONObject> list = academicDao.searchPaper(jsonObject);
        return CommonUtil.successPage(jsonObject, list , count);
    }

    @Override
    public JSONObject searchProject(JSONObject jsonObject) {
        System.out.println("前端传过来的项目列表要求为: " + jsonObject);
        String beginTime=jsonObject.getString("valueStart");
        String endTime=jsonObject.getString("valueEnd");
        // System.out.println("beginTime="+beginTime+endTime);
        if(beginTime!=null&&beginTime.length()!=0){
            beginTime = beginTime.replace("-", ".").substring(0,10);
            jsonObject.put("beginTime", beginTime);
        }
        if(endTime!=null&&endTime.length()!=0){
            endTime = endTime.replace("-", ".").substring(0,10);
            jsonObject.put("endTime", endTime);
        }
        long unitId = jsonObject.getLongValue("unitId");
        System.out.println("aaa="+jsonObject.getString("pageNum"));
        CommonUtil.fillPageParam(jsonObject);
        int count = academicDao.countSearchProject(jsonObject);
        List<JSONObject> list = academicDao.searchProject(jsonObject);
        System.out.println("-----------projectList-------"+list+"count="+count);
        return CommonUtil.successPage(jsonObject, list , count);
    }

    @Override
    public JSONObject searchPatent(JSONObject jsonObject) {
        System.out.println("前端传过来的项目列表要求为: " + jsonObject);
        String beginTime=jsonObject.getString("valueStart");
        String endTime=jsonObject.getString("valueEnd");
        // System.out.println("beginTime="+beginTime+endTime);
        if(beginTime!=null&&beginTime.length()!=0){
            beginTime = beginTime.replace("-", ".").substring(0,10);
            jsonObject.put("beginTime", beginTime);
        }
        if(endTime!=null&&endTime.length()!=0){
            endTime = endTime.replace("-", ".").substring(0,10);
            jsonObject.put("endTime", endTime);
        }
        long unitId = jsonObject.getLongValue("unitId");
        System.out.println("aaa="+jsonObject.getString("pageNum"));
        CommonUtil.fillPageParam(jsonObject);
        int count = academicDao.countSearchPatent(jsonObject);
        List<JSONObject> list = academicDao.searchPatent(jsonObject);
        return CommonUtil.successPage(jsonObject, list , count);
    }

    @Override
    public JSONObject searchPublication(JSONObject jsonObject) {
        System.out.println("前端传过来的项目列表要求为: " + jsonObject);
        String beginTime=jsonObject.getString("valueStart");
        String endTime=jsonObject.getString("valueEnd");
        // System.out.println("beginTime="+beginTime+endTime);
        if(beginTime!=null&&beginTime.length()!=0){
            beginTime = beginTime.replace("-", ".").substring(0,10);
            jsonObject.put("beginTime", beginTime);
        }
        if(endTime!=null&&endTime.length()!=0){
            endTime = endTime.replace("-", ".").substring(0,10);
            jsonObject.put("endTime", endTime);
        }
        long unitId = jsonObject.getLongValue("unitId");
        System.out.println("aaa="+jsonObject.getString("pageNum"));
        CommonUtil.fillPageParam(jsonObject);
        int count = academicDao.countSearchPublication(jsonObject);
        List<JSONObject> list = academicDao.searchPublication(jsonObject);
        return CommonUtil.successPage(jsonObject, list , count);
    }

    @Override
    public JSONObject getProjectteacher(JSONObject jsonObject) {

        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        String beginTime=jsonObject.getString("valueStart");
        String endTime=jsonObject.getString("valueEnd");
        // System.out.println("beginTime="+beginTime+endTime);
        if(beginTime!=null&&beginTime.length()!=0){
            beginTime = beginTime.replace("-", ".").substring(0,10);
            jsonObject.put("beginTime", beginTime);
        }
        if(endTime!=null&&endTime.length()!=0){
            endTime = endTime.replace("-", ".").substring(0,10);
            jsonObject.put("endTime", endTime);
        }
        Long unitId = jsonObject.getLongValue("unitId");
        jsonObject.put("unitId", unitId);
        List<JSONObject> list = academicDao.getProjectteacher(jsonObject);
        System.out.println("listlist="+list);
        return CommonUtil.successPage(list);
    }

    @Override
    public JSONObject getPatentteacher(JSONObject jsonObject) {

        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        String beginTime=jsonObject.getString("valueStart");
        String endTime=jsonObject.getString("valueEnd");
        // System.out.println("beginTime="+beginTime+endTime);
        if(beginTime!=null&&beginTime.length()!=0){
            beginTime = beginTime.replace("-", ".").substring(0,10);
            jsonObject.put("beginTime", beginTime);
        }
        if(endTime!=null&&endTime.length()!=0){
            endTime = endTime.replace("-", ".").substring(0,10);
            jsonObject.put("endTime", endTime);
        }
        Long unitId = jsonObject.getLongValue("unitId");
        jsonObject.put("unitId", unitId);
        List<JSONObject> list = academicDao.getPatentteacher(jsonObject);
        System.out.println("listlist="+list);
        return CommonUtil.successPage(list);
    }



    @Override
    public JSONObject getPublicationteacher(JSONObject jsonObject) {

        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        String beginTime=jsonObject.getString("valueStart");
        String endTime=jsonObject.getString("valueEnd");
        // System.out.println("beginTime="+beginTime+endTime);
        if(beginTime!=null&&beginTime.length()!=0){
            beginTime = beginTime.replace("-", ".").substring(0,10);
            jsonObject.put("beginTime", beginTime);
        }
        if(endTime!=null&&endTime.length()!=0){
            endTime = endTime.replace("-", ".").substring(0,10);
            jsonObject.put("endTime", endTime);
        }
        Long unitId = jsonObject.getLongValue("unitId");
        jsonObject.put("unitId", unitId);
        List<JSONObject> list = academicDao.getPublicationteacher(jsonObject);
        System.out.println("listlist="+list);
        return CommonUtil.successPage(list);
    }

    @Override
    public JSONObject listProject(JSONObject jsonObject) {
        System.out.println("前端传过来的项目列表要求为: " + jsonObject);
        CommonUtil.fillPageParam(jsonObject);
        long unitId = jsonObject.getLongValue("unitId");
        int count = academicDao.countProject(unitId);
        List<JSONObject> list = academicDao.listProject(jsonObject);
        System.out.println("-----------projectList-------"+list);
        return CommonUtil.successPage(jsonObject, list , count);
    }

    @Override
    public JSONObject listProjectAll(JSONObject jsonObject) {
        Long unitId = jsonObject.getLongValue("unitId");
        jsonObject.put("unitId", unitId);
        List<JSONObject> list = academicDao.listProjectAll(jsonObject);
        return CommonUtil.successPage(list);
    }

    @Override
    public JSONObject getPaperInYears(JSONObject jsonObject) {
        Long unitId = jsonObject.getLongValue("unitId");
        jsonObject.put("unitId", unitId);
        List<JSONObject> list = academicDao.getPaperInYears(jsonObject);
        List<String> yearsList = new ArrayList<>();
        List<String> numList = new ArrayList<>();
        for (int i = 0;i < list.size();i++){
            yearsList.add(list.get(i).get("years")+"");
            numList.add(list.get(i).get("num") + "");
        }
        System.out.println(numList);
        System.out.println(yearsList);
        JSONObject jo = new JSONObject();
        jo.put("num",numList);
        jo.put("years",yearsList);
        return CommonUtil.successJson(jo);
    }

    @Override
    public JSONObject getPatentInYears(JSONObject jsonObject) {
        Long unitId = jsonObject.getLongValue("unitId");
        jsonObject.put("unitId", unitId);
        List<JSONObject> list = academicDao.getPatentInYears(jsonObject);
        List<String> yearsList = new ArrayList<>();
        List<String> numList = new ArrayList<>();
        for (int i = 0;i < list.size();i++){
            yearsList.add(list.get(i).get("years")+"");
            numList.add(list.get(i).get("num") + "");
        }
        System.out.println(numList);
        System.out.println(yearsList);
        JSONObject jo = new JSONObject();
        jo.put("num",numList);
        jo.put("years",yearsList);
        return CommonUtil.successJson(jo);
    }

    @Override
    public JSONObject getProjectInYears(JSONObject jsonObject) {
        Long unitId = jsonObject.getLongValue("unitId");
        jsonObject.put("unitId", unitId);
        List<JSONObject> list = academicDao.getProjectInYears(jsonObject);
        List<String> yearsList = new ArrayList<>();
        List<String> numList = new ArrayList<>();
        for (int i = 0;i < list.size();i++){
            yearsList.add(list.get(i).get("years")+"");
            numList.add(list.get(i).get("num") + "");
        }
        System.out.println(numList);
        System.out.println(yearsList);
        JSONObject jo = new JSONObject();
        jo.put("num",numList);
        jo.put("years",yearsList);
        return CommonUtil.successJson(jo);
    }

    @Override
    public JSONObject getPaperByTeacher(Long id) {
        List<JSONObject> paperByTeacher = academicDao.getPaperByTeacher(id);
        return CommonUtil.successPage(paperByTeacher);
    }

    @Override
    public JSONObject getPatentByTeacher(Long id) {
        List<JSONObject> patentByTeacher = academicDao.getPatentByTeacher(id);
        return CommonUtil.successPage(patentByTeacher);
    }

    @Override
    public JSONObject getProjectByTeacher(Long id) {
        List<JSONObject> projectByTeacher = academicDao.getProjectByTeacher(id);
        return CommonUtil.successPage(projectByTeacher);
    }



    @Override
    public JSONObject identifyTeacher(JSONObject jsonObject) {
        String authors = jsonObject.getString("authors").replace(" ","").replace("，",",").replace("、",",");
        long unitId = jsonObject.getLongValue("unitId");
        System.out.println(authors);
        String[] split = authors.split(",");
        List<JSONObject> list = new ArrayList<>();
        for (int i = 0;i < split.length;i++){
            JSONObject searchJson = new JSONObject();
            searchJson.put("unitId" , unitId);
            searchJson.put("name", split[i]);
            List<JSONObject> infoByName = teacherDao.getInfoByName(searchJson);
            if (!infoByName.isEmpty())
            for (int j = 0;j < infoByName.size();j++){
                list.add(infoByName.get(j));
            }
        }
        System.out.println(list);
        return CommonUtil.successPage(list);
    }

    @Override
    public JSONObject deletePaperTeacher(JSONObject jsonObject) {
        academicDao.deletePaperTeacher(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject deletePatentTeacher(JSONObject jsonObject) {
        academicDao.deletePatentTeacher(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject deleteProjectTeacher(JSONObject jsonObject) {
        academicDao.deleteProjectTeacher(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject addPaperTeacher(JSONObject jsonObject) {
        //处理关联成员
        if (jsonObject.getString("tIds") !="[]") {
            String tIds = jsonObject.getString("tIds").replace("[", "").replace("]", "").replace(" ", "");
            String[] split = tIds.split(",");
            long id = jsonObject.getLongValue("id");
            ArrayList<Long> list = new ArrayList<>();
            for (int k = 0; k < split.length; k++) {
                list.add(Long.valueOf(split[k]));
            }
            System.out.println("id + list" + id + list);
            academicDao.addPaperTeacher(id, list);
        }
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject addPatentTeacher(JSONObject jsonObject) {
        if (jsonObject.getString("tIds") !="[]") {
            String tIds = jsonObject.getString("tIds").replace("[", "").replace("]", "").replace(" ", "");
            String[] split = tIds.split(",");
            long id = jsonObject.getLongValue("id");
            ArrayList<Long> list = new ArrayList<>();
            for (int k = 0; k < split.length; k++) {
                list.add(Long.valueOf(split[k]));
            }
            System.out.println("id + list" + id + list);
            academicDao.addPatentTeacher(id, list);
        }
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject addProjectTeacher(JSONObject jsonObject) {
        if (jsonObject.getString("tIds") !="[]") {
            String tIds = jsonObject.getString("tIds").replace("[", "").replace("]", "").replace(" ", "");
            String[] split = tIds.split(",");
            long id = jsonObject.getLongValue("id");
            ArrayList<Long> list = new ArrayList<>();
            for (int k = 0; k < split.length; k++) {
                list.add(Long.valueOf(split[k]));
            }
            System.out.println("id + list" + id + list);
            academicDao.addProjectTeacher(id, list);
        }
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject addAllStatistic(JSONObject jsonObject) {
        academicDao.addAllStatistic(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject getTotal(long unitId) {
        return academicDao.getTotal(unitId);
    }

    @Override
    public JSONObject getAllCount(long unitId) {
        int paperTotal=academicDao.countPaper(unitId);
        int projectTotal=academicDao.countProject(unitId);
        int patentTotal=academicDao.countPatent(unitId);
        int publicationTotal=academicDao.countPublication(unitId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("paperTotal",paperTotal);
        jsonObject.put("projectTotal",projectTotal);
        jsonObject.put("patentTotal",patentTotal);
        jsonObject.put("publicationTotal",publicationTotal);
        return jsonObject;
    }

    @Override
    public JSONObject deleteAllStatistic(int unitId) {
        academicDao.deleteAllStatistic(unitId);
        return CommonUtil.successJson();
    }

//    @Override
//    public JSONObject aiPaper(Long id) throws IOException {
//        JSONObject paper = academicDao.getPaper(id);
//        String summary = paper.getString("summary");
//        String title = paper.getString("title");
//        String keywords = paper.getString("keyword");
//        LinkedHashMap<String, Double> occupy = TextUtil.classify(title + keywords + summary);
//        paper.put("occupy" , occupy);
//        JSONArray keyword = TextUtil.keywordTextRank(title + keywords, summary);
//        paper.put("keyword",keyword);
//        ArrayList<JSONObject> teacherList = (ArrayList) paper.get("teacherList");
//        List<JSONObject> links = links(teacherList);
//        paper.put("links",links);
//        System.out.println("link---"+links);
//        return CommonUtil.successJson(paper);
//    }

    public List<JSONObject> links(List<JSONObject> teacherList){
        LinkedHashMap<Long, List<Long>> hashMap = new LinkedHashMap<>();
        for (int i = 0;i < teacherList.size();i++){
            JSONObject teacher = teacherList.get(i);
            Long tId = teacher.getLongValue("tId");
            List<Long> papersByTeacher = academicDao.getPapersByTeacher(tId);
            hashMap.put(tId , papersByTeacher);
        }
        System.out.println(hashMap);

        ArrayList<JSONObject> links = new ArrayList<>();
        Iterator<Map.Entry<Long, List<Long>>> iterator = hashMap.entrySet().iterator();
        int flag = 1;
        while (iterator.hasNext()){
            Map.Entry<Long, List<Long>> next = iterator.next();
            Long tId = next.getKey();
            List<Long> papers = new ArrayList<>(next.getValue());
            //下一个
            Iterator<Map.Entry<Long, List<Long>>> tmp = hashMap.entrySet().iterator();
            int s = 0;
            while ( s < flag){
                tmp.next();
                s++;
            }
            flag++;
            while (tmp.hasNext()){

                HashMap<Long, Integer> mapList = new HashMap<>();
                ArrayList<Long> doublePaper = new ArrayList<>();
                JSONObject twoLink= new JSONObject();

                Map.Entry<Long, List<Long>> next1 =tmp.next();
                Long tId1 = next1.getKey();
                List<Long> papers1 = new ArrayList<>(next1.getValue());
                papers1.addAll(papers);//找出重复的元素
                for (Long paperId : papers1){
                    int count = 1;
                    if (mapList.get(paperId) != null){
                        count = mapList.get(paperId) + 1;
                    }
                    mapList.put(paperId , count);
                }
                for (Long paperId : mapList.keySet()){
                    if (mapList.get(paperId)!=null && mapList.get(paperId) > 1){
                        doublePaper.add(paperId);
                    }
                }

                if (!doublePaper.isEmpty()){
                    int size = doublePaper.size();
                    twoLink.put("source" , String.valueOf(tId));
                    twoLink.put("target" , String.valueOf(tId1));
                    twoLink.put("value" , String.valueOf(size));
                    twoLink.put("paperIds" , doublePaper.toString());
                    links.add(twoLink);
                }
            }

        }
        return links;
    }

//    @Override
//    public JSONObject aiUnitPaper(JSONObject jsonObject) {
//        List<JSONObject> teacherList = teacherDao.listTeacherAll(jsonObject);
//        JSONObject res = new JSONObject();
//        res.put("teacherList" , teacherList);
//        List<JSONObject> links = links(teacherList);
//        res.put("links",links);
//        return CommonUtil.successJson(res);
//    }

//    @Override
//    public JSONObject aiCatalogue(JSONObject jsonObject) throws IOException {
//        List<JSONObject> list = academicDao.listPaperAll(jsonObject);
//        List<List<String>> dataList = new ArrayList<>();
//        for (JSONObject paper : list){
//            String summary = paper.getString("summary");
//            String title = paper.getString("title");
//            String keywords = paper.getString("keyword");
//            Integer hot = paper.getInteger("hot");
//            LinkedHashMap<String, Double> occupy = TextUtil.classify(title + keywords + summary);
//            Iterator<Map.Entry<String, Double>> iterator = occupy.entrySet().iterator();
//            List<String> res = new ArrayList<>();
//            if (iterator.hasNext()){
//                Map.Entry<String, Double> next = iterator.next();
//                res.add(String.valueOf(convert2Num(next.getKey())));
//                res.add(String.valueOf(new Double(next.getValue()*100).intValue()));
//                res.add(String.valueOf(hot));
//                res.add(title + " ("+summary+") ");
//                dataList.add(res);
//            }
//        }
//        JSONObject result = new JSONObject();
//        result.put("paperList",list);
//        result.put("dataList",dataList);
//        return CommonUtil.successJson(result);
//    }

//    @Override
//    public JSONObject recommendPaper(JSONObject jsonObject) throws IOException {
//        List<JSONObject> paperList = academicDao.listPaperAll(jsonObject);
//        LinkedHashMap<Long, LinkedHashMap<Long, Double>> hm = new LinkedHashMap<>();
//        for (int i = 0;i < paperList.size();i++){
//            JSONObject paper1 = paperList.get(i);
//            Long id1 = paper1.getLongValue("id");
//            String title1 = paper1.getString("title");
//            String summary1 = paper1.getString("summary");
//            String keyword1 = paper1.getString("keyword");
//            String total1 = title1 + keyword1 + summary1;
//
//            LinkedHashMap<Long, Double> ld1;
//            if (!hm.containsKey(id1)) {
//                ld1= new LinkedHashMap<>();
//            }
//            else {
//                ld1 = hm.get(id1);
//            }
//            for (int j = i+1 ; j < paperList.size();j++){
//                JSONObject paper2 = paperList.get(j);
//                Long id2 = paper2.getLongValue("id");
//                String title2 = paper2.getString("title");
//                String summary2 = paper2.getString("summary");
//                String keyword2 = paper2.getString("keyword");
//                String total2 = title2 + keyword2 + summary2;
//                Double cos = TextUtil.twoTextCompare(total1, total2);
//                ld1.put(id2,cos);
//                if (!hm.containsKey(id2)){
//                    LinkedHashMap<Long, Double> ld2 = new LinkedHashMap<>();
//                    ld2.put(id1,cos);
//                    hm.put(id2,ld2);
//                }
//                else {
//                    LinkedHashMap<Long, Double> ld2 = hm.get(id2);
//                    ld2.put(id1,cos);
//                }
//            }
//            hm.put(id1 , ld1);
//        }
//        Iterator<Map.Entry<Long, LinkedHashMap<Long, Double>>> iterator = hm.entrySet().iterator();
//
//        LinkedHashMap<Long, LinkedHashMap<Long, Double>> sortHm = new LinkedHashMap<>();
//        while (iterator.hasNext()){
//            Map.Entry<Long, LinkedHashMap<Long, Double>> next = iterator.next();
//            Long id = next.getKey();
//            LinkedHashMap<Long, Double> cosMap = next.getValue();
//            Set<Map.Entry<Long, Double>> set = cosMap.entrySet();
//            ArrayList<Map.Entry<Long, Double>> list = new ArrayList<>(set);
//            Collections.sort(list, new Comparator<Map.Entry<Long, Double>>() {
//                @Override
//                public int compare(Map.Entry<Long, Double> o1, Map.Entry<Long, Double> o2) {
//                    if (o2.getValue() - o1.getValue() > 0)
//                        return 1;
//                    else if (o2.getValue() - o1.getValue() < 0)
//                        return -1;
//                    else
//                        return 0;
//                }
//            });
//
//            //创建有序集合LinkedHashMap，存放数据
//            LinkedHashMap<Long, Double> sortList = new LinkedHashMap<Long, Double>();
//            //遍历list
//            for (Map.Entry<Long, Double> entry : list) {
//                //将数据存到linkedHashMap
//                sortList.put(entry.getKey(),entry.getValue());
//            }
//            sortHm.put(id , sortList);
//        }
//
//        Iterator<Map.Entry<Long, LinkedHashMap<Long, Double>>> iterator1 = sortHm.entrySet().iterator();
//        while (iterator1.hasNext()){
//            Map.Entry<Long, LinkedHashMap<Long, Double>> next = iterator1.next();
//            Long id = next.getKey();
//            LinkedHashMap<Long, Double> value = next.getValue();
//            int flag = 0;
//            Iterator<Map.Entry<Long, Double>> iterator2 = value.entrySet().iterator();
//            StringBuilder similarPaper = new StringBuilder();
//            while (iterator2.hasNext() && flag<5){
//                Map.Entry<Long, Double> next1 = iterator2.next();
//                Long key = next1.getKey();
//                if (flag == 0)
//                    similarPaper.append(String.valueOf(key));
//                else
//                    similarPaper.append(","+ String.valueOf(key));
//                flag++;
//            }
//            JSONObject sql = new JSONObject();
//            sql.put("id",id);
//            sql.put("similarPaper" ,similarPaper.toString());
//            academicDao.updatePaperRecommend(sql);
//        }
//
//        return CommonUtil.successJson();
//    }

//    @Override
//    public JSONObject addSearch(JSONObject jsonObject) {
//        System.out.println("猜猜我是谁？"+jsonObject);
//        String text = jsonObject.getString("text");
//        List<JSONObject> list = new ArrayList<>();
//        if (jsonObject.getString("type").equals("0"))
//            list = paperRepository.findByTitleOrSummaryOrAuthorsOrKeywordOrCollegeNameLikeOrderByHotDesc(text,
//                text,text,
//                text,text);
//        else if (jsonObject.getString("type").equals("1"))
//            list = projectRepository.findByTitleOrSummaryOrApplicationOrLeaderOrCollegeNameLikeOrderByHotDesc(text,
//                    text,text,
//                    text,text);
//        else if (jsonObject.getString("type").equals("2"))
//            list = patentRepository.findByTitleOrSummaryOrAuthorsOrResearchOrCollegeNameLikeOrderByHotDesc(text,
//                    text,text,
//                    text,text);
//        return CommonUtil.successPage(list);
//    }

    public static Integer convert2Num(String word){
        /*switch (word){
            case "Web" :
                return 0;
            case "操作系统" :
                return 1;
            case "区块链" :
                return 2;
            case "人工智能" :
                return 3;
            case "软件工程" :
                return 4;
            case "大数据" :
                return 5;
            case "数据库" :
                return 6;
            case "数据挖掘" :
                return 7;
            case "图像处理" :
                return 8;
            case "云计算" :
                return 9;
        }*/
        switch (word){
            case "程序设计" :
                return 0;
            case "操作系统" :
                return 1;
            case "区块链" :
                return 2;
            case "人工智能" :
                return 3;
            case "大数据" :
                return 4;
            case "a" :
                return 5;
            case "数据库" :
                return 6;
            case "数据挖掘" :
                return 7;
            case "计算机视觉" :
                return 8;
            case "云计算" :
                return 9;
        }
        return 0;
    }

    @Override
    public JSONObject listPatent(JSONObject jsonObject) {
        System.out.println("前端传过来的专利列表要求为: " + jsonObject);
        CommonUtil.fillPageParam(jsonObject);
        long unitId = jsonObject.getLongValue("unitId");
        int count = academicDao.countPatent(unitId);
        List<JSONObject> list = academicDao.listPatent(jsonObject);
        System.out.println("-----------patentList-------"+list);
        return CommonUtil.successPage(jsonObject, list , count);
    }

    @Override
    public JSONObject listPatentAll(JSONObject jsonObject) {
        Long unitId = jsonObject.getLongValue("unitId");
        jsonObject.put("unitId", unitId);
        List<JSONObject> list = academicDao.listPatentAll(jsonObject);
        return CommonUtil.successPage(list);
    }


    @Override
    public JSONObject deletePaper(JSONObject jsonObject) {
        academicDao.removePaper(jsonObject);
        return CommonUtil.successJson();
    }
    @Override
    public JSONObject deletePubilcation(JSONObject jsonObject) {
        academicDao.removePubilcation(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addAllPaper(JSONObject jsonObject) {
        JSONArray jsonArray=jsonObject.getJSONArray("data");
        long unit_id=jsonObject.getLongValue("unitId");
        System.out.println("unit_id="+unit_id);
        String scholat_username = jsonObject.getString("scholat_username");
        for (int i = 0; i <jsonArray.size() ; i++) {
            Object tt = jsonArray.get(i); //遍历所有论文信息
            JSONObject t=(JSONObject) tt;
            long scholat_paper_id=t.getLongValue("id");
            if(academicDao.paperDeleteExitIf(scholat_paper_id)!=0){
                academicDao.NoDeletePaper(scholat_paper_id);
                continue;
            }else if(academicDao.paperExitIf(scholat_paper_id)!=0){
                continue;
            }
        //    System.out.println("p1p1="+tt);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            t.put("updateTime" , df.format(new Date()));
            if(t.getString("type").equals("期刊论文")){
                t.put("papertype" , 0);
            }else{
                t.put("papertype" , 1);
            }
            t.put("unitId",unit_id);
            t.put("scholat_username",scholat_username);
            t.put("scholat_paper_id" , t.getString("id"));
            //"datetime" -> "2018-12-31"
//        jsonObject.put("hot" ,1000);
            //从新版本输入时间
            t.put("datetime",t.getString("date"));
            System.out.println("ttt="+t);
            academicDao.addPaper(t);
        }
        return CommonUtil.successJson();
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addAllProject(JSONObject jsonObject) {
        JSONArray jsonArray=jsonObject.getJSONArray("data");
        long unit_id=jsonObject.getLongValue("unitId");
        String scholat_username = jsonObject.getString("scholat_username");
        for (int i = 0; i <jsonArray.size() ; i++) {
            Object tt = jsonArray.get(i); //遍历所有项目信息
            JSONObject t=(JSONObject) tt;
            long scholat_project_id=t.getLongValue("id");
            if(academicDao.projectDeleteExitIf(scholat_project_id)!=0){
                academicDao.NoDeleteProject(scholat_project_id);
                continue;
            }else if(academicDao.projectExitIf(scholat_project_id)!=0){
                continue;
            }
            System.out.println("p1p1="+tt);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            t.put("updateTime" , df.format(new Date()));
            t.put("unitId",unit_id);
            t.put("scholat_username",scholat_username);
            t.put("projectType",t.getString("originAndId"));
            t.put("scholat_project_id",t.getLongValue("id"));
            t.put("title",t.getString("name"));
            System.out.println("ttt="+t);
            academicDao.addProject(t);
        }

        System.out.println("jsonObjectjsonObject="+jsonObject);
        return CommonUtil.successJson();
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addAllPatent(JSONObject jsonObject) {
        JSONArray jsonArray=jsonObject.getJSONArray("data");
        long unit_id=jsonObject.getLongValue("unitId");
        String scholat_username = jsonObject.getString("scholat_username");
        for (int i = 0; i <jsonArray.size() ; i++) {
            Object tt = jsonArray.get(i); //遍历所有专利信息
            JSONObject t=(JSONObject) tt;
            long scholat_patent_id=t.getLongValue("id");
            if(academicDao.patentDeleteExitIf(scholat_patent_id)!=0){
                academicDao.NoDeletePatent(scholat_patent_id);
                continue;
            }else if(academicDao.patentExitIf(scholat_patent_id)!=0){
                continue;
            }
           // System.out.println("p1p1="+tt);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            t.put("updateTime" , df.format(new Date()));
            t.put("unitId",unit_id);
            t.put("scholat_username",scholat_username);
            t.put("datetime" , t.getString("date"));
            t.put("patentType",t.getString("typeAndId"));
            t.put("scholat_patent_id",t.getLongValue("id"));
            t.put("title",t.getString("name"));
            System.out.println("ttt="+t);
            academicDao.addPatent(t);
        }

    //    System.out.println("jsonObjectjsonObject="+jsonObject);
        return CommonUtil.successJson();
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addAllPublication(JSONObject jsonObject) {
        JSONArray jsonArray=jsonObject.getJSONArray("data");
        long unit_id=jsonObject.getLongValue("unitId");
        String scholat_username = jsonObject.getString("scholat_username");
        for (int i = 0; i <jsonArray.size() ; i++) {
            Object tt = jsonArray.get(i); //遍历所有著作信息
            JSONObject t=(JSONObject) tt;
            long scholat_publication_id=t.getLongValue("id");
            if(academicDao.publicationDeleteExitIf(scholat_publication_id)!=0){
                academicDao.NoDeletePublication(scholat_publication_id);
                continue;
            }else if(academicDao.publicationExitIf(scholat_publication_id)!=0){
                continue;
            }
            // System.out.println("p1p1="+tt);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            t.put("updateTime" , df.format(new Date()));
            t.put("unitId",unit_id);
            t.put("scholat_username",scholat_username);
            t.put("datetime" , t.getString("date"));
            t.put("scholat_publication_id",t.getLongValue("id"));
            t.put("title",t.getString("content"));
            System.out.println("ttt="+t);
            academicDao.addPublication(t);
        }

        //    System.out.println("jsonObjectjsonObject="+jsonObject);
        return CommonUtil.successJson();
    }
    @Override
    public JSONObject addPaper(JSONObject jsonObject) {

        Long scholat_paper_id=jsonObject.getLongValue("scholat_paper_id");
        if(academicDao.paperExitIf(scholat_paper_id)!=0)
            return CommonUtil.successJson();
        int flagN = academicDao.paperDeleteExitIf(scholat_paper_id);//判断添加的论文是否原来删除过
        if(flagN!=0){
            academicDao.NoDeletePaper(scholat_paper_id);//将论文由删除状态改为已添加
        }else{
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            jsonObject.put("updateTime" , df.format(new Date()));
            String datetime = jsonObject.getString("date");
            datetime=datetime.replace(".null","");
            //"datetime" -> "2018-12-31"
//        jsonObject.put("hot" ,1000);
            jsonObject.put("datetime",datetime);
         //   System.out.println("--------datetime--------" + datetime);
            //从新版本输入时间
//            if (datetime!=null && datetime.indexOf(".") == -1) {
//                String[] split = datetime.split("-");
//                StringBuilder sb = new StringBuilder();
//                sb.append(split[0]);
//                sb.append('.');
//                sb.append(split[1]);
//                datetime = sb.toString();
//            }
            academicDao.addPaper(jsonObject);
        }

        return CommonUtil.successJson();
    }
    @Override
    public JSONObject addPublication(JSONObject jsonObject) {
        Long scholat_publication_id=jsonObject.getLongValue("scholat_publication_id");
        int flagN = academicDao.publicationDeleteExitIf(scholat_publication_id);//判断添加的著作是否原来删除过
        if(flagN!=0){
            academicDao.NoDeletePublication(scholat_publication_id);//将著作由删除状态改为已添加
        }else{
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            jsonObject.put("updateTime" , df.format(new Date()));
            jsonObject.put("datetime",jsonObject.getString("date"));
            academicDao.addPublication(jsonObject);
        }

        return CommonUtil.successJson();
    }

    @Override
    public JSONObject updatePaper(JSONObject jsonObject) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        jsonObject.put("updateTime" , df.format(new Date()));
        String datetime = jsonObject.getString("datetime");
        //"datetime" -> "2018-12-31"

        System.out.println("--------datetime--------" + datetime);
        //过滤非法字符
        datetime = datetime.replace("-undefined-01","");

        System.out.println("--------datetime--------" + datetime);
        //从新版本输入时间
        if (datetime!=null && datetime.indexOf(".") == -1) {
            String[] split = datetime.split("-");
            StringBuilder sb = new StringBuilder();
            sb.append(split[0]);
            sb.append('.');
            sb.append(split[1]);
            datetime = sb.toString();
        }
        jsonObject.put("datetime",datetime);
        academicDao.updatePaper(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject getPatent(Long id) {
        JSONObject patent = academicDao.getPatent(id);
        return patent;
    }

    @Override
    public JSONObject deletePatent(JSONObject jsonObject) {
        academicDao.removePatent(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject addProject(JSONObject jsonObject) {
        Long scholat_project_id=jsonObject.getLongValue("scholat_project_id");
        if(academicDao.projectExitIf(scholat_project_id)!=0)
            return CommonUtil.successJson();
        int flagN = academicDao.projectDeleteExitIf(scholat_project_id);//判断添加的论文是否原来删除过
        if(flagN!=0){
            academicDao.NoDeleteProject(scholat_project_id);//将论文由删除状态改为已添加
        }else {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            jsonObject.put("updateTime", df.format(new Date()));
            System.out.println("-----test----" + jsonObject);
            String application=jsonObject.getString("application");
            application = application.replace("项目成员：", "");
            application=application.replace("*"," ");
            jsonObject.put("application",application);
            String endDate = jsonObject.getString("endDate");
            endDate=endDate.replace("-","");
           if(endDate.substring(0, 1).equals(".")){ endDate=endDate.replaceFirst(".","");}
            endDate=endDate.replace("null.null","");
            System.out.println("--------datetime--------" + endDate);
            jsonObject.put("endDate", endDate);
            academicDao.addProject(jsonObject);
        }
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject updatePatent(JSONObject jsonObject) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        jsonObject.put("updateTime" , df.format(new Date()));
        String datetime = jsonObject.getString("datetime");
        //"datetime" -> "2018-12-31"

        System.out.println("--------datetime--------" + datetime);
        //过滤非法字符
        datetime = datetime.replace("-undefined-01","");

        System.out.println("--------datetime--------" + datetime);
        //从新版本输入时间
        if (datetime!=null && datetime.indexOf(".") == -1) {
            String[] split = datetime.split("-");
            StringBuilder sb = new StringBuilder();
            sb.append(split[0]);
            sb.append('.');
            sb.append(split[1]);
            datetime = sb.toString();
        }
        jsonObject.put("datetime",datetime);
        academicDao.updatePatent(jsonObject);

        //处理关联成员
        if (jsonObject.getString("tIds") !="[]") {
            String tIds = jsonObject.getString("tIds").replace("[", "").replace("]", "").replace(" ", "");
            String[] split = tIds.split(",");
            long id = jsonObject.getLongValue("id");
            ArrayList<Long> list = new ArrayList<>();
            for (int k = 0; k < split.length; k++) {
                list.add(Long.valueOf(split[k]));
            }
            System.out.println("id + list" + id + list);
            academicDao.addPaperTeacher(id, list);
        }

        return CommonUtil.successJson();
    }

    @Override
    public JSONObject getProject(Long id) {
        return academicDao.getProject(id);
    }

    @Override
    public JSONObject getPublication(Long id) {
        return academicDao.getPublication(id);
    }


    @Override
    public JSONObject deleteProject(JSONObject jsonObject) {
        academicDao.removeProject(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject addPatent(JSONObject jsonObject) {
        Long scholat_patent_id=jsonObject.getLongValue("scholat_patent_id");
        if(academicDao.patentExitIf(scholat_patent_id)!=0)
            return CommonUtil.successJson();
        int flagN = academicDao.patentDeleteExitIf(scholat_patent_id);//判断添加的专利是否原来删除过
        if(flagN!=0){
            academicDao.NoDeletePatent(scholat_patent_id);//将利由删除状态改为已添加
        }else {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            jsonObject.put("updateTime" , df.format(new Date()));
            jsonObject.put("datetime",jsonObject.getString("date"));
            academicDao.addPatent(jsonObject);
        }
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject updateProject(JSONObject jsonObject) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        jsonObject.put("updateTime" , df.format(new Date()));
        System.out.println("-----test----" + jsonObject);
        String startDate = jsonObject.getString("startDate");
        startDate = startDate.replace("-undefined-01","");

        String endDate = jsonObject.getString("endDate");
        endDate = endDate.replace("-undefined-01","");
        //"datetime" -> "2018-12-31"

        System.out.println("--------datetime--------" + startDate);
        //从新版本输入开始时间
        if (startDate!=null && startDate.indexOf(".") == -1) {
            String[] split = startDate.split("-");
            StringBuilder sb = new StringBuilder();
            sb.append(split[0]);
            sb.append('.');
            sb.append(split[1]);
            startDate = sb.toString();

        }

        //从新版本输入结束时间
        if (endDate!=null && endDate.indexOf(".") == -1) {
            String[] split = endDate.split("-");
            StringBuilder sb = new StringBuilder();
            sb.append(split[0]);
            sb.append('.');
            sb.append(split[1]);
            endDate = sb.toString();

        }

        jsonObject.put("startDate",startDate);
        jsonObject.put("endDate",endDate);
        academicDao.updateProject(jsonObject);


        //处理关联成员
        if (jsonObject.getString("tIds") !="[]") {
            String tIds = jsonObject.getString("tIds").replace("[", "").replace("]", "").replace(" ", "");
            String[] split = tIds.split(",");
            long id = jsonObject.getLongValue("id");
            ArrayList<Long> list = new ArrayList<>();
            for (int k = 0; k < split.length; k++) {
                list.add(Long.valueOf(split[k]));
            }
            System.out.println("id + list" + id + list);
            academicDao.addPaperTeacher(id, list);
        }

        return CommonUtil.successJson();
    }
    @Override
    public JSONObject updatePublication(JSONObject jsonObject) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        jsonObject.put("updateTime" , df.format(new Date()));
        System.out.println("-----test----" + jsonObject);
        String datetime = jsonObject.getString("datetime");
        datetime = datetime.replace("-undefined-01","");
        //"datetime" -> "2018-12-31"
        System.out.println("--------datetime--------" + datetime);
        //从新版本输入开始时间
        if (datetime!=null && datetime.indexOf(".") == -1) {
            String[] split = datetime.split("-");
            StringBuilder sb = new StringBuilder();
            sb.append(split[0]);
            sb.append('.');
            sb.append(split[1]);
            datetime = sb.toString();
        }
        jsonObject.put("datetime",datetime);
        academicDao.updatePublication(jsonObject);
        return CommonUtil.successJson();
    }


    @Override
    public void exportPaper(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject json=CommonUtil.request2Json(request);
        String aa = URLDecoder.decode(json.getString("data"), "utf-8");
        JSONObject json2 =JSONObject.parseObject(new String(aa));//换成json格式
        String beginTime=json2.getString("valueStart");
        String endTime=json2.getString("valueEnd");
        // System.out.println("beginTime="+beginTime+endTime);转换日期格式
        if(beginTime!=null&&beginTime.length()!=0){
            beginTime = beginTime.replace("-", ".").substring(0,10);
            json2.put("beginTime", beginTime);
        }
        if(endTime!=null&&endTime.length()!=0){
            endTime = endTime.replace("-", ".").substring(0,10);
            json2.put("endTime", endTime);
        }
        Long unitId = json2.getLongValue("unitId");
        json2.put("unitId", unitId);
        List<JSONObject> list=academicDao.getPaperteacher(json2);
        List<paperExcel> listPaper = new ArrayList<paperExcel>();
        for (int i = 0; i <list.size() ; i++) {
            JSONObject paper=list.get(i);
            paperExcel data = new paperExcel();
            if((paper.getIntValue("papertype"))==0){
                data.setPapertype("期刊论文");
            }else{
                data.setPapertype("会议论文");
            }
            data.setTitle(paper.getString("title"));
            data.setAuthors(paper.getString("authors"));
            data.setSource(paper.getString("source"));
            data.setDatetime(paper.getString("datetime"));
            data.setKeyword(paper.getString("keyword"));
            listPaper.add(data);
        }
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("教师论文信息", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), paperExcel.class).autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(listPaper);
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
    public void exportPaper2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject json=CommonUtil.request2Json(request);
        String aa = URLDecoder.decode(json.getString("data"), "utf-8");
        JSONObject json2 =JSONObject.parseObject(new String(aa));//换成json格式
        String beginTime=json2.getString("valueStart");
        String endTime=json2.getString("valueEnd");
        // System.out.println("beginTime="+beginTime+endTime);转换日期格式
        if(beginTime!=null&&beginTime.length()!=0){
            beginTime = beginTime.replace("-", ".").substring(0,10);
            json2.put("beginTime", beginTime);
        }
        if(endTime!=null&&endTime.length()!=0){
            endTime = endTime.replace("-", ".").substring(0,10);
            json2.put("endTime", endTime);
        }
        Long unitId = json2.getLongValue("unitId");
        json2.put("unitId", unitId);
        List<JSONObject> list=academicDao.getSearchPaper(json2);
        List<paperExcel> listPaper = new ArrayList<paperExcel>();
        for (int i = 0; i <list.size() ; i++) {
            JSONObject paper=list.get(i);
            paperExcel data = new paperExcel();
            if((paper.getIntValue("papertype"))==0){
                data.setPapertype("期刊论文");
            }else{
                data.setPapertype("会议论文");
            }
            data.setTitle(paper.getString("title"));
            data.setAuthors(paper.getString("authors"));
            data.setSource(paper.getString("source"));
            data.setDatetime(paper.getString("datetime"));
            data.setKeyword(paper.getString("keyword"));
            listPaper.add(data);
        }
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("教师论文信息", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), paperExcel.class).autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(listPaper);
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
    public void exportProject(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject json=CommonUtil.request2Json(request);
        String aa = URLDecoder.decode(json.getString("data"), "utf-8");
        JSONObject json2 =JSONObject.parseObject(new String(aa));//换成json格式
        String beginTime=json2.getString("valueStart");
        String endTime=json2.getString("valueEnd");
        // System.out.println("beginTime="+beginTime+endTime);转换日期格式
        if(beginTime!=null&&beginTime.length()!=0){
            beginTime = beginTime.replace("-", ".").substring(0,10);
            json2.put("beginTime", beginTime);
        }
        if(endTime!=null&&endTime.length()!=0){
            endTime = endTime.replace("-", ".").substring(0,10);
            json2.put("endTime", endTime);
        }
        Long unitId = json2.getLongValue("unitId");
        json2.put("unitId", unitId);
        List<JSONObject> list=academicDao.getProjectteacher(json2);
        List<projectExcel> listProject = new ArrayList<projectExcel>();
        for (int i = 0; i <list.size() ; i++) {
            JSONObject project=list.get(i);
            projectExcel data = new projectExcel();
            data.setTitle(project.getString("title"));
            data.setApplication(project.getString("application"));
            data.setProjectType(project.getString("project_type"));
            data.setFunding(project.getString("funding"));
            data.setProjectNumber(project.getString("project_number"));
            data.setStartDate(project.getString("start_date"));
            data.setEndDate(project.getString("end_date"));
            listProject.add(data);
        }
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("教师项目信息", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), projectExcel.class).autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(listProject);
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
    public void exportTeacherPaper(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject json=CommonUtil.request2Json(request);
        String aa = URLDecoder.decode(json.getString("data"), "utf-8");
        JSONObject json2 =JSONObject.parseObject(new String(aa));//换成json格式
        String beginTime=json2.getString("valueStart");
        String endTime=json2.getString("valueEnd");
        // System.out.println("beginTime="+beginTime+endTime);转换日期格式
        if(beginTime!=null&&beginTime.length()!=0){
            beginTime = beginTime.replace("-", ".").substring(0,10);
            json2.put("beginTime", beginTime);
        }
        if(endTime!=null&&endTime.length()!=0){
            endTime = endTime.replace("-", ".").substring(0,10);
            json2.put("endTime", endTime);
        }
        Long unitId = json2.getLongValue("unitId");
        json2.put("unitId", unitId);
        String tIds=json2.getString("tIds");
        String[] ArrIds = tIds.substring(1, tIds.length() - 1).split(",");
        List<paperExcel> listPaper = new ArrayList<paperExcel>();
        for (int i = 0; i <= ArrIds.length-1; i++) {
            json2.put("id",Integer.parseInt(ArrIds[i]));
            System.out.println( json2);
            List<JSONObject> list=academicDao.getSearchTeacherPaper(json2);
            for (int j = 0; j <list.size() ; j++) {
                JSONObject paper=list.get(j);
                paperExcel data = new paperExcel();
                if((paper.getIntValue("papertype"))==0){
                    data.setPapertype("期刊论文");
                }else{
                    data.setPapertype("会议论文");
                }
                data.setTitle(paper.getString("title"));
                data.setAuthors(paper.getString("authors"));
                data.setSource(paper.getString("source"));
                data.setDatetime(paper.getString("datetime"));
                data.setKeyword(paper.getString("keyword"));
                listPaper.add(data);
            }
        }
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("教师论文信息", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), paperExcel.class).autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(listPaper);
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
    public void exportTeacherProject(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject json=CommonUtil.request2Json(request);
        String aa = URLDecoder.decode(json.getString("data"), "utf-8");
        JSONObject json2 =JSONObject.parseObject(new String(aa));//换成json格式
        String beginTime=json2.getString("valueStart");
        String endTime=json2.getString("valueEnd");
        // System.out.println("beginTime="+beginTime+endTime);转换日期格式
        if(beginTime!=null&&beginTime.length()!=0){
            beginTime = beginTime.replace("-", ".").substring(0,10);
            json2.put("beginTime", beginTime);
        }
        if(endTime!=null&&endTime.length()!=0){
            endTime = endTime.replace("-", ".").substring(0,10);
            json2.put("endTime", endTime);
        }
        Long unitId = json2.getLongValue("unitId");
        json2.put("unitId", unitId);
        String tIds=json2.getString("tIds");
        String[] ArrIds = tIds.substring(1, tIds.length() - 1).split(",");
        List<projectExcel> listProject = new ArrayList<projectExcel>();
        for (int i = 0; i <= ArrIds.length-1; i++) {
            json2.put("id",Integer.parseInt(ArrIds[i]));
            System.out.println( json2);
            List<JSONObject> list=academicDao.getSearchTeacherProject(json2);
            for (int j = 0; j <list.size() ; j++) {
                JSONObject project=list.get(j);
                projectExcel data = new projectExcel();
                data.setTitle(project.getString("title"));
                data.setApplication(project.getString("application"));
                data.setProjectType(project.getString("project_type"));
                data.setFunding(project.getString("funding"));
                data.setProjectNumber(project.getString("project_number"));
                data.setStartDate(project.getString("start_date"));
                data.setEndDate(project.getString("end_date"));
                listProject.add(data);
            }
        }
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("教师项目信息", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), projectExcel.class).autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(listProject);
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
    public void exportTeacherPatent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject json=CommonUtil.request2Json(request);
        String aa = URLDecoder.decode(json.getString("data"), "utf-8");
        JSONObject json2 =JSONObject.parseObject(new String(aa));//换成json格式
        String beginTime=json2.getString("valueStart");
        String endTime=json2.getString("valueEnd");
        // System.out.println("beginTime="+beginTime+endTime);转换日期格式
        if(beginTime!=null&&beginTime.length()!=0){
            beginTime = beginTime.replace("-", ".").substring(0,10);
            json2.put("beginTime", beginTime);
        }
        if(endTime!=null&&endTime.length()!=0){
            endTime = endTime.replace("-", ".").substring(0,10);
            json2.put("endTime", endTime);
        }
        Long unitId = json2.getLongValue("unitId");
        json2.put("unitId", unitId);
        String tIds=json2.getString("tIds");
        String[] ArrIds = tIds.substring(1, tIds.length() - 1).split(",");
        List<patentExcel> listPatent = new ArrayList<patentExcel>();
        for (int i = 0; i <= ArrIds.length-1; i++) {
            json2.put("id",Integer.parseInt(ArrIds[i]));
            System.out.println( json2);
            List<JSONObject> list=academicDao.getSearchTeacherPatent(json2);
            for (int j = 0; j <list.size() ; j++) {
                JSONObject project=list.get(j);
                patentExcel data = new patentExcel();
                data.setTitle(project.getString("title"));
                data.setAuthors(project.getString("authors"));
                data.setPatent_type(project.getString("patentType"));
                data.setPatent_number(project.getString("patentNumber"));
                data.setDatetime(project.getString("datetime"));
                listPatent.add(data);
            }
        }
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("教师专利信息", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), patentExcel.class).autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(listPatent);
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
    public void exportProject2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject json=CommonUtil.request2Json(request);
        String aa = URLDecoder.decode(json.getString("data"), "utf-8");
        JSONObject json2 =JSONObject.parseObject(new String(aa));//换成json格式
        String beginTime=json2.getString("valueStart");
        String endTime=json2.getString("valueEnd");
        // System.out.println("beginTime="+beginTime+endTime);转换日期格式
        if(beginTime!=null&&beginTime.length()!=0){
            beginTime = beginTime.replace("-", ".").substring(0,10);
            json2.put("beginTime", beginTime);
        }
        if(endTime!=null&&endTime.length()!=0){
            endTime = endTime.replace("-", ".").substring(0,10);
            json2.put("endTime", endTime);
        }
        Long unitId = json2.getLongValue("unitId");
        json2.put("unitId", unitId);
        List<JSONObject> list=academicDao.getSearchProject(json2);
        List<projectExcel> listProject = new ArrayList<projectExcel>();
        for (int i = 0; i <list.size() ; i++) {
            JSONObject project=list.get(i);
            projectExcel data = new projectExcel();
            data.setTitle(project.getString("title"));
            data.setApplication(project.getString("application"));
            data.setProjectType(project.getString("project_type"));
            data.setFunding(project.getString("funding"));
            data.setProjectNumber(project.getString("project_number"));
            data.setStartDate(project.getString("start_date"));
            data.setEndDate(project.getString("end_date"));
            listProject.add(data);
        }
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("教师项目信息", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), projectExcel.class).autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(listProject);
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
    public void exportPatent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject json=CommonUtil.request2Json(request);
        String aa = URLDecoder.decode(json.getString("data"), "utf-8");
        JSONObject json2 =JSONObject.parseObject(new String(aa));//换成json格式
        String beginTime=json2.getString("valueStart");
        String endTime=json2.getString("valueEnd");
        // System.out.println("beginTime="+beginTime+endTime);转换日期格式
        if(beginTime!=null&&beginTime.length()!=0){
            beginTime = beginTime.replace("-", ".").substring(0,10);
            json2.put("beginTime", beginTime);
        }
        if(endTime!=null&&endTime.length()!=0){
            endTime = endTime.replace("-", ".").substring(0,10);
            json2.put("endTime", endTime);
        }
        Long unitId = json2.getLongValue("unitId");
        json2.put("unitId", unitId);
        List<JSONObject> list=academicDao.getPatentteacher(json2);
        List<patentExcel> listPatent = new ArrayList<patentExcel>();
        for (int i = 0; i <list.size() ; i++) {
            JSONObject patent=list.get(i);
            patentExcel data = new patentExcel();
            data.setTitle(patent.getString("title"));
            data.setAuthors(patent.getString("authors"));
            data.setPatent_type(patent.getString("patentType"));
            data.setDatetime(patent.getString("datetime"));
            data.setPatent_number(patent.getString("patentNumber"));
            listPatent.add(data);
        }
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("教师专利信息", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), patentExcel.class).autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(listPatent);
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
    public void exportPatent2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject json=CommonUtil.request2Json(request);
        String aa = URLDecoder.decode(json.getString("data"), "utf-8");
        JSONObject json2 =JSONObject.parseObject(new String(aa));//换成json格式
        String beginTime=json2.getString("valueStart");
        String endTime=json2.getString("valueEnd");
        // System.out.println("beginTime="+beginTime+endTime);转换日期格式
        if(beginTime!=null&&beginTime.length()!=0){
            beginTime = beginTime.replace("-", ".").substring(0,10);
            json2.put("beginTime", beginTime);
        }
        if(endTime!=null&&endTime.length()!=0){
            endTime = endTime.replace("-", ".").substring(0,10);
            json2.put("endTime", endTime);
        }
        Long unitId = json2.getLongValue("unitId");
        json2.put("unitId", unitId);
        List<JSONObject> list=academicDao.getSearchPatent(json2);
        List<patentExcel> listPatent = new ArrayList<patentExcel>();
        for (int i = 0; i <list.size() ; i++) {
            JSONObject patent=list.get(i);
            patentExcel data = new patentExcel();
            data.setTitle(patent.getString("title"));
            data.setAuthors(patent.getString("authors"));
            data.setPatent_type(patent.getString("patentType"));
            data.setDatetime(patent.getString("datetime"));
            data.setPatent_number(patent.getString("patentNumber"));
            listPatent.add(data);
        }
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("教师专利信息", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), patentExcel.class).autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(listPatent);
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
    public void exportTeacherPublication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject json=CommonUtil.request2Json(request);
        String aa = URLDecoder.decode(json.getString("data"), "utf-8");
        JSONObject json2 =JSONObject.parseObject(new String(aa));//换成json格式
        String beginTime=json2.getString("valueStart");
        String endTime=json2.getString("valueEnd");
        // System.out.println("beginTime="+beginTime+endTime);转换日期格式
        if(beginTime!=null&&beginTime.length()!=0){
            beginTime = beginTime.replace("-", ".").substring(0,10);
            json2.put("beginTime", beginTime);
        }
        if(endTime!=null&&endTime.length()!=0){
            endTime = endTime.replace("-", ".").substring(0,10);
            json2.put("endTime", endTime);
        }
        Long unitId = json2.getLongValue("unitId");
        json2.put("unitId", unitId);
        String tIds=json2.getString("tIds");
        String[] ArrIds = tIds.substring(1, tIds.length() - 1).split(",");
        List<publicationExcel> listPublication = new ArrayList<publicationExcel>();
        for (int i = 0; i <= ArrIds.length-1; i++) {
            json2.put("id",Integer.parseInt(ArrIds[i]));
            System.out.println( json2);
            List<JSONObject> list=academicDao.getSearchTeacherPublication(json2);
            for (int j = 0; j <list.size() ; j++) {
                JSONObject publication=list.get(j);
                publicationExcel data = new publicationExcel();
                data.setTitle(publication.getString("title"));
                data.setAuthors(publication.getString("authors"));
                data.setPress(publication.getString("press"));
                data.setCitation(publication.getString("citation"));
                data.setDatetime(publication.getString("datetime"));
                listPublication.add(data);
            }
        }
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("教师著作信息", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), publicationExcel.class).autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(listPublication);
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
    public void exportPublication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject json=CommonUtil.request2Json(request);
        String aa = URLDecoder.decode(json.getString("data"), "utf-8");
        JSONObject json2 =JSONObject.parseObject(new String(aa));//换成json格式
        String beginTime=json2.getString("valueStart");
        String endTime=json2.getString("valueEnd");
        // System.out.println("beginTime="+beginTime+endTime);转换日期格式
        if(beginTime!=null&&beginTime.length()!=0){
            beginTime = beginTime.replace("-", ".").substring(0,10);
            json2.put("beginTime", beginTime);
        }
        if(endTime!=null&&endTime.length()!=0){
            endTime = endTime.replace("-", ".").substring(0,10);
            json2.put("endTime", endTime);
        }
        Long unitId = json2.getLongValue("unitId");
        json2.put("unitId", unitId);
        List<JSONObject> list=academicDao.getPublicationteacher(json2);
        List<publicationExcel> listPublication = new ArrayList<publicationExcel>();
        for (int i = 0; i <list.size() ; i++) {
            JSONObject patent=list.get(i);
            publicationExcel data = new publicationExcel();
            data.setTitle(patent.getString("title"));
            data.setAuthors(patent.getString("authors"));
            data.setCitation(patent.getString("citation"));
            data.setPress(patent.getString("press"));
            data.setDatetime(patent.getString("datetime"));
            listPublication.add(data);
        }
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("教师著作信息", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), publicationExcel.class).autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(listPublication);
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
    public void exportPublication2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject json=CommonUtil.request2Json(request);
        String aa = URLDecoder.decode(json.getString("data"), "utf-8");
        JSONObject json2 =JSONObject.parseObject(new String(aa));//换成json格式
        String beginTime=json2.getString("valueStart");
        String endTime=json2.getString("valueEnd");
        // System.out.println("beginTime="+beginTime+endTime);转换日期格式
        if(beginTime!=null&&beginTime.length()!=0){
            beginTime = beginTime.replace("-", ".").substring(0,10);
            json2.put("beginTime", beginTime);
        }
        if(endTime!=null&&endTime.length()!=0){
            endTime = endTime.replace("-", ".").substring(0,10);
            json2.put("endTime", endTime);
        }
        Long unitId = json2.getLongValue("unitId");
        json2.put("unitId", unitId);
        List<JSONObject> list=academicDao.getSearchPublication(json2);
        List<publicationExcel> listPublication = new ArrayList<publicationExcel>();
        for (int i = 0; i <list.size() ; i++) {
            JSONObject publication=list.get(i);
            publicationExcel data = new publicationExcel();
            data.setTitle(publication.getString("title"));
            data.setAuthors(publication.getString("authors"));
            data.setPress(publication.getString("press"));
            data.setCitation(publication.getString("citation"));
            data.setDatetime(publication.getString("datetime"));
            listPublication.add(data);
        }
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("教师著作信息", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), publicationExcel.class).autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(listPublication);
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
    public int getPaperTotal(long id) {

        return academicDao.countPaper(id);
    }
    @Override
    public int getProjectTotal(long id) {

        return academicDao.countProject(id);
    }
    @Override
    public int getPatentTotal(long id) {

        return academicDao.countPatent(id);
    }
    @Override
    public int getPublicationTotal(long id) {

        return academicDao.countPublication(id);
    }
}
