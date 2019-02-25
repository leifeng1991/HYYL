package com.xxzlkj.shop.event;

/**
 * 售后详情变化
 * Created by Administrator on 2017/4/11.
 */

public class AfterSaleDetailEvent {
    private boolean isChange;

    public AfterSaleDetailEvent(boolean isChange) {
        this.isChange = isChange;
    }

    public boolean isChange() {
        return isChange;
    }

    public void setChange(boolean change) {
        isChange = change;
    }
}
