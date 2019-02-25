package com.xxzlkj.huayiyanglao.activity;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.adapter.MonitoringMessageAdapter;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.MessageCenterListBean;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;

import java.util.HashMap;
import java.util.Map;

/**
 * 监护消息
 */
public class MonitoringMessageActivity extends MyBaseActivity {
    public static final int REQUEST_CODE_555 = 555;
    private MyRecyclerView myRecyclerView;
    private MonitoringMessageAdapter mMonitoringMessageAdapter;
    private int page = 1;
    private int clickPosition;

    public static Intent newIntent(Context context) {
        return new Intent(context, MonitoringMessageActivity.class);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_monitoring_message);
    }

    @Override
    protected void findViewById() {
        myRecyclerView = getView(R.id.id_recycler_view);// 消息列表
        myRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        myRecyclerView.setPullRefreshAndLoadingMoreEnabled(true, true);
        mMonitoringMessageAdapter = new MonitoringMessageAdapter(mContext, this, R.layout.adapter_monitoring_message_list_item);
        myRecyclerView.setAdapter(mMonitoringMessageAdapter);
    }

    @Override
    protected void setListener() {
        // 刷新和加载监听
        myRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getWatchGuardianship();
            }

            @Override
            public void onLoadMore() {
                page = mMonitoringMessageAdapter.getItemCount() / 20 + 1;
                getWatchGuardianship();
            }
        });
        //item点击事件
        mMonitoringMessageAdapter.setOnItemClickListener((position, item) -> {
            clickPosition = position;
            // 1 申请中 2已通过 3申请失败
            String state = item.getState();
            if ("1".equals(state))
                // 跳转到详情
                startActivityForResult(CheckGuardianshipRequestActivity.newIntent(mContext, item.getId()), REQUEST_CODE_555);
        });
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("监护消息");
        getWatchGuardianship();
    }


    /**
     * 申请监护列表
     */
    private void getWatchGuardianship() {
        Map<String, String> map = new HashMap<>();
        User loginUserDoLogin = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (loginUserDoLogin == null) {
            finish();
            return;
        }
        // 用户id
        map.put(ZLConstants.Params.UID, loginUserDoLogin.getData().getId());
        // 分页
        map.put(ZLConstants.Params.PAGE, page + "");
        RequestManager.createRequest(ZLURLConstants.WATCH_GUARDIANSHIP_URL, map, new OnMyActivityRequestListener<MessageCenterListBean>(this) {
            @Override
            public void onSuccess(MessageCenterListBean bean) {
                myRecyclerView.handlerSuccessHasRefreshAndLoad(mMonitoringMessageAdapter, page == 1, bean.getData());
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                myRecyclerView.handlerError(mMonitoringMessageAdapter, page == 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case REQUEST_CODE_555:
                    mMonitoringMessageAdapter.getList().get(clickPosition).setState(CheckGuardianshipRequestActivity.getIntentResult(data));
                    mMonitoringMessageAdapter.notifyDataSetChanged();
                    break;
            }

        }
    }
}
