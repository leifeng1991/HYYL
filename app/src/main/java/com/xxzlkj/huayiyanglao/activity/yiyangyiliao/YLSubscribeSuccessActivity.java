package com.xxzlkj.huayiyanglao.activity.yiyangyiliao;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.MainTabActivity;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.HealthOrderInfoBean;
import com.xxzlkj.huayiyanglao.weight.ShapeButton;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.ActivityUtils;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.zrq.spanbuilder.Spans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 医养医疗预约成功
 */
public class YLSubscribeSuccessActivity extends MyBaseActivity {
    private ImageView mPayTypeImageView;
    private TextView mPayTypeTextView, mPayMoneyTextView, mServiceItemsTextView, mSubscribeTimeTextView, mServiecePointTextView;
    private ShapeButton mBackShapeButton, mHomeShapeButton;
    private String mOrderId;
    private HealthOrderInfoBean.DataBean.GoodsBean goodsBean;

    /**
     * @param context 上下文 （必传）
     * @param orderId 订单id  （必传）
     */
    public static Intent newIntent(Context context, String orderId) {
        Intent intent = new Intent(context, YLSubscribeSuccessActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(StringConstants.INTENT_PARAM_ID, orderId);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_yyyl_subscribe_success);
    }

    @Override
    protected void findViewById() {
        mPayTypeImageView = getView(R.id.id_pay_type_image);// 支付方式logo
        mPayTypeTextView = getView(R.id.id_pay_type);// 支付方式
        mPayMoneyTextView = getView(R.id.id_pay_money);// 付款金额
        mServiceItemsTextView = getView(R.id.id_service_items);// 服务项目
        mSubscribeTimeTextView = getView(R.id.id_subscribe_time);// 预约时间
        mServiecePointTextView = getView(R.id.id_service_point);// 服务点
        mBackShapeButton = getView(R.id.id_back);// 返回
        mHomeShapeButton = getView(R.id.id_home);// 首页
    }

    @Override
    protected void setListener() {
        // 返回
        mBackShapeButton.setOnClickListener(v -> {
            ActivityUtils.jumpToActivity(YLSubscribeSuccessActivity.this, YLDetailActivity.newIntent(mContext, goodsBean.getId()));
            finish();
        });
        // 首页
        mHomeShapeButton.setOnClickListener(v -> ActivityUtils.finishToActivity(YLSubscribeSuccessActivity.this, MainTabActivity.class));
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("预约成功");
        mOrderId = getIntent().getStringExtra(StringConstants.INTENT_PARAM_ID);
        getOrderInfo();
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
                HealthOrderInfoBean.DataBean data = bean.getData();
                List<HealthOrderInfoBean.DataBean.GoodsBean> goods = data.getGoods();
                if (goods != null && goods.size() > 0) {
                    goodsBean = goods.get(0);
                }
                setData(data);
            }
        });
    }

    private void setData(HealthOrderInfoBean.DataBean data) {
        // 支付方式 0未支付 1支付宝 2微信
        String payment = data.getPayment();
        switch (NumberFormatUtils.toInt(payment)) {
            case 0:// 未支付
                break;
            case 1:// 支付宝
                mPayTypeImageView.setImageResource(R.mipmap.pay_zhifubao);
                mPayTypeTextView.setText("支付宝支付");
                break;
            case 2:// 微信
                mPayTypeImageView.setImageResource(R.mipmap.pay_weixin);
                mPayTypeTextView.setText("微信支付");
                break;
        }
        mPayMoneyTextView.setText(Spans.builder().text("付款金额：").text("¥ " + data.getPrice()).color(0xffff725c).build());
        String service_project = data.getGoods().get(0).getTitle();
        mServiceItemsTextView.setText(Spans.builder().text("服务项目：").color(0xFF746F6E).text(TextUtils.isEmpty(service_project) ? "--" : service_project).build());
        String startTime = data.getService_starttime();
        mSubscribeTimeTextView.setText(Spans.builder().text("预约时间：").color(0xFF746F6E).
                text(TextUtils.isEmpty(startTime) ? "--" : DateUtils.getYearMonthDayHourMinute(NumberFormatUtils.toLong(startTime) * 1000)).build());
        String service_point = data.getAddress_address();
        mServiecePointTextView.setText(Spans.builder().text("服务点：").color(0xFF746F6E).text(TextUtils.isEmpty(service_point) ? "--" : service_point).build());
    }

}
