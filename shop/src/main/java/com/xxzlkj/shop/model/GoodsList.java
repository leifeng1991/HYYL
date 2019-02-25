package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品列表
 * Created by Administrator on 2017/3/21.
 */

public class GoodsList extends BaseBean{

    private List<Goods> data=new ArrayList<>();

    public List<Goods> getData() {
        return data;
    }

    public void setData(List<Goods> data) {
        this.data = data;
    }
}
