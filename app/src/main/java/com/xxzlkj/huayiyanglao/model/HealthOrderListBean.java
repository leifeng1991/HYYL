package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.List;

/**
 * 描述:
 *
 * @author leifeng
 *         2018/3/10 15:09
 */


public class HealthOrderListBean extends BaseBean{

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1000001129
         * orderid : 20180310103047711492377
         * price : 0.01
         * addtime : 1520649047
         * state : 1
         * is_tui : 1
         * uidtime : 0
         * endtime : 1520816400
         * is_refund : 1
         * pid : 0
         * type : 1
         * shoptime : 0
         * ordinal : 0
         * service_starttime : 1520816400
         * service_endtime : 1520820000
         * cancel_desc :
         * count : 1
         * goods : [{"goods_id":"134","title":"测试1","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1520645508784395","price":"0.01","prices":"0.00","ads":"测试","num":"1","unit":"1","unit_desc":""}]
         */

        private String id;
        private String orderid;
        private String price;
        private String addtime;
        private String state;
        private String is_tui;
        private String uidtime;
        private String endtime;
        private String is_refund;
        private String pid;
        private String type;
        private String shoptime;
        private String ordinal;
        private String service_starttime;
        private String service_endtime;
        private String cancel_desc;
        private String count;
        private List<GoodsBean> goods;

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

        public String getUidtime() {
            return uidtime;
        }

        public void setUidtime(String uidtime) {
            this.uidtime = uidtime;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public String getIs_refund() {
            return is_refund;
        }

        public void setIs_refund(String is_refund) {
            this.is_refund = is_refund;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getShoptime() {
            return shoptime;
        }

        public void setShoptime(String shoptime) {
            this.shoptime = shoptime;
        }

        public String getOrdinal() {
            return ordinal;
        }

        public void setOrdinal(String ordinal) {
            this.ordinal = ordinal;
        }

        public String getService_starttime() {
            return service_starttime;
        }

        public void setService_starttime(String service_starttime) {
            this.service_starttime = service_starttime;
        }

        public String getService_endtime() {
            return service_endtime;
        }

        public void setService_endtime(String service_endtime) {
            this.service_endtime = service_endtime;
        }

        public String getCancel_desc() {
            return cancel_desc;
        }

        public void setCancel_desc(String cancel_desc) {
            this.cancel_desc = cancel_desc;
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
             * goods_id : 134
             * title : 测试1
             * simg : http://zhaolin-10029121.image.myqcloud.com/sample1520645508784395
             * price : 0.01
             * prices : 0.00
             * ads : 测试
             * num : 1
             * unit : 1
             * unit_desc :
             */

            private String goods_id;
            private String title;
            private String simg;
            private String price;
            private String prices;
            private String ads;
            private String num;
            private String unit;
            private String unit_desc;

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
