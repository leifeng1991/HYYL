package com.xxzlkj.shop.event;

/**
 * 购物车
 * Created by Administrator on 2017/4/12.
 */

public class ShopCartEvent {
    String goodsId;

    String shopCartId;

    String num;

    public ShopCartEvent(String goodsId, String shopCartId, String num) {
        this.goodsId = goodsId;
        this.shopCartId = shopCartId;
        this.num = num;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getShopCartId() {
        return shopCartId;
    }

    public void setShopCartId(String shopCartId) {
        this.shopCartId = shopCartId;
    }
}
