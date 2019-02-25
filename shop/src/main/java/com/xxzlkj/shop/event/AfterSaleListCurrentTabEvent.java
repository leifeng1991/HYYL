package com.xxzlkj.shop.event;

/**
 * 描述:设置当前tab
 *
 * @author zhangrq
 *         2017/3/24 11:03
 */
public class AfterSaleListCurrentTabEvent {
    private int position;

    public AfterSaleListCurrentTabEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
