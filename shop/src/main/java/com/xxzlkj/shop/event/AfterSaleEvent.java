package com.xxzlkj.shop.event;

/**
 * 售后列表和退款详情列表变化
 * Created by Administrator on 2017/4/11.
 */

public class AfterSaleEvent {
    private boolean isChange;

    public AfterSaleEvent(boolean isChange) {
        this.isChange = isChange;
    }

    public boolean isChange() {
        return isChange;
    }

    public void setChange(boolean change) {
        isChange = change;
    }
}
