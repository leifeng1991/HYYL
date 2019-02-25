package com.xxzlkj.shop.activity.shopOrder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.adapter.OrderDesAdapter;
import com.xxzlkj.shop.adapter.OrderListAdapter;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.interfac.ShopLibraryInterface;
import com.xxzlkj.shop.model.OrderDesBean;
import com.xxzlkj.shop.utils.OrderUtils;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.CallPhoneUtils;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.StringUtil;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;
import com.xxzlkj.zhaolinshare.base.util.ZLUtils;

import java.util.HashMap;


/**
 * 订单详情
 *
 * @author zhangrq
 */
public class OrderDesActivity extends MyBaseActivity {

    public static final int BTN_STATE_CANCEL_ORDER = 0;// 取消订单
    public static final int BTN_STATE_GO_PAYMENT = 1;// 去付款
    public static final int BTN_STATE_GO_REFUND = 2;// 去退款
    public static final int BTN_STATE_REFUND_ING = 3;// 退款中
    //    public static final int BTN_STATE_CANCEL_APPLICATION = 4;// 撤销申请
    public static final int BTN_STATE_CONFIRM_GOODS = 5;// 确认收货
    public static final int BTN_STATE_DELETE_ORDER = 6;// 删除订单
    public static final int BTN_STATE_ALREADY_REFUND = 7;// 已退款
    private static final String ORDER_ID = "order_id";
    private RecyclerView mRecyclerView;
    private OrderDesAdapter adapter;
    private LinearLayout ll_state_des, ll_order_content, ll_order_des_bottom;
    private ImageView iv_state_des;
    private TextView tv_state_des, tv_order_content, tv_order_des_btn1, tv_order_des_btn2,
            tv_orderId, tv_addTime, tv_des_name_phone, tv_address_name, tv_des_phone, tv_address_phone, tv_address_address, tv_payment, tv_price, tv_ems, tv_favorable, tv_payment_money;
    private OrderDesBean.DataBean data;
    private String orderId;
    private static OrderListAdapter.MyOrderListOnClickListener onOrderListBtnClickListener;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_order_des);
    }

    @Override
    protected void findViewById() {
        // 头
        ll_state_des = getView(R.id.ll_state_des);
        iv_state_des = getView(R.id.iv_state_des);
        tv_state_des = getView(R.id.tv_state_des);
        // 订单、收货人信息
        tv_orderId = getView(R.id.tv_orderId);
        tv_addTime = getView(R.id.tv_addTime);
        tv_des_name_phone = getView(R.id.tv_des_name_phone);
        tv_des_phone = getView(R.id.tv_des_phone);

        tv_address_name = getView(R.id.tv_address_name);
        tv_address_phone = getView(R.id.tv_address_phone);
        tv_address_address = getView(R.id.tv_address_address);
        ll_order_content = getView(R.id.ll_order_content);
        tv_order_content = getView(R.id.tv_order_content);
        // 支付信息
        mRecyclerView = getView(R.id.recyclerView);
        tv_payment = getView(R.id.tv_payment);
        tv_price = getView(R.id.tv_price);
        tv_ems = getView(R.id.tv_ems);
        tv_favorable = getView(R.id.tv_favorable);// 优惠金额
        tv_payment_money = getView(R.id.tv_payment_money);
        // 底部按钮
        ll_order_des_bottom = getView(R.id.ll_order_des_bottom);
        tv_order_des_btn1 = getView(R.id.tv_order_des_btn1);
        tv_order_des_btn2 = getView(R.id.tv_order_des_btn2);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("订单详情");
        setTitleRightText("投诉");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        adapter = new OrderDesAdapter(mContext, R.layout.item_order_des);
        mRecyclerView.setAdapter(adapter);
        orderId = getIntent().getStringExtra(ORDER_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNetData();
    }

    public static Intent newIntent(Context context, OrderListAdapter.MyOrderListOnClickListener onClickListener, String orderId) {
        Intent intent = new Intent(context, OrderDesActivity.class);
        intent.putExtra(ORDER_ID, orderId);
        OrderDesActivity.onOrderListBtnClickListener = onClickListener;
        return intent;
    }

    public static Intent newIntent(Context context, String orderId) {
        Intent intent = new Intent(context, OrderDesActivity.class);
        intent.putExtra(ORDER_ID, orderId);
        return intent;
    }

    private void getNetData() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("id", orderId);
        RequestManager.createRequest(URLConstants.ORDER_DETAIL_URL, stringStringHashMap, new OnMyActivityRequestListener<OrderDesBean>(this) {

            @Override
            public void onSuccess(OrderDesBean bean) {
                setData(bean);
            }
        });
    }

    private void setData(OrderDesBean bean) {

        data = bean.getData();
//        状态 1待付款 2,3待发货 4待收货 4已完成 5已取消 6已退款

        String state = bean.getData().getState();
        ll_order_des_bottom.setVisibility(View.VISIBLE);
        if ("1".equals(state)) {
            // 待付款
            // 设置头部
            ll_state_des.setBackgroundResource(R.mipmap.bg_order_des_dfk);
            iv_state_des.setImageResource(R.mipmap.ic_order_des_dfk);
            tv_state_des.setText("等待买家付款");

            ll_order_des_bottom.setVisibility(View.VISIBLE);
            setBtnStateVisibility(tv_order_des_btn1, "取消订单", BTN_STATE_CANCEL_ORDER);
            setBtnStateVisibility(tv_order_des_btn2, "立即支付", BTN_STATE_GO_PAYMENT);
        } else if ("2".equals(state) || "3".equals(state)) {
            // 待发货
            // 设置头部
            ll_state_des.setBackgroundResource(R.mipmap.bg_order_des_dfh);
            iv_state_des.setImageResource(R.mipmap.ic_order_des_dfh);
            tv_state_des.setText("等待卖家发货");
//            payment	支付方式 0未支付 1支付宝 2微信 3银联 4钱包 5货到付款
            if ("5".equals(data.getPayment())) {
                // 5货到付款
                ll_order_des_bottom.setVisibility(View.VISIBLE);
                tv_order_des_btn1.setVisibility(View.GONE);
                setBtnStateVisibility(tv_order_des_btn2, "取消订单", BTN_STATE_CANCEL_ORDER);
            } else {
                // 线上支付
                if ("2".equals(data.getIs_tui())) {
                    // 2退款中：退款中
                    ll_order_des_bottom.setVisibility(View.VISIBLE);
                    setBtnStateVisibility(tv_order_des_btn1, "退款中", BTN_STATE_REFUND_ING);
                    tv_order_des_btn2.setVisibility(View.GONE);
//                setBtnStateVisibility(tv_order_des_btn2, "撤销申请", BTN_STATE_CANCEL_APPLICATION);
                } else {
                    // 待发货
                    ll_order_des_bottom.setVisibility(View.VISIBLE);
                    tv_order_des_btn1.setVisibility(View.GONE);
                    if ("2".equals(data.getIs_refund())) {
                        //  is_refund	等于2时 不可以申请退款
                        tv_order_des_btn2.setText("退款");
                        tv_order_des_btn2.setBackgroundResource(R.drawable.shape_rectangle_order_gray_button);
                        tv_order_des_btn2.setEnabled(false);
                    } else {
                        setBtnStateVisibility(tv_order_des_btn2, "退款", BTN_STATE_GO_REFUND);
                    }
                }
            }
        } else if ("4".equals(state)) {
            // 4已完成(判断)
//            state等于4时判断此值 等于0时为待收货 不为0时为已完成
            if ("0".equals(data.getUidtime())) {
                // 0：待收货;
                ll_state_des.setBackgroundResource(R.mipmap.bg_order_des_yfh);
                iv_state_des.setImageResource(R.mipmap.ic_order_des_dfh);
                tv_state_des.setText("卖家已发货");

                ll_order_des_bottom.setVisibility(View.VISIBLE);
                tv_order_des_btn1.setVisibility(View.GONE);
                setBtnStateVisibility(tv_order_des_btn2, "确认收货", BTN_STATE_CONFIRM_GOODS);
            } else {
                //非0：已完成
                // 设置头部
                ll_state_des.setBackgroundResource(R.mipmap.bg_order_des_jycg);
                iv_state_des.setImageResource(R.mipmap.ic_order_des_jycg);
                tv_state_des.setText("交易成功");

                ll_order_des_bottom.setVisibility(View.VISIBLE);
                tv_order_des_btn1.setVisibility(View.GONE);
                setBtnStateVisibility(tv_order_des_btn2, "删除订单", BTN_STATE_DELETE_ORDER);
            }

        } else if ("5".equals(state)) {
            // 5已取消
            ll_state_des.setBackgroundResource(R.mipmap.bg_order_des_yqx);
            iv_state_des.setImageResource(R.mipmap.ic_order_des_jygb);
            tv_state_des.setText("已取消");

            ll_order_des_bottom.setVisibility(View.VISIBLE);
            tv_order_des_btn1.setVisibility(View.GONE);
            setBtnStateVisibility(tv_order_des_btn2, "删除订单", BTN_STATE_DELETE_ORDER);
        } else if ("6".equals(state)) {
            // 6已退款
            ll_state_des.setBackgroundResource(R.mipmap.bg_order_des_dfh);
            iv_state_des.setImageResource(R.mipmap.ic_order_des_dfh);
            tv_state_des.setText("等待卖家发货");

            ll_order_des_bottom.setVisibility(View.VISIBLE);
            setBtnStateVisibility(tv_order_des_btn1, "已退款", BTN_STATE_ALREADY_REFUND);
            tv_order_des_btn2.setVisibility(View.GONE);

        }
        // 设置通用的配置
        // 订单、收货人信息
        TextViewUtils.setText(tv_orderId, "订单编号：", data.getId());
        TextViewUtils.setText(tv_addTime, "下单时间：", DateUtils.getYearMonthDayHourMinuteSeconds(NumberFormatUtils.toLong(data.getAddtime()) * 1000));
//        store_uid	配送人id  如果为0不显示配送信息 不为0显示下面4条
//        delivery 配送方式：1兆邻自营 2门店自提 等于1显示配送员信息 等于2 显示门店信息
        if ("1".equals(data.getDelivery())) {
            // 1兆邻自营  显示配送员信息
            if ("0".equals(data.getStore_uid())) {
                // 为0不显示配送信息
                tv_des_name_phone.setText("暂无配送信息");
                tv_des_phone.setVisibility(View.GONE);
            } else {
                // 显示下面4条
                TextViewUtils.setText(tv_des_name_phone, "配送人员：", data.getUsername(), "   ", data.getPhone());
                tv_des_phone.setVisibility(View.VISIBLE);
                ZLUtils.initCallPhoneTextView(tv_des_phone);// 添加打电话图案
                tv_des_phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CallPhoneUtils.callPhoneDialog(OrderDesActivity.this, data.getPhone());
                    }
                });
            }
        } else if ("2".equals(data.getDelivery())) {
            // 2门店自提  显示门店信息
            tv_des_name_phone.setText(String.format("门店自提 %s", data.getStore_address()));
            tv_des_phone.setVisibility(View.VISIBLE);
            ZLUtils.initCallPhoneTextView(tv_des_phone);// 添加打电话图案
            tv_des_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CallPhoneUtils.callPhoneDialog(OrderDesActivity.this, data.getStore_phone());
                }
            });
        }

        TextViewUtils.setText(tv_address_name, data.getAddress_name());
        TextViewUtils.setText(tv_address_phone, data.getAddress_phone());
        TextViewUtils.setText(tv_address_address, data.getAddress_address());

        if (TextUtils.isEmpty(data.getContent())) {
            // 不显示买家留言
            ll_order_content.setVisibility(View.GONE);
        } else {
            ll_order_content.setVisibility(View.VISIBLE);
            tv_order_content.setText(data.getContent());
        }
        // 设置列表展示
        adapter.clear();
        adapter.addList(data.getGoods());

        if (!TextUtils.isEmpty(data.getPayment())) {
            // 支付信息
            String paymentStr = null;
            switch (data.getPayment()) {
                case "0":
                    paymentStr = "未支付";
                    break;
                case "1":
                    paymentStr = "支付宝";
                    break;
                case "2":
                    paymentStr = "微信";
                    break;
                case "3":
                    paymentStr = "银联";
                    break;
                case "4":
                    paymentStr = "钱包";
                    break;
                case "5":
                    paymentStr = "货到付款";
                    break;
            }
            TextViewUtils.setText(tv_payment, paymentStr);//支付方式 0未支付 1支付宝 2微信 3银联 4钱包 5货到付款
        }


        TextViewUtils.setText(tv_price, "¥ ", data.getPrices());// 商品总额
        // 快递 //  差快递费用、优惠
//        if (NumberFormatUtils.toDouble(data.getEms()) == 0)
//            TextViewUtils.setText(tv_ems, "¥ ", data.getEms());
//        else
//            TextViewUtils.setText(tv_ems, "+ ¥ ", data.getEms());
        // 优惠
//        discount 是否为折扣订单，0为普通订单，1-9为1-9折，折扣订单
//        prices 折扣前价格
        double favorable = NumberFormatUtils.toDouble(data.getPrices()) - NumberFormatUtils.toDouble(data.getPrice());
        TextViewUtils.setText(tv_favorable, favorable == 0 ? "¥ " : "- ¥ ", StringUtil.saveTwoDecimal(String.valueOf(favorable)));

        TextViewUtils.setText(tv_payment_money, "¥ ", data.getPrice());// 实付价格

    }

    private void setBtnStateVisibility(TextView button, String text, int tag) {
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(this);
        button.setTag(tag);
        button.setText(text);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_title_left) {// 左上角按钮
            onBackPressed();
            return;
        }

        if (data == null) {
            ToastManager.showShortToast(mContext, "未获取到网络数据，请检查网络或稍后再试");
            return;
        }
        int i1 = v.getId();
        if (i1 == R.id.tv_title_right) {// 投诉
            startActivity(ApplyComplainActivity.newIntent(mContext, data.getId()));

        }

        if (v.getTag() == null)
            return;
        int btnState = (int) v.getTag();
        switch (btnState) {
            case BTN_STATE_CANCEL_ORDER:
                // 取消订单
                OrderUtils.submitCancelOrderHasDialog(this, data.getId(), new OrderUtils.OnSuccessListener() {
                    @Override
                    public void onSuccess() {
                        if (onOrderListBtnClickListener != null)
                            onOrderListBtnClickListener.cancelOrderSuccess();
                        getNetData();
                    }
                });
                break;
            case BTN_STATE_GO_PAYMENT:
                // 去付款
                if (BaseApplication.getInstance() instanceof ShopLibraryInterface) {
                    // 让主项目处理
                    startActivity(((ShopLibraryInterface) BaseApplication.getInstance()).getPayActivityIntent(mContext,
                            data.getOrderid(), data.getId(), 1, data.getGroupon_team_id()));
                }
                break;
            case BTN_STATE_GO_REFUND:
                // 退款(跳到申请退款)
                startActivity(ApplyRefundActivity.newIntent(mContext, data.getId(), data.getPrice(), false));
                break;
            case BTN_STATE_REFUND_ING:
                // 退款中(跳到退款详情)
                startActivity(OrderRefundDesActivity.newIntent(mContext, data.getId()));
                break;
//            case BTN_STATE_CANCEL_APPLICATION:
//                // 撤销申请
//                OrderUtils.submitCancelOrderRefundHasDialog(this, data.getId(), new OrderUtils.OnSuccessListener() {
//                    @Override
//                    public void onSuccess() {
//                        // 列表没有此按钮状态
//                        getNetData();
//                    }
//                });
//                break;
            case BTN_STATE_CONFIRM_GOODS:
                // 确认收货
                OrderUtils.submitFinishOrderHasDialog(this, data.getId(), new OrderUtils.OnSuccessListener() {
                    @Override
                    public void onSuccess() {
                        if (onOrderListBtnClickListener != null)
                            onOrderListBtnClickListener.finishOrderSuccess();
                        getNetData();
                    }
                });
                break;
            case BTN_STATE_DELETE_ORDER:
                // 删除订单
                OrderUtils.submitDelOrderHasDialog(this, data.getId(), new OrderUtils.OnSuccessListener() {
                    @Override
                    public void onSuccess() {
                        if (onOrderListBtnClickListener != null)
                            onOrderListBtnClickListener.delOrderSuccess();
                        getNetData();
                    }
                });
                break;
            case BTN_STATE_ALREADY_REFUND:
                // 已退款(跳到退款详情)
                startActivity(OrderRefundDesActivity.newIntent(mContext, data.getId()));
                break;
        }
    }
}
