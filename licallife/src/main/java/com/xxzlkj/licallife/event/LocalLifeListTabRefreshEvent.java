package com.xxzlkj.licallife.event;

/**
 * 描述:设置第几个刷新
 *
 * @author zhangrq
 *         2017/3/24 11:03
 */
public class LocalLifeListTabRefreshEvent {
    private int position;

    public LocalLifeListTabRefreshEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
