package com.xxzlkj.shop.event;

import android.view.View;

import com.xxzlkj.shop.model.Goods;


/**
 * 添加购物车动画
 * Created by Administrator on 2017/6/29.
 */

public class AddShopCartActionEvent {
    private View view;
    private Goods goods;
    private boolean flag;

    public AddShopCartActionEvent(View view, Goods goods, boolean flag) {
        this.view = view;
        this.goods = goods;
        this.flag = flag;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
