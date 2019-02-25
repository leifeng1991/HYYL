package com.xxzlkj.huayiyanglao.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.activity.PayActivity;
import com.xxzlkj.huayiyanglao.activity.foodorder.TakeOutApplyRefundActivity;
import com.xxzlkj.huayiyanglao.activity.foodorder.TakeOutOrderDesActivity;
import com.xxzlkj.huayiyanglao.activity.foodorder.TakeOutOrderRefundDesActivity;
import com.xxzlkj.huayiyanglao.adapter.FoodsOrderListAdapter;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.OrderListBean;
import com.xxzlkj.huayiyanglao.util.HYDialogUtil;
import com.xxzlkj.huayiyanglao.util.HYNetRequestUtil;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.ViewPageFragment;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import java.util.HashMap;

/**
 * 描述:订单列表
 *
 * @author zhangrq
 *         2017/3/20 16:37
 */
public class OrderListTabFragment extends ViewPageFragment {
    public static final String STATE = "state";
    private MyRecyclerView mRecyclerView;
    private FoodsOrderListAdapter adapter;
    private String delivery;
    private int pageNo;

    /**
     * @param delivery 1 外卖订单 2到店用餐（必传）
     */
    public static Bundle newBundle(String delivery) {
        Bundle bundle = new Bundle();
        bundle.putString(STATE, delivery);
        return bundle;
    }

    /**
     * @param delivery 1 外卖订单 2到店用餐（必传）
     */
    public static OrderListTabFragment
    newInstance(String delivery) {
        OrderListTabFragment fragment = new OrderListTabFragment();
        fragment.setArguments(newBundle(delivery));
        return fragment;
    }

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_tab_order_list, container, false);
    }

    @Override
    protected void findViewById() {
        mRecyclerView = getView(R.id.recyclerView);
        // 初始化
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setPullRefreshAndLoadingMoreEnabled(true, false);
        // 0.外卖订单、1.到店订单
        delivery = getArguments().getString(STATE);
        adapter = new FoodsOrderListAdapter(mContext, (BaseActivity) mActivity, R.layout.adapter_foods_item_order_list, "1".equals(delivery));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void setListener() {
        // 刷新加载监听
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
        // item点击事件
        adapter.setOnItemClickListener((position, item) -> {
            if ("2".equals(item.getIs_tui()) || "7".equals(item.getState())){
                // 跳转到退款详情
                startActivity(TakeOutOrderRefundDesActivity.newIntent(mContext, item.getId()));
            }else {
                // 跳转到订单详情
                startActivity(TakeOutOrderDesActivity.newIntent(mContext, item.getId()));
            }

        });
        // adapter底部第一个按钮点击事件
        adapter.setOnLeftButtonListener(this::dealWithButtonClick);
        // adapter底部第二个按钮点击事件
        adapter.setOnRightButtonListener(this::dealWithButtonClick);
    }

    @Override
    public void processLogic() {

    }

    @Override
    public void refreshOnceData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mRecyclerView.refresh();
    }

    private void getNetData() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        // 会员id(必传)
        User loginUser = ZhaoLinApplication.getInstance().getLoginUser();
        if (loginUser == null) return;
        stringStringHashMap.put(ZLConstants.Params.UID, loginUser.getData().getId());
        // 1 外卖订单 2到店用餐（必传）
        stringStringHashMap.put(ZLConstants.Params.DELIVERY, delivery);
        // 分页 默认为1 一页20条
        stringStringHashMap.put(ZLConstants.Params.PAGE, pageNo + "");
        RequestManager.createRequest(ZLURLConstants.FOODS_ORDER_LIST_URL, stringStringHashMap, new OnMyActivityRequestListener<OrderListBean>((BaseActivity) mActivity) {

            @Override
            public void onSuccess(OrderListBean bean) {
                mRecyclerView.handlerSuccessHasRefreshAndLoad(adapter, pageNo == 1, bean.getData());
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                mRecyclerView.handlerError(adapter, pageNo == 1);
            }
        });
    }

    private void dealWithButtonClick(String text, int position, OrderListBean.DataBean itemBean) {
        if ("立即支付".equals(text)) {

            // 跳转到支付界面
            mActivity.startActivity(PayActivity.newIntent(mContext, itemBean.getOrderid(), "", 3, ""));
        } else if ("退款".equals(text)) {

            // 跳转到退款界面
            mActivity.startActivity(TakeOutApplyRefundActivity.newIntent(mContext, itemBean.getId(), itemBean.getPrice(), false));
        } else if ("催单".equals(text)) {
            // 1:已催单 0：为催单
            String is_urge = itemBean.getIs_urge();
            if ("1".equals(is_urge)) {
                ToastManager.showMsgToast(mContext, "你已催单");
            } else {
                HYNetRequestUtil.foodsUrgeOrder((BaseActivity) mActivity, itemBean.getId(), bean -> {
                    ToastManager.showMsgToast(mContext, "催单成功");
                    itemBean.setIs_urge("1");
                    adapter.changeItem(position, itemBean);
                });
            }

        } else if ("评价".equals(text)) {

        } else if ("追加评价".equals(text)) {

        } else if ("确认送达".equals(text)) {
            HYNetRequestUtil.foodsFinishOrder((BaseActivity) mActivity, itemBean.getId(), bean -> {
                // 确认送达请求成功
                itemBean.setState("11");
                adapter.changeItem(position, itemBean);
            });
        } else if ("删除订单".equals(text)) {
            showDeleteOrderDialog(position, itemBean);
        } else if ("取消订单".equals(text)) {
            HYDialogUtil.submitCancelOrderHasDialog((BaseActivity) mActivity, itemBean.getId(), () -> {
                itemBean.setState("2");
                adapter.changeItem(position, itemBean);
            });
        }
    }

    private void showDeleteOrderDialog(int position, OrderListBean.DataBean itemBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("确定要删除此订单？");
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton("确定", (dialog, which) -> {
            dialog.dismiss();
            HYNetRequestUtil.foodsDelOrder((BaseActivity) mActivity, itemBean.getId(), bean -> {
                // 订单删除成功
                adapter.removeItem(position);
            });
        });
        builder.show();
    }

}
