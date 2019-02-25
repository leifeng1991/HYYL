package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/31.
 */

public class Order extends BaseBean {

    /**
     *
     * data : {"price":"0.01","orderid":"20170331164409860221","title":"1000000414"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * price : 0.01
         * orderid : 20170331164409860221
         * title : 1000000414
         */
        private String price;
        private String orderid;
        private String title;
        //家政-->下单新增
        private String id;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
