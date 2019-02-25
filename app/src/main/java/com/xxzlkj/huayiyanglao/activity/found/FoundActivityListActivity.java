package com.xxzlkj.huayiyanglao.activity.found;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.adapter.FoundActivityAdapter;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.Found;
import com.xxzlkj.huayiyanglao.webView.ShareWebViewActivity;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;

import java.util.HashMap;


/**
 * 活动列表
 * create an instance of this fragment.
 */
public class FoundActivityListActivity extends MyBaseActivity {
    private MyRecyclerView mRecyclerView;
    private FoundActivityAdapter mFoundAdapter;
    private int page = 1;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, FoundActivityListActivity.class);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_found_activity);
    }

    @Override
    protected void findViewById() {
        mRecyclerView = getView(R.id.id_found_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setPullRefreshAndLoadingMoreEnabled(true, true);
        mFoundAdapter = new FoundActivityAdapter(mContext, this, R.layout.adapter_found_list_item);
        mRecyclerView.setAdapter(mFoundAdapter);

        getView(R.id.iv_title_left).setVisibility(View.GONE);
    }

    @Override
    public void setListener() {
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getFoundList();
            }

            @Override
            public void onLoadMore() {
                page = mFoundAdapter.getItemCount() / mRecyclerView.loadSize + 1;
                getFoundList();
            }
        });
        mFoundAdapter.setOnItemClickListener((position, item) -> {
            if ("3".equals(item.getType())) {
                // 跳转到H5
                startActivity(ShareWebViewActivity.newIntent(mContext, item.getUrl(), item.getIs_share(), item.getShare_img(), item.getTitle(), item.getDesc()));
            } else {
                int pv = NumberFormatUtils.toInt(item.getPv()) + 1;
                item.setPv(pv + "");
                mFoundAdapter.changeItem(position, item);
                startActivity(FoundDetailActivity.newIntent(mContext, item.getId()));
            }
        });
    }

    @Override
    public void processLogic() {
        setTitleLeftBack();
        setTitleName("活动列表");
        getFoundList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 用户参与活动列表
     */
    private void getFoundList() {
        HashMap<String, String> map = new HashMap<>();
        User loginUser = ZhaoLinApplication.getInstance().getLoginUser();
        if (loginUser == null) {
            finish();
            return;
        }
        map.put(URLConstants.REQUEST_PARAM_UID, loginUser.getData().getId());
        map.put(URLConstants.REQUEST_PARAM_PAGE, String.valueOf(page));
        RequestManager.createRequest(ZLURLConstants.MY_ACTIVITY_LIST_URL,
                map, new OnMyActivityRequestListener<Found>(this) {
                    @Override
                    public void onSuccess(Found bean) {
                        mRecyclerView.handlerSuccessHasRefreshAndLoad(mFoundAdapter, page == 1, bean.getData());
                    }

                    @Override
                    public void onFailed(boolean isError, String code, String message) {
                        super.onFailed(isError, code, message);
                        mRecyclerView.handlerError(mFoundAdapter, page == 1);
                    }
                });
    }

}
