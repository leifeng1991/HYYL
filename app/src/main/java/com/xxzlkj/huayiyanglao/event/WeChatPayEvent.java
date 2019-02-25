package com.xxzlkj.huayiyanglao.event;

/**
 * 微信支状态反馈
 * Created by Administrator on 2017/4/13.
 */

public class WeChatPayEvent {
    private int state;

    public WeChatPayEvent(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
