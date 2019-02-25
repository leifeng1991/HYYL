package com.xxzlkj.licallife.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.activity.localLife.LocalLifeDesActivity;
import com.xxzlkj.licallife.adapter.LocalLifeListAdapter;
import com.xxzlkj.licallife.model.LocalLifeListBean;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.ViewPageFragment;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;

import java.util.HashMap;

/**
 * 描述:本地生活列表（本地生活）
 *
 * @author zhangrq
 *         2017/3/20 16:37
 */
public class LocalLifeListTabFragment extends ViewPageFragment {

    private MyRecyclerView mRecyclerView;
    private LocalLifeListAdapter adapter;
    private String state;
    private String uid;
    private int pageNo;

    public static Bundle newBundle(String state) {
        Bundle bundle = new Bundle();
        bundle.putString("state", state);
        return bundle;
    }

    public static LocalLifeListTabFragment newInstance(String state) {
        LocalLifeListTabFragment fragment = new LocalLifeListTabFragment();
        fragment.setArguments(newBundle(state));
        return fragment;
    }

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_tab_local_life_list, container, false);
    }

    @Override
    protected void findViewById() {
        mRecyclerView = getView(R.id.recyclerView);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void processLogic() {
        // 0为全部
        state = getArguments().getString("state");
        User user = BaseApplication.getInstance().getLoginUser();
        uid = user.getData().getId();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setPullRefreshAndLoadingMoreEnabled(true, false);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                getNetData();
            }

            @Override
            public void onLoadMore() {
                pageNo = adapter.getItemCount() / mRecyclerView.loadSize + 1;
                getNetData();
            }
        });

        adapter = new LocalLifeListAdapter(mContext, R.layout.item_local_life_list);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<LocalLifeListBean.DataBean>() {
            @Override
            public void onClick(int position, LocalLifeListBean.DataBean item) {
                // 跳到详情页面
                startActivityForResult(LocalLifeDesActivity.newIntent(mContext, item.getId(), item.getTables()), 1000);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshOnceData();
    }

    @Override
    public void refreshOnceData() {
        mRecyclerView.refresh();
    }

    private void getNetData() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
//        0或不传 全部订单 1待付款 2待发货 3待收货
        if (!"0".equals(state))
            stringStringHashMap.put("state", state);
        stringStringHashMap.put("uid", uid);
        stringStringHashMap.put("page", pageNo + "");
        RequestManager.createRequest(URLConstants.JZ_ORDER_URL, stringStringHashMap, new OnMyActivityRequestListener<LocalLifeListBean>((BaseActivity) mActivity) {

            @Override
            public void onSuccess(LocalLifeListBean bean) {
                mRecyclerView.handlerSuccessHasRefreshAndLoad(adapter, pageNo == 1, bean.getData());
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                super.onFailed(isError, code, message);
                mRecyclerView.handlerError(adapter, pageNo == 1);
            }
        });
    }
}
