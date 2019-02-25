package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * by Administrator on 2017/3/29.
 */

public class HarvestAddressListBean extends BaseBean {

    private List<AddressBean.DataBean> data = new ArrayList<>();

    public List<AddressBean.DataBean> getData() {
        return data;
    }

    public void setData(List<AddressBean.DataBean> data) {
        this.data = data;
    }
}
