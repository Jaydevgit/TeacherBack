package com.teacher.scholat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.dao.AcademicDao;
import com.teacher.scholat.dao.TeacherDao;
import com.teacher.scholat.dao.UnitDao;
//import com.teacher.scholat.repository.PaperRepository;
//import com.teacher.scholat.repository.PatentRepository;
//import com.teacher.scholat.repository.ProjectRepository;
import com.teacher.scholat.service.AcademicService;
import com.teacher.scholat.util.CommonUtil;
//import com.teacher.scholat.util.EditDistance;
//import com.teacher.scholat.util.TextUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import com.teacher.scholat.util.GetScholatAcademic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AcademicServiceImpl implements AcademicService {

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
            if (list.size()!=0){
                for (int k = 0;k < list1.size();k++){
                    //Long必须要用longValue作比较，否则只是比较地址
                    if (id.longValue() == list1.get(k).longValue())
                    {
                        jsonObject1.put("exist",1);
                        break;
                    }
                }
            }
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
        return null;
    }

    @Override
    public JSONObject getPaper(Long id) {
        JSONObject paper = academicDao.getPaper(id);
        ArrayList<JSONObject> teacherList = (ArrayList<JSONObject>) paper.get("teacherList");
        if (teacherList.get(0).getString("tCount").equals("0"))
            paper.put("teacherList" ,new ArrayList<>());
        System.out.println("paper" + paper);
        String similarPaper = paper.getString("similarPaper");
        System.out.println("similarPaper" + similarPaper);
        List<JSONObject> similarList = new ArrayList<>();
        if (similarPaper != null && similarPaper.indexOf(",")!= -1){
        String[] split = similarPaper.split(",");
        for (int i = 0;i < split.length;i++){
            Long ad = Long.valueOf(split[i]);
            System.out.println(ad);
            JSONObject paper1 = academicDao.getPaper(ad);
            similarList.add(paper1);
            }
        }
        else if (similarPaper != null )
        {       Long ad = Long.valueOf(similarPaper);
                System.out.println(ad);
                JSONObject paper1 = academicDao.getPaper(Long.valueOf(ad));
                similarList.add(paper1);

        }
        paper.put("similarList" , similarList);

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
    public JSONObject listProject(JSONObject jsonObject) {
        System.out.println("前端传过来的论文列表要求为: " + jsonObject);
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

//    @Override
//    public JSONObject identifyTeacher(JSONObject jsonObject) {
//        String authors = jsonObject.getString("authors").replace(" ","").replace("，",",").replace("、",",");
//        long unitId = jsonObject.getLongValue("unitId");
//        System.out.println(authors);
//        String[] split = authors.split(",");
//        List<JSONObject> list = new ArrayList<>();
//        for (int i = 0;i < split.length;i++){
//            JSONObject searchJson = new JSONObject();
//            searchJson.put("unitId" , unitId);
//            searchJson.put("name", split[i]);
//            List<JSONObject> infoByName = teacherDao.getInfoByName(searchJson);
//            if (!infoByName.isEmpty())
//            for (int j = 0;j < infoByName.size();j++){
//                list.add(infoByName.get(j));
//            }
//        }
//        System.out.println(list);
//        return CommonUtil.successPage(list);
//    }

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
            if(academicDao.paperExitIf(scholat_paper_id)!=0){
                continue;
            }
            System.out.println("p1p1="+tt);
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
        System.out.println("unit_id="+unit_id);
        String scholat_username = jsonObject.getString("scholat_username");
        academicDao.addProject(jsonObject);
        System.out.println("jsonObjectjsonObject="+jsonObject);
        return CommonUtil.successJson();
    }
    @Override
    public JSONObject addPaper(JSONObject jsonObject) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        jsonObject.put("updateTime" , df.format(new Date()));
        String datetime = jsonObject.getString("datetime");
        //"datetime" -> "2018-12-31"
//        jsonObject.put("hot" ,1000);
        System.out.println("--------datetime--------" + datetime);
        //从新版本输入时间
        if (datetime!=null && datetime.indexOf(".") == -1) {
            String[] split = datetime.split("-");
            StringBuilder sb = new StringBuilder();
            sb.append(split[0]);
            sb.append('.');
            sb.append(split[1]);
            datetime = sb.toString();

            jsonObject.put("datetime",datetime);
        }

        academicDao.addPaper(jsonObject);
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
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        jsonObject.put("updateTime" , df.format(new Date()));
        System.out.println("-----test----" + jsonObject);
        String startDate = jsonObject.getString("startDate");
        String endDate = jsonObject.getString("endDate");
        //"datetime" -> "2018-12-31"

        System.out.println("--------datetime--------" + endDate);
        //从新版本输入开始时间
        if (startDate!=null && startDate.indexOf(".") == -1) {
            String[] split = startDate.split("-");
            StringBuilder sb = new StringBuilder();
            sb.append(split[0]);
            sb.append('.');
            sb.append(split[1]);
            startDate = sb.toString();

            jsonObject.put("startDate",startDate);
        }

        //从旧版本输入开始时间
        if (startDate!=null && startDate.indexOf(".") != -1) {
            String[] split = startDate.split("\\.");
            StringBuilder sb = new StringBuilder();
            if (split[0]!= "-")
                sb.append(split[0]);
            else
                sb.append("1800");
            sb.append('.');
            if (split[1]!= "-")
                sb.append(split[1]);
            else
                sb.append("01");
            startDate = sb.toString();
            jsonObject.put("startDate",startDate);
        }

        //从新版本输入结束时间
        if (endDate!=null && endDate.indexOf(".") == -1) {
            String[] split = endDate.split("-");
            StringBuilder sb = new StringBuilder();
            sb.append(split[0]);
            sb.append('.');
            sb.append(split[1]);
            endDate = sb.toString();

            jsonObject.put("endDate",endDate);
        }
        System.out.println("endDate:"+endDate);
        //从旧版本输入结束时间
        if (!endDate.equals(".") && endDate.indexOf(".") != -1) {
            String[] split = endDate.split("\\.");
            StringBuilder sb = new StringBuilder();
            if (split[0]!= "null" && split[0] != "-")
                sb.append(split[0]);
            else
                sb.append("1800");
            sb.append('.');
            if (split[1]!= "null" && split[1] != "-")
                sb.append(split[1]);
            else
                sb.append("01");
            endDate = sb.toString();
        }

        if (endDate.equals("."))
            endDate = "1800.01";
        jsonObject.put("endDate",endDate);
        academicDao.addProject(jsonObject);
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
    public JSONObject deleteProject(JSONObject jsonObject) {
        academicDao.removeProject(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject addPatent(JSONObject jsonObject) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        jsonObject.put("updateTime" , df.format(new Date()));
        String datetime = jsonObject.getString("datetime");
        //"datetime" -> "2018-12-31"

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
        //从旧版本输入时间
        if (datetime!="." && datetime.indexOf(".") != -1) {
            String[] split = datetime.split("\\.");
            StringBuilder sb = new StringBuilder();
            if (split[0]!= "null" && split[0] != "-")
                sb.append(split[0]);
            else
                sb.append("1800");
            sb.append('.');
            if (split[1]!= "null" && split[1] != "-")
                sb.append(split[1]);
            else
                sb.append("01");
            datetime = sb.toString();
        }
        jsonObject.put("datetime",datetime);
        academicDao.addPatent(jsonObject);
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



}