package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.List;

public class OrderListBean extends BaseBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 15
         * orderid : 2017112915581675454191
         * price : 11.50
         * addtime : 1511942296
         * state : 2
         * is_tui : 1
         * is_refund : 1
         * starttime : 1511951400
         * stoptime : 1511958600
         * comment : 0
         * goods : [{"id":"13","title":"加多宝凉茶来一份","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1495158619373550","price":"3.50","ads":"比市场价还便宜5毛","num":"1"},{"id":"14","title":"腊八粥","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1495607519820124","price":"8.00","ads":"香甜腊八粥营养健康","num":"1"}]
         */

        private String id;
        private String orderid;
        private String price;
        private String addtime;
        private String state;
        private String is_tui;
        private String is_refund;
        private String starttime;
        private String stoptime;
        private String comment;
        private String is_urge;
        private List<GoodsBean> goods;

        public String getIs_urge() {
            return is_urge;
        }

        public void setIs_urge(String is_urge) {
            this.is_urge = is_urge;
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

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getIs_tui() {
            return is_tui;
        }

        public void setIs_tui(String is_tui) {
            this.is_tui = is_tui;
        }

        public String getIs_refund() {
            return is_refund;
        }

        public void setIs_refund(String is_refund) {
            this.is_refund = is_refund;
        }

        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public String getStoptime() {
            return stoptime;
        }

        public void setStoptime(String stoptime) {
            this.stoptime = stoptime;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public List<GoodsBean> getGoods() {
            return goods;
        }

        public void setGoods(List<GoodsBean> goods) {
            this.goods = goods;
        }

        public static class GoodsBean {
            /**
             * id : 13
             * title : 加多宝凉茶来一份
             * simg : http://zhaolin-10029121.image.myqcloud.com/sample1495158619373550
             * price : 3.50
             * ads : 比市场价还便宜5毛
             * num : 1
             */

            private String id;
            private String title;
            private String simg;
            private String price;
            private String ads;
            private String num;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
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
        }
    }
}
