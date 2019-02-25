package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 售后详情
 * Created by Administrator on 2017/4/11.
 */

public class RefundDetail extends BaseBean{

    private List<RefundDataBean> data;

    public List<RefundDataBean> getData() {
        return data;
    }

    public void setData(List<RefundDataBean> data) {
        this.data = data;
    }

    public static class RefundDataBean implements Serializable{
        /**
         * id : 156484690
         * title : 买家申请售后，等待卖家处理
         * orderid : 1000000651
         * pid : 9503989
         * uid : 111361
         * addtime : 1492164886
         * statetime : 1492164911
         * state : 2
         * status : 1
         * content : 没理由
         * express :
         * express_num :
         * express_id :
         * num : 1
         * price : 0.01
         * desc :
         * type : 2
         * user_id : 111341
         * qu_mode : 2
         * tui_mode : 1
         * door_time : 1492164900
         * doot_content : 早上
         * store_id : 2222316
         * store_title : 店铺2
         * store_address : 店铺1店铺1店铺1店铺1店铺1店铺1店铺1店铺1店铺1
         * user_queren_time : 1492164936
         * chexiao_time : 0
         * queren_time : 1492167383
         * jujueshouhuo_time : 0
         * system : 1
         * del : 1
         * refundtime : 0
         * refuse :
         * username : hello
         * phone : 15036833790
         * store_phone : 15810544575
         * store_username : 张魁
         * reason : 商品有损坏
         * class_type : 1
         * style : 1
         * img : ["http://zhaolin-10029121.image.myqcloud.com/sample1492858698753"]
         * state_title : 同意退货
         * countdown : 2
         * time : 1492167383
         * coupon_price
         */

        private String id;
        private String title;
        private String orderid;
        private String pid;
        private String uid;
        private String addtime;
        private String statetime;
        private String state;
        private String status;
        private String content;
        private String express;
        private String express_num;
        private String express_id;
        private String num;
        private String price;
        private String desc;
        private String type;
        private String user_id;
        private String qu_mode;
        private String tui_mode;
        private String door_time;
        private String doot_content;
        private String store_id;
        private String store_title;
        private String store_address;
        private String user_queren_time;
        private String chexiao_time;
        private String queren_time;
        private String jujueshouhuo_time;
        private String system;
        private String del;
        private String refundtime;
        private String refuse;
        private String username;
        private String phone;
        private String store_phone;
        private String store_username;
        private String reason;
        private String class_type;
        private String style;
        private List<String> img;
        private String state_title;
        private String countdown;
        private String time;
        private String endtime;
        private String discount;
        private String coupon_price;

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
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

        public String getStatetime() {
            return statetime;
        }

        public void setStatetime(String statetime) {
            this.statetime = statetime;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getExpress() {
            return express;
        }

        public void setExpress(String express) {
            this.express = express;
        }

        public String getExpress_num() {
            return express_num;
        }

        public void setExpress_num(String express_num) {
            this.express_num = express_num;
        }

        public String getExpress_id() {
            return express_id;
        }

        public void setExpress_id(String express_id) {
            this.express_id = express_id;
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

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getQu_mode() {
            return qu_mode;
        }

        public void setQu_mode(String qu_mode) {
            this.qu_mode = qu_mode;
        }

        public String getTui_mode() {
            return tui_mode;
        }

        public void setTui_mode(String tui_mode) {
            this.tui_mode = tui_mode;
        }

        public String getDoor_time() {
            return door_time;
        }

        public void setDoor_time(String door_time) {
            this.door_time = door_time;
        }

        public String getDoot_content() {
            return doot_content;
        }

        public void setDoot_content(String doot_content) {
            this.doot_content = doot_content;
        }

        public String getStore_id() {
            return store_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public String getStore_title() {
            return store_title;
        }

        public void setStore_title(String store_title) {
            this.store_title = store_title;
        }

        public String getStore_address() {
            return store_address;
        }

        public void setStore_address(String store_address) {
            this.store_address = store_address;
        }

        public String getUser_queren_time() {
            return user_queren_time;
        }

        public void setUser_queren_time(String user_queren_time) {
            this.user_queren_time = user_queren_time;
        }

        public String getChexiao_time() {
            return chexiao_time;
        }

        public void setChexiao_time(String chexiao_time) {
            this.chexiao_time = chexiao_time;
        }

        public String getQueren_time() {
            return queren_time;
        }

        public void setQueren_time(String queren_time) {
            this.queren_time = queren_time;
        }

        public String getJujueshouhuo_time() {
            return jujueshouhuo_time;
        }

        public void setJujueshouhuo_time(String jujueshouhuo_time) {
            this.jujueshouhuo_time = jujueshouhuo_time;
        }

        public String getSystem() {
            return system;
        }

        public void setSystem(String system) {
            this.system = system;
        }

        public String getDel() {
            return del;
        }

        public void setDel(String del) {
            this.del = del;
        }

        public String getRefundtime() {
            return refundtime;
        }

        public void setRefundtime(String refundtime) {
            this.refundtime = refundtime;
        }

        public String getRefuse() {
            return refuse;
        }

        public void setRefuse(String refuse) {
            this.refuse = refuse;
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

        public String getStore_phone() {
            return store_phone;
        }

        public void setStore_phone(String store_phone) {
            this.store_phone = store_phone;
        }

        public String getStore_username() {
            return store_username;
        }

        public void setStore_username(String store_username) {
            this.store_username = store_username;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getClass_type() {
            return class_type;
        }

        public void setClass_type(String class_type) {
            this.class_type = class_type;
        }

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public List<String> getImg() {
            return img;
        }

        public void setImg(List<String> img) {
            this.img = img;
        }

        public String getState_title() {
            return state_title;
        }

        public void setState_title(String state_title) {
            this.state_title = state_title;
        }

        public String getCountdown() {
            return countdown;
        }

        public void setCountdown(String countdown) {
            this.countdown = countdown;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getCoupon_price() {
            return coupon_price;
        }

        public void setCoupon_price(String coupon_price) {
            this.coupon_price = coupon_price;
        }
    }
}
