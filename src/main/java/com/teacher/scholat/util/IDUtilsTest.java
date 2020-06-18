package com.teacher.scholat.util;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class IDUtilsTest {
    public static String genImageName() {
        //取当前时间的长整形值包含毫秒
        long millis = System.currentTimeMillis();
        //long millis = System.nanoTime();
        //加上三位随机数
        Random random = new Random();
        MD5Util md5Util=new MD5Util();

        int end3 = random.nextInt(999);
        //如果不足三位前面补0
        String str = millis + String.format("%03d", end3);
        str=md5Util.md5(str);

        return str;
    }
    @org.junit.jupiter.api.Test
    void genImageName2() {
        System.out.println(genImageName());
    }
}