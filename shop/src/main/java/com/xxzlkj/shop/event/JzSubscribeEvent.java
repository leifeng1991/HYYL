package com.xxzlkj.shop.event;

/**
 * 家政 保洁 立即下单
 * Created by Administrator on 2017/4/21.
 */

public class JzSubscribeEvent {
    // 地址id
    private String addressId;
    // 详细地址
    private String address;
    // 手机号
    private String phone;

    public JzSubscribeEvent(String addressId, String phone, String address) {
        this.addressId = addressId;
        this.phone = phone;
        this.address = address;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
