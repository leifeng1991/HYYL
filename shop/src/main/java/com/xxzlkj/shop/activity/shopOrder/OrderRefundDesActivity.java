package com.xxzlkj.shop.activity.shopOrder;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.model.OrderRefundBean;
import com.xxzlkj.shop.utils.OrderUtils;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;
import com.zrq.spanbuilder.Spans;

import java.util.HashMap;


/**
 * 描述:订单退款详情
 *
 * @author zhangrq
 *         2017/3/25 13:34
 */

@SuppressWarnings("ALL")
public class OrderRefundDesActivity extends MyBaseActivity {

    private String orderId;
    private TextView tv_refund_header_money, tv_refund_type, tv_header_ing_des, tv_refund_reason, tv_refund_id, tv_refund_time, tv_refund_des, tv_refund_money, tv_refund_but2, tv_refund_header_time, tv_refund_but1, tv_refund_name;
    private LinearLayout ll_header_success, ll_header_ing;
    private OrderRefundBean.DataBean data;
    private String status;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_order_refund_des);
    }

    @Override
    protected void findViewById() {
        // 成功头
        ll_header_success = getView(R.id.ll_header_success);
        tv_refund_header_money = getView(R.id.tv_refund_header_money);
        tv_refund_header_time = getView(R.id.tv_refund_header_time);
        // 申请中头
        ll_header_ing = getView(R.id.ll_header_ing);
        tv_header_ing_des = getView(R.id.tv_header_ing_des);
        tv_refund_but1 = getView(R.id.tv_refund_but1);
        tv_refund_but2 = getView(R.id.tv_refund_but2);
        // 内容
        tv_refund_name = getView(R.id.tv_refund_name);
        tv_refund_type = getView(R.id.tv_refund_type);
        tv_refund_money = getView(R.id.tv_refund_money);
        tv_refund_reason = getView(R.id.tv_refund_reason);
        tv_refund_des = getView(R.id.tv_refund_des);
        tv_refund_id = getView(R.id.tv_refund_id);
        tv_refund_time = getView(R.id.tv_refund_time);

    }

    @Override
    protected void setListener() {
        tv_refund_but1.setOnClickListener(this);
        tv_refund_but2.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("退款详情");
        orderId = getIntent().getStringExtra("orderId");

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 获取网络数据
        getNetData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (data == null) {
            ToastManager.showShortToast(mContext, "获取网络数据错误，请稍后再试");
            return;
        }
        int id = v.getId();

        if (id == R.id.tv_refund_but1) {
            //修改退款申请,跳到申请退款页面
            //        status 申请中时判断，等于1时为可修改状态，2为不可修改
            if ("1".equals(status)) {
                //1 可修改状态
                startActivity(ApplyRefundActivity.newIntent(mContext, data.getOrderid(), data.getPrice(), true));
            } else if ("2".equals(status)) {
                //2 不可修改
                ToastManager.showShortToast(mContext, "已进入退款流程，不可操作！");
            }
        } else if (id == R.id.tv_refund_but2) {
            //撤销退款申请
            if ("1".equals(status)) {
                //1 可修改状态
                OrderUtils.submitCancelOrderRefundHasDialog(this, data.getOrderid(), new OrderUtils.OnSuccessListener() {
                    @Override
                    public void onSuccess() {
                        // 重新获取网络刷新数据
                        getNetData();
                    }
                });
            } else if ("2".equals(status)) {
                //2 不可修改
                ToastManager.showShortToast(mContext, "已进入退款流程，不可操作！");
            }
        }
    }

    public static Intent newIntent(Context context, String orderid) {
        Intent intent = new Intent(context, OrderRefundDesActivity.class);
        intent.putExtra("orderId", orderid);
        return intent;
    }

    public void getNetData() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
//        id	订单id(必传)
        stringStringHashMap.put("id", orderId);
        RequestManager.createRequest(URLConstants.ORDER_REFUND_INFO_URL, stringStringHashMap, new OnMyActivityRequestListener<OrderRefundBean>(this) {

            @Override
            public void onSuccess(OrderRefundBean bean) {
                OrderRefundDesActivity.this.data = bean.getData();
                setData(bean);
            }
        });
    }

    private void setData(OrderRefundBean bean) {
//        状态 1申请中 2已通过 3商家拒绝退款 5取消退款 为1时 有修改 和 取消
//        status 申请中时判断，等于1时为可修改状态，2为不可修改
        OrderRefundBean.DataBean data = bean.getData();
        String state = data.getState();
        status = data.getStatus();
        if ("1".equals(state)) {
            //1申请中
            ll_header_ing.setVisibility(View.VISIBLE);
            ll_header_success.setVisibility(View.GONE);
            tv_header_ing_des.setText("商家将在三天内联系您，协商退款事宜，请保持手机畅通");
        } else if ("2".equals(state)) {
            // 2通过
            // 2 退款成功
            ll_header_success.setVisibility(View.VISIBLE);
            ll_header_ing.setVisibility(View.GONE);
            tv_refund_header_money.setText(getItemSpans("退款金额：", "¥" + data.getPrice()));
            tv_refund_header_time.setText(getItemSpans("退款时间：", DateUtils.getYearMonthDayHourMinute(NumberFormatUtils.toLong(data.getStatetime()) * 1000)));
        }
        // 内容
//        tv_refund_name.setText(getItemSpans("店铺名称：", data.getStore_title()));
        tv_refund_name.setText(getItemSpans("店铺名称：", "兆邻自营"));
        String type = data.getType();
//        1订单退款 2商品售后
        String typeStr = "- -";
        if (TextUtils.equals("1", type)) {
            // 1订单退款
            typeStr = "仅退款";
        } else if (TextUtils.equals("2", type)) {
            // 2商品售后
            typeStr = "退货退款";
        }
//        tv_refund_type.setText(getItemSpans("退款类型：", typeStr));
        tv_refund_type.setText(getItemSpans("退款类型：", "仅退款"));
        tv_refund_money.setText(getItemSpans("退款金额：", "¥" + data.getPrice()));
        tv_refund_des.setText(getItemSpans("退款原因：", data.getTitle()));

        if (TextUtils.isEmpty(data.getContent())) {
            tv_refund_reason.setText("退款说明：");
        } else
            tv_refund_reason.setText(getItemSpans("退款说明：", data.getContent()));

        tv_refund_id.setText(getItemSpans("退款编号：", data.getId()));
        tv_refund_time.setText(getItemSpans("申请时间：", DateUtils.getYearMonthDayHourMinute(NumberFormatUtils.toLong(data.getAddtime()) * 1000)));
    }

    private CharSequence getItemSpans(String titleName, String value) {
        if (TextUtils.isEmpty(value)) {
            value = "- -";
        }
        return Spans.builder()
                .text(titleName + " ").color(ContextCompat.getColor(mContext, R.color.black_746f6e))
                .text(value + "").color(ContextCompat.getColor(mContext, R.color.black_444444))
                .build();
    }


}
