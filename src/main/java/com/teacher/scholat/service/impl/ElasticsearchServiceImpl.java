package com.teacher.scholat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.service.ElasticsearchService;
import com.teacher.scholat.util.CommonUtil;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ElasticsearchServiceImpl  implements ElasticsearchService {

    @Autowired
     RestHighLevelClient client;

    @Override
    public JSONObject esTeacher(JSONObject requestJson) {

        GetIndexRequest request = new GetIndexRequest("teacher_index");
        GetResponse getResponse = null;
        try {
            boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
            System.out.println(exists);
        } catch (IOException e) {
            e.printStackTrace();
        }
     //  System.out.println(getResponse.getSourceAsString()+getResponse.getSource());
        return CommonUtil.successJson();
    }
}
