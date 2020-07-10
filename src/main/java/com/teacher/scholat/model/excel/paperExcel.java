package com.teacher.scholat.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

@ColumnWidth(30)
public class paperExcel {
    @ColumnWidth(50)
    @ExcelProperty(value = "题目", index = 0)
    private String title; //标题
    @ColumnWidth(40)
    @ExcelProperty(value = "作者", index = 1)
    private String authors;// 作者
    @ColumnWidth(40)
    @ExcelProperty(value = "来源", index = 2)
    private String source;// 来源

    @ColumnWidth(40)
    @ExcelProperty(value = "关键词", index = 3)
    private String keyword;// 关键词

    @ColumnWidth(20)
    @ExcelProperty(value = "发表时间", index = 4)
    private String datetime;// 发表时间
/*
    @Field(store=true,index = false, type = FieldType.Keyword)
    private String folderPath;// 文件地址*/
//    private String summary;// 概要
    @ColumnWidth(20)
    @ExcelProperty(value = "论文类型", index = 5)
    private String papertype; // 论文类型



//    private Long unitId; // 学院ID


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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getPapertype() {
        return papertype;
    }

    public void setPapertype(String papertype) {
        this.papertype = papertype;
    }
}
