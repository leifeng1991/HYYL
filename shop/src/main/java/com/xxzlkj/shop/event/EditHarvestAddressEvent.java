package com.xxzlkj.shop.event;

/**
 * 编辑/添加地址
 * Created by Administrator on 2017/4/26.
 */

public class EditHarvestAddressEvent {
    private String address;
    private double latitude;
    private double longitude;

    public EditHarvestAddressEvent(String address, double latitude, double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
