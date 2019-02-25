package com.xxzlkj.huayiyanglao.activity.yiyangyiliao;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.HealthOrderInfoBean;
import com.xxzlkj.huayiyanglao.util.YLOrderDealUtil;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.StringUtil;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;
import com.zrq.spanbuilder.Spans;

import java.util.HashMap;
import java.util.Map;

import static com.xxzlkj.huayiyanglao.activity.PayActivity.ORDER_ID;


/**
 * 医养医疗订单详情
 *
 * @author zhangrq
 */
public class YLOrderDesActivity extends MyBaseActivity implements YLOrderDealUtil.OnNetRequestSuccessListener {
    private TextView mStateTextView, mOrderNumberTextView, mAddTimeTextView, mZhuTextView,
            mRefundTimeTextView, mUserNamePhoneTextView, mServicePointTextView, mNoteTextView,
            mTimeTextView, mTitleTextView, mReservationNumberTextView, mPriceTextView,
            mGoodsTotalPriceTextView, mDiscountsTextView, mTotalPriceTextView, mRefundTotalPriceTextView;
    private LinearLayout mNoteLayoutLinearLayout;
    private ImageView mImageView;
    private RelativeLayout mWarmTipRelativeLayout;
    private Button mButton1, mButton2;
    private String mOrderId;
    private HealthOrderInfoBean.DataBean data;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_yyyl_order_des);
    }

    @Override
    protected void findViewById() {
        mStateTextView = getView(R.id.id_state);// 订单状态
        mOrderNumberTextView = getView(R.id.id_order_number);// 订单编号
        mAddTimeTextView = getView(R.id.id_add_time);// 下单时间
        mZhuTextView = getView(R.id.id_zhu);// 注
        mRefundTimeTextView = getView(R.id.id_refund_time);// 退款时间
        mUserNamePhoneTextView = getView(R.id.id_user_name_phone);// 姓名+手机号
        mServicePointTextView = getView(R.id.id_service_point);// 服务点
        mNoteLayoutLinearLayout = getView(R.id.id_note_layout);// 备注信息布局
        mNoteTextView = getView(R.id.id_note);// 备注信息
        mTimeTextView = getView(R.id.id_time);// 服务时间
        mImageView = getView(R.id.id_image);// 产品图片
        mTitleTextView = getView(R.id.id_title);// 产品名称
        mReservationNumberTextView = getView(R.id.id_reservation_number);// 预约号
        mPriceTextView = getView(R.id.id_price);// 价格
        mGoodsTotalPriceTextView = getView(R.id.id_goods_total_price);// 商品总额
        mDiscountsTextView = getView(R.id.id_discounts);// 优惠
        mTotalPriceTextView = getView(R.id.id_total_price);// 实付金额
        mRefundTotalPriceTextView = getView(R.id.id_refund_total_price);// 退款金额
        mWarmTipRelativeLayout = getView(R.id.id_warm_tip_layout);// 提示
        mButton1 = getView(R.id.id_button1);// 按钮1
        mButton2 = getView(R.id.id_button2);// 按钮2
    }

    @Override
    protected void setListener() {
        // 按钮1点击事件
        mButton1.setOnClickListener(v -> dealLeftButtonClick());
        // 按钮2点击事件
        mButton2.setOnClickListener(v -> dealRightButtonClick());
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("订单详情");
        mOrderId = getIntent().getStringExtra(ORDER_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getOrderInfo();
    }

    public static Intent newIntent(Context context, String orderId) {
        Intent intent = new Intent(context, YLOrderDesActivity.class);
        intent.putExtra(ORDER_ID, orderId);
        return intent;
    }

    /**
     * 获取订单详情
     */
    private void getOrderInfo() {
        Map<String, String> map = new HashMap<>();
        // 订单id
        map.put(ZLConstants.Strings.ID, mOrderId);
        RequestManager.createRequest(ZLURLConstants.HEALTH_ORDER_INFO_URL, map, new OnMyActivityRequestListener<HealthOrderInfoBean>(this) {
            @Override
            public void onSuccess(HealthOrderInfoBean bean) {
                data = bean.getData();
                setData(data);
            }
        });
    }

    public void setData(HealthOrderInfoBean.DataBean data) {
        // 状态 1待付款 2待服务 3,4服务中 4已完成 5已取消 6已退款
        String state = data.getState();
        if ("1".equals(state)) {
            // 待付款
            mStateTextView.setText("待付款");
            setLeftRightButton("取消预约", "立即支付");
            mWarmTipRelativeLayout.setVisibility(View.VISIBLE);
        } else if ("2".equals(state)) {
            //待服务
            mStateTextView.setText("待服务");
            setLeftRightButton("取消预约", "");
            mWarmTipRelativeLayout.setVisibility(View.VISIBLE);
        } else if ("5".equals(state) || "6".equals(state)) {
            // 已失效
            mStateTextView.setText("已失效");
            setLeftRightButton("删除订单", "再次预约");
        } else {
            // 已完成
            mStateTextView.setText("已完成");
            setLeftRightButton("删除订单", "再次预约");
        }
        mOrderNumberTextView.setText(String.format("订单编号：%s", data.getOrderid()));
        mAddTimeTextView.setText(String.format("下单时间：%s", DateUtils.getYearMonthDayHourMinuteSeconds(NumberFormatUtils.toLong(data.getAddtime()) * 1000)));
        String cancel_desc = data.getCancel_desc();
        mZhuTextView.setVisibility(TextUtils.isEmpty(cancel_desc) ? View.GONE : View.VISIBLE);
        mZhuTextView.setText(TextUtils.isEmpty(cancel_desc) ? "" : cancel_desc);
        String tui_addtime = data.getTui_addtime();
        mRefundTimeTextView.setVisibility(TextUtils.isEmpty(tui_addtime) ? View.GONE : View.VISIBLE);
        if (!TextUtils.isEmpty(tui_addtime))
            mRefundTimeTextView.setText(String.format("退款时间：%s", DateUtils.getYearMonthDayHourMinuteSeconds(NumberFormatUtils.toLong(tui_addtime) * 1000)));

        mServicePointTextView.setText(String.format("服务点：%s", data.getAddress_address()));
        String content = data.getContent();
        mNoteLayoutLinearLayout.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);
        mNoteTextView.setText(content);
        mTimeTextView.setText(String.format("%s-%s", DateUtils.getYearMonthDay(
                NumberFormatUtils.toLong(data.getService_starttime()) * 1000, "yyyy.MM.dd HH:mm"),
                DateUtils.getYearMonthDay(NumberFormatUtils.toLong(data.getService_endtime()) * 1000, "HH:mm")));
        HealthOrderInfoBean.DataBean.GoodsBean goodsBean = data.getGoods().get(0);
        if (goodsBean != null) {
            // 店名+店电话
            mUserNamePhoneTextView.setText(String.format("%s       %s", data.getShop_title(), data.getShop_phone()));
            PicassoUtils.setImageBig(mContext, goodsBean.getSimg(), mImageView);
            mTitleTextView.setText(goodsBean.getTitle());
            String ordinal = data.getOrdinal();
            String res_type = data.getRes_type();
            mReservationNumberTextView.setVisibility("2".equals(res_type) ? View.VISIBLE : View.GONE);
            mReservationNumberTextView.setText(String.format("预约号：%s号", ordinal));
            mPriceTextView.setText(String.format("¥%s", StringUtil.saveTwoDecimal(goodsBean.getPrice())));
            mGoodsTotalPriceTextView.setText(String.format("¥%s", StringUtil.saveTwoDecimal(goodsBean.getPrice())));
        }
        // 0未支付 1支付宝 2微信
//        String payment = data.getPayment();
        mTotalPriceTextView.setText(Spans.builder().text("实际支付金额：").color(0xFF222833).size(15).text("¥" + StringUtil.saveTwoDecimal(data.getPrice())).build());
        String tui_ratio = data.getTui_ratio();
        mRefundTotalPriceTextView.setVisibility(TextUtils.isEmpty(tui_ratio) ? View.GONE : View.VISIBLE);
        if (!TextUtils.isEmpty(tui_ratio)) {
            mRefundTotalPriceTextView.setText(Spans.builder().text("100".equals(tui_ratio) ? "(全额退款)" : ("(退款" + tui_ratio + "%)")).color(0xFF222833).size(15).text("¥" + data.getTui_price()).build());
        }
    }

    private void setLeftRightButton(String leftText, String rightText) {
        mButton1.setVisibility(TextUtils.isEmpty(leftText) ? View.GONE : View.VISIBLE);
        mButton2.setVisibility(TextUtils.isEmpty(rightText) ? View.GONE : View.VISIBLE);
        mButton1.setText(leftText);
        mButton2.setText(rightText);
    }

    /**
     * 处理按钮1点击事件
     */
    private void dealLeftButtonClick() {
        if (data != null) {
            String leftText = mButton1.getText().toString();
            if ("取消预约".equals(leftText)) {
                if ("1".equals(data.getState())) {
                    // 直接展示取消弹框
                    YLOrderDealUtil.showCancelOrderDialog(YLOrderDesActivity.this, data.getId(),
                            data.getState(), false, "确认取消预约？", "", this);
                } else {
                    YLOrderDealUtil.healthOrderRefundPrice(YLOrderDesActivity.this, data.getId(), data.getState(), this);
                }
            } else if ("删除订单".equals(leftText)) {
                YLOrderDealUtil.showDeleteOrderDialog(YLOrderDesActivity.this, data.getId(), this);
            }
        } else {
            ToastManager.showShortToast(mContext, "网络请求出错");
        }
    }

    /**
     * 处理按钮2点击事件
     */
    private void dealRightButtonClick() {
        if (data != null) {
            String rightText = mButton2.getText().toString();
            String goods_id = data.getGoods().get(0).getGoods_id();
            if ("立即支付".equals(rightText)) {
                // 支付前检测
                YLOrderDealUtil.checkPay(this, data.getOrderid(), data.getId(), goods_id);
            } else if ("再次预约".equals(rightText)) {
                // 跳转到产品详情
                startActivity(YLDetailActivity.newIntent(mContext, goods_id));
            }
        } else {
            // 提示
            ToastManager.showShortToast(mContext, "网络请求出错");
        }
    }

    @Override
    public void onSuccess(boolean isDeleteOrder) {
        if (isDeleteOrder) {
            finish();
        } else {
            // 重新刷新订单详情
            getOrderInfo();
        }
    }
}
