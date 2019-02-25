package com.xxzlkj.huayiyanglao.receiver;

import android.content.Context;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/9/29 15:36
 */
public class RongNotificationReceiver extends PushMessageReceiver {

    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage message) {
        System.out.println("接收到推送消息onNotificationMessageArrived==" + message.toString());
//        message.setTargetUserName("兆邻");
//        message.setSenderName("兆邻");
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage message) {
        System.out.println("接收到推送消息点击了onNotificationMessageClicked==" + message.toString());

//        if (ZLConstants.Strings.SYSTEM_MESSAGE_ID.equals(message.getTargetId()) || ZLConstants.Strings.SYSTEM_MESSAGE_ID.equals(message.getSenderId())) {
//            // 系统消息
//            Intent intent = new Intent(context, SystemMessagesActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            context.startActivity(intent);
//            return true;
//        } else if (ZLConstants.Strings.INTERACTIVE_MESSAGE_ID.equals(message.getTargetId()) || ZLConstants.Strings.INTERACTIVE_MESSAGE_ID.equals(message.getSenderId())) {
//            // 互动消息
//            Intent intent = new Intent(context, InteractionLeaveWordActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            context.startActivity(intent);
//            return true;
//        } else
            // 普通聊天消息，走融云自己的逻辑
            return false;
    }
}
