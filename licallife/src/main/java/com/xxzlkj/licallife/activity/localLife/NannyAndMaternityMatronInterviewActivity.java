package com.xxzlkj.licallife.activity.localLife;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.interfac.LocalLifeLibraryInterface;
import com.xxzlkj.licallife.model.OrderAddParameterBean;
import com.xxzlkj.licallife.utils.ZLTimePickerView;
import com.xxzlkj.shop.activity.address.HarvestAddressActivity;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.event.InterviewEvent;
import com.xxzlkj.shop.model.Order;
import com.xxzlkj.shop.utils.ZLDateUtils;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.SystemBarUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.HashMap;


/**
 * 本地生活--保姆、月嫂--面试
 *
 * @author zhangrq
 */
public class NannyAndMaternityMatronInterviewActivity extends BaseActivity {
    public static final String PARAMETER_BEAN = "parameterBean";
    private boolean isNanny;
    private RadioGroup mInterviewTypeRadioGroup;
    private LinearLayout mAddressLinearLayout, mTimeLinearLayout;
    private TextView mTimeTextView, mBuyNowTextView, mAddressTextView, mPrePriceTextView, mOrderPriceTextView;
    private ImageView mRightArrowImageView;
    private OrderAddParameterBean parameterBean;
    // 面试时间（时间戳）
    private long auditionTime;
    // 1:到店面试 2：到家面试
    private String auditionType = "1";
    // 面试地址
    private String auditionAddress;
    private String addressId;

    /**
     * @param parameterBean 下单部分参数bean
     * @return
     */
    public static Intent newIntent(Context context, OrderAddParameterBean parameterBean) {
        Intent intent = new Intent(context, NannyAndMaternityMatronInterviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PARAMETER_BEAN, parameterBean);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_nanny_and_maternity_matron_interview);
        SystemBarUtils.setStatusBarTranslucent(this);
        SystemBarUtils.setStatusBarLightMode(this, true);
        SystemBarUtils.setPaddingTopStatusBarHeight(this, getView(R.id.titleBar));
        ZLDateUtils.initCurrentTime();
    }

    @Override
    protected void findViewById() {
        mInterviewTypeRadioGroup = getView(R.id.rg_interview);// 面试方式
        mAddressLinearLayout = getView(R.id.id_subscribe_address_layout);// 面试地址布局
        mAddressTextView = getView(R.id.id_subscribe_address);// 面试地址
        mRightArrowImageView = getView(R.id.id_right_arrow);// 面试地址右侧箭头
        mTimeLinearLayout = getView(R.id.id_subscribe_time_layout);// 面试时间布局
        mTimeTextView = getView(R.id.id_subscribe_time);// 面试时间
        mPrePriceTextView = getView(R.id.tv_pre_money);// 预付金额
        mOrderPriceTextView = getView(R.id.tv_order_money);// 订单金额
        mBuyNowTextView = getView(R.id.tv_subscribe);// 按钮立即预约
    }

    @Override
    protected void setListener() {
        EventBus.getDefault().register(this);
        mInterviewTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.rb_shop) {
                    auditionType = "1";
                    mAddressLinearLayout.setEnabled(false);
                    auditionAddress = parameterBean.getShopAddress();
                    mRightArrowImageView.setVisibility(View.GONE);

                } else if (checkedId == R.id.rb_home) {
                    auditionType = "2";
                    mAddressLinearLayout.setEnabled(true);
                    auditionAddress = "";
                    mRightArrowImageView.setVisibility(View.VISIBLE);

                }
                mAddressTextView.setText(auditionAddress);
            }
        });
        // 面试地址
        mAddressLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(HarvestAddressActivity.newIntent(mContext, 3, addressId));
            }
        });
        mAddressLinearLayout.setEnabled(false);
        // 面试时间
        mTimeLinearLayout.setOnClickListener(v -> {
            // 选择时间
            selectTime(ZLDateUtils.getCurrentTimeSeconds() * 1000);
        });

        // 按钮立即支付
        mBuyNowTextView.setOnClickListener(v -> {
            // 检查
            // 开始时间（时间戳）
            if (TextUtils.isEmpty(auditionAddress)) {
                ToastManager.showShortToast(mContext, "请选择面试地址");
                return;
            } else if (auditionTime == 0) {
                ToastManager.showShortToast(mContext, "请选择面试时间");
                return;
            }
            // 都成功，提交生成订单，后支付
            preOrderCheck();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZLDateUtils.cancelTimer();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 选择时间
     */
    private void selectTime(long startDate) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(startDate);
        startCalendar.set(startCalendar.get(Calendar.YEAR),
                startCalendar.get(Calendar.MONTH),
                startCalendar.get(Calendar.DAY_OF_MONTH), startCalendar.get(Calendar.HOUR_OF_DAY), startCalendar.get(Calendar.MINUTE));
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(NumberFormatUtils.toLong(parameterBean.getEndtime()) * 1000 - 24 * 60 * 60 * 1000);

        endCalendar.set(endCalendar.get(Calendar.YEAR),
                endCalendar.get(Calendar.MONTH),
                endCalendar.get(Calendar.DAY_OF_MONTH), endCalendar.get(Calendar.HOUR_OF_DAY) + 23, endCalendar.get(Calendar.MINUTE) + 59);
        ZLTimePickerView pvTime = new ZLTimePickerView.Builder(this, (date, v) -> {
            if (ZLDateUtils.getCurrentTimeSeconds() * 1000 < date.getTime()) {
                //选中事件回调
                long time = date.getTime();
                mTimeTextView.setText(DateUtils.getYearMonthDayHourMinute(time));
                auditionTime = time / 1000;
            } else {
                ToastManager.showShortToast(mContext, "选择时间无效，请重新选择");
            }


        }).setTitleText("请选择时间")
                .setTitleSize(15)
                .setTitleColor(0xffababab)
                .setBgColor(0xffc6c9cf)
                .setRangDate(startCalendar, endCalendar)
                .setTitleBgColor(0xfff5f5f5)
                .setType(TimePickerView.Type.MONTH_DAY_HOUR_MIN).build();
        pvTime.show();
    }

    @Override
    protected void processLogic() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            parameterBean = (OrderAddParameterBean) bundle.getSerializable(PARAMETER_BEAN);
            auditionAddress = parameterBean.getShopAddress();
            isNanny = parameterBean.isNanny();// 是否是保姆预约

            mAddressTextView.setText(auditionAddress);
            mPrePriceTextView.setText(String.format("预付金额：￥%s", parameterBean.getPrice()));
            mOrderPriceTextView.setText(String.format("订单金额：￥%s", parameterBean.getPrices()));
        }

        setTitleLeftBack();
        setTitleName(isNanny ? "保姆" : "月嫂");

    }

    /**
     * 下单前检测
     */
    private void preOrderCheck() {
        HashMap<String, String> map = new HashMap<>();
        User user = BaseApplication.getInstance().getLoginUserDoLogin(this);
        if (user == null) {
            return;
        }

        // 2:保洁师 3:月嫂 4:保姆 5:小时工
        map.put(URLConstants.REQUEST_PARAM_TYPE, isNanny ? "4" : "3");
        // 产品id
        map.put(URLConstants.REQUEST_PARAM_IDS, parameterBean.getIds());
        // 开始时间（时间戳）
        map.put(URLConstants.REQUEST_PARAM_ENDTIME, parameterBean.getEndtime());
        // 时长（小时）
        map.put(URLConstants.REQUEST_PARAM_NUM, parameterBean.getNum());
        // 类型3/4传值 1：月 2：天
        map.put("timetype", parameterBean.getTimetype());// 时间类型
        RequestManager.createRequest(URLConstants.JZ_ADD_ORDER_CHECK_URL,
                map, new OnMyActivityRequestListener<BaseBean>(this) {

                    @Override
                    public void onSuccess(BaseBean bean) {
                        summitOrder();
                    }

                    @Override
                    public void handlerStart() {

                    }
                });
    }

    /**
     * 提交订单
     */
    private void summitOrder() {
        HashMap<String, String> map = new HashMap<>();
        User user = BaseApplication.getInstance().getLoginUserDoLogin(this);
        if (user == null) {
            return;
        }

        if (TextUtils.isEmpty(GlobalParams.communityId)) {
            ToastManager.showMsgToast(mContext, getString(R.string.noLocalLifeServiceHint));
            return;
        }
        // 小区id
        map.put(URLConstants.REQUEST_PARAM_COMMUNITY_ID, GlobalParams.communityId);

        // 1保洁项目 2保洁师 3 月嫂 4保姆 5小时工
        map.put(URLConstants.REQUEST_PARAM_TYPE, isNanny ? "4" : "3");// true为保姆预约页面，false为月嫂预约页面
        // 下单人id
        map.put(URLConstants.REQUEST_PARAM_UID, user.getData().getId());
        // 产品id
        map.put(URLConstants.REQUEST_PARAM_IDS, parameterBean.getIds());
        // 开始时间（时间戳）
        map.put(URLConstants.REQUEST_PARAM_ENDTIME, parameterBean.getEndtime());
        // 时长（小时）
        // 月的话乘以2，因为后台用的半个月为单位，自己用的一个月为单位，所以要乘以2；天的不用
        map.put(URLConstants.REQUEST_PARAM_NUM, parameterBean.getNum());
        // 地址id（必传）
        map.put(URLConstants.REQUEST_PARAM_ADDRESS, parameterBean.getAddressId());
        // 实际支付金额（必传）
        map.put(URLConstants.REQUEST_PARAM_PRICE, parameterBean.getPrice());// 设置预约金额
        // 全部金额（必传）
        map.put("prices", parameterBean.getPrices());// 全部金额
        // 时间类型（必传） timetype 1 月 2天
        map.put("timetype", parameterBean.getTimetype());// 时间类型
        // 面试时间
        map.put("audition_time", String.valueOf(auditionTime));
        // 面试类型 1:到点 2：到家
        map.put("audition_type", auditionType);
        // 面试地址
        map.put("audition_address", auditionAddress);
        // 订单备注
        String mNoteStr = parameterBean.getContent();
        if (!TextUtils.isEmpty(mNoteStr))
            map.put(URLConstants.REQUEST_PARAM_CONTENT, mNoteStr);

        RequestManager.createRequest(URLConstants.JZ_ORDER_ADD, map, new OnMyActivityRequestListener<Order>(this) {
            @Override
            public void onSuccess(Order bean) {
                Order.DataBean data = bean.getData();
                // 跳到支付选择页面
                if (BaseApplication.getInstance() instanceof LocalLifeLibraryInterface) {
                    startActivity(((LocalLifeLibraryInterface) BaseApplication.getInstance()).getPayActivityIntent(mContext, data.getOrderid(), data.getTitle(), 2, ""));
                }
                finish();
            }

        });
    }


    /**
     * 更新面试地址
     */
    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshInterviewAddress(InterviewEvent event) {
        auditionAddress = event.getAddress();
        mAddressTextView.setText(event.getAddress());
        addressId = event.getAddressId();
    }
}
