package com.teacher.scholat.controller;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.service.AcademicService;
import com.teacher.scholat.service.UnitService;
import com.teacher.scholat.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/statistic")
public class Statistic {
    @Autowired
    AcademicService academicService;
    @Autowired
    UnitService unitService;

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

}
