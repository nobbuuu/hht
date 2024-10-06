package com.booyue.poetry.bean;

public class BookOneHomeBean {
    private String imgUrl;
    private String titleimgUrl;
    private String title;
    private String titleTwo;

    public BookOneHomeBean(String imgUrl, String title, String titleTwo) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.titleTwo = titleTwo;
    }
    public BookOneHomeBean(String imgUrl,String titleimgUrl, String title, String titleTwo) {
        this.imgUrl = imgUrl;
        this.titleimgUrl = titleimgUrl;
        this.title = title;
        this.titleTwo = titleTwo;
    }

    public String getTitleimgUrl() {
        return titleimgUrl;
    }

    public void setTitleimgUrl(String titleimgUrl) {
        this.titleimgUrl = titleimgUrl;
    }

    public String getTitleTwo() {
        return titleTwo;
    }

    public void setTitleTwo(String titleTwo) {
        this.titleTwo = titleTwo;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
