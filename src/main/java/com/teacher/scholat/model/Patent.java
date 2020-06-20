package com.teacher.scholat.model;



public class Patent {

    /**
     * @Description: @Id注解必须是springframework包下的
     * org.springframework.data.annotation.Id
     *@Author: https://blog.csdn.net/chen_2890
     */
    private Long id;

    private String title; //标题

    private String authors;// 作者

    private String datetime;// 发表时间
/*
    @Field(store=true,index = false, type = FieldType.Keyword)
    private String folderPath;// 文件地址*/


    private String summary;// 概要

    private String patentType; // 论文类型

    private Long unitId; // 学院ID


    private String research;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPatentType() {
        return patentType;
    }

    public void setPatentType(String patentType) {
        this.patentType = patentType;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }



    public String getResearch() {
        return research;
    }

    public void setResearch(String research) {
        this.research = research;
    }



    public Patent(Long id, String title, String authors, String datetime, String summary, String patentType,
                  Long unitId, Integer views, Integer hot , String research, String collegeName) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.datetime = datetime;
        this.summary = summary;
        this.patentType = patentType;
        this.unitId = unitId;

        this.research = research;
    }

    public Patent() {
    }
}
