package com.sunmi.sunmit2demo.bean;

/**
 * Created by highsixty on 2018/3/13.
 * mail  gaolulin@sunmi.com
 */

public class GvBeans {
    private int imgId;
    private String name;
    private String price;
    private String code;
    private int mode;
    private int logo ;

    public GvBeans(){

    }
    public GvBeans(int imgId, String name, String price) {
        this.imgId = imgId;
        this.name = name;
        this.price = price;
    }
    public int getImgId() {
        return imgId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
    public int getLogo() {
        return logo;
    }

    public GvBeans setLogo(int logo) {
        this.logo = logo;
        return this;
    }
}
