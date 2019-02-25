package com.xxzlkj.licallife.model;

public class SortBean {
    private String title;
    private String order;

    public SortBean(String title, String order) {
        this.title = title;
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
