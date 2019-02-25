package com.xxzlkj.licallife.event;

/**
 * 本地生活售后
 * Created by Administrator on 2017/5/10.
 */

public class AddRefundEvent {
    private boolean isUpdata;

    public AddRefundEvent(boolean isUpdata) {
        this.isUpdata = isUpdata;
    }

    public boolean isUpdata() {
        return isUpdata;
    }

    public void setUpdata(boolean updata) {
        isUpdata = updata;
    }
}
