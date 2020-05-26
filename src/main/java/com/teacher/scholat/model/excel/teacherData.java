package com.teacher.scholat.model.excel;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

@ColumnWidth(15)
public class teacherData {
    @ExcelProperty(value = "姓名", index = 0)
    private String username;
    @ExcelProperty(value = "性别", index = 1)
    private String sex;
    @ColumnWidth(25)
    @ExcelProperty(value = "邮箱", index = 2)
    private String email;
    @ExcelProperty(value = "职称", index = 3)
    private String post;
    @ExcelProperty(value = "职务", index = 4)
    private String duty;
    @ExcelProperty(value = "头衔", index = 5)
    private String label;
    @ColumnWidth(25)
    @ExcelProperty(value = "专业", index = 6)
    private String subject;
    @ColumnWidth(25)
    @ExcelProperty(value = "学历学位", index = 7)
    private String degree;
    @ColumnWidth(25)
    @ExcelProperty(value = "毕业学校", index = 8)
    private String graduateFrom;
    @ColumnWidth(100)
    @ExcelProperty(value = "研究方向", index = 9)
    private String research_direction;
    @ColumnWidth(25)
    @ExcelProperty(value = "办公地点", index = 10)
    private String work_place;
    @ExcelProperty(value = "办公电话", index = 11)
    private String phone;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getGraduateFrom() {
        return graduateFrom;
    }

    public void setGraduateFrom(String graduateFrom) {
        this.graduateFrom = graduateFrom;
    }

    public String getResearch_direction() {
        return research_direction;
    }

    public void setResearch_direction(String research_direction) {
        this.research_direction = research_direction;
    }

    public String getWork_place() {
        return work_place;
    }

    public void setWork_place(String work_place) {
        this.work_place = work_place;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
