package com.teacher.scholat.util;

import com.alibaba.fastjson.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetScholatProfile {
    public static JSONArray getScholatProfileByEmail(String email) {
        String requestUrl = "http://www.scholat.com/rest/personalOapi/getProfileByWorkEmail/14da67741f1ae95eb1f8e0795aeb8152/2/"+email;
        return getProfileByUrl(requestUrl);
    }
    public static JSONArray getScholatProfileByChineseName(String chineseName) {
        String requestUrl = "http://www.scholat.com/rest/personalOapi/profileByPersonname/14da67741f1ae95eb1f8e0795aeb8152/2/"+chineseName;
        return getProfileByUrl(requestUrl);
    }
    public static JSONArray getScholatProfileByUserName(String username) {
        String requestUrl = "http://www.scholat.com/rest/personalOapi/profileByUsername/14da67741f1ae95eb1f8e0795aeb8152/2/"+username;
        return getProfileByUrl(requestUrl);
    }
    public static JSONArray getScholatPaperByUserName(String username) {
        String requestUrl = "http://www.scholat.com/rest/academicOapi/14da67741f1ae95eb1f8e0795aeb8152/2/paper/"+username;
        return getProfileByUrl(requestUrl);
    }

    public static JSONArray getProfileByUrl(String requestUrl){
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            // 读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            String jsonText = sb.toString();
            if(jsonText.indexOf("error")!=-1)return new JSONArray();
            JSONArray jsonArray = JSONArray.parseArray(jsonText);
            reader.close();
            conn.disconnect();
            return jsonArray;
        } catch (Exception e) {
            e.printStackTrace();
            JSONArray jsonArray = new JSONArray();
            return jsonArray;
        }
    }
}
