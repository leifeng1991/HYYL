package com.xxzlkj.huayiyanglao.activity.foodorder;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.activity.PayActivity;
import com.xxzlkj.huayiyanglao.activity.address.EditHarvestAddressActivity;
import com.xxzlkj.huayiyanglao.activity.address.HarvestAddressActivity;
import com.xxzlkj.huayiyanglao.adapter.ConfirmOderListAdapter;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.event.DeleteAddressEvent;
import com.xxzlkj.huayiyanglao.event.FoodsSaleListEvent;
import com.xxzlkj.huayiyanglao.event.FoodsShopCartEvent;
import com.xxzlkj.huayiyanglao.model.AddressBean;
import com.xxzlkj.huayiyanglao.model.FoodsAddOrderBean;
import com.xxzlkj.huayiyanglao.model.FoodsCartListBean;
import com.xxzlkj.huayiyanglao.weight.ShapeButton;
import com.xxzlkj.shop.activity.shop.MakeSureOrderActivity;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.utils.ZLDialogUtil;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 外卖-确认订单
 *
 * @author zhangrq
 */
public class TakeOutConfirmOrderActivity extends MyBaseActivity {
    public static final int REQUEST_CODE = 2001;
    private RelativeLayout mNoAddressLayout;
    private LinearLayout mHasAddressLayout;
    private TextView mUserNameTextView, mUserPhoneTextView, mAddressTextView, mGoodsTotalPriceTextView,
            mFreightTextView, mDiscountsTextView, mTotalPriceTextView;
    private ShapeButton mDefaultShapeButton;
    private RecyclerView mRecyclerView;
    private Button mImmediatePaymentButton;
    private EditText mNoteEditText;
    private ConfirmOderListAdapter mConfirmOderListAdapter;
    private String mShopId;
    private User loginUser;
    private String mAddressId;
    private boolean isHomeDelivery;
    private double totalPrice;
    private String mIds;
    private String mNums;
    private Dialog dialog;

    /**
     * @param context
     * @param mShopId        店铺id(必传)
     * @param isHomeDelivery 是否是外卖到家true:外卖到家 false：到店食用（必传）
     * @return
     */
    public static Intent newIntent(Context context, String mShopId, boolean isHomeDelivery) {
        Intent intent = new Intent(context, TakeOutConfirmOrderActivity.class);
        intent.putExtra(ZLConstants.Strings.SHOPID, mShopId);
        intent.putExtra(ZLConstants.Strings.IS_HOME_DELIVERY, isHomeDelivery);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_confirm_order);
    }

    @Override
    protected void findViewById() {
        EventBus.getDefault().register(this);
        mHasAddressLayout = getView(R.id.id_has_address_layout);// 有收获地址布局
        mUserNameTextView = getView(R.id.id_user_name);// 收货人姓名
        mUserPhoneTextView = getView(R.id.id_user_phone);// 收货人电话
        mAddressTextView = getView(R.id.id_address);// 收货地址
        mDefaultShapeButton = getView(R.id.id_default);// 默认地址标记
        mNoAddressLayout = getView(R.id.id_no_address_layout);// 没有收获地址显示布局
        mRecyclerView = getView(R.id.id_recycler_view);// 外卖商品列表
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mConfirmOderListAdapter = new ConfirmOderListAdapter(mContext, R.layout.adapter_dialog_list_item);
        mRecyclerView.setAdapter(mConfirmOderListAdapter);
        mGoodsTotalPriceTextView = getView(R.id.id_goods_total_price);// 商品总金额
        mFreightTextView = getView(R.id.id_freight);// 运费
        mDiscountsTextView = getView(R.id.id_discounts);// 优惠
        mNoteEditText = getView(R.id.id_note);// 备注
        mTotalPriceTextView = getView(R.id.id_total_price);// 总金额
        mImmediatePaymentButton = getView(R.id.id_immediate_payment);// 立即支付
        // 默认不能点击
        mImmediatePaymentButton.setEnabled(false);
    }

    @Override
    protected void setListener() {
        // 有收获地址布局点击事件
        mHasAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到地址列表界面
                startActivityForResult(HarvestAddressActivity.newIntent(mContext, mAddressId), REQUEST_CODE);
            }
        });
        // 没有收获地址布局点击事件
        mNoAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到添加地址界面
                startActivityForResult(EditHarvestAddressActivity.newIntent(mContext, mAddressId), ZLConstants.Integers.REQUEST_CODE_ADD_ADDRESS);
            }
        });
        // 立即支付点击事件
        mImmediatePaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodsAddOrder();
            }
        });
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("确认订单");
        loginUser = ZhaoLinApplication.getInstance().getLoginUser();
        if (loginUser == null) {
            finish();
            return;
        }
        mShopId = getIntent().getStringExtra(ZLConstants.Strings.SHOPID);
        isHomeDelivery = getIntent().getBooleanExtra(ZLConstants.Strings.IS_HOME_DELIVERY, true);
        getDefaultAddress();
        getFoodsCartList();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null)
            switch (requestCode) {
                case REQUEST_CODE:// 选择地址
                    AddressBean.DataBean result = HarvestAddressActivity.getResult(data);
                    if (result != null) {
                        setAddressData(result.getName(), result.getPhone(), result.getId(), result.getState(), result.getAddress());
                    }
                    break;
                case ZLConstants.Integers.REQUEST_CODE_ADD_ADDRESS:// 增加新地址
                    AddressBean.DataBean result1 = EditHarvestAddressActivity.getResult(data);
                    if (result1 != null) {
                        mHasAddressLayout.setVisibility(View.VISIBLE);
                        mNoAddressLayout.setVisibility(View.GONE);
                        setAddressData(result1.getName(), result1.getPhone(), result1.getId(), result1.getState(), result1.getAddress());
                    }
                    break;
            }

    }

    /**
     * 购物车列表
     */
    private void getFoodsCartList() {
        Map<String, String> map = new HashMap<>();
        map.put(ZLConstants.Params.SHOPID, mShopId);
        // 会员id(会员id)
        map.put(ZLConstants.Params.UID, loginUser.getData().getId());
        RequestManager.createRequest(ZLURLConstants.FOODS_CART_LIST_URL, map,
                new OnMyActivityRequestListener<FoodsCartListBean>(this) {
                    @Override
                    public void onSuccess(FoodsCartListBean bean) {
                        double mTotalPrice = 0;
                        // 商品id 多个用英文逗号隔开
                        String ids = "";
                        // 商品数量 多个用英文逗号隔开 id和数量必须一一对应
                        String nums = "";
                        mConfirmOderListAdapter.clearAndAddList(bean.getData());

                        // 遍历获取购物车中商品总数和所有商品总价格
                        for (int i = 0; i < mConfirmOderListAdapter.getList().size(); i++) {
                            List<FoodsCartListBean.DataBean.ListBean> list = mConfirmOderListAdapter.getList().get(i).getList();
                            for (int j = 0; j < list.size(); j++) {
                                FoodsCartListBean.DataBean.ListBean listBean = list.get(j);
                                String num = listBean.getNum();
                                int number = NumberFormatUtils.toInt(num);
                                mTotalPrice += NumberFormatUtils.toDouble(listBean.getPrice()) * number;
                                String pid = listBean.getPid();
                                ids = TextUtils.isEmpty(ids) ? pid : ids + "," + pid;
                                nums = TextUtils.isEmpty(nums) ? num : nums + "," + num;
                            }
                        }
                        totalPrice = mTotalPrice;
                        mIds = ids;
                        mNums = nums;
                        mTotalPriceTextView.setText(String.format("￥%s", mTotalPrice));
                        mGoodsTotalPriceTextView.setText(String.format("￥%s", mTotalPrice));

                    }

                    @Override
                    public void onFailed(boolean isError, String code, String message) {

                    }
                });
    }

    /**
     * 我的默认地址
     */
    private void getDefaultAddress() {

        HashMap<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_UID, loginUser.getData().getId());
        RequestManager.createRequest(URLConstants.REQUEST_MY_FIRST_ADDRESS,
                map, new OnMyActivityRequestListener<AddressBean>(this) {
                    @Override
                    public void onSuccess(AddressBean bean) {
                        mHasAddressLayout.setVisibility(View.VISIBLE);
                        mNoAddressLayout.setVisibility(View.GONE);
                        AddressBean.DataBean data = bean.getData();
                        setAddressData(data.getName(), data.getPhone(), data.getId(), data.getState(), data.getAddress());
                    }

                    @Override
                    public void onFailed(boolean isError, String code, String message) {
                        LogUtil.e("===========", code);
                        mAddressId = "";
                        if ("400".equals(code)) {
                            mHasAddressLayout.setVisibility(View.GONE);
                            mNoAddressLayout.setVisibility(View.VISIBLE);
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

    private void setAddressData(String userName, String userPhone, String mAddressId, String state, String address) {
        mUserNameTextView.setText(userName);
        mUserPhoneTextView.setText(userPhone);
        // 默认的显示和隐藏
        mDefaultShapeButton.setVisibility("2".equals(state) ? View.VISIBLE : View.GONE);
        mAddressTextView.setText(address);
        this.mAddressId = mAddressId;
        foodsShopDistance();
    }

    /**
     * 检测地址距离
     */
    private void foodsShopDistance() {
        HashMap<String, String> map = new HashMap<>();
        // 地址id(必传)
        map.put(URLConstants.REQUEST_PARAM_ID, mAddressId);
        // 店铺id
        map.put(ZLConstants.Params.SHOPID, mShopId);
        RequestManager.createRequest(ZLURLConstants.FOODS_SHOP_DISTANCE_URL,
                map, new OnMyActivityRequestListener<BaseBean>(this) {
                    @Override
                    public void onSuccess(BaseBean bean) {
                        // 设置支付按钮可以点击
                        mImmediatePaymentButton.setEnabled(true);
                        mImmediatePaymentButton.setBackgroundColor(0xff54B1F8);
                    }

                    @Override
                    public void onFailed(boolean isError, String code, String message) {
                        if (isFinishing()) return;
                        if (dialog == null) {
                            dialog = ZLDialogUtil.showOrderDialog(TakeOutConfirmOrderActivity.this,
                                    R.mipmap.beyond_ditance, message, new ZLDialogUtil.OnDialogButtonClickListener() {
                                        @Override
                                        public void onLeftClick() {

                                        }

                                        @Override
                                        public void onRightClick() {
                                            startActivityForResult(HarvestAddressActivity.newIntent(mContext, mAddressId), REQUEST_CODE);
                                        }
                                    });
                        } else {
                            if (!dialog.isShowing()) {
                                dialog.show();
                            }
                        }
                        // 设置支付按钮不可以点击
                        mImmediatePaymentButton.setEnabled(false);
                        mImmediatePaymentButton.setBackgroundColor(0xffcccccc);
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
     * 立即下单
     */
    private void foodsAddOrder() {
        HashMap<String, String> map = new HashMap<>();
        // 会员id(必传)
        map.put(ZLConstants.Params.UID, loginUser.getData().getId());
        // 商品id，多个商品用逗号隔开(必传)
        if (TextUtils.isEmpty(mIds))
            return;
        map.put(ZLConstants.Params.IDS, mIds);
        // 商品数量，多个商品用逗号隔开，与商品id一一对应（必传）
        if (TextUtils.isEmpty(mNums))
            return;
        map.put(ZLConstants.Params.NUM, mNums);
        // 就餐方式 1外卖到家 2到店食用
        map.put(ZLConstants.Params.DELIVERY, isHomeDelivery ? "1" : "2");
        // 地址id（必传）
        map.put(ZLConstants.Params.ADDRESS, mAddressId);
        // 店铺id(必传)
        map.put(ZLConstants.Params.SHOPID, mShopId);
        // 实际支付金额（必传）
        if (TextUtils.isEmpty(StringUtil.saveTwoDecimal(totalPrice)))
            return;
        map.put(ZLConstants.Params.PRICE, StringUtil.saveTwoDecimal(totalPrice));
        // 订单备注
        String noteText = mNoteEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(noteText))
            map.put(ZLConstants.Params.CONTENT, noteText);
        RequestManager.createRequest(ZLURLConstants.FOODS_ADD_ORDER_URL,
                map, new OnMyActivityRequestListener<FoodsAddOrderBean>(this) {
                    @Override
                    public void onSuccess(FoodsAddOrderBean bean) {
                        String nums = "";
                        for (int i = 0; i < mIds.split(",").length; i++) {
                            nums = i == 0 ? "0" : nums + "," + "0";
                        }
                        EventBus.getDefault().postSticky(new FoodsShopCartEvent());
                        EventBus.getDefault().postSticky(new FoodsSaleListEvent(mIds, nums));
                        FoodsAddOrderBean.DataBean data = bean.getData();
                        startActivity(PayActivity.newIntent(mContext, data.getOrderid(), data.getTitle(), 3, ""));
                        finish();
                    }

                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteAddress(DeleteAddressEvent event) {
        String addressId = event.getAddressId();
        if (!TextUtils.isEmpty(addressId) && addressId.equals(mAddressId))
            getDefaultAddress();
    }
}
