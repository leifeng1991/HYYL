package com.xxzlkj.shop.event;

/**
 * Created by Administrator on 2017/8/22.
 */

public class ShopCartAddressEvent {
    // 1：增加 2：修改 3：删除
    private int type;

    private String addressId;

    public ShopCartAddressEvent(int type, String addressId) {
        this.type = type;
        this.addressId = addressId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
}
