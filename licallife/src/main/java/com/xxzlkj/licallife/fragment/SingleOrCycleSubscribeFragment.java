package com.xxzlkj.licallife.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.activity.localLife.SelectServiceTimeActivity;
import com.xxzlkj.licallife.interfac.LocalLifeLibraryInterface;
import com.xxzlkj.licallife.model.CleanerDetailBean;
import com.xxzlkj.licallife.model.ProjectDetail;
import com.xxzlkj.licallife.model.ScheduleBean;
import com.xxzlkj.shop.activity.address.HarvestAddressActivity;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.event.JzSubscribeEvent;
import com.xxzlkj.shop.model.Order;
import com.xxzlkj.shop.utils.ZLDateUtils;
import com.xxzlkj.shop.weight.CustomButton;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.ReuseViewFragment;
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


/**
 * 单次/周期预约
 * create an instance of this fragment.
 */
public class SingleOrCycleSubscribeFragment extends ReuseViewFragment {
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
    private CleanerDetailBean.DataBean mCleanerDataBean;
    // 1:保洁项目 2：保洁师
    private int mProductType;
    // 1：单次预约 2：周期预约
    private int mSingleOrCycleSubscribe;
    private List<CleanerDetailBean.DataBean.GroupBeanX> leftDatas;
    private List<List<CleanerDetailBean.DataBean.GroupBeanX.GroupBean>> rightDatas;
    private String groupGroupTime;

    /**
     * @param position 1：单次预约 2：周期预约 （必传） TODO 目前暂时没有周期预约后期可能会有
     * @param id       保洁产品id （必传）
     * @param type     1:保洁项目 只能传1 (保洁项目) （必传）
     * @param data     保洁产品数据 （必传）
     * @return
     */
    public static SingleOrCycleSubscribeFragment newInstance(int position, String id, int type, ProjectDetail.DataBean data) {
        SingleOrCycleSubscribeFragment fragment = new SingleOrCycleSubscribeFragment();
        Bundle args = new Bundle();
        args.putString(StringConstants.INTENT_PARAM_ID, id);
        args.putInt(StringConstants.INTENT_PARAM_TYPE, type);
        args.putInt(StringConstants.INTENT_PARAM_POSITION, position);
        args.putSerializable(StringConstants.INTENT_PARAM_PROJECTDETAIL_DATABEAN, data);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * @param position 1：单次预约 2：周期预约 TODO 目前暂时没有周期预约后期可能会有
     * @param type     2：保洁师 3：小时工 只能传2或者3 （必传）
     * @param data     保洁师/小时工 数据 （必传）
     * @return
     */
    public static SingleOrCycleSubscribeFragment newInstance(int position, int type, CleanerDetailBean.DataBean data) {
        SingleOrCycleSubscribeFragment fragment = new SingleOrCycleSubscribeFragment();
        Bundle args = new Bundle();
        args.putInt(StringConstants.INTENT_PARAM_TYPE, type);
        args.putInt(StringConstants.INTENT_PARAM_POSITION, position);
        args.putSerializable(StringConstants.INTENT_PARAM_PROJECTDETAIL_DATABEAN, data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_single_subscribe, container, false);
    }

    @Override
    protected void findViewById() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
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
        Bundle bundle = getArguments();
        if (bundle != null) {
            mProductType = bundle.getInt(StringConstants.INTENT_PARAM_TYPE);
            mSingleOrCycleSubscribe = bundle.getInt(StringConstants.INTENT_PARAM_POSITION);
            // 控制服务周期和服务频次的显示和隐藏
            switch (mSingleOrCycleSubscribe) {
                case 1:// 隐藏 单次预约
                    mServiceFrequencyLayout.setVisibility(View.GONE);
                    mServiceCycleLayout.setVisibility(View.GONE);

                    // 设置初始数据
                    switch (mProductType) {
                        case 1:
                            // 1:保洁项目
                            id = bundle.getString(StringConstants.INTENT_PARAM_ID);
                            mProjectDataBean = (ProjectDetail.DataBean) bundle.getSerializable(StringConstants.INTENT_PARAM_PROJECTDETAIL_DATABEAN);
                            if (mProjectDataBean != null) {
                                initProjectData();
                            }
                            break;
                        case 2:// 2：保洁师
                            mLinearLayout.setOnClickListener(this);
                            // 显示右侧箭头
                            mArrowImageView.setVisibility(View.VISIBLE);
                            mCleanerDataBean = (CleanerDetailBean.DataBean) bundle.getSerializable(StringConstants.INTENT_PARAM_PROJECTDETAIL_DATABEAN);
                            if (mCleanerDataBean != null) {
                                initCleanerData();
                            }
                            break;
                        case 3:// 3：小时工
                            mCleanerDataBean = (CleanerDetailBean.DataBean) bundle.getSerializable(StringConstants.INTENT_PARAM_PROJECTDETAIL_DATABEAN);
                            if (mCleanerDataBean != null) {
                                initHourWorkData();
                            }
                            break;
                    }

                    setPrice();
                    break;
                case 2:// 显示 周期预约
                    mServiceFrequencyLayout.setVisibility(View.VISIBLE);
                    mServiceCycleLayout.setVisibility(View.VISIBLE);
                    mParentLayout.setVisibility(View.GONE);
                    break;
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ZLDateUtils.cancelTimer();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.id_subscribe_address_layout) {
            mActivity.startActivity(HarvestAddressActivity.newIntent(getContext(), 2, addressId));

        } else if (i == R.id.id_subscribe_time_layout) {// 选择时间
            if (mProductType == 1 && mProjectDataBean != null) {
                // 1:保洁项目（数量：计件-->选开始时间; 时间：计时-->选时间段 ）
                String unit = mProjectDataBean.getUnit(); // 1.数量    2.时间
                startActivityForResult(SelectServiceTimeActivity.newIntent(mContext, "1".equals(unit) ? "1" : "2", mProjectDataBean, number * 30, time), REQUEST_CODE_SELECT_TIME);
            } else if ((mProductType == 2 || mProductType == 3) && mCleanerDataBean != null) {
                // 2：保洁师、小时工
                boolean isHourWork = mProductType != 2;

                int serviceMinutesNumber;// 服务分钟数

                String serverTimeStyle;
                if (isHourWork) {
                    // 小时工（选时间段）
                    serverTimeStyle = "2";
                    serviceMinutesNumber = number * 30;
                } else {
                    serviceMinutesNumber = NumberFormatUtils.toInt(groupGroupTime) * number;
                    // 保洁师
                    if (serviceMinutesNumber > 0)
                        // 有时间折算（选时间段）
                        serverTimeStyle = "2";
                    else
                        // 无时间折算（选开始时间）
                        serverTimeStyle = "1";
                }
                startActivityForResult(SelectServiceTimeActivity.newIntent(mContext, serverTimeStyle, mCleanerDataBean, serviceMinutesNumber, time), REQUEST_CODE_SELECT_TIME);
            }

        } else if (i == R.id.id_subscribe_reduce) {
            reduce();
            showToast();

        } else if (i == R.id.id_subscribe_add) {
            addition();
            showToast();

        } else if (i == R.id.id_subscribe_buy_now) {
            checkValue();

        } else if (i == R.id.id_subscribe_order) {
        } else if (i == R.id.id_service_frequency_layout) {
            showServiceFrequencyDialog();

        } else if (i == R.id.id_service_cycle_layout) {
            showServiceCycleDialog();

        } else if (i == R.id.id_layout) {
            showDialog();

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
     * 设置初始化数据（保洁师）
     */
    private void initCleanerData() {
        unit = "1";
        number = 1;
        leftDatas = new ArrayList<>();
        rightDatas = new ArrayList<>();
        List<CleanerDetailBean.DataBean.GroupBeanX> group = mCleanerDataBean.getGroup();
        leftDatas = group;
        if (group != null && group.size() > 0) {
            for (int i = 0; i < group.size(); i++) {

                List<CleanerDetailBean.DataBean.GroupBeanX.GroupBean> group1 = group.get(i).getGroup();
                rightDatas.add(group1);
                // 设置默认值
                if (i == 0) {
                    if (group1 != null && group1.size() > 0) {
                        CleanerDetailBean.DataBean.GroupBeanX.GroupBean groupBean = group1.get(0);
                        unitDesc = groupBean.getUnit_desc();
                        mSubscribeTip3TextView.setText(groupBean.getTitle() + "(" + unitDesc + ")");
                        unitPrice = groupBean.getPrice();
                        groupGroupTime = groupBean.getTime();
                        int serviceMinutesNumber = NumberFormatUtils.toInt(groupGroupTime);// 服务分钟数
                        if (serviceMinutesNumber > 0) {
                            // 有时间折算
                            mTipTextView.setVisibility(View.VISIBLE);
                            mTipTextView.setText(String.format("当前服务，单次服务完成预计需要%d分钟", serviceMinutesNumber));
                        }
                        mNumberTextView.setText(String.format("%d%s", 1, unitDesc));
                        id = groupBean.getId();
                    }
                }
            }
        }
    }

    /**
     * 设置初始化数据（小时工）
     */
    private void initHourWorkData() {
        id = mCleanerDataBean.getId();
        unit = "2";
        unitPrice = mCleanerDataBean.getPrice();
        unitDesc = "小时";
        number = NumberFormatUtils.toInt(mCleanerDataBean.getBase(), 1) * 2;
        mSubscribeTip3TextView.setText(String.format("%s(半小时)", mCleanerDataBean.getName()));
        mSubscribeTip4TextView.setText("服务时长（" + number * 0.5 + "小时起约）");
        mNumberTextView.setText(String.format("%s小时", number * 0.5));
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
            int minNumber = 0;
            switch (mProductType) {
                case 1:// 项目
                    switch (unit) {
                        case "1":
                            minNumber = NumberFormatUtils.toInt(mProjectDataBean.getBase(), 1);
                            break;
                        case "2":
                            minNumber = NumberFormatUtils.toInt(mProjectDataBean.getBase(), 1) * 2;
                            break;
                    }
                    break;
                case 2:// 保洁师
                    minNumber = 1;
                    break;
                case 3:// 小时工
                    minNumber = NumberFormatUtils.toInt(mCleanerDataBean.getBase(), 1) * 2;
                    break;
            }
            switch (unit) {
                case "1":// 数量
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
        // 周期预约 检测
        if (mSingleOrCycleSubscribe == 2) {
            if (TextUtils.isEmpty(mServiceFrequencyTextView.getText().toString().trim())) {
                ToastManager.showMsgToast(mContext, "服务频次不能为空");
                return;
            }
            if (TextUtils.isEmpty(mServiceCycleTextView.getText().toString().trim())) {
                ToastManager.showMsgToast(mContext, "服务周期不能为空");
                return;
            }
        }
        if (TextUtils.isEmpty(id)) {
            return;
        }
        String type = null;
        switch (mProductType) {
            case 1:// 保洁项目
                type = "1";
                break;
            case 2:// 保洁师
                type = "2";
                break;
            case 3:// 小时工
                type = "5";
                break;
        }

        if (TextUtils.isEmpty(type)) {
            return;
        }

        if ("1".equals(type)) {
            summitOrder(type);
        } else {
            preOrderCheck(type);
        }
    }

    /**
     * 下单前检测
     */
    private void preOrderCheck(String type) {
        HashMap<String, String> map = new HashMap<>();
        User user = BaseApplication.getInstance().getLoginUserDoLogin(getActivity());
        if (user == null) {
            return;
        }

        // 2:保洁师 3:月嫂 4:保姆 5:小时工
        map.put(URLConstants.REQUEST_PARAM_TYPE, type);
        // 产品id
        map.put(URLConstants.REQUEST_PARAM_IDS, id);
        // 开始时间（时间戳）
        map.put(URLConstants.REQUEST_PARAM_ENDTIME, String.valueOf(time / 1000));
        // 时长（小时）
        map.put(URLConstants.REQUEST_PARAM_NUM, String.valueOf(number));
        RequestManager.createRequest(URLConstants.JZ_ADD_ORDER_CHECK_URL,
                map, new OnMyActivityRequestListener<BaseBean>((BaseActivity) getActivity()) {

                    @Override
                    public void onSuccess(BaseBean bean) {
                        summitOrder(type);
                    }

                    @Override
                    public void handlerStart() {

                    }
                });
    }

    /**
     * 提交订单
     */
    private void summitOrder(String type) {
        HashMap<String, String> map = new HashMap<>();
        User user = BaseApplication.getInstance().getLoginUserDoLogin(getActivity());
        if (user == null) {
            return;
        }

        // 1:保洁项目 2:保洁师 3:月嫂 4:保姆 5:小时工
        map.put(URLConstants.REQUEST_PARAM_TYPE, type);
        if (TextUtils.isEmpty(GlobalParams.communityId)) {
            ToastManager.showMsgToast(mContext, getString(R.string.noLocalLifeServiceHint));
            return;
        }
        // 小区id
        map.put(URLConstants.REQUEST_PARAM_COMMUNITY_ID, GlobalParams.communityId);
        // 下单人id
        map.put(URLConstants.REQUEST_PARAM_UID, user.getData().getId());
        // 产品id
        map.put(URLConstants.REQUEST_PARAM_IDS, id);
        // 开始时间（时间戳）
        map.put(URLConstants.REQUEST_PARAM_ENDTIME, String.valueOf(time / 1000));
        // 时长（小时）
        map.put(URLConstants.REQUEST_PARAM_NUM, String.valueOf(number));
        // 地址id（必传）
        map.put(URLConstants.REQUEST_PARAM_ADDRESS, addressId);
        // 实际支付金额（必传）
        map.put(URLConstants.REQUEST_PARAM_PRICE, String.valueOf(price));
        String mNoteStr = mNoteEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(mNoteStr)) {
            // 订单备注
            map.put(URLConstants.REQUEST_PARAM_CONTENT, mNoteStr);
        }
        RequestManager.createRequest(URLConstants.JZ_ORDER_ADD,
                map, new OnMyActivityRequestListener<Order>((BaseActivity) getActivity()) {
                    @Override
                    public void onSuccess(Order bean) {
                        Order.DataBean data = bean.getData();
                        if (BaseApplication.getInstance() instanceof LocalLifeLibraryInterface) {
                            mActivity.startActivity(((LocalLifeLibraryInterface) BaseApplication.getInstance()).getPayActivityIntent(mContext, data.getOrderid(), data.getTitle(), 2, ""));
                        }
                        getActivity().finish();
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
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(getContext(), new OptionsPickerView.OnOptionsSelectListener() {
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
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(getContext(), new OptionsPickerView.OnOptionsSelectListener() {
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

    /**
     * 服务项目对话框
     */
    private void showDialog() {
        // 判空
        if (rightDatas != null && rightDatas.size() > 0 && leftDatas != null && leftDatas.size() > 0) {
            //条件选择器
            OptionsPickerView pvOptions = new OptionsPickerView.Builder(getContext(), new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3, View v) {

                    CleanerDetailBean.DataBean.GroupBeanX.GroupBean groupBean = rightDatas.get(options1).get(option2);
                    unitDesc = groupBean.getUnit_desc();
                    unitPrice = groupBean.getPrice();
                    mSubscribeTip3TextView.setText(groupBean.getTitle() + "(" + unitDesc + ")");
                    id = groupBean.getId();
                    groupGroupTime = groupBean.getTime();
                    int serviceMinutesNumber = NumberFormatUtils.toInt(groupGroupTime);
                    if (serviceMinutesNumber > 0) {
                        // 有时间折算
                        mTipTextView.setVisibility(View.VISIBLE);
                        mTipTextView.setText(String.format("当前服务，单次服务完成预计需要%d分钟", serviceMinutesNumber));
                    } else {
                        // 无时间这算
                        mTipTextView.setVisibility(View.GONE);
                    }
                    mNumberTextView.setText(number + unitDesc);
                    setPrice();
                    showToast();
                }
            })
                    .setLineSpacingMultiplier(2.0f)
                    .setDividerColor(0xffc6c9cf)
                    .setTitleSize(14)
                    .setBgColor(0xffe1e1e1)
                    .setTitleBgColor(0xfff5f5f5)
                    .setTitleText("请选择服务项目")
                    .setContentTextSize(16)
                    .build();
            pvOptions.setPicker(leftDatas, rightDatas);
            pvOptions.show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_TIME) {
            // 选择时间返回
            ScheduleBean.TimeBean result = SelectServiceTimeActivity.getResult(data);
            if (result != null) {
                // 选择了时间
                long l = NumberFormatUtils.toLong(result.getTimestamp()) * 1000;
                mTimeTextView.setText(DateUtils.getYearMonthDay(l, "yyyy-MM-dd HH:mm"));
                time = l;
            }
        }
    }

    /**
     * 更新地址id，地址，手机号
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updataJzSubscrib(JzSubscribeEvent event) {
        addressId = event.getAddressId();
        mPhoneTextView.setText(event.getPhone());
        mAddressTextView.setText(event.getAddress());
    }
}
