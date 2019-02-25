package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 为你推荐商品
 * Created by Administrator on 2017/3/23.
 */

public class RecommendGoods extends BaseBean {

    private List<Goods> data = new ArrayList<>();

    public List<Goods> getData() {
        return data;
    }

    public void setData(List<Goods> data) {
        this.data = data;
    }

}
