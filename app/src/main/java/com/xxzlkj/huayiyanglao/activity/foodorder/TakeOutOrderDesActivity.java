package com.xxzlkj.huayiyanglao.activity.foodorder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.activity.PayActivity;
import com.xxzlkj.huayiyanglao.adapter.FoodsOrderListAdapterAdapter;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.OrderDesBean;
import com.xxzlkj.huayiyanglao.util.HYDialogUtil;
import com.xxzlkj.huayiyanglao.util.HYNetRequestUtil;
import com.xxzlkj.huayiyanglao.weight.ShapeButton;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.CallPhoneUtils;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import java.util.HashMap;

import static com.xxzlkj.huayiyanglao.activity.PayActivity.ORDER_ID;


/**
 * 订单详情
 *
 * @author zhangrq
 */
public class TakeOutOrderDesActivity extends MyBaseActivity {
    private TextView mStateTextView, mStateTipTextView, mGoodsTotalPriceTextView, mFreightTextView,
            mDiscountsTextView, mTotalPriceTextView, mTimeTextView;
    private Button mLeftButton, mRightButton;
    private LinearLayout mListLinearLayout;
    private String orderId;
    private OrderDesBean.DataBean beanData;
    private FoodsOrderListAdapterAdapter mFoodsOrderListAdapterAdapter;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_take_out_order_des);
    }

    @Override
    protected void findViewById() {
        mStateTextView = getView(R.id.id_state);// 状态
        mStateTipTextView = getView(R.id.id_state_tip);// 状态提示
        mListLinearLayout = getView(R.id.id_list_layout);// 列表布局
        mTimeTextView = getView(R.id.id_time);// 配送时间
        RecyclerView mRecyclerView = getView(R.id.id_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mFoodsOrderListAdapterAdapter = new FoodsOrderListAdapterAdapter(mContext, R.layout.adapter_foods_item_order_list_item);
        mRecyclerView.setAdapter(mFoodsOrderListAdapterAdapter);
        mGoodsTotalPriceTextView = getView(R.id.id_goods_total_price);// 商品总额
        mFreightTextView = getView(R.id.id_freight);// 运费
        mDiscountsTextView = getView(R.id.id_discounts);// 优惠
        mTotalPriceTextView = getView(R.id.id_total_price);// 实际支付金额
        mLeftButton = getView(R.id.id_button1);// 底部第一个按钮
        mRightButton = getView(R.id.id_button2);// 底部第二个按钮
    }

    @Override
    protected void setListener() {
        // 底部第一个按钮点击事件
        mLeftButton.setOnClickListener(v -> {
            if (beanData != null)
                dealWithButtonClick(mLeftButton.getText().toString());
        });
        // 底部第二个按钮点击事件
        mRightButton.setOnClickListener(v -> {
            if (beanData != null)
                dealWithButtonClick(mRightButton.getText().toString());
        });
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("订单详情");
        setTitleRightText("投诉");
        orderId = getIntent().getStringExtra(ORDER_ID);
    }

    @Override
    public void onTitleRightClick(View view) {
        super.onTitleRightClick(view);
        // 跳转到投诉界面
        if (beanData != null)
            startActivity(TakeOutApplyComplainActivity.newIntent(mContext, beanData.getId()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFoodsOrderInfo();
    }

    public static Intent newIntent(Context context, String orderId) {
        Intent intent = new Intent(context, TakeOutOrderDesActivity.class);
        intent.putExtra(ORDER_ID, orderId);
        return intent;
    }

    /**
     * 订单详情
     */
    private void getFoodsOrderInfo() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        // 订单id(必传)
        stringStringHashMap.put(ZLConstants.Params.ID, orderId);
        RequestManager.createRequest(ZLURLConstants.FOODS_ORDER_INFO_URL, stringStringHashMap, new OnMyActivityRequestListener<OrderDesBean>(this) {

            @Override
            public void onSuccess(OrderDesBean bean) {
                beanData = bean.getData();
                setData(beanData);
            }
        });
    }

    /**
     * 设置数据
     */
    private void setData(OrderDesBean.DataBean data) {
        mFoodsOrderListAdapterAdapter.clearAndAddList(data.getGoods());
        mGoodsTotalPriceTextView.setText(String.format("￥%s", data.getPrice()));
        mTotalPriceTextView.setText(String.format("￥%s", data.getPrice()));
        mListLinearLayout.removeAllViews();
        mListLinearLayout.addView(getViewItem("订单编号：" + data.getOrderid(), "下单时间：" +
                DateUtils.getYearMonthDayHourMinuteSeconds(NumberFormatUtils.toLong(data.getAddtime()) * 1000), false));
        // 就餐方式 1外卖订单 2到店用餐 1的话就显示用户地址信息 2的话显示门店信息
        String delivery = data.getDelivery();
        //用餐开始时间戳 状态1,2,7不显示时间
        long startTime = NumberFormatUtils.toLong(data.getStarttime()) * 1000;
        //用餐截止时间戳 状态1,2,7不显示时间
        long stopTime = NumberFormatUtils.toLong(data.getStoptime()) * 1000;
        mTimeTextView.setText(String.format("%s-%s", DateUtils.getYearMonthDay(startTime,
                "M月d日 HH:mm"), DateUtils.getYearMonthDay(stopTime, "HH:mm")));

        // 1。3不显示 2申请退款中（替换掉state状态）
        String upmsg = data.getUpmsg();
        String downmsg = data.getDownmsg();
        String is_tui = data.getIs_tui();
        if ("2".equals(is_tui)) {
            setTextByState(upmsg, downmsg, "", "退款中");
            mListLinearLayout.addView(getViewItem("1".equals(delivery) ? "配送信息" : "核销编号：" + data.getConsume_code(), "1".equals(delivery) ? "订单尚未送出" : "到店就餐时，请厨师订单支付编号，便于买家核销", false));
        } else {
            // 状态 1待付款 2已取消 3、待结单 4已结单 5配送中 6退款中 7已退款 9已送达 10待就餐 11订单完成
            int state = NumberFormatUtils.toInt(data.getState(), 1);
            switch (state) {
                case 1:// 待付款
                    setTextByState(upmsg, downmsg, "取消订单", "立即支付");
                    break;
                case 2:// 已取消
                    setTextByState(upmsg, downmsg, "", "删除订单");
                    break;
                case 3:// 待接单
                case 4:// 待配送
                    setTextByState(upmsg, downmsg, "", "退款");
                    mListLinearLayout.addView(getViewItem("配送信息", "订单尚未送出", false));
                    break;
                case 5:// 配送中
                    setTextByState(upmsg, downmsg, "", "催单");
                    mListLinearLayout.addView(getViewItem("配送信息", data.getStore_title(), true));
                    break;
                case 7:// 已退款
                    setTextByState(upmsg, downmsg, "", "已退款");
                    mListLinearLayout.addView(getViewItem("1".equals(delivery) ? "配送信息" : "核销编号：" + data.getConsume_code(), "1".equals(delivery) ? "订单尚未送出" : "到店就餐时，请厨师订单支付编号，便于买家核销", "1".equals(delivery)));
                    break;
                case 9:// 已送达
                    setTextByState(upmsg, downmsg, "催单", "确认送达");
                    mListLinearLayout.addView(getViewItem("配送信息", data.getStore_title(), true));
                    break;
                case 10:// 待就餐
                    setTextByState(upmsg, downmsg, "", "退款");
                    mListLinearLayout.addView(getViewItem("核销编号：" + data.getConsume_code(), "到店就餐时，请厨师订单支付编号，便于买家核销", false));
                    break;
                case 11:// 订单完成
                    setTextByState(upmsg, downmsg, "删除订单", "");
                    mListLinearLayout.addView(getViewItem("1".equals(delivery) ? "配送信息" : "核销编号：" + data.getConsume_code(), "1".equals(delivery) ? data.getStore_title() : "到店就餐时，请厨师订单支付编号，便于买家核销", "1".equals(delivery)));
                    break;
            }
        }

        mListLinearLayout.addView(getViewItem("1".equals(delivery) ? data.getAddress_name() + "　　" +
                        data.getAddress_phone() : data.getStore_title() + "　　" + data.getStore_phone(),
                "1".equals(delivery) ? data.getAddress_address() : data.getStore_address(), false));
        // 订单备注
        String content = data.getContent();
        if (!TextUtils.isEmpty(content))
            mListLinearLayout.addView(getViewItem("备注信息", content, false));
    }

    private void setTextByState(String stateString, String stateTipString, String leftButtonText, String rightButtonText) {
        mStateTextView.setVisibility(!TextUtils.isEmpty(stateString) ? View.VISIBLE : View.GONE);
        mStateTextView.setText(stateString);
        mStateTipTextView.setVisibility(!TextUtils.isEmpty(stateTipString) ? View.VISIBLE : View.GONE);
        mStateTipTextView.setText(stateTipString);
        mLeftButton.setVisibility(!TextUtils.isEmpty(leftButtonText) ? View.VISIBLE : View.GONE);
        mLeftButton.setText(leftButtonText);
        mRightButton.setVisibility(!TextUtils.isEmpty(rightButtonText) ? View.VISIBLE : View.GONE);
        mRightButton.setText(rightButtonText);
    }

    private View getViewItem(String title, String content, boolean isVisible) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_order_des_item, null);
        TextView mTitleTextView = view.findViewById(R.id.id_title);
        mTitleTextView.setText(title);
        TextView mContentTextView = view.findViewById(R.id.id_content);
        mContentTextView.setText(content);
        ShapeButton mPhoneShapeButton = view.findViewById(R.id.id_phone);
        mPhoneShapeButton.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        if (isVisible)
            mPhoneShapeButton.setOnClickListener(v -> {
                if (beanData != null)
                    CallPhoneUtils.callPhoneDialog(TakeOutOrderDesActivity.this, beanData.getStore_phone());
            });
        return view;
    }

    private void dealWithButtonClick(String text) {
        if ("取消订单".equals(text)) {
            HYDialogUtil.submitCancelOrderHasDialog(this, beanData.getId(), this::getFoodsOrderInfo);
        } else if ("删除订单".equals(text)) {
            showDeleteOrderDialog();
        } else if ("催单".equals(text)) {
            // 1:已催单 0：为催单
            String is_urge = beanData.getIs_urge();
            if ("1".equals(is_urge)) {
                ToastManager.showMsgToast(mContext, "你已催单");
            } else {
                HYNetRequestUtil.foodsUrgeOrder(this, beanData.getId(), bean -> {
                    ToastManager.showMsgToast(mContext, "催单成功");
                    beanData.setIs_urge("1");
                });
            }
        } else if ("确认送达".equals(text)) {
            HYNetRequestUtil.foodsFinishOrder(this, beanData.getId(), bean -> {
                // 确认送达请求成功
                getFoodsOrderInfo();
            });
        } else if ("立即支付".equals(text)) {
            // 跳转到支付界面
            startActivity(PayActivity.newIntent(mContext, beanData.getOrderid(), "", 3, ""));
        } else if ("退款".equals(text)) {
            // 跳转到退款界面
            startActivity(TakeOutApplyRefundActivity.newIntent(mContext, beanData.getId(), beanData.getPrice(), false));
        }
    }

    private void showDeleteOrderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定要删除此订单？");
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton("确定", (dialog, which) -> {
            dialog.dismiss();
            HYNetRequestUtil.foodsDelOrder(TakeOutOrderDesActivity.this, beanData.getId(), bean -> {
                // 订单删除成功
                finish();
            });
        });
        builder.show();
    }
}
