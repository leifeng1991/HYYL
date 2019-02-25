package com.xxzlkj.shop.activity.shop;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.shopOrder.OrderListActivity;
import com.xxzlkj.shop.adapter.RecommendGoodsAdapter;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.model.OrderDesBean;
import com.xxzlkj.shop.model.RecommendGoods;
import com.xxzlkj.shop.utils.ShopActivityUtils;
import com.xxzlkj.shop.weight.RecyclerViewDividerItemDecoration;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.zrq.spanbuilder.Spans;

import java.util.HashMap;
import java.util.Map;

/**
 * （订单详情）支付成功之后跳转到此界面
 */
public class PaySuccessOrderDetailActivity extends MyBaseActivity {
    public static final String ORDER_ID = "order_id";
    private ImageView mImageView;
    private ImageView mPayLogoImageView;
    private TextView mTitleTextView;
    private TextView mContentTextView;
    private TextView mReceiverTextView;
    private TextView mAdressTextView;
    private TextView mPayTypeTextView;
    private TextView mPriceTextView;
    private TextView mCheckOrderTextView;
    private TextView mBackHomeTextView;
    private TextView mTipTextView;
    private TextView mStateTipTextView;
    private RecyclerView mRecomendRecyclerView;
    // 订单id
    private String mOrderId;

    /**
     * @param context 上下文 （必传）
     * @param orderId 订单id (必传)
     * @return
     */
    public static Intent newIntent(Context context, String orderId) {
        Intent intent = new Intent(context, PaySuccessOrderDetailActivity.class);
        intent.putExtra(ORDER_ID, orderId);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_pay_success_order_detail);
    }

    @Override
    protected void findViewById() {
        mImageView = getView(R.id.id_psod_image);
        mPayLogoImageView = getView(R.id.id_psod_pay_logo);
        mTitleTextView = getView(R.id.id_psod_title);
        mContentTextView = getView(R.id.id_psod_content);
        mReceiverTextView = getView(R.id.id_psod_receiver);
        mAdressTextView = getView(R.id.id_psod_adresss);
        mPayTypeTextView = getView(R.id.id_psod_pay_type);
        mPriceTextView = getView(R.id.id_psod_pay_price);
        mCheckOrderTextView = getView(R.id.id_psod_check_order);
        mBackHomeTextView = getView(R.id.id_psod_back_home);
        mTipTextView = getView(R.id.id_psod_tip);
        mStateTipTextView = getView(R.id.id_psod_state_tip);
        // 订单id
        mOrderId = getIntent().getStringExtra(ORDER_ID);
        // 推荐商品
        mRecomendRecyclerView = getView(R.id.id_sc_recommend_list);
        mRecomendRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        // 禁止RecyclerView滑动解决RecyclerView不显示问题
        mRecomendRecyclerView.setNestedScrollingEnabled(false);
        // 分割线
        RecyclerViewDividerItemDecoration dividerItemDecoration = new RecyclerViewDividerItemDecoration(mContext, LinearLayout.VERTICAL,2);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.shape_divide_3));
        mRecomendRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecomendRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void setListener() {
        mCheckOrderTextView.setOnClickListener(this);
        mBackHomeTextView.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("订单详情");
        checkPayRear();
        getNetData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.id_psod_check_order) {
            startActivity(OrderListActivity.newIntent(mContext, 0));
            finish();

        } else if (i == R.id.id_psod_back_home) {
            ShopActivityUtils.jumpToShopHomeActivity(this);

        }
    }

    /**
     * 获取数据
     */
    private void getNetData() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("id", mOrderId);
        RequestManager.createRequest(URLConstants.PAY_ORDER_INFO_URL, stringStringHashMap, new OnMyActivityRequestListener<OrderDesBean>(this) {

            @Override
            public void onSuccess(OrderDesBean bean) {
                setData(bean);
            }
        });
    }

    /**
     * 设置数据
     *
     * @param bean
     */
    private void setData(OrderDesBean bean) {
        OrderDesBean.DataBean data = bean.getData();
        mReceiverTextView.setText("收件人：" + data.getAddress_name() + "　　" + data.getAddress_phone());
        mAdressTextView.setText("收货地址：" + data.getAddress_address());
        mPriceTextView.setText(Spans.builder().text("应付金额：", 15, 0xff606367).text("￥" + data.getPrice(), 15, 0xffFF725C).build());
        // payment支付方式 0未支付 1支付宝 2微信 3银联 4钱包 5货到付款
        int intPayment = NumberFormatUtils.toInt(data.getPayment());
        String strPayment = "";
        int resIdPayment = -1;
        switch (intPayment) {
            case 0:// 未支付
                break;
            case 1:// 支付宝
                strPayment = "支付宝";
                resIdPayment = R.mipmap.pay_zhifubao;
                break;
            case 2:// 微信
                strPayment = "微信";
                resIdPayment = R.mipmap.pay_weixin;
                break;
            case 3:// 银联
                strPayment = "银联";
                resIdPayment = R.mipmap.pay_yinlian;
                break;
            case 4:// 钱包
                strPayment = "钱包";
                resIdPayment = R.mipmap.pay_wallet;
                break;
            case 5:// 货到付款
                strPayment = "货到付款";
                break;
        }

        if (!TextUtils.isEmpty(strPayment)) {
            mPayTypeTextView.setText(strPayment);
        }
        if (resIdPayment != -1) {
            mPayLogoImageView.setImageResource(resIdPayment);
        }
        // 状态 1待付款 2,3待发货 4待收货 4已完成 5已取消 6已退款
        int intSate = NumberFormatUtils.toInt(data.getState());
        switch (intSate) {
            case 1:// 待付款
                break;
            case 2:
            case 3:
                break;
        }

        getRecommendList(data.getStore_id());
    }

    /**
     * 获取推荐商品列表
     */
    private void getRecommendList(String storeId) {
        Map<String, String> map = new HashMap<>();
        String id = BaseApplication.getInstance().getLoginUser().getData().getId();
        if (TextUtils.isEmpty(id)) {
            id = "0";
        }
        if (TextUtils.isEmpty(GlobalParams.storeId)) {
            return;
        }
        // 会员id (必传)
        map.put(URLConstants.REQUEST_PARAM_UID, id);
        // 店铺id (必传)
        map.put(URLConstants.REQUEST_PARAM_STORE_ID, storeId);
        RequestManager.createRequest(URLConstants.REQUEST_RECOMMEND_GOODS, map,
                new OnMyActivityRequestListener<RecommendGoods>(this) {
                    @Override
                    public void onSuccess(RecommendGoods bean) {
                        RecommendGoodsAdapter mRecommendGoodsAdapter =
                                new RecommendGoodsAdapter(PaySuccessOrderDetailActivity.this, R.layout.adapter_recommend_item);
                        mRecommendGoodsAdapter.addList(bean.getData());
                        mRecomendRecyclerView.setAdapter(mRecommendGoodsAdapter);
                        mRecommendGoodsAdapter.setOnItemClickListener((position, item) -> startActivity(GoodsDetailActivity.newIntent(mContext, item.getId())));
                    }
                });
    }

    /**
     * 支付后检测
     */
    private void checkPayRear() {
        Map<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_ORDERID, mOrderId);
        RequestManager.createRequest(URLConstants.CHECK_PAY_REAR_URL, map,
                new OnMyActivityRequestListener<BaseBean>(this) {
                    @Override
                    public void onSuccess(BaseBean bean) {

                    }
                });
    }
}
