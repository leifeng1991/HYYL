package com.xxzlkj.shop.activity.shopOrder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.xxzlkj.shop.R;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.event.AfterSaleDetailEvent;
import com.xxzlkj.shop.utils.MyDrawableUtils;
import com.xxzlkj.shop.weight.CustomPopupWindow;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;
import com.zrq.spanbuilder.Spans;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * 退货方式
 */
public class SalesReturnWayActivity extends MyBaseActivity {
    public static final String STORE_NAME = "store_name";
    public static final String ORDER_ID = "order_id";
    private RelativeLayout mFirstRelativeLayout;
    private RelativeLayout mSecondRelativeLayout;
    private RelativeLayout mTypeRelativeLayout1;
    private TextView mTypeRelativeLayout2;
    private TextView mTypeTextView;
    private TextView mTimeOrStoreTextView;
    private TextView mTip2;
    private EditText mInstructionEditText;
    private Button mSubmitButton;
    private String storeName;
    private String orderId;
    private String quMode = "1";
    private String time;

    public static Intent newIntent(Context context, String name, String orderId) {
        Intent intent = new Intent(context, SalesReturnWayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(STORE_NAME, name);
        bundle.putString(ORDER_ID, orderId);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_sales_return_way);
    }

    @Override
    protected void findViewById() {
        mFirstRelativeLayout = getView(R.id.id_as_apply_type_layout);
        mSecondRelativeLayout = getView(R.id.id_as_yy_layout);
        TextView mTip1 = getView(R.id.id_as_type_tip);
        mTip1.setText(Spans.builder().text("退货方式").text("*", 14, 0xffff725c).text("：").build());
        mTip2 = getView(R.id.id_as_yy_tip);
        mTypeTextView = getView(R.id.id_as_type);
        mTimeOrStoreTextView = getView(R.id.id_as_yy);
        mInstructionEditText = getView(R.id.id_sm_instructions);
        mSubmitButton = getView(R.id.btn_summit);
        MyDrawableUtils.setButtonShapeOrange(mContext, mSubmitButton);

        mTypeRelativeLayout1 = getView(R.id.id_srw_first_type);
        mTypeRelativeLayout2 = getView(R.id.id_srw_second_type);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            storeName = bundle.getString(STORE_NAME);
            orderId = bundle.getString(ORDER_ID);
        }
    }

    @Override
    protected void setListener() {
        mFirstRelativeLayout.setOnClickListener(this);
        mSecondRelativeLayout.setOnClickListener(this);
        mSubmitButton.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("退货方式");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.id_as_apply_type_layout) {
            List<String> mStringList = new ArrayList<>();
            mStringList.add("上门取件");
            mStringList.add("送至门店");
//                mStringList.add("寄快递");
            CustomPopupWindow mPopupWindow = new CustomPopupWindow(this, "请选择退货方式", new CustomPopupWindow.OnMyClickListener() {
                @Override
                public void sureClickListener(String item) {
                    mSecondRelativeLayout.setVisibility(View.VISIBLE);
                    mTypeTextView.setText(item);
                    if ("送至门店".equals(item)) {
                        quMode = "1";
                        mSecondRelativeLayout.setClickable(false);
                        mTip2.setText(Spans.builder().text("门店选择").text("*", 14, 0xffff725c).text("：").build());
                        mTypeRelativeLayout1.setVisibility(View.GONE);
                        mTypeRelativeLayout2.setVisibility(View.VISIBLE);
                        mTimeOrStoreTextView.setText(storeName);
                    } else {
                        quMode = "2";
                        mSecondRelativeLayout.setClickable(true);
                        mTip2.setText(Spans.builder().text("上门时间").text("*", 14, 0xffff725c).text("：").build());
                        mTypeRelativeLayout2.setVisibility(View.GONE);
                        mTypeRelativeLayout1.setVisibility(View.VISIBLE);
                        mTimeOrStoreTextView.setText("");
                        mTimeOrStoreTextView.setHint("请选择取货时间");
                    }
                }

                @Override
                public void cancelClickListener() {
                }
            }, mStringList);
            mPopupWindow.showAtLocationBottom(this, R.id.id_mso_main);

        } else if (i == R.id.id_as_yy_layout) {
            long startTime = System.currentTimeMillis();
            long endTime = startTime + 15 * 24 * 60 * 60 * 1000;

            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTimeInMillis(startTime);
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTimeInMillis(endTime);

            //时间选择器
            TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {//选中事件回调
                    LogUtil.e("date", date.getTime() + "");
                    time = String.valueOf(date.getTime() / 1000);
                    mTimeOrStoreTextView.setText(DateUtils.getYearMonthDayHourMinute(date.getTime()));
                }
            }).setTitleText("请选择时间")
                    .setTitleSize(15)
                    .setTitleColor(0xffababab)
                    .setRangDate(startCalendar, endCalendar)
                    .setTitleBgColor(0xfff5f5f5)
                    .setType(TimePickerView.Type.YEAR_MONTH_DAY_HOUR_MIN).build();
            pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
            pvTime.show();

        } else if (i == R.id.btn_summit) {
            summitQuMode();

        }
    }

    /**
     * 确认取货方式
     */
    private void summitQuMode() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put(URLConstants.REQUEST_PARAM_ID, orderId);
        User user = BaseApplication.getInstance().getLoginUser();
        if (user == null) {
            return;
        }
        stringStringHashMap.put(URLConstants.REQUEST_PARAM_UID, user.getData().getId());
        if (TextUtils.isEmpty(mTypeTextView.getText().toString().trim())) {
            ToastManager.showShortToast(mContext, "请选择退货方式");
            return;
        }
        stringStringHashMap.put(URLConstants.REQUEST_PARAM_QU_MODE, quMode);
        if ("2".equals(quMode)) {
            if (TextUtils.isEmpty(time)) {
                ToastManager.showShortToast(mContext, "请选择取件时间");
                return;
            }
            stringStringHashMap.put(URLConstants.REQUEST_PARAM_DOOR_TIME, time);
            String instuction = mInstructionEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(instuction)) {
                stringStringHashMap.put(URLConstants.REQUEST_PARAM_DOOT_CONTENT, instuction);
            }
        }
        RequestManager.createRequest(URLConstants.REQUEST_QU_MODE, stringStringHashMap, new OnMyActivityRequestListener<BaseBean>(this) {
            @Override
            public void onSuccess(BaseBean bean) {
                EventBus.getDefault().postSticky(new AfterSaleDetailEvent(true));
                finish();
            }
        });
    }
}
