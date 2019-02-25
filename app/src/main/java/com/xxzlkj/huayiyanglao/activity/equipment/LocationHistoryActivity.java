package com.xxzlkj.huayiyanglao.activity.equipment;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.adapter.BeyondFenceAdapter;
import com.xxzlkj.huayiyanglao.adapter.LocationRecordAdapter;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.BeyondFenceBean;
import com.xxzlkj.huayiyanglao.model.LocationRecordListBean;
import com.xxzlkj.huayiyanglao.model.WatchPositionBean;
import com.xxzlkj.shop.weight.CustomButton;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PermissionHelper;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 描述:定位记录
 *
 * @author zhangrq
 *         2017/3/21 14:48
 */
public class LocationHistoryActivity extends MyBaseActivity implements AMap.OnCameraChangeListener, AMap.OnMapLoadedListener {
    public static final int DELETE_REQUEST_CODE = 500;
    public static final int FINISH_REQUEST_CODE = 600;
    public static final String IS_BIND_DEVICE = "is_bind_device";
    public static final String IMEI = "imei";
    public static final String PHONE = "phone";
    private CoordinatorLayout mCoordinatorLayout;
    private LinearLayout mLinearLayout;
    //    private ViewPager viewPager;
    private RelativeLayout rl_bottom_layout;
    private ImageView iv_drag, mCurrentLocationImageView, mWatchLocationImageView, mTipImageView;
    private TextView mTipTextView1, mTipTextView2, mBeyondFenceTextView, mLocationRecordTextView;
    private View mIndicatorView;
    private CustomButton mTipButton;
    private MapView mMapView;
    private AMap aMap;
    private WifiManager mWifiManager;
    private PermissionHelper permissionHelper;
    private AMapLocationClient mlocationClient = null;
    private LatLng mCurrentLocation, mWatchLatlng;
    private Marker mWatchMarker;
    private List<Marker> markers = new ArrayList<>();
    private boolean mIsClickCurrent, mIsClickWatch;
    private int state;
    private String imei;
    private String phone;
    private int dimensionPixelOffset;
    private float translationX1;
    private float translationX2;
    private ObjectAnimator animator1;
    private ObjectAnimator animator2;
    private ObjectAnimator animator3;
    private MyRecyclerView beyondFenceRecyclerView, locationRecordRecyclerView;
    private LocationRecordAdapter locationRecordAdapter;
    private BeyondFenceAdapter beyondFenceAdapter;
    private int page = 1;
    public List<LatLng> latLngs = new ArrayList<>();
    private AnimatorSet animatorSet;

    /**
     * @param state 0：未绑定设备、未申请监护 1：已绑定设备 2：已申请监护（申请中） 3：已申请监护（申请通过并且被监护者已经绑定） 4:已申请监护（申请通过并且被监护者未绑定设备）
     * @param imei  当state=2时用到
     * @param phone 当state=2时用到
     */
    public static Intent newIntent(Context context, int state, String imei, String phone) {
        Intent intent = new Intent(context, LocationHistoryActivity.class);
        intent.putExtra(IS_BIND_DEVICE, state);
        intent.putExtra(IMEI, imei);
        intent.putExtra(PHONE, phone);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_location_history);
    }

    @Override
    protected void findViewById() {
        mCoordinatorLayout = getView(R.id.id_coordinator_layout);// 展示地图和定位记录数据布局
        mLinearLayout = getView(R.id.id_layout_2);// 展示提示内容不仅
        mMapView = getView(R.id.id_location_mapview);// 地图布局
        rl_bottom_layout = getView(R.id.rl_bottom_layout);// 底部布局
        iv_drag = getView(R.id.iv_drag);// 拖拽按钮
        mBeyondFenceTextView = getView(R.id.id_beyond_fence);// 超出围栏
        mLocationRecordTextView = getView(R.id.id_location_record);// 定位记录
        mIndicatorView = getView(R.id.id_indicator);// 指示器
        mCurrentLocationImageView = getView(R.id.id_current_location);// 回到当前位置按钮
        mWatchLocationImageView = getView(R.id.id_watch_location);// 回到手表位置按钮
        beyondFenceRecyclerView = getView(R.id.id_recycler_view1);// 超出围栏列表
        beyondFenceRecyclerView.setPullRefreshAndLoadingMoreEnabled(false, true);
        beyondFenceRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        beyondFenceAdapter = new BeyondFenceAdapter(mContext, R.layout.adapter_beyond_fence_item);
        beyondFenceRecyclerView.setAdapter(beyondFenceAdapter);
        locationRecordRecyclerView = getView(R.id.id_recycler_view2);// 定位记录列表
        locationRecordRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        locationRecordAdapter = new LocationRecordAdapter(mContext, R.layout.adapter_location_record_item);
        locationRecordRecyclerView.setAdapter(locationRecordAdapter);
        mTipImageView = getView(R.id.id_tip_image);// 提示图片
        mTipTextView1 = getView(R.id.id_tip_text_1);// 提示文字1
        mTipTextView2 = getView(R.id.id_tip_text_2);// 提示文字2
        mTipButton = getView(R.id.id_tip_button);// 提示按钮
        dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.text_margin) + getResources().getDimensionPixelOffset(R.dimen.text_width);
        translationX1 = mBeyondFenceTextView.getTranslationX();
        translationX2 = mIndicatorView.getTranslationX();
        //初始化定位
        initLocation();
    }

    @Override
    protected void setListener() {
        BottomSheetBehavior<RelativeLayout> bottomSheetBehavior = BottomSheetBehavior.from(rl_bottom_layout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);// 设置当前状态为展开的
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    // 折叠状态，箭头向上，默认箭头向下
                    iv_drag.setRotation(180);
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    // 展开完全的状态，箭头向下
                    iv_drag.setRotation(0);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        // 回到当前位置
        mCurrentLocationImageView.setOnClickListener(v -> {
            if (mCurrentLocation != null) {
                //然后可以移动到定位点,使用animateCamera就有动画效果
                mIsClickCurrent = true;
                clickDealWith(mCurrentLocation);
            } else {
                ToastManager.showShortToast(mContext, "定位失败");
            }

        });
        // 回到手表位置
        mWatchLocationImageView.setOnClickListener(v -> watchPosition());
        // 设备管理
        mTipButton.setOnClickListener(v -> {
            // 跳转到设备管理
            startActivityForResult(DeviceManageActivity.newIntent(mContext, state, "", ""), FINISH_REQUEST_CODE);
        });
        animatorSet = new AnimatorSet();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mBeyondFenceTextView.setTextColor(ContextCompat.getColor(mContext, mLocationRecordTextView.getAlpha() == 1 ? R.color.text_8 : R.color.blue_54B1F8));
                mLocationRecordTextView.setTextColor(ContextCompat.getColor(mContext, mLocationRecordTextView.getAlpha() == 1 ? R.color.blue_54B1F8 : R.color.text_8));
            }
        });
        // 超出围栏
        mBeyondFenceTextView.setOnClickListener(v -> {
            if (mLocationRecordTextView.getAlpha() == 1) {
                // 定位记录已经显示，点击超出围栏归原
                animator1 = ObjectAnimator.ofFloat(mBeyondFenceTextView, "translationX", -dimensionPixelOffset, translationX1);
                animator2 = ObjectAnimator.ofFloat(mIndicatorView, "translationX", dimensionPixelOffset, translationX2);
                animator3 = ObjectAnimator.ofFloat(mLocationRecordTextView, "alpha", 1, 0);
                startAnimal();
                beyondFenceRecyclerView.setVisibility(View.VISIBLE);
                locationRecordRecyclerView.setVisibility(View.GONE);
                if (beyondFenceAdapter.selectPosition != -1) {
                    // 如果被选中就显示当前选中的所有定位记录
                    addMoreMarker(latLngs);
                } else {
                    // 未选中回到档当前定位位置
                    addMoreMarker(new ArrayList<>());
                }

            }

        });
        // 超出围栏选中
        beyondFenceAdapter.setOnCheckedListener(day -> {
            if (beyondFenceAdapter.selectPosition == -1) {
                // 取消选中
                addMoreMarker(new ArrayList<>());
            } else {
                // 选中
                // 获取所有定位点
                getWatchCaveatDayList(day);
            }

        });
        // 超出围栏列表点击事件
        beyondFenceAdapter.setOnItemClickListener((position, item) -> {
            if (mLocationRecordTextView.getAlpha() == 0) {
                // 定位记录没显示时，点击超出围栏item开始动画
                animator1 = ObjectAnimator.ofFloat(mBeyondFenceTextView, "translationX", translationX1, -dimensionPixelOffset);
                animator2 = ObjectAnimator.ofFloat(mIndicatorView, "translationX", translationX2, dimensionPixelOffset);
                animator3 = ObjectAnimator.ofFloat(mLocationRecordTextView, "alpha", 0, 1);
                startAnimal();
                beyondFenceRecyclerView.setVisibility(View.GONE);
                locationRecordRecyclerView.setVisibility(View.VISIBLE);
                // 获取所有定位点
                getWatchCaveatDayList(item.getDay());
            }
        });
        // 定位记录列表点击事件
        locationRecordAdapter.setOnItemClickListener((position, item) -> {
            // 只展示一个定位记录点
            List<LatLng> latLngs = new ArrayList<>();
            latLngs.add(new LatLng(NumberFormatUtils.toDouble(item.getLatitude()), NumberFormatUtils.toDouble(item.getLongitude())));
            addMoreMarker(latLngs);
            // 更新选中状态
            locationRecordAdapter.selectionPosition = position;
            locationRecordAdapter.notifyDataSetChanged();
        });
        // 刷新和加载
        beyondFenceRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getWatchCaveatDay();
            }

            @Override
            public void onLoadMore() {
                page = beyondFenceAdapter.getItemCount() / 20 + 1;
                getWatchCaveatDay();
            }
        });
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("定位记录");
        state = getIntent().getIntExtra(IS_BIND_DEVICE, 0);
        imei = getIntent().getStringExtra(IMEI);
        phone = getIntent().getStringExtra(PHONE);
        if (state == 1 || state == 3) {
            mCoordinatorLayout.setVisibility(View.VISIBLE);
            // 绑定设备
            setTitleRightText("设置");
            // 初始化布局
            getWatchCaveatDay();
        } else {
            // 未绑定设备
            mLinearLayout.setVisibility(View.VISIBLE);
            switch (state) {
                case 0:// 未绑定未监控
                    mTipImageView.setImageResource(R.mipmap.ic_device);
                    mTipTextView1.setText("您尚未绑定设备");
                    mTipTextView2.setText("请先进入设备管理去绑定设备");
                    mTipButton.setText("设备管理");
                    mTipButton.setVisibility(View.VISIBLE);
                    break;
                case 2:// 已申请监护（申请中）
                    mTipImageView.setImageResource(R.mipmap.ic_checking);
                    mTipTextView1.setText("请等待对方审核通过");
                    mTipTextView2.setText(String.format("%s:%s", TextUtils.isEmpty(imei) ? "手机号" : "监护设备号", TextUtils.isEmpty(imei) ? phone : imei));
                    break;
                case 4:// 已申请监护（申请通过并且被监护者未绑定设备）
                    mTipImageView.setImageResource(R.mipmap.ic_device);
                    mTipTextView1.setText("定位功能不能使用");
                    mTipTextView2.setText("您监护的人尚未绑定设备");
                    break;
            }
            mTipTextView1.setVisibility(View.VISIBLE);
            mTipTextView2.setVisibility(View.VISIBLE);

        }


    }

    @Override
    public void onTitleRightClick(View view) {
        super.onTitleRightClick(view);
        // 跳转到定位设置
        startActivityForResult(LocationSettingActivity.newIntent(mContext), DELETE_REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化地图
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setOnCameraChangeListener(this);
        aMap.setOnMapLoadedListener(this);

        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(
                BitmapDescriptorFactory.fromResource(R.mipmap.ic_blue_point));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        // 自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(0);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        // 此方法必须重写
        mMapView.onCreate(savedInstanceState);

        // 请求权限
        permissionHelper = new PermissionHelper(this);
        permissionHelper.requestPermissions("请授予[位置]权限，否则无法定位", new PermissionHelper.PermissionListener() {
                    @Override
                    public void doAfterGrand(String... permission) {
                        // 请求权限成功
                        startLocation();
                    }

                    @Override
                    public void doAfterDenied(String... permission) {
                        // 设置失败
                    }
                }, Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.ACCESS_FINE_LOCATION);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        stopLocation();
        destroyLocation();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }

    @Override
    public void onMapLoaded() {

    }


    private void startAnimal() {
        animatorSet.playTogether(animator1, animator2, animator3);
        animatorSet.start();
    }

    /**
     * 初始化定位，设置回调监听
     */
    private void initLocation() {
        //初始化client
        mlocationClient = new AMapLocationClient(this.getApplicationContext());
        // 设置定位监听
        mlocationClient.setLocationListener(locationListener);
    }

    /**
     * 设置定位参数
     *
     * @return 定位参数类
     */
    private AMapLocationClientOption getOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setLocationCacheEnable(false);//设置是否返回缓存中位置，默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        return mOption;
    }


    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (null != aMapLocation) {
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 16f));
                mCurrentLocation = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            } else {
                LogUtil.e("location", "定位失败，loc is null");
            }
        }
    };

    /**
     * 开始定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void startLocation() {
        //设置定位参数
        mlocationClient.setLocationOption(getOption());
        // 启动定位
        mlocationClient.startLocation();
    }

    /**
     * 停止定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void stopLocation() {
        // 停止定位
        mlocationClient.stopLocation();
    }

    /**
     * 销毁定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void destroyLocation() {
        if (null != mlocationClient) {
            /*
              如果AMapLocationClient是在当前Activity实例化的，
              在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mlocationClient.onDestroy();
            mlocationClient = null;
            mlocationClient = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 处理点击定位和手表按钮
     *
     * @param latLng 经纬度坐标
     */
    private void clickDealWith(LatLng latLng) {
        if (mIsClickCurrent && mIsClickWatch) {
            // 重置
            mIsClickCurrent = false;
            mIsClickWatch = false;
            showCurrentAndWatchPoint();
        } else {
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19f));
        }
    }

    /**
     * 把当前定位和手表位置都显示出来
     */
    private void showCurrentAndWatchPoint() {
        if (mCurrentLocation != null && mWatchLatlng != null) {
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();//存放所有点的经纬度
            boundsBuilder.include(mCurrentLocation);//把所有点都include进去（LatLng类型）
            boundsBuilder.include(mWatchLatlng);//把所有点都include进去（LatLng类型）
            aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 100));//第二个参数为四周留空宽度
        }

    }

    /**
     * 添加单个Marker
     */
    private void addSingleMarker(LatLng latLng) {
        MarkerOptions markerOption = new MarkerOptions().draggable(true);
        markerOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_watch));
        if (mWatchMarker != null) {
            // 先移除
            mWatchMarker.remove();
        }
        mWatchMarker = aMap.addMarker(markerOption);
        mWatchMarker.setPosition(latLng);
    }

    /**
     * 添加多个Marker
     *
     * @param latLngs 坐标集合
     */
    public void addMoreMarker(List<LatLng> latLngs) {
        MarkerOptions markerOption = new MarkerOptions().draggable(true);
        markerOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_people));
        // 移除所有
        if (markers.size() > 0) {
            for (int i = 0; i < markers.size(); i++) {
                // 先移除
                markers.get(i).remove();
            }
        }
        // 添加
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();//存放所有点的经纬度
        for (int i = 0; i < latLngs.size(); i++) {
            Marker marker = aMap.addMarker(markerOption);
            LatLng latLng = latLngs.get(i);
            marker.setPosition(latLng);
            markers.add(marker);
            boundsBuilder.include(latLng);//把所有点都include进去（LatLng类型）

        }
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 100));//第二个参数为四周留空宽度
        // 回到当前位置
        if (latLngs.size() == 0)
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLocation, 19f));

    }


    /**
     * 手表最后位置
     */
    private void watchPosition() {
        Map<String, String> map = new HashMap<>();
        User loginUserDoLogin = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (loginUserDoLogin == null) {
            finish();
            return;
        }
        // 用户id
        map.put(ZLConstants.Params.UID, loginUserDoLogin.getData().getId());
        RequestManager.createRequest(ZLURLConstants.WATCH_POSITION_URL, map, new OnMyActivityRequestListener<WatchPositionBean>(this) {
            @Override
            public void onSuccess(WatchPositionBean bean) {
                mWatchLatlng = new LatLng(NumberFormatUtils.toDouble(bean.getData().getLatitude()), NumberFormatUtils.toDouble(bean.getData().getLongitude()));
                addSingleMarker(mWatchLatlng);
                watchClick();
            }

            @Override
            public void handlerStart() {

            }

            @Override
            public void handlerEnd() {

            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                if (mWatchLatlng != null)
                    addSingleMarker(mWatchLatlng);
                watchClick();
            }
        });
    }

    /**
     * 手表图标点击
     */
    private void watchClick() {
        if (mWatchLatlng != null) {
            //然后可以移动到定位点,使用animateCamera就有动画效果
            mIsClickWatch = true;
            clickDealWith(mWatchLatlng);
        } else {
            ToastManager.showShortToast(mContext, "暂未获取到手表位置");
        }
    }

    /**
     * 位置定位（按天）
     */
    private void getWatchCaveatDay() {
        HashMap<String, String> map = new HashMap<>();
        User user = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (user == null) {
            finish();
            return;
        }
        // 用户id
        map.put("uid", user.getData().getId());
        map.put("page", page + "");
        RequestManager.createRequest(ZLURLConstants.WATCH_CAVEAT_DAY_URL, map, new OnMyActivityRequestListener<BeyondFenceBean>(this) {
            @Override
            public void onSuccess(BeyondFenceBean bean) {
                // 设置数据
                beyondFenceRecyclerView.handlerSuccessHasRefreshAndLoad(beyondFenceAdapter, page == 1, bean.getData());
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                super.onFailed(isError, code, message);
                beyondFenceRecyclerView.getxRecyclerView().refreshComplete();
                beyondFenceRecyclerView.getxRecyclerView().loadMoreComplete();
            }
        });
    }

    /**
     * 手表警告位置列表
     *
     * @param day 天
     */
    private void getWatchCaveatDayList(String day) {
        HashMap<String, String> map = new HashMap<>();
        User user = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (user == null) {
            finish();
            return;
        }
        // 用户id
        map.put("uid", user.getData().getId());
        // 天
        map.put("day", day);
        RequestManager.createRequest(ZLURLConstants.WATCH_CAVEAT_DAY_LIST_URL, map, new OnMyActivityRequestListener<LocationRecordListBean>((BaseActivity) this) {
            @Override
            public void onSuccess(LocationRecordListBean bean) {
                List<LocationRecordListBean.DataBean> data = bean.getData();
                // 先清除
                if (latLngs.size() > 0)
                    latLngs.clear();
                // 遍历获取位置记录坐标集合
                for (int i = 0; i < data.size(); i++) {
                    LocationRecordListBean.DataBean item = data.get(i);
                    latLngs.add(new LatLng(NumberFormatUtils.toDouble(item.getLatitude()), NumberFormatUtils.toDouble(item.getLongitude())));
                }
                // 显示所有定位覆盖物
                addMoreMarker(latLngs);
                // 设置数据
                locationRecordRecyclerView.handlerSuccessHasRefreshAndLoad(locationRecordAdapter, true, bean.getData());
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                super.onFailed(isError, code, message);
                // 清空定位集合
                latLngs.clear();
                locationRecordAdapter.clear();
                // 清空定位覆盖物
                addMoreMarker(latLngs);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == DELETE_REQUEST_CODE) {
                // 清空所有数据
                page = 1;
                beyondFenceAdapter.clear();
                locationRecordAdapter.clear();
                latLngs.clear();
                // 清空定位覆盖物
                addMoreMarker(new ArrayList<>());
            } else if (requestCode == FINISH_REQUEST_CODE) {
                finish();
            }
        }
    }

}
