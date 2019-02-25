package com.xxzlkj.licallife.fragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.activity.localLife.LocalLifeRefundGoodsInfoActivity;
import com.xxzlkj.licallife.adapter.JzRefundDetailAdapter;
import com.xxzlkj.licallife.adapter.JzRefundOrderGoodsAdapter;
import com.xxzlkj.licallife.event.AddRefundEvent;
import com.xxzlkj.licallife.model.JzRefundOrderGoods;
import com.xxzlkj.licallife.model.LocaLifeRefundGoods;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.ViewPageFragment;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;


/**
 * 售后（本地生活）
 */
public class JzAfterSaleTabFragment extends ViewPageFragment {
    private MyRecyclerView mRecyclerView;
    // 可申请售后适配器
    private JzRefundOrderGoodsAdapter mRefundOrderGoodsAdapter;
    // 退款详情适配器
    private JzRefundDetailAdapter mRefundDetailAdapter;
    private int pageNo = 1;
    // 类型 0:可申请售后订单列表 1:已经申请售后订单列表
    private int mType;

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_after_sale_tab, container, false);
    }

    @Override
    protected void findViewById() {
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        mRecyclerView = getView(R.id.recyclerView);
        Bundle bundle = getArguments();
        if (bundle != null){
            mType = bundle.getInt(StringConstants.INTENT_PARAM_TYPE);
        }
        if (BaseApplication.getInstance().getLoginUserDoLogin(getActivity()) == null){
            mActivity.finish();
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setPullRefreshAndLoadingMoreEnabled(true, true);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                if (mType == 0){// 可申请售后订单列表
                    if (BaseApplication.getInstance().getLoginUserDoLogin(getActivity()) != null){
                        requestAfterSale();
                    }
                }else {// 已经申请售后订单列表
                    if (BaseApplication.getInstance().getLoginUserDoLogin(getActivity()) != null){
                        requestRefundInfo();
                    }
                }

            }

            @Override
            public void onLoadMore() {
                if (mType == 0){// 可申请售后订单列表
                    pageNo = mRefundOrderGoodsAdapter.getItemCount() / mRecyclerView.loadSize + 1;
                    requestAfterSale();
                }else {// 已经申请售后订单列表
                    pageNo = mRefundDetailAdapter.getItemCount() / mRecyclerView.loadSize + 1;
                    requestRefundInfo();
                }

            }
        });

        mRefundOrderGoodsAdapter = new JzRefundOrderGoodsAdapter(mContext,mActivity, R.layout.adapter_jz_ast_list_item);
        mRefundDetailAdapter = new JzRefundDetailAdapter(mContext,mActivity, R.layout.adapter_rd_list_item);
        if (mType == 0){
            mRecyclerView.setAdapter(mRefundOrderGoodsAdapter);
        }else {
            mRecyclerView.setAdapter(mRefundDetailAdapter);
        }
    }

    @Override
    public void setListener() {
        mRefundDetailAdapter.setOnItemClickListener((position, item) -> mActivity.startActivity(LocalLifeRefundGoodsInfoActivity.newIntent(mContext, item.getId())));
    }

    @Override
    public void processLogic() {

    }

    /**
     *
     * @param type 0:可申请售后订单列表 1:已经申请售后订单列表
     * @return
     */
    public static Bundle newBundle(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(StringConstants.INTENT_PARAM_TYPE, type);
        return bundle;
    }

    /**
     *
     * @param type 0:可申请售后订单列表 1:已经申请售后订单列表
     * @return
     */
    public static JzAfterSaleTabFragment newInstance(int type) {
        JzAfterSaleTabFragment fragment = new JzAfterSaleTabFragment();
        fragment.setArguments(newBundle(type));
        return fragment;
    }

    @Override
    public void refreshOnceData() {
        pageNo = 1;
        if (mType == 0){// 可申请售后订单列表
            if (BaseApplication.getInstance().getLoginUserDoLogin(getActivity()) != null){
                requestAfterSale();
            }
        }else {// 已经申请售后订单列表
            if (BaseApplication.getInstance().getLoginUserDoLogin(getActivity()) != null){
                requestRefundInfo();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 可申请售后订单请求
     */
    private void requestAfterSale() {
        HashMap<String, String> map = new HashMap<>();

        map.put(URLConstants.REQUEST_PARAM_UID, BaseApplication.getInstance().getLoginUser().getData().getId());
        map.put(URLConstants.REQUEST_PARAM_PAGE,String.valueOf(pageNo));
        RequestManager.createRequest(URLConstants.JZ_REFUND_ORDER_GOODS_LIST, map,
                new OnMyActivityRequestListener<JzRefundOrderGoods>((BaseActivity) mActivity) {

            @Override
            public void onSuccess(JzRefundOrderGoods bean) {
                mRecyclerView.handlerSuccessHasRefreshAndLoad(mRefundOrderGoodsAdapter, pageNo == 1, bean.getData());
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                super.onFailed(isError, code, message);
                mRecyclerView.handlerError(mRefundOrderGoodsAdapter);
            }
        });
    }

    /**
     * 退款详情列表网络请求
     */
    private void requestRefundInfo() {
        HashMap<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_UID, BaseApplication.getInstance().getLoginUser().getData().getId());
        map.put(URLConstants.REQUEST_PARAM_PAGE,String.valueOf(pageNo));
        RequestManager.createRequest(URLConstants.JZ_REFUND_GOODS, map, new OnMyActivityRequestListener<LocaLifeRefundGoods>((BaseActivity) mActivity) {

            @Override
            public void onSuccess(LocaLifeRefundGoods bean) {
                mRecyclerView.handlerSuccessHasRefreshAndLoad(mRefundDetailAdapter, pageNo == 1, bean.getData());
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                super.onFailed(isError, code, message);
                mRecyclerView.handlerError(mRefundDetailAdapter);
            }
        });
    }

    /**
     * 数据更新
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = false)
    public void updataData(AddRefundEvent event){
        if (event.isUpdata()){
            pageNo = 1;
            if (mType == 0){
                if (BaseApplication.getInstance().getLoginUserDoLogin(getActivity()) != null){
                    requestAfterSale();
                }
            }else {
                if (BaseApplication.getInstance().getLoginUserDoLogin(getActivity()) != null){
                    requestRefundInfo();
                }
            }
        }
    }

}
