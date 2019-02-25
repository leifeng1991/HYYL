package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.util.RongYunUtils;
import com.xxzlkj.huayiyanglao.util.ZLUserUtils;
import com.xxzlkj.huayiyanglao.weight.ShapeButton;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.UserUtils;
import com.xxzlkj.zhaolinshare.chat.TimeUtils;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;


/**
 * 描述:
 *
 * @author zhangrq
 *         2017/9/20 11:25
 */
public class MessageAdapter extends BaseAdapter<Conversation> {

    public MessageAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, Conversation itemBean) {
        ImageView mImage = holder.getView(R.id.id_message_image);// 头像
        ShapeButton mUnreadMessageCount = holder.getView(R.id.id_unread_message);// 未读消息数量
        TextView mTitle = holder.getView(R.id.id_title);// 标题
        ShapeButton mRedCircle = holder.getView(R.id.id_red_circle);// 红点
        TextView mTime = holder.getView(R.id.id_time);// 时间
        TextView mContent = holder.getView(R.id.id_content);// 内容
        // 设置头像、用户名
        // 设置默认值
        mImage.setImageDrawable(null);// 头像
        mTitle.setText("");// 用户名
        String rongYunTargetId = itemBean.getTargetId();
        ZLUserUtils.getUserInfo(UserUtils.getUserID(rongYunTargetId, false), new ZLUserUtils.OnGetUserInfoListener() {
            @Override
            public void onSuccess(User bean) {
                PicassoUtils.setImageSmall(mContext, bean.getData().getSimg(), mImage);// 头像
                mTitle.setText(bean.getData().getUsername());// 用户名
            }

            @Override
            public void onError(int errorCode, String errorMessage) {

            }
        });
        // 设置时间

        mTime.setText(TimeUtils.getMsgFormatTime(itemBean.getSentTime()));// 时间
        // 设置内容
        MessageContent latestMessage = itemBean.getLatestMessage();
        if (latestMessage instanceof TextMessage) {
            // 文本
            String content = ((TextMessage) latestMessage).getContent();
            mContent.setText(content);// 内容
        } else if (latestMessage instanceof VoiceMessage) {
            // 声音
            mContent.setText("[语音]");// 内容
        } else if (latestMessage instanceof ImageMessage) {
            // 图片
            mContent.setText("[图片]");// 内容
        } else if (latestMessage instanceof LocationMessage) {
            // 位置
            mContent.setText("[位置]");// 内容
        }
        // 设置消息提示
        // 设置默认值
        mRedCircle.setVisibility(View.GONE);
        mUnreadMessageCount.setVisibility(View.GONE);
        RongYunUtils.getUnreadCount(Conversation.ConversationType.PRIVATE, rongYunTargetId, new RongIMClient.ResultCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                //  1000:系统消息；1001互动消息
                if (ZLConstants.Strings.INTERACTIVE_MESSAGE_ID.equals(rongYunTargetId) || ZLConstants.Strings.SYSTEM_MESSAGE_ID.equals(rongYunTargetId)) {
                    // 特殊的用户
                    mUnreadMessageCount.setVisibility(View.GONE);
                    mRedCircle.setVisibility(integer > 0 ? View.VISIBLE : View.GONE);
                } else {
                    // 普通的用户
                    mRedCircle.setVisibility(View.GONE);
                    if (integer > 0) {
                        mUnreadMessageCount.setVisibility(View.VISIBLE);
                        mUnreadMessageCount.setText(integer > 99 ? "99+" : String.valueOf(integer));
                    } else {
                        mUnreadMessageCount.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                mRedCircle.setVisibility(View.GONE);
                mUnreadMessageCount.setVisibility(View.GONE);
            }
        });

    }

}
