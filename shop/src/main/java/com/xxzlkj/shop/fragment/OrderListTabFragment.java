package com.xxzlkj.shop.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.shop.R;
import com.xxzlkj.shop.adapter.OrderListAdapter;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.model.OrderListBean;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.ViewPageFragment;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;

import java.util.HashMap;

/**
 * 描述:订单列表
 *
 * @author zhangrq
 *         2017/3/20 16:37
 */
public class OrderListTabFragment extends ViewPageFragment {

    private MyRecyclerView mRecyclerView;
    private OrderListAdapter adapter;
    private boolean isShop;
    private String state;
    private String store_id;
    private String uid;
    private CountDownTimer countDownTimer;
    private int pageNo;

    public static Bundle newBundle(String state) {
        Bundle bundle = new Bundle();
        bundle.putString("state", state);
        return bundle;
    }

    public static OrderListTabFragment newInstance(String state) {
        OrderListTabFragment fragment = new OrderListTabFragment();
        fragment.setArguments(newBundle(state));
        return fragment;
    }

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_tab_order_list, container, false);
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
//        store_id = UserUtils.getShopId(user, "1");
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

        adapter = new OrderListAdapter(mContext, (BaseActivity) mActivity, R.layout.item_order_list, state);
        mRecyclerView.setAdapter(adapter);
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
        RequestManager.createRequest(URLConstants.ORDER_URL, stringStringHashMap, new OnMyActivityRequestListener<OrderListBean>((BaseActivity) mActivity) {

            @Override
            public void onSuccess(OrderListBean bean) {

                // 假数据
//                List<OrderListBean.DataBean> list=new ArrayList<OrderListBean.DataBean>();
//                for (int i = 0; i < 100; i++) {
//                    list.add(new OrderListBean.DataBean());
//                }
//                bean.setData(list);
                mRecyclerView.handlerSuccessHasRefreshAndLoad(adapter, pageNo == 1, bean.getData());
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
//                super.onFailed(isError, code, message);
                mRecyclerView.handlerError(adapter, pageNo == 1);
            }
        });
    }
}
