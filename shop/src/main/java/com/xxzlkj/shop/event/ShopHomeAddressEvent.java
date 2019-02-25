package com.xxzlkj.shop.event;


import com.xxzlkj.shop.model.AddressBean;

/**
 * Created by Administrator on 2017/8/21.
 */

public class ShopHomeAddressEvent {
    private boolean isAddOrDelete;
    private AddressBean.DataBean addressBean;

    public ShopHomeAddressEvent(boolean isChange,AddressBean.DataBean addressBean) {
        this.isAddOrDelete = isChange;
        this.addressBean = addressBean;
    }

    public AddressBean.DataBean getDetaileAddress() {
        return addressBean;
    }

    public void setDetaileAddress(AddressBean.DataBean addressBean) {
        this.addressBean = addressBean;
    }


    public boolean isAddOrDelete() {
        return isAddOrDelete;
    }

    public void setAddOrDelete(boolean addOrDelete) {
        isAddOrDelete = addOrDelete;
    }
}
