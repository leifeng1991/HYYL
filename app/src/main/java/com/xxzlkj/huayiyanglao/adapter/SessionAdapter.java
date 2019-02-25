package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lqr.emoji.MoonUtils;
import com.xxzlkj.huayiyanglao.util.RongYunUtils;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.model.GlideApp;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;
import com.xxzlkj.zhaolinshare.base.util.UserUtils;
import com.xxzlkj.zhaolinshare.chat.BubbleImageView;
import com.xxzlkj.zhaolinshare.chat.CircularProgressBar;
import com.xxzlkj.zhaolinshare.chat.MediaFileUtils;
import com.xxzlkj.zhaolinshare.chat.RedPacketMessage;
import com.xxzlkj.zhaolinshare.chat.TimeUtils;
import com.xxzlkj.zhaolinshare.chat.manager.MediaManager;
import com.xxzlkj.zhaolinshare.chat.utils.VideoThumbLoader;

import java.io.File;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.FileMessage;
import io.rong.message.GroupNotificationMessage;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.RecallNotificationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

import static com.xxzlkj.zhaolinshare.chat.R.id.ivAvatar;


/**
 * 描述:
 *
 * @author zhangrq
 *         2017/5/16 13:35
 */
public class SessionAdapter extends BaseAdapter<Message> {
    // 接收、发送文字
    private static final int SEND_TEXT = com.xxzlkj.zhaolinshare.chat.R.layout.item_text_send;
    private static final int RECEIVE_TEXT = com.xxzlkj.zhaolinshare.chat.R.layout.item_text_receive;
    // 接收、发送图片
    private static final int SEND_IMAGE = com.xxzlkj.zhaolinshare.chat.R.layout.item_image_send;
    private static final int RECEIVE_IMAGE = com.xxzlkj.zhaolinshare.chat.R.layout.item_image_receive;
    // 接收、发送纸贴
    private static final int SEND_STICKER = com.xxzlkj.zhaolinshare.chat.R.layout.item_sticker_send;
    private static final int RECEIVE_STICKER = com.xxzlkj.zhaolinshare.chat.R.layout.item_sticker_receive;
    // 接收、发送视频
    private static final int SEND_VIDEO = com.xxzlkj.zhaolinshare.chat.R.layout.item_video_send;
    private static final int RECEIVE_VIDEO = com.xxzlkj.zhaolinshare.chat.R.layout.item_video_receive;
    // 接收、发送定位
    private static final int SEND_LOCATION = com.xxzlkj.zhaolinshare.chat.R.layout.item_location_send;
    private static final int RECEIVE_LOCATION = com.xxzlkj.zhaolinshare.chat.R.layout.item_location_receive;
    // 接收到通知
    private static final int RECEIVE_NOTIFICATION = com.xxzlkj.zhaolinshare.chat.R.layout.item_notification;
    // 接收、发送声音
    private static final int RECEIVE_VOICE = com.xxzlkj.zhaolinshare.chat.R.layout.item_audio_receive;
    private static final int SEND_VOICE = com.xxzlkj.zhaolinshare.chat.R.layout.item_audio_send;
    // 接收、发送红包
    private static final int RECEIVE_RED_PACKET = com.xxzlkj.zhaolinshare.chat.R.layout.item_red_packet_receive;
    private static final int SEND_RED_PACKET = com.xxzlkj.zhaolinshare.chat.R.layout.item_red_packet_send;
    // 未定义的消息、通知
    private static final int UNDEFINE_MSG = com.xxzlkj.zhaolinshare.chat.R.layout.item_no_support_msg_type;
    private static final int RECALL_NOTIFICATION = com.xxzlkj.zhaolinshare.chat.R.layout.item_notification;

    public static final int DEFAULT_MAX_AUDIO_RECORD_TIME_SECOND = 120;
    private final String mSessionId;
    private final RecyclerView mRvMsg;
    private UserInfo myUserInfo;
    private UserInfo otherUserInfo;

    public SessionAdapter(Context context, int itemId, String mSessionId, RecyclerView mRvMsg) {
        super(context, itemId);
        this.mSessionId = mSessionId;
        this.mRvMsg = mRvMsg;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, Message itemBean) {
        // 设置时间
        setTime(holder, itemBean, position);
        // 设置布局显示
        setView(holder, itemBean, position);
        if (!(itemBean.getContent() instanceof GroupNotificationMessage) && !(itemBean.getContent() instanceof RecallNotificationMessage) && (getItemViewType(position) != UNDEFINE_MSG)) {
            // 不是组的，不是撤回的，不是未定义的消息
            // 设置头像
            setAvatar(holder, itemBean, position);
            // 设置用户名
            setName(holder, itemBean, position);
            // 设置状态，发送中、失败、完成
            setStatus(holder, itemBean, position);
            // 设置点击
            setOnClick(holder, itemBean, position);
        }
    }

    private void setView(BaseViewHolder helper, Message message, int position) {
        //根据消息类型设置消息显示内容
        MessageContent msgContent = message.getContent();
        if (msgContent instanceof TextMessage) {
            // 文本
            MoonUtils.identifyFaceExpression(mContext, helper.getView(com.xxzlkj.zhaolinshare.chat.R.id.tvText), ((TextMessage) msgContent).getContent(), ImageSpan.ALIGN_BOTTOM);
        } else if (msgContent instanceof ImageMessage) {
            // 图片
            ImageMessage imageMessage = (ImageMessage) msgContent;
            BubbleImageView bivPic = helper.getView(com.xxzlkj.zhaolinshare.chat.R.id.bivPic);
            GlideApp.with(mContext).load(imageMessage.getLocalUri() == null ? imageMessage.getRemoteUri() : imageMessage.getLocalUri()).into(bivPic);
        } else if (msgContent instanceof FileMessage) {
            // 文件消息
            FileMessage fileMessage = (FileMessage) msgContent;
            if (MediaFileUtils.isImageFileType(fileMessage.getName())) {
                // 图片
                ImageView ivPic = helper.getView(com.xxzlkj.zhaolinshare.chat.R.id.ivSticker);
                GlideApp.with(mContext).load(fileMessage.getLocalPath() == null ? fileMessage.getMediaUrl() : fileMessage.getLocalPath()).placeholder(com.xxzlkj.zhaolinshare.chat.R.mipmap.default_icon).into(ivPic);
            } else if (MediaFileUtils.isVideoFileType(fileMessage.getName())) {
                // 视频
                BubbleImageView bivPic = helper.getView(com.xxzlkj.zhaolinshare.chat.R.id.bivPic);
                if (fileMessage.getLocalPath() != null && new File(fileMessage.getLocalPath().getPath()).exists()) {
                    // 视频本地存在
                    VideoThumbLoader.getInstance().showThumb(fileMessage.getLocalPath().getPath(), bivPic, 200, 200);
                } else {
                    bivPic.setImageResource(com.xxzlkj.zhaolinshare.chat.R.mipmap.ic_launcher);
                }
            }
        } else if (msgContent instanceof LocationMessage) {
            // 定位
            LocationMessage locationMessage = (LocationMessage) msgContent;
            helper.setText(com.xxzlkj.zhaolinshare.chat.R.id.tvTitle, locationMessage.getPoi());
            ImageView ivLocation = helper.getView(com.xxzlkj.zhaolinshare.chat.R.id.ivLocation);
            GlideApp.with(mContext).load(locationMessage.getImgUri()).into(ivLocation);
        } else if (msgContent instanceof GroupNotificationMessage) {
            // 组的消息
//            GroupNotificationMessage groupNotificationMessage = (GroupNotificationMessage) msgContent;
//            try {
//                UserInfo curUserInfo = DBManager.getInstance().getUserInfo(UserCache.getId());
//                GroupNotificationMessageData data = JsonMananger.jsonToBean(groupNotificationMessage.getData(), GroupNotificationMessageData.class);
//                String operation = groupNotificationMessage.getOperation();
//                String notification = "";
//                String operatorName = data.getOperatorNickname().equals(curUserInfo.getName()) ? UIUtils.getString(R.string.you) : data.getOperatorNickname();
//                String targetUserDisplayNames = "";
//                List<String> targetUserDisplayNameList = data.getTargetUserDisplayNames();
//                for (String name : targetUserDisplayNameList) {
//                    targetUserDisplayNames += name.equals(curUserInfo.getName()) ? UIUtils.getString(R.string.you) : name;
//                }
//                if (operation.equalsIgnoreCase(GroupNotificationMessage.GROUP_OPERATION_CREATE)) {
//                    notification = UIUtils.getString(R.string.created_group, operatorName);
//                } else if (operation.equalsIgnoreCase(GroupNotificationMessage.GROUP_OPERATION_DISMISS)) {
//                    notification = operatorName + UIUtils.getString(R.string.dismiss_groups);
//                } else if (operation.equalsIgnoreCase(GroupNotificationMessage.GROUP_OPERATION_KICKED)) {
//                    if (operatorName.contains(UIUtils.getString(R.string.you))) {
//                        notification = UIUtils.getString(R.string.remove_group_member, operatorName, targetUserDisplayNames);
//                    } else {
//                        notification = UIUtils.getString(R.string.remove_self, targetUserDisplayNames, operatorName);
//                    }
//                } else if (operation.equalsIgnoreCase(GroupNotificationMessage.GROUP_OPERATION_ADD)) {
//                    notification = UIUtils.getString(R.string.invitation, operatorName, targetUserDisplayNames);
//                } else if (operation.equalsIgnoreCase(GroupNotificationMessage.GROUP_OPERATION_QUIT)) {
//                    notification = operatorName + UIUtils.getString(R.string.quit_groups);
//                } else if (operation.equalsIgnoreCase(GroupNotificationMessage.GROUP_OPERATION_RENAME)) {
//                    notification = UIUtils.getString(R.string.change_group_name, operatorName, data.getTargetGroupName());
//                }
//                helper.setText(R.id.tvNotification, notification);
//            } catch (HttpException e) {
//                e.printStackTrace();
//            }
        } else if (msgContent instanceof VoiceMessage) {
            // 声音
            VoiceMessage voiceMessage = (VoiceMessage) msgContent;
            int increment = (int) (PixelUtil.getScreenWidth(mContext) / 2 / DEFAULT_MAX_AUDIO_RECORD_TIME_SECOND * voiceMessage.getDuration());

            boolean listened = message.getReceivedStatus().isListened();
            boolean isSend = message.getMessageDirection() == Message.MessageDirection.SEND;
            if (isSend)
                // 自己发送的不显示红点
                helper.setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.ivIsRead, View.GONE);
            else
                // 判断是否显示红点
                helper.setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.ivIsRead, listened ? View.GONE : View.VISIBLE);//设置是否已经读

            RelativeLayout rlAudio = helper.setText(com.xxzlkj.zhaolinshare.chat.R.id.tvDuration, voiceMessage.getDuration() + "''").getView(com.xxzlkj.zhaolinshare.chat.R.id.rlAudio);
            ViewGroup.LayoutParams params = rlAudio.getLayoutParams();
            params.width = PixelUtil.dip2px(mContext, 85) + PixelUtil.dip2px(mContext, increment);
            rlAudio.setLayoutParams(params);
        } else if (msgContent instanceof RedPacketMessage) {
            // 红包
            RedPacketMessage redPacketMessage = (RedPacketMessage) msgContent;
            helper.setText(com.xxzlkj.zhaolinshare.chat.R.id.tvRedPacketGreeting, redPacketMessage.getContent());
        } else if (msgContent instanceof RecallNotificationMessage) {
            // 撤销通知
            RecallNotificationMessage recallNotificationMessage = (RecallNotificationMessage) msgContent;
            String operatorId = recallNotificationMessage.getOperatorId();
            String operatorName;
            User user = UserUtils.getLoginUser(mContext);
            if (user != null && operatorId.equalsIgnoreCase(user.getData().getId())) {
                operatorName = "你";
            } else {
                operatorName = "对方";
            }
            helper.setText(com.xxzlkj.zhaolinshare.chat.R.id.tvNotification, operatorName + "撤回了一条消息");
        }
    }

    private void setOnClick(BaseViewHolder helper, final Message message, int position) {
        helper.getView(com.xxzlkj.zhaolinshare.chat.R.id.llError).setOnClickListener(v -> {

            // 点击错误的显示，删除消息,后发送消息
            RongIMClient.getInstance().deleteMessages(new int[]{message.getMessageId()}, new RongIMClient.ResultCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    // 先删除消息，后发送
                    removeItem(message);
                    MessageContent content = message.getContent();
                    if (content instanceof TextMessage) {
                        // 发送消息
                        RongYunUtils.sendTextMsg(Conversation.ConversationType.PRIVATE, mSessionId,
                                ((TextMessage) content).getContent(), mRvMsg, SessionAdapter.this);
                    } else if (content instanceof ImageMessage) {
                        // 发送图片
                        RongYunUtils.sendImgMsg(Conversation.ConversationType.PRIVATE, mSessionId,
                                ((ImageMessage) content).getThumUri(), ((ImageMessage) content).getLocalUri(), mRvMsg, SessionAdapter.this);
                    } else if (content instanceof FileMessage) {
                        // 发送文件
                        RongYunUtils.sendFileMsg(Conversation.ConversationType.PRIVATE, mSessionId,
                                new File(((FileMessage) content).getLocalPath().getPath()), mRvMsg, SessionAdapter.this);
                    } else if (content instanceof VoiceMessage) {
                        // 发送声音
                        VoiceMessage voiceMessage = (VoiceMessage) content;
                        RongYunUtils.sendAudioFile(Conversation.ConversationType.PRIVATE, mSessionId,
                                voiceMessage.getUri(), voiceMessage.getDuration(), mRvMsg, SessionAdapter.this);
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        });
        // 点击头像
        helper.getView(ivAvatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastManager.showShortToast(mContext, "跳到详情");
            }
        });

        // 点击内容
        MessageContent content = message.getContent();
        if (content instanceof ImageMessage) {
//                ImageMessage imageMessage = (ImageMessage) content;
//                Intent intent = new Intent(mContext, ShowBigImageActivity.class);
//                intent.putExtra("url", imageMessage.getLocalUri() == null ? imageMessage.getRemoteUri().toString() : imageMessage.getLocalUri().toString());
//                mContext.jumpToActivity(intent);
        } else if (content instanceof FileMessage) {
//                FileMessage fileMessage = (FileMessage) content;
//                if (MediaFileUtils.isVideoFileType(fileMessage.getName())) {
//                    helper.getView(R.id.bivPic).setOnClickListener(v -> {
//                        boolean isSend = message.getMessageDirection() == Message.MessageDirection.SEND ? true : false;
//                        if (isSend) {
//                            if (fileMessage.getLocalPath() != null && new File(fileMessage.getLocalPath().getPath()).exists()) {
//                                FileOpenUtils.openFile(mContext, fileMessage.getLocalPath().getPath());
//                            } else {
//                                downloadMediaMessage(message);
//                            }
//                        } else {
//                            Message.ReceivedStatus receivedStatus = message.getReceivedStatus();
//                            if (receivedStatus.isDownload() || receivedStatus.isRetrieved()) {
//                                if (fileMessage.getLocalPath() != null) {
//                                    FileOpenUtils.openFile(mContext, fileMessage.getLocalPath().getPath());
//                                } else {
//                                    UIUtils.showToast(UIUtils.getString(R.string.file_out_of_date));
//                                }
//                            } else {
//                                downloadMediaMessage(message);
//                            }
//                        }
//                    });
//                }
        } else if (content instanceof VoiceMessage) {
            final VoiceMessage voiceMessage = (VoiceMessage) content;
            final ImageView ivAudio = helper.getView(com.xxzlkj.zhaolinshare.chat.R.id.ivAudio);
            helper.setOnClickListener(com.xxzlkj.zhaolinshare.chat.R.id.rlAudio, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 开始动画
                    final AnimationDrawable animationDrawable = (AnimationDrawable) ivAudio.getBackground();
                    startAnim(animationDrawable);
                    // 设置已读
                    RongYunUtils.setMessageReceivedStatusIsRead(SessionAdapter.this, message);
                    //播放前重置。
                    MediaManager.release();
                    //开始播放
                    MediaManager.playSoundUri(mContext, voiceMessage.getUri(), new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            // 停止动画
                            stopAnim(animationDrawable);
                        }
                    });

                }
            });
        } else if (content instanceof RedPacketMessage) {
//                RedPacketMessage redPacketMessage = (RedPacketMessage) content;
//                int chatType = mConversationType == Conversation.ConversationType.PRIVATE ? RPConstant.RP_ITEM_TYPE_SINGLE : RPConstant.RP_ITEM_TYPE_GROUP;
//                String redPacketId = redPacketMessage.getBribery_ID();
//                String redPacketType = redPacketMessage.getBribery_Message();
//                String receiverId = UserCache.getId();
//                String direct = RPConstant.MESSAGE_DIRECT_RECEIVE;
//                RedPacketUtil.openRedPacket(((SessionActivity) mContext), chatType, redPacketId, redPacketType, receiverId, direct);
        }
    }

    private void startAnim(AnimationDrawable animationDrawable) {
        stopAnim(animationDrawable);
        animationDrawable.start();
    }

    private void stopAnim(AnimationDrawable animationDrawable) {
        animationDrawable.stop();
        animationDrawable.selectDrawable(0);
    }

    private void setStatus(BaseViewHolder helper, Message item, int position) {
        MessageContent msgContent = item.getContent();
        if (msgContent instanceof TextMessage || msgContent instanceof LocationMessage || msgContent instanceof VoiceMessage) {
            // 文本、定位、语音消息
            //只需要设置自己发送的状态
            Message.SentStatus sentStatus = item.getSentStatus();
            if (sentStatus == Message.SentStatus.SENDING) {
                // 发送中
                helper.setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.pbSending, View.VISIBLE).setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.llError, View.GONE);
            } else if (sentStatus == Message.SentStatus.FAILED) {
                // 发送是吧
                helper.setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.pbSending, View.GONE).setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.llError, View.VISIBLE);
            } else if (sentStatus == Message.SentStatus.SENT) {
                // 发送
                helper.setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.pbSending, View.GONE).setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.llError, View.GONE);
            }
        } else if (msgContent instanceof ImageMessage) {
            // 图片消息
            BubbleImageView bivPic = helper.getView(com.xxzlkj.zhaolinshare.chat.R.id.bivPic);
            boolean isSend = item.getMessageDirection() == Message.MessageDirection.SEND;
            if (isSend) {
                // 发送
                Message.SentStatus sentStatus = item.getSentStatus();
                if (sentStatus == Message.SentStatus.SENDING) {
                    // 发送中
                    bivPic.setProgressVisible(true);
                    if (!TextUtils.isEmpty(item.getExtra()))
                        bivPic.setPercent(NumberFormatUtils.toInt(item.getExtra()));// 设置进度百分比
                    bivPic.showShadow(true);// 设置阴影
                    helper.setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.llError, View.GONE);// 设置是否显示错误
                } else if (sentStatus == Message.SentStatus.FAILED) {
                    // 发送失败
                    bivPic.setProgressVisible(false);
                    bivPic.showShadow(false);
                    helper.setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.llError, View.VISIBLE);
                } else if (sentStatus == Message.SentStatus.SENT) {
                    // 发送
                    bivPic.setProgressVisible(false);
                    bivPic.showShadow(false);
                    helper.setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.llError, View.GONE);
                }
            } else {
                // 接收
                Message.ReceivedStatus receivedStatus = item.getReceivedStatus();
                bivPic.setProgressVisible(false);
                bivPic.showShadow(false);
                helper.setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.llError, View.GONE);
            }
        } else if (msgContent instanceof FileMessage) {
            // 文件消息
            BubbleImageView bivPic = helper.getView(com.xxzlkj.zhaolinshare.chat.R.id.bivPic);
            FileMessage fileMessage = (FileMessage) msgContent;
            boolean isSend = item.getMessageDirection() == Message.MessageDirection.SEND;
            if (MediaFileUtils.isImageFileType(fileMessage.getName())) {
                // 图片类型
                if (isSend) {
                    // 发送
                    Message.SentStatus sentStatus = item.getSentStatus();
                    if (sentStatus == Message.SentStatus.SENDING) {
                        helper.setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.llError, View.GONE);
                    } else if (sentStatus == Message.SentStatus.FAILED) {
                        helper.setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.llError, View.VISIBLE);
                    } else if (sentStatus == Message.SentStatus.SENT) {
                        helper.setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.llError, View.GONE);
                    }
                } else {
                    // 接收
                    if (bivPic != null) {
                        bivPic.setProgressVisible(false);
                        bivPic.showShadow(false);
                    }
                    helper.setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.llError, View.GONE);
                }
            } else if (MediaFileUtils.isVideoFileType(fileMessage.getName())) {
                // 视频类型
                CircularProgressBar cpbLoading = helper.getView(com.xxzlkj.zhaolinshare.chat.R.id.cpbLoading);// 环形的进度条
                if (isSend) {
                    // 发送
                    Message.SentStatus sentStatus = item.getSentStatus();
                    if (sentStatus == Message.SentStatus.SENDING || fileMessage.getLocalPath() == null || (fileMessage.getLocalPath() != null && !new File(fileMessage.getLocalPath().getPath()).exists())) {
                        // 发送中
                        if (!TextUtils.isEmpty(item.getExtra())) {
                            cpbLoading.setMax(100);
                            cpbLoading.setProgress(NumberFormatUtils.toInt(item.getExtra()));
                        } else {
                            cpbLoading.setMax(100);
                            cpbLoading.setProgress(0);
                        }
                        helper.setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.llError, View.GONE).setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.cpbLoading, View.VISIBLE);
                        bivPic.showShadow(true);
                    } else if (sentStatus == Message.SentStatus.FAILED) {
                        // 发送失败
                        helper.setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.llError, View.VISIBLE).setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.cpbLoading, View.GONE);
                        bivPic.showShadow(false);
                    } else if (sentStatus == Message.SentStatus.SENT) {
                        // 发送
                        helper.setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.llError, View.GONE).setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.cpbLoading, View.GONE);
                        bivPic.showShadow(false);
                    }
                } else {
                    // 接收
                    Message.ReceivedStatus receivedStatus = item.getReceivedStatus();
                    if (receivedStatus.isDownload() || fileMessage.getLocalPath() != null) {
                        // 已经下载了
                        helper.setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.llError, View.GONE).setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.cpbLoading, View.GONE);
                        bivPic.showShadow(false);
                    } else {
                        // 未下载
                        if (!TextUtils.isEmpty(item.getExtra())) {
                            cpbLoading.setMax(100);
                            cpbLoading.setProgress(NumberFormatUtils.toInt(item.getExtra()));
                        } else {
                            cpbLoading.setMax(100);
                            cpbLoading.setProgress(0);
                        }
                        helper.setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.llError, View.GONE).setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.cpbLoading, View.VISIBLE);
                        bivPic.showShadow(true);
                    }
                }
            }
        }
    }

    /**
     * 设置头像
     */
    private void setAvatar(BaseViewHolder helper, Message item, int position) {
        ImageView ivAvatar = helper.getView(com.xxzlkj.zhaolinshare.chat.R.id.ivAvatar);
        boolean isSend = item.getMessageDirection() == Message.MessageDirection.SEND;
        // 因为用户信息可能获取不到
        Uri uri = isSend ? (myUserInfo == null ? null : myUserInfo.getPortraitUri()) : (otherUserInfo == null ? null : otherUserInfo.getPortraitUri());
        if (uri != null) {
            PicassoUtils.setImageRaw(ivAvatar.getContext(), uri, ivAvatar);
        }
    }

    /**
     * 设置用户名
     */
    private void setName(BaseViewHolder helper, Message item, int position) {
        if (item.getConversationType() == Conversation.ConversationType.PRIVATE) {
            helper.setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.tvName, View.GONE);
        } else {
            boolean isSend = item.getMessageDirection() == Message.MessageDirection.SEND;
            // 因为用户信息可能获取不到
            String name = isSend ? myUserInfo == null ? null : myUserInfo.getName() : otherUserInfo == null ? null : otherUserInfo.getName();
            helper.setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.tvName, View.VISIBLE).setText(com.xxzlkj.zhaolinshare.chat.R.id.tvName, name);
        }
    }

    /**
     * 设置时间
     */
    private void setTime(BaseViewHolder holder, Message item, int position) {
        boolean isSend = item.getMessageDirection() == Message.MessageDirection.SEND;
        long msgTime = isSend ? item.getSentTime() : item.getReceivedTime();// 当前条目消息的时间
        if (position > 0) {
            Message preMsg = getList().get(position - 1);
            boolean isSendForPreMsg = preMsg.getMessageDirection() == Message.MessageDirection.SEND;
            long preMsgTime = isSendForPreMsg ? preMsg.getSentTime() : preMsg.getReceivedTime();// 上一个条目消息的时间
            if (msgTime - preMsgTime > (5 * 60 * 1000)) {// 大于5分钟
                holder.setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.tvTime, View.VISIBLE).setText(com.xxzlkj.zhaolinshare.chat.R.id.tvTime, TimeUtils.getMsgFormatTime(msgTime));
            } else {
                holder.setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.tvTime, View.GONE);
            }
        } else {
            holder.setVisibility(com.xxzlkj.zhaolinshare.chat.R.id.tvTime, View.VISIBLE).setText(com.xxzlkj.zhaolinshare.chat.R.id.tvTime, TimeUtils.getMsgFormatTime(msgTime));
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message msg = getList().get(position);
        boolean isSend = msg.getMessageDirection() == Message.MessageDirection.SEND;

        MessageContent msgContent = msg.getContent();
        if (msgContent instanceof TextMessage) {
            return isSend ? SEND_TEXT : RECEIVE_TEXT;
        }
        if (msgContent instanceof ImageMessage) {
            return isSend ? SEND_IMAGE : RECEIVE_IMAGE;
        }
        if (msgContent instanceof FileMessage) {
            FileMessage fileMessage = (FileMessage) msgContent;
            if (MediaFileUtils.isImageFileType(fileMessage.getName())) {
                return isSend ? SEND_STICKER : RECEIVE_STICKER;
            } else if (MediaFileUtils.isVideoFileType(fileMessage.getName())) {
                return isSend ? SEND_VIDEO : RECEIVE_VIDEO;
            }
        }
        if (msgContent instanceof LocationMessage) {
            return isSend ? SEND_LOCATION : RECEIVE_LOCATION;
        }
        if (msgContent instanceof GroupNotificationMessage) {
            return RECEIVE_NOTIFICATION;
        }
        if (msgContent instanceof VoiceMessage) {
            return isSend ? SEND_VOICE : RECEIVE_VOICE;
        }
//        if (msgContent instanceof RedPacketMessage) {
//            return isSend ? SEND_RED_PACKET : RECEIVE_RED_PACKET;
//        }
        if (msgContent instanceof RecallNotificationMessage) {
            return RECALL_NOTIFICATION;
        }
        return UNDEFINE_MSG;
    }

    public void setMyUserInfo(UserInfo myUserInfo) {
        this.myUserInfo = myUserInfo;
    }

    public void setOtherUserInfo(UserInfo otherUserInfo) {
        this.otherUserInfo = otherUserInfo;
    }
}
