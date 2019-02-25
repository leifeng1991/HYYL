package com.xxzlkj.licallife.activity.localLife;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.model.NannyAndMaternityMatronDesBean;
import com.xxzlkj.licallife.model.OrderAddParameterBean;
import com.xxzlkj.licallife.model.ScheduleBean;
import com.xxzlkj.shop.activity.address.HarvestAddressActivity;
import com.xxzlkj.shop.event.JzSubscribeEvent;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.StringUtil;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;
import com.zrq.spanbuilder.Spans;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * 本地生活--保姆、月嫂--预约
 *
 * @author zhangrq
 */
public class NannyAndMaternityMatronSubscribeActivity extends MyBaseActivity {
    public static final String IS_NANNY = "isNanny";
    public static final String DATA_BEAN = "dataBean";
    public static final int REQUEST_CODE_SELECT_TIME = 123;
    private boolean isNanny;
    private LinearLayout mAddressLinearLayout, mTimeLinearLayout, mCouponsLayout;
    private TextView mTimeTextView, mBuyNowTextView, mSubTotalPriceTextView, mCouponsTextView,
            mNumberTextView, mBillingMoneyTextView, mPrePriceTitleTextView, mPrePriceTextView, mOrderPriceTextView,
            mPayTypeTextView, mAddressTextView;
    private RadioGroup mBillingTypeRadioGroup;
    private ImageView mReduceImageView, mAddImageView;
    private EditText mNoteEditText;
    private String addressId;
    //时间戳
    private long serviceTime = 0;
    private String serviceUnitStr = "个月";// 默认选中第一个，按月计费
    private double serviceNumber = 1;// 默认为1个月
    private String id;
    private RadioGroup.OnCheckedChangeListener listener;
    private boolean isMonthBilling;
    private double addOrReduceNumber, billingMoneyNumForOne, baseDayNumber, prePricePercentNumber, baseMonthNumber, halfMonthMoneyNumber, oneDayMoneyNumber;
    private String prePriceNumberStr;
    private String orderMoneyStr;
    private NannyAndMaternityMatronDesBean.DataBean dataBean;

    /**
     * @param isNanny  必传 isNanny 是否是保姆预约页面，true为保姆预约页面，false为月嫂预约页面
     * @param dataBean 必传 订单详情bean
     */
    public static Intent newIntent(Context context, boolean isNanny, NannyAndMaternityMatronDesBean.DataBean dataBean) {
        Intent intent = new Intent(context, NannyAndMaternityMatronSubscribeActivity.class);
        intent.putExtra(IS_NANNY, isNanny);
        intent.putExtra(DATA_BEAN, dataBean);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_nanny_and_maternity_matron_subscribe);
    }

    @Override
    protected void findViewById() {
        mAddressLinearLayout = getView(R.id.id_subscribe_address_layout);// 服务地址布局
        mAddressTextView = getView(R.id.id_subscribe_address);// 服务地址
        mTimeLinearLayout = getView(R.id.id_subscribe_time_layout);// 服务时间布局
        mTimeTextView = getView(R.id.id_subscribe_time);// 服务时间

        mBillingMoneyTextView = getView(R.id.tv_billing_money);// 计费单价金额
        mBillingTypeRadioGroup = getView(R.id.rg_subscribe);// 计费方式
        mNumberTextView = getView(R.id.id_subscribe_number);// 数量
        mReduceImageView = getView(R.id.id_subscribe_reduce);// 减
        mAddImageView = getView(R.id.id_subscribe_add);// 加
        mSubTotalPriceTextView = getView(R.id.id_subscribe_subtotal_price);// 小计
        mNoteEditText = getView(R.id.id_subscribe_note);// 备注
        mPayTypeTextView = getView(R.id.id_subscribe_pay_type);// 支付方式
        mCouponsLayout = getView(R.id.id_subscribe_coupons_layout);// 抵用券布局
        mCouponsTextView = getView(R.id.id_subscribe_coupons);// 抵用券
        mOrderPriceTextView = getView(R.id.id_subscribe_order_price);// 订单金额
        mPrePriceTitleTextView = getView(R.id.id_subscribe_pre_price_title);// 预定金额Title
        mPrePriceTextView = getView(R.id.id_subscribe_pre_price);// 预定金额

        mBuyNowTextView = getView(R.id.id_subscribe_buy_now);// 按钮立即支付
    }

    @Override
    protected void setListener() {
        EventBus.getDefault().register(this);
        // 服务地址
        mAddressLinearLayout.setOnClickListener(v -> startActivity(HarvestAddressActivity.newIntent(mContext, 2, addressId)));
        // 服务时间
        mTimeLinearLayout.setOnClickListener(v -> {
            // 选择时间
            if (dataBean != null) {
                startActivityForResult(SelectServiceTimeActivity.newIntent(mContext, "3", dataBean, 0, serviceTime / 1000), REQUEST_CODE_SELECT_TIME);
            }
        });
        // 计费方式
        listener = (group, checkedId) -> {
            if (checkedId == R.id.rb_month) {// 按月计费
                isMonthBilling = true;// 是否是按月计费
                serviceNumber = baseMonthNumber;// 归原
                addOrReduceNumber = 0.5;// 每次增加或者减少的值
                serviceUnitStr = "个月";
                billingMoneyNumForOne = halfMonthMoneyNumber * 2;// 计费单价金额

            } else if (checkedId == R.id.rb_day) {// 按天计费
                isMonthBilling = false;// 是否是按月计费
                serviceNumber = baseDayNumber;// 归原
                addOrReduceNumber = 1;// 每次增加或者减少的值
                serviceUnitStr = "天";
                billingMoneyNumForOne = oneDayMoneyNumber;// 计费单价金额

            }
            // 设置计费单价金额
            mBillingMoneyTextView.setText(String.valueOf("￥" + billingMoneyNumForOne));
            // 设置数量 和 小计的价格
            setNumberAndMoney();


        };
        mBillingTypeRadioGroup.setOnCheckedChangeListener(listener);
        // 减按钮
        mReduceImageView.setOnClickListener(v -> {
            if (serviceNumber == (isMonthBilling ? baseMonthNumber : baseDayNumber)) {
                ToastManager.showShortToast(mContext, "已到最小值");
            } else {
                // 已减，重新设置显示
                serviceNumber -= addOrReduceNumber;
                // 设置数量 和 小计的价格
                setNumberAndMoney();
            }
        });
        // 加按钮
        mAddImageView.setOnClickListener(v -> {
            // 已加，重新设置显示
            serviceNumber += addOrReduceNumber;
            // 设置数量 和 小计的价格
            setNumberAndMoney();
        });
        // 抵用券布局(暂无)
//        mCouponsLayout.setOnClickListener(v -> {
//            ToastManager.showShortToast(mContext, "抵用券");
//        });
        // 按钮立即支付
        mBuyNowTextView.setOnClickListener(v -> {
            // 检查
            // 开始时间（时间戳）
            if (TextUtils.isEmpty(addressId)) {
                ToastManager.showShortToast(mContext, "请选择服务地址");
                return;
            } else if (serviceTime == 0) {
                ToastManager.showShortToast(mContext, "请选择服务时间");
                return;
            }
            // 下一步
            next();

        });
    }

    /**
     * 设置数量和金额
     */
    private void setNumberAndMoney() {
        // 设置数量
        mNumberTextView.setText(String.valueOf(StringUtil.subZeroAndDot(serviceNumber + "") + serviceUnitStr));
        // 设置小计金额(数量*单价)
        orderMoneyStr = StringUtil.saveTwoDecimal(serviceNumber * billingMoneyNumForOne);
        mSubTotalPriceTextView.setText(String.valueOf("￥" + orderMoneyStr));
        // 设置订单金额，目前没有抵用券，所以金额和小计相同
        mOrderPriceTextView.setText(String.valueOf("￥" + orderMoneyStr));
        // 设置预定金额（订单金额 * 预定百分比）
        prePriceNumberStr = StringUtil.saveTwoDecimal(NumberFormatUtils.toDouble(orderMoneyStr) * prePricePercentNumber / 100);
        mPrePriceTextView.setText(String.valueOf("￥" + prePriceNumberStr));
    }

    @Override
    protected void processLogic() {
        isNanny = getIntent().getBooleanExtra(IS_NANNY, false);// 是否是保姆预约

        dataBean = (NannyAndMaternityMatronDesBean.DataBean) getIntent().getSerializableExtra(DATA_BEAN);// 已经判断一定不为null
        if (dataBean == null) {
            finish();
            ToastManager.showShortToast(mContext, "传值错误");
            return;
        }

        id = dataBean.getId();// id
//            base	起约基数（月）
//            bases	起约基数（日）
//            price 	半月价格
//            prices	每日价格
        String baseMonth = dataBean.getBase();// 起约基数（月）
        baseMonthNumber = NumberFormatUtils.toDouble(baseMonth, 1);
        String baseDay = dataBean.getBases();//  起约基数（日）
        baseDayNumber = NumberFormatUtils.toDouble(baseDay, 1);
        String halfMonthMoney = dataBean.getPrice();// 半个月薪
        halfMonthMoneyNumber = NumberFormatUtils.toDouble(halfMonthMoney);
        String oneDayMoney = dataBean.getPrices();//  日薪
        oneDayMoneyNumber = NumberFormatUtils.toDouble(oneDayMoney);
        String prePricePercent = dataBean.getShop().getYuesao();// 预约金额百分比 0-100
        prePricePercentNumber = NumberFormatUtils.toDouble(prePricePercent);

        setTitleLeftBack();
        setTitleName(isNanny ? "保姆" : "月嫂");

        // 设置默认选中 按月计费
        listener.onCheckedChanged(mBillingTypeRadioGroup, R.id.rb_month);// 设置默认选中
        // 设置预定金额Title提示
        mPrePriceTitleTextView.setText(Spans.builder().text("预定金额").text("（订单金额的" + prePricePercent + "%）").size(12).build());

    }

    /**
     * 下一步
     */
    private void next() {
        // 请求参数bean
        OrderAddParameterBean parameterBean = new OrderAddParameterBean();
        parameterBean.setType(isNanny ? "4" : "3");
        parameterBean.setNanny(isNanny);
        parameterBean.setIds(id);
        parameterBean.setEndtime(String.valueOf(serviceTime / 1000));
        parameterBean.setNum(String.valueOf(isMonthBilling ? serviceNumber * 2 : serviceNumber));
        parameterBean.setAddressId(addressId);
        parameterBean.setPrice(String.valueOf(prePriceNumberStr));
        parameterBean.setPrices(String.valueOf(orderMoneyStr));
        parameterBean.setTimetype(isMonthBilling ? "1" : "2");
        String mNoteStr = mNoteEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(mNoteStr))
            parameterBean.setContent(mNoteStr);
        NannyAndMaternityMatronDesBean.DataBean.ShopBean shop = dataBean.getShop();
        parameterBean.setShopAddress(shop.getAddress());
        startActivity(NannyAndMaternityMatronInterviewActivity.newIntent(mContext, parameterBean));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_TIME) {
            // 选择时间返回
            ScheduleBean.TimeBean result = SelectServiceTimeActivity.getResult(data);
            if (result != null) {
                // 选择了时间
                long l = NumberFormatUtils.toLong(result.getTimestamp()) * 1000;
                mTimeTextView.setText(DateUtils.getYearMonthDay(l, "yyyy-MM-dd"));
                serviceTime = l;
            }
        }
    }

    /**
     * 更新地址id，地址，手机号
     */
    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateJzSubscribe(JzSubscribeEvent event) {
        addressId = event.getAddressId();
        mAddressTextView.setText(event.getAddress());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
