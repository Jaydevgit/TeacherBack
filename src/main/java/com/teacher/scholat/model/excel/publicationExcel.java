package com.teacher.scholat.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

@ColumnWidth(30)
public class publicationExcel {
    @ColumnWidth(50)
    @ExcelProperty(value = "名称", index = 0)
    private String title; //名称
    @ColumnWidth(40)
    @ExcelProperty(value = "作者", index = 1)
    private String authors;// 作者
    @ColumnWidth(40)
    @ExcelProperty(value = "著作介绍", index = 2)
    private String citation;// 著作介绍
    @ColumnWidth(40)
    @ExcelProperty(value = "出版社", index = 3)
    private String press;// 出版社
    @ColumnWidth(20)
    @ExcelProperty(value = "发表时间", index = 4)
    private String datetime;// 发表时间

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

    public String getCitation() {
        return citation;
    }

    public void setCitation(String citation) {
        this.citation = citation;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
