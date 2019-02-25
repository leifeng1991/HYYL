package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 购物车商品列表model
 * Created by Administrator on 2017/3/22.
 */

public class ShopCartList extends BaseBean {

    /**
     * data : {"g":[{"id":"1800","goods_id":"9503845","num":"1","uid":"111341","addtime":"1489997753","title":"雪花啤酒淡爽 350ml","price":"5.00","prices":"7.00","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1489134726321929","state":"1","groupid":"437","ads":"啤酒品类:淡爽 容量:350ml "}],"goods":[]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        private List<GBean> g;
        private List<GBean> goods;

        public List<GBean> getG() {
            return g;
        }

        public void setG(List<GBean> g) {
            this.g = g;
        }

        public List<GBean> getGoods() {
            return goods;
        }

        public void setGoods(List<GBean> goods) {
            this.goods = goods;
        }

        public static class GBean implements Serializable{
            /**
             * id : 1800
             * goods_id : 9503845
             * num : 1
             * uid : 111341
             * addtime : 1489997753
             * title : 雪花啤酒淡爽 350ml
             * price : 5.00
             * prices : 7.00
             * simg : http://zhaolin-10029121.image.myqcloud.com/sample1489134726321929
             * state : 1
             * groupid : 437
             * ads : 啤酒品类:淡爽 容量:350ml
             * stock
             * min_qty
             */

            private String id;
            private String goods_id;
            private String num;
            private String uid;
            private String addtime;
            private String title;
            private String price;
            private String prices;
            private String simg;
            private String state;
            private String groupid;
            private String ads;
            private String stock;
            private String min_qty;
            private String activitys;
            private String stoptime;
            private String activity_desc;
            private String activity_icon_img;

            public String getActivity_icon_img() {
                return activity_icon_img;
            }

            public void setActivity_icon_img(String activity_icon_img) {
                this.activity_icon_img = activity_icon_img;
            }

            public String getActivitys() {
                return activitys;
            }

            public void setActivitys(String activitys) {
                this.activitys = activitys;
            }

            public String getStoptime() {
                return stoptime;
            }

            public void setStoptime(String stoptime) {
                this.stoptime = stoptime;
            }

            public String getActivity_desc() {
                return activity_desc;
            }

            public void setActivity_desc(String activity_desc) {
                this.activity_desc = activity_desc;
            }

            private boolean isChecked;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
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

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getGroupid() {
                return groupid;
            }

            public void setGroupid(String groupid) {
                this.groupid = groupid;
            }

            public String getAds() {
                return ads;
            }

            public void setAds(String ads) {
                this.ads = ads;
            }

            public boolean isChecked() {
                return isChecked;
            }

            public void setIsChecked(boolean checked) {
                isChecked = checked;
            }

            public String getStock() {
                return stock;
            }

            public void setStock(String stock) {
                this.stock = stock;
            }

            public String getMin_qty() {
                return min_qty;
            }

            public void setMin_qty(String min_qty) {
                this.min_qty = min_qty;
            }
        }

    }
}
