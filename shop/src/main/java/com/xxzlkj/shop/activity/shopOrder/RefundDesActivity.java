package com.xxzlkj.shop.activity.shopOrder;

import android.content.Context;
import android.content.Intent;

import com.xxzlkj.shop.R;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;


/**
 * 退款详情
 *
 * @author zhangrq
 */
public class RefundDesActivity extends MyBaseActivity {
    private static final String ORDER_ID = "order_id";

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_refund_des);
    }

    @Override
    protected void findViewById() {
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("退款详情");
    }

    public static Intent newIntent(Context context, String orderId) {
        Intent intent = new Intent(context, RefundDesActivity.class);
        intent.putExtra(ORDER_ID, orderId);
        return intent;
    }
}
