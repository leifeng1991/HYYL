package com.xxzlkj.huayiyanglao.listener;


import android.content.Context;
import android.os.Vibrator;
import android.support.annotation.NonNull;

import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.zhaolinshare.chat.event.ReceiveNewMessageEvent;

import org.greenrobot.eventbus.EventBus;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;
import io.rong.push.RongPushClient;
import io.rong.push.notification.PushNotificationMessage;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/5/22 11:27
 */
public class MyOnReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {
    private final Context mContent;

    public MyOnReceiveMessageListener(Context context) {
        this.mContent = context;
    }

    /**
     * 收到消息的处理。
     *
     * @param message 收到的消息实体。
     * @param left    剩余未拉取消息数目。
     */
    @Override
    public boolean onReceived(Message message, int left) {
        //开发者根据自己需求自行处理
        System.out.println("接收到新消息" + message);
        // 发送通知。如果使用 IMLib 开发，当应用在后台需要弹后台通知时，可以直接调用此函数弹出通知。
        if (ZhaoLinApplication.getInstance().isForeground()) {
            // 在前台，发送震动提示
            Vibrator v = (Vibrator) mContent.getSystemService(Context.VIBRATOR_SERVICE);
            if (v != null)
                v.vibrate(new long[]{50, 150, 50, 150}, -1);
        } else {
            // 在后台，发送消息通知
            PushNotificationMessage notificationMessage = getPushNotificationMessage(message);
            RongPushClient.sendNotification(mContent, notificationMessage);
        }
        // 发送Event
        EventBus.getDefault().postSticky(new ReceiveNewMessageEvent(message));
        return false;
    }

    @NonNull
    private PushNotificationMessage getPushNotificationMessage(Message message) {
//        private String pushId;
//        private RongPushClient.ConversationType conversationType;
//        private long receivedTime;
//        private String objectName;
//        private String senderId;
//        private String senderName;
//        private Uri senderPortrait;
//        private String targetId;
//        private String targetUserName;
//        private String pushTitle;
//        private String pushContent;
//        private String pushData;
//        private String extra;
//        private String isFromPush;
        PushNotificationMessage notificationMessage = new PushNotificationMessage();
//        notificationMessage.setPushId(message.getpushId);
        notificationMessage.setConversationType(RongPushClient.ConversationType.setValue(message.getConversationType().getValue()));
        notificationMessage.setReceivedTime(message.getReceivedTime());
        notificationMessage.setObjectName(message.getObjectName());
        notificationMessage.setSenderId(message.getSenderUserId());
//        notificationMessage.setSenderName("发送者的名字");
//        notificationMessage.setSenderPortrait(message.getsenderPortrait());
        notificationMessage.setTargetId(message.getTargetId());
        notificationMessage.setTargetUserName("兆邻");// 一直都显示
//        notificationMessage.setPushTitle("推送的标题");
        MessageContent content = message.getContent();
        if (content instanceof TextMessage)
            notificationMessage.setPushContent(((TextMessage) content).getContent());// 第一条消息显示这个
//        notificationMessage.setPushData("推送的数据");
        notificationMessage.setExtra(message.getExtra());
//        notificationMessage.setPushFlag(message.());
        return notificationMessage;
    }
}
