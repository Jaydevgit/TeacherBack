package com.teacher.scholat.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

@ColumnWidth(30)
public class patentExcel {
    @ColumnWidth(50)
    @ExcelProperty(value = "名称", index = 0)
    private String title; //名称
    @ColumnWidth(40)
    @ExcelProperty(value = "作者", index = 1)
    private String authors;// 作者
    @ColumnWidth(40)
    @ExcelProperty(value = "专利类型", index = 2)
    private String patent_type;// 专利类型

    @ColumnWidth(20)
    @ExcelProperty(value = "专利编号", index = 3)
    private String patent_number; // 专利号

    @ColumnWidth(20)
    @ExcelProperty(value = "发表时间", index = 4)
    private String datetime;// 发表时间
    /*
        @Field(store=true,index = false, type = FieldType.Keyword)
        private String folderPath;// 文件地址*/

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

    public String getPatent_type() {
        return patent_type;
    }

    public void setPatent_type(String patent_type) {
        this.patent_type = patent_type;
    }

    public String getPatent_number() {
        return patent_number;
    }

    public void setPatent_number(String patent_number) {
        this.patent_number = patent_number;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
