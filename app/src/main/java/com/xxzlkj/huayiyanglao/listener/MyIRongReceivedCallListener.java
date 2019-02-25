package com.xxzlkj.huayiyanglao.listener;

import android.content.Context;
import android.content.Intent;

import com.xxzlkj.huayiyanglao.CallActivity;

import io.rong.calllib.IRongReceivedCallListener;
import io.rong.calllib.RongCallSession;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/11/24 15:25
 */
public class MyIRongReceivedCallListener implements IRongReceivedCallListener {
    private final Context mContent;

    public MyIRongReceivedCallListener(Context mContent) {
        this.mContent = mContent;
    }

    /**
     * 来电回调
     *
     * @param callSession 通话实体
     */
    @Override
    public void onReceivedCall(RongCallSession callSession) {
        //accept or hangup the call
        // 跳到接听页面
        Intent intent = CallActivity.newIntentReceive(mContent, callSession.getCallId(), callSession.getCallerUserId());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContent.startActivity(intent);
    }

    /**
     * targetSDKVersion>＝23时检查权限的回调。当targetSDKVersion<23的时候不需要实现。
     * 在这个回调里用户需要使用Android6.0新增的动态权限分配接口requestCallPermissions通知用户授权，
     * 然后在onRequestPermissionResult回调里根据用户授权或者不授权分别回调
     * RongCallClient.getInstance().onPermissionGranted()和
     * RongCallClient.getInstance().onPermissionDenied()来通知CallLib。
     * 其中audio call需要获取Manifest.permission.RECORD_AUDIO权限；
     * video call需要获取Manifest.permission.RECORD_AUDIO和Manifest.permission.CAMERA两项权限。
     *
     * @param callSession 通话实体
     */
    @Override
    public void onCheckPermission(RongCallSession callSession) {
        // 跳到接听页面，里面处理了权限
        Intent intent = CallActivity.newIntentReceive(mContent, callSession.getCallId(), callSession.getCallerUserId());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContent.startActivity(intent);
    }
}
