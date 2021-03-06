package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

/**
 * 描述:
 *
 * @author leifeng
 *         2018/2/26 15:20
 */


public class WatchGuardianshipInfoBean extends BaseBean{

    /**
     * data : {"id":"1282","pid":"111346","uid":"1113041","state":"1","addtime":"1519625789","name":"和","del":"1","username":"","simg":"","phone":"15036833790"}
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
         * id : 1282
         * pid : 111346
         * uid : 1113041
         * state : 1
         * addtime : 1519625789
         * name : 和
         * del : 1
         * username :
         * simg :
         * phone : 15036833790
         */

        private String id;
        private String pid;
        private String uid;
        private String state;
        private String addtime;
        private String name;
        private String del;
        private String username;
        private String simg;
        private String phone;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
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

        public String getDel() {
            return del;
        }

        public void setDel(String del) {
            this.del = del;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getSimg() {
            return simg;
        }

        public void setSimg(String simg) {
            this.simg = simg;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
