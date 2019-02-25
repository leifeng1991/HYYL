package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.List;

public class OrderDesBean extends BaseBean {

    /**
     * data : {"id":"4","orderid":"2017113015353125251326","is_refund":"1","uid":"1113034","price":"25.50","addtime":"1512027331","comment":"0","delivery":"1","store_id":"2222318","state":"2","address_name":"真武","address_phone":"15033333333","address_address":"北京市西城区复兴门外真武庙二条真武","content":"","is_tui":1,"starttime":"1512099000","stoptime":"1512108000","consume_code":null,"store_title":"兆邻直营","store_phone":"15661656456","store_address":"西大望路","goods":[{"id":"17","title":"猪肉茴香","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1495607567772363","price":"15.00","ads":"浓香美味","num":"1"},{"id":"16","title":"加多宝凉茶来一份","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1495158619373550","price":"3.50","ads":"比市场价还便宜5毛","num":"3"}],"prices":"18.5"}
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
         * id : 4
         * orderid : 2017113015353125251326
         * is_refund : 1
         * uid : 1113034
         * price : 25.50
         * addtime : 1512027331
         * comment : 0
         * delivery : 1
         * store_id : 2222318
         * state : 2
         * address_name : 真武
         * address_phone : 15033333333
         * address_address : 北京市西城区复兴门外真武庙二条真武
         * content :
         * is_tui : 1
         * starttime : 1512099000
         * stoptime : 1512108000
         * consume_code : null
         * store_title : 兆邻直营
         * store_phone : 15661656456
         * store_address : 西大望路
         * goods : [{"id":"17","title":"猪肉茴香","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1495607567772363","price":"15.00","ads":"浓香美味","num":"1"},{"id":"16","title":"加多宝凉茶来一份","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1495158619373550","price":"3.50","ads":"比市场价还便宜5毛","num":"3"}]
         * prices : 18.5
         */

        private String id;
        private String orderid;
        private String is_refund;
        private String uid;
        private String price;
        private String addtime;
        private String comment;
        private String delivery;
        private String store_id;
        private String state;
        private String address_name;
        private String address_phone;
        private String address_address;
        private String content;
        private String is_tui;
        private String starttime;
        private String stoptime;
        private String consume_code;
        private String store_title;
        private String store_phone;
        private String store_address;
        private String prices;
        private String is_urge;
        private String overtime;
        private String upmsg;
        private String downmsg;
        private List<OrderListBean.DataBean.GoodsBean> goods;

        public String getUpmsg() {
            return upmsg;
        }

        public void setUpmsg(String upmsg) {
            this.upmsg = upmsg;
        }

        public String getDownmsg() {
            return downmsg;
        }

        public void setDownmsg(String downmsg) {
            this.downmsg = downmsg;
        }

        public String getOvertime() {
            return overtime;
        }

        public void setOvertime(String overtime) {
            this.overtime = overtime;
        }

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

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getDelivery() {
            return delivery;
        }

        public void setDelivery(String delivery) {
            this.delivery = delivery;
        }

        public String getStore_id() {
            return store_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
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

        public String getConsume_code() {
            return consume_code;
        }

        public void setConsume_code(String consume_code) {
            this.consume_code = consume_code;
        }

        public String getStore_title() {
            return store_title;
        }

        public void setStore_title(String store_title) {
            this.store_title = store_title;
        }

        public String getStore_phone() {
            return store_phone;
        }

        public void setStore_phone(String store_phone) {
            this.store_phone = store_phone;
        }

        public String getStore_address() {
            return store_address;
        }

        public void setStore_address(String store_address) {
            this.store_address = store_address;
        }

        public String getPrices() {
            return prices;
        }

        public void setPrices(String prices) {
            this.prices = prices;
        }

        public List<OrderListBean.DataBean.GoodsBean> getGoods() {
            return goods;
        }

        public void setGoods(List<OrderListBean.DataBean.GoodsBean> goods) {
            this.goods = goods;
        }

    }
}
