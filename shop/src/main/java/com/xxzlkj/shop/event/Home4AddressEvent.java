package com.xxzlkj.shop.event;


import com.xxzlkj.shop.model.AddressBean;


public class Home4AddressEvent {
    private boolean isAddOrDelete;

    private AddressBean.DataBean bean;

    public Home4AddressEvent(boolean isRefresh, AddressBean.DataBean bean) {
        this.isAddOrDelete = isRefresh;
        this.bean = bean;
    }

    public boolean isAddOrDelete() {
        return isAddOrDelete;
    }

    public void setAddOrDelete(boolean addOrDelete) {
        isAddOrDelete = addOrDelete;
    }

    public AddressBean.DataBean getBean() {
        return bean;
    }

    public void setBean(AddressBean.DataBean bean) {
        this.bean = bean;
    }
}
