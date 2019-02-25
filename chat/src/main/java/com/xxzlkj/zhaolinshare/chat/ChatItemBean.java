package com.xxzlkj.zhaolinshare.chat;

import android.net.Uri;

import io.rong.imlib.model.Message;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/3/29 21:47
 */

public class ChatItemBean {
    private String textContent;
    private Uri imageLocalUri;
    private Uri voiceUri;
    private int voiceDuration;
    private int style;
    private boolean isRead;
    private int messageId;
    private Message.MessageDirection messageDirection;

    public ChatItemBean() {
    }

    public Message.MessageDirection getMessageDirection() {
        return messageDirection;
    }

    public void setMessageDirection(Message.MessageDirection messageDirection) {
        this.messageDirection = messageDirection;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public Uri getImageLocalUri() {
        return imageLocalUri;
    }

    public void setImageLocalUri(Uri imageLocalUri) {
        this.imageLocalUri = imageLocalUri;
    }

    public Uri getVoiceUri() {
        return voiceUri;
    }

    public void setVoiceUri(Uri voiceUri) {
        this.voiceUri = voiceUri;
    }

    public int getVoiceDuration() {
        return voiceDuration;
    }

    public void setVoiceDuration(int voiceDuration) {
        this.voiceDuration = voiceDuration;
    }
}
