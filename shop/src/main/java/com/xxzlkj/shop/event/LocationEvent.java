package com.xxzlkj.shop.event;

/**
 * 定位
 * Created by Administrator on 2017/4/26.
 */

public class LocationEvent {
    private boolean isFinish;

    public LocationEvent(boolean isFinish) {
        this.isFinish = isFinish;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }
}
