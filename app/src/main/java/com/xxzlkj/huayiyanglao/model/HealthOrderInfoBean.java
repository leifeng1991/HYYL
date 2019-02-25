package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.List;

/**
 * 描述:
 *
 * @author leifeng
 *         2018/3/10 10:20
 */


public class HealthOrderInfoBean extends BaseBean{

    /**
     * data : {"id":"1000001128","uid":"1113041","title":"","price":"0.01","prices":"0.00","timetype":"0","unit":"1","unit_desc":"","addtime":"1520648181","state":"2","content":"","refuse":"","type":"1","orderid":"20180310101621558841982","account_id":"","ems":"0.00","del":"1","express":"","express_id":"","express_num":"","payment":"2","payment_num":"","buytime":"1520648204","finishtime":"0","sendtime":"0","address_name":"","address_longitude":"","address_latitude":"","address_address":"三里河一区","address_phone":"","is_tui":"1","endtime":"1520816400","uidtime":"0","formertime":"0","edittime":"0","edit_desc":"","is_refund":"1","shopid":"1","shoptime":"0","shop_uid":"0","shop_uname":"","shop_uphone":"","shop_uids":"0","discount":"0","health_property_id":"26","pid":"0","audition_time":"0","audition_type":"1","audition_address":"","time":"0","day":"","service_starttime":"1520816400","service_endtime":"1520820000","number_id":"0","res_type":"1","cancel_desc":"","ordinal":"0","shop_title":"西大望路店","shop_phone":"123456789","shop_tui_time":"120","shop_tui_deposit":"50","shop_no_tui_time":"60","tui_introduction":"服务开始时间大于2小时取消订单，全额退款；在1小时到2小时之间取消订单,退实付金额的50%；小于1小时时取消订单，不允许退款。","goods":[{"id":"134","title":"测试1","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1520645508784395","price":"0.01","prices":"0.00","ads":"测试","num":"1","goods_id":"134","unit":"1","unit_desc":"","endtime":"1520816400","shop_title":"西大望路店","shop_phone":"123456789"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1000001128
         * uid : 1113041
         * title :
         * price : 0.01
         * prices : 0.00
         * timetype : 0
         * unit : 1
         * unit_desc :
         * addtime : 1520648181
         * state : 2
         * content :
         * refuse :
         * type : 1
         * orderid : 20180310101621558841982
         * account_id :
         * ems : 0.00
         * del : 1
         * express :
         * express_id :
         * express_num :
         * payment : 2
         * payment_num :
         * buytime : 1520648204
         * finishtime : 0
         * sendtime : 0
         * address_name :
         * address_longitude :
         * address_latitude :
         * address_address : 三里河一区
         * address_phone :
         * is_tui : 1
         * endtime : 1520816400
         * uidtime : 0
         * formertime : 0
         * edittime : 0
         * edit_desc :
         * is_refund : 1
         * shopid : 1
         * shoptime : 0
         * shop_uid : 0
         * shop_uname :
         * shop_uphone :
         * shop_uids : 0
         * discount : 0
         * health_property_id : 26
         * pid : 0
         * audition_time : 0
         * audition_type : 1
         * audition_address :
         * time : 0
         * day :
         * service_starttime : 1520816400
         * service_endtime : 1520820000
         * number_id : 0
         * res_type : 1
         * cancel_desc :
         * ordinal : 0
         * shop_title : 西大望路店
         * shop_phone : 123456789
         * shop_tui_time : 120
         * shop_tui_deposit : 50
         * shop_no_tui_time : 60
         * tui_introduction : 服务开始时间大于2小时取消订单，全额退款；在1小时到2小时之间取消订单,退实付金额的50%；小于1小时时取消订单，不允许退款。
         * goods : [{"id":"134","title":"测试1","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1520645508784395","price":"0.01","prices":"0.00","ads":"测试","num":"1","goods_id":"134","unit":"1","unit_desc":"","endtime":"1520816400","shop_title":"西大望路店","shop_phone":"123456789"}]
         */

        private String id;
        private String uid;
        private String title;
        private String price;
        private String prices;
        private String timetype;
        private String unit;
        private String unit_desc;
        private String addtime;
        private String state;
        private String content;
        private String refuse;
        private String type;
        private String orderid;
        private String account_id;
        private String ems;
        private String del;
        private String express;
        private String express_id;
        private String express_num;
        private String payment;
        private String payment_num;
        private String buytime;
        private String finishtime;
        private String sendtime;
        private String address_name;
        private String address_longitude;
        private String address_latitude;
        private String address_address;
        private String address_phone;
        private String is_tui;
        private String endtime;
        private String uidtime;
        private String formertime;
        private String edittime;
        private String edit_desc;
        private String is_refund;
        private String shopid;
        private String shoptime;
        private String shop_uid;
        private String shop_uname;
        private String shop_uphone;
        private String shop_uids;
        private String discount;
        private String health_property_id;
        private String pid;
        private String audition_time;
        private String audition_type;
        private String audition_address;
        private String time;
        private String day;
        private String service_starttime;
        private String service_endtime;
        private String number_id;
        private String res_type;
        private String cancel_desc;
        private String ordinal;
        private String shop_title;
        private String shop_phone;
        private String shop_tui_time;
        private String shop_tui_deposit;
        private String shop_no_tui_time;
        private String tui_introduction;
        private String tui_price;
        private String tui_ratio;
        private String tui_addtime;
        private List<GoodsBean> goods;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public String getTimetype() {
            return timetype;
        }

        public void setTimetype(String timetype) {
            this.timetype = timetype;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getRefuse() {
            return refuse;
        }

        public void setRefuse(String refuse) {
            this.refuse = refuse;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public String getAccount_id() {
            return account_id;
        }

        public void setAccount_id(String account_id) {
            this.account_id = account_id;
        }

        public String getEms() {
            return ems;
        }

        public void setEms(String ems) {
            this.ems = ems;
        }

        public String getDel() {
            return del;
        }

        public void setDel(String del) {
            this.del = del;
        }

        public String getExpress() {
            return express;
        }

        public void setExpress(String express) {
            this.express = express;
        }

        public String getExpress_id() {
            return express_id;
        }

        public void setExpress_id(String express_id) {
            this.express_id = express_id;
        }

        public String getExpress_num() {
            return express_num;
        }

        public void setExpress_num(String express_num) {
            this.express_num = express_num;
        }

        public String getPayment() {
            return payment;
        }

        public void setPayment(String payment) {
            this.payment = payment;
        }

        public String getPayment_num() {
            return payment_num;
        }

        public void setPayment_num(String payment_num) {
            this.payment_num = payment_num;
        }

        public String getBuytime() {
            return buytime;
        }

        public void setBuytime(String buytime) {
            this.buytime = buytime;
        }

        public String getFinishtime() {
            return finishtime;
        }

        public void setFinishtime(String finishtime) {
            this.finishtime = finishtime;
        }

        public String getSendtime() {
            return sendtime;
        }

        public void setSendtime(String sendtime) {
            this.sendtime = sendtime;
        }

        public String getAddress_name() {
            return address_name;
        }

        public void setAddress_name(String address_name) {
            this.address_name = address_name;
        }

        public String getAddress_longitude() {
            return address_longitude;
        }

        public void setAddress_longitude(String address_longitude) {
            this.address_longitude = address_longitude;
        }

        public String getAddress_latitude() {
            return address_latitude;
        }

        public void setAddress_latitude(String address_latitude) {
            this.address_latitude = address_latitude;
        }

        public String getAddress_address() {
            return address_address;
        }

        public void setAddress_address(String address_address) {
            this.address_address = address_address;
        }

        public String getAddress_phone() {
            return address_phone;
        }

        public void setAddress_phone(String address_phone) {
            this.address_phone = address_phone;
        }

        public String getIs_tui() {
            return is_tui;
        }

        public void setIs_tui(String is_tui) {
            this.is_tui = is_tui;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public String getUidtime() {
            return uidtime;
        }

        public void setUidtime(String uidtime) {
            this.uidtime = uidtime;
        }

        public String getFormertime() {
            return formertime;
        }

        public void setFormertime(String formertime) {
            this.formertime = formertime;
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

        public String getIs_refund() {
            return is_refund;
        }

        public void setIs_refund(String is_refund) {
            this.is_refund = is_refund;
        }

        public String getShopid() {
            return shopid;
        }

        public void setShopid(String shopid) {
            this.shopid = shopid;
        }

        public String getShoptime() {
            return shoptime;
        }

        public void setShoptime(String shoptime) {
            this.shoptime = shoptime;
        }

        public String getShop_uid() {
            return shop_uid;
        }

        public void setShop_uid(String shop_uid) {
            this.shop_uid = shop_uid;
        }

        public String getShop_uname() {
            return shop_uname;
        }

        public void setShop_uname(String shop_uname) {
            this.shop_uname = shop_uname;
        }

        public String getShop_uphone() {
            return shop_uphone;
        }

        public void setShop_uphone(String shop_uphone) {
            this.shop_uphone = shop_uphone;
        }

        public String getShop_uids() {
            return shop_uids;
        }

        public void setShop_uids(String shop_uids) {
            this.shop_uids = shop_uids;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getHealth_property_id() {
            return health_property_id;
        }

        public void setHealth_property_id(String health_property_id) {
            this.health_property_id = health_property_id;
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

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
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

        public String getNumber_id() {
            return number_id;
        }

        public void setNumber_id(String number_id) {
            this.number_id = number_id;
        }

        public String getRes_type() {
            return res_type;
        }

        public void setRes_type(String res_type) {
            this.res_type = res_type;
        }

        public String getCancel_desc() {
            return cancel_desc;
        }

        public void setCancel_desc(String cancel_desc) {
            this.cancel_desc = cancel_desc;
        }

        public String getOrdinal() {
            return ordinal;
        }

        public void setOrdinal(String ordinal) {
            this.ordinal = ordinal;
        }

        public String getShop_title() {
            return shop_title;
        }

        public void setShop_title(String shop_title) {
            this.shop_title = shop_title;
        }

        public String getShop_phone() {
            return shop_phone;
        }

        public void setShop_phone(String shop_phone) {
            this.shop_phone = shop_phone;
        }

        public String getShop_tui_time() {
            return shop_tui_time;
        }

        public void setShop_tui_time(String shop_tui_time) {
            this.shop_tui_time = shop_tui_time;
        }

        public String getShop_tui_deposit() {
            return shop_tui_deposit;
        }

        public void setShop_tui_deposit(String shop_tui_deposit) {
            this.shop_tui_deposit = shop_tui_deposit;
        }

        public String getShop_no_tui_time() {
            return shop_no_tui_time;
        }

        public void setShop_no_tui_time(String shop_no_tui_time) {
            this.shop_no_tui_time = shop_no_tui_time;
        }

        public String getTui_introduction() {
            return tui_introduction;
        }

        public void setTui_introduction(String tui_introduction) {
            this.tui_introduction = tui_introduction;
        }

        public String getTui_price() {
            return tui_price;
        }

        public void setTui_price(String tui_price) {
            this.tui_price = tui_price;
        }

        public String getTui_ratio() {
            return tui_ratio;
        }

        public void setTui_ratio(String tui_ratio) {
            this.tui_ratio = tui_ratio;
        }

        public String getTui_addtime() {
            return tui_addtime;
        }

        public void setTui_addtime(String tui_addtime) {
            this.tui_addtime = tui_addtime;
        }

        public List<GoodsBean> getGoods() {
            return goods;
        }

        public void setGoods(List<GoodsBean> goods) {
            this.goods = goods;
        }

        public static class GoodsBean {
            /**
             * id : 134
             * title : 测试1
             * simg : http://zhaolin-10029121.image.myqcloud.com/sample1520645508784395
             * price : 0.01
             * prices : 0.00
             * ads : 测试
             * num : 1
             * goods_id : 134
             * unit : 1
             * unit_desc :
             * endtime : 1520816400
             * shop_title : 西大望路店
             * shop_phone : 123456789
             */

            private String id;
            private String title;
            private String simg;
            private String price;
            private String prices;
            private String ads;
            private String num;
            private String goods_id;
            private String unit;
            private String unit_desc;
            private String endtime;
            private String shop_title;
            private String shop_phone;

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

            public String getShop_phone() {
                return shop_phone;
            }

            public void setShop_phone(String shop_phone) {
                this.shop_phone = shop_phone;
            }
        }
    }
}
