package com.teacher.scholat.model;

public class academic {
   private Long id;
    private Long unitId;
    private Long paperTotal;
    private Long projectTotal;
    private Long patentTotal;
    private Long publicationTotal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public Long getPaperTotal() {
        return paperTotal;
    }

    public void setPaperTotal(Long paperTotal) {
        this.paperTotal = paperTotal;
    }

    public Long getProjectTotal() {
        return projectTotal;
    }

    public void setProjectTotal(Long projectTotal) {
        this.projectTotal = projectTotal;
    }

    public Long getPatentTotal() {
        return patentTotal;
    }

    public void setPatentTotal(Long patentTotal) {
        this.patentTotal = patentTotal;
    }

    public Long getPublicationTotal() {
        return publicationTotal;
    }

    public void setPublicationTotal(Long publicationTotal) {
        this.publicationTotal = publicationTotal;
    }
}
