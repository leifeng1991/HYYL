package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏
 * Created by Administrator on 2017/4/18.
 */

public class Collect extends BaseBean {

    private List<DataBean> data = new ArrayList<>();

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 860
         * addtime : 1492511166
         * goods_id : 9503972
         * title : 卡夫奥利奥饼干奥利奥 轻甜
         * price : 5.50
         * prices : 5.50
         * simg : http://zhaolin-10029121.image.myqcloud.com/sample1491817763518417
         * ads : 卡夫奥利奥饼干
         * state : 2
         */

        private String id;
        private String addtime;
        private String goods_id;
        private String title;
        private String price;
        private String prices;
        private String simg;
        private String ads;
        private String state;
        private String activity_icon_img;

        private boolean isChecked;

        public String getActivity_icon_img() {
            return activity_icon_img;
        }

        public void setActivity_icon_img(String activity_icon_img) {
            this.activity_icon_img = activity_icon_img;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
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

        public String getAds() {
            return ads;
        }

        public void setAds(String ads) {
            this.ads = ads;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }
    }
}
