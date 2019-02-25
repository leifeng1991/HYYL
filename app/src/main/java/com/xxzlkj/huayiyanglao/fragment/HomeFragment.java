package com.xxzlkj.huayiyanglao.fragment;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.sunfusheng.marqueeview.DisplayUtil;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.xxzlkj.huayiyanglao.MainTabActivity;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.adapter.CommunityListAdapter;
import com.xxzlkj.huayiyanglao.adapter.HomeContentAdapter;
import com.xxzlkj.huayiyanglao.adapter.HomeMenuAdapter;
import com.xxzlkj.huayiyanglao.adapter.HomeTitleAdapter;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.GetCommunityListBean;
import com.xxzlkj.huayiyanglao.model.HomeAddressBean;
import com.xxzlkj.huayiyanglao.model.HomeIndexBean;
import com.xxzlkj.huayiyanglao.util.GaodeLocationUtil;
import com.xxzlkj.huayiyanglao.util.HomeRecyclerViewHelperListener;
import com.xxzlkj.huayiyanglao.util.PicassoImageLoader;
import com.xxzlkj.huayiyanglao.util.ZLActivityUtils;
import com.xxzlkj.shop.activity.address.EditHarvestAddressActivity;
import com.xxzlkj.shop.activity.address.LocationActivity;
import com.xxzlkj.shop.activity.shop.HotSearchActivity;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.event.Home4AddressEvent;
import com.xxzlkj.shop.event.ShopHomeLocationEvent;
import com.xxzlkj.shop.model.AddressBean;
import com.xxzlkj.shop.model.CheckDistanceBean;
import com.xxzlkj.shop.model.HarvestAddressList;
import com.xxzlkj.shop.utils.ZLPreferencesUtils;
import com.xxzlkj.shop.utils.listview.CommBaseAdapter;
import com.xxzlkj.shop.utils.listview.ViewHolder;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.ReuseViewFragment;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnBaseRequestListener;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.BasePopupWindow;
import com.xxzlkj.zhaolinshare.base.util.PermissionHelper;
import com.xxzlkj.zhaolinshare.base.util.PreferencesSaver;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 描述: 首页
 *
 * @author zhangrq
 *         2017/11/22 9:31
 */
public class HomeFragment extends ReuseViewFragment {
    private static final long LOOPER_PERIOD = 5000;
    private final int PERMISSION_REQUEST_CODE = 456;

    private RecyclerView mMenuRecyclerView, mTitleRecyclerView, mContentRecyclerView, recyclerView_opened_house;
    private ViewGroup ll_title;
    private AppBarLayout appbar;
    private HomeMenuAdapter mMenuAdapter;
    private GridLayoutManager mMenuLayoutManager;
    private HomeTitleAdapter mHomeTitleAdapter;
    private LinearLayoutManager linearLayoutManager;
    private HomeContentAdapter homeContentAdapter;
    private Banner mBanner;
    private TextView mLocationTextView;
    private ImageView iv_home_search, mMessageImageView;
    private View.OnClickListener locationTextViewOnClickListener;
    private int index;
    private PermissionHelper mHelper;
    private List<HomeAddressBean> marqueeDataList = new ArrayList<>();
    private ListView mListView;
    private RelativeLayout mNoAddressLayout;
    private TextView mNewAddAddressTextView;
    private RelativeLayout mLocationLayout;
    private BasePopupWindow mPopupWindow;
    private boolean isTitleStyleWhite;
    private int currentTitleStyle;
    private CommBaseAdapter<AddressBean.DataBean> mAddressAdapter;
    private Timer timer;
    private TimerTask timerTask;
    private CommunityListAdapter communityListAdapter;
    private ViewGroup vg_location, vg_location_no;
    private boolean isEnterShopHome;
    private int looperCount;
    private List<HomeIndexBean.DataBean.BannerBean> banner;
    private boolean isClickLeft;
    private SmartRefreshLayout mSmartRefreshLayout;
    private RelativeLayout mShadeRelativeLayout;
    private HomeRecyclerViewHelperListener contentRecyclerViewHelper;

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void findViewById() {
        mSmartRefreshLayout = getView(R.id.smartRefreshLayout);

        vg_location = getView(R.id.vg_location);// 定位成功布局

        appbar = getView(R.id.appbar);
        // 头部布局
        ll_title = getView(R.id.ll_title);// 头布局
        mLocationTextView = getView(R.id.tv_location);// 定位
        //搜索框布局
        iv_home_search = getView(R.id.iv_home_search);
        mMessageImageView = getView(R.id.id_home_image);
        // banner
        mBanner = getView(R.id.id_banner);
        mBanner.setImageLoader(new PicassoImageLoader());
        mMenuRecyclerView = getView(R.id.recyclerView_grid);// 宫格菜单
        mTitleRecyclerView = getView(R.id.id_title_recycler_view);// title
        mContentRecyclerView = getView(R.id.id_content_recycler_view);// 内容

        vg_location_no = getView(R.id.vg_location_no);// 定位失败布局
        recyclerView_opened_house = getView(R.id.recyclerView_opened_house);// 已开通小区

        // 初始化菜单
        mMenuRecyclerView.setNestedScrollingEnabled(false);
        mMenuLayoutManager = new GridLayoutManager(mContext, 2, LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, final int position) {

                LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return super.calculateSpeedPerPixel(displayMetrics) * 50;//返回滚过1px需要多少ms
                    }
                };
                linearSmoothScroller.setTargetPosition(position);
                startSmoothScroll(linearSmoothScroller);
            }
        };
        mMenuRecyclerView.setLayoutManager(mMenuLayoutManager);
        mMenuAdapter = new HomeMenuAdapter(mContext, R.layout.adapter_home_menu_item);
        mMenuRecyclerView.setAdapter(mMenuAdapter);
        // 初始化内容
        mTitleRecyclerView.setNestedScrollingEnabled(false);
        mTitleRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mHomeTitleAdapter = new HomeTitleAdapter(mContext, R.layout.adapter_home_title_list_item);
        mTitleRecyclerView.setAdapter(mHomeTitleAdapter);
        linearLayoutManager = new LinearLayoutManager(mContext);
        mContentRecyclerView.setLayoutManager(linearLayoutManager);
        homeContentAdapter = new HomeContentAdapter(mContext, mActivity);
        contentRecyclerViewHelper = new HomeRecyclerViewHelperListener(mContentRecyclerView, linearLayoutManager);
        mContentRecyclerView.addOnScrollListener(contentRecyclerViewHelper);
        mContentRecyclerView.setAdapter(homeContentAdapter);
        // Add the sticky headers decoration
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(homeContentAdapter);
        mContentRecyclerView.addItemDecoration(headersDecor);

        homeContentAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });

        initCommunityList();// 初始化失败布局的已开通小区
        initPopWindow();// 初始化popupWindow

        mShadeRelativeLayout = getView(R.id.id_shade);// 遮罩
        mShadeRelativeLayout.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void setListener() {
        EventBus.getDefault().register(this);

        // 定位失败布局按钮点击监听
        getView(R.id.tv_location_btn).setOnClickListener(v -> startActivity(LocationActivity.newIntent(mContext, 2)));// 手动定位按钮
        getView(R.id.tv_setting_btn).setOnClickListener(v -> startAppSettings());// 设置定位按钮
        iv_home_search.setOnClickListener(v -> {
            if (TextUtils.isEmpty(GlobalParams.storeId)) {
                ToastManager.showShortToast(mContext, getString(com.xxzlkj.shop.R.string.noShopServiceHint));
            } else {
                startActivity(HotSearchActivity.newIntent(mContext));
            }
        });// 搜索
        mMessageImageView.setOnClickListener(v -> ((MainTabActivity) mActivity).setCurrentTab(2));// 消息

        // 配送至XXX按钮
        locationTextViewOnClickListener = v -> {
            // 展示收货地址
            if (ZhaoLinApplication.getInstance().getLoginUser() != null) {
//                homeTitleHeight
                mPopupWindow.showAsDropDown(ll_title, 0, 0);// 去掉阴影高度展示
                mShadeRelativeLayout.setBackgroundColor(Color.argb(88, 0, 0, 0));
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

        };
        mLocationTextView.setOnClickListener(locationTextViewOnClickListener);// 定位地址

        // PopWindow 选择定位地址点击
        mLocationLayout.setOnClickListener(v -> {
            startActivity(LocationActivity.newIntent(mContext, 2));
            mPopupWindow.dismiss();
        });// 定位
        // PopWindow 列表Item点击
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            mPopupWindow.dismiss();
            AddressBean.DataBean item = mAddressAdapter.getItem(position);
            // 保存地址信息
            ZLPreferencesUtils.saveAddressInfo(mContext, item);
            // 设置配送地址、内容数据
            setAllViewDataByAddressIdOrLocation(item.getId(), 255, 0);

        });
        // PopWindow 增加地址点击
        mNewAddAddressTextView.setOnClickListener(v -> {
            mPopupWindow.dismiss();
            startActivity(EditHarvestAddressActivity.newIntent(mContext, 1, null, 2));
        });
        // 添加地址
        mNoAddressLayout.setOnClickListener(v -> startActivity(EditHarvestAddressActivity.newIntent(mContext, 1, null, 2)));// 添加地址
        // 失败布局，已开通小区点击
        communityListAdapter.setOnItemClickListener((position, item) -> {
            // 设置头
            setSuccessAddressTitle(item.getTitle(), 0, 0);
            // 设置成功的内容
            showSuccessLayout();
            // 显示内容
            checkAddressDistance(bean -> {
                // 设置内容
                // 获取当前小区的数据
                getTopData(bean.getData().getCommunity_id());

            }, null, 0, 0, item.getId());
        });


        // 设置菜单滚动
        mMenuRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            //用来标记是否正在向左滑动true / 右滑动false
            boolean isSlidingToLeftOrRight = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int firstVisibleItemPosition = mMenuLayoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = mMenuLayoutManager.findLastVisibleItemPosition();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (isSlidingToLeftOrRight) {
                        // 向左滑
                        mMenuRecyclerView.smoothScrollToPosition(lastVisibleItemPosition);
                    } else {
                        // 向右滑
                        mMenuRecyclerView.smoothScrollToPosition(firstVisibleItemPosition);
                    }
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isSlidingToLeftOrRight = dx > 0;
            }
        });
        // title的背景设置
        appbar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset == 0) {
                // 没垂直偏移量,展开状态
                ll_title.setBackgroundColor(Color.argb(0, 255, 255, 255));
                if (!isTitleStyleWhite)
                    setTitleStyle(true);

            } else if (Math.abs(verticalOffset) < appBarLayout.getTotalScrollRange()) {
                // 垂直偏移量小于全部滚动的值，折叠中状态
                float scale = (float) Math.abs(verticalOffset) / appBarLayout.getTotalScrollRange();
                int alpha = (int) (255 * scale);
                ll_title.setBackgroundColor(Color.argb(alpha, 255, 255, 255));//设置标题栏的透明度及颜色

                if (isTitleStyleWhite)
                    setTitleStyle(false);

            } else {
                // 垂直偏移量大于等于全部滚动的值，关闭状态
                ll_title.setBackgroundColor(Color.argb(255, 255, 255, 255));

                if (isTitleStyleWhite)
                    setTitleStyle(false);
            }
        });
        // 左侧item点击事件
        mHomeTitleAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<HomeIndexBean.DataBean.IndexBean>() {
            @Override
            public void onClick(int position, HomeIndexBean.DataBean.IndexBean item) {
                isClickLeft = true;
                // 改变选中item
                if (mHomeTitleAdapter.selectPosition != position) {
                    mHomeTitleAdapter.selectPosition = position;
                    // 右侧滑到对应的item
                    contentRecyclerViewHelper.scrollToPosition(position);
                    mHomeTitleAdapter.notifyDataSetChanged();
                }
            }
        });
        // 右边内容滚动监听事件
        mContentRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                // 改变左侧选中item
                if (mHomeTitleAdapter.selectPosition != firstVisibleItemPosition && !isClickLeft) {
                    mHomeTitleAdapter.selectPosition = firstVisibleItemPosition;
                    mTitleRecyclerView.scrollToPosition(firstVisibleItemPosition);
                    mHomeTitleAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                isClickLeft = false;
            }
        });
        // 宫格菜单item点击事件
        mMenuAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<HomeIndexBean.DataBean.MenuBean>() {
            @Override
            public void onClick(int position, HomeIndexBean.DataBean.MenuBean item) {
                // 跳转
                ZLActivityUtils.jumpToActivityByType(mActivity, item.getType(), item.getTo(), item.getToptitle());
            }
        });
        // banner跳转
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (banner != null) {
                    HomeIndexBean.DataBean.BannerBean bannerBean = banner.get(position);
                    if (bannerBean != null)
                        // 跳转
                        ZLActivityUtils.jumpToActivityByType(mActivity, bannerBean.getType(), bannerBean.getTo(), bannerBean.getToptitle());
                }

            }
        });
        // 刷新监听
        mSmartRefreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
        mSmartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            // 设置所有数据
            setAllViewData();
            mSmartRefreshLayout.finishRefresh();
        });
    }

    @Override
    public void processLogic() {
        // 开启轮训，title
        startLooperTitle();
        // 设置所有数据
        setAllViewData();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 获取我的所有的地址
        getMyAllAddress();
    }

    //如果你需要考虑更好的体验，可以这么操作
    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        mBanner.stopAutoPlay();
    }

    private void setAllViewData() {
        // 如果为"" 每次进来都用定位地址 不为""：每次进来都用自己选中的地址
        String localSaveAddressID = PreferencesSaver.getStringAttr(mContext, StringConstants.PREFERENCES_MY_ADDRESS_ID);
        if (TextUtils.isEmpty(localSaveAddressID)) {
            // 如果为null 代表没有选中本地的收货地址，每次进来都用定位地址
            getLocationAndSetAllViewData();
        } else {
            // 不为null：代表选中了本地的收货地址，每次进来都用自己选中的地址
            // 设置此页面的所有信息
            setAllViewDataByAddressIdOrLocation(localSaveAddressID, 0, 0);
        }
    }

    // 获取定位的
    private void getLocationAndSetAllViewData() {
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
                            setAllViewDataByAddressIdOrLocation(null, longitude, latitude);

                            // 只定位一次
                            gaodeLocationUtil.stopLocation();
                            gaodeLocationUtil.destroyLocation();
                        });
                        gaodeLocationUtil.startLocation();
                    }

                    @Override
                    public void doAfterDenied(String... permission) {
                        // 设置失败
                        String localSaveAddressID = PreferencesSaver.getStringAttr(mContext, StringConstants.PREFERENCES_MY_ADDRESS_ID);
                        setAllViewDataByAddressIdOrLocation(localSaveAddressID, 0, 0);
                    }
                }, Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.ACCESS_FINE_LOCATION);

    }

    //直接把参数交给mHelper就行了
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 根据定位的地址名称、经度、纬度 设置地址Title、内容
     *
     * @param addressID 定位地址ID，可为null ，为空则通过经度、纬度 来搜索设置地址Title
     * @param longitude 经度
     * @param latitude  纬度
     */
    public void setAllViewDataByAddressIdOrLocation(String addressID, double longitude, double latitude) {
        // 归原
        marqueeDataList.clear();// 清空数据

        // 定位失败，并且没选中小区，
        if (TextUtils.isEmpty(addressID) && (longitude == 0 || latitude == 0)) {
            // 没选择地址并且定位失败，显示定位失败和已开通小区列表
            setLocationErrorLayoutAndHouseList();
        } else {
            // 定位成功或者选择了地址
            // 设置成功的地址头
            String addressTitle = PreferencesSaver.getStringAttr(mContext, StringConstants.PREFERENCES_MY_ADDRESS);
            setSuccessAddressTitle(addressTitle, longitude, latitude);
            // 设置成功的内容
            showSuccessLayout();
            // 检查距离，成功后，获取community_id，显示内容
            checkAddressDistance(bean -> {
                // 设置内容
                // 获取当前小区的数据
                getTopData(bean.getData().getCommunity_id());

            }, addressID, longitude, latitude, null);
        }

    }

    /**
     * 检查地址距离
     *
     * @param addressId 有地址id，优先id，如没有则用经度、纬度获取
     */
    private void checkAddressDistance(OnCheckAddressSuccessListener listener, String addressId, double longitude, double latitude, String communityId) {
        HashMap<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(addressId)) {
            map.put(URLConstants.REQUEST_PARAM_ID, addressId);
        }
        map.put("longitude", String.valueOf(longitude));
        map.put("latitude", String.valueOf(latitude));
        if (!TextUtils.isEmpty(communityId))
            map.put("community_id", communityId);
        map.put("type", "index");// 首页传index，商城首页传shop，其他不传
        RequestManager.createRequest(URLConstants.REQUEST_CHECK_DISTANCE, map, new OnBaseRequestListener<CheckDistanceBean>() {

            @Override
            public void handlerSuccess(CheckDistanceBean bean) {
                // 表示成功
                if (listener != null) {
                    listener.onCheckAddressSuccess(bean);
                }
                if ("200".equals(bean.getCode())) {
                    // 200	表示成功:展示Title提示(当前服务小区：XXX)
                    marqueeDataList.add(new HomeAddressBean(bean.getData().getTitle(), 1));
                } else {
                    // 其余失败，切换提示
                    // 先增加错误提示，后展示当前门店
                    marqueeDataList.add(new HomeAddressBean(bean.getData().getTitle(), 2));
                }

                isEnterShopHome = !TextUtils.isEmpty(bean.getData().getStore_id());
                // 全局保存小区id、店铺id
                GlobalParams.communityId = bean.getData().getCommunity_id();
                GlobalParams.storeId = bean.getData().getStore_id();
                GlobalParams.isOpenLocalLife = "1".equals(bean.getData().getIs_cleaning());
            }

            @Override
            public void handlerError(int errorCode, String errorMessage) {
                ToastManager.showErrorToast(mContext, errorCode, errorMessage);
                isEnterShopHome = false;
            }

        });
    }

    /**
     * 设置小区失败布局和已开通小区
     */
    private void setLocationErrorLayoutAndHouseList() {
        marqueeDataList.add(new HomeAddressBean("服务地址：获取定位失败", 4));
//        mLocationTextView.setText("服务地址：获取定位失败");
        startLooperTitle();
        mLocationTextView.setOnClickListener(locationTextViewOnClickListener);
        // 展示失败布局
        showErrorLayout();
        // 获取已开通小区
        getCommunityList();
    }

    private void showErrorLayout() {
        vg_location_no.setVisibility(View.VISIBLE);
        mBanner.setVisibility(View.GONE);
        vg_location.setVisibility(View.GONE);
        // 设置黑色
        setTitleStyle(false);
    }

    private void showSuccessLayout() {
        vg_location_no.setVisibility(View.GONE);
        mBanner.setVisibility(View.VISIBLE);
        vg_location.setVisibility(View.VISIBLE);
    }

    /**
     * 获取已开通小区
     */
    private void getCommunityList() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        RequestManager.createRequest(URLConstants.GET_COMMUNITY_LIST_URL, stringStringHashMap, new OnMyActivityRequestListener<GetCommunityListBean>((BaseActivity) mActivity) {

            @Override
            public void onSuccess(GetCommunityListBean bean) {
                // 设置数据
                communityListAdapter.clearAndAddList(bean.getData());
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                super.onFailed(isError, code, message);
            }
        });
    }

    private void setSuccessAddressTitle(String locationAddressName, double longitude, double latitude) {
        // 设置头
        if (TextUtils.isEmpty(locationAddressName)) {
            // 定位地址为空，通过经纬度设置
            setAddressTitleByLocation(longitude, latitude);
        } else {
            // 定位地址不为空，设置
            marqueeDataList.add(new HomeAddressBean("服务地址：" + locationAddressName, 3));
//            mLocationTextView.setText(String.valueOf("配送至：" + locationAddressName));
            startLooperTitle();
        }
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
        PoiSearch poiSearch = new PoiSearch(mContext, query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                if (poiResult != null && poiResult.getPois() != null && poiResult.getPois().size() > 0) {
                    PoiItem poiItem = poiResult.getPois().get(0);
                    String title = poiItem.getTitle();
                    // 设置地址头
                    setSuccessAddressTitle(title, 0, 0);
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


    private void getTopData(String community_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("community_id", community_id);
        RequestManager.createRequest(ZLURLConstants.REQUEST_INDEX_URL, map, new OnMyActivityRequestListener<HomeIndexBean>((BaseActivity) mActivity) {
            @Override
            public void onSuccess(HomeIndexBean bean) {
                setData(bean);
            }
        });
    }

    private void setData(HomeIndexBean bean) {
        HomeIndexBean.DataBean data = bean.getData();
        if (data != null) {
            // banner
            banner = data.getBanner();
            if (banner != null && banner.size() > 0) {
                List<String> bannerUrl = new ArrayList<>();
                for (int i = 0; i < banner.size(); i++) {
                    bannerUrl.add(banner.get(i).getSimg());
                }
                mBanner.setImages(bannerUrl).start();
            }
            // 宫格菜单
            List<HomeIndexBean.DataBean.MenuBean> menu = data.getMenu();
            if (menu != null && menu.size() > 0) {
                // 显示宫格菜单
                mMenuRecyclerView.setVisibility(View.VISIBLE);
                mMenuAdapter.clearAndAddList(menu);
            } else {
                // 不显示宫格菜单
                mMenuRecyclerView.setVisibility(View.GONE);
            }

            // 设置左侧和右侧数据
            List<HomeIndexBean.DataBean.IndexBean> index = data.getIndex();
            mHomeTitleAdapter.clearAndAddList(index);
            homeContentAdapter.clear();
            homeContentAdapter.addAll(index);
        }
    }

    /**
     * 获取我的所有收货地址
     */
    private void getMyAllAddress() {
        HashMap<String, String> map = new HashMap<>();
        User user = ZhaoLinApplication.getInstance().getLoginUser();
        if (user == null) {
            return;
        }
        map.put(URLConstants.REQUEST_PARAM_UID, user.getData().getId());
        RequestManager.createRequest(URLConstants.REQUEST_MY_ADDRESS, map, new OnMyActivityRequestListener<HarvestAddressList>((BaseActivity) mActivity) {
            @Override
            public void handlerStart() {

            }

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
                        mPopupWindow.setHeight(DisplayUtil.getWindowWidth(getActivity()));
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
                                        mActivity.startActivity(EditHarvestAddressActivity.newIntent(mContext, 2, data.get(position).getId(), 2));
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


    private void initCommunityList() {
        recyclerView_opened_house.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView_opened_house.setNestedScrollingEnabled(false);
        communityListAdapter = new CommunityListAdapter(mContext, R.layout.item_get_community_list);
        recyclerView_opened_house.setAdapter(communityListAdapter);
    }

    /**
     * 开启Title轮训
     */
    private void startLooperTitle() {
        // 取消
        cancelLooperTitle();

        timer = new Timer();
        timerTask = new TimerTask() {

            @Override
            public void run() {
                looperCount++;
                int size = marqueeDataList.size();
                // 运行在主线程
                mLocationTextView.post(() -> {
                    if (size == 1) {
                        // 直接设置值，没有动画
                        HomeAddressBean homeAddressBean = marqueeDataList.get(0);
                        setTitleByStyle(homeAddressBean.getTitle(), homeAddressBean.getTitleStyle());
                    } else if (size > 1) {
                        // 动画设置值
                        HomeAddressBean homeAddressBean = marqueeDataList.get(looperCount % size);

                        ObjectAnimator.ofFloat(mLocationTextView, "alpha", 1f, 0f, 1f).setDuration(1000).start();
                        // 设置值
                        mLocationTextView.postDelayed(() -> setTitleByStyle(homeAddressBean.getTitle(), homeAddressBean.getTitleStyle()), 500);
                    }
                });

            }
        };
        // 常用于轮询
        timer.schedule(timerTask, 0, LOOPER_PERIOD);
    }


    /**
     * @param titleStyle 1表示成功：提示：（当前服务小区:XXX）；2表示失败：提示：（超出配送范围,已展示XXX服务）；3展示定位地址：4展示定位失败
     */
    private void setTitleByStyle(String title, int titleStyle) {
        // 设置内容
        mLocationTextView.setText(title);
        currentTitleStyle = titleStyle;
        // 设置样式
        setTitleStyle(isTitleStyleWhite);
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
        mPopupWindow = new BasePopupWindow(inflate, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.RED));
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mShadeRelativeLayout.setBackgroundColor(Color.TRANSPARENT);
            }
        });
    }

    private void setTitleStyle(boolean isTitleStyleWhite) {
        this.isTitleStyleWhite = isTitleStyleWhite;
        mLocationTextView.setTextColor(isTitleStyleWhite ? 0xffffffff : 0xff999999);
        // 设置左右两边的图片
        Drawable left = null;
        Drawable right = null;

//        1表示成功：提示：（当前服务小区:XXX）；2表示失败：提示：（超出配送范围,已展示XXX服务）；3展示定位地址：4展示定位失败
        switch (currentTitleStyle) {
            case 1:
                // 1表示成功：提示：（当前服务小区:XXX）,左--房子图片
                left = ContextCompat.getDrawable(mContext, isTitleStyleWhite ? R.mipmap.ic_home_house_white : R.mipmap.ic_home_house_black);
                right = null;
                break;
            case 2:
                // 2表示失败：提示：（超出配送范围,已展示XXX服务），左--叹号图片
                left = ContextCompat.getDrawable(mContext, isTitleStyleWhite ? R.mipmap.ic_home_error_white : R.mipmap.ic_home_error_black);
                right = null;
                break;
            case 3:
                // 3展示定位地址,左--定位图片，右--箭头
                left = ContextCompat.getDrawable(mContext, isTitleStyleWhite ? R.mipmap.ic_home_location_white : R.mipmap.ic_home_location_black);
                right = ContextCompat.getDrawable(mContext, isTitleStyleWhite ? R.mipmap.ic_home_down_white : R.mipmap.ic_home_down_black);
                break;
            case 4:
                // 4展示定位失败,左--房子图片
                left = ContextCompat.getDrawable(mContext, isTitleStyleWhite ? R.mipmap.ic_home_location_white : R.mipmap.ic_home_location_black);
                right = ContextCompat.getDrawable(mContext, isTitleStyleWhite ? R.mipmap.ic_home_down_white : R.mipmap.ic_home_down_black);
                break;
        }
        if (left != null)
            left.setBounds(0, 0, left.getIntrinsicWidth(), left.getIntrinsicHeight());
        if (right != null)
            right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
        mLocationTextView.setCompoundDrawables(left, null, right, null);
        // 设置右边
        iv_home_search.setImageResource(isTitleStyleWhite ? R.mipmap.ic_white_zoom : R.mipmap.search_home_icon);
//        mMessageImageView.setImageResource(isTitleStyleWhite ? R.mipmap.white_message : R.mipmap.gray_message);

    }

    public void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + mContext.getPackageName()));
        startActivityForResult(intent, PERMISSION_REQUEST_CODE);
    }

    private void cancelLooperTitle() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            // 从设置页面回来的，重新的获取地址
            getLocationAndSetAllViewData();
        }
    }

    interface OnCheckAddressSuccessListener {
        void onCheckAddressSuccess(CheckDistanceBean bean);
    }

    /**
     * 定位返回
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
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
            setAllViewDataByAddressIdOrLocation(null, latLonPoint.getLongitude(), latLonPoint.getLatitude());
        }
    }

    /**
     * 地址变化
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateAddress(Home4AddressEvent event) {
        if (event.isAddOrDelete()) {
            // 添加新的地址
            getMyAllAddress();
            AddressBean.DataBean dataBean = event.getBean();
            if (dataBean != null) {
                // 新增地址,用此地址作为默认的
                ZLPreferencesUtils.saveAddressInfo(mContext, dataBean);
                setAllViewDataByAddressIdOrLocation(dataBean.getId(), 0, 0);
            }

        } else {
            // 删除选中的地址
            setAllViewData();
            getMyAllAddress();
            mListView.setItemChecked(-1, true);
            index = -1;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        // 取消
        cancelLooperTitle();
    }

}
