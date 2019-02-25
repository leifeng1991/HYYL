package com.xxzlkj.shop.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.shop.SelectCouponsActivity;
import com.xxzlkj.shop.adapter.SelectCouponsListAdapter;
import com.xxzlkj.shop.model.SelectCouponsBean;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.base.ViewPageFragment;

import java.util.List;

/**
 * 描述:选择优惠券tab列表
 *
 * @author zhangrq
 *         2017/3/21 14:48
 */
public class SelectCouponsTabFragment extends ViewPageFragment {

    private MyRecyclerView mRecyclerView;
    private SelectCouponsListAdapter adapter;
    public String state;
    private View tvNoCouponsView;
    private SelectCouponsBean.DataBean.ItemBean checkedBean;
    private Button btn_config;
    private SelectCouponsActivity activity;

    /**
     * @param bundle 传入的bundle
     * @param state  状态，判断当前的tab
     */
    public static SelectCouponsTabFragment newInstance(Bundle bundle, SelectCouponsActivity activity, String state) {
        SelectCouponsTabFragment fragment = new SelectCouponsTabFragment();
        fragment.setArguments(bundle);
        fragment.state = state;
        fragment.activity = activity;
        return fragment;
    }

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_tab_coupons_select, container, false);
    }

    @Override
    protected void findViewById() {
        mRecyclerView = getView(R.id.recyclerView);
        tvNoCouponsView = getView(R.id.tv_no_coupons_view);
        btn_config = getView(R.id.btn_config);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void processLogic() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setPullRefreshAndLoadingMoreEnabled(true, false);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                activity.getNetData();
            }

            @Override
            public void onLoadMore() {
                activity.getNetData();
            }
        });

        adapter = new SelectCouponsListAdapter(mContext, R.layout.item_select_coupons, state);
        mRecyclerView.setAdapter(adapter);

        // state 为1，则没有立即使用按钮，不可点击
        if ("2".equals(state)) {
            // 不可用栏目
            btn_config.setVisibility(View.GONE);
        } else {
            // 可使用栏目
            // 立即使用按钮
            btn_config.setVisibility(View.VISIBLE);
            btn_config.setOnClickListener(v -> {
                Intent data = new Intent();
                data.putExtra(SelectCouponsActivity.CHECKED_ITEM, checkedBean);
                mActivity.setResult(Activity.RESULT_OK, data);
                mActivity.finish();
            });
            // item点击的逻辑
            adapter.setOnItemClickListener((position, item) -> {
                if (item.isChecked()) {
                    // 之前已经选中，取消选中
                    checkedBean = null;
                    item.setChecked(false);
                } else {
                    // 之前没有选中，选中这个
                    checkedBean = item;
                    for (SelectCouponsBean.DataBean.ItemBean dataBean : adapter.getList()) {
                        dataBean.setChecked(dataBean == item);
                    }
                }
                adapter.notifyDataSetChanged();
            });
        }

        // 设置之前选中的位置
        Bundle bundle = getArguments();
        if (bundle != null) {
            checkedBean = (SelectCouponsBean.DataBean.ItemBean) bundle.getSerializable(SelectCouponsActivity.CHECKED_ITEM);
        }

    }


    @Override
    public void refreshOnceData() {

    }

    public void setListData(List<SelectCouponsBean.DataBean.ItemBean> listData) {
        if (listData.size() > 0) {
            // 有数据
            mRecyclerView.setVisibility(View.VISIBLE);
            tvNoCouponsView.setVisibility(View.GONE);
            mRecyclerView.handlerSuccessOnlyHasRefresh(adapter, listData);
            // 设置选中
            for (SelectCouponsBean.DataBean.ItemBean dataBean : adapter.getList()) {
                dataBean.setChecked(dataBean.equals(checkedBean));
            }
            adapter.notifyDataSetChanged();
        } else {
            // 没有数据
            mRecyclerView.setVisibility(View.GONE);
            tvNoCouponsView.setVisibility(View.VISIBLE);
        }
    }

    public void setFailed() {
        // 网络问题等
        mRecyclerView.setVisibility(View.VISIBLE);
        tvNoCouponsView.setVisibility(View.GONE);
        mRecyclerView.handlerError(adapter);
    }
}

