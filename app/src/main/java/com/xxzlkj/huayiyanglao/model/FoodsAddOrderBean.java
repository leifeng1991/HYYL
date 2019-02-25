package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

/**
 * 描述:
 *
 * @author leifeng
 *         2017/11/29 14:28
 */


public class FoodsAddOrderBean extends BaseBean{

    /**
     * data : {"price":"11.50","orderid":"2017112914272395516093","title":"8"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * price : 11.50
         * orderid : 2017112914272395516093
         * title : 8
         */

        private String price;
        private String orderid;
        private String title;

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
    }
}
