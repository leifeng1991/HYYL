package com.xxzlkj.shop.activity.shopOrder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.adapter.RefundGoodsInfoAdapter;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.event.AfterSaleDetailEvent;
import com.xxzlkj.shop.model.RefundDetail;
import com.xxzlkj.shop.model.RefundGoods;
import com.xxzlkj.shop.weight.HeaderRecyclerView;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

/**
 * 售后详情
 */
public class AfterSalesDetailActivity extends MyBaseActivity {
    public static final String INTENT_PARAM_REFUNDGOODS = "intent_param_refundgoods";
    private String mAfterOrderId;
    private String mOrderId;
    //编号
    private TextView mNumberTextView;
    //状态
    private TextView mStateTextView;
    private HeaderRecyclerView mRecyclerView;
    private TextView mOrderDetailTextView;

    private RefundGoods.DataBean dataBean;

    public static Intent newIntent(Context context, RefundGoods.DataBean dataBean) {
        Intent intent = new Intent(context,AfterSalesDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(INTENT_PARAM_REFUNDGOODS,dataBean);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_after_sales_detail);
    }

    @SuppressLint("InflateParams")
    @Override
    protected void findViewById() {
        EventBus.getDefault().register(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            dataBean = (RefundGoods.DataBean) bundle.getSerializable(INTENT_PARAM_REFUNDGOODS);
            if (dataBean != null) {
                mAfterOrderId = dataBean.getId();
            }
        }

        View mHeaderLayout = LayoutInflater.from(this).inflate(R.layout.view_asd_header, null);
        mNumberTextView = (TextView) mHeaderLayout.findViewById(R.id.id_asd_number);
        mStateTextView = (TextView) mHeaderLayout.findViewById(R.id.id_asd_state);
        mOrderDetailTextView = (TextView) findViewById(R.id.id_asd_order_detail);
        mRecyclerView = getView(R.id.id_asd_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addHeaderView(mHeaderLayout);
    }

    @Override
    protected void setListener() {
        mOrderDetailTextView.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("售后详情");
        getRefundGoodsInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.id_asd_order_detail) {
            if (!TextUtils.isEmpty(mOrderId)) {
                startActivity(OrderDesActivity.newIntent(mContext, mOrderId));
            }

        }
    }

    /**
     * 获取售后订单详情
     */
    private void getRefundGoodsInfo(){
        Map<String,String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_ID,mAfterOrderId);
        RequestManager.createRequest(URLConstants.REQUEST_REFUND_GOODS_INFO, map,
                new OnMyActivityRequestListener<RefundDetail>(this) {
                    @Override
                    public void onSuccess(RefundDetail bean) {
                        RefundDetail.RefundDataBean refundDataBean = bean.getData().get(0);
                        mOrderId = refundDataBean.getOrderid();
                        LogUtil.e("--------------------"+refundDataBean.getId());
                        mNumberTextView.setText(String.format("售后编号：%s", refundDataBean.getId()));
                        RefundGoodsInfoAdapter mRefundGoodsInfoAdapter = new
                                RefundGoodsInfoAdapter(AfterSalesDetailActivity.this,AfterSalesDetailActivity.this,R.layout.adapter_rgi_list_item,dataBean);
                        mRefundGoodsInfoAdapter.addList(bean.getData());
                        mRecyclerView.setAdapter(mRefundGoodsInfoAdapter);
                        String strState = "";
                        switch (NumberFormatUtils.toInt(refundDataBean.getState())){
                            case 1://申请中
                                strState = "退款中";
                                break;
                            case 2://通过
                                strState = "退款完成";
                                break;
                            case 3://否定
                                strState = "退款取消";
                                break;
                            case 4://同意等待取货
                                strState = "退款中";
                                break;
                            case 5://用户取消
                                strState = "退款取消";
                                break;
                        }
                        mStateTextView.setText(strState);
                    }
                });
    }

    /**
     * 更新售后详情
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updataAfterSalesDel(AfterSaleDetailEvent event){
        if (event.isChange()){
            getRefundGoodsInfo();
        }
    }
}
