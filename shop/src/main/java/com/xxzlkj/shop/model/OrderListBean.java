package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class OrderListBean extends BaseBean {


    private List<DataBean> data = new ArrayList<>();

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * addtime : 1490956791
         * count : 1
         * goods : [{"ads":"尼康相机描述","id":"9503892","num":"1","price":"0.01","prices":"5000.00","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1490781947940236","title":"尼康相机玫瑰金 20G"}]
         * id : 1000000420
         * is_tui : 1
         * orderid : 20170331183951231943
         * price : 0.01
         * state : 1
         * uidtime : 0
         */

        private String addtime = "";
        private String count = "";
        private String id = "";
        private String is_tui = "";
        private String orderid = "";
        private String price = "";
        private String state = "";
        private String uidtime = "";
        private String activity_type;// 订单类型 0：正常 1：预售 2：团购
        private String groupon_team_id = "";// 团购时加入团购的组id
        private List<GoodsBean> goods = new ArrayList<>();

        public String getActivity_type() {
            return activity_type;
        }

        public void setActivity_type(String activity_type) {
            this.activity_type = activity_type;
        }

        public String getGroupon_team_id() {
            return groupon_team_id;
        }

        public void setGroupon_team_id(String groupon_team_id) {
            this.groupon_team_id = groupon_team_id;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIs_tui() {
            return is_tui;
        }

        public void setIs_tui(String is_tui) {
            this.is_tui = is_tui;
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

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getUidtime() {
            return uidtime;
        }

        public void setUidtime(String uidtime) {
            this.uidtime = uidtime;
        }

        public List<GoodsBean> getGoods() {
            return goods;
        }

        public void setGoods(List<GoodsBean> goods) {
            this.goods = goods;
        }

        public static class GoodsBean {
            /**
             * ads : 尼康相机描述
             * id : 9503892
             * num : 1
             * price : 0.01
             * prices : 5000.00
             * simg : http://zhaolin-10029121.image.myqcloud.com/sample1490781947940236
             * title : 尼康相机玫瑰金 20G
             */

            private String activity_desc;
            private String activity_type;
            private String ads;
            private String id;
            private String num;
            private String price;
            private String prices;
            private String simg;
            private String title;

            public String getActivity_desc() {
                return activity_desc;
            }

            public void setActivity_desc(String activity_desc) {
                this.activity_desc = activity_desc;
            }

            public String getActivity_type() {
                return activity_type;
            }

            public void setActivity_type(String activity_type) {
                this.activity_type = activity_type;
            }

            public String getAds() {
                return ads;
            }

            public void setAds(String ads) {
                this.ads = ads;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
