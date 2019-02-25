package com.xxzlkj.licallife.activity.localLife;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.model.JzRefundGoodsInfo;
import com.xxzlkj.shop.config.URLConstants;
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
 * 售后详情（本地生活）
 *
 */

public class LocalLifeRefundGoodsInfoActivity extends MyBaseActivity {

    private String orderId;
    private TextView tv_refund_header_money,  tv_header_ing_des, tv_refund_reason, tv_refund_id, tv_refund_time, tv_refund_des, tv_refund_money, tv_refund_but2, tv_refund_header_time, tv_refund_but1, tv_refund_name;
    private LinearLayout ll_header_success, ll_header_ing;
    private JzRefundGoodsInfo.DataBean data;
    private String state;
    private LinearLayout mCancelLinearLayout;
    private TextView mCancelYuanYinTextView;
    private TextView mCancelTimeTextView;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_local_life_refund_des);
    }

    @Override
    protected void findViewById() {
        // 取消头
        mCancelLinearLayout = getView(R.id.ll_header_cancel);
        mCancelYuanYinTextView = getView(R.id.tv_refund_cancel_yuanyin);
        mCancelTimeTextView = getView(R.id.tv_refund_cancel_time);
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
        setTitleName("售后详情");
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
        int i = v.getId();
        if (i == R.id.tv_refund_but1) {//修改退款申请,跳到申请退款页面
            if ("1".equals(state)) {// state为1时可操作
                startActivity(LocalLifeAddRefundActivity.newIntent(mContext, data.getId(), data.getPrice(), "", true));
            } else {// 不可操作
                ToastManager.showShortToast(mContext, "已进入退款流程，不可操作！");
            }


        } else if (i == R.id.tv_refund_but2) {//撤销退款申请
            if ("1".equals(state) || "4".equals(state)) {// state为1或者4时可操作
                // 重新获取网络刷新数据
                OrderUtils.submitLocalLifeCancelRefundGoodsHasDialog(this, data.getId(), this::getNetData);
            } else {// 不可操作
                ToastManager.showShortToast(mContext, "已进入退款流程，不可操作！");
            }

        }
    }

    public static Intent newIntent(Context context, String orderid) {
        Intent intent = new Intent(context, LocalLifeRefundGoodsInfoActivity.class);
        intent.putExtra("orderId", orderid);
        return intent;
    }

    public void getNetData() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
//        id	订单id(必传)
        stringStringHashMap.put("id", orderId);
        RequestManager.createRequest(URLConstants.JZ_REFUND_GOODS_INFO, stringStringHashMap, new OnMyActivityRequestListener<JzRefundGoodsInfo>(this) {

            @Override
            public void onSuccess(JzRefundGoodsInfo bean) {
                LocalLifeRefundGoodsInfoActivity.this.data = bean.getData();
                setData(bean);
            }
        });
    }

    private void setData(JzRefundGoodsInfo bean) {
//        状态 1申请中 2已通过 3商家拒绝退款 5取消退款 为1时 有修改 和 取消
//        status 申请中时判断，等于1时为可修改状态，2为不可修改
        JzRefundGoodsInfo.DataBean data = bean.getData();
        state = data.getState();
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
        }else if ("5".equals(state)){
            mCancelLinearLayout.setVisibility(View.VISIBLE);
            ll_header_ing.setVisibility(View.GONE);
            mCancelYuanYinTextView.setText(getItemSpans("关闭原因：",data.getDesc()));
            mCancelTimeTextView.setText(getItemSpans("关闭时间：",DateUtils.getYearMonthDayHourMinute(NumberFormatUtils.toLong(data.getStatetime()) * 1000)));
        }
        // 内容
        tv_refund_name.setText(getItemSpans("店铺名称：", data.getShop_title()));
        tv_refund_money.setText(getItemSpans("退款金额：", "¥" + data.getPrice()));
        tv_refund_des.setText(getItemSpans("退款原因：", data.getTitle()));
        tv_refund_reason.setText(getItemSpans("退款说明：", data.getDesc()));

        tv_refund_id.setText(getItemSpans("售后编号：", data.getId()));
        tv_refund_time.setText(getItemSpans("申请时间：", DateUtils.getYearMonthDayHourMinute(NumberFormatUtils.toLong(data.getAddtime()) * 1000)));
    }

    private CharSequence getItemSpans(String titleName, String value) {
        if (TextUtils.isEmpty(value))
            value = "";
        return Spans.builder()
                .text(titleName + " ").color(ContextCompat.getColor(mContext, R.color.black_746f6e))
                .text(value + "").color(ContextCompat.getColor(mContext, R.color.black_444444))
                .build();
    }


}
