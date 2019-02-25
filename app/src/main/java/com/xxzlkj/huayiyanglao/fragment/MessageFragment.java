package com.xxzlkj.huayiyanglao.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.SessionActivity;
import com.xxzlkj.huayiyanglao.activity.ContactActivity;
import com.xxzlkj.huayiyanglao.activity.MessageCenterActivity;
import com.xxzlkj.huayiyanglao.activity.SearchActivity;
import com.xxzlkj.huayiyanglao.adapter.MessageAdapter;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.util.RongYunUtils;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.ReuseViewFragment;
import com.xxzlkj.zhaolinshare.base.util.SystemBarUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;
import com.xxzlkj.zhaolinshare.chat.event.ReceiveNewMessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;


/**
 * 描述：消息
 *
 * @author zhangrq
 *         2017/9/2 10:58
 */
public class MessageFragment extends ReuseViewFragment {
    private MyRecyclerView mMyRecyclerView;
    private MessageAdapter mMessageAdapter;
    private TextView tv_title_left;
    private long timeStamp;

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    protected void findViewById() {
        SystemBarUtils.setPaddingTopStatusBarHeight(getActivity(), getView(R.id.titleBar));
        tv_title_left = getView(R.id.tv_title_left);// 左边
        mMyRecyclerView = getView(R.id.id_recyclerview);// 消息列表
        mMyRecyclerView.setPullRefreshAndLoadingMoreEnabled(true, true);
        mMyRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mMessageAdapter = new MessageAdapter(mContext, R.layout.adapter_message_list_item);
        mMyRecyclerView.setAdapter(mMessageAdapter);
    }

    @Override
    public void setListener() {
        EventBus.getDefault().register(this);
        // 通讯录
        tv_title_left.setOnClickListener(v -> startActivity(new Intent(mContext, ContactActivity.class)));
        // 刷新和加载监听
        mMyRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                timeStamp = 0;
                getConversationListByPage();
            }

            @Override
            public void onLoadMore() {
                if (mMessageAdapter != null && mMessageAdapter.getItemCount() > 0) {
                    // 之后还有
                    Conversation conversation = mMessageAdapter.getList().get(mMessageAdapter.getItemCount() - 1);
                    timeStamp = conversation.getReceivedTime();
                }
                getConversationListByPage();
            }
        });
        // 设置点击
        mMessageAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Conversation>() {
            @Override
            public void onClick(int position, Conversation item) {
                // 跳到聊天页面
                // 1000:系统消息；1001互动消息
                String targetId = item.getTargetId();
                if (ZLConstants.Strings.SYSTEM_MESSAGE_ID.equals(targetId)) {
                    // 系统消息
                    // 设置消息已读
                    RongYunUtils.setMessageIsRead(Conversation.ConversationType.PRIVATE, ZLConstants.Strings.SYSTEM_MESSAGE_ID);
                    // 跳转到消息中心
                    startActivity(new Intent(mContext, MessageCenterActivity.class));
                } else if (ZLConstants.Strings.INTERACTIVE_MESSAGE_ID.equals(targetId)) {
                    // 互动消息
                    // 设置消息已读
                    RongYunUtils.setMessageIsRead(Conversation.ConversationType.PRIVATE, ZLConstants.Strings.INTERACTIVE_MESSAGE_ID);
                    // 跳转到互动消息列表
//                    startActivity(new Intent(mContext, InteractionLeaveWordActivity.class));
                } else {
                    // 普通聊天页面
                    startActivity(SessionActivity.newIntent(mContext, targetId));
                }
            }
        });
    }

    @Override
    public void processLogic() {
        tv_title_left.setText("通讯录");
        setTitleName("消息");
        setTitleRightText("添加");
    }

    @Override
    public void onTitleRightClick(View view) {
        // 跳转到搜索界面
        startActivity(SearchActivity.newIntent(mContext));
    }

    @Override
    public void onResume() {
        super.onResume();
        // 分页获取会话列表
        getConversationListByPage();
    }

    /**
     * 分页获取会话列表
     */
    private void getConversationListByPage() {
        if (RongYunUtils.checkConnect(mContext)) {
            RongYunUtils.getConversationListByPage(new RongIMClient.ResultCallback<List<Conversation>>() {
                @Override
                public void onSuccess(List<Conversation> conversations) {
                    int loadSize = 20;
//                1000:系统消息；1001互动消息
                    if (conversations != null && conversations.size() > 0) {
                        // 特殊处理，排序，互动消息置顶
                        for (int i = 0; i < conversations.size(); i++) {
                            Conversation conversation = conversations.get(i);
                            if (ZLConstants.Strings.INTERACTIVE_MESSAGE_ID.equals(conversation.getTargetId())) {
                                // 此为：互动消息，置顶
                                if (timeStamp == 0) {
                                    // 刷新，数据源排序
                                    conversations.remove(conversation);
                                    conversations.add(0, conversation);
                                } else {
                                    // 加载，数据源删除此条，列表在顶部添加此条
                                    loadSize = 19;
                                    mMessageAdapter.getList().add(0, conversation);
                                    conversations.remove(conversation);
                                }

                                break;
                            }
                        }
                    }

                    MyRecyclerView.handlerSuccessHasRefreshAndLoad(mMyRecyclerView.getxRecyclerView(), mMessageAdapter, loadSize, timeStamp == 0, conversations);
                    mMyRecyclerView.checkDataShow(mMessageAdapter);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    mMyRecyclerView.handlerError(mMessageAdapter, timeStamp == 0);
                    ToastManager.showShortToast(mContext, "获取回话列表错误" + errorCode);
                }
            }, timeStamp, 20, Conversation.ConversationType.PRIVATE);
        } else {
            // 没链接上
            mMyRecyclerView.showView(false, true);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true) //在ui线程执行
    public void receiveNewMessage(ReceiveNewMessageEvent event) {
        getConversationListByPage();
    }
}