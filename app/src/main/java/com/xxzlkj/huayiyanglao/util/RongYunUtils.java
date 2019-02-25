package com.xxzlkj.huayiyanglao.util;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.EditText;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;

import java.io.File;
import java.util.List;

import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.FileMessage;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/5/20 17:15
 */
@SuppressWarnings("WeakerAccess")
public class RongYunUtils {
    private static int mMessageCount = 5;//一次获取历史消息的最大数量

    /**
     * 获取会话历史消息
     */
    public static void getLocalHistoryMessage(final Conversation.ConversationType conversationType, final String targetId, final XRecyclerView mRvMsg, final BaseAdapter<Message> mAdapter) {
        //没有消息第一次调用应设置为:-1。
        int messageId;
        if (mAdapter.getItemCount() > 0) {
            messageId = mAdapter.getList().get(0).getMessageId();
        } else {
            messageId = -1;
        }
        // 从本地获取消息数量
        RongIMClient.getInstance().getHistoryMessages(conversationType, targetId, messageId, mMessageCount, new RongIMClient.ResultCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> messages) {
//                getView().getRefreshLayout().endRefreshing();
                if (messages == null || messages.size() == 0)
                    // 本地没有数据，从服务器上获取
                    getRemoteHistoryMessages(conversationType, targetId, mRvMsg, mAdapter);
                else
                    setHistoryMsg(messages, mRvMsg, mAdapter);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
//                getView().getRefreshLayout().endRefreshing();
                loadMessageError(mRvMsg, errorCode);
            }
        });
    }

    /**
     * 获取会话历史消息,从服务器上获取
     */
    public static void getRemoteHistoryMessages(Conversation.ConversationType conversationType, String targetId, final XRecyclerView mRvMsg, final BaseAdapter<Message> mAdapter) {
        //消息中的 sentTime；第一次可传 0，获取最新 count 条。
        long dateTime = 0;
        if (mAdapter.getItemCount() > 0) {
            dateTime = mAdapter.getList().get(0).getSentTime();
        } else {
            dateTime = 0;
        }

        RongIMClient.getInstance().getRemoteHistoryMessages(conversationType, targetId, dateTime, mMessageCount,
                new RongIMClient.ResultCallback<List<Message>>() {
                    @Override
                    public void onSuccess(List<Message> messages) {
                        setHistoryMsg(messages, mRvMsg, mAdapter);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        loadMessageError(mRvMsg, errorCode);
                    }
                });
    }

    /**
     * 给列表设置历史消息
     */
    private static void setHistoryMsg(final List<Message> messages, final XRecyclerView mRvMsg, BaseAdapter<Message> mAdapter) {
        //messages的时间顺序从新到旧排列，所以必须反过来加入到mData中
        if (messages != null && messages.size() > 0) {
            // 设置刷新完成

            List<Message> list = mAdapter.getList();
            int size = mAdapter.getItemCount();
            for (Message msg : messages) {
                list.add(0, msg);
            }

            mRvMsg.refreshComplete();
            mAdapter.notifyItemRangeInserted(0, messages.size());

            if (size == 0) {
                mRvMsg.scrollToPosition(messages.size());
            }
        }
    }

    private static void loadMessageError(XRecyclerView mRvMsg, RongIMClient.ErrorCode errorCode) {
        System.out.println("拉取历史消息失败，errorCode = " + errorCode);
        // 设置刷新完成
        mRvMsg.refreshComplete();
    }

    /**
     * 发送文本消息
     */
    public static void sendTextMsg(Conversation.ConversationType conversationType, String targetId, String content, final RecyclerView mRvMsg, final BaseAdapter<Message> mAdapter) {
        String pushContent = getPushContent(content);
        RongIMClient.getInstance().sendMessage(conversationType, targetId, TextMessage.obtain(content), pushContent, null,
                new RongIMClient.SendMessageCallback() {// 发送消息的回调
                    @Override
                    public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                        updateMessageStatus(mAdapter, integer);
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        updateMessageStatus(mAdapter, integer);
                    }
                }, new RongIMClient.ResultCallback<Message>() {//消息存库的回调，可用于获取消息实体
                    @Override
                    public void onSuccess(Message message) {
                        mAdapter.addItemAtLastHasHeader(message);
                        rvMoveToBottom(mRvMsg);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        System.out.println(errorCode);
                    }
                });
    }

    /**
     * 获取推送内容
     */
    private static String getPushContent(String content) {
        User user = ZhaoLinApplication.getInstance().getLoginUser();
        return user == null || TextUtils.isEmpty(user.getData().getUsername())
                ? content : user.getData().getUsername() + ":" + content;
    }

    /**
     * 发送图片消息
     */
    public static void sendImgMsg(Conversation.ConversationType conversationType, String targetId, Uri imageFileThumbUri, Uri imageFileSourceUri, final RecyclerView mRvMsg, final BaseAdapter<Message> mAdapter) {
        String pushContent = getPushContent("[图片]");
        ImageMessage imgMsg = ImageMessage.obtain(imageFileThumbUri, imageFileSourceUri);
        RongIMClient.getInstance().sendImageMessage(conversationType, targetId, imgMsg, pushContent, null,
                new RongIMClient.SendImageMessageCallback() {
                    @Override
                    public void onAttached(Message message) {
                        //保存数据库成功
                        mAdapter.addItemAtLastHasHeader(message);
                        rvMoveToBottom(mRvMsg);
                    }

                    @Override
                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                        //发送失败
                        updateMessageStatus(mAdapter, message);
                    }

                    @Override
                    public void onSuccess(Message message) {
                        //发送成功
                        updateMessageStatus(mAdapter, message);
                        rvMoveToBottom(mRvMsg);
                    }

                    @Override
                    public void onProgress(Message message, int progress) {
                        //发送进度
                        message.setExtra(progress + "");
                        updateMessageStatus(mAdapter, message);
                    }
                });
    }

    /**
     * 发送文件消息
     */
    public static void sendFileMsg(Conversation.ConversationType conversationType, String targetId, File file, final RecyclerView mRvMsg, final BaseAdapter<Message> mAdapter) {
        String pushContent = getPushContent("[文件]");

        Message fileMessage = Message.obtain(targetId, conversationType, FileMessage.obtain(Uri.fromFile(file)));
//        fileMessage.getContent().setMyUserInfo();
        RongIMClient.getInstance().sendMediaMessage(fileMessage, pushContent, null, new IRongCallback.ISendMediaMessageCallback() {
            @Override
            public void onProgress(Message message, int progress) {
                //发送进度
                message.setExtra(progress + "");
                updateMessageStatus(mAdapter, message);
            }

            @Override
            public void onCanceled(Message message) {

            }

            @Override
            public void onAttached(Message message) {
                //保存数据库成功
                mAdapter.addItemAtLastHasHeader(message);
                rvMoveToBottom(mRvMsg);
            }

            @Override
            public void onSuccess(Message message) {
                //发送成功
                updateMessageStatus(mAdapter, message);
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                //发送失败
                updateMessageStatus(mAdapter, message);
            }
        });
    }

    /**
     * 发送语音消息
     */
    public static void sendAudioFile(Conversation.ConversationType conversationType, String targetId, Uri audioPath, int duration, final RecyclerView mRvMsg, final BaseAdapter<Message> mAdapter) {
        if (audioPath != null) {
            File file = new File(audioPath.getPath());
            if (!file.exists() || file.length() == 0L) {
                System.out.println("发送语音失败，文件长度为0或语音权限没有开启");
                return;
            }
            String pushContent = getPushContent("[语音]");
            VoiceMessage voiceMessage = VoiceMessage.obtain(audioPath, duration);
            RongIMClient.getInstance().sendMessage(Message.obtain(targetId, conversationType, voiceMessage), pushContent, null, new IRongCallback.ISendMessageCallback() {
                @Override
                public void onAttached(Message message) {
                    //保存数据库成功
                    mAdapter.addItemAtLastHasHeader(message);
                    rvMoveToBottom(mRvMsg);
                }

                @Override
                public void onSuccess(Message message) {
                    //发送成功
                    updateMessageStatus(mAdapter, message);
                }

                @Override
                public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                    //发送失败
                    updateMessageStatus(mAdapter, message);
                }
            });
        }
    }

    /**
     * 发送定位消息
     */
    public static void sendLocationMessage(Conversation.ConversationType conversationType, String targetId, double lat, double lng, String poi, final RecyclerView mRvMsg, final BaseAdapter<Message> mAdapter) {
        String pushContent = getPushContent("[语音]");

        LocationMessage message = LocationMessage.obtain(lat, lng, poi, Uri.parse(getMapUrl(lat, lng)));
        RongIMClient.getInstance().sendLocationMessage(Message.obtain(targetId, conversationType, message), pushContent, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
                //保存数据库成功
                mAdapter.addItemAtLastHasHeader(message);
                rvMoveToBottom(mRvMsg);
            }

            @Override
            public void onSuccess(Message message) {
                //发送成功
                updateMessageStatus(mAdapter, message);
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                //发送失败
                updateMessageStatus(mAdapter, message);
            }
        });
    }

    //    获取位置静态图
    //    http://apis.map.qq.com/ws/staticmap/v2/?center=39.8802147,116.415794&zoom=10&size=600*300&maptype=landform&markers=size:large|color:0xFFCCFF|label:k|39.8802147,116.415794&key=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77
    //    http://st.map.qq.com/api?size=708*270&center=114.215843,22.685120&zoom=17&referer=weixin
    //    http://st.map.qq.com/api?size=708*270&center=116.415794,39.8802147&zoom=17&referer=weixin
    private static String getMapUrl(double lat, double lng) {
        return "http://st.map.qq.com/api?size=708*270&center=" + lng + "," + lat + "&zoom=17&referer=weixin";
    }

    /**
     * 重置草稿，获取到草稿，然后赋值
     */
    public static void resetDraft(final Conversation.ConversationType conversationType, final String targetId, final EditText editText) {
        // 获取草稿
        RongIMClient.getInstance().getTextMessageDraft(conversationType, targetId, new RongIMClient.ResultCallback<String>() {
            @Override
            public void onSuccess(String s) {
                editText.setText(s);
                // 清除草稿
                RongIMClient.getInstance().clearTextMessageDraft(conversationType, targetId, new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {

                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    /**
     * 保存草稿
     */
    public static void saveDraft(Conversation.ConversationType conversationType, String targetId, EditText editText) {
        String draft = editText.getText().toString();
        if (!TextUtils.isEmpty(draft)) {
            RongIMClient.getInstance().saveTextMessageDraft(conversationType, targetId, draft, new RongIMClient.ResultCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {

                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }

    /**
     * 接收到新消息
     */
    public static void receiveNewMessage(Conversation.ConversationType conversationType, String targetId, Message message, RecyclerView mRvMsg, BaseAdapter<Message> mAdapter) {
        mAdapter.addItemAtLastHasHeader(message);
        rvMoveToBottom(mRvMsg);
        setMessageIsRead(conversationType, targetId);
    }

    /**
     * 设置消息已读
     */
    public static void setMessageIsRead(Conversation.ConversationType conversationType, String targetId) {
        RongIMClient.getInstance().clearMessagesUnreadStatus(conversationType, targetId, new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                LogUtil.e("=====onSuccess");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                LogUtil.e("=====onError");
            }
        });
    }

    /**
     * 更新消息状态--根据messageId
     */
    public static void updateMessageStatus(final BaseAdapter<Message> mAdapter, int messageId) {
        RongIMClient.getInstance().getMessage(messageId, new RongIMClient.ResultCallback<Message>() {
            @Override
            public void onSuccess(Message message) {
                updateMessageStatus(mAdapter, message);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    /**
     * 更新消息状态--根据message
     */
    private static void updateMessageStatus(BaseAdapter<Message> mAdapter, Message message) {
        List<Message> mData = mAdapter.getList();
        for (int i = 0; i < mData.size(); i++) {
            Message msg = mData.get(i);
            if (msg.getMessageId() == message.getMessageId()) {
                mAdapter.changeItem(msg, message);
                return;
            }
        }
    }

    /**
     * 设置消息已读
     */
    public static void setMessageReceivedStatusIsRead(final BaseAdapter<Message> mAdapter, final Message message) {
        message.getReceivedStatus().setListened();
        RongIMClient.getInstance().setMessageReceivedStatus(message.getMessageId(), message.getReceivedStatus(), new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                // 更新消息状态
                updateMessageStatus(mAdapter, message);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });

    }

    /**
     * 设置消息已读
     */
    public static boolean checkConnect(Context context) {
        RongIMClient.ConnectionStatusListener.ConnectionStatus currentConnectionStatus = RongIMClient.getInstance().getCurrentConnectionStatus();
//        NETWORK_UNAVAILABLE(-1, "Network is unavailable."),
//                CONNECTED(0, "Connect Success."),
//                CONNECTING(1, "Connecting"),
//                DISCONNECTED(2, "Disconnected"),
//                KICKED_OFFLINE_BY_OTHER_CLIENT(3, "Login on the other device, and be kicked offline."),
//                TOKEN_INCORRECT(4, "Token incorrect."),
//                SERVER_INVALID(5, "Server invalid.");
        switch (currentConnectionStatus) {
            case NETWORK_UNAVAILABLE:
                // 无网络
                System.out.println("无网络");
                return false;
            case CONNECTED:
            case CONNECTING:
                // 链接成功
                return true;
            case DISCONNECTED:
                // 未链接
                System.out.println("融云未链接成功");
                return false;
            case KICKED_OFFLINE_BY_OTHER_CLIENT:
                // 其他设备登录了
                System.out.println("其他设备登录了");
                return false;
            case TOKEN_INCORRECT:
                // token失效
                System.out.println("token失效");
                return false;
            case SERVER_INVALID:
                // 服务无效
                System.out.println("服务无效");
                return false;
            default:
                return false;
        }
    }

    /**
     * 分页获取会话列表
     */
    public static void getConversationListByPage(RongIMClient.ResultCallback<List<Conversation>> callback, long timeStamp, int count, Conversation.ConversationType... conversationTypes) {
        /**
         * <p>分页获取会话列表</p>
         * <p><strong>注意：</strong>当更换设备或者清除缓存后，能拉取到暂存在融云服务器中该账号当天收发过消息的会话。</p>
         *
         * @param callback          获取会话列表的回调
         * @param timeStamp         时间戳，获取从此时间戳往前的会话，第一次传 0
         * @param count             取回的会话个数。当实际取回的会话个数小于 count 值时，表明已取完数据
         * @param conversationTypes 选择要获取的会话类型
         */
        RongIMClient.getInstance().getConversationListByPage(callback, timeStamp, count, conversationTypes);
    }

    /**
     * 获取某会话的未读消息数
     */
    public static void getUnreadCount(Conversation.ConversationType conversationType, String targetId, RongIMClient.ResultCallback<Integer> callback) {
        /**
         * 根据会话类型的目标 Id，回调方式获取来自某用户（某会话）的未读消息数。
         *
         * @param conversationType 会话类型。
         * @param targetId         目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id。
         * @param callback         未读消息数的回调。
         */
        RongIMClient.getInstance().getUnreadCount(conversationType, targetId, callback);
    }

    /**
     * 获取所有未读消息数。即除了聊天室之外其它所有会话类型的未读消息数。
     *
     * @param callback 消息数的回调。
     */
    public static void getTotalUnreadCount(RongIMClient.ResultCallback<Integer> callback) {
        RongIMClient.getInstance().getUnreadCount(callback, Conversation.ConversationType.PRIVATE);
    }

    /**
     * 设置滚动到底部
     */
    public static void rvMoveToBottom(final RecyclerView mRvMsg) {
        if (mRvMsg == null || mRvMsg.getAdapter() == null)
            return;
        mRvMsg.postDelayed(new Runnable() {
            @Override
            public void run() {
                int position = mRvMsg.getAdapter().getItemCount();
                if (position < 0)
                    position = 0;
                mRvMsg.smoothScrollToPosition(position);
            }
        }, 50);
    }
}
