package com.xxzlkj.shop.activity.shop;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.address.EditHarvestAddressActivity;
import com.xxzlkj.shop.activity.address.HarvestAddressActivity;
import com.xxzlkj.shop.adapter.RecommendGoodsAdapter;
import com.xxzlkj.shop.adapter.ShopCartFailShopAdapter;
import com.xxzlkj.shop.adapter.ShopCartListAdapter;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.event.ShopCartAddressEvent;
import com.xxzlkj.shop.event.ShopCartEvent;
import com.xxzlkj.shop.event.ShopCartListEvent;
import com.xxzlkj.shop.model.AddressBean;
import com.xxzlkj.shop.model.CheckDistanceBean;
import com.xxzlkj.shop.model.Discount;
import com.xxzlkj.shop.model.Goods;
import com.xxzlkj.shop.model.GoodsDetail;
import com.xxzlkj.shop.model.RecommendGoods;
import com.xxzlkj.shop.model.ShopCartList;
import com.xxzlkj.shop.model.ShopCartModel;
import com.xxzlkj.shop.weight.CustomPopupWindow;
import com.xxzlkj.shop.weight.RecyclerViewDividerItemDecoration;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 购物车
 */
public class ShopCartActivity extends MyBaseActivity {
    private RelativeLayout mAddressRelativeLayout, mNoAddressRelativeLayout, mBottomLayout, mBottomEidtLayout, mNoGoodsLayout;
    private TextView mNameTextView, mPhoneTextView, mDefaultTextView, mAddressTextView, mEditTextView,
            mTotalPriceTextView, mTotalNumTextView, mDeleteTextView, mPreferentialTextView;
    private ExpandableListView mShopListView;
    private ListView mFailShopListView;
    private View vg_recommend_layout;
    private RecyclerView mRecomendRecyclerView;
    private CheckBox mBottomEditCheckBox;
    private ShopCartListAdapter mShopCartListAdapter;
    private ShopCartFailShopAdapter mFailShopAdapter;
    private LinearLayout mHasGoodsLayout;
    //结算商品数量
    private int totalNumber = 0;
    //结算总金额
    private double mTotalPrice = 0.00;
    private User mLoginUser;
    //商品id
    private String goodsId;
    //购物车id
    private String shopCartId;
    // 折扣系数
    private String mDiscount;
    // 属性选择
    private CustomPopupWindow mCustomPopupWindow;
    // 地址
    private AddressBean.DataBean mAddressBean;
    // 是否刷新数据
    private boolean isRefresh;
    // 门店id
    private String mStoreId;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, ShopCartActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_shop_cart);
    }

    @Override
    protected void findViewById() {
        SystemBarUtils.setStatusBarLightMode(this, true);
        EventBus.getDefault().register(this);
        mEditTextView = getView(R.id.tv_title_right);// 编辑
        mEditTextView.setVisibility(View.VISIBLE);
        mEditTextView.setText("编辑");

        mAddressRelativeLayout = getView(R.id.id_address_layout);// 有收货地址
        mNameTextView = getView(R.id.id_name);// 收货人姓名
        mPhoneTextView = getView(R.id.id_phone);// 收货人手机号
        mDefaultTextView = getView(R.id.id_default);// 默认
        mAddressTextView = getView(R.id.id_address);// 收货人地址
        mNoAddressRelativeLayout = getView(R.id.id_no_address_layout);// 没有收货地址

        mHasGoodsLayout = getView(R.id.id_sc_layout_1);// 商品列表和失效商品列表父布局
        mNoGoodsLayout = getView(R.id.id_sc_layout_2);// 购物车为空时布局
        mShopListView = getView(R.id.id_sc_goods_list);// 商品列表
        mFailShopListView = getView(R.id.id_sc_fail_list);// 失效商品列表
        vg_recommend_layout = getView(R.id.vg_recommend_layout);// 推荐商品列表
        mRecomendRecyclerView = getView(R.id.id_sc_recommend_list);// 推荐商品列表
        mRecomendRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        // 禁止RecyclerView滑动解决RecyclerView不显示问题
        mRecomendRecyclerView.setNestedScrollingEnabled(false);
        // 分割线
        RecyclerViewDividerItemDecoration dividerItemDecoration = new RecyclerViewDividerItemDecoration(mContext, LinearLayout.VERTICAL, 2);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.shape_divide_3));
        mRecomendRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecomendRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mBottomLayout = getView(R.id.id_sc_bottom);// 底部布局
        mBottomEidtLayout = getView(R.id.id_sc_bottom_edit);
        mBottomEditCheckBox = getView(R.id.id_sc_cb_edit);// 全选
        mTotalPriceTextView = getView(R.id.id_sc_total_price);// 实付价格
        mPreferentialTextView = getView(R.id.id_sc_preferential);// 总额 + 立减
        mTotalNumTextView = getView(R.id.id_sc_settlement);// 结算数量
        mDeleteTextView = getView(R.id.id_sc_delete);// 删除
        // 设置默认门店id
        mStoreId = GlobalParams.storeId;

        intPopupWindow();
    }

    @Override
    protected void setListener() {
        mEditTextView.setOnClickListener(this);
        mDeleteTextView.setOnClickListener(this);
        mTotalNumTextView.setOnClickListener(this);
        mBottomEditCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShopCartListAdapter != null && mShopCartListAdapter.getGroupCount() > 0) {
                    mShopCartListAdapter.setupAllChecked(mBottomEditCheckBox.isChecked());
                }
            }
        });
        mBottomEditCheckBox.setClickable(false);
        // 没有收货地址时布局点击事件
        mNoAddressRelativeLayout.setOnClickListener(this);
        // 有收货地址时布局点击事件
        mAddressRelativeLayout.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("购物车");

        mLoginUser = BaseApplication.getInstance().getLoginUserDoLogin(this);
        if (mLoginUser != null) {
            getAddress();
            getRecommendList();
            getDisCount();
        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRefresh) {
            getShopCartList();
            isRefresh = false;
        }
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
        if (i == R.id.tv_title_right) {
            String mEditStr = mEditTextView.getText().toString();
            if ("编辑".equals(mEditStr)) {
                mEditTextView.setText("完成");
                mBottomLayout.setVisibility(View.GONE);
                mBottomEidtLayout.setVisibility(View.VISIBLE);
                if (mShopCartListAdapter != null && mShopCartListAdapter.getGroupCount() > 0) {
                    ShopCartListAdapter.flag = true;
                    mShopCartListAdapter.notifyDataSetChanged();
                }
            } else if ("完成".equals(mEditStr)) {
                mEditTextView.setText("编辑");
                mBottomEidtLayout.setVisibility(View.GONE);
                mBottomLayout.setVisibility(View.VISIBLE);
                if (mShopCartListAdapter != null && mShopCartListAdapter.getGroupCount() > 0) {
                    ShopCartListAdapter.flag = false;
                    mShopCartListAdapter.notifyDataSetChanged();
                }
            }

        } else if (i == R.id.id_sc_delete) {
            if (mShopCartListAdapter != null && mShopCartListAdapter.getGroupCount() > 0) {
                mShopCartListAdapter.removeGoods();
            }

        } else if (i == R.id.id_sc_settlement) {
            if (mAddressBean != null) {
                if (totalNumber > 0) {
                    ShopCartList.DataBean dataBean = mShopCartListAdapter.getGoodsList();
                    if (dataBean.getGoods().size() > 0) {
                        startActivity(MakeSureOrderActivity.newIntent(this, dataBean, mAddressBean, mTotalPrice, ""));
                    }
                } else {
                    ToastManager.showMsgToast(mContext, "请选择商品");

                }
            } else {
                ToastManager.showMsgToast(mContext, "你还没有添加收货地址");
            }


        } else if (i == R.id.id_address_layout) {
            startActivityForResult(HarvestAddressActivity.newIntent(mContext, 5, mAddressBean.getId()), StringConstants.REQUSTCODE_ORDER);

        } else if (i == R.id.id_no_address_layout) {
            startActivityForResult(EditHarvestAddressActivity.newIntent(mContext, 1, null, 4), StringConstants.REQUSTCODE_ORDER);

        }
    }

    /**
     * PopupWindow
     */
    private void intPopupWindow() {
        mCustomPopupWindow = new CustomPopupWindow(this, new CustomPopupWindow.AddShopCartListener() {
            @Override
            public void sureClick(TextView numberText) {
                if (!TextUtils.isEmpty(goodsId) && !TextUtils.isEmpty(shopCartId)) {
                    updataGoods(shopCartId, goodsId, numberText);
                    mEditTextView.setText("编辑");
                    mBottomEidtLayout.setVisibility(View.GONE);
                    mBottomLayout.setVisibility(View.VISIBLE);
                    setDefaultValue();
                }
            }

            @Override
            public void addClick(TextView numberText) {
                // 商品加一
                Integer number = NumberFormatUtils.toInt(numberText.getText().toString());
                number++;
                numberText.setText(String.valueOf(number));
            }

            @Override
            public void reduceClick(TextView numberText) {
                // 商品减一
                Integer numberLast = NumberFormatUtils.toInt(numberText.getText().toString());
                if (numberLast > 1) {
                    numberLast--;
                    numberText.setText(String.valueOf(numberLast));
                }
            }
        }, false);
    }

    /**
     * 修改购物车商品信息
     */
    private void updataGoods(String gouWuCheId, String goodsId, TextView mPopupNumberTextView) {
        Map<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_ID, gouWuCheId);
        mLoginUser = BaseApplication.getInstance().getLoginUserDoLogin(this);
        if (mLoginUser != null) {
            map.put(URLConstants.REQUEST_PARAM_UID, mLoginUser.getData().getId());
            map.put(URLConstants.REQUEST_PARAM_GOODS_ID, goodsId);
            map.put(URLConstants.REQUEST_PARAM_NUM, mPopupNumberTextView.getText().toString());
            RequestManager.createRequest(URLConstants.REQUEST_EDIT_CART_GOODS, map, new OnMyActivityRequestListener<BaseBean>(this) {
                @Override
                public void onSuccess(BaseBean bean) {
                    getShopCartList();
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void setDefaultValue() {
        mBottomEditCheckBox.setChecked(false);
        mTotalNumTextView.setText("结算（" + 0 + "）");

        if (!TextUtils.isEmpty(mDiscount)) {
            mTotalPriceTextView.setText("￥0.00");
            mPreferentialTextView.setText("总额：￥0.00 立减：￥0.00");
        } else {
            mTotalPriceTextView.setText("￥0.00");
        }
    }

    /**
     * 获取购物车商品列表
     */
    private void getShopCartList() {
        User user = BaseApplication.getInstance().getLoginUser();
        if (user != null) {
            Map<String, String> map = new HashMap<>();
            // 会员id（必传）
            map.put(URLConstants.REQUEST_PARAM_UID, user.getData().getId());
            String adrressId = PreferencesSaver.getStringAttr(mContext, StringConstants.PREFERENCES_MY_ADDRESS_ID);
            if (mAddressBean != null) {
                // 收货地址不为null
                // 地址id
                map.put(URLConstants.REQUEST_PARAM_ADDRESSID, mAddressBean.getId());
            } else {
                // 收货地址为null或者其他情况
                if (!TextUtils.isEmpty(adrressId)) {
                    // 地址id
                    map.put(URLConstants.REQUEST_PARAM_ADDRESSID, adrressId);
                } else {
                    if (GlobalParams.longitude != 0 && GlobalParams.latitude != 0) {
                        // 经度
                        map.put(URLConstants.REQUEST_PARAM_LONGITUDE, GlobalParams.longitude + "");
                        // 纬度
                        map.put(URLConstants.REQUEST_PARAM_LATITUDE, GlobalParams.latitude + "");
                    } else {
                        // 小区id
                        if (!TextUtils.isEmpty(GlobalParams.communityId))
                            map.put(URLConstants.REQUEST_PARAM_COMMUNITY_ID, GlobalParams.communityId);

                    }
                }
            }

            RequestManager.createRequest(URLConstants.REQUEST_CART_LIST, map,
                    new OnMyActivityRequestListener<ShopCartList>(this) {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(ShopCartList bean) {
                            setDefaultValue();
                            List<ShopCartList.DataBean.GBean> mGoods = bean.getData().getGoods();
                            List<ShopCartList.DataBean.GBean> mG = bean.getData().getG();
                            // 失效商品
                            if (mG != null && mG.size() > 0) {
                                mFailShopAdapter = new ShopCartFailShopAdapter(ShopCartActivity.this, ShopCartActivity.this, mG);
                                mFailShopListView.setAdapter(mFailShopAdapter);
                            }

                            //定义父列表项List数据集合
                            List<Map<String, Object>> mParentMapList = new ArrayList<>();
                            //定义子列表项List数据集合
                            List<List<Map<String, Object>>> mChildMapList = new ArrayList<>();

                            for (int i = 0; i < 1; i++) {
                                //提供当前父列的子列数据
                                List<Map<String, Object>> childMapList = new ArrayList<>();
                                for (int j = 0; j < mGoods.size(); j++) {
                                    Map<String, Object> childMap = new HashMap<>();
                                    ShopCartList.DataBean.GBean goodsBean = mGoods.get(j);
                                    childMap.put("childName", goodsBean);
                                    childMapList.add(childMap);
                                }
                                mChildMapList.add(childMapList);

                                if (childMapList.size() > 0) {
                                    //提供父列表的数据
                                    Map<String, Object> parentMap = new HashMap<>();
                                    ShopCartModel shopCartModel = new ShopCartModel();
                                    shopCartModel.setName("兆邻自营");
                                    parentMap.put("parentName", shopCartModel);
                                    mParentMapList.add(parentMap);
                                }
                            }

                            if (mParentMapList.size() > 0) {
                                mShopCartListAdapter = new ShopCartListAdapter(ShopCartActivity.this, ShopCartActivity.this, mParentMapList, mChildMapList);
                                mShopListView.setAdapter(mShopCartListAdapter);

                                for (int i = 0; i < mParentMapList.size(); i++) {
                                    mShopListView.expandGroup(i);
                                }

                                mShopCartListAdapter.setOnGoodsCheckedChangeListener((totalCount, totalPrice) -> {
                                    mTotalNumTextView.setText("结算（" + totalCount + "）");
                                    if (!TextUtils.isEmpty(mDiscount)) {
                                        String discountResult = StringUtil.saveTwoDecimal(String.valueOf(NumberFormatUtils.toFloat(mDiscount) * totalPrice));
                                        mTotalPriceTextView.setText("￥" + discountResult);
                                        mPreferentialTextView.setText("总额：￥" + totalPrice + " 立减：￥" + StringUtil.saveTwoDecimal(String.valueOf(totalPrice - NumberFormatUtils.toFloat(discountResult))));
                                    } else {
                                        mTotalPriceTextView.setText("￥" + StringUtil.saveTwoDecimal(totalPrice));
                                    }

                                    totalNumber = totalCount;
                                    mTotalPrice = totalPrice;
                                });

                                mShopCartListAdapter.setOnAllCheckedBoxNeedChangeListener(allParentIsChecked -> mBottomEditCheckBox.setChecked(allParentIsChecked));

                                mShopCartListAdapter.setOnCheckHasGoodsListener(isHasGoods -> setupViewsShow(isHasGoods));
                            }

                            if ((mG == null || mG.size() <= 0)
                                    && (mShopCartListAdapter == null || mShopCartListAdapter.getGroupCount() <= 0)) {
                                setupViewsShow(false);
                            } else {
                                setupViewsShow(true);
                            }
                            // 设置全选按钮可选
                            mBottomEditCheckBox.setClickable(true);
                        }

                        @Override
                        public void onFailed(boolean isError, String code, String message) {
                            setDefaultValue();
                            setupViewsShow(false);
                            mBottomEditCheckBox.setClickable(false);
                        }
                    });
        }

    }

    /**
     * 获取商店id
     *
     * @return
     */
    public String getStoreId() {
        return mStoreId;
    }

    /**
     * 处理listView的显示和隐藏
     *
     * @param isHasGoods
     */
    private void setupViewsShow(boolean isHasGoods) {
        if (!isHasGoods && (mFailShopAdapter == null || mFailShopAdapter.getCount() == 0)) {
            // 购物车为空
            mShopListView.setVisibility(View.GONE);
            mFailShopListView.setVisibility(View.GONE);
            mHasGoodsLayout.setVisibility(View.GONE);
            mNoGoodsLayout.setVisibility(View.VISIBLE);
        } else {
            // 购物车不为空
            mShopListView.setVisibility(View.VISIBLE);
            mFailShopListView.setVisibility(View.VISIBLE);
            mHasGoodsLayout.setVisibility(View.VISIBLE);
            mNoGoodsLayout.setVisibility(View.GONE);
        }
        // 控制全选按钮状态和是否可以点击
        if (!isHasGoods) {
            mBottomEditCheckBox.setChecked(false);
            mBottomEditCheckBox.setClickable(false);
        }
    }

    private void getAddress() {
        // 本地保存的地址
        String addressId = PreferencesSaver.getStringAttr(mContext, StringConstants.PREFERENCES_MY_ADDRESS_ID);
        if (TextUtils.isEmpty(addressId)) {
            getDefaultAddress();
        } else {
            getAddressDetail(addressId);
        }
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
                            setAddressData(mAddressBean);
                            checkDistance(mAddressBean.getId());
                        }
                    }

                    @Override
                    public void onFailed(boolean isError, String code, String message) {
                        LogUtil.e("===========", code);
                        if ("400".equals(code)) {
                            mAddressRelativeLayout.setVisibility(View.GONE);
                            mNoAddressRelativeLayout.setVisibility(View.VISIBLE);
                            if (TextUtils.isEmpty(mStoreId)) {
                                showDialog();
                            }
                            mAddressBean = null;
                        }

                        getShopCartList();

                    }

                    @Override
                    public void handlerStart() {

                    }

                    @Override
                    public void handlerEnd() {

                    }
                });
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("没有收货地址，无法查询库存哦~");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(EditHarvestAddressActivity.newIntent(mContext, 1, null, 4), StringConstants.REQUSTCODE_ORDER);
            }
        });
        builder.show();
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
                    checkDistance(data.getId());
                }
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                mAddressBean = null;
            }
        });
    }

    /**
     * 设置地址相关数据
     *
     * @param addressData
     */
    private void setAddressData(AddressBean.DataBean addressData) {
        if (mAddressBean != null) {
            // 地址不为空
            mAddressRelativeLayout.setVisibility(View.VISIBLE);
            mNoAddressRelativeLayout.setVisibility(View.GONE);
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
            mAddressRelativeLayout.setVisibility(View.GONE);
            mNoAddressRelativeLayout.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(mStoreId)) {
                showDialog();
            }
            mAddressBean = null;
        }

        getShopCartList();
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
                    }

                    @Override
                    public void onError(String code, String message) {
                        mStoreId = "";
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
     * 获取推荐商品列表
     */
    private void getRecommendList() {
        // 默认隐藏
        vg_recommend_layout.setVisibility(View.GONE);
        Map<String, String> map = new HashMap<>();
        String id = mLoginUser.getData().getId();
        if (TextUtils.isEmpty(id)) {
            id = "0";
        }
        if (TextUtils.isEmpty(GlobalParams.storeId)) {
            return;
        }
        // 店铺id (必传)
        map.put(URLConstants.REQUEST_PARAM_STORE_ID, GlobalParams.storeId);
        // 会员id (必传)
        map.put(URLConstants.REQUEST_PARAM_UID, id);
        RequestManager.createRequest(URLConstants.REQUEST_RECOMMEND_GOODS, map,
                new OnMyActivityRequestListener<RecommendGoods>(this) {
                    @Override
                    public void onSuccess(RecommendGoods bean) {
                        RecommendGoodsAdapter mRecommendGoodsAdapter =
                                new RecommendGoodsAdapter(ShopCartActivity.this, R.layout.adapter_recommend_item);
                        List<Goods> data = bean.getData();
                        mRecommendGoodsAdapter.addList(data);
                        vg_recommend_layout.setVisibility(data.size() == 0 ? View.GONE : View.VISIBLE);// 是否展示推荐
                        mRecomendRecyclerView.setAdapter(mRecommendGoodsAdapter);
                        mRecommendGoodsAdapter.setOnItemClickListener((position, item) -> {
                            startActivity(GoodsDetailActivity.newIntent(mContext, item.getId(), mStoreId));
                            isRefresh = true;
                        });
                    }

                    @Override
                    public void onFailed(boolean isError, String code, String message) {
                        vg_recommend_layout.setVisibility(View.GONE);
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
                @Override
                public void onSuccess(Discount bean) {
                    mDiscount = bean.getData().getDiscount();
                    if (!TextUtils.isEmpty(mDiscount)) {
                        mPreferentialTextView.setVisibility(View.VISIBLE);
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
     * 获取商品详情数据
     */
    private void getGoodsDetail(String id) {
        Map<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_ID, id);
        if (mLoginUser == null) {
            map.put(URLConstants.REQUEST_PARAM_UID, "0");
        } else {
            map.put(URLConstants.REQUEST_PARAM_UID, mLoginUser.getData().getId());
        }

        // 店铺id (必传)
        if (!TextUtils.isEmpty(mStoreId)) {
            map.put(URLConstants.REQUEST_PARAM_STORE_ID, mStoreId);
        }

        RequestManager.createRequest(URLConstants.REQUEST_GOODS_INFO, map, new OnMyActivityRequestListener<GoodsDetail>(this) {
            @Override
            public void onSuccess(GoodsDetail bean) {
                mCustomPopupWindow.setPopupWindow(ShopCartActivity.this, bean);
                if (!mCustomPopupWindow.isShowing()) {
                    mCustomPopupWindow.showAtLocationBottom(ShopCartActivity.this, R.id.id_rl_foot);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case StringConstants.REQUSTCODE_ORDER:
                    // 收货地址
                    mAddressBean = (AddressBean.DataBean)
                            data.getSerializableExtra(StringConstants.INTENT_PARAM_LOC);
                    setAddressData(mAddressBean);
                    checkDistance(mAddressBean.getId());
                    break;
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void shopCartEvent(ShopCartEvent event) {
        if (!TextUtils.isEmpty(event.getShopCartId())) {
            shopCartId = event.getShopCartId();
        }
        if (!TextUtils.isEmpty(event.getNum()) && mCustomPopupWindow != null) {
            mCustomPopupWindow.setNumberText(event.getNum());
        }

        goodsId = event.getGoodsId();
        getGoodsDetail(event.getGoodsId());
    }

    /**
     * 刷新购入车商品列表
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshShopCartList(ShopCartListEvent event) {
        if (event.isRefresh()) {
            getShopCartList();
        }
    }

    /**
     * 刷新地址
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshShopCartAddress(ShopCartAddressEvent event) {
        switch (event.getType()) {
            case 1:// 新增一个地址
                getAddress();
                break;
            case 2:// 修改一个地址
                if (mAddressBean != null) {
                    if (event.getAddressId().equals(mAddressBean.getId())) {
                        getAddressDetail(event.getAddressId());
                    }
                }
                break;
            case 3:// 删除一个地址
                if (mAddressBean != null) {
                    if (event.getAddressId().equals(mAddressBean.getId())) {
                        getAddress();
                    }
                }
                break;
        }
    }

}
