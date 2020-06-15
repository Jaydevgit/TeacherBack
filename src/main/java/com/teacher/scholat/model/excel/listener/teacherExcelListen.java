package com.teacher.scholat.model.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.model.excel.importTeacher;
import com.teacher.scholat.model.excel.teacherData;
import com.teacher.scholat.service.ManagerService;
import com.teacher.scholat.util.CommonUtil;
import com.teacher.scholat.util.PinyinUtil;
import com.teacher.scholat.util.constants.ErrorEnum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.teacher.scholat.util.PinyinUtil.getFullSpell;


public class teacherExcelListen extends AnalysisEventListener<importTeacher> {


    public ManagerService managerService;
    public int unitId;
    public String editName;
    public teacherExcelListen( ) {
        this.managerService = managerService;
    }
    public teacherExcelListen(ManagerService managerService,int  unitId,String editName) {
        this.managerService = managerService;
        this.unitId=unitId;
        this.editName=editName;
    }
    public static boolean isEmail(String string) {
        if (string == null)
            return false;
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(string);
        if (m.matches())
            return true;
        else
            return false;
    }
    public static boolean isPhone(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            return m.matches();
        }
    }
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
         return pattern.matcher(str).matches();
    }
    @Override
    public void invoke(importTeacher importTeacher, AnalysisContext analysisContext) {
        if(importTeacher==null){
            throw new Error("Excel导入数据为空");
        }
        System.out.println("aaaa"+unitId+editName);
        String email=importTeacher.getEmail();
        String phone=importTeacher.getPhone();
        System.out.println("email=="+email);
        if(email!=null&&isEmail(email)&&isNumeric(phone)){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("email",email);

            JSONObject jsonObject2= managerService.judgeEmailExist(jsonObject);
            String flag = jsonObject2.getString("flag");
            System.out.println("flag="+flag);
            int whetherHasEmail = Integer.parseInt(flag);
            if(whetherHasEmail==0){
                String domain=getFullSpell(importTeacher.getUsername());
                String pinyin= PinyinUtil.getPinyinString(importTeacher.getUsername());
              //  JSONObject j=new JSONObject();
                jsonObject.put("domain_name",domain);
                JSONObject j2=  managerService.judgeDomainExist2(jsonObject);
                int countDomain = Integer.parseInt(j2.getString("flag"))+1; //相同域名数+1->生成新域名
                domain=domain+countDomain;
                jsonObject.put("domain_name",domain);
                int sexNum=0;
                if(importTeacher.getSex().equals("女")){
                    sexNum=1;
                }
                String username = importTeacher.getUsername();
                String degree = importTeacher.getDegree();
                String post = importTeacher.getPost();
                String duty = importTeacher.getDuty();
                String label = importTeacher.getLabel();
                String department_name = importTeacher.getDepartment_name();
                String work_place = importTeacher.getWork_place();
                String research_direction = importTeacher.getResearch_direction();
                jsonObject.put("username",username);
                jsonObject.put("state",1);
                jsonObject.put("sex",sexNum);
                jsonObject.put("degree",degree);
                jsonObject.put("post",post);
                jsonObject.put("duty",duty);
                jsonObject.put("label",label);
                jsonObject.put("department_name",department_name);
                jsonObject.put("work_place",work_place);
                jsonObject.put("research_direction",research_direction);
                jsonObject.put("phone",phone);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date date = new Date();
                String time = dateFormat.format(date); //可以把日期转换转指定格式的字符串
                jsonObject.put("create_time", time);
                jsonObject.put("update_time", time);
                jsonObject.put("pinyin", pinyin);
                jsonObject.put("unit_id", unitId);
                jsonObject.put("edit_name", editName);
                int i = managerService.addImportTeacher(jsonObject);
                System.out.println("domain=="+domain+"sexNum="+sexNum+"time="+time+"pinyin="+pinyin+"i="+i);
            }
        }

    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
