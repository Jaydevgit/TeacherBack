package com.teacher.scholat.model;


public class Paper {

    /**
     * @Description: @Id注解必须是springframework包下的
     * org.springframework.data.annotation.Id
     *@Author: https://blog.csdn.net/chen_2890
     */
    private Long id;

    private String title; //标题

    private String authors;// 作者

    private String source;// 来源

    private String sourceDetail;// 详细来源

    private String datetime;// 发表时间
/*
    @Field(store=true,index = false, type = FieldType.Keyword)
    private String folderPath;// 文件地址*/

    private String keyword;// 关键词

    private String summary;// 概要

    private Integer papertype; // 论文类型

    private Long unitId; // 学院ID


    public Paper(Long id, String title, String authors, String source, String sourceDetail, String datetime, String keyword, String summary, Integer papertype, Long unitId, Integer views,
                 Integer hot, String collegeName) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.source = source;
        this.sourceDetail = sourceDetail;
        this.datetime = datetime;
        this.keyword = keyword;
        this.summary = summary;
        this.papertype = papertype;
        this.unitId = unitId;

    }

    @Override
    public String toString() {
        return "Paper{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", authors='" + authors + '\'' +
                ", source='" + source + '\'' +
                ", sourceDetail='" + sourceDetail + '\'' +
                ", datetime='" + datetime + '\'' +
                ", keyword='" + keyword + '\'' +
                ", summary='" + summary + '\'' +
                ", papertype=" + papertype +
                ", unitId=" + unitId +
                '}';
    }

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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceDetail() {
        return sourceDetail;
    }

    public void setSourceDetail(String sourceDetail) {
        this.sourceDetail = sourceDetail;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Paper() {
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getPapertype() {
        return papertype;
    }

    public void setPapertype(Integer papertype) {
        this.papertype = papertype;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }


}
