package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.List;

public class OrderDesBean extends BaseBean {


    /**
     * data : {"id":"1000000418","orderid":"20170331173928203347","uid":"111361","price":"0.01","addtime":"1490953168","payment":"0","uidtime":"0","state":"1","address_name":"leifeng","address_phone":"15036833790","address_address":"北京市朝阳区北京市朝阳区人保寿险大厦","content":"","store_uid":"0","is_tui":1,"sendtime":"0","username":"","phone":"","goods":[{"id":"9503892","title":"尼康相机玫瑰金 20G","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1490781947940236","price":"0.01","prices":"5000.00","ads":"尼康相机描述","num":"1"}]}
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
         * id : 1000000418
         * orderid : 20170331173928203347
         * uid : 111361
         * price : 0.01
         * addtime : 1490953168
         * payment : 0
         * uidtime : 0
         * state : 1
         * address_name : leifeng
         * address_phone : 15036833790
         * address_address : 北京市朝阳区北京市朝阳区人保寿险大厦
         * content :
         * store_uid : 0
         * is_tui : 1
         * sendtime : 0
         * username :
         * phone :
         * goods : [{"id":"9503892","title":"尼康相机玫瑰金 20G","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1490781947940236","price":"0.01","prices":"5000.00","ads":"尼康相机描述","num":"1"}]
         */

        private String id;
        private String orderid;
        private String uid;
        private String price;
        private String addtime;
        private String payment;
        private String uidtime;
        private String state;
        private String address_name;
        private String address_phone;
        private String address_address;
        private String content;
        private String store_uid;
        private String store_address;
        private String store_phone;
        private String store_title;
        private String is_tui;
        private String sendtime;
        private String username;
        private String phone;
        private String delivery;
        private String discount;// 是否为折扣订单，0为普通订单，1-9为1-9折，折扣订单
        private String prices;// 折扣前价格;
        private String is_refund;
        private String store_id;// 门店id
        private String activity_type;// 订单类型 0：正常 1：预售 2：团购
        private String groupon_team_id;// 团购时加入团购的组id
        private List<GoodsBean> goods;

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

        public String getStore_id() {
            return store_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public String getIs_refund() {
            return is_refund;
        }

        public void setIs_refund(String is_refund) {
            this.is_refund = is_refund;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getPrices() {
            return prices;
        }

        public void setPrices(String prices) {
            this.prices = prices;
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

        public String getStore_title() {
            return store_title;
        }

        public void setStore_title(String store_title) {
            this.store_title = store_title;
        }

        public String getDelivery() {
            return delivery;
        }

        public void setDelivery(String delivery) {
            this.delivery = delivery;
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

        public String getStore_uid() {
            return store_uid;
        }

        public void setStore_uid(String store_uid) {
            this.store_uid = store_uid;
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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public List<GoodsBean> getGoods() {
            return goods;
        }

        public void setGoods(List<GoodsBean> goods) {
            this.goods = goods;
        }

        public static class GoodsBean {
            /**
             * id : 9503892
             * title : 尼康相机玫瑰金 20G
             * simg : http://zhaolin-10029121.image.myqcloud.com/sample1490781947940236
             * price : 0.01
             * prices : 5000.00
             * ads : 尼康相机描述
             * num : 1
             */
            private String activity_desc;
            private String activity_type;
            private String id;
            private String title;
            private String simg;
            private String price;
            private String prices;
            private String ads;
            private String num;

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
