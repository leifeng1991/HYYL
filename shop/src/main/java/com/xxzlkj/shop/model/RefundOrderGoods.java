package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 可申请售后商品订单
 * Created by Administrator on 2017/4/8.
 */

public class RefundOrderGoods extends BaseBean {

    private List<DataBean> data = new ArrayList<>();

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1000000472
         * orderid : 20170408112346380010
         * price : 0.01
         * addtime : 1491621826
         * count : 1
         * discount: 0.8
         * coupon_price:10.00
         * goods : [{"id":"9503892","title":"尼康相机玫瑰金 20G","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1490781947940236","price":"0.01","prices":"5000.00","ads":"尼康相机描述","num":"1"}]
         */

        private String id;
        private String orderid;
        private String price;
        private String addtime;
        private String count;
        private String discount;
        private String coupon_price;
        private List<Goods> goods = new ArrayList<>();

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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public List<Goods> getGoods() {
            return goods;
        }

        public void setGoods(List<Goods> goods) {
            this.goods = goods;
        }

        public String getCoupon_price() {
            return coupon_price;
        }

        public void setCoupon_price(String coupon_price) {
            this.coupon_price = coupon_price;
        }
    }
}
