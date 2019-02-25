package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

/**
 * 团购
 * Created by Administrator on 2017/3/25.
 */

public class TuanGou extends BaseBean{

    /**
     * id : 46
     * title : 蓝色浪潮采摘园 仅售200元，价值240元蓝莓采摘1份！
     * simg : http://zhaolin-10029121.image.myqcloud.com/sample1490239389933341
     * goods_id : 9503857
     * price : 3.00
     * prices : 4.00
     */

    private String id;
    private String title;
    private String simg;
    private String goods_id;
    private String price;
    private String prices;
    private String ads;
    private String sale;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSimg() {
        return simg;
    }

    public void setSimg(String simg) {
        this.simg = simg;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
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

    public String getAds() {
        return ads;
    }

    public void setAds(String ads) {
        this.ads = ads;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }
}
