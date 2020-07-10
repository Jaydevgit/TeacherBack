package com.teacher.scholat.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

@ColumnWidth(30)
public class projectExcel {
    @ColumnWidth(50)
    @ExcelProperty(value = "名称", index = 0)
    private String title; //名称
    @ColumnWidth(40)
    @ExcelProperty(value = "申请者", index = 1)
    private String application;// 申请者
    @ColumnWidth(40)
    @ExcelProperty(value = "项目类型", index = 2)
    private String projectType;// 项目类型
    @ColumnWidth(20)
    @ExcelProperty(value = "项目资金", index = 3)
    private String funding;// 项目资金

    @ColumnWidth(20)
    @ExcelProperty(value = "项目编号", index = 4)
    private String projectNumber; // 项目编号

    @ColumnWidth(20)
    @ExcelProperty(value = "开始日期", index = 5)
    private String startDate;// 开始日期
    @ColumnWidth(20)
    @ExcelProperty(value = "截至日期", index = 6)
    private String endDate;// 截至日期

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

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getFunding() {
        return funding;
    }

    public void setFunding(String funding) {
        this.funding = funding;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
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
}
