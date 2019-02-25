package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.List;

/**
 * 描述:
 *
 * @author leifeng
 *         2017/12/4 15:23
 */


public class HealthGoodsListBean extends BaseBean{

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 30
         * title : 深度按摩
         * ads : 深度按摩广告
         * simg : http://zhaolin-10029121.image.myqcloud.com/sample1512097513433952
         * price : 200.00
         * type : 1
         * unit : 1
         * unit_desc : 次
         * shop_title : 西大望路
         */

        private String id;
        private String title;
        private String ads;
        private String simg;
        private String price;
        private String type;
        private String unit;
        private String unit_desc;
        private String shop_title;
        private String toptitle;
        private String remark;
        private String url;
        private String to;
        private String show_time;
        private String address;

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getToptitle() {
            return toptitle;
        }

        public void setToptitle(String toptitle) {
            this.toptitle = toptitle;
        }

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

        public String getAds() {
            return ads;
        }

        public void setAds(String ads) {
            this.ads = ads;
        }

        public String getSimg() {
            return simg;
        }

        public void setSimg(String simg) {
            this.simg = simg;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getUnit_desc() {
            return unit_desc;
        }

        public void setUnit_desc(String unit_desc) {
            this.unit_desc = unit_desc;
        }

        public String getShop_title() {
            return shop_title;
        }

        public void setShop_title(String shop_title) {
            this.shop_title = shop_title;
        }

        public String getShow_time() {
            return show_time;
        }

        public void setShow_time(String show_time) {
            this.show_time = show_time;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
