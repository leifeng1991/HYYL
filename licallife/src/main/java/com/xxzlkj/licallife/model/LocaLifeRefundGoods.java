package com.xxzlkj.licallife.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.List;

/**
 * 本地生活已经申请售后订单
 * Created by Administrator on 2017/5/10.
 */

public class LocaLifeRefundGoods extends BaseBean{

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 156484727
         * orderid : 1000000713
         * refund_price : 0.02
         * addtime : 1494410533
         * state : 1
         * status : 1
         * goods_id : 9504741
         * title : 测试商品2
         * ads :
         * num : 2
         * price : 0.01
         * prices : 0.00
         * simg : http://zhaolin-10029121.image.myqcloud.com/sample1489211525773340
         */

        private String id;
        private String orderid;
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
        private String unit;
        private String unit_desc;
        private String endtime;

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

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

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
