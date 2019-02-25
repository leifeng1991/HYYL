package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.io.Serializable;

/**
 * 描述:
 *
 * @author leifeng
 *         2018/1/5 9:59
 */


public class UserWatchBean extends BaseBean implements Serializable{

    /**
     * data : {"id":"1113041","fence_state":"2","fence_m":"600","fence_sos":"","imei":"111","fence_longitude":"116.48962363600732","fence_latitude":"39.89771993624397","watch_phone":"","affection_phone":"","watch_starttime":"","watch_endtime":"","watch_interval":"30"}
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
         * id : 1113041
         * fence_state : 2
         * fence_m : 600
         * fence_sos :
         * imei : 111
         * fence_longitude : 116.48962363600732
         * fence_latitude : 39.89771993624397
         * watch_phone :
         * affection_phone :
         * watch_starttime :
         * watch_endtime :
         * watch_interval : 30
         */

        private String id;
        private String fence_state;
        private String fence_m;
        private String fence_sos;
        private String imei;
        private String fence_longitude;
        private String fence_latitude;
        private String watch_phone;
        private String affection_phone;
        private String watch_starttime;
        private String watch_endtime;
        private String watch_interval;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFence_state() {
            return fence_state;
        }

        public void setFence_state(String fence_state) {
            this.fence_state = fence_state;
        }

        public String getFence_m() {
            return fence_m;
        }

        public void setFence_m(String fence_m) {
            this.fence_m = fence_m;
        }

        public String getFence_sos() {
            return fence_sos;
        }

        public void setFence_sos(String fence_sos) {
            this.fence_sos = fence_sos;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getFence_longitude() {
            return fence_longitude;
        }

        public void setFence_longitude(String fence_longitude) {
            this.fence_longitude = fence_longitude;
        }

        public String getFence_latitude() {
            return fence_latitude;
        }

        public void setFence_latitude(String fence_latitude) {
            this.fence_latitude = fence_latitude;
        }

        public String getWatch_phone() {
            return watch_phone;
        }

        public void setWatch_phone(String watch_phone) {
            this.watch_phone = watch_phone;
        }

        public String getAffection_phone() {
            return affection_phone;
        }

        public void setAffection_phone(String affection_phone) {
            this.affection_phone = affection_phone;
        }

        public String getWatch_starttime() {
            return watch_starttime;
        }

        public void setWatch_starttime(String watch_starttime) {
            this.watch_starttime = watch_starttime;
        }

        public String getWatch_endtime() {
            return watch_endtime;
        }

        public void setWatch_endtime(String watch_endtime) {
            this.watch_endtime = watch_endtime;
        }

        public String getWatch_interval() {
            return watch_interval;
        }

        public void setWatch_interval(String watch_interval) {
            this.watch_interval = watch_interval;
        }
    }
}
