package com.teacher.scholat.model;

public class SubCatalogue {
    private Long id;
    private String name;
    private Long seq;
    private Long parentId;

    @Override
    public String toString() {
        return "SubCatalogue{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", seq=" + seq +
                ", parentId=" + parentId +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
