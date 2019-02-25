package com.xxzlkj.shop.activity;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.sunfusheng.marqueeview.DisplayUtil;
import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.address.EditHarvestAddressActivity;
import com.xxzlkj.shop.activity.address.LocationActivity;
import com.xxzlkj.shop.activity.shop.HotSearchActivity;
import com.xxzlkj.shop.activity.shop.ShopCartActivity;
import com.xxzlkj.shop.activity.shop.SpeedGoodsCategoryActivity;
import com.xxzlkj.shop.adapter.ShopHomeCommonNavigatorAdapter;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.event.AddShopCartActionEvent;
import com.xxzlkj.shop.event.DataChangEvent;
import com.xxzlkj.shop.event.Home4AddressEvent;
import com.xxzlkj.shop.event.ShopHomeAddressEvent;
import com.xxzlkj.shop.event.ShopHomeLocationEvent;
import com.xxzlkj.shop.fragment.ShopHomeTabFragment;
import com.xxzlkj.shop.model.AddressBean;
import com.xxzlkj.shop.model.CheckDistanceBean;
import com.xxzlkj.shop.model.Goods;
import com.xxzlkj.shop.model.HarvestAddressList;
import com.xxzlkj.shop.model.ShopNavBean;
import com.xxzlkj.shop.utils.ActivityUtils;
import com.xxzlkj.shop.utils.AddShopCartUtil;
import com.xxzlkj.shop.utils.ShopCartNumberUtils;
import com.xxzlkj.shop.utils.ZLDialogUtil;
import com.xxzlkj.shop.utils.ZLPreferencesUtils;
import com.xxzlkj.shop.utils.listview.CommBaseAdapter;
import com.xxzlkj.shop.utils.listview.ViewHolder;
import com.xxzlkj.shop.weight.MyMarqueeView;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.BaseFragmentPagerAdapter;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnBaseRequestListener;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.BasePopupWindow;
import com.xxzlkj.zhaolinshare.base.util.DialogBuilder;
import com.xxzlkj.zhaolinshare.base.util.GaodeLocationUtil;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PermissionHelper;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;
import com.xxzlkj.zhaolinshare.base.util.PreferencesSaver;
import com.xxzlkj.zhaolinshare.base.util.SystemBarUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 描述:商城首页
 *
 * @author zhangrq
 *         2017/6/26 9:48
 */
@SuppressWarnings({"DefaultAnnotationParam", "unused", "Convert2streamapi"})
public class ShopHomeActivity extends BaseActivity {
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final String TYPE = "titleType";
    private ViewPager viewPager;
    private MagicIndicator magicIndicator;
    private List<Fragment> fragmentList = new ArrayList<>();
    private ImageView iv_home, iv_classify, iv_cart, iv_me, iv_me_2, iv_cart_2, iv_back;
    private MyMarqueeView mMarqueeViewHintAddress;
    private TextView tv_message_number;
    private ViewGroup oneFragmentButtonLayout;
    private RelativeLayout mRelativeLayout;
    private AppBarLayout mAppBarLayout;
    private RelativeLayout mAlphaRelativeLayout;
    private ViewGroup vg_location_no, vg_shop_server_no;
    private TextView mLocationTextView;
    // 收货地址
    private BasePopupWindow mPopupWindow;
    private ListView mListView;
    private RelativeLayout mNoAddressLayout;
    private RelativeLayout mLocationLayout;
    private TextView mNewAddAddressTextView;
    private RelativeLayout twoFragmentButtonLayout;
    private TextView mShopCartNumber;
    private CommBaseAdapter<AddressBean.DataBean> mAddressAdapter;
    private View.OnClickListener locationTextViewOnClickListener;
    // 默认选中
    private int index = -1;
    private final int PERMISSION_REQUEST_CODE = 123;
    private String titleType;
    private static View contentView;
    // 当前小区是否开通商城服务
    private boolean isDredgeMallService;
    private Dialog dialog;
    private PermissionHelper mHelper;

    /**
     * @param type 可以为null 为空代表跳到第一个 2 火爆预售;3 兆邻团购 ;4 果蔬生鲜
     */
    public static Intent newIntent(Context context, String type) {
        Intent intent = new Intent(context, ShopHomeActivity.class);
        intent.putExtra(TYPE, type);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        // 复用原来的 View
//        if (contentView == null) {
//            contentView = View.inflate(this, R.layout.activity_shop_home, null);
//        } else {
//            ViewGroup parent = (ViewGroup) contentView.getParent();
//            if (parent != null) {
//                parent.removeView(contentView);
//            }
//        }
        setContentView(R.layout.activity_shop_home);

        SystemBarUtils.setStatusBarColor(this, Color.BLACK);
    }

    @Override
    protected void findViewById() {
        EventBus.getDefault().register(this);
        // 头
        mAppBarLayout = getView(R.id.appBarLayout);
        mRelativeLayout = getView(R.id.id_shop_home_address_layout);
        mLocationTextView = getView(R.id.id_shop_home_location);
        // tab
        magicIndicator = getView(R.id.magic_indicator);
        // 内容
        viewPager = getView(R.id.viewPager);
        // popupWindow显示和隐藏背景变化
        mAlphaRelativeLayout = getView(R.id.id_shop_home_alph);
        mAlphaRelativeLayout.setAlpha(0);
        // 不在配送中布局
        mMarqueeViewHintAddress = getView(R.id.mv_hint_address);
        // 定位失败布局
        vg_location_no = getView(R.id.vg_location_no);
        // 所在小区暂无开通商城服务布局
        vg_shop_server_no = getView(R.id.vg_shop_server_no);

        // 一级界面按钮布局
        oneFragmentButtonLayout = getView(R.id.vg_root);
        iv_home = getView(R.id.iv_home);
        iv_classify = getView(R.id.iv_classify);
        iv_cart = getView(R.id.iv_cart);
        tv_message_number = getView(R.id.tv_message_number);
        iv_me = getView(R.id.iv_me);

        // 二级界面按钮布局
        twoFragmentButtonLayout = getView(R.id.rl_bottom_shop);
        iv_back = getView(R.id.iv_back);// 返回
        iv_cart_2 = getView(R.id.iv_cart_2);// 购物车按钮
        mShopCartNumber = getView(R.id.tv_message_number_2);// 购物车数量
        iv_me_2 = getView(R.id.iv_me_2);// 我的


        // 初始化popupWindow
        initPopWindow();
    }

    @Override
    public void setListener() {
        // 头右上角按钮
        getView(R.id.iv_title_right_search).setOnClickListener(v -> {
            if (isDredgeMallService) {
                startActivity(HotSearchActivity.newIntent(mContext));
            } else {
                // 弹框
                if (dialog != null) {
                    dialog.show();
                }
            }

        });// 搜索按钮
        // 定位失败布局按钮点击监听
        getView(R.id.tv_location_btn).setOnClickListener(v -> startActivity(LocationActivity.newIntent(mContext, 2)));// 手动定位按钮
        getView(R.id.tv_setting_btn).setOnClickListener(v -> startAppSettings());// 设置定位按钮
        // 一级页面按钮
        oneFragmentButtonLayout.setOnClickListener(v -> {
        });// 按钮点击不处理
        iv_home.setOnClickListener(v -> finish());// 首页
        iv_classify.setOnClickListener(v -> {
            if (isDredgeMallService) {
                // 分类
                int currentItem = viewPager.getCurrentItem();
                if (fragmentList.size() > currentItem
                        && fragmentList.get(currentItem) instanceof ShopHomeTabFragment
                        && !TextUtils.isEmpty(((ShopHomeTabFragment) fragmentList.get(currentItem)).getType())
                        && !TextUtils.isEmpty(GlobalParams.storeId)) {
                    String type = ((ShopHomeTabFragment) fragmentList.get(currentItem)).getType();
                    // 根据TitleType跳到对应全部分类
                    jumpToSpeedGoodsCategoryActivityByType(this, type, GlobalParams.storeId);

                } else {
                    ToastManager.showShortToast(mContext, "请检查网络或重新选择地址");
                }
            } else {
                // 弹框
                if (dialog != null) {
                    dialog.show();
                }
            }

        });
        iv_cart.setOnClickListener(v -> startActivity(ShopCartActivity.newIntent(mContext)));// 购物车
        iv_me.setOnClickListener(v -> ActivityUtils.finishToMainTabActivityTab(this, 4));// 我的
        // 二级页面按钮
        twoFragmentButtonLayout.setOnClickListener(v -> {
        });// 按钮点击不处理
        iv_back.setOnClickListener(v -> onBackPressed());// 返回按钮
        iv_cart_2.setOnClickListener(v -> startActivity(ShopCartActivity.newIntent(mContext)));// 购物车按钮
        iv_me_2.setOnClickListener(v -> ActivityUtils.finishToMainTabActivityTab(this, 4));// 我的
        // 设置AppBarLayout的监听
        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            // 下面滚动了，设置隐藏
            if (Math.abs(verticalOffset) > 0) {
                setAddressHintVisibility(false);
            }
        });
        // 配送至XXX按钮
        locationTextViewOnClickListener = v -> {
            // 展示收货地址
            showLocationPopupWindow();

        };

        mLocationTextView.setOnClickListener(locationTextViewOnClickListener);

        // PopWindow 选择定位地址点击
        mLocationLayout.setOnClickListener(v -> {
            startActivity(LocationActivity.newIntent(mContext, 2));
            mPopupWindow.dismiss();
            mAlphaRelativeLayout.setAlpha(0);
        });// 定位
        // PopWindow 列表Item点击
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            mPopupWindow.dismiss();
            mAlphaRelativeLayout.setAlpha(0);
            AddressBean.DataBean item = mAddressAdapter.getItem(position);
            // 保存地址信息
            ZLPreferencesUtils.saveAddressInfo(mContext, item);
            // 设置配送地址、内容数据
//            setAddressTitle(item.getAddress(), NumberFormatUtils.toDouble(item.getLongitude()), NumberFormatUtils.toDouble(item.getLatitude()));
//            checkAddressAndRefreshCurrentFragment(item.getId(), NumberFormatUtils.toDouble(item.getLongitude()), NumberFormatUtils.toDouble(item.getLatitude()));
            // 目前全部刷新，不部分刷新
            setAllViewDataByLocation(item.getAddress(), NumberFormatUtils.toDouble(item.getLongitude()), NumberFormatUtils.toDouble(item.getLatitude()));

            // 刷新app首页
            EventBus.getDefault().postSticky(new Home4AddressEvent(true, item));
        });
        // PopWindow 增加地址点击
        mNewAddAddressTextView.setOnClickListener(v -> {
            mPopupWindow.dismiss();
            mAlphaRelativeLayout.setAlpha(0);
            startActivity(EditHarvestAddressActivity.newIntent(mContext, 1, null, 3));
        });
        // 添加地址
        mNoAddressLayout.setOnClickListener(v -> startActivity(EditHarvestAddressActivity.newIntent(mContext, 1, null, 3)));// 添加地址
        // ViewPager
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 隐藏地址超出范围提示
                setAddressHintVisibility(false);
                // 只有ShopHomeTabFragment 的布局才有按钮
                Fragment fragment = fragmentList.get(position);
                if (fragment instanceof ShopHomeTabFragment) {
                    // ShopHomeTabFragment 判断显示按钮一、按钮二
                    setCurrentFragment(((ShopHomeTabFragment) fragment).isGoTop());
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 根据TitleType跳到对应全部分类
     *
     * @param type 5商城首页 2 火爆预售;3 兆邻团购 ;
     */
    public static void jumpToSpeedGoodsCategoryActivityByType(Activity activity, String type, String storeId) {
        // 5商城首页;2火爆预售;3 兆邻团购 ;
        String currentTitleId = "0";
        if ("5".equals(type)) {
            // 5商城首页
            currentTitleId = "0";
        } else if ("2".equals(type)) {
            // 2火爆预售
            currentTitleId = "-1";
        } else if ("3".equals(type)) {
            // 3兆邻团购
            currentTitleId = "-2";
        }
        // 跳到商品分类
        activity.startActivity(SpeedGoodsCategoryActivity.newIntent(activity.getApplicationContext(), storeId, currentTitleId));
    }

    private void showLocationPopupWindow() {
        if (BaseApplication.getInstance().getLoginUser() != null) {
            mPopupWindow.showAsDropDown(mRelativeLayout, 0, 1);
            mAlphaRelativeLayout.setAlpha(0.5f);
            // 得到地址列表默认选中的下坐标
            if (mAddressAdapter != null && mAddressAdapter.getCount() > 0) {
                String stringAttr = PreferencesSaver.getStringAttr(mContext, StringConstants.PREFERENCES_MY_ADDRESS_ID);
                if (TextUtils.isEmpty(stringAttr)) {
                    // 没有选中
                    index = -1;
                } else {
                    // 遍历找到选中下坐标
                    for (int i = 0; i < mAddressAdapter.getCount(); i++) {
                        AddressBean.DataBean dataBean = mAddressAdapter.getItem(i);
                        if (stringAttr.equals(dataBean.getId())) {
                            index = i;
                            break;
                        }
                    }
                }

                // 设置地址列表默认选中
                mListView.setItemChecked(index, true);
            }

        } else {
            startActivity(LocationActivity.newIntent(mContext, 2));
        }
    }


    @Override
    public void processLogic() {
        // 获取type，跳到某个栏目
        titleType = getIntent().getStringExtra(TYPE);

        // 如果为"" 每次进来都用定位地址 不为""：每次进来都用自己选中的地址
        String localSaveAddress = PreferencesSaver.getStringAttr(mContext, StringConstants.PREFERENCES_MY_ADDRESS);
        if (TextUtils.isEmpty(localSaveAddress)) {
            // 如果为null 每次进来都用定位地址
            getLocationAndSetAllViewData();
        } else {
            // 不为null：每次进来都用自己选中的地址
            double longitude = NumberFormatUtils.toDouble(PreferencesSaver.getStringAttr(mContext, StringConstants.PREFERENCES_MY_LONGITUDE));// 地址经度
            double latitude = NumberFormatUtils.toDouble(PreferencesSaver.getStringAttr(mContext, StringConstants.PREFERENCES_MY_LATITUDE));// 地址纬度
            // 设置此页面的所有信息
            setAllViewDataByLocation(localSaveAddress, longitude, latitude);
        }
    }


    // 获取定位的
    private void getLocationAndSetAllViewData() {
        if (GlobalParams.longitude == 0 || GlobalParams.latitude == 0) {
            // 之前定位失败，重新定位获取
            mHelper = new PermissionHelper(this);
            mHelper.requestPermissions("请授予[位置]权限，否则无法获取附近小区", new PermissionHelper.PermissionListener() {
                        @Override
                        public void doAfterGrand(String... permission) {
                            // 请求权限成功
                            final GaodeLocationUtil gaodeLocationUtil = new GaodeLocationUtil();
                            AMapLocationClientOption defaultOption = gaodeLocationUtil.getDefaultOption();
                            gaodeLocationUtil.initLocation(mContext, defaultOption, aMapLocation -> {
                                double longitude = aMapLocation.getLongitude();
                                double latitude = aMapLocation.getLatitude();
                                // 全局保存经纬度
                                GlobalParams.longitude = longitude;
                                GlobalParams.latitude = latitude;
                                // 重新设置所有数据
                                setAllViewDataByLocation(null, GlobalParams.longitude, GlobalParams.latitude);

                                // 只定位一次
                                gaodeLocationUtil.stopLocation();
                                gaodeLocationUtil.destroyLocation();
                            });
                            gaodeLocationUtil.startLocation();
                        }

                        @Override
                        public void doAfterDenied(String... permission) {
                            // 设置失败
                            String localSaveAddress = PreferencesSaver.getStringAttr(mContext, StringConstants.PREFERENCES_MY_ADDRESS);
                            setAllViewDataByLocation(localSaveAddress, 0, 0);
                        }
                    }, android.Manifest.permission.ACCESS_COARSE_LOCATION
                    , android.Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            // 定位成功，设置值
            setAllViewDataByLocation(null, GlobalParams.longitude, GlobalParams.latitude);
        }

    }

    //直接把参数交给mHelper就行了
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 根据定位的地址名称、经度、纬度 设置地址Title、内容
     *
     * @param locationAddressName 定位地址名称，可为null ，为空则通过经度、纬度 来搜索设置地址Title
     * @param longitude           经度
     * @param latitude            纬度
     */
    public void setAllViewDataByLocation(String locationAddressName, double longitude, double latitude) {
        if (longitude == 0 || latitude == 0) {
            // 定位失败
            setLocationErrorView(longitude, latitude);
            // 获取一级标题所有数据
            getOneTabsByNet(false, longitude, latitude);
        } else {
            // 定位成功
            setLocationSuccessView(locationAddressName, longitude, latitude);
            // 设置内容
            // 检查地址距离,并获取一级标题所有数据
            String addressId = PreferencesSaver.getStringAttr(mContext, StringConstants.PREFERENCES_MY_ADDRESS_ID);
            // 检查距离，成功后，获取一级tab，显示内容
            checkAddress(() -> getOneTabsByNet(true, longitude, latitude), addressId, longitude, latitude);
        }

    }

    private void setLocationSuccessView(String locationAddressName, double longitude, double latitude) {
        // 定位成功
        vg_location_no.setVisibility(View.GONE);
        setOneFragmentButtonIsShow(true);
        setAddressTitle(locationAddressName, longitude, latitude);


    }

    private void setAddressTitle(String locationAddressName, double longitude, double latitude) {
        // 设置头
        if (TextUtils.isEmpty(locationAddressName)) {
            // 定位地址为空，通过经纬度设置
            setAddressTitleByLocation(longitude, latitude);
        } else {
            // 定位地址不为空，设置
            mLocationTextView.setText("配送至：" + locationAddressName);
        }
    }

    private void setLocationErrorView(double longitude, double latitude) {
        mLocationTextView.setText("配送至：获取定位失败");
        mLocationTextView.setOnClickListener(locationTextViewOnClickListener);
        vg_location_no.setVisibility(View.VISIBLE);
        setOneFragmentButtonIsShow(false);
    }

    /**
     * 检查地址距离
     *
     * @param addressId 有地址id，优先id，如没有则用经度、纬度获取
     */
    private void checkAddress(OnCheckAddressSuccessListener listener, String addressId, double longitude, double latitude) {
        HashMap<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(addressId)) {
            map.put(URLConstants.REQUEST_PARAM_ID, addressId);
        }
        map.put("longitude", String.valueOf(longitude));
        map.put("latitude", String.valueOf(latitude));
        map.put("type", "shop");// 首页传index，商城首页传shop，其他不传
        RequestManager.createRequest(URLConstants.REQUEST_CHECK_DISTANCE, map, new OnBaseRequestListener<CheckDistanceBean>() {

            @Override
            public void handlerSuccess(CheckDistanceBean bean) {
                setAddressHintVisibility(true);// 展示提示
                List<String> marqueeViewList = new ArrayList<>();
                List<Integer> imageIds = new ArrayList<>();
                if ("200".equals(bean.getCode())) {
                    // 200	表示成功:展示Title提示(XXXXX为您服务)
                    marqueeViewList.add(bean.getData().getTitle());
                    imageIds.add(R.mipmap.ic_shop_home_success);
                    mMarqueeViewHintAddress.setOnItemChangeListener(null);// 取消滚动改变监听
                    // 不展示所在小区暂未开通商城服务
                    vg_shop_server_no.setVisibility(View.GONE);
                    // 开通商城服务
                    isDredgeMallService = true;
                    if (listener != null) {
                        listener.onCheckAddressSuccess();
                    }
                } else {
                    // 其余失败，切换提示
                    // 先增加错误提示，后展示当前门店
                    marqueeViewList.add(bean.getMessage());
                    marqueeViewList.add(bean.getData().getTitle());
                    imageIds.add(R.mipmap.ic_shop_home_error);
                    imageIds.add(R.mipmap.ic_shop_home_current);
                    // 弹框 并 展示所在小区暂未开通商城服务
                    setTabIsShow(false);
                    vg_shop_server_no.setVisibility(View.VISIBLE);
                    // 没有开通商城服务
                    isDredgeMallService = false;
                    // 弹框
                    if (dialog == null) {
                        dialog = ZLDialogUtil.zhaoLinGeneralDialog(ShopHomeActivity.this, R.mipmap.ic_dialog_shop_server_no, null, "当前地址所在小区暂无开通商城服务\n请切换地址", new ZLDialogUtil.OnGeneralClickListener() {
                            @Override
                            public void onLeftButtonClick(DialogBuilder dialog) {
                                dialog.dismiss();
                            }

                            @Override
                            public void onRightOrCenterButtonClick(DialogBuilder dialog) {
                                dialog.dismiss();
                                // 展示popwindow
                                showLocationPopupWindow();
                            }
                        }, "取消", "确定");
                    } else {
                        dialog.show();
                    }

                }
                mMarqueeViewHintAddress.setDrawableIds(imageIds);
//                mMarqueeViewHintAddress.postDelayed(() -> {
//                    mMarqueeViewHintAddress.startWithList(marqueeViewList);// 滚动
//                }, 500);
                mMarqueeViewHintAddress.setFlipInterval(3000);
                mMarqueeViewHintAddress.startWithList(marqueeViewList);// 滚动

                // 全局保存小区id、店铺id
                GlobalParams.communityId = bean.getData().getCommunity_id();
                GlobalParams.storeId = bean.getData().getStore_id();
                GlobalParams.isOpenLocalLife = "1".equals(bean.getData().getIs_cleaning());

            }

            @Override
            public void handlerError(int errorCode, String errorMessage) {
                ToastManager.showErrorToast(mContext, errorCode, errorMessage);
                // 没有开通商城服务
                isDredgeMallService = false;
            }

        });
    }

    public interface OnCheckAddressSuccessListener {
        void onCheckAddressSuccess();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 设置购物车数量（一级页面）
        ShopCartNumberUtils.getShopCartNumber(this, tv_message_number);
        // 设置购物车数量（二级页面）
        ShopCartNumberUtils.getShopCartNumber(this, mShopCartNumber);
        // 获取我的所有收货地址
        getMyAllAddress();
    }

    /**
     * 获取所有的一级 Titles
     *
     * @param isSetViewPagerContent 是否设置ViewPager内容
     */
    private void getOneTabsByNet(boolean isSetViewPagerContent, double longitude, double latitude) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        RequestManager.createRequest(URLConstants.SHOP_NAV_URL, stringStringHashMap, new OnMyActivityRequestListener<ShopNavBean>(this) {

            @Override
            public void onSuccess(ShopNavBean bean) {
                // 设置数据
                // 超过一个,设置显示tab，地址可滚动；否则，不显示tab、地址不可滚动
                boolean isShowTabs = bean.getData().size() > 1;

                setTabIsShow(isShowTabs);

                setOneTabsData(isSetViewPagerContent, longitude, latitude, bean.getData());
            }
        });
    }

    /**
     * 设置显示tab，地址可滚动；否则，不显示tab、地址不可滚动
     */
    private void setTabIsShow(boolean isShowTabs) {
        mRelativeLayout.setPadding(
                mRelativeLayout.getPaddingLeft(),
                mRelativeLayout.getPaddingTop(),
                mRelativeLayout.getPaddingRight(),
                isShowTabs ? PixelUtil.dip2px(mContext, 4) : PixelUtil.dip2px(mContext, 12));
        AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams) mRelativeLayout.getLayoutParams();
//                app:layout_scrollFlags="scroll|enterAlways|snap"
        layoutParams.setScrollFlags(isShowTabs ?
                AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS | AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
                : 0);
        magicIndicator.setVisibility(isShowTabs ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置添加Title对于的 Tab Fragment
     */
    private void setOneTabsData(boolean isSetViewPagerContent, double longitude, double latitude, List<ShopNavBean.DataBean> data) {
        // 指示器
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        ShopHomeCommonNavigatorAdapter indicatorAdapter = new ShopHomeCommonNavigatorAdapter(viewPager, data);
        commonNavigator.setAdjustMode(data.size() == 4);// 4个的时候，固定
        commonNavigator.setAdapter(indicatorAdapter);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);

        // 设置是否不显示内容布局
        if (isSetViewPagerContent) {
            // 设置内容
            fragmentList.clear();
            for (ShopNavBean.DataBean dataBean : data) {
                String type = dataBean.getType();
                // 5商城首页 2 火爆预售;3 兆邻团购 ;
                fragmentList.add(ShopHomeTabFragment.newInstance(type));// 由于一个type一个接口，所以只传type就ok
            }
            // 初始化viewPager和指示器
            BaseFragmentPagerAdapter fragmentPagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
            viewPager.setOffscreenPageLimit(fragmentList.size());// 设置预加载的数量
            viewPager.setAdapter(fragmentPagerAdapter);

            // 跳到某个tab位置
            if (!TextUtils.isEmpty(titleType)) {
                int index = getIndex(data, titleType);
                // 跳到这个位置上
                viewPager.setCurrentItem(index);
                titleType = null;
            }
        }


    }

    /**
     * 获得默认当前页下坐标
     */
    private int getIndex(List<ShopNavBean.DataBean> list, String titleType) {
        int index = 0;
        for (int i = 0; i < list.size(); i++) {
            ShopNavBean.DataBean itemBean = list.get(i);
            if (TextUtils.equals(titleType, itemBean.getType())) {
                index = i;
            }
        }
        return index;
    }

    /**
     * 设置刷新当前Fragment
     */
    private void setRefreshAllFragment() {
        if (viewPager == null) {
            return;
        }
        // 设置全部刷新
        for (Fragment fragment : fragmentList) {
            if (fragment instanceof ShopHomeTabFragment) {
                ShopHomeTabFragment shopHomeTabFragment = (ShopHomeTabFragment) fragment;
                shopHomeTabFragment.setRefresh();
            }
        }

//        // 设置内容，通知当前页面，其它页面，则通过显示的时候调用
//        int currentItem = viewPager.getCurrentItem();
//        if (fragmentList.size() > 0 && fragmentList.size() > currentItem + 1) {
//            // 通知当前页面刷新
//            Fragment currentFragment = fragmentList.get(currentItem);
//            // 设置不显示地址
//            setAddressHintVisibility(false);
//        }
    }

    /**
     * 根据经度、纬度 设置Title 地址
     */
    public void setAddressTitleByLocation(double mLongitude, double mLatitude) {
        // 第一个参数表示搜索字符串，第二个参数表示POI搜索类型二选其一
        // 第三个参数表示POI搜索区域的编码，必设
        PoiSearch.Query query = new PoiSearch.Query("", "");

        query.setPageSize(1);// 设置每页最多返回多少条poiitem
        query.setPageNum(0);// 设置查第0页
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                if (poiResult != null && poiResult.getPois() != null && poiResult.getPois().size() > 0) {
                    PoiItem poiItem = poiResult.getPois().get(0);
                    String title = poiItem.getTitle();
                    mLocationTextView.setText("配送至：" + title);
                    mLocationTextView.setOnClickListener(locationTextViewOnClickListener);
                }
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
        //设置搜索的范围
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(mLatitude, mLongitude), 2000));//设置周边搜索的中心点以及半径
        //开始搜索
        poiSearch.searchPOIAsyn();
    }

    /**
     * 初始化PopupWindow
     */
    private void initPopWindow() {
        final View inflate = View.inflate(mContext, R.layout.popup_wiondow_address, null);
        mListView = (ListView) inflate.findViewById(R.id.id_pa_list);
        mNoAddressLayout = (RelativeLayout) inflate.findViewById(R.id.id_pa_no_address_layout);
        mNewAddAddressTextView = (TextView) inflate.findViewById(R.id.id_pa_add_address);
        mLocationLayout = (RelativeLayout) inflate.findViewById(R.id.id_address_location_layout);
        mPopupWindow = new BasePopupWindow(inflate, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 点击外部
        mPopupWindow.setOnDismissListener(() -> mAlphaRelativeLayout.setAlpha(0));

    }

    /**
     * 获取我的所有收货地址
     */
    private void getMyAllAddress() {
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
                    mListView.setVisibility(View.VISIBLE);
                    mNoAddressLayout.setVisibility(View.GONE);
                    mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                    if (data.size() > 0 && data.size() <= 3) {
                        mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                    } else {
                        mPopupWindow.setHeight(DisplayUtil.getWindowWidth(ShopHomeActivity.this));
                    }

                    if (mAddressAdapter != null) {// 不为空
                        if (mAddressAdapter.getCount() > 0) {// 清除已有数据
                            mAddressAdapter.clearData();
                        }
                        // 添加新的集合
                        mAddressAdapter.addList(data);
                    } else {// 为空
                        mAddressAdapter = new CommBaseAdapter<AddressBean.DataBean>(getContext(), R.layout.adapter_address_item, data) {
                            @Override
                            protected void convert(ViewHolder viewHolder, AddressBean.DataBean item, int position) {
                                TextView mAddressTextView = viewHolder.getView(R.id.id_address_location);
                                TextView mNamePhoneTextView = viewHolder.getView(R.id.id_address_name_phone);
                                TextView mDefaultTextView = viewHolder.getView(R.id.id_address_default);
                                ImageView mEditImageView = viewHolder.getView(R.id.id_hai_edit);
                                mEditImageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mPopupWindow.dismiss();
                                        startActivity(EditHarvestAddressActivity.newIntent(mContext, 2, data.get(position).getId(), 3));
                                    }
                                });
                                // 收货地址
                                String address = item.getAddress();
                                if (!TextUtils.isEmpty(address)) {
                                    mAddressTextView.setText(address);
                                }
                                // 收货人和收货人手机号
                                String name = item.getName();
                                String phone = item.getPhone();
                                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone)) {
                                    mNamePhoneTextView.setText(name + "     " + phone);
                                }
                                // 是否是默认地址
                                String state = item.getState();
                                if ("2".equals(state)) {// 默认地址
                                    mDefaultTextView.setVisibility(View.VISIBLE);
                                } else {// 非默认地址
                                    mDefaultTextView.setVisibility(View.GONE);
                                }

                            }
                        };
                        mListView.setAdapter(mAddressAdapter);
                    }

                } else {
                    mNoAddressLayout.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
//                super.onFailed(isError, code, message);
                mNoAddressLayout.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 设置一级页面所有按钮是否显示
     */
    public void setOneFragmentButtonIsShow(boolean isShow) {
        if (oneFragmentButtonLayout != null)
            oneFragmentButtonLayout.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void setOneFragmentButtonAnimation(boolean isEnterAnimation) {
        if (oneFragmentButtonLayout != null) {
            if (isEnterAnimation) {
                // 进入0.2-1
                oneFragmentButtonLayout.animate().alpha(1f).setDuration(500).start();
            } else {
                // 退出1-0.2
                oneFragmentButtonLayout.animate().alpha(0.2f).setDuration(500).start();
            }
        }
    }

    /**
     * 设置二级页面购物车按钮是否显示
     */
    public void setTwoFragmentButtonIsShow(boolean isShow) {
        if (twoFragmentButtonLayout != null)
            twoFragmentButtonLayout.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置当前的Fragment
     *
     * @param isOneFragment 是否是OneFragment
     */
    public void setCurrentFragment(boolean isOneFragment) {
        setOneFragmentButtonIsShow(isOneFragment);
        setTwoFragmentButtonIsShow(!isOneFragment);
        // 在 one Fragment的时候，可以点击，在two Fragment的时候不可点击
//        mLocationTextView.setOnClickListener(isOneFragment ? locationTextViewOnClickListener : null);
    }

    public void setAddressHintVisibility(boolean isShow) {
        if (mMarqueeViewHintAddress != null) {
            mMarqueeViewHintAddress.setVisibility(isShow ? View.VISIBLE : View.GONE);
            if (!isShow) {
                // 不显示，停止滚动
                mMarqueeViewHintAddress.stopFlipping();
            }
        }
    }

    /**
     * activity的回退事件
     */
    @Override
    public void onBackPressed() {
        int currentItem = viewPager.getCurrentItem();
        if (fragmentList.size() > currentItem && fragmentList.get(currentItem) instanceof ShopHomeTabFragment) {
            // 当前页面为ShopHomeTabFragment 由它自己处理事件
            ShopHomeTabFragment fragment = (ShopHomeTabFragment) fragmentList.get(currentItem);
            fragment.onBackPressed();
        } else {
            super.onBackPressed();//执行activity回退
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 定位返回
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    public void setLocationAddress(ShopHomeLocationEvent event) {
        PoiItem poiItem = event.getPoiItem();
        if (poiItem != null) {
            index = -1;
            // 移除地址信息
            ZLPreferencesUtils.removeAddressInfo(mContext);
            // 设置配送地址、内容信息
            LatLonPoint latLonPoint = poiItem.getLatLonPoint();
            // 全局保存定位信息
            GlobalParams.longitude = latLonPoint.getLongitude();
            GlobalParams.latitude = latLonPoint.getLatitude();
            // 设置值
            setAllViewDataByLocation(poiItem.getTitle(), latLonPoint.getLongitude(), latLonPoint.getLatitude());
            // 检查距离，成功后，获取到了storeId 后刷新当前的Fragment
//            setAddressTitle(null, latLonPoint.getLongitude(), latLonPoint.getLatitude());
//            checkAddressAndRefreshCurrentFragment(null, latLonPoint.getLongitude(), latLonPoint.getLatitude());
        }
    }

    private void checkAddressAndRefreshCurrentFragment(String addressId, double longitude, double latitude) {
        // 检查地址
        checkAddress(this::setRefreshAllFragment, addressId, longitude, latitude);
    }


    /**
     * 地址变化
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    public void updateAddress(ShopHomeAddressEvent event) {
        if (event.isAddOrDelete()) {
            // 新增地址
            getMyAllAddress();
            AddressBean.DataBean dataBean = event.getDetaileAddress();
            if (dataBean == null) {
                // 删除地址
                /*String addressId = PreferencesSaver.getStringAttr(mContext, StringConstants.PREFERENCES_MY_ADDRESS_ID);
                if (addressId.equals(event.getAddressId())){
                    setAddressTitle(null,GlobalParams.longitude,GlobalParams.latitude);
                    checkAddressAndRefreshCurrentFragment(null, GlobalParams.longitude,GlobalParams.latitude);
                }*/
            } else {
                // 新增地址
//                setAddressTitle(dataBean.getAddress(), 0, 0);
//                checkAddressAndRefreshCurrentFragment(dataBean.getId(), 0, 0);
                // 目前全部刷新，不部分刷新
                ZLPreferencesUtils.saveAddressInfo(mContext, dataBean);
                setAllViewDataByLocation(dataBean.getAddress(), NumberFormatUtils.toDouble(dataBean.getLongitude()), NumberFormatUtils.toDouble(dataBean.getLatitude()));

            }

        } else {
            // 删除自己选中的地址
//            setAddressTitle(null, GlobalParams.longitude, GlobalParams.latitude);
//            checkAddressAndRefreshCurrentFragment(null, GlobalParams.longitude, GlobalParams.latitude);
            // 目前全部刷新，不部分刷新
            setAllViewDataByLocation(null, GlobalParams.longitude, GlobalParams.latitude);
            mListView.setItemChecked(-1, true);
            index = -1;
        }
    }

    /**
     * 添加购物车及动画
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    public void addGoodsToShopCart(AddShopCartActionEvent event) {
        View view = event.getView();
        Goods goods = event.getGoods();
        if (BaseApplication.getInstance().getLoginUserDoLogin(this) != null) {
            if (event.isFlag()) {
                AddShopCartUtil.addShopCart(ShopHomeActivity.this, view, tv_message_number, goods.getId());
            } else {
                AddShopCartUtil.addShopCart(ShopHomeActivity.this, view, mShopCartNumber, goods.getId());
            }
        }
    }

    /**
     * 购物车数量
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true) //在ui线程执行
    public void updataShopCart(DataChangEvent event) {
        ShopCartNumberUtils.getShopCartNumber(this, tv_message_number);
        ShopCartNumberUtils.getShopCartNumber(this, mShopCartNumber);
    }

    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + mContext.getPackageName()));
        startActivityForResult(intent, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            // 从设置页面回来的，重新的获取地址
            getLocationAndSetAllViewData();
        }
    }
}
