package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

/**
 * 描述:
 *
 * @author leifeng
 *         2018/2/27 17:33
 */


public class UserCardNoStateBean extends BaseBean{

    /**
     * data : {"id":"1202","cardno":"530602198310102738","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1519723797922747825","state":"1","desc":"","uid":"1113044","addtime":"1519723940","name":"范东"}
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
         * id : 1202
         * cardno : 530602198310102738
         * simg : http://zhaolin-10029121.image.myqcloud.com/sample1519723797922747825
         * state : 1
         * desc :
         * uid : 1113044
         * addtime : 1519723940
         * name : 范东
         */

        private String id;
        private String cardno;
        private String simg;
        private String state;
        private String desc;
        private String uid;
        private String addtime;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCardno() {
            return cardno;
        }

        public void setCardno(String cardno) {
            this.cardno = cardno;
        }

        public String getSimg() {
            return simg;
        }

        public void setSimg(String simg) {
            this.simg = simg;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
