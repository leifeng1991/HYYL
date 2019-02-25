package com.xxzlkj.zhaolinshare.chat.event;

import io.rong.imlib.model.Message;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/5/22 11:48
 */
public class ReceiveNewMessageEvent {
    Message message;

    public ReceiveNewMessageEvent(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
