package com.xxzlkj.licallife.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class LocalLifeListBean extends BaseBean {


    private List<DataBean> data=new ArrayList<>();

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * addtime : 1492659248
         * count : 2
         * endtime : 1492658710
         * goods : [{"ads":"","id":"1","num":"2","price":"30.00","prices":"0.00","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1489211525773340","title":"测试商品1"}]
         * id : 1000000693
         * is_refund : 1
         * is_tui : 1
         * orderid : 201704201134081202697
         * price : 120.00
         * state : 1
         * uidtime : 0
         */

        private String addtime;
        private String count;
        private String endtime;
        private String id;
        private String is_refund;
        private String is_tui;
        private String orderid;
        private String price;
        private String state;
        private String uidtime;
        private String pid;
        private String shoptime;
        private String type;
        private String tables;

        public String getTables() {
            return tables;
        }

        public void setTables(String tables) {
            this.tables = tables;
        }

        private List<GoodsBean> goods=new ArrayList<>();

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getShoptime() {
            return shoptime;
        }

        public void setShoptime(String shoptime) {
            this.shoptime = shoptime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIs_refund() {
            return is_refund;
        }

        public void setIs_refund(String is_refund) {
            this.is_refund = is_refund;
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
             * ads :
             * id : 1
             * num : 2
             * price : 30.00
             * prices : 0.00
             * simg : http://zhaolin-10029121.image.myqcloud.com/sample1489211525773340
             * title : 测试商品1
             * unit
             * unit_desc
             */

            private String ads;
            private String id;
            private String num;
            private String price;
            private String prices;
            private String simg;
            private String title;
            private String unit;
            private String unit_desc;

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

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }

            public String getUnit_desc() {
                return unit_desc;
            }

            public void setUnit_desc(String unit_desc) {
                this.unit_desc = unit_desc;
            }

        }
    }
}
