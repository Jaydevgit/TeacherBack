package com.teacher.scholat.service;

import com.alibaba.fastjson.JSONObject;

public interface ElasticsearchService {
    JSONObject esTeacher(JSONObject requestJson);
}
