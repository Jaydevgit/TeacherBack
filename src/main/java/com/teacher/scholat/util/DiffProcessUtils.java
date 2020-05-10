package com.teacher.scholat.util;
import com.teacher.scholat.model.Teacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DiffProcessUtils {
    private static Logger log = LoggerFactory.getLogger(DiffProcessUtils.class);
    private static final String insertHeadInCK = "<span style=\"background-color:#2ecc71;\">";
    private static final String insertEndInCK = "</span>";
    private static final String delTagHeadInCK = "<s><span style=\"background-color:#d35400;\">";
    private static final String delTagEndInCK = "</span></s>";
    private static final String insertHead = "<ins style='background:#2ecc71;'><font style='vertical-align: inherit;'><font style='vertical-align: inherit;'>";
    private static final String insertEnd = "</font></font></ins>";
    private static final String delTagHead = "<del style='background:#ffe6e6;'><font style='vertical-align: inherit;'><font style='vertical-align: inherit;'>";
    private static final String delTagEnd = "</font></font></del>";

    public static List<String> compareAndReturn(String oldStr, String prefixOld, String suffixOld, String newStr, String prefixNew, String suffixNew){
        if(!StringUtils.hasText(oldStr)){
            log.info("oldStr can not be empty");
//            return null;
            oldStr="";
        }
        if(!StringUtils.hasText(newStr)){
            log.info("newStr can not be empty");
            newStr="";
        }
        List<String> list = new ArrayList<String>();

        StringBuffer forOldStr = new StringBuffer();
        StringBuffer forNewStr = new StringBuffer();
        diff_match_patch dmp = new diff_match_patch();
        LinkedList<diff_match_patch.Diff> diffs = dmp.diff_main(oldStr, newStr);
        for(diff_match_patch.Diff diff : diffs){
            switch(diff.operation) {
                case EQUAL:forOldStr.append(diff.text);  forNewStr.append(diff.text); break;
                case DELETE:forOldStr.append(prefixOld+diff.text+suffixOld);break;
                case INSERT:forNewStr.append(prefixNew+diff.text+suffixNew);break;
            }
        }
        list.add(forOldStr.toString());
        list.add(forNewStr.toString());
        return list;

    }
    public static List<Teacher> compareVersion(Teacher oldVersion, Teacher newVersion){
        if(oldVersion==null||newVersion==null) return null;
        List<Teacher> result = new ArrayList<Teacher>();
        // 个人简历
        List<String> res = compareAndReturn(DelTagsUtil.delHtmlTags(oldVersion.getIntro()),
                delTagHeadInCK,delTagEndInCK,DelTagsUtil.delHtmlTags(newVersion.getIntro()),insertHeadInCK,insertEndInCK);
        if(res!=null){
            oldVersion.setIntro(res.get(0));
            newVersion.setIntro(res.get(1));
        }
        // 中文名
        res = compareAndReturn(oldVersion.getUsername(),delTagHeadInCK,delTagEndInCK,newVersion.getUsername(),insertHeadInCK,insertEndInCK);
        if(res!=null){
            oldVersion.setUsername(res.get(0));
            newVersion.setUsername(res.get(1));
        }
        // 学者网用户名
        res = compareAndReturn(oldVersion.getScholat_username(),delTagHeadInCK,delTagEndInCK,newVersion.getScholat_username(),insertHeadInCK,insertEndInCK);
        if(res!=null){
            oldVersion.setScholat_username(res.get(0));
            newVersion.setScholat_username(res.get(1));
        }
        // 部门名
        res = compareAndReturn(oldVersion.getDepartment_name(),delTagHeadInCK,delTagEndInCK,newVersion.getDepartment_name(),insertHeadInCK,insertEndInCK);
        if(res!=null){
            oldVersion.setDepartment_name(res.get(0));
            newVersion.setDepartment_name(res.get(1));
        }
        // 头像地址
        res = compareAndReturn(oldVersion.getAvatar(),delTagHeadInCK,delTagEndInCK,newVersion.getAvatar(),insertHeadInCK,insertEndInCK);
        if(res!=null){
            //oldVersion.setAvatar(res.get(0));
            //newVersion.setAvatar(res.get(1));
        }
        // email
        res = compareAndReturn(oldVersion.getEmail(),delTagHeadInCK,delTagEndInCK,newVersion.getEmail(),insertHeadInCK,insertEndInCK);
        if(res!=null){
            oldVersion.setEmail(res.get(0));
            newVersion.setEmail(res.get(1));
        }
        // update_time
        res = compareAndReturn(oldVersion.getUpdate_time(),delTagHeadInCK,delTagEndInCK,newVersion.getUpdate_time(),insertHeadInCK,insertEndInCK);
        if(res!=null){
            oldVersion.setUpdate_time(res.get(0));
            newVersion.setUpdate_time(res.get(1));
        }
        // 学历
        res = compareAndReturn(oldVersion.getDegree(),delTagHeadInCK,delTagEndInCK,newVersion.getDegree(),insertHeadInCK,insertEndInCK);
        if(res!=null){
            oldVersion.setDegree(res.get(0));
            newVersion.setDegree(res.get(1));
        }
        // 职务
        res = compareAndReturn(oldVersion.getPost(),delTagHeadInCK,delTagEndInCK,newVersion.getPost(),insertHeadInCK,insertEndInCK);
        if(res!=null){
            oldVersion.setPost(res.get(0));
            newVersion.setPost(res.get(1));
        }
        // 研究方向
        res = compareAndReturn(oldVersion.getResearch_direction(),delTagHeadInCK,delTagEndInCK,newVersion.getResearch_direction(),insertHeadInCK,insertEndInCK);
        if(res!=null){
            oldVersion.setResearch_direction(res.get(0));
            newVersion.setResearch_direction(res.get(1));
        }

        System.out.println(oldVersion.getUsername());
        System.out.println(newVersion.getUsername());
        result.add(oldVersion);
        result.add(newVersion);
        return  result;
    }
}
