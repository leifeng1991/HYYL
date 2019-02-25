package com.xxzlkj.licallife.event;

/**
 * 描述:设置当前tab
 *
 * @author zhangrq
 *         2017/3/24 11:03
 */
public class LocalLifeListCurrentTabEvent {
    private int position;

    public LocalLifeListCurrentTabEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
