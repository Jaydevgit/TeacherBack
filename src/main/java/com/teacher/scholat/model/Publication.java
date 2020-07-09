package com.teacher.scholat.model;

public class Publication {
    private Long id;

    private String title; //标题

    private String authors;// 作者

    private String datetime;// 发表时间
/*
    @Field(store=true,index = false, type = FieldType.Keyword)
    private String folderPath;// 文件地址*/

    private String keyword;// 关键词

    private String summary;// 概要

    private Integer papertype; // 论文类型

    private Long unitId; // 学院ID
}
