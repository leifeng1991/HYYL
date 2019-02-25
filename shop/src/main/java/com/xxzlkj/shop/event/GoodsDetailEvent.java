package com.xxzlkj.shop.event;

/**
 * 商品详情
 * Created by Administrator on 2017/4/12.
 */

public class GoodsDetailEvent {
    String id;

    public GoodsDetailEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
