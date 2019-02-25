package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

/**
 * 描述:
 *
 * @author leifeng
 *         2017/12/6 13:44
 */


public class CallPhoneBean extends BaseBean{

    /**
     * data : {"uid":"1113034","netPhone":"1508903432","kfPhone":"1508903433","famPhone":"1508903434"}
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
         * uid : 1113034
         * netPhone : 1508903432
         * kfPhone : 1508903433
         * famPhone : 1508903434
         */

        private String uid;
        private String netPhone;
        private String kfPhone;
        private String famPhone;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getNetPhone() {
            return netPhone;
        }

        public void setNetPhone(String netPhone) {
            this.netPhone = netPhone;
        }

        public String getKfPhone() {
            return kfPhone;
        }

        public void setKfPhone(String kfPhone) {
            this.kfPhone = kfPhone;
        }

        public String getFamPhone() {
            return famPhone;
        }

        public void setFamPhone(String famPhone) {
            this.famPhone = famPhone;
        }
    }
}
