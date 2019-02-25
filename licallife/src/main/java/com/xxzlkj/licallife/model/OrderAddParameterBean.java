package com.xxzlkj.licallife.model;

import java.io.Serializable;


public class OrderAddParameterBean implements Serializable{
    // 1:保洁项目 2：保洁师 3：月嫂 4:保姆 5：小时工
    private String type;
    // 产品id
    private String ids;
    // 开始时间（时间戳）
    private String endtime;
    // 时长（小时）
    private String num;
    // 地址id(必传)
    private String addressId;
    // 订单备注
    private String content;
    // 实际支付金额（必传）（定金）
    private String price;
    // 全部金额
    private String prices;
    // 1:月 2：天
    private String timetype;
    // 门店地址
    private String shopAddress;
    // 是否是保姆预约
    private boolean isNanny;

    public boolean isNanny() {
        return isNanny;
    }

    public void setNanny(boolean nanny) {
        isNanny = nanny;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public String getTimetype() {
        return timetype;
    }

    public void setTimetype(String timetype) {
        this.timetype = timetype;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }
}
