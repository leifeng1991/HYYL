package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

/**
 * 微信预支付订单
 * Created by Administrator on 2017/4/13.
 */

public class WeChatPay extends BaseBean {

    /**
     * data : {"appid":"wxfb1b4aba25f9403c","partnerid":"1440164202","prepayid":"wx20170413150442487f31eca40699912840","package":"Sign=WXPay","noncestr":"BAebKnKh9T08j7Z5","timestamp":1492067082,"sign":"F710581A2BB5DD403C16B3E6043AB49A"}
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
         * appid : wxfb1b4aba25f9403c
         * partnerid : 1440164202
         * prepayid : wx20170413150442487f31eca40699912840
         * package : Sign=WXPay
         * noncestr : BAebKnKh9T08j7Z5
         * timestamp : 1492067082
         * sign : F710581A2BB5DD403C16B3E6043AB49A
         */

        private String appid;
        private String partnerid;
        private String prepayid;
        private String packageX;
        private String noncestr;
        private String timestamp;
        private String sign;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
