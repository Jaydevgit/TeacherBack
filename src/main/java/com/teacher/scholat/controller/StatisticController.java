package com.teacher.scholat.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.dao.AcademicDao;
import com.teacher.scholat.dao.TeacherDao;
import com.teacher.scholat.model.Teacher;
import com.teacher.scholat.service.AcademicService;
import com.teacher.scholat.service.TeacherService;
import com.teacher.scholat.service.UnitService;
import com.teacher.scholat.util.CommonUtil;
import com.teacher.scholat.util.GetScholatAcademic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/statistic")
public class StatisticController {
    @Autowired
    AcademicService academicService;
    @Autowired
    UnitService unitService;
    @Resource
    private TeacherDao teacherDao;
    @Resource
    AcademicDao academicDao;

    //统计所有个数
    @GetMapping("/total")
    @Transactional(rollbackFor = Exception.class)
    public void total(){
        List<JSONObject> allUnit = unitService.getAllUnit();
        for (int i = 0; i <= allUnit.size()-1; i++) {
            int unitId = allUnit.get(i).getIntValue("id");
            int paperTotal=academicService.getPaperTotal(unitId);
            int projectTotal=academicService.getProjectTotal(unitId);
            int patentTotal=academicService.getPatentTotal(unitId);
            int publicationTotal=academicService.getPublicationTotal(unitId);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("unitId",unitId);
            jsonObject.put("paperTotal",paperTotal);
            jsonObject.put("projectTotal",projectTotal);
            jsonObject.put("patentTotal",patentTotal);
            jsonObject.put("publicationTotal",publicationTotal);
            academicService.deleteAllStatistic(unitId);
            academicService.addAllStatistic(jsonObject);
            System.out.println("学院"+unitId+"，总论文数为"+paperTotal
                    +"，总项目数为"+projectTotal
                    +"，总专利数为"+patentTotal
                    +"，总著作数为"+publicationTotal);
        }
    }

    //采集所有论文
    @GetMapping("/getAllPaper")
    @Transactional(rollbackFor = Exception.class)
    public void getAllPaper(){
        List<JSONObject> allTeacher= teacherDao.listTeacherAllUnit();
        for (int i = 0; i < allTeacher.size(); i++) {
            String scholatUsername=allTeacher.get(i).getString("tScholat_username");
            Integer unitId=allTeacher.get(i).getIntValue("tUnitId");
            JSONObject jsonObject=new JSONObject();
            //获取所有教师学者网用户名及本地学院id
            jsonObject.put("scholat_username",scholatUsername);
            jsonObject.put("unitId",unitId);
          //  System.out.println(jsonObject);
            JSONArray jsonArray = GetScholatAcademic.getScholatPaperByUserName(scholatUsername);
            List<Long> list1 = academicDao.paperIdsByScholatname(jsonObject);
           // System.out.println("----------list1--------"+list1+"paperArray"+jsonArray);
            //List<JSONObject> paperAll = academicDao.listPaperAll(jsonObject);

            List<JSONObject> list = new ArrayList<>();
            if (jsonArray.toString().equals("[null]"))
                continue;
            //每一篇论文
            for (int n = 0;n < jsonArray.size();n++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(n);
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
                jsonObject1.put("scholat_username",scholatUsername);
                jsonObject1.put("unitId",unitId);
                jsonObject1.put("scholat_paper_id",jsonObject1.getLongValue("id"));
                if(jsonObject1.getString("type").equals("期刊论文")){
                    jsonObject1.put("papertype" , 0);
                }else{
                    jsonObject1.put("papertype" , 1);
                }
                System.out.println("===="+jsonObject1);
                academicService.addPaper(jsonObject1);
                list.add(jsonObject1);
            }

        }
    }

    //采集所有项目
    @GetMapping("/getAllProject")
    @Transactional(rollbackFor = Exception.class)
    public void getAllProject(){
        List<JSONObject> allTeacher= teacherDao.listTeacherAllUnit();
        for (int i = 0; i < allTeacher.size(); i++) {
            String scholatUsername=allTeacher.get(i).getString("tScholat_username");
            Integer unitId=allTeacher.get(i).getIntValue("tUnitId");
            JSONObject jsonObject=new JSONObject();
            //获取所有教师学者网用户名及本地学院id
            jsonObject.put("scholat_username",scholatUsername);
            jsonObject.put("unitId",unitId);
            //  System.out.println(jsonObject);
            JSONArray jsonArray = GetScholatAcademic.getScholatProjectByUserName(scholatUsername);
            // System.out.println("paperArray"+jsonArray);

            List<JSONObject> list = new ArrayList<>();
            if (jsonArray.toString().equals("[null]"))
                continue;
            //遍历每一项目
            for (int n = 0;n < jsonArray.size();n++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(n);
                String authors = jsonObject1.getString("authors");
                jsonObject1.put("application" , jsonObject1.getString("application"));
                jsonObject1.put("scholat_username",scholatUsername);
                jsonObject1.put("unitId",unitId);
                jsonObject1.put("scholat_project_id",jsonObject1.getLongValue("id"));
                jsonObject1.put("title",jsonObject1.getString("name"));
                jsonObject1.put("projectType",jsonObject1.getString("originAndId"));

                System.out.println("===="+jsonObject1);
                academicService.addProject(jsonObject1);
                list.add(jsonObject1);
            }

        }
    }

    //查询所有个数
    @GetMapping("/getTotal/{unitId}")
    public JSONObject getTotal(@PathVariable("unitId") long unitId){
        System.out.println("传来来的学院参数id为。。。。。。"+unitId);
        return CommonUtil.successJson(academicService.getTotal(unitId));
        }
}

