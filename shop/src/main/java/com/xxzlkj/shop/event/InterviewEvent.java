package com.xxzlkj.shop.event;

/**
 * Created by Administrator on 2017/8/12.
 */

public class InterviewEvent {
    private String address;

    private String addressId;

    public InterviewEvent(String address, String addressId) {
        this.address = address;
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
}
