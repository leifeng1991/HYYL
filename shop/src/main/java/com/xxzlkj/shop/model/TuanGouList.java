package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 团购集合
 * Created by Administrator on 2017/3/25.
 */

public class TuanGouList extends BaseBean {

    private List<TuanGou> data = new ArrayList<>();

    public List<TuanGou> getData() {
        return data;
    }

    public void setData(List<TuanGou> data) {
        this.data = data;
    }

}
