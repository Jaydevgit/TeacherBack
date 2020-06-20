package com.teacher.scholat.model;


public class Project {

    /**
     * @Description: @Id注解必须是springframework包下的
     * org.springframework.data.annotation.Id
     *@Author: https://blog.csdn.net/chen_2890
     */
    private Long id;

    private String title; //标题

    private String application;// 参与者

    private String leader;// 负责人

    private String startDate;// 发表时间

    private String endDate;// 结束时间
/*
    @Field(store=true,index = false, type = FieldType.Keyword)
    private String folderPath;// 文件地址*/


    private String summary;// 概要

    private String projectType; // 项目类型

    private Long unitId; // 学院ID



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

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }


    public Project() {
    }

    public Project(Long id, String title, String application, String leader, String startDate, String endDate, String summary, String projectType, Long unitId, Integer views, Integer hot, String collegeName) {
        this.id = id;
        this.title = title;
        this.application = application;
        this.leader = leader;
        this.startDate = startDate;
        this.endDate = endDate;
        this.summary = summary;
        this.projectType = projectType;
        this.unitId = unitId;

    }
}
