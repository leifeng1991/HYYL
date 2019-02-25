package com.xxzlkj.licallife.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 可申请售后商品订单（本地生活）
 * Created by Administrator on 2017/4/8.
 */

public class JzRefundOrderGoods extends BaseBean {

    private List<DataBean> data = new ArrayList<>();

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1000000713
         * orderid : 201704221636049522756
         * price : 0.02
         * addtime : 1492850164
         * endtime : 1492995600
         * count : 2
         * goods : [{"id":"2","title":"测试商品2","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1489211525773340","price":"0.01","prices":"0.00","ads":"","num":"2"}]
         */

        private String id;
        private String orderid;
        private String price;
        private String addtime;
        private String endtime;
        private String count;
        private List<GoodsBean> goods = new ArrayList<>();

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

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public List<GoodsBean> getGoods() {
            return goods;
        }

        public void setGoods(List<GoodsBean> goods) {
            this.goods = goods;
        }

        public static class GoodsBean {
            /**
             * id : 2
             * title : 测试商品2
             * simg : http://zhaolin-10029121.image.myqcloud.com/sample1489211525773340
             * price : 0.01
             * prices : 0.00
             * ads :
             * num : 2
             */

            private String id;
            private String title;
            private String simg;
            private String price;
            private String prices;
            private String ads;
            private String num;
            private String unit;
            private String unit_desc;

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

            public String getPrices() {
                return prices;
            }

            public void setPrices(String prices) {
                this.prices = prices;
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
