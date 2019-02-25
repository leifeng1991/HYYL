package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 已经申请售后的列表
 * Created by Administrator on 2017/4/11.
 */

public class RefundGoods extends BaseBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * id : 156484646
         * orderid : 1000000503
         * tui_mode : 1
         * refund_price : 0.01
         * addtime : 1491881062
         * state : 1
         * status : 1
         * goods_id : 9504521
         * title : 天喔茶庄-蜂蜜柚子茶加多宝
         * ads : 天喔茶庄-蜂蜜柚子茶描述
         * num : 1
         * price : 0.01
         * prices : 1.00
         * simg : http://zhaolin-10029121.image.myqcloud.com/sample149069370013070
         */

        private String id;
        private String orderid;
        private String tui_mode;
        private String refund_price;
        private String addtime;
        private String state;
        private String status;
        private String goods_id;
        private String title;
        private String ads;
        private String num;
        private String price;
        private String prices;
        private String simg;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public String getTui_mode() {
            return tui_mode;
        }

        public void setTui_mode(String tui_mode) {
            this.tui_mode = tui_mode;
        }

        public String getRefund_price() {
            return refund_price;
        }

        public void setRefund_price(String refund_price) {
            this.refund_price = refund_price;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
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

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
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

        public String getSimg() {
            return simg;
        }

        public void setSimg(String simg) {
            this.simg = simg;
        }
    }
}
