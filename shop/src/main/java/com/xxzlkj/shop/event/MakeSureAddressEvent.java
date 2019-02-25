package com.xxzlkj.shop.event;


import com.xxzlkj.shop.model.AddressBean;

/**
 * 地址变化
 * Created by Administrator on 2017/3/30.
 */

public class MakeSureAddressEvent {
    // 1:增加 2：修改 3：删除
    private int type;
    private String addressId;
    private AddressBean.DataBean addressBean;

    public MakeSureAddressEvent(int type, String addressId, AddressBean.DataBean addressBean) {
        this.type = type;
        this.addressId = addressId;
        this.addressBean = addressBean;
    }

    public AddressBean.DataBean getDetaileAddress() {
        return addressBean;
    }

    public void setDetaileAddress(AddressBean.DataBean addressBean) {
        this.addressBean = addressBean;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
