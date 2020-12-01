package com.teacher.scholat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.teacher.scholat.service.ElasticsearchService;
import com.teacher.scholat.util.CommonUtil;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ElasticsearchServiceImpl  implements ElasticsearchService {

    @Autowired
     RestHighLevelClient client;

    @Override
    public JSONObject esTeacher(JSONObject requestJson) {

        SearchRequest searchRequest = new SearchRequest("teacher_index");
        //构建搜索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        //高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("research_direction");
        highlightBuilder.requireFieldMatch(true);
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);
        //查询条件
        boolQuery.must(QueryBuilders.matchQuery("research_direction", "计算"));

        //精确而查询
        //     MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        sourceBuilder.query(boolQuery);
        sourceBuilder.from(1);
        sourceBuilder.size(20);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
       // System.out.println(JSON.toJSONString(searchResponse.getHits()));
        //解析结果
        ArrayList<Map<String,Object>> list = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField researchDirection = highlightFields.get("research_direction");
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();//原来结果
            if(researchDirection!=null){
                Text[] fragments = researchDirection.fragments();
                String n_researchDirection="";
                for (Text text : fragments) {
                    n_researchDirection+=text;
                }
                sourceAsMap.put("research_direction",n_researchDirection); //高亮字段替换原来内容
            }
            list.add(sourceAsMap);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("teacherList",list);
        return CommonUtil.successJson(jsonObject);
    }
}
