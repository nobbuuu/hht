package com.booyue.poetry.bean;

public class LayoutBean {


    private int id;
    private int type;

    public LayoutBean() {

    }

    public LayoutBean(int id, int type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
