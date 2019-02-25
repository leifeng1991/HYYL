package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.io.Serializable;

/**
 * 地址
 * Created by Administrator on 2017/6/30.
 */

public class AddressBean extends BaseBean implements Serializable{

    /**
     * data : {"id":"194","phone":"15099999999","state":1,"name":"共和国","address":"哈哈哈","longitude":"116.435781","latitude":"39.926605"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable, AddressInfoInterface {
        /**
         * id : 194
         * phone : 15099999999
         * state : 1
         * name : 共和国
         * address : 哈哈哈
         * longitude : 116.435781
         * latitude : 39.926605
         */

        private String id;
        private String phone;
        private String state;
        private String name;
        private String identity;
        private String address;
        private String position;
        // 经度
        private String longitude;
        // 纬度
        private String latitude;

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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
    }

    public interface AddressInfoInterface {
        String getId();

        String getAddress();

        String getLongitude();

        String getLatitude();
    }

}
