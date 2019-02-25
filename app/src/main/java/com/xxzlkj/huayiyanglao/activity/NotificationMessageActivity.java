package com.xxzlkj.huayiyanglao.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.activity.found.FoundDetailActivity;
import com.xxzlkj.huayiyanglao.adapter.NotificationMessageAdapter;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.model.MessageBean;
import com.xxzlkj.huayiyanglao.util.JumpToWebView;
import com.xxzlkj.shop.activity.shop.GoodsDetailActivity;
import com.xxzlkj.shop.activity.shop.SearchGoodsListActivity;
import com.xxzlkj.shop.activity.shopOrder.OrderDesActivity;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 通知消息
 */
public class NotificationMessageActivity extends MyBaseActivity {
    public static final String MESSAGE_TYPE = "message_type";
    private MyRecyclerView mMyRecyclerView;
    private NotificationMessageAdapter mMessageCenterAdapter;
    private int page = 1;
    // 分类消息id
    private String mMessageTypeId;
    // 标题
    private String mTitle;

    /**
     * @param context     上下文  （必传）
     * @param messagetype 分类消息类型  （必传）
     * @param title       标题栏 标题  （必传）
     */
    public static Intent newIntent(Context context, String messagetype, String title) {
        Intent intent = new Intent(context, NotificationMessageActivity.class);
        intent.putExtra(MESSAGE_TYPE, messagetype);
        intent.putExtra(StringConstants.INTENT_PARAM_TITLE, title);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity__notification_message);
    }

    @Override
    protected void findViewById() {
        mMyRecyclerView = getView(R.id.id_mc_list);
        mMyRecyclerView.setPullRefreshAndLoadingMoreEnabled(true, true);
        mMyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMessageCenterAdapter = new NotificationMessageAdapter(this, R.layout.adapter_notification_message_item);
        mMyRecyclerView.setAdapter(mMessageCenterAdapter);

        mMessageTypeId = getIntent().getStringExtra(MESSAGE_TYPE);
        mTitle = getIntent().getStringExtra(StringConstants.INTENT_PARAM_TITLE);
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
            // 跳转类型
            int type = NumberFormatUtils.toInt(item.getType());
            switch (type) {
                case 0:// 普通消息
                case 1:// 优惠券
                    startActivity(MessageDesActivity.newIntent(mContext, URLConstants.CONTENT_TABLE_MESSAGE_URL + item.getId(), item.getType()));
                    break;
                case 2:// h5
                    JumpToWebView.getInstance(this).jumpToHasTitleWebView(item.getUrl(), item.getTitle());
                    break;
                case 3:// 商品
                    startActivity(GoodsDetailActivity.newIntent(mContext, item.getGoods_id()));
                    break;
                case 4:// 果蔬生鲜
                    startActivity(SearchGoodsListActivity.newIntent(mContext, 2, item.getMarket_id(), "", ""));
                    break;
                case 5:// 发现活动
                    startActivity(FoundDetailActivity.newIntent(mContext, item.getActivity_id()));
                    break;
                case 6:// 订单详情
                    // 跳转到订单详情
                    startActivity(OrderDesActivity.newIntent(mContext, item.getOrderid()));
                    break;
            }

        });
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName(mTitle);
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
        map.put(URLConstants.REQUEST_PARAM_MESSAGE_TYPE, mMessageTypeId);
        map.put(URLConstants.REQUEST_PARAM_PAGE, page + "");
        RequestManager.createRequest(URLConstants.MESSAGE_URL, map, new OnMyActivityRequestListener<MessageBean>(this) {
            @Override
            public void onSuccess(MessageBean bean) {
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
