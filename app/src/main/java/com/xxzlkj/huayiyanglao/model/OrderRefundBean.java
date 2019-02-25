package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/3/31 15:36
 */
public class OrderRefundBean extends BaseBean {

    /**
     * data : {"addtime":"1490943863","chexiao_time":"0","content":"","del":"1","desc":"","door_time":"0","doot_content":"","express":"","express_id":"","express_num":"","id":"156484588","jujueshouhuo_time":"0","num":"1","orderid":"1000000393","pid":"0","price":"0.01","qu_mode":"0","qu_mode_time":"0","queren_time":"0","state":"1","statetime":"0","store_address":"","store_id":"2222316","store_title":"","system":"1","title":"买错了","tui_mode":"0","type":"1","uid":"111345","user_id":"111355","user_queren_time":"0"}
     */

    private DataBean data=new DataBean();

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * addtime : 1490943863
         * chexiao_time : 0
         * content :
         * del : 1
         * desc :
         * door_time : 0
         * doot_content :
         * express :
         * express_id :
         * express_num :
         * id : 156484588
         * jujueshouhuo_time : 0
         * num : 1
         * orderid : 1000000393
         * pid : 0
         * price : 0.01
         * qu_mode : 0
         * qu_mode_time : 0
         * queren_time : 0
         * state : 1
         * statetime : 0
         * store_address :
         * store_id : 2222316
         * store_title :
         * system : 1
         * title : 买错了
         * tui_mode : 0
         * type : 1
         * uid : 111345
         * user_id : 111355
         * user_queren_time : 0
         */

        private String addtime;
        private String phone;
        private String warning;
        private String chexiao_time;
        private String content;
        private String del;
        private String desc;
        private String door_time;
        private String doot_content;
        private String express;
        private String express_id;
        private String express_num;
        private String id;
        private String jujueshouhuo_time;
        private String num;
        private String orderid;
        private String pid;
        private String price;
        private String qu_mode;
        private String qu_mode_time;
        private String queren_time;
        private String state;
        private String statetime;
        private String store_address;
        private String store_id;
        private String store_title;
        private String system;
        private String title;
        private String tui_mode;
        private String type;
        private String uid;
        private String user_id;
        private String user_queren_time;
        private String status;

        public void setStatus(String status) {
            this.status = status;
        }

        public String getWarning() {
            return warning;
        }

        public void setWarning(String warning) {
            this.warning = warning;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getChexiao_time() {
            return chexiao_time;
        }

        public void setChexiao_time(String chexiao_time) {
            this.chexiao_time = chexiao_time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDel() {
            return del;
        }

        public void setDel(String del) {
            this.del = del;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getJujueshouhuo_time() {
            return jujueshouhuo_time;
        }

        public void setJujueshouhuo_time(String jujueshouhuo_time) {
            this.jujueshouhuo_time = jujueshouhuo_time;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getQu_mode() {
            return qu_mode;
        }

        public void setQu_mode(String qu_mode) {
            this.qu_mode = qu_mode;
        }

        public String getQu_mode_time() {
            return qu_mode_time;
        }

        public void setQu_mode_time(String qu_mode_time) {
            this.qu_mode_time = qu_mode_time;
        }

        public String getQueren_time() {
            return queren_time;
        }

        public void setQueren_time(String queren_time) {
            this.queren_time = queren_time;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getStatetime() {
            return statetime;
        }

        public void setStatetime(String statetime) {
            this.statetime = statetime;
        }

        public String getStore_address() {
            return store_address;
        }

        public void setStore_address(String store_address) {
            this.store_address = store_address;
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

        public String getSystem() {
            return system;
        }

        public void setSystem(String system) {
            this.system = system;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTui_mode() {
            return tui_mode;
        }

        public void setTui_mode(String tui_mode) {
            this.tui_mode = tui_mode;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_queren_time() {
            return user_queren_time;
        }

        public void setUser_queren_time(String user_queren_time) {
            this.user_queren_time = user_queren_time;
        }

        public String getStatus() {
            return status;
        }
    }
}
