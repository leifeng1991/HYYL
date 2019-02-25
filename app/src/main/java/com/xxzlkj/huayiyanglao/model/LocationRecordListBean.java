package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.List;

/**
 * 描述:定位记录
 *
 * @author leifeng
 *         2018/1/31 10:27
 */


public class LocationRecordListBean extends BaseBean{

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 144
         * uid : 1113041
         * addtime : 1496910478
         * deviceid : 865946021096597
         * cardno :
         * personno :
         * watchonline : 1
         * longitude : 116.434762
         * latitude : 39.92476
         * locrefreshrate : 60
         * familyphonecount :
         * familyrelation :
         * familyphonenumber :
         * disalarmswitch : 0
         * juli : 675
         * day : 2018-11-12
         * address : 北京市朝阳区朝外街道朝阳门北大街中国石化大厦
         */

        private String id;
        private String uid;
        private String addtime;
        private String deviceid;
        private String cardno;
        private String personno;
        private String watchonline;
        private String longitude;
        private String latitude;
        private String locrefreshrate;
        private String familyphonecount;
        private String familyrelation;
        private String familyphonenumber;
        private String disalarmswitch;
        private String juli;
        private String day;
        private String address;

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

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getDeviceid() {
            return deviceid;
        }

        public void setDeviceid(String deviceid) {
            this.deviceid = deviceid;
        }

        public String getCardno() {
            return cardno;
        }

        public void setCardno(String cardno) {
            this.cardno = cardno;
        }

        public String getPersonno() {
            return personno;
        }

        public void setPersonno(String personno) {
            this.personno = personno;
        }

        public String getWatchonline() {
            return watchonline;
        }

        public void setWatchonline(String watchonline) {
            this.watchonline = watchonline;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLocrefreshrate() {
            return locrefreshrate;
        }

        public void setLocrefreshrate(String locrefreshrate) {
            this.locrefreshrate = locrefreshrate;
        }

        public String getFamilyphonecount() {
            return familyphonecount;
        }

        public void setFamilyphonecount(String familyphonecount) {
            this.familyphonecount = familyphonecount;
        }

        public String getFamilyrelation() {
            return familyrelation;
        }

        public void setFamilyrelation(String familyrelation) {
            this.familyrelation = familyrelation;
        }

        public String getFamilyphonenumber() {
            return familyphonenumber;
        }

        public void setFamilyphonenumber(String familyphonenumber) {
            this.familyphonenumber = familyphonenumber;
        }

        public String getDisalarmswitch() {
            return disalarmswitch;
        }

        public void setDisalarmswitch(String disalarmswitch) {
            this.disalarmswitch = disalarmswitch;
        }

        public String getJuli() {
            return juli;
        }

        public void setJuli(String juli) {
            this.juli = juli;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
