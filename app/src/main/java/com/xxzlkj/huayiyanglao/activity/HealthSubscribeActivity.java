package com.xxzlkj.huayiyanglao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.activity.address.HarvestAddressActivity;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.AddressBean;
import com.xxzlkj.licallife.model.CleanerDetailBean;
import com.xxzlkj.licallife.model.ProjectDetail;
import com.xxzlkj.licallife.model.ScheduleBean;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.event.JzSubscribeEvent;
import com.xxzlkj.shop.model.Order;
import com.xxzlkj.shop.utils.ZLDateUtils;
import com.xxzlkj.shop.weight.CustomButton;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.StringUtil;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.xxzlkj.shop.config.StringConstants.INTENT_PARAM_PROJECTDETAIL_DATABEAN;
import static com.xxzlkj.shop.config.URLConstants.REQUEST_PARAM_ADDRESS;
import static com.xxzlkj.shop.config.URLConstants.REQUEST_PARAM_CONTENT;
import static com.xxzlkj.shop.config.URLConstants.REQUEST_PARAM_ENDTIME;
import static com.xxzlkj.shop.config.URLConstants.REQUEST_PARAM_IDS;
import static com.xxzlkj.shop.config.URLConstants.REQUEST_PARAM_NUM;
import static com.xxzlkj.shop.config.URLConstants.REQUEST_PARAM_PRICE;
import static com.xxzlkj.shop.config.URLConstants.REQUEST_PARAM_TYPE;
import static com.xxzlkj.shop.config.URLConstants.REQUEST_PARAM_UID;

/**
 * 单次/周期预约
 * create an instance of this fragment.
 */
public class HealthSubscribeActivity extends MyBaseActivity {
    public static final int REQUEST_CODE_SELECT_TIME = 123;
    public LinearLayout mParentLayout;
    private LinearLayout mServiceAddressLayout;
    private LinearLayout mServiceTimeLayout;
    private TextView mTipTextView;
    private TextView mAddressTextView;
    private TextView mTimeTextView;
    private TextView mUnitPriceTextView;
    private TextView mNumberTextView;
    private TextView mSubTotalPriceTextView;
    private TextView mTotalPriceTextView;
    private TextView mPhoneTextView;
    private TextView mCouponsTextView;
    private CustomButton mBuyNowTextView;
    private TextView mOrderTextView;
    private TextView mSubscribeTip3TextView;
    private TextView mSubscribeTip4TextView;
    private ImageView mReduceImageView;
    private ImageView mAddImageView;
    private EditText mNoteEditText;
    private LinearLayout mLinearLayout;
    private ImageView mArrowImageView;
    // 服务频次
    private LinearLayout mServiceFrequencyLayout;
    private TextView mServiceFrequencyTextView;
    // 服务周期
    private LinearLayout mServiceCycleLayout;
    private TextView mServiceCycleTextView;
    // 保洁产品/保洁师id
    private String id;
    // 保洁产品单价
    private String unitPrice;
    //数量
    private int number;
    //地址id
    private String addressId;
    //总价
    private double price;
    //时间戳
    private long time = 0;
    // 计量方式 1：数量 2：时间（时间是半个小时一个单位）
    private String unit;
    // 计量单位
    private String unitDesc;
    private ProjectDetail.DataBean mProjectDataBean;

    /**
     * @param id   产品id （必传）
     * @param data 产品数据 （必传）
     * @return
     */
    public static Intent newIntent(Context context, String id, ProjectDetail.DataBean data) {
        Intent intent = new Intent(context, HealthSubscribeActivity.class);
        Bundle args = new Bundle();
        args.putString(StringConstants.INTENT_PARAM_ID, id);
        args.putSerializable(INTENT_PARAM_PROJECTDETAIL_DATABEAN, data);
        intent.putExtras(args);
        return intent;
    }


    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_single_subscribe);
    }

    @Override
    protected void findViewById() {
        mParentLayout = getView(R.id.id_subscribe_main);
        mTipTextView = getView(R.id.id_tip);// 提示（只有保洁师有）
        mServiceAddressLayout = getView(R.id.id_subscribe_address_layout);// 服务地址布局
        mServiceTimeLayout = getView(R.id.id_subscribe_time_layout);// 服务时间布局
        mServiceFrequencyLayout = getView(R.id.id_service_frequency_layout);// 服务频次布局
        mServiceCycleLayout = getView(R.id.id_service_cycle_layout);// 服务周期布局
        mAddressTextView = getView(R.id.id_subscribe_address);// 服务地址
        mTimeTextView = getView(R.id.id_subscribe_time_time);// 服务时间
        mServiceFrequencyTextView = getView(R.id.id_service_frequency_time);// 服务频次
        mServiceCycleTextView = getView(R.id.id_service_cycle_time);// 服务周期
        mUnitPriceTextView = getView(R.id.id_subscribe_unit_price);// 单价
        mNumberTextView = getView(R.id.id_subscribe_number);// 数量
        mSubTotalPriceTextView = getView(R.id.id_subscribe_subtotal_price);// 小计
        mTotalPriceTextView = getView(R.id.id_subscribe_total_price);// 合计
        mPhoneTextView = getView(R.id.id_subscribe_phone);// 联系电话
        mCouponsTextView = getView(R.id.id_subscribe_coupons);// 抵用券
        mReduceImageView = getView(R.id.id_subscribe_reduce);// 减
        mAddImageView = getView(R.id.id_subscribe_add);// 加
        mNoteEditText = getView(R.id.id_subscribe_note);// 备注
        mBuyNowTextView = getView(R.id.id_subscribe_buy_now);// 立即支付
        mOrderTextView = getView(R.id.id_subscribe_order);// 生成订单
        mSubscribeTip3TextView = getView(R.id.id_subscribe_tip_3);// 服务项目
        mSubscribeTip4TextView = getView(R.id.id_subscribe_tip_4);

        mLinearLayout = getView(R.id.id_layout);
        // 右边箭头
        mArrowImageView = getView(R.id.id_right_arrow);
    }

    @Override
    public void setListener() {
        mServiceAddressLayout.setOnClickListener(this);
        mServiceTimeLayout.setOnClickListener(this);
        mServiceFrequencyLayout.setOnClickListener(this);
        mServiceCycleLayout.setOnClickListener(this);
        mReduceImageView.setOnClickListener(this);
        mAddImageView.setOnClickListener(this);
        mBuyNowTextView.setOnClickListener(this);
        mOrderTextView.setOnClickListener(this);
    }

    @Override
    public void processLogic() {
        ZLDateUtils.initCurrentTime();
        setTitleLeftBack();
        setTitleName("预约");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mServiceFrequencyLayout.setVisibility(View.GONE);
            mServiceCycleLayout.setVisibility(View.GONE);
            // 1:保洁项目
            id = bundle.getString(StringConstants.INTENT_PARAM_ID);
            mProjectDataBean = (ProjectDetail.DataBean) bundle.getSerializable(INTENT_PARAM_PROJECTDETAIL_DATABEAN);
            if (mProjectDataBean != null) {
                initProjectData();
                setPrice();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ZLDateUtils.cancelTimer();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.id_subscribe_address_layout:// 服务地址
                startActivityForResult(HarvestAddressActivity.newIntent(mContext, addressId), ZLConstants.Integers.REQUEST_CODE_ADDRESS);
                break;
            case R.id.id_subscribe_time_layout:// 服务时间
                // 1:保洁项目（数量：计件-->选开始时间; 时间：计时-->选时间段 ）
                String unit = mProjectDataBean.getUnit(); // 1.数量    2.时间
                startActivityForResult(HealthSelectServiceTimeActivity.newIntent(mContext, "1".equals(unit) ? "1" : "2", mProjectDataBean, number * 30, time), REQUEST_CODE_SELECT_TIME);
                break;
            case R.id.id_subscribe_reduce:// 减半个小时
                reduce();
                showToast();
                break;
            case R.id.id_subscribe_add:// 加半个小时
                addition();
                showToast();
                break;
            case R.id.id_subscribe_buy_now:// 立即下单
                checkValue();
                break;
            case R.id.id_subscribe_order:// 生成订单
                break;
            case R.id.id_service_frequency_layout:// 服务频次
                showServiceFrequencyDialog();
                break;
            case R.id.id_service_cycle_layout:// 服务周期
                showServiceCycleDialog();
                break;
            case R.id.id_layout:// 服务项目
                break;
        }
    }

    /**
     * 设置初始化数据（保洁项目）
     */
    private void initProjectData() {
        unitPrice = mProjectDataBean.getPrice();
        unitDesc = mProjectDataBean.getUnit_desc();
        unit = mProjectDataBean.getUnit();
        if (!TextUtils.isEmpty(unit)) {
            switch (unit) {
                case "1":// 数量
                    number = NumberFormatUtils.toInt(mProjectDataBean.getBase(), 1);
                    mSubscribeTip3TextView.setText(mProjectDataBean.getTitle() + "(" + unitDesc + ")");
                    mSubscribeTip4TextView.setText("数量（" + mProjectDataBean.getBase() + unitDesc + "起约）");//数量（2台起约）
                    mNumberTextView.setText(String.format("%s%s", mProjectDataBean.getBase(), unitDesc));
                    break;
                case "2":// 时间
                    number = NumberFormatUtils.toInt(mProjectDataBean.getBase(), 1) * 2;
                    mSubscribeTip3TextView.setText(String.format("%s(半小时)", mProjectDataBean.getTitle()));
                    mSubscribeTip4TextView.setText("服务时长（" + number * 0.5 + "小时起约）");//服务时长（2小时起约）
                    mNumberTextView.setText(String.format("%s小时", number * 0.5));

                    break;
            }
        }
    }

    /**
     * 设置对应的各个价格
     */
    private void setPrice() {
        price = NumberFormatUtils.toDouble(unitPrice) * number;
        mUnitPriceTextView.setText(String.format("￥%s", unitPrice));
        mSubTotalPriceTextView.setText(String.format("￥%s", StringUtil.saveTwoDecimal(String.valueOf(price))));
        mTotalPriceTextView.setText(String.format("￥%s", StringUtil.saveTwoDecimal(String.valueOf(price))));
    }

    /**
     * 减法
     */
    private void reduce() {
        if (!TextUtils.isEmpty(unit)) {
            // 最小值
            int minNumber;
            switch (unit) {
                case "1":// 数量
                    minNumber = NumberFormatUtils.toInt(mProjectDataBean.getBase(), 1);
                    if (number == minNumber) {
                        // 减到最小
                        ToastManager.showMsgToast(mContext, "已到最小值");
                    } else {
                        // 减一
                        number--;
                    }

                    mNumberTextView.setText(number + unitDesc);
                    break;
                case "2":// 时间
                    minNumber = NumberFormatUtils.toInt(mProjectDataBean.getBase(), 1) * 2;
                    if (number == minNumber) {
                        // 减到最小
                        ToastManager.showMsgToast(mContext, "已到最小值");
                    } else {
                        // 减一
                        number--;
                    }

                    mNumberTextView.setText(number * 0.5 + unitDesc);
                    break;
            }

            setSubTotalAndTotalPrice();
        }
    }

    /**
     * 加法
     */
    private void addition() {
        // 加一
        number++;
        if (!TextUtils.isEmpty(unit)) {
            switch (unit) {
                case "1":// 数量
                    mNumberTextView.setText(String.format("%d%s", number, unitDesc));
                    break;
                case "2":// 时间
                    mNumberTextView.setText(String.format("%s%s", number * 0.5, unitDesc));
                    break;
            }
        }

        setSubTotalAndTotalPrice();
    }

    private void showToast() {
        if (!TextUtils.isEmpty(mTimeTextView.getText().toString().trim())) {
            ToastManager.showShortToast(mContext, "已修改服务信息，请重新选择服务时间");
            // 归原
            time = 0;
            mTimeTextView.setText(null);
        }
    }

    /**
     * 设置小计和合计价格
     */
    private void setSubTotalAndTotalPrice() {
        BigDecimal b1 = new BigDecimal(Integer.toString(number));
        BigDecimal b2 = new BigDecimal(unitPrice);
        price = b1.multiply(b2).doubleValue();
        mSubTotalPriceTextView.setText(String.format("￥%s", price));
        mTotalPriceTextView.setText(String.format("￥%s", price));
    }

    /**
     * 下单前检测
     */
    private void checkValue() {
        if (TextUtils.isEmpty(addressId)) {
            ToastManager.showShortToast(mContext, "服务地址不能为空");
            return;
        }
        if (time == 0) {
            ToastManager.showShortToast(mContext, "服务时间不能为空");
            return;
        }
        if (ZLDateUtils.getCurrentTimeSeconds() > time) {
            ToastManager.showShortToast(mContext, "服务时间已过期，请修改服务时间");
            return;
        }

        if (TextUtils.isEmpty(id)) {
            return;
        }
        preOrderCheck();
    }

    /**
     * 下单前检测
     */
    private void preOrderCheck() {
        HashMap<String, String> map = new HashMap<>();
        User user = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (user == null) {
            return;
        }
        // 1:预约
        map.put(REQUEST_PARAM_TYPE, "1");
        // 产品id
        map.put(REQUEST_PARAM_IDS, id);
        // 开始时间（时间戳）
        map.put(REQUEST_PARAM_ENDTIME, String.valueOf(time / 1000));
        // 时长（小时）
        map.put(REQUEST_PARAM_NUM, String.valueOf(number));
        RequestManager.createRequest(ZLURLConstants.HEALTH_ADD_ORDER_CHECK_URL,
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
        User user = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (user == null) {
            return;
        }

        if (TextUtils.isEmpty(GlobalParams.communityId)) {
            ToastManager.showMsgToast(mContext, getString(R.string.noLocalLifeServiceHint));
            return;
        }
        // 小区id
        map.put(URLConstants.REQUEST_PARAM_COMMUNITY_ID, GlobalParams.communityId);
        // 下单人id
        map.put(REQUEST_PARAM_UID, user.getData().getId());
        // 产品id
        map.put(REQUEST_PARAM_IDS, id);
        // 开始时间（时间戳）
        map.put(REQUEST_PARAM_ENDTIME, String.valueOf(time / 1000));
        // 时长（小时）
        map.put(REQUEST_PARAM_NUM, String.valueOf(number));
        // 地址id（必传）
        map.put(REQUEST_PARAM_ADDRESS, addressId);
        // 实际支付金额（必传）
        map.put(REQUEST_PARAM_PRICE, String.valueOf(price));
        // 1：预约
        map.put(REQUEST_PARAM_TYPE, "1");
        String mNoteStr = mNoteEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(mNoteStr)) {
            // 订单备注
            map.put(REQUEST_PARAM_CONTENT, mNoteStr);
        }
        RequestManager.createRequest(ZLURLConstants.HEALTH_ORDER_ADD_URL,
                map, new OnMyActivityRequestListener<Order>(this) {
                    @Override
                    public void onSuccess(Order bean) {
                        Order.DataBean data = bean.getData();
                        startActivity(PayActivity.newIntent(mContext, data.getOrderid(), data.getTitle(), 2, ""));
                        finish();
                    }

                });
    }

    /**
     * 服务频次对话框
     */
    private void showServiceFrequencyDialog() {
        List<String> list = new ArrayList<>();
        String[] s = new String[]{"一", "两", "三", "四"};
        for (int i = 0; i < 4; i++) {
            list.add(s[i] + "周");
        }
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
            }
        })
                .setLineSpacingMultiplier(2.0f)
                .setDividerColor(0xffc6c9cf)
                .setTitleSize(14)
                .setBgColor(0xffe1e1e1)
                .setTitleBgColor(0xfff5f5f5)
                .setTitleText("请选择服务频次")
                .setContentTextSize(16)
                .build();
        pvOptions.setPicker(list);
        pvOptions.show();
    }

    /**
     * 服务周期对话框
     */
    private void showServiceCycleDialog() {
        List<String> list1 = new ArrayList<>();
        List<String> list = new ArrayList<>();
        List<List<String>> list2 = new ArrayList<>();
        String[] s = new String[]{"一", "两", "三", "四"};
        for (int i = 0; i < 4; i++) {
            list1.add(s[i] + "周");
            list.add(s[i] + "次");
        }
        for (int i = 0; i < 4; i++) {
            list2.add(list);
        }
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
            }
        })
                .setLineSpacingMultiplier(2.0f)
                .setDividerColor(0xffc6c9cf)
                .setTitleSize(14)
                .setBgColor(0xffe1e1e1)
                .setTitleBgColor(0xfff5f5f5)
                .setTitleText("请选择服务周期")
                .setContentTextSize(16)
                .build();
        pvOptions.setPicker(list1, list2);
        pvOptions.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null)
            if (requestCode == REQUEST_CODE_SELECT_TIME) {
                // 选择时间返回
                ScheduleBean.TimeBean result = HealthSelectServiceTimeActivity.getResult(data);
                if (result != null) {
                    // 选择了时间
                    long l = NumberFormatUtils.toLong(result.getTimestamp()) * 1000;
                    mTimeTextView.setText(DateUtils.getYearMonthDay(l, "yyyy-MM-dd HH:mm"));
                    time = l;
                }
            } else if (requestCode == ZLConstants.Integers.REQUEST_CODE_ADDRESS) {
                AddressBean.DataBean addressBean = HarvestAddressActivity.getResult(data);
                addressId = addressBean.getId();
                mPhoneTextView.setText(addressBean.getPhone());
                mAddressTextView.setText(addressBean.getAddress());
            }
    }

}
