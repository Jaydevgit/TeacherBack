package com.teacher.scholat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.dao.CatalogueDao;
import com.teacher.scholat.dao.TeacherDao;
import com.teacher.scholat.service.CatalogueService;
import com.teacher.scholat.util.CommonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

@Service
public class CatalogueServiceImpl implements CatalogueService {

    @Resource
    CatalogueDao catalogueDao;

    @Resource
    TeacherDao teacherDao;

    @Override
    public JSONObject getCatalogues(JSONObject object) {
        List<JSONObject> catalogue = catalogueDao.findCatalogueByUnit(object);
        return CommonUtil.successPage(catalogue);
    }

    @Override
    public JSONObject getTeacherByCatalogue(JSONObject object) {
        List<JSONObject> teacher = catalogueDao.getTeacherByCatalogue(object);
        return CommonUtil.successPage(teacher);
    }

    @Override
    public JSONObject getTeacherByCatalogueId(JSONObject object) {
        List<JSONObject> teacher = catalogueDao.getTeacherByCatalogueId(object);
        return CommonUtil.successPage(teacher);
    }

    @Override
    public JSONObject getTeacherAllCatalogues(JSONObject object) {
        List<JSONObject> teacher = catalogueDao.getTeacherAllCatalogues(object);
        return CommonUtil.successPage(teacher);
    }


    @Override
    public JSONObject getTeacherByCatalogueAndPage(JSONObject object) {
        System.out.println("前端传过来的教师列表要求为: "+object);
        CommonUtil.fillPageParam(object);
        System.out.println("检查offset"+object);
        int count = catalogueDao.countTeacher(object);
        System.out.println("........有"+count+"位教师");
        List<JSONObject> list = catalogueDao.getTeacherByCatalogueAndPage(object);
        System.out.println(list);
        return CommonUtil.successPage(object, list, count);
    }

    @Override
    public JSONObject getByCatalogueAndLetterAndPage(JSONObject object) {
        System.out.println("前端传过来的教师列表要求为: "+object);
        CommonUtil.fillPageParam(object);
        System.out.println("检查offset"+object);
        int count = catalogueDao.countLetterTeacher(object);
        System.out.println("........有"+count+"位教师");
        List<JSONObject> list = catalogueDao.getByCatalogueAndLetterAndPage(object);
        System.out.println(list);
        return CommonUtil.successPage(object, list, count);
    }

    @Override
    public JSONObject addCatalogue(JSONObject object) {
        catalogueDao.addCatalogue(object);
        return CommonUtil.successJson();
    }

    @Override
    @Transactional
    public JSONObject addMulCatalogue(JSONObject object) {
        String string=object.getString("list");
        String unitId=object.getString("unitId");
        string=string.substring(1,string.length()-1);
        String[] list=string.split(",");
        for(String item : list )
        {
            item=item.trim();
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("unitId",unitId);
            jsonObject.put("catalogue",item);
            catalogueDao.addCatalogue(jsonObject);
        }
        List<JSONObject> catalogue = catalogueDao.findCatalogueByUnit(object);
        return CommonUtil.successPage(catalogue);
    }

    @Override
    public JSONObject deleteCatalogue(JSONObject object) {
        catalogueDao.deleteCatalogue(object);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject updateCatalogue(JSONObject object) {
        catalogueDao.updateCatalogueTeacher(object);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject addSubCatalogue(JSONObject object)
    {
        catalogueDao.addSubCatalogue(object);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject removeTeacher(JSONObject object)
    {
        catalogueDao.removeTeacher(object);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject addSMulubCatalogue(JSONObject object) {
        String string=object.getString("list");
        String unitId=object.getString("unitId");
        String parentId="";
        string=string.substring(1,string.length()-1);
        String[] list=string.split(",");

        for(String item : list )
        {
            item=item.trim();
            String[] list_child=item.split("@加@");
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("unitId",unitId);
            jsonObject.put("catalogue",list_child[1].trim());
            jsonObject.put("parentId",list_child[0].trim());
            System.out.println(jsonObject);
            catalogueDao.addSubCatalogue(jsonObject);
        }
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject removeMultTeacher(JSONObject Object) {
        System.out.println("删除的id:"+Object);
        catalogueDao.removeMultTeacher(Object);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject topTeacher(JSONObject object) {
        //添加時間
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        object.put("topTime", ts);
        catalogueDao.topTeacher(object);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject topCatalogue(JSONObject object) {
        //添加時間
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        object.put("topTime", ts);
        catalogueDao.topCatalogue(object);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject sortCatalogue(JSONObject object) {
        System.out.println("---------sort------" + object);
       /* Object cIds1 = object.get("cIds");
        catalogueDao.judgeIsParentById()*/
        String cIds = object.getString("cIds").replace("[","").replace("]","");
        String[] cIdsSplit = cIds.split(",");
        System.out.println(cIdsSplit);
        //判断是否在
        JSONObject searchId = new JSONObject();
        searchId.put("id" , cIdsSplit[0]);
        //判断是否为主栏目
        Boolean panduan = catalogueDao.judgeIsParentById(searchId) == null ? true : false;
        //如果是排序主菜单，先把所有主菜单次序变成999放到最后
        if (panduan) {
            List<JSONObject> catalogues = catalogueDao.findCatalogueByUnit(object);
            for (JSONObject c : catalogues) {
                c.put("seq", 9999);
                c.put("cId", c.getLongValue("id"));
                catalogueDao.topCatalogue(c);
            }
            //把特定的次序保存
            JSONObject dao = new JSONObject();
            for (int i = 0; i < cIdsSplit.length; i++) {
                System.out.println(cIdsSplit[i]);
                dao.put("cId", cIdsSplit[i]);
                dao.put("seq", i + 1);
                catalogueDao.topCatalogue(dao);
            }
        }//如果排序子菜单
        else {
            Integer parentId = catalogueDao.judgeIsParentById(searchId);
            List<Integer> subIds = catalogueDao.getSubIdByCatalogue(parentId);
            for (Integer c : subIds) {
                JSONObject jo = new JSONObject();
                jo.put("seq", 9999);
                jo.put("cId", c);
                catalogueDao.topCatalogue(jo);
            }

            //把特定的次序保存
            JSONObject dao = new JSONObject();
            for (int i = 0; i < cIdsSplit.length; i++) {
                System.out.println(cIdsSplit[i]);
                dao.put("cId", cIdsSplit[i]);
                dao.put("seq", i + 1);
                catalogueDao.topCatalogue(dao);
            }
        }
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject sortTeacher(JSONObject object) {
        //先把所有次序变成999放到最后
        List<JSONObject> cts = catalogueDao.getTeacherByCatalogue(object);
        for (JSONObject ct : cts){
            ct.put("tSeq" , 9999);
            catalogueDao.topTeacher(ct);
        }

        //把特定的次序保存
        String ctIds = object.getString("ctIds").replace("[","").replace("]","");
        String[] ctIdsSplit = ctIds.split(",");
        System.out.println(ctIdsSplit);
        JSONObject dao = new JSONObject();
        for (int i = 0;i< ctIdsSplit.length;i++){
            System.out.println(ctIdsSplit[i]);
            dao.put("ctId",ctIdsSplit[i]);
            dao.put("tSeq",i + 1);
            catalogueDao.topTeacher(dao);
        }
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject addMultTeacher(JSONObject object) {
        catalogueDao.addMultTeacher(object);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject addSingleTeacher(JSONObject object){
        catalogueDao.addSingleTeacher(object);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject addCatalogueTeacher(JSONObject object)
    {
        String tId = object.getString("tIds").replace("[","").replace("]","");
        String cIdsSplit[] = object.getString("cId").replace("[","").replace("]","").split(",");
        System.out.println("分配教师"+tId+"===到栏目"+cIdsSplit.toString()+"中");
        JSONObject dao = new JSONObject();
        for (int i = 0 ;i<cIdsSplit.length;i++){
            System.out.println(cIdsSplit[i].toString()+"==="+tId);
            if(cIdsSplit[i].toString()!=""&&cIdsSplit[i].toString()!=null&&!cIdsSplit[i].isEmpty()){
//                System.out.println(cIdsSplit[i].toString()+"==="+tId);
                dao.put("tId",tId);
                dao.put("cId",cIdsSplit[i]);
                catalogueDao.addCatalogueTeacher(dao);
            }

        }
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject updateCatalogueTeacher(JSONObject object) {
        catalogueDao.updateCatalogueTeacher(object);

        return CommonUtil.successJson();
    }

    @Override
    public JSONObject getDepartSubjectByUnit(JSONObject object) {
        List<JSONObject> list = catalogueDao.getDepartSubjectByUnit(object);
        return CommonUtil.successPage(list);
    }

    @Override
    public JSONObject sortUnitTeacher(JSONObject object)
    {
        //先把所有次序变成999放到最后
        List<JSONObject> cts = teacherDao.listTeacherAll(object);
        for (JSONObject ct : cts){
            ct.put("seq" , 9999);
            catalogueDao.sortUnitTeacher(ct);
        }

        //把特定的次序保存
        String tIds = object.getString("tIds").replace("[","").replace("]","");
        String[] tIdsSplit = tIds.split(",");
        System.out.println(tIdsSplit);
        JSONObject dao = new JSONObject();
        for (int i = 0;i< tIdsSplit.length;i++){
            System.out.println(tIdsSplit[i]);
            dao.put("tId",tIdsSplit[i]);
            dao.put("seq",i + 1);
            catalogueDao.sortUnitTeacher(dao);
        }
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject getCatalogueNameByCatalogueId(JSONObject object) {
        List<JSONObject> cName=catalogueDao.getCatalogueNameByCatalogueId(object);
        System.out.println("cName="+cName);
        return CommonUtil.successJson(cName);
    }
}
