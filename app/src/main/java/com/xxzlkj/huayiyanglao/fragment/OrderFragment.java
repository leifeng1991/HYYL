package com.xxzlkj.huayiyanglao.fragment;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.activity.foodorder.TakeOutOrderListActivity;
import com.xxzlkj.huayiyanglao.activity.found.FoundActivityListActivity;
import com.xxzlkj.huayiyanglao.activity.found.FoundDetailActivity;
import com.xxzlkj.huayiyanglao.activity.yiyangyiliao.YLOrderListActivity;
import com.xxzlkj.huayiyanglao.adapter.ParticipateActivitiesAdapter;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.OrderNumBean;
import com.xxzlkj.huayiyanglao.util.HYNetRequestUtil;
import com.xxzlkj.licallife.activity.localLife.LocalLifeListActivity;
import com.xxzlkj.shop.activity.shopOrder.AfterSaleListActivity;
import com.xxzlkj.shop.activity.shopOrder.OrderListActivity;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.ReuseViewFragment;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.SystemBarUtils;
import com.zrq.spanbuilder.Spans;

import java.util.HashMap;


/**
 * 描述：订单
 *
 * @author zhangrq
 *         2017/9/2 10:58
 */
public class OrderFragment extends ReuseViewFragment {
    private TextView mAllOrderTextView, mObligationTextView, mSendGoodsTextView, mWaitReceivingTextView,
            mRemainEvaluatedTextView, mServiceAllOrderTextView, mSubscribeTextView, mServiceRemainEvaluatedTextView,
            mDeliveryOrdersTextView, mShopOrderTextView;
    private RelativeLayout mHasActivityRelativeLayout, mParticipateActivitiesLayout;
    private TextView mNoActivityTextView, mTitleTextView, mTimeTextView, mBorwsingNumberTextView, mAddressTextView,
            mCollectNumberTextView, mApplyNumberTextView, mNoLoginTextView, mYyylObligationTextView,
            mYyylForServiceTextView, mYyylLostEfficacyTextView, mYyylDoneTextView;
    private NestedScrollView mNestedScrollView;
    private LinearLayout mShopOrderLayout, mServiceOrderLayout, mDeliveryOrderLayout, mYyylOrderLayout;
    private OrderNumBean.DataBean.ActivityBean activity;

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    protected void findViewById() {
        SystemBarUtils.addStatusBarTranslucentFlags(mActivity);
        SystemBarUtils.setStatusBarLightMode(mActivity, true);
        SystemBarUtils.setPaddingTopStatusBarHeight(mActivity, getView(R.id.titleBar));
        getView(R.id.iv_title_left).setVisibility(View.GONE);

        mNoLoginTextView = getView(R.id.id_no_login);
        mNestedScrollView = getView(R.id.id_scroll);

        mShopOrderLayout = getView(R.id.id_shop_order_layout);// 购物订单
        mServiceOrderLayout = getView(R.id.id_service_orders_layout);// 服务订单
        mDeliveryOrderLayout = getView(R.id.id_delivery_orders_layout);// 外卖订单

        mAllOrderTextView = getView(R.id.id_all_orders);// 购物订单-全部订单
        mObligationTextView = getView(R.id.id_obligation);// 购物订单-待付款
        mSendGoodsTextView = getView(R.id.id_send_goods);// 购物订单-待发货
        mWaitReceivingTextView = getView(R.id.id_wait_receiving);// 购物订单-待收货
        mRemainEvaluatedTextView = getView(R.id.id_remain_evaluated);// 购物订单-待评价

        mServiceAllOrderTextView = getView(R.id.id_service_all_orders);// 服务订单-全部订单
        mSubscribeTextView = getView(R.id.id_subscribe);// 服务订单-预约中
        mServiceRemainEvaluatedTextView = getView(R.id.id_service_remain_evaluated);// 服务订单-待评价

        mDeliveryOrdersTextView = getView(R.id.id_delivery_orders);// 外卖订单-外卖订单
        mShopOrderTextView = getView(R.id.id_shop_order);// 外卖订单-到店订单

        mNoActivityTextView = getView(R.id.id_no_participate_activities);// 没有活动
        mHasActivityRelativeLayout = getView(R.id.id_parent_layout);// 有活动
        mTitleTextView = getView(R.id.id_title);// 标题
        mTimeTextView = getView(R.id.id_time);// 时间
        mBorwsingNumberTextView = getView(R.id.id_browsing_number);// 浏览数量
        mAddressTextView = getView(R.id.id_address);// 地址
        mCollectNumberTextView = getView(R.id.id_collect_number);// 收藏数量
        mApplyNumberTextView = getView(R.id.id_apply_number);// 报名数量
        mParticipateActivitiesLayout = getView(R.id.id_participate_activities_layout);

        mYyylOrderLayout = getView(R.id.id_yyyl_order_layout);// 医养医疗布局
        mYyylObligationTextView = getView(R.id.id_yyyl_obligation);// 医养医疗-待付款
        mYyylForServiceTextView = getView(R.id.id_yyyl_for_service);// 医养医疗-待服务
        mYyylLostEfficacyTextView = getView(R.id.id_yyyl_lost_efficacy);// 医养医布-已失效
        mYyylDoneTextView = getView(R.id.id_yyyl_done);// 医养医疗-已完成
    }

    @Override
    public void setListener() {
        setOnClick(mAllOrderTextView, mObligationTextView, mSendGoodsTextView, mWaitReceivingTextView,
                mRemainEvaluatedTextView, mServiceAllOrderTextView, mSubscribeTextView, mServiceRemainEvaluatedTextView,
                mDeliveryOrdersTextView, mShopOrderTextView, mHasActivityRelativeLayout, mParticipateActivitiesLayout,
                mNoLoginTextView, mYyylObligationTextView, mYyylForServiceTextView, mYyylLostEfficacyTextView, mYyylDoneTextView);
    }

    @Override
    public void processLogic() {
        setTitleName("订单");

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        User loginUserDoLogin = ZhaoLinApplication.getInstance().getLoginUserDoLogin(getActivity());
        if (loginUserDoLogin == null)
            return;
        switch (v.getId()) {
            case R.id.id_all_orders:// 购物订单-全部订单
                startActivity(OrderListActivity.newIntent(mContext, 0));
                break;
            case R.id.id_obligation:// 购物订单-待付款
                startActivity(OrderListActivity.newIntent(mContext, 1));
                break;
            case R.id.id_send_goods:// 购物订单-待发货
                startActivity(OrderListActivity.newIntent(mContext, 2));
                break;
            case R.id.id_wait_receiving:// 购物订单-待收货
                startActivity(OrderListActivity.newIntent(mContext, 3));
                break;
            case R.id.id_remain_evaluated:// 购物订单-售后退款
                startActivity(AfterSaleListActivity.newIntent(mContext));
                break;

            case R.id.id_service_all_orders:// 服务订单-全部订单
                startActivity(LocalLifeListActivity.newIntent(mContext, 0));
                break;
            case R.id.id_subscribe:// 服务订单-待服务
                startActivity(LocalLifeListActivity.newIntent(mContext, 2));
                break;
            case R.id.id_service_remain_evaluated:// 服务订单-服务中
                startActivity(LocalLifeListActivity.newIntent(mContext, 3));
                break;

            case R.id.id_delivery_orders:// 外卖订单-外卖订单
                startActivity(TakeOutOrderListActivity.newIntent(mContext, 0));
                break;
            case R.id.id_shop_order:// 外卖订单-到店订单
                startActivity(TakeOutOrderListActivity.newIntent(mContext, 1));
                break;
            case R.id.id_parent_layout:// 活动详情
                if (activity != null)
                    startActivity(FoundDetailActivity.newIntent(mContext, activity.getId()));
                break;
            case R.id.id_participate_activities_layout:// 活动更多
                startActivity(FoundActivityListActivity.newIntent(mContext));
                break;
            case R.id.id_yyyl_obligation:// 医养医疗-待付款
                startActivity(YLOrderListActivity.newIntent(mContext, 0));
                break;
            case R.id.id_yyyl_for_service:// 医养医疗-待服务
                startActivity(YLOrderListActivity.newIntent(mContext, 1));
                break;
            case R.id.id_yyyl_lost_efficacy:// 医养医疗-已失效
                startActivity(YLOrderListActivity.newIntent(mContext, 2));
                break;
            case R.id.id_yyyl_done:// 医养医疗-已完成
                startActivity(YLOrderListActivity.newIntent(mContext, 3));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getOrderNum();
    }

    private void setNumber(TextView textView, String number, String title) {
        textView.setText(Spans.builder().text(number + "\n\n").text(title).color(0xff999999).build());
    }

    private void setOnClick(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    /**
     * 获取 活动列表
     */
    public void getOrderNum() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        // 用户id(必传)
        User loginUserDoLogin = ZhaoLinApplication.getInstance().getLoginUser();
        if (loginUserDoLogin == null) {
            mNoLoginTextView.setVisibility(View.VISIBLE);
            mNestedScrollView.setVisibility(View.GONE);
            return;
        } else {
            mNoLoginTextView.setVisibility(View.GONE);
            mNestedScrollView.setVisibility(View.VISIBLE);
        }
        stringStringHashMap.put(ZLConstants.Params.UID, loginUserDoLogin.getData().getId());
        RequestManager.createRequest(ZLURLConstants.ORDER_NUM_URL, stringStringHashMap,
                new OnMyActivityRequestListener<OrderNumBean>((BaseActivity) mActivity) {

                    @Override
                    public void onSuccess(OrderNumBean bean) {
                        OrderNumBean.DataBean data = bean.getData();
                        // 商城订单
                        OrderNumBean.DataBean.OrderBean order = data.getOrder();
                        if (order != null) {
                            mShopOrderLayout.setVisibility(View.VISIBLE);
                            setNumber(mAllOrderTextView, order.getAll(), "全部订单");
                            setNumber(mObligationTextView, order.getPay(), "待付款");
                            setNumber(mSendGoodsTextView, order.getSend(), "待发货");
                            setNumber(mWaitReceivingTextView, order.getFinish(), "待收货");
                            setNumber(mRemainEvaluatedTextView, order.getTui(), "售后退款");
                        } else {
                            mShopOrderLayout.setVisibility(View.GONE);
                        }
                        // 服务订单
                        OrderNumBean.DataBean.CleaningBean cleaning = data.getCleaning();
                        if (cleaning != null) {
                            mServiceOrderLayout.setVisibility(View.VISIBLE);
                            setNumber(mServiceAllOrderTextView, cleaning.getAll(), "全部订单");
                            setNumber(mSubscribeTextView, cleaning.getSend(), "待服务");
                            setNumber(mServiceRemainEvaluatedTextView, cleaning.getComment(), "服务中");
                        } else {
                            mServiceOrderLayout.setVisibility(View.GONE);
                        }
                        // 健康餐桌订单
                        OrderNumBean.DataBean.FoodsBean foods = data.getFoods();
                        if (foods != null) {
                            mDeliveryOrderLayout.setVisibility(View.VISIBLE);
                            setNumber(mDeliveryOrdersTextView, foods.getOuter(), "外卖订单");
                            setNumber(mShopOrderTextView, foods.getTo(), "到店订单");
                        } else {
                            mDeliveryOrderLayout.setVisibility(View.GONE);
                        }
                        // 活动
                        activity = data.getActivity();
                        if (activity != null && !TextUtils.isEmpty(activity.getId())) {
                            mNoActivityTextView.setVisibility(View.GONE);
                            mHasActivityRelativeLayout.setVisibility(View.VISIBLE);
                            mTitleTextView.setText(activity.getTitle());
                            mAddressTextView.setText(activity.getAddress());
                            if (!TextUtils.isEmpty(activity.getStarttime()))
                                mTimeTextView.setText(DateUtils.getMonthDay(NumberFormatUtils.toLong(activity.getStarttime()) * 1000));
                            mBorwsingNumberTextView.setText(activity.getPv());
                            mCollectNumberTextView.setText(activity.getCell());
                            mApplyNumberTextView.setText(String.format("已报名\n%s", activity.getCount()));
                        } else {
                            mNoActivityTextView.setVisibility(View.VISIBLE);
                            mHasActivityRelativeLayout.setVisibility(View.GONE);
                        }
                        // 医养医疗
                        OrderNumBean.DataBean.HealthBean health = data.getHealth();
                        if (health != null) {
                            mYyylOrderLayout.setVisibility(View.VISIBLE);
                            setNumber(mYyylObligationTextView, health.getPay(), "待付款");
                            setNumber(mYyylForServiceTextView, health.getSend(), "待服务");
                            setNumber(mYyylLostEfficacyTextView, health.getFailure(), "已失效");
                            setNumber(mYyylDoneTextView, health.getFinish(), "已完成");
                        } else {
                            mYyylOrderLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void handlerStart() {

                    }

                    @Override
                    public void handlerEnd() {

                    }
                });
    }
}
