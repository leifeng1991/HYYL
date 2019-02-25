package com.xxzlkj.licallife.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NannyAndMaternityMatronListBean extends BaseBean {


    private List<DataBean> data = new ArrayList<>();

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * id : 9
         * name : 刘某某
         * simg :
         * price : 100.00
         * onduty : 1000
         * label :
         */

        private String id;// 在收藏列表里代表收藏id，在保姆月嫂列表里代表，保姆月嫂id
        private String peopleid;// 产品id
        private String name;
        private String simg;
        private String price;
        private String onduty;
        private String label;
        private String praise;
        private String type;// 月嫂 yuesao；保姆 baomu

        public String getPraise() {
            return praise;
        }

        public void setPraise(String praise) {
            this.praise = praise;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getOnduty() {
            return onduty;
        }

        public void setOnduty(String onduty) {
            this.onduty = onduty;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getPeopleid() {
            return peopleid;
        }

        public void setPeopleid(String peopleid) {
            this.peopleid = peopleid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
