package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/15.
 */

public class SpelledGroupListBean extends BaseBean {

    private List<TeamBean> data = new ArrayList<>();

    public List<TeamBean> getData() {
        return data;
    }

    public void setData(List<TeamBean> data) {
        this.data = data;
    }

}
