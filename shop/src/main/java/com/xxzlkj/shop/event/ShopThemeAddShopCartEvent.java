package com.xxzlkj.shop.event;

import android.view.View;

/**
 * Created by Administrator on 2017/7/4.
 */

public class ShopThemeAddShopCartEvent {
    private String id;
    private View view;

    public ShopThemeAddShopCartEvent(String id, View view) {
        this.id = id;
        this.view = view;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
