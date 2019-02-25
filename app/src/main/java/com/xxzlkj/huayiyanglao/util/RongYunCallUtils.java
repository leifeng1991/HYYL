package com.xxzlkj.huayiyanglao.util;

import java.util.ArrayList;
import java.util.List;

import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallCommon;
import io.rong.calllib.RongCallSession;
import io.rong.imlib.model.Conversation;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/5/20 17:15
 */
public class RongYunCallUtils {
    /**
     * 发起一个通话
     *
     * @param targetId 目标会话id
     * @return 呼叫id
     */
    public static String startCall(String targetId) {
        /**
         * 发起一个通话
         *
         * @param conversationType 会话类型
         * @param targetId         目标会话id
         * @param userIds          邀请参与通话的用户ID列表
         * @param mediaType        发起的通话媒体类型
         * @param extra            附加信息
         * @return 呼叫id
         */
        List<String> userIds = new ArrayList<>();
        userIds.add(targetId);
        return RongCallClient.getInstance().startCall(Conversation.ConversationType.PRIVATE, targetId, userIds, RongCallCommon.CallMediaType.AUDIO, null);
    }

    /**
     * 接听通话
     *
     * @param callId 呼叫id，可以从来电监听的callSession中获取
     */
    public static void acceptCall(String callId) {
        RongCallClient.getInstance().acceptCall(callId);
    }

    /**
     * 挂断通话
     *
     * @param callId 呼叫id，可以从来电监听的callSession中获取
     */
    public static void hangUpCall(String callId) {
        RongCallClient.getInstance().hangUpCall(callId);
    }

    /**
     * 设置是否打开本地音频
     *
     * @param enabled true:打开本地音频 false:关闭本地音频
     */
    public static void setEnableLocalAudio(boolean enabled) {
        RongCallClient.getInstance().setEnableLocalAudio(enabled);
    }

    /**
     * 设置是否打开免提
     *
     * @param enabled true:打开免提 false:关闭免提
     */
    public static void setEnableSpeakerphone(boolean enabled) {
        RongCallClient.getInstance().setEnableSpeakerphone(enabled);
    }

    /**
     * 获取当前通话实体，通话实体中维护着当前通话的所有信息。
     *
     * @return 当前通话实体
     */
    public static RongCallSession getCallSession() {
        return RongCallClient.getInstance().getCallSession();
    }
}
