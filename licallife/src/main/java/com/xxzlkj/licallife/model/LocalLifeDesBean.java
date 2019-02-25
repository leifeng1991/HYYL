package com.xxzlkj.licallife.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class LocalLifeDesBean extends BaseBean {


    /**
     * data : {"id":"1000000704","orderid":"201704211636493777069","is_refund":"1","uid":"111339","price":"0.02","addtime":"1492763809","endtime":"1492658710","payment":"0","uidtime":"0","state":"1","address_name":"毕珂","address_phone":"18623792320","address_address":"中弘北京像素你麻痹","content":"","is_tui":1,"sendtime":"0","edittime":"0","edit_desc":"","shop_title":"测试家政店铺1","goods":[{"id":"1","title":"测试商品1","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1489211525773340","price":"0.01","prices":"0.00","ads":"","num":"2","goods_id":"1","endtime":"1492658710","shop_title":"测试家政店铺1"}]}
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
         * id : 1000000704
         * orderid : 201704211636493777069
         * is_refund : 1
         * uid : 111339
         * price : 0.02
         * addtime : 1492763809
         * endtime : 1492658710
         * payment : 0
         * uidtime : 0
         * state : 1
         * address_name : 毕珂
         * address_phone : 18623792320
         * address_address : 中弘北京像素你麻痹
         * content :
         * is_tui : 1
         * sendtime : 0
         * edittime : 0
         * edit_desc :
         * shop_title : 测试家政店铺1
         * goods : [{"id":"1","title":"测试商品1","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1489211525773340","price":"0.01","prices":"0.00","ads":"","num":"2","goods_id":"1","endtime":"1492658710","shop_title":"测试家政店铺1"}]
         */

        private String id;
        private String orderid;
        private String is_refund;
        private String uid;
        private String price;// 支付金额
        private String prices;// 全部金额
        private String addtime;
        private String endtime;
        private String formertime;
        private String payment;
        private String uidtime;
        private String state;
        private String address_name;
        private String address_phone;
        private String address_address;
        private String content;
        private String is_tui;
        private String sendtime;
        private String edittime;
        private String edit_desc;
        private String shop_title;
        private String refund_id;// 为0则未申请售后
        private String pid;
        private String audition_time;
        private String audition_type;
        private String audition_address = "";
        private String shoptime;
        private String timetype;
        private String type;
        private List<GoodsBean> goods = new ArrayList<>();

        public String getTimetype() {
            return timetype;
        }

        public void setTimetype(String timetype) {
            this.timetype = timetype;
        }

        public String getPrices() {
            return prices;
        }

        public void setPrices(String prices) {
            this.prices = prices;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getAudition_time() {
            return audition_time;
        }

        public void setAudition_time(String audition_time) {
            this.audition_time = audition_time;
        }

        public String getAudition_type() {
            return audition_type;
        }

        public void setAudition_type(String audition_type) {
            this.audition_type = audition_type;
        }

        public String getAudition_address() {
            return audition_address;
        }

        public void setAudition_address(String audition_address) {
            this.audition_address = audition_address;
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

        public String getRefund_id() {
            return refund_id;
        }

        public void setRefund_id(String refund_id) {
            this.refund_id = refund_id;
        }

        public String getFormertime() {
            return formertime;
        }

        public void setFormertime(String formertime) {
            this.formertime = formertime;
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

        public String getIs_refund() {
            return is_refund;
        }

        public void setIs_refund(String is_refund) {
            this.is_refund = is_refund;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public String getPayment() {
            return payment;
        }

        public void setPayment(String payment) {
            this.payment = payment;
        }

        public String getUidtime() {
            return uidtime;
        }

        public void setUidtime(String uidtime) {
            this.uidtime = uidtime;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getAddress_name() {
            return address_name;
        }

        public void setAddress_name(String address_name) {
            this.address_name = address_name;
        }

        public String getAddress_phone() {
            return address_phone;
        }

        public void setAddress_phone(String address_phone) {
            this.address_phone = address_phone;
        }

        public String getAddress_address() {
            return address_address;
        }

        public void setAddress_address(String address_address) {
            this.address_address = address_address;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getIs_tui() {
            return is_tui;
        }

        public void setIs_tui(String is_tui) {
            this.is_tui = is_tui;
        }

        public String getSendtime() {
            return sendtime;
        }

        public void setSendtime(String sendtime) {
            this.sendtime = sendtime;
        }

        public String getEdittime() {
            return edittime;
        }

        public void setEdittime(String edittime) {
            this.edittime = edittime;
        }

        public String getEdit_desc() {
            return edit_desc;
        }

        public void setEdit_desc(String edit_desc) {
            this.edit_desc = edit_desc;
        }

        public String getShop_title() {
            return shop_title;
        }

        public void setShop_title(String shop_title) {
            this.shop_title = shop_title;
        }

        public List<GoodsBean> getGoods() {
            return goods;
        }

        public void setGoods(List<GoodsBean> goods) {
            this.goods = goods;
        }

        public static class GoodsBean {
            /**
             * id : 1
             * title : 测试商品1
             * simg : http://zhaolin-10029121.image.myqcloud.com/sample1489211525773340
             * price : 0.01
             * prices : 0.00
             * ads :
             * num : 2
             * goods_id : 1
             * endtime : 1492658710
             * shop_title : 测试家政店铺1
             */

            private String id;
            private String title;
            private String simg;
            private String price;
            private String prices;
            private String ads;
            private String num;
            private String goods_id;
            private String endtime;
            private String shop_title;
            private String unit;
            private String unit_desc;

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

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }

            public String getEndtime() {
                return endtime;
            }

            public void setEndtime(String endtime) {
                this.endtime = endtime;
            }

            public String getShop_title() {
                return shop_title;
            }

            public void setShop_title(String shop_title) {
                this.shop_title = shop_title;
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
