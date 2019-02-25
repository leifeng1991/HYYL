package com.xxzlkj.huayiyanglao.event;

/**
 * 描述:
 *
 * @author leifeng
 *         2017/11/30 9:38
 */


public class DeleteAddressEvent {
    private String addressId;

    public DeleteAddressEvent(String addressId) {
        this.addressId = addressId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
}
