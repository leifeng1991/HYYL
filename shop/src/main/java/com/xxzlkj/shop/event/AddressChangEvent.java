package com.xxzlkj.shop.event;


import com.xxzlkj.shop.model.AddressBean;

/**
 * 地址变化
 * Created by Administrator on 2017/3/30.
 */

public class AddressChangEvent {
    private boolean isChange;
    private String addressId;
    private AddressBean.DataBean addressBean;

    public AddressChangEvent(boolean isChange, String addressId, AddressBean.DataBean addressBean) {
        this.isChange = isChange;
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

    public boolean isChange() {
        return isChange;
    }

    public void setChange(boolean change) {
        isChange = change;
    }

}
