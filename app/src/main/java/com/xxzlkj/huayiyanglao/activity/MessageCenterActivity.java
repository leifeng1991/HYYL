package com.xxzlkj.huayiyanglao.activity;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.adapter.MessageCenterAdapter;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.model.MessageTypeBean;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息中心
 */
public class MessageCenterActivity extends MyBaseActivity {
    private MyRecyclerView mMyRecyclerView;
    private MessageCenterAdapter mMessageCenterAdapter;
    private int page = 1;

    public static Intent newIntent(Context context) {
        return new Intent(context, MessageCenterActivity.class);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_message_center);
    }

    @Override
    protected void findViewById() {
        mMyRecyclerView = getView(R.id.id_recycler_view);
        mMyRecyclerView.setPullRefreshAndLoadingMoreEnabled(true, true);
        mMyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMessageCenterAdapter = new MessageCenterAdapter(this, R.layout.adapter_message_center_item);
        mMyRecyclerView.setAdapter(mMessageCenterAdapter);
    }

    @Override
    protected void setListener() {
        mMyRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getNetData();
            }

            @Override
            public void onLoadMore() {
                page = mMessageCenterAdapter.getItemCount() / mMyRecyclerView.loadSize + 1;
                getNetData();
            }
        });
        // 读取消息详情
        mMessageCenterAdapter.setOnItemClickListener((position, item) -> {
            if ("5".equals(item.getId())) {
                // 监护消息
                startActivity(MonitoringMessageActivity.newIntent(mContext));
            } else {
                // 其他
                startActivity(NotificationMessageActivity.newIntent(mContext, item.getId(), item.getTitle()));
            }
            // 标记已读
            item.setMessage_state("2");
            mMessageCenterAdapter.notifyDataSetChanged();

        });
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("消息中心");
        // 因为有可能是从消息推送里面跳转过来的，所以判断登录
        User loginUserDoLogin = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (loginUserDoLogin == null) {
            // 没登录，设置登录回调
            LoginActivity.setOnLoginListener(new LoginActivity.OnLoginListener() {
                @Override
                public void onLoginSuccess() {
                    // 登录成功，获取数据
                    getNetData();
                }

                @Override
                public void onLoginFailed() {
                    // 跳到登录页面，点了返回键等取消操作
                    finish();
                }
            });
        } else {
            // 登录了，获取数据
            getNetData();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    /**
     * 消息列表数据
     */
    private void getNetData() {
        User loginUser = ZhaoLinApplication.getInstance().getLoginUser();
        if (loginUser == null) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_UID, loginUser.getData().getId());
        map.put(URLConstants.REQUEST_PARAM_PAGE, page + "");
        RequestManager.createRequest(URLConstants.MESSAGE_TYPE_URL, map, new OnMyActivityRequestListener<MessageTypeBean>(this) {
            @Override
            public void onSuccess(MessageTypeBean bean) {
                mMyRecyclerView.handlerSuccessHasRefreshAndLoad(mMessageCenterAdapter, page == 1, bean.getData());
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                super.onFailed(isError, code, message);
                mMyRecyclerView.handlerError(mMessageCenterAdapter, page == 1);
            }
        });
    }

}
