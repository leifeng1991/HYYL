package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */

public class CofirmOrderBean extends BaseBean {

    private List<DataBean> data = new ArrayList<>();

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 9505563
         * stock : 0
         */

        private String id;
        private String stock;
        private String price;
        private String activitys;// 0:普通 1:预售 2：团购
        private String activity_desc;// 活动描述 只有activitys不等于0时才显示

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStock() {
            return stock;
        }

        public void setStock(String stock) {
            this.stock = stock;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getActivitys() {
            return activitys;
        }

        public void setActivitys(String activitys) {
            this.activitys = activitys;
        }

        public String getActivity_desc() {
            return activity_desc;
        }

        public void setActivity_desc(String activity_desc) {
            this.activity_desc = activity_desc;
        }
    }
}
