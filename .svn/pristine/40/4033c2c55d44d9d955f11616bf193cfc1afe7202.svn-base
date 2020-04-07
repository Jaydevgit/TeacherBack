package com.teacher.scholat.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

    // salt要和前端salt相同, 因为是用来判断输入密码和传入后台是不是相似的
    public static String salt = "teacherHome";

    // 后台的盐
    public static String saltBack = "scholat-teacher";

    // 封装md5加密
    public static String md5(String str) {
        return DigestUtils.md5Hex(str);
    }

    // 第一次前端加密
    public  static String inputToBack(String str) {
        return md5(str+salt);
    }
    // 第二次加密: 后台加密, 这里的盐是后台的盐可以通过自行进行选择
    public static String toDb(String str){
        return md5(str+saltBack);
    }

    // 用来和数据库密码做比较, 也就是原始密码进来就直接两次加密和数据库比较, 一般用于登录
    public static String inputCompareWithDb(String str){
        return toDb(inputToBack(str));
    }

/*    // 第二次加密: 后台加密, 这里的盐是后台的盐可以通过自行进行选择
    public static String toDb(String str, String dbSalt){
        return md5(str+dbSalt);
    }

    // 用来和数据库密码做比较
    public static String inputCompareWithDb(String str, String dbSalt){
        return toDb(inputToBack(str),dbSalt);
    }*/

}