package com.xxzlkj.shop.activity.shop;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.address.EditHarvestAddressActivity;
import com.xxzlkj.shop.activity.address.HarvestAddressActivity;
import com.xxzlkj.shop.adapter.DataBeanAdapter;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.event.DataChangEvent;
import com.xxzlkj.shop.event.MakeSureAddressEvent;
import com.xxzlkj.shop.event.ShopCartListEvent;
import com.xxzlkj.shop.interfac.ShopLibraryInterface;
import com.xxzlkj.shop.model.AddressBean;
import com.xxzlkj.shop.model.CheckDistanceBean;
import com.xxzlkj.shop.model.CofirmOrderBean;
import com.xxzlkj.shop.model.Discount;
import com.xxzlkj.shop.model.HarvestAddressList;
import com.xxzlkj.shop.model.Order;
import com.xxzlkj.shop.model.SelectCouponsBean;
import com.xxzlkj.shop.model.ShopCartList;
import com.xxzlkj.shop.utils.ZLDialogUtil;
import com.xxzlkj.shop.weight.CustomPopupWindow;
import com.xxzlkj.shop.weight.CustomScrollView;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnBaseRequestListener;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PreferencesSaver;
import com.xxzlkj.zhaolinshare.base.util.StringUtil;
import com.xxzlkj.zhaolinshare.base.util.SystemBarUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;


/**
 * 确认订单
 */
@SuppressWarnings("ALL")
public class MakeSureOrderActivity extends MyBaseActivity {
    private TextView mPhoneTextView, mNameTextView, mDefaultTextView, mAddressTextView, mZfTextView,
            mPsTextView, mFaPiaoTypeTextView, mOderNowTextView, mFaPiaoContentTextView, mTotalPriceTextView,
            mDiscountTextView, mKnockPriceTextView, mCouponsLeftTextView, mCouponsTypeTextView, mCouponsCanUseRightTextView;
    private RelativeLayout mAddressLayout, mFaPiaoTypeRl, mZfpsRelativeLayout, mFaPiaoContentRl, mNoHarvestAddressLayout;
    // 优惠券相关
    private LinearLayout mCouponsLayout;
    private ImageView mCouponsImageView;
    private GifImageView mGifImageView;
    private CustomScrollView mNestedScrollView;

    private RecyclerView mRecyclerView;
    private CheckBox mCheckBox;
    private LinearLayout mLinearLayout;
    private ShopCartList.DataBean dataBean;
    private List<ShopCartList.DataBean.GBean> gBeanList;
    // 总价
    private double mTotalPrice;
    // 实际支付的价格
    private double mRealPrice;
    //使用优惠券之前优惠价格
    private double mUseCouponsBeforePrice = 0;
    //发票类型
    private List<String> faPiaoList;
    //发票内容
    private List<String> stringList;
    private EditText mNoteEditText;
    private TextView rightTotalPrice;
    private User mLoginUser;
    // 配送时间
    private String mPeiSongTime;
    // 店铺id
    private String mStoreId;
    // 地址
    private AddressBean.DataBean mAddressBean;
    // 地址id
    private String mAddressId;
    // 折扣系数
    private String mDiscount;
    // 商品id集
    private String ids = "";
    // 商品数量集
    private String nums = "";
    // 优惠券id
    private String couponsId = "0";
    private SelectCouponsBean.DataBean.ItemBean itemBean = null;
    // flag:true 从支付配送界面 返回 falg:false 从地址选择界面 返回
    private boolean flag;
    private Dialog dialog;
    // true:跳转到选择优惠券
    private boolean intentToClass;
    //  团购组id
    private String grouponTeamId;

    /**
     * @param context       上下文（必传）
     * @param dataBean      商品列表bean （必传）
     * @param addressBean   地址bean 购物车页必传
     * @param totalPrice    所有商品总价 （必传）
     * @param grouponTeamId 团购组id 当商品时团购商品时必传
     * @return
     */
    public static Intent newIntent(Context context, ShopCartList.DataBean dataBean, AddressBean.DataBean addressBean, double totalPrice, String grouponTeamId) {
        Intent intent = new Intent(context, MakeSureOrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(StringConstants.PREFERENCES_KEY_KEYWORD_LIST, dataBean);
        bundle.putSerializable(StringConstants.INTENT_PARAM_ADDRESS_BEAN, addressBean);
        bundle.putDouble(StringConstants.INTENT_PARAM_TOTALPRICE, totalPrice);
        bundle.putString(StringConstants.INTENT_GROUPON_TEAM_ID, grouponTeamId);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLoginUser != null && mAddressBean == null) {//没有地址时，添加完地址进行地址的获取
            // 优先本地保存的地址
            String addressId = PreferencesSaver.getStringAttr(mContext, StringConstants.PREFERENCES_MY_ADDRESS_ID);
            if (TextUtils.isEmpty(addressId)) {
                getDefaultAddress();
            } else {
                if (TextUtils.isEmpty(mAddressId)) {
                    getMyAllAddress(addressId);
                    mAddressId = addressId;
                } else {
                    if (!flag) {
                        checkDistance(mAddressId);
                    }
                }

            }

        } else {
            if (!TextUtils.isEmpty(mAddressId) && !flag) {
                checkDistance(mAddressId);
            }
        }

    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_make_sure_order);
    }

    @Override
    protected void findViewById() {
        SystemBarUtils.setStatusBarLightMode(this, true);
        EventBus.getDefault().register(this);

        mPhoneTextView = getView(R.id.id_mso_phone);
        mNameTextView = getView(R.id.id_mso_name);
        mDefaultTextView = getView(R.id.id_mso_default);
        mAddressTextView = getView(R.id.id_mso_address);
        mZfpsRelativeLayout = getView(R.id.id_mso_zfps);
        mFaPiaoTypeRl = getView(R.id.id_mso_type);
        // 发票
        mFaPiaoTypeTextView = getView(R.id.id_mso_fp_type);
        mFaPiaoContentRl = getView(R.id.id_mso_content);
        mFaPiaoContentTextView = getView(R.id.id_mso_fp_content);
        mTotalPriceTextView = getView(R.id.id_mso_tp);
        mCheckBox = getView(R.id.id_mso_cb);
        mNoteEditText = getView(R.id.id_mso_note);
        mLinearLayout = getView(R.id.id_mso_layout);
        rightTotalPrice = getView(R.id.id_mso_total_price);
        mZfTextView = getView(R.id.id_mso_online);
        mPsTextView = getView(R.id.id_mso_lift);
        mOderNowTextView = getView(R.id.id_mso_order_now);
        mAddressLayout = getView(R.id.id_mso_address_mananger);
        mNoHarvestAddressLayout = getView(R.id.id_no_address_layout);
        mDiscountTextView = getView(R.id.id_mso_discount);
        mKnockPriceTextView = getView(R.id.id_mso_reduce_price);
        mRecyclerView = getView(R.id.id_mso_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 优惠券
        mCouponsLayout = getView(R.id.id_mso_coupons);
        mCouponsLeftTextView = getView(R.id.id_mso_coupons_left);
        mCouponsTypeTextView = getView(R.id.id_mso_coupons_type);
        mCouponsCanUseRightTextView = getView(R.id.id_mso_coupons_can_use);
        mCouponsImageView = getView(R.id.id_mso_coupons_image);
        mGifImageView = getView(R.id.id_mso_coupons_image);
        mNestedScrollView = getView(R.id.id_mso_scroll);

        mLoginUser = BaseApplication.getInstance().getLoginUser();

    }

    @Override
    protected void setListener() {
        mFaPiaoTypeRl.setOnClickListener(this);
        mFaPiaoContentRl.setOnClickListener(this);
        mZfpsRelativeLayout.setOnClickListener(this);
        mOderNowTextView.setOnClickListener(this);
        // 设置不可点击啊
        mOderNowTextView.setBackgroundColor(0xffcccccc);
        mOderNowTextView.setClickable(false);
        mAddressLayout.setOnClickListener(this);
        mNoHarvestAddressLayout.setOnClickListener(this);
        mCouponsLayout.setOnClickListener(this);
        mGifImageView.setOnClickListener(this);
        mCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mLinearLayout.setVisibility(View.VISIBLE);
            } else {
                mLinearLayout.setVisibility(View.GONE);
            }
        });
        // 优惠券图片显示和隐藏
        mNestedScrollView.setOnTouchEventListener(ev -> {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    mGifImageView.setVisibility(View.GONE);
                    break;
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    mGifImageView.setVisibility(View.VISIBLE);
                    break;
            }
        });

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("确认订单");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            dataBean = (ShopCartList.DataBean) bundle.getSerializable(StringConstants.PREFERENCES_KEY_KEYWORD_LIST);
            mAddressBean = (AddressBean.DataBean) bundle.getSerializable(StringConstants.INTENT_PARAM_ADDRESS_BEAN);
            mTotalPrice = bundle.getDouble(StringConstants.INTENT_PARAM_TOTALPRICE);
            grouponTeamId = bundle.getString(StringConstants.INTENT_GROUPON_TEAM_ID);
            gBeanList = dataBean.getGoods();
            // 获取商品id集和商品数集
            if (dataBean != null && gBeanList != null && gBeanList.size() > 0) {
                // 商品id和数量num一一对应
                for (int i = 0; i < gBeanList.size(); i++) {
                    if (i == gBeanList.size() - 1) {
                        ids = ids + gBeanList.get(i).getGoods_id();
                        nums = nums + gBeanList.get(i).getNum();
                    } else {
                        ids = ids + gBeanList.get(i).getGoods_id() + ",";
                        nums = nums + gBeanList.get(i).getNum() + ",";
                    }

                }

                // 检测是否有可用优惠券
                checkCoupons();
                // 设置地址相关数据
                setAddressData(mAddressBean);
            }
        }

        mDiscountTextView.setVisibility(View.GONE);
        mTotalPriceTextView.setText("￥" + StringUtil.saveTwoDecimal(String.valueOf(mTotalPrice)));
        mRealPrice = mTotalPrice;

        rightTotalPrice.setText("￥" + StringUtil.saveTwoDecimal(String.valueOf(mTotalPrice)));

        getDisCount();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.id_mso_address_mananger) {
            //地址选择、管理
            intentToClass = false;
            startActivityForResult(HarvestAddressActivity.newIntent(mContext, 1, mAddressId), StringConstants.REQUSTCODE_ORDER);
        } else if (id == R.id.id_mso_type) {
            // 发票类型
            intentToClass = false;
            choiceFaPiaoType();
        } else if (id == R.id.id_mso_content) {
            // 发票内容
            intentToClass = false;
            choiceFaPiaoContent();
        } else if (id == R.id.id_mso_zfps) {
            // 支付方式和配送方式选择
            intentToClass = false;
            choicePayAndDistribution();
        } else if (id == R.id.id_mso_order_now) {
            // 立即下单
            if (!TextUtils.isEmpty(mStoreId)) {
                addOrder();
            }
        } else if (id == R.id.id_mso_type) {
            // 没有收货地址跳到地址选择和管理
            intentToClass = false;
            startActivity(EditHarvestAddressActivity.newIntent(mContext, 1, "", 5));
        } else if (id == R.id.id_mso_coupons || id == R.id.id_mso_coupons_image) {
            // 优惠券layout
            // 点击优惠券图片
            intentToClass = true;
            startActivityForResult(SelectCouponsActivity.newIntent(this, itemBean, ids, nums, grouponTeamId), StringConstants.REQUSTCODE_COUPOS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 检测是否有可用优惠券
     */
    private void checkCoupons() {
        if (!TextUtils.isEmpty(ids) && !TextUtils.isEmpty(nums)) {
            restoreCoupons();
            SelectCouponsActivity.getNetDataByPublic(ids, nums, grouponTeamId, new OnBaseRequestListener<SelectCouponsBean>() {
                @Override
                public void handlerSuccess(SelectCouponsBean bean) {
                    if (bean.getData().getUsable().size() > 0) {
                        // 有
                        mCouponsCanUseRightTextView.setVisibility(View.VISIBLE);

                    } else {
                        // 无
                        mCouponsCanUseRightTextView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void handlerError(int errorCode, String errorMessage) {
                    mCouponsCanUseRightTextView.setVisibility(View.GONE);
                }
            });
        }
    }

    /**
     * 归原优惠券
     */
    private void restoreCoupons() {
        // 优惠券id
        couponsId = "0";
        mCouponsTypeTextView.setVisibility(View.GONE);
        mCouponsLeftTextView.setVisibility(View.GONE);
        mCouponsCanUseRightTextView.setVisibility(View.VISIBLE);
        mTotalPriceTextView.setText("￥" + StringUtil.saveTwoDecimal(mRealPrice));
        mKnockPriceTextView.setText("-￥" + StringUtil.saveTwoDecimal(mUseCouponsBeforePrice));
    }

    /**
     * 设置地址相关数据
     *
     * @param addressData
     */
    private void setAddressData(AddressBean.DataBean addressData) {
        if (addressData != null) {
            // 地址不为空
            mAddressLayout.setVisibility(View.VISIBLE);
            mNoHarvestAddressLayout.setVisibility(View.GONE);
            mAddressId = addressData.getId();
            mAddressTextView.setText(addressData.getAddress());
            mPhoneTextView.setText(addressData.getPhone());
            mNameTextView.setText(addressData.getName());
            if ("2".equals(addressData.getState())) {
                // 是默认地址
                mDefaultTextView.setVisibility(View.VISIBLE);
            } else {
                // 不是默认地址
                mDefaultTextView.setVisibility(View.GONE);
            }
        } else {
            // 地址为空
            mAddressLayout.setVisibility(View.GONE);
            mNoHarvestAddressLayout.setVisibility(View.VISIBLE);
            mAddressBean = null;
        }
    }


    /**
     * 选择支付配送方式
     */
    private void choicePayAndDistribution() {
        Intent intent = new Intent(this, PayAndDistributionActivity.class);
        String addressId;
        if (!TextUtils.isEmpty(mAddressId)) {
            addressId = mAddressId;
        } else {
            ToastManager.showShortToast(mContext, "暂无收货地址");
            return;
        }
        if (dataBean != null) {
            Bundle bundle = new Bundle();
            bundle.putString(StringConstants.INTENT_PARAM_IDS, ids);
            if ("兆邻配送".equals(mPsTextView.getText().toString().trim())) {// 兆邻配送
                bundle.putInt(StringConstants.INTENT_PARAM_PSTYPE, 1);
            } else {// 门店自提
                bundle.putInt(StringConstants.INTENT_PARAM_PSTYPE, 2);
            }

            if ("在线支付".equals(mZfTextView.getText().toString())) {// 在线支付
                bundle.putInt(StringConstants.INTENT_PARAM_ZFTYPE, 1);
            } else {// 货到付款
                bundle.putInt(StringConstants.INTENT_PARAM_ZFTYPE, 2);
            }

            bundle.putString(StringConstants.INTENT_PARAM_ADDRESSID, addressId);

            if (!TextUtils.isEmpty(mStoreId)) {
                bundle.putString(StringConstants.INTENT_PARAM_STOREID, mStoreId);
            }
            intent.putExtras(bundle);

        }
        startActivityForResult(intent, StringConstants.REQUSTCODE_MSO);
    }

    /**
     * 选择发票类型
     */
    private void choiceFaPiaoType() {
        if (faPiaoList == null) {
            faPiaoList = new ArrayList<>();
            faPiaoList.add("电子发票");
            faPiaoList.add("纸质发票");
        }
        CustomPopupWindow popupWindow = new CustomPopupWindow(this, "请选择发票类型", new CustomPopupWindow.OnMyClickListener() {
            @Override
            public void sureClickListener(String item) {
                mFaPiaoTypeTextView.setText(item);
            }

            @Override
            public void cancelClickListener() {

            }
        }, faPiaoList);
        popupWindow.showAtLocationBottom(this, R.id.id_mso_main);
    }

    /**
     * 选择发票内容
     */
    private void choiceFaPiaoContent() {
        if (stringList == null) {
            stringList = new ArrayList<>();
            stringList.add("食品");
            stringList.add("饮料");
            stringList.add("酒");
            stringList.add("日用品");
        }
        CustomPopupWindow popupWindow = new CustomPopupWindow(this, "请选择发票内容", new CustomPopupWindow.OnMyClickListener() {
            @Override
            public void sureClickListener(String item) {
                mFaPiaoContentTextView.setText(item);
            }

            @Override
            public void cancelClickListener() {

            }
        }, stringList);
        popupWindow.showAtLocationBottom(this, R.id.id_mso_main);
    }

    /**
     * 我的默认地址
     */
    private void getDefaultAddress() {
        if (mLoginUser == null)
            return;
        HashMap<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_UID, mLoginUser.getData().getId());
        RequestManager.createRequest(URLConstants.REQUEST_MY_FIRST_ADDRESS,
                map, new OnMyActivityRequestListener<AddressBean>(this) {
                    @Override
                    public void onSuccess(AddressBean bean) {
                        mAddressBean = bean.getData();
                        if (mAddressBean != null) {
                            mAddressId = mAddressBean.getId();
                            mAddressLayout.setVisibility(View.VISIBLE);
                            mNoHarvestAddressLayout.setVisibility(View.GONE);
                            mAddressTextView.setText(mAddressBean.getAddress());
                            mPhoneTextView.setText(mAddressBean.getPhone());
                            mNameTextView.setText(mAddressBean.getName());
                            if ("2".equals(mAddressBean.getState())) {
                                mDefaultTextView.setVisibility(View.VISIBLE);
                            } else {
                                mDefaultTextView.setVisibility(View.GONE);
                            }
                            checkDistance(mAddressBean.getId());
                        } else {
                            mAddressLayout.setVisibility(View.GONE);
                            mNoHarvestAddressLayout.setVisibility(View.VISIBLE);
                            mAddressBean = null;
                            show();
                        }
                    }

                    @Override
                    public void onFailed(boolean isError, String code, String message) {
                        LogUtil.e("===========", code);
                        if ("400".equals(code)) {
                            mAddressLayout.setVisibility(View.GONE);
                            mNoHarvestAddressLayout.setVisibility(View.VISIBLE);
                            mAddressBean = null;
                            show();
                        }

                    }

                    @Override
                    public void handlerStart() {

                    }

                    @Override
                    public void handlerEnd() {

                    }
                });
    }

    /**
     * 检测距离
     *
     * @param id
     */
    private void checkDistance(String id) {
        HashMap<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_ID, id);
        RequestManager.createRequest(URLConstants.REQUEST_CHECK_DISTANCE,
                map, new OnMyActivityRequestListener<CheckDistanceBean>(this) {
                    @Override
                    public void onSuccess(CheckDistanceBean bean) {
                        mStoreId = bean.getData().getStore_id();
                        checkConfirmOrder(ids, mStoreId);
                    }

                    @Override
                    public void onError(String code, String message) {
                        if (isFinishing()) return;
                        if (dialog == null) {
                            dialog = ZLDialogUtil.showOrderDialog(MakeSureOrderActivity.this,
                                    R.mipmap.beyond_ditance, message, new ZLDialogUtil.OnDialogButtonClickListener() {
                                        @Override
                                        public void onLeftClick() {

                                        }

                                        @Override
                                        public void onRightClick() {
                                            startActivityForResult(HarvestAddressActivity.newIntent(mContext, 1, mAddressId), StringConstants.REQUSTCODE_ORDER);
                                        }
                                    });
                        } else {
                            if (!dialog.isShowing()) {
                                dialog.show();
                            }
                        }

                        setGoodList(null);
                        // 不可点击
                        mOderNowTextView.setBackgroundColor(0xffcccccc);
                        mOderNowTextView.setClickable(false);
                    }

                    @Override
                    public void handlerStart() {

                    }

                    @Override
                    public void handlerEnd() {

                    }
                });
    }

    private void checkConfirmOrder(String ids, String storeId) {
        HashMap<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_IDS, ids);
        map.put(URLConstants.REQUEST_PARAM_STOREID, storeId);
        // 团购小组id
        if (!TextUtils.isEmpty(grouponTeamId))
            map.put(URLConstants.REQUEST_PARAM_GROUPON_TEAM_ID, grouponTeamId);
        RequestManager.createRequest(URLConstants.CONFIRM_ORDER_URL,
                map, new OnBaseRequestListener<CofirmOrderBean>() {

                    @Override
                    public void handlerSuccess(CofirmOrderBean bean) {
                        List<CofirmOrderBean.DataBean> data = bean.getData();
                        if (data != null && data.size() > 0) {
                            List<CofirmOrderBean.DataBean> newData = new ArrayList<>();
                            Collections.reverse(data);
                            String[] idStr = ids.split(",");
                            String[] numStr = nums.split(",");
                            // 归原
                            mOderNowTextView.setBackgroundColor(0xffff725c);
                            mOderNowTextView.setClickable(true);
                            // 获取集合的正确顺序
                            for (int i = 0; i < idStr.length; i++) {
                                String anIdStr = idStr[i];
                                for (int j = 0; j < data.size(); j++) {
                                    CofirmOrderBean.DataBean dataBean = data.get(j);
                                    if (anIdStr.equals(dataBean.getId())) {
                                        // 更改价格
                                        gBeanList.get(i).setPrice(dataBean.getPrice());
                                        newData.add(dataBean);
                                        break;
                                    }
                                }
                            }
                            // 设置提示和提交按钮状态
                            for (int i = 0; i < numStr.length; i++) {
                                String stock = newData.get(i).getStock();
                                if (NumberFormatUtils.toDouble(stock) < NumberFormatUtils.toDouble(numStr[i])) {
                                    showDialog();
                                    // 设置不可点击啊
                                    mOderNowTextView.setBackgroundColor(0xffcccccc);
                                    mOderNowTextView.setClickable(false);
                                    break;
                                }
                            }
                            // 商品列表
                            setGoodList(newData);
                        }

                    }

                    @Override
                    public void handlerError(int errorCode, String errorMessage) {

                    }
                });
    }

    private void showDialog() {
        if (isFinishing()) return;// 防止Activity销毁
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("部分商品库存不足，请返回修改订单");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.show();
    }


    /**
     * 立即下单
     */
    private void addOrder() {
        if (mLoginUser == null)
            return;
        HashMap<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_UID, mLoginUser.getData().getId());
        if (dataBean != null && dataBean.getGoods() != null && dataBean.getGoods().size() > 0) {
            // 商品id和数量num一一对应
            map.put(URLConstants.REQUEST_PARAM_IDS, ids);
            map.put(URLConstants.REQUEST_PARAM_NUM, nums);
        }

        // 配送方式
        if ("兆邻配送".equals(mPsTextView.getText().toString().trim())) {
            map.put(URLConstants.REQUEST_PARAM_DELIVERY, "1");
        } else {
            map.put(URLConstants.REQUEST_PARAM_DELIVERY, "2");
        }
        // 地址id
        map.put(URLConstants.REQUEST_PARAM_ADDRESS, mAddressId);
        // 店铺id 选择地址时传回的id
        map.put(URLConstants.REQUEST_PARAM_STORE_ID, mStoreId);
        // 实际支付金额
        String totalPriceStr = mTotalPriceTextView.getText().toString();
        map.put(URLConstants.REQUEST_PARAM_PRICE, totalPriceStr.substring(1, totalPriceStr.length()));
        // 订单备注
        String noteStr = mNoteEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(noteStr)) {
            map.put(URLConstants.REQUEST_PARAM_CONTENT, noteStr);
        }
        // 优惠券id 没有传0
        map.put(URLConstants.REQUEST_PARAM_COUPON_ID, couponsId);
        // 团购小组id
        if (!TextUtils.isEmpty(grouponTeamId))
            map.put(URLConstants.REQUEST_PARAM_GROUPON_TEAM_ID, grouponTeamId);
        RequestManager.createRequest(URLConstants.REQUEST_ADD_ORDER,
                map, new OnMyActivityRequestListener<Order>(this) {
                    @Override
                    public void onSuccess(Order bean) {
                        EventBus.getDefault().postSticky(new ShopCartListEvent(true));
                        Order.DataBean data = bean.getData();
                        if ("在线支付".equals(mZfTextView.getText().toString().trim())) {// 在线支付
                            double toDouble = NumberFormatUtils.toDouble(bean.getData().getPrice());
                            if (toDouble <= 0) {
                                requestNet(data.getOrderid(), false);
                            } else {
                                if (BaseApplication.getInstance() instanceof ShopLibraryInterface) {
                                    // 让主项目处理
                                    startActivityForResult(((ShopLibraryInterface) BaseApplication.getInstance()).getPayActivityIntent(MakeSureOrderActivity.this,
                                            bean.getData().getOrderid(), bean.getData().getTitle(), 1, grouponTeamId),
                                            StringConstants.REQUSTCODE_MSO);
                                }
                                finish();
                            }
                            EventBus.getDefault().postSticky(new DataChangEvent(0, true));

                        } else {// 货到付款
                            requestNet(data.getOrderid(), true);
                        }

                    }
                });
    }

    /**
     * @param orderId
     * @param falg    true:货到付款 false:减到0元时支付
     */
    private void requestNet(final String orderId, boolean falg) {
        HashMap<String, String> map = new HashMap<>();
        // 订单id
        map.put(URLConstants.REQUEST_PARAM_ORDERID, orderId);
        String url;
        if (falg) {// 货到付款
            url = URLConstants.REQUEST_COD;
        } else {// 减到0元时支付
            url = URLConstants.ZEROPAY_URL;
        }
        RequestManager.createRequest(url, map, new OnMyActivityRequestListener<BaseBean>(this) {
            @Override
            public void onSuccess(BaseBean bean) {
                startActivity(PaySuccessOrderDetailActivity.newIntent(mContext, orderId));
                finish();
            }
        });
    }

    /**
     * 检测全场折扣
     */
    private void getDisCount() {
        Map<String, String> map = new HashMap<>();
        if (mLoginUser != null) {
            RequestManager.createRequest(URLConstants.REQUEST_IS_ALL_DISCOUNT, map, new OnMyActivityRequestListener<Discount>(this) {
                @SuppressLint("SetTextI18n")
                @Override
                public void onSuccess(Discount bean) {
                    // 折扣系数
                    mDiscount = bean.getData().getDiscount();
                    if (!TextUtils.isEmpty(mDiscount)) {// 折扣后
                        mDiscountTextView.setVisibility(View.VISIBLE);
                        mDiscountTextView.setText("全场" + StringUtil.discountNumber2ChineseText(mDiscount) + "折");
                        mRealPrice = mTotalPrice * NumberFormatUtils.toFloat(mDiscount);
                        mKnockPriceTextView.setText("-￥" + StringUtil.saveTwoDecimal(String.valueOf(mTotalPrice - mRealPrice)));
                        mTotalPriceTextView.setText("￥" + StringUtil.saveTwoDecimal(String.valueOf(mRealPrice)));
                        mUseCouponsBeforePrice = mTotalPrice - mRealPrice;
                    } else {
                        mDiscountTextView.setVisibility(View.GONE);
                        mTotalPriceTextView.setText("￥" + StringUtil.saveTwoDecimal(String.valueOf(mTotalPrice)));
                        mRealPrice = mTotalPrice;
                    }

                }

                @Override
                public void handlerStart() {
                }

                @Override
                public void handlerEnd() {
                }

                @Override
                public void onError(String code, String message) {

                }
            });
        }
    }

    /**
     * 获取我的所有收货地址
     */
    private void getMyAllAddress(String id) {
        HashMap<String, String> map = new HashMap<>();
        User user = BaseApplication.getInstance().getLoginUser();
        if (user == null) {
            return;
        }
        map.put(URLConstants.REQUEST_PARAM_UID, user.getData().getId());
        RequestManager.createRequest(URLConstants.REQUEST_MY_ADDRESS, map, new OnMyActivityRequestListener<HarvestAddressList>(this) {
            @Override
            public void onSuccess(HarvestAddressList bean) {
                List<AddressBean.DataBean> data = bean.getData();
                if (data != null && data.size() > 0) {
                    for (int i = 0; i < data.size(); i++) {
                        if (id.equals(data.get(i).getId())) {
                            mAddressLayout.setVisibility(View.VISIBLE);
                            mNoHarvestAddressLayout.setVisibility(View.GONE);
                            mAddressTextView.setText(data.get(i).getAddress());
                            mPhoneTextView.setText(data.get(i).getPhone());
                            mNameTextView.setText(data.get(i).getName());
                            if ("2".equals(data.get(i).getState())) {
                                mDefaultTextView.setVisibility(View.VISIBLE);
                            } else {
                                mDefaultTextView.setVisibility(View.GONE);
                            }
                            checkDistance(id);
                            break;
                        }
                    }
                }

            }

        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case StringConstants.REQUSTCODE_MSO:// 支付和配送方式
                    // 检测是否有可用优惠券
                    checkCoupons();
                    // 支付配送方式
                    String payType = data.getStringExtra(StringConstants.INTENT_PARAM_PAYTYPE);
                    String peiSongType = data.getStringExtra(StringConstants.INTENT_PARAM_PEISONGTYPE);
                    mPeiSongTime = data.getStringExtra(StringConstants.INTENT_PARAM_PS_TIME);
                    String strStoreId = data.getStringExtra(StringConstants.INTENT_PARAM_STOREID);
                    if (!TextUtils.isEmpty(strStoreId) && "门店自提".equals(peiSongType)) {
                        flag = true;
                        // 门店id
                        mStoreId = strStoreId;
                        checkConfirmOrder(ids, mStoreId);
                    } else {
                        flag = false;
                    }

                    if (!TextUtils.isEmpty(payType)) {
                        mZfTextView.setText(payType);
                    }
                    if (!TextUtils.isEmpty(peiSongType)) {
                        mPsTextView.setText(peiSongType);
                    }
                    break;
                case StringConstants.REQUSTCODE_ORDER:// 地址
                    // 检测是否有可用优惠券
                    checkCoupons();
                    // 收货地址
                    AddressBean.DataBean db = (AddressBean.DataBean)
                            data.getSerializableExtra(StringConstants.INTENT_PARAM_LOC);
                    if (db != null) {
                        mPsTextView.setText("兆邻配送");
                        flag = false;
                        mAddressId = db.getId();
                        mAddressLayout.setVisibility(View.VISIBLE);
                        mNoHarvestAddressLayout.setVisibility(View.GONE);
                        mAddressTextView.setText(db.getAddress());
                        mPhoneTextView.setText(db.getPhone());
                        mNameTextView.setText(db.getName());
                        if ("2".equals(db.getState())) {
                            mDefaultTextView.setVisibility(View.VISIBLE);
                        } else {
                            mDefaultTextView.setVisibility(View.GONE);
                        }
//                        checkDistance(db.getId());
                    } else {
                        mAddressLayout.setVisibility(View.GONE);
                        mNoHarvestAddressLayout.setVisibility(View.VISIBLE);
                    }
                    break;
                case StringConstants.REQUSTCODE_COUPOS:// 优惠券
                    // 优惠券
                    itemBean = SelectCouponsActivity.getResult(data);
                    // 优惠价格
                    double couponsPrice = 0;
                    // 实付价格
                    double realPrice = 0;
                    if (itemBean != null) {// 不为空时
                        // 优惠券id
                        couponsId = itemBean.getId();
                        String rightType = "";
                        // 优惠类型
                        String type = itemBean.getType();
                        // 折扣系数/减免金额
                        String discount = itemBean.getDiscount();
                        double toDouble = NumberFormatUtils.toDouble(discount);
                        if ("1".equals(type)) {// 1 折扣
                            rightType = StringUtil.discountNumber2ChineseText(discount) + "折";
                            realPrice = mRealPrice * toDouble;
                            couponsPrice = mRealPrice - realPrice + mUseCouponsBeforePrice;
                        } else if ("2".equals(type)) {// 2 满减
                            String filled = itemBean.getFilled();
                            realPrice = mRealPrice - toDouble;
                            couponsPrice = mUseCouponsBeforePrice + toDouble;
                            if (toDouble > mRealPrice) {// 减免价格大于实际价格
                                couponsPrice = mTotalPrice;
                            }
                            if ("0".equals(filled)) {// 无限制
                                rightType = "减免" + discount + "元";
                            } else {// 有钱数限制
                                rightType = "满" + filled + "减" + discount;
                            }
                        }
                        // 优惠右边显示信息
                        if (!TextUtils.isEmpty(rightType)) {
                            mCouponsTypeTextView.setVisibility(View.VISIBLE);
                            mCouponsLeftTextView.setVisibility(View.VISIBLE);
                            mCouponsCanUseRightTextView.setVisibility(View.GONE);
                            mCouponsTypeTextView.setText(rightType);
                            if (realPrice <= 0) {
                                mTotalPriceTextView.setText("￥0.00");
                            } else {
                                mTotalPriceTextView.setText("￥" + StringUtil.saveTwoDecimal(realPrice));
                            }
                            if (couponsPrice > 0) {
                                mKnockPriceTextView.setText("-￥" + StringUtil.saveTwoDecimal(couponsPrice));
                            }
                        }
                    } else {// 为空时还原
                        restoreCoupons();
                    }
                    break;
            }
        }
    }

    /**
     * 没有收货地址显示提示对话框
     */
    private void show() {
        setGoodList(null);
        // 对话框
        if (isFinishing()) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您还没有收货地址，请添加地址后继续下单");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(EditHarvestAddressActivity.newIntent(mContext, 1, "", 5));
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * 设置商品列表
     */
    private void setGoodList(List<CofirmOrderBean.DataBean> dataBeanList) {
        // 商品列表
        if (gBeanList != null && gBeanList.size() > 0) {
            DataBeanAdapter mDataBeanAdapter = new DataBeanAdapter(MakeSureOrderActivity.this, dataBeanList, R.layout.adapter_hr_item_item_5);
            mDataBeanAdapter.addList(gBeanList);
            mRecyclerView.setAdapter(mDataBeanAdapter);

            if (!intentToClass && dataBeanList != null && dataBeanList.size() > 0) {

                double totalPrice = 0;
                // 遍历重新计算总价
                for (int i = 0; i < dataBeanList.size(); i++) {
                    totalPrice += NumberFormatUtils.toDouble(dataBeanList.get(i).getPrice()) * NumberFormatUtils.toDouble(nums.split(",")[i], 0);
                }

                mTotalPrice = totalPrice;

                mTotalPriceTextView.setText("￥" + StringUtil.saveTwoDecimal(String.valueOf(mTotalPrice)));
                mRealPrice = mTotalPrice;

                rightTotalPrice.setText("￥" + StringUtil.saveTwoDecimal(String.valueOf(mTotalPrice)));

                getDisCount();
            }

        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updataAddress(MakeSureAddressEvent event) {
        if (!TextUtils.isEmpty(mAddressId)) {
            if (event.getAddressId().equals(mAddressId)) {//当编辑/删除地址时更新
                // 检测是否有可用优惠券
                checkCoupons();
                switch (event.getType()) {
                    case 1:// 增加
                    case 3:// 删除
                        getDefaultAddress();
                        break;
                    case 2:// 修改
                        getAddressDetail(event.getAddressId());
                        break;

                }

            }
        }

    }

    /**
     * 获取地址详细信息
     */
    private void getAddressDetail(String deafultAddressId) {
        HashMap<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_ID, deafultAddressId);
        RequestManager.createRequest(URLConstants.REQUEST_ADDRESS_INFO, map, new OnMyActivityRequestListener<AddressBean>(this) {
            @Override
            public void onSuccess(AddressBean bean) {
                AddressBean.DataBean data = bean.getData();
                if (data != null) {
                    mAddressBean = data;
                    setAddressData(data);
                }
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                mAddressBean = null;
            }
        });
    }

}
