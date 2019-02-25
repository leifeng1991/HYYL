package com.xxzlkj.huayiyanglao;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.util.RongYunCallUtils;
import com.xxzlkj.huayiyanglao.util.ZLUserUtils;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;
import com.xxzlkj.zhaolinshare.base.util.PermissionHelper;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.SystemBarUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.rong.calllib.IRongCallListener;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallCommon;
import io.rong.calllib.RongCallSession;


/**
 * 打电话页面
 *
 * @author zhangrq
 */
public class CallActivity extends BaseActivity {

    public static final String TARGET_ID = "targetId";
    public static final String CALL_ID = "callId";
    public static final String CALLER_USER_ID = "callerUserId";
    private String callerUserId, callId, targetId;
    private MediaPlayer mMediaPlayer;
    private ImageView iv_avatar;
    private ViewGroup vg_calling_layout, vg_receive_call_layout;
    private TextView tv_title_state, tv_calling_speakerphone, tv_calling_hang_up, tv_calling_mute, tv_calling_time, tv_receive_call_accept, tv_receive_call_hang_up, tv_send_call_layout, tv_name;
    private boolean enableLocalAudio = true, enableSpeakerphone;
    private Timer timer;
    private TimerTask timerTask;
    private long callingAllSeconds;
    private boolean checkPermissions;
    private PermissionHelper permissionHelper;

    /**
     * 此方法仅仅用于发起通话
     *
     * @param targetId 必传，目标会话id
     */
    public static Intent newIntentSend(Context context, String targetId) {
        Intent intent = new Intent(context, CallActivity.class);
        intent.putExtra(TARGET_ID, targetId);
        return intent;
    }

    /**
     * 此方法仅仅用于接听通话
     *
     * @param callId       呼叫id，可以从来电监听的callSession中获取
     * @param callerUserId 发起通话的人
     */
    public static Intent newIntentReceive(Context context, String callId, String callerUserId) {
        Intent intent = new Intent(context, CallActivity.class);
        intent.putExtra(CALL_ID, callId);
        intent.putExtra(CALLER_USER_ID, callerUserId);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_call);
        SystemBarUtils.setStatusBarTranslucent(this);
        SystemBarUtils.setStatusBarLightMode(this, true);
    }

    @Override
    protected void findViewById() {
        iv_avatar = getView(R.id.iv_avatar);// 头像
        tv_name = getView(R.id.tv_name);// 用户名
        tv_title_state = getView(R.id.tv_title_state);// 状态提示
        // 发起通话布局
        tv_send_call_layout = getView(R.id.tv_send_call_layout);// 取消按钮
        // 接听通话布局
        vg_receive_call_layout = getView(R.id.vg_receive_call_layout);// 接听通话布局
        tv_receive_call_hang_up = getView(R.id.tv_receive_call_hang_up);// 接听通话--拒绝按钮
        tv_receive_call_accept = getView(R.id.tv_receive_call_accept);// 接听通话--接听按钮
        // 通话中布局
        vg_calling_layout = getView(R.id.vg_calling_layout);// 通话中布局
        tv_calling_time = getView(R.id.tv_calling_time);// 通话中--时间
        tv_calling_mute = getView(R.id.tv_calling_mute);// 通话中--静音按钮
        tv_calling_hang_up = getView(R.id.tv_calling_hang_up);// 通话中--挂断按钮
        tv_calling_speakerphone = getView(R.id.tv_calling_speakerphone);// 通话中--免提按钮
    }


    @Override
    protected void setListener() {
        // 发起通话布局
        // --取消发起聊天
        tv_send_call_layout.setOnClickListener(v -> {
            hangUpCall();
            // 销毁此页面
            finish();
        });
        // 接听通话布局
        // --拒绝按钮，后面的操作在回调里面处理了
        tv_receive_call_hang_up.setOnClickListener(v -> RongYunCallUtils.hangUpCall(callId));
        // --接听按钮，后面的操作在回调里面处理了
        tv_receive_call_accept.setOnClickListener(v -> RongYunCallUtils.acceptCall(callId));

        // 通话中布局
        // --静音按钮
        // TODO 改
        tv_calling_mute.setOnClickListener(v -> RongYunCallUtils.setEnableLocalAudio(enableLocalAudio = !enableLocalAudio));
        // --挂断按钮
        tv_calling_hang_up.setOnClickListener(v -> hangUpCall());
        // --免提按钮
        tv_calling_speakerphone.setOnClickListener(v -> RongYunCallUtils.setEnableSpeakerphone(enableSpeakerphone = !enableSpeakerphone));

        RongCallClient.getInstance().setVoIPCallListener(new IRongCallListener() {
            /**
             * 电话已拨出。
             * 主叫端拨出电话后，通过回调 onCallOutgoing 通知当前 call 的详细信息。
             *
             * @param callSession 通话实体。
             * @param localVideo  本地 camera 信息。
             */
            @Override
            public void onCallOutgoing(RongCallSession callSession, SurfaceView localVideo) {
                LogUtil.i("onCallOutgoing");
                startRing();
                // 设置布局显示
                setViewShow(1);
            }

            /**
             * 已建立通话。
             * 通话接通时，通过回调 onCallConnected 通知当前 call 的详细信息。
             *
             * @param callSession 通话实体。
             * @param localVideo  本地 camera 信息。
             */
            @Override
            public void onCallConnected(RongCallSession callSession, SurfaceView localVideo) {
                LogUtil.i("onCallConnected");
                stopRing();
                // 设置通话中显示
                setViewShow(3);
                // 开始倒计时
                startTimer();
            }

            /**
             * 通话结束。
             * 通话中，对方挂断，己方挂断，或者通话过程网络异常造成的通话中断，都会回调 onCallDisconnected。
             *
             * @param callSession 通话实体。
             * @param reason      通话中断原因。详细看demo
             */
            @Override
            public void onCallDisconnected(RongCallSession callSession, RongCallCommon.CallDisconnectedReason reason) {
                LogUtil.i("onCallDisconnected");
                String text = null;
                switch (reason) {
                    case CANCEL:
                        text = "已取消";
                        break;
                    case REJECT:
                        text = "已拒绝";
                        break;
                    case NO_RESPONSE:
                    case BUSY_LINE:
                        text = "未接听";
                        break;
                    case REMOTE_BUSY_LINE:
                        text = "对方忙,请稍后再拨";
                        break;
                    case REMOTE_CANCEL:
                        text = "对方已取消";
                        break;
                    case REMOTE_REJECT:
                        text = "对方已拒绝";
                        break;
                    case REMOTE_NO_RESPONSE:
                        text = "对方未接听";
                        break;
                    case REMOTE_HANGUP:
                    case HANGUP:
                    case NETWORK_ERROR:
                    case INIT_VIDEO_ERROR:
                        text = "通话结束";
                        break;
                }
                ToastManager.showShortToast(mContext, text);
                // 停止铃声
                stopRing();
                // 停止倒计时
                cancelTimer();
                // 销毁当前页面
                finish();
            }

            /**
             * 被叫端正在振铃。
             * 主叫端拨出电话，被叫端收到请求，发出振铃响应时，回调 onRemoteUserRinging。
             *
             * @param userId 振铃端用户 id。
             */
            @Override
            public void onRemoteUserRinging(String userId) {
                LogUtil.i("onRemoteUserRinging");
            }

            /**
             * 被叫端加入通话。
             * 主叫端拨出电话，被叫端收到请求后，加入通话，回调 onRemoteUserJoined。
             *
             * @param userId      加入用户的 id。
             * @param mediaType   加入用户的媒体类型，audio or video。
             * @param remoteVideo 加入用户者的 camera 信息。
             */
            @Override
            public void onRemoteUserJoined(String userId, RongCallCommon.CallMediaType mediaType, SurfaceView remoteVideo) {
                LogUtil.i("onRemoteUserJoined");

            }

            /**
             * 通话中的某一个参与者，邀请好友加入通话，发出邀请请求后，回调 onRemoteUserInvited。
             *
             * @param userId    被邀请者的 id。
             * @param mediaType 被邀请者的 id。
             */
            @Override
            public void onRemoteUserInvited(String userId, RongCallCommon.CallMediaType mediaType) {
                LogUtil.i("onRemoteUserInvited");

            }

            /**
             * 通话中的远端参与者离开。
             * 回调 onRemoteUserLeft 通知状态更新。
             *
             * @param userId 远端参与者的 id。
             * @param reason 远端参与者离开原因。
             */
            @Override
            public void onRemoteUserLeft(String userId, RongCallCommon.CallDisconnectedReason reason) {
                LogUtil.i("onRemoteUserLeft");

            }

            /**
             * 当通话中的某一个参与者切换通话类型，例如由 audio 切换至 video，回调 onMediaTypeChanged。
             *
             * @param userId    切换者的 userId。
             * @param mediaType 切换者，切换后的媒体类型。
             * @param video     切换着，切换后的 camera 信息，如果由 video 切换至 audio，则为 null。
             */
            @Override
            public void onMediaTypeChanged(String userId, RongCallCommon.CallMediaType mediaType, SurfaceView video) {
                LogUtil.i("onMediaTypeChanged");

            }

            /**
             * 通话过程中，发生异常。
             *
             * @param errorCode 异常原因。
             */
            @Override
            public void onError(RongCallCommon.CallErrorCode errorCode) {
                LogUtil.i("onError");
                ToastManager.showShortToast(mContext, "通话异常");
                finish();
            }

            /**
             * 远端参与者 camera 状态发生变化时，回调 onRemoteCameraDisabled 通知状态变化。
             *
             * @param userId   远端参与者 id。
             * @param disabled 远端参与者 camera 是否可用。
             */
            @Override
            public void onRemoteCameraDisabled(String userId, boolean disabled) {
                LogUtil.i("onRemoteCameraDisabled");
            }
        });
    }

    @Override
    protected void processLogic() {
        // 请求权限并设置View数据
        requestPermissionsAndInitViewData(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 请求权限并设置View数据
        requestPermissionsAndInitViewData(getIntent());
    }

    /**
     * 请求权限并设置View数据
     */
    private void requestPermissionsAndInitViewData(Intent intent) {
        permissionHelper = new PermissionHelper(this);
        permissionHelper.requestPermissions("请授予[麦克风]权限，否则无法语音通话", new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                // 请求权限成功
                initViewData(intent);
            }

            @Override
            public void doAfterDenied(String... permission) {
                finish();
            }
        }, android.Manifest.permission.RECORD_AUDIO);
    }

    /**
     * 设置View数据
     */
    private void initViewData(Intent intent) {
        // 获取值
        targetId = intent.getStringExtra(TARGET_ID);// 发起通话--目标id
        callId = intent.getStringExtra(CALL_ID);// 接听通话--callId
        callerUserId = intent.getStringExtra(CALLER_USER_ID);// 接听通话--发起人id
        // 设置值
        if (!TextUtils.isEmpty(targetId)) {
            // 发起聊天
            RongYunCallUtils.startCall(targetId);
            // 设置布局显示
            setViewShow(1);
            // 获取头像信息
            getUserInfo(targetId);
        } else if (!TextUtils.isEmpty(callId) && !TextUtils.isEmpty(callerUserId)) {
            // 接收聊天
            startRing();
            // 设置布局显示
            setViewShow(2);
            // 获取头像信息
            getUserInfo(callerUserId);
        } else {
            ToastManager.showShortToast(mContext, "传值有误");
            finish();
        }
    }

    /**
     * 挂断电话
     */
    private void hangUpCall() {
        RongCallSession callSession = RongYunCallUtils.getCallSession();
        if (callSession != null) {
            // 挂断通话
            RongYunCallUtils.hangUpCall(callSession.getCallId());
        }
    }

    /**
     * 设置布局显示
     *
     * @param state 1：发起通话布局显示；2：接听通话布局显示；3：通话中布局显示
     */
    private void setViewShow(int state) {
        tv_send_call_layout.setVisibility(state == 1 ? View.VISIBLE : View.GONE);// 发起通话布局
        vg_receive_call_layout.setVisibility(state == 2 ? View.VISIBLE : View.GONE);// 接听通话布局
        vg_calling_layout.setVisibility(state == 3 ? View.VISIBLE : View.GONE);// 通话中布局
        // 设置头部的状态
        tv_title_state.setText(state == 1 ? "正在呼叫..." : (state == 2 ? "等待接听..." : "正在通话..."));
    }

    private void getUserInfo(String userId) {
        ZLUserUtils.getUserInfo(userId, new ZLUserUtils.OnGetUserInfoListener() {
            @Override
            public void onSuccess(User bean) {
                PicassoUtils.setImageSmall(mContext, bean.getData().getSimg(), iv_avatar);// 头像
                tv_name.setText(bean.getData().getUsername());// 用户名
            }

            @Override
            public void onError(int errorCode, String errorMessage) {

            }
        });
    }

    public void startRing() {
        mMediaPlayer = MediaPlayer.create(this, R.raw.voip_outgoing_ring);

        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }

    public void stopRing() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer = null;
        }
    }

    /**
     * 开启倒计时
     */
    private void startTimer() {
        cancelTimer();

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                // 设置时间
                long minutes = callingAllSeconds / 60;
                long seconds = callingAllSeconds % 60;
                tv_calling_time.post(() -> tv_calling_time.setText(String.format(Locale.CHINA, "%02d:%02d", minutes, seconds)));
                callingAllSeconds++;
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    /**
     * 关闭倒计时
     */
    private void cancelTimer() {
        callingAllSeconds = 0;// 所有时间

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] paramArrayOfInt) {
        super.onRequestPermissionsResult(requestCode, permissions, paramArrayOfInt);
        permissionHelper.handleRequestPermissionsResult(requestCode, permissions, paramArrayOfInt);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 挂断通话
        hangUpCall();
        // 停止通话
        stopRing();
    }
}
