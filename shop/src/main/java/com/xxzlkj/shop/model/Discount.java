package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

/**
 * 折扣
 * Created by Administrator on 2017/5/11.
 */

public class Discount extends BaseBean {

    /**
     * data : {"discount":"0.8"}
     */

    private DataBean data = new DataBean();

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * discount : 0.8
         */

        private String discount;

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }
    }
}
