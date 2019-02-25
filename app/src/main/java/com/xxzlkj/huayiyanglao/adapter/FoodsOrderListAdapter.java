package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.activity.PayActivity;
import com.xxzlkj.huayiyanglao.activity.foodorder.TakeOutOrderDesActivity;
import com.xxzlkj.huayiyanglao.activity.foodorder.TakeOutOrderRefundDesActivity;
import com.xxzlkj.huayiyanglao.fragment.OrderListTabFragment;
import com.xxzlkj.huayiyanglao.model.OrderListBean;
import com.xxzlkj.huayiyanglao.weight.ShapeButton;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.zrq.spanbuilder.Spans;


/**
 * 描述: 订单adapter
 *
 * @author zhangrq
 *         2016/12/12 11:06
 */

public class FoodsOrderListAdapter extends BaseAdapter<OrderListBean.DataBean> {
    private final BaseActivity mActivity;
    private final boolean isHomeDelivery;
    private OnLeftButtonListener leftButtonListener;
    private OnRightButtonListener rightButtonListener;

    /**
     * @param context
     * @param mActivity
     * @param itemId
     * @param isHomeDelivery 是否是外卖到家true:外卖到家 false：到店食用（必传）
     */
    public FoodsOrderListAdapter(Context context, BaseActivity mActivity, int itemId, boolean isHomeDelivery) {
        super(context, itemId);
        this.mActivity = mActivity;
        this.isHomeDelivery = isHomeDelivery;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final OrderListBean.DataBean itemBean) {
        TextView tv_delivery_tip = holder.getView(R.id.tv_delivery_tip);// 配送时间
        TextView tv_delivery_time = holder.getView(R.id.tv_delivery_time);// 配送时间
        TextView tv_order_state = holder.getView(R.id.tv_order_state);// 当前状态
        RecyclerView recyclerView = holder.getView(R.id.recyclerView);// 商品布局
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        FoodsOrderListAdapterAdapter mFoodsOrderListAdapterAdapter = new FoodsOrderListAdapterAdapter(mContext, R.layout.adapter_foods_item_order_list_item);
        recyclerView.setAdapter(mFoodsOrderListAdapterAdapter);
        TextView tv_order_add_time = holder.getView(R.id.tv_order_add_time);// 添加订单的时间
        TextView tv_price = holder.getView(R.id.tv_price);// 订单金额

        LinearLayout ll_but_all = holder.getView(R.id.ll_but_all);// 底部按钮总布局
        ShapeButton tv_order_but1 = holder.getView(R.id.tv_order_but1);// 按钮1
        ShapeButton tv_order_but2 = holder.getView(R.id.tv_order_but2);// 按钮2
        View view_line = holder.getView(R.id.view_line);// 底部的线

        mFoodsOrderListAdapterAdapter.setOnItemClickListener(new OnItemClickListener<OrderListBean.DataBean.GoodsBean>() {
            @Override
            public void onClick(int position, OrderListBean.DataBean.GoodsBean item) {
                if ("2".equals(itemBean.getIs_tui()) || "7".equals(itemBean.getState())){
                    // 跳转到退款详情
                    mActivity.startActivity(TakeOutOrderRefundDesActivity.newIntent(mContext, itemBean.getId()));
                }else {
                    // 跳转到订单详情
                    mActivity.startActivity(TakeOutOrderDesActivity.newIntent(mContext, itemBean.getId()));
                }
            }
        });

        // 1。3不显示 2申请退款中（替换掉state状态）
        String is_tui = itemBean.getIs_tui();
        // 状态 1待付款 2已取消 3、4待配送 5配送中 6退款中 7已退款 9已送达 10待就餐 11订单完成
        int state = NumberFormatUtils.toInt(itemBean.getState(), 1);
        if ("2".equals(is_tui)) {
            setButtonTextByState(tv_order_state, ll_but_all, tv_order_but1, tv_order_but2,
                    "退款中", "", "", View.GONE);
        } else {
            switch (state) {
                case 1:// 待付款
                    setButtonTextByState(tv_order_state, ll_but_all, tv_order_but1, tv_order_but2,
                            "待付款", "取消订单", "立即支付", View.VISIBLE);
                    break;
                case 2:// 已取消
                    setButtonTextByState(tv_order_state, ll_but_all, tv_order_but1, tv_order_but2,
                            "已取消", "删除订单", "", View.VISIBLE);
                    break;
                case 3:// 待配送
                case 4:// 待配送
                    setButtonTextByState(tv_order_state, ll_but_all, tv_order_but1, tv_order_but2,
                            "待配送", "", "退款", View.VISIBLE);
                    break;
                case 5:// 配送中
                    setButtonTextByState(tv_order_state, ll_but_all, tv_order_but1, tv_order_but2,
                            "配送中", "", "催单", View.VISIBLE);
                    break;
                case 7:// 已退款
                    setButtonTextByState(tv_order_state, ll_but_all, tv_order_but1, tv_order_but2,
                            "已退款", "", "", View.GONE);
                    break;
                case 9:// 已送达
                    setButtonTextByState(tv_order_state, ll_but_all, tv_order_but1, tv_order_but2,
                            "已送达", "催单", "确认送达", View.VISIBLE);
                    break;
                case 10:// 待就餐
                    setButtonTextByState(tv_order_state, ll_but_all, tv_order_but1, tv_order_but2,
                            "待就餐", "", "退款", View.VISIBLE);
                    break;
                case 11:// 订单完成
                    setButtonTextByState(tv_order_state, ll_but_all, tv_order_but1, tv_order_but2,
                            "已完成", "删除订单", "", View.VISIBLE);
                    break;
            }
        }
        //用餐开始时间戳 状态1,5,6不显示时间
        long startTime = NumberFormatUtils.toLong(itemBean.getStarttime()) * 1000;
        //用餐截止时间戳 状态1,5,6不显示时间
        long stopTime = NumberFormatUtils.toLong(itemBean.getStoptime()) * 1000;
        tv_delivery_time.setText(String.format("%s-%s", DateUtils.getYearMonthDay(startTime,
                "M月d日 HH:mm"), DateUtils.getYearMonthDay(stopTime, "HH:mm")));
        tv_price.setText(Spans.builder().text("合计：").text(String.format("￥%s", itemBean.getPrice())).color(0xffFF6600).build());
        long addtime = NumberFormatUtils.toLong(itemBean.getAddtime()) * 1000;
        tv_order_add_time.setText(DateUtils.getYearMonthDayHourMinute(addtime));
        mFoodsOrderListAdapterAdapter.addList(itemBean.getGoods());
        // 左边按钮点击事件
        tv_order_but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leftButtonListener != null)
                    leftButtonListener.onLeftButtonClick(tv_order_but1.getText().toString(), position, itemBean);
            }
        });
        // 右边按钮点击事件
        tv_order_but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightButtonListener != null)
                    rightButtonListener.onRightButtonClick(tv_order_but2.getText().toString(), position, itemBean);
            }
        });
    }

    private void setButtonTextByState(TextView tv_order_state, LinearLayout ll_but_all, ShapeButton tv_order_but1, ShapeButton tv_order_but2, String stateString, String leftButtonString, String rightButtonString, int visible) {
        tv_order_state.setText(stateString);
        ll_but_all.setVisibility(visible);
        tv_order_but1.setVisibility((TextUtils.isEmpty(leftButtonString)) ? View.GONE : View.VISIBLE);
        tv_order_but2.setVisibility((TextUtils.isEmpty(rightButtonString)) ? View.GONE : View.VISIBLE);
        tv_order_but1.setText(leftButtonString);
        tv_order_but2.setText(rightButtonString);
    }

    public void setOnLeftButtonListener(OnLeftButtonListener leftButtonListener) {
        this.leftButtonListener = leftButtonListener;
    }

    public void setOnRightButtonListener(OnRightButtonListener rightButtonListener) {
        this.rightButtonListener = rightButtonListener;
    }

    public interface OnLeftButtonListener {
        void onLeftButtonClick(String leftText, int position, OrderListBean.DataBean itemBean);
    }

    public interface OnRightButtonListener {
        void onRightButtonClick(String rightText, int position, OrderListBean.DataBean itemBean);
    }


}


