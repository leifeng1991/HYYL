package com.xxzlkj.huayiyanglao.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.activity.PayActivity;
import com.xxzlkj.huayiyanglao.activity.yiyangyiliao.YLDetailActivity;
import com.xxzlkj.huayiyanglao.activity.yiyangyiliao.YLOrderDesActivity;
import com.xxzlkj.huayiyanglao.adapter.YLOrderListAdapter;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.HealthOrderListBean;
import com.xxzlkj.huayiyanglao.model.HealthOrderRefundPriceBean;
import com.xxzlkj.huayiyanglao.util.YLOrderDealUtil;
import com.xxzlkj.huayiyanglao.util.ZLDialogUtil;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.ViewPageFragment;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;

import java.util.HashMap;

/**
 * 描述:医养医疗订单列表
 *
 * @author zhangrq
 *         2017/3/20 16:37
 */
public class YLOrderListTabFragment extends ViewPageFragment implements YLOrderDealUtil.OnNetRequestSuccessListener {
    public static final String STATE = "state";
    private MyRecyclerView mRecyclerView;
    private YLOrderListAdapter adapter;
    private int type;
    private int pageNo;
    private boolean isRefresh;
    private ViewPager mViewPager;
    private int position;// 当前ViewPager位置

    /**
     * @param type 1:待付款 2:待服务 5:已失效 4:已完成（必传）
     */
    public static YLOrderListTabFragment newInstance(int type, ViewPager mViewPager) {
        YLOrderListTabFragment fragment = new YLOrderListTabFragment();
        fragment.mViewPager = mViewPager;
        fragment.setArguments(newBundle(type));
        return fragment;
    }

    /**
     * @param type 1:待付款 2:待服务 5:已失效 4:已完成（必传）
     * @return
     */
    public static Bundle newBundle(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(STATE, type);
        return bundle;
    }

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_tab_order_list, container, false);
    }

    @Override
    protected void findViewById() {
        mRecyclerView = getView(R.id.recyclerView);
        init();
    }

    private void init() {
        // 初始化
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setPullRefreshAndLoadingMoreEnabled(true, false);
        // 0:待付款 1:待服务 2:已失效 3:已完成
        type = getArguments().getInt(STATE);
        adapter = new YLOrderListAdapter(mContext, R.layout.adapter_yyyl_order_list_item);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void setListener() {
        // 刷新加载监听
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                getHealthOrderList();
            }

            @Override
            public void onLoadMore() {
                pageNo = adapter.getItemCount() / mRecyclerView.loadSize + 1;
                getHealthOrderList();
            }
        });
        // 订单列表点击事件
        adapter.setOnItemClickListener((position, item) -> {
            // 跳转到商品详情
            startActivity(YLOrderDesActivity.newIntent(mContext, item.getId()));
        });
        // 左边按钮点击事件
        adapter.setOnLeftButtonListener((leftText, position, itemBean) -> {
            if ("取消预约".equals(leftText)) {
                if ("1".equals(itemBean.getState())) {
                    // 直接展示取消弹框
                    YLOrderDealUtil.showCancelOrderDialog(mActivity, itemBean.getId(),
                            itemBean.getState(), false, "确认取消预约？", "", this);
                } else {
                    YLOrderDealUtil.healthOrderRefundPrice(mActivity, itemBean.getId(), itemBean.getState(), this);
                }
            } else if ("删除订单".equals(leftText)) {
                YLOrderDealUtil.showDeleteOrderDialog(mActivity, itemBean.getId(), this);
            }

        });
        // 右边按钮点击事件
        adapter.setOnRightButtonListener((rightText, position, itemBean) -> {
            String goods_id = itemBean.getGoods().get(0).getGoods_id();
            if ("立即支付".equals(rightText)) {
                // 支付前检测
                YLOrderDealUtil.checkPay(mActivity, itemBean.getOrderid(), itemBean.getId(), goods_id);
            } else if ("再次预约".equals(rightText)) {
                // 跳转到产品详情
                startActivity(YLDetailActivity.newIntent(mContext, goods_id));
            }
        });

    }

    @Override
    public void processLogic() {
        switch (type) {
            case 1:// 1:待付款
                position = 0;
                break;
            case 2://  2:待服务
                position = 1;
                break;
            case 5:// 5:已失效
                position = 2;
                break;
            case 4:// 4:已完成
                position = 3;
                break;
        }
    }

    @Override
    public void refreshOnceData() {
        mRecyclerView.refresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isRefresh) {
            // 刷新
            mRecyclerView.refresh();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isRefresh = mViewPager.getCurrentItem() == position;
    }

    /**
     * 获取订单列表
     */
    private void getHealthOrderList() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        // 会员id(必传)
        User loginUser = ZhaoLinApplication.getInstance().getLoginUser();
        if (loginUser == null) return;
        stringStringHashMap.put(ZLConstants.Params.UID, loginUser.getData().getId());
        // 0或不传 全部订单 1待付款 2待服务 3服务中（必传）
        stringStringHashMap.put(ZLConstants.Params.STATE, type + "");
        // 分页 默认为1 一页20条
        stringStringHashMap.put(ZLConstants.Params.PAGE, pageNo + "");
        RequestManager.createRequest(ZLURLConstants.HEALTH_ORDER_LIST_URL, stringStringHashMap, new OnMyActivityRequestListener<HealthOrderListBean>((BaseActivity) mActivity) {

            @Override
            public void onSuccess(HealthOrderListBean bean) {
                mRecyclerView.handlerSuccessHasRefreshAndLoad(adapter, pageNo == 1, bean.getData());
                // 无论刷新是否成功都置为false
                isRefresh = false;
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                mRecyclerView.handlerError(adapter, pageNo == 1);
                // 无论刷新是否成功都置为false
                isRefresh = false;
            }
        });
    }

    @Override
    public void onSuccess(boolean isDeleteOrder) {
        mRecyclerView.refresh();
    }
}
