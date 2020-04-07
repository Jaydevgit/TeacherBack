package com.teacher.scholat.model;

public class Apply {
    private Integer id;
    private String school_name;
    private String unit_name;
    private String school_eng;
    private String unit_eng;
    private String certificate_front;
    private String certificate_back;
    private String certificate_working;
    private String certificate_logo;
    private String phone;
    private String email;
    private Integer state;
    private String create_time;
    private String update_time;
    private Integer unit_id;
    private String password;
    private String chinese_name;
    private String username;
    private String token;

    // 数据库外的
    private Integer unit_profile_id;
    private Integer login_id;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getUnit_profile_id() {
        return unit_profile_id;
    }

    public void setUnit_profile_id(Integer unit_profile_id) {
        this.unit_profile_id = unit_profile_id;
    }

    public Integer getLogin_id() {
        return login_id;
    }

    public void setLogin_id(Integer login_id) {
        this.login_id = login_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCertificate_logo() {
        return certificate_logo;
    }

    public void setCertificate_logo(String certificate_logo) {
        this.certificate_logo = certificate_logo;
    }

    public String getChinese_name() {
        return chinese_name;
    }

    public void setChinese_name(String chinese_name) {
        this.chinese_name = chinese_name;
    }

    public String getCertificate_front() {
        return certificate_front;
    }

    public void setCertificate_front(String certificate_front) {
        this.certificate_front = certificate_front;
    }

    public String getCertificate_back() {
        return certificate_back;
    }

    public void setCertificate_back(String certificate_back) {
        this.certificate_back = certificate_back;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }


    public String getCertificate_working() {
        return certificate_working;
    }

    public void setCertificate_working(String certificate_working) {
        this.certificate_working = certificate_working;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public Integer getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(Integer unit_id) {
        this.unit_id = unit_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSchool_eng() {
        return school_eng;
    }

    public void setSchool_eng(String school_eng) {
        this.school_eng = school_eng;
    }

    public String getUnit_eng() {
        return unit_eng;
    }

    public void setUnit_eng(String unit_eng) {
        this.unit_eng = unit_eng;
    }
}
