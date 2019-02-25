package com.xxzlkj.huayiyanglao.event;

/**
 * 描述:
 *
 * @author leifeng
 *         2018/3/9 16:09
 */


public class SelectServiceTimeEvent {
    private int current_item;

    public SelectServiceTimeEvent(int current_item) {
        this.current_item = current_item;
    }

    public int getCurrent_item() {
        return current_item;
    }

    public void setCurrent_item(int current_item) {
        this.current_item = current_item;
    }
}
