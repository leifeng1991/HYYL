package com.xxzlkj.shop.fragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.shop.R;
import com.xxzlkj.shop.adapter.RefundDetailAdapter;
import com.xxzlkj.shop.adapter.RefundOrderGoodsAdapter;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.event.AfterSaleEvent;
import com.xxzlkj.shop.model.RefundGoods;
import com.xxzlkj.shop.model.RefundOrderGoods;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.ReuseViewFragment;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;


/**
 * 描述:售后
 *
 * @author zhangrq
 *         2017/3/21 14:48
 */
public class AfterSaleTabFragment extends ReuseViewFragment {
    private MyRecyclerView mRecyclerView;
    //可申请售后适配器
    private RefundOrderGoodsAdapter mRefundOrderGoodsAdapter;
    //退款详情适配器
    private RefundDetailAdapter mRefundDetailAdapter;
    private int pageNo = 1;
    private int mType;

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_after_sale_tab, container, false);
    }

    @Override
    protected void findViewById() {
        mRecyclerView = getView(R.id.recyclerView);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mType = bundle.getInt(StringConstants.INTENT_PARAM_TYPE);
        }
    }

    @Override
    public void setListener() {

    }

    @Override
    public void processLogic() {
        EventBus.getDefault().register(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setPullRefreshAndLoadingMoreEnabled(true, true);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                if (mType == 0) {
                    requestAfterSale();
                } else {
                    requestRefundInfo();
                }

            }

            @Override
            public void onLoadMore() {
                if (mType == 0) {
                    pageNo = mRefundOrderGoodsAdapter.getItemCount() / mRecyclerView.loadSize + 1;
                    requestAfterSale();
                } else {
                    pageNo = mRefundDetailAdapter.getItemCount() / mRecyclerView.loadSize + 1;
                    requestRefundInfo();
                }

            }
        });

        mRefundOrderGoodsAdapter = new RefundOrderGoodsAdapter(mContext,mActivity, R.layout.adapter_ast_list_item);
        mRefundDetailAdapter = new RefundDetailAdapter(mContext, mActivity,R.layout.adapter_rd_list_item);
        if (mType == 0) {
            mRecyclerView.setAdapter(mRefundOrderGoodsAdapter);
        } else {
            mRecyclerView.setAdapter(mRefundDetailAdapter);
        }

        mRecyclerView.refresh();
    }

    public static Bundle newBundle(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(StringConstants.INTENT_PARAM_TYPE, type);
        return bundle;
    }

    public static AfterSaleTabFragment newInstance(int state) {
        AfterSaleTabFragment fragment = new AfterSaleTabFragment();
        fragment.setArguments(newBundle(state));
        return fragment;
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
        User user = BaseApplication.getInstance().getLoginUser();
        if (user == null) {
            return;
        }
        map.put(URLConstants.REQUEST_PARAM_UID, user.getData().getId());
        map.put(URLConstants.REQUEST_PARAM_PAGE, String.valueOf(pageNo));
        RequestManager.createRequest(URLConstants.REQUEST_REFUND_ORDER_GOODS_LIST, map,
                new OnMyActivityRequestListener<RefundOrderGoods>((BaseActivity) mActivity) {

                    @Override
                    public void onSuccess(RefundOrderGoods bean) {
                        LogUtil.e("========" + bean.getData().size());
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
        User user = BaseApplication.getInstance().getLoginUser();
        if (user == null) {
            return;
        }
        map.put(URLConstants.REQUEST_PARAM_UID, user.getData().getId());
        map.put(URLConstants.REQUEST_PARAM_PAGE, String.valueOf(pageNo));
        RequestManager.createRequest(URLConstants.REQUEST_REFUND_GOODS, map, new OnMyActivityRequestListener<RefundGoods>((BaseActivity) mActivity) {

            @Override
            public void onSuccess(RefundGoods bean) {
                mRecyclerView.handlerSuccessHasRefreshAndLoad(mRefundDetailAdapter, pageNo == 1, bean.getData());
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                super.onFailed(isError, code, message);
                mRecyclerView.handlerError(mRefundDetailAdapter);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void update(AfterSaleEvent event) {
        if (event.isChange()) {
            pageNo = 1;
            if (mType == 0) {
                requestAfterSale();
            } else {
                requestRefundInfo();
            }
        }
    }
}
