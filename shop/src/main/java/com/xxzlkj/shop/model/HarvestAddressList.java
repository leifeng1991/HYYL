package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/29.
 */

public class HarvestAddressList extends BaseBean {

    private List<AddressBean.DataBean> data = new ArrayList<>();

    public List<AddressBean.DataBean> getData() {
        return data;
    }

    public void setData(List<AddressBean.DataBean> data) {
        this.data = data;
    }
}
