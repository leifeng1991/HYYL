package com.xxzlkj.shop.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.shopOrder.OrderDesActivity;
import com.xxzlkj.shop.event.OrderListTabRefreshEvent;
import com.xxzlkj.shop.interfac.ShopLibraryInterface;
import com.xxzlkj.shop.model.OrderListBean;
import com.xxzlkj.shop.utils.OrderUtils;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.zrq.spanbuilder.Spans;

import org.greenrobot.eventbus.EventBus;


/**
 * 描述: 订单adapter
 *
 * @author zhangrq
 *         2016/12/12 11:06
 */

public class OrderListAdapter extends BaseAdapter<OrderListBean.DataBean> {
    public static final int BTN_STATE_CANCEL_ORDER = 0;// 取消订单
    public static final int BTN_STATE_GO_PAYMENT = 1;// 去付款
    public static final int BTN_STATE_CONFIRM_GOODS = 2;// 确认收货
    public static final int BTN_STATE_DELETE_ORDER = 3;// 删除订单

    private final BaseActivity mActivity;
    private final String listIndexState;

    public OrderListAdapter(Context context, BaseActivity mActivity, int itemId, String state) {
        super(context, itemId);
        this.mActivity = mActivity;
        this.listIndexState = state;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final OrderListBean.DataBean itemBean) {
        TextView tv_order_state = holder.getView(R.id.tv_order_state);
        RecyclerView recyclerView = holder.getView(R.id.recyclerView);
        TextView tv_order_info_des = holder.getView(R.id.tv_order_info_des);
        LinearLayout ll_but_all = holder.getView(R.id.ll_but_all);
        TextView tv_order_but1 = holder.getView(R.id.tv_order_but1);
        TextView tv_order_but2 = holder.getView(R.id.tv_order_but2);
        View view_line = holder.getView(R.id.view_line);

        final MyOrderListOnClickListener onClickListener = new MyOrderListOnClickListener(itemBean, listIndexState);
//        状态 1待付款 2,3待发货 4待收货 4已完成 5已取消 6已退款
        String state = itemBean.getState();
        tv_order_state.setTextColor(ContextCompat.getColor(mContext, R.color.orange_ff725c));

        if ("1".equals(state)) {
            // 待付款
            tv_order_state.setText("待付款");
            ll_but_all.setVisibility(View.VISIBLE);
            setBtnStateVisibility(tv_order_but1, onClickListener, false, "取消订单", BTN_STATE_CANCEL_ORDER);
            setBtnStateVisibility(tv_order_but2, onClickListener, true, "立即付款", BTN_STATE_GO_PAYMENT);

        } else if ("2".equals(state) || "3".equals(state)) {
            // 待发货
            if ("2".equals(itemBean.getIs_tui())) {
                // 2退款中：退款中
                tv_order_state.setText("退款中");
                // 按钮的状态设置
                ll_but_all.setVisibility(View.GONE);
            } else {
                // 待发货
                tv_order_state.setText("待发货");
                // 按钮的状态设置
                ll_but_all.setVisibility(View.GONE);
            }

        } else if ("4".equals(state)) {
            // 4已完成(判读)
//            完成状态里面的，已耗时时间判读：(finishtime-addtime) -(endtime -addtime)>0 超时，<0正常耗时;;;简化：finishtime-endtime>0 超时，<0正常耗时
//         state等于4时判断此值 等于0时为待收货 不为0时为已完成
            if ("0".equals(itemBean.getUidtime())) {
                // 0：待收货;
                tv_order_state.setText("待收货");
                // 按钮的状态设置
                ll_but_all.setVisibility(View.VISIBLE);
                tv_order_but1.setVisibility(View.GONE);
                setBtnStateVisibility(tv_order_but2, onClickListener, true, "确认收货", BTN_STATE_CONFIRM_GOODS);
            } else {
                //非0：已完成
                tv_order_state.setText("已完成");
                // 按钮的状态设置
                ll_but_all.setVisibility(View.VISIBLE);
                setBtnStateVisibility(tv_order_but1, onClickListener, false, "删除订单", BTN_STATE_DELETE_ORDER);
                tv_order_but2.setVisibility(View.GONE);
            }

        } else if ("5".equals(state)) {
            // 5已取消
            tv_order_state.setText("已取消");
            tv_order_state.setTextColor(ContextCompat.getColor(mContext, R.color.gray_7d7d7d));
            // 按钮的状态设置
            ll_but_all.setVisibility(View.VISIBLE);
            setBtnStateVisibility(tv_order_but1, onClickListener, false, "删除订单", BTN_STATE_DELETE_ORDER);
            tv_order_but2.setVisibility(View.GONE);
        } else if ("6".equals(state)) {
            // 6已退款
            tv_order_state.setText("已退款");
            ll_but_all.setVisibility(View.GONE);
        }

        // 通用的设置
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        OrderListAdapterAdapter adapter = new OrderListAdapterAdapter(mContext, R.layout.item_order_list_item);
        recyclerView.setAdapter(adapter);
        adapter.addList(itemBean.getGoods());
//        共计 2 件商品   合计：19.8（含运费¥0.00）
        tv_order_info_des.setText(Spans.builder()
                .text("共计 ")
                .text(itemBean.getCount()).size(16).color(ContextCompat.getColor(mContext, R.color.black_222833))
                .text("件商品   合计：")
                .text(itemBean.getPrice()).size(16).color(ContextCompat.getColor(mContext, R.color.black_222833))
                .text("（含运费¥0.00）")//  运费
                .build());

        view_line.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
        // 解决内部RecyclerView不能点击,后期优化解决方法
        adapter.setOnItemClickListener(new OnItemClickListener<OrderListBean.DataBean.GoodsBean>() {
            @Override
            public void onClick(int position, OrderListBean.DataBean.GoodsBean item) {
                mActivity.startActivity(OrderDesActivity.newIntent(mContext, onClickListener, itemBean.getId()));
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 传onClickListener为了刷新列表
                mActivity.startActivity(OrderDesActivity.newIntent(mContext, onClickListener, itemBean.getId()));
            }
        });
    }


    private void setBtnStateVisibility(TextView button, MyOrderListOnClickListener onClickListener, boolean isOrange, String text, int tag) {
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(onClickListener);
        button.setTag(tag);
        button.setText(text);
        button.setTextColor(isOrange ? ContextCompat.getColor(mContext, R.color.orange_ff725c) :
                ContextCompat.getColor(mContext, R.color.black_777777));
        button.setBackgroundResource(isOrange ? R.drawable.shape_rectangle_order_orange :
                R.drawable.shape_rectangle_order_gray);
    }

    public class MyOrderListOnClickListener implements View.OnClickListener {

        private final OrderListBean.DataBean itemBean;
        private final String listIndexState;

        MyOrderListOnClickListener(OrderListBean.DataBean itemBean, String listIndexState) {
            this.itemBean = itemBean;
            this.listIndexState = listIndexState;
        }

        @Override
        public void onClick(View v) {
            int btnState = (int) v.getTag();
            switch (btnState) {
                case BTN_STATE_CANCEL_ORDER:
                    // 取消订单
                    OrderUtils.submitCancelOrderHasDialog(mActivity, itemBean.getId(), new OrderUtils.OnSuccessListener() {
                        @Override
                        public void onSuccess() {
                            // 刷新列表
                            cancelOrderSuccess();
                        }
                    });
                    break;
                case BTN_STATE_GO_PAYMENT:
                    // 去付款，直接跳到选择付款方式页面
                    if (BaseApplication.getInstance() instanceof ShopLibraryInterface) {
                        // 让主项目处理
                        mActivity.startActivity(((ShopLibraryInterface) BaseApplication.getInstance()).getPayActivityIntent(mContext,
                                itemBean.getOrderid(), itemBean.getId(), 1, itemBean.getGroupon_team_id()));
                    }
                    break;
                case BTN_STATE_CONFIRM_GOODS:
                    // 确认收货
                    OrderUtils.submitFinishOrderHasDialog(mActivity, itemBean.getId(), new OrderUtils.OnSuccessListener() {
                        @Override
                        public void onSuccess() {
                            // 刷新列表
                            finishOrderSuccess();
                        }
                    });
                    break;
                case BTN_STATE_DELETE_ORDER:
                    // 删除订单
                    OrderUtils.submitDelOrderHasDialog(mActivity, itemBean.getId(), new OrderUtils.OnSuccessListener() {
                        @Override
                        public void onSuccess() {
                            // 刷新列表
                            delOrderSuccess();
                        }
                    });
                    break;
            }
        }

        /**
         * 取消订单
         */
        public void cancelOrderSuccess() {
            // 简单操作，复杂操作，看商家端
            EventBus.getDefault().postSticky(new OrderListTabRefreshEvent(0));//全部页面刷新
            EventBus.getDefault().postSticky(new OrderListTabRefreshEvent(NumberFormatUtils.toInt(listIndexState)));//自己页面刷新
        }

        /**
         * 删除订单
         */
        public void delOrderSuccess() {
            // 简单操作，复杂操作，看商家端
            EventBus.getDefault().postSticky(new OrderListTabRefreshEvent(0));//全部页面刷新
            EventBus.getDefault().postSticky(new OrderListTabRefreshEvent(NumberFormatUtils.toInt(listIndexState)));//自己页面刷新
        }

        /**
         * 确认收货
         */
        public void finishOrderSuccess() {
            // 简单操作，复杂操作，看商家端
            EventBus.getDefault().postSticky(new OrderListTabRefreshEvent(0));//全部页面刷新
            EventBus.getDefault().postSticky(new OrderListTabRefreshEvent(NumberFormatUtils.toInt(listIndexState)));//自己页面刷新
        }
    }

}


