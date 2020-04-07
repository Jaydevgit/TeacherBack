package com.teacher.scholat.util;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class InviteToScholat {
    public static String inviteToScholat(JSONObject jsonObject) {
        System.out.println("-------------------- 请求开始：进入到邀请加入学者网 --------------------");
        JSONObject params = new JSONObject();
        // params.put("token", "14da67741f1ae95eb1f8e0795aeb8152"); // dxm
        params.put("token", "14da67741f1ae95eb1f8e0795aeb8152");
        params.put("appid", "2");
        params.put("name", "dxm测试");
        //params.put("email", "413459865@qq.com");
        params.put("email", "489572627@qq.com");
        params.put("workUnit", "华南师范大学");
        params.put("title", "");
        params.put("degree", "Master");
        params.put("introduction", "");
        params.put("avatar", "");
        System.out.println("准备发送邀请到学者网邀请的信息为：");
        System.out.println(params.toString());
//		String params = "{\"token\":\"14da67741f1ae95eb1f8e0795aeb8152\",\"appid\":\"2\",\"name\":\"Ronghua Lin\","
//				+ "\"email\":\"249280256@qq.com\",\"workUnit\":\"South China Normal University\",\"title\":\"\","
//				+ "\"degree\":\"Master\",\"introduction\":\"\",\"avatar\":\"\"}";
        String requestUrl = "http://www.scholat.com/scholat/rest/signInOapi/appInvite";
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setConnectTimeout(3000); // 设置超时3秒
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.connect();
            OutputStreamWriter out = new OutputStreamWriter(
                    connection.getOutputStream(), "UTF-8");
            out.write(params.toString());
            out.flush();
            out.close();
            // 读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            System.out.println(sb);
            reader.close();
            connection.disconnect();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

}
