package com.xxzlkj.shop.event;

/**
 * Created by Administrator on 2017/8/18.
 */

public class ShopCartListEvent {
    private boolean isRefresh;

    public ShopCartListEvent(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }
}
