package com.xxzlkj.shop.activity.address;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.xxzlkj.shop.R;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.event.LocationEvent;
import com.xxzlkj.shop.event.ShopHomeLocationEvent;
import com.xxzlkj.shop.utils.GaodeUtils;
import com.xxzlkj.shop.utils.listview.CommBaseAdapter;
import com.xxzlkj.shop.utils.listview.ViewHolder;
import com.xxzlkj.shop.weight.xlistview.XListView;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;
import com.xxzlkj.zhaolinshare.base.util.SystemBarUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * 高德定位
 */
public class LocationActivity extends CheckPermissionsActivity implements XListView.IXListViewListener, PoiSearch.OnPoiSearchListener, AMap.OnCameraChangeListener, AMap.OnMapLoadedListener {
    private MapView mMapView;
    private AMap aMap = null;
    private XListView mLocationListView;
    // 地址
    private CommBaseAdapter<PoiItem> mAdapter;
    // 页数
    private int mPage = 1;
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private AMapLocationClient mlocationClient = null;
    private Marker locationMarker;
    private LatLonPoint searchLatlonPoint;
    private WifiManager mWifiManager;
    private LatLng checkinpoint, mlocation;
    private String cityCode = "";
    // 默认为1 1:编辑地址界面 2：从商城首页跳进来
    private int fromJumpObjectType = 1;

    /**
     * @param type 1:编辑地址界面 2：从商城首页跳进来(必传)
     * @return
     */
    public static Intent newIntent(Context context, int type) {
        Intent intent = new Intent(context, LocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(StringConstants.INTENT_PARAM_TYPE, type);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);

        SystemBarUtils.setStatusBarColor(this, ContextCompat.getColor(mContext, R.color.title_bar_bg));
        SystemBarUtils.setStatusBarLightMode(this, true);
        findViewById(android.R.id.content).setBackgroundColor(ContextCompat.getColor(mContext, R.color.content_bg_zhaolin));

        EventBus.getDefault().register(this);

        mMapView = (MapView) findViewById(R.id.id_location_mapview);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setOnCameraChangeListener(this);
        aMap.setOnMapLoadedListener(this);

        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        // 此方法必须重写
        mMapView.onCreate(savedInstanceState);

    }

    /**
     * 初始化控件
     */
    protected void initMyView() {
        ImageView mLocationImageView = (ImageView) findViewById(R.id.id_location_icon);
        mLocationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlocation != null && locationMarker != null) {
                    //然后可以移动到定位点,使用animateCamera就有动画效果
                    aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mlocation, 16));
                }
            }
        });
        mLocationListView = (XListView) findViewById(R.id.id_location_list);
        mLocationListView.setPullRefreshEnable(false);
        mLocationListView.setPullLoadEnable(true);
        mLocationListView.setXListViewListener(this);

        ImageView leftImage = (ImageView) findViewById(R.id.iv_title_left);
        leftImage.setVisibility(View.VISIBLE);
        leftImage.setOnClickListener(v -> finish());
        TextView rightTextView = (TextView) findViewById(R.id.tv_title_right);
        rightTextView.setVisibility(View.VISIBLE);
        rightTextView.setText("确定 ");
        rightTextView.setOnClickListener(v -> {
            if (mAdapter != null && mAdapter.getCount() > 0) {
                int checkedItemPosition = mLocationListView.getCheckedItemPosition() - 1;
                if (mLocationListView.getCheckedItemPosition() != -1 && checkedItemPosition >= 0) {
                    PoiItem mPoiItem = mAdapter.getItem(checkedItemPosition);
                    switch (fromJumpObjectType) {
                        case 1: // 编辑地址
                            Intent intent = getIntent();
                            intent.putExtra(StringConstants.INTENT_PARAM_LOCATION, mPoiItem.getCityName() + mPoiItem.getAdName() + mPoiItem.getSnippet());
                            intent.putExtra(StringConstants.INTENT_PARAM_LATITUDE, mPoiItem.getLatLonPoint().getLatitude());
                            intent.putExtra(StringConstants.INTENT_PARAM_LONGITUDE, mPoiItem.getLatLonPoint().getLongitude());
                            setResult(RESULT_OK, intent);
                            break;
                        case 2: // 商城首页
                            // 商城首页定位
                            EventBus.getDefault().postSticky(new ShopHomeLocationEvent(mPoiItem));
                            break;
                    }

                    finish();
                }
                LogUtil.e("checkedItemPosition", checkedItemPosition + "");
            }
        });

        EditText mSearchEditText = (EditText) findViewById(R.id.id_location_search);
        mSearchEditText.setOnClickListener(v -> startActivity(LocationSearchActivity.newIntent(LocationActivity.this, cityCode, fromJumpObjectType)));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fromJumpObjectType = bundle.getInt(StringConstants.INTENT_PARAM_TYPE);
        }
    }

    /**
     * 设置自定义定位蓝点
     */
    private void setupLocationStyle() {
        aMap = mMapView.getMap();
        // 设置默认定位按钮是否显示
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);//只定位一次。
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.drawable.gps_point));
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(FILL_COLOR);
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void permissonsRequestSuccess() {
        initMyView();
        setupLocationStyle();
        //初始化定位
        initLocation();
        //开始定位
        startLocation();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void loadViewLayout() {

    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic() {

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
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        mPage++;
        searchNearBy();
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
                //解析定位结果
                String result = GaodeUtils.getLocationStr(aMapLocation);
                mlocation = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                searchLatlonPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mlocation, 16f));
                checkinpoint = mlocation;
                cityCode = aMapLocation.getCityCode();
                if (searchLatlonPoint != null) {
//                    searchNearBy();
                }
                LogUtil.e("location", result);
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
        checkWifiSetting();
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

    /**
     * 周边搜索
     */
    private void searchNearBy() {
        // 第一个参数表示搜索字符串，第二个参数表示POI搜索类型二选其一
        // 第三个参数表示POI搜索区域的编码，必设
        PoiSearch.Query query = new PoiSearch.Query("", "", "");
        query.setPageSize(20);
        query.setPageNum(mPage - 1);
        PoiSearch poisearch = new PoiSearch(this, query);
        poisearch.setOnPoiSearchListener(this);
        poisearch.setBound(new PoiSearch.SearchBound(searchLatlonPoint, 2000, true));
        poisearch.searchPOIAsyn();
    }


    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {

        if (poiResult != null) {
            ArrayList<PoiItem> pois = poiResult.getPois();
            if (pois != null && pois.size() > 0) {
                if (pois.size() < 20) {
                    mLocationListView.setPullLoadEnable(false);
                }
                if (mAdapter == null) {
                    mAdapter = new CommBaseAdapter<PoiItem>(this, R.layout.adapter_location_item, pois) {
                        @Override
                        protected void convert(ViewHolder viewHolder, PoiItem item, int position) {
                            if (TextUtils.isEmpty(item.getTitle())) {
                                viewHolder.setVisible(R.id.id_li_location, false);
                            } else {
                                viewHolder.setVisible(R.id.id_li_location, true);
                                viewHolder.setText(R.id.id_li_location, item.getTitle());
                            }

                            if (TextUtils.isEmpty(item.getSnippet())) {
                                viewHolder.setVisible(R.id.id_li_detail_location, false);
                            } else {
                                viewHolder.setVisible(R.id.id_li_detail_location, true);
                                viewHolder.setText(R.id.id_li_detail_location, item.getCityName() + item.getAdName() + item.getSnippet());
                            }
                        }
                    };
                    mLocationListView.setAdapter(mAdapter);
                    mLocationListView.setItemChecked(1, true);
                } else {
                    mAdapter.addList(pois);
                    mLocationListView.stopLoadMore();
                }
            } else {
                LogUtil.e("=============kong===============");
                mLocationListView.stopLoadMore();
            }

        } else {
            mLocationListView.stopLoadMore();
            mLocationListView.setPullLoadEnable(false);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void closeActivit(LocationEvent event) {
        if (event.isFinish()) {
            finish();
        }
    }

    /**
     * 地图移动过程回调
     *
     * @param cameraPosition
     */
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    /**
     * 地图移动结束回调
     *
     * @param cameraPosition
     */
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        checkinpoint = cameraPosition.target;
        searchLatlonPoint.setLatitude(checkinpoint.latitude);
        searchLatlonPoint.setLongitude(checkinpoint.longitude);
        mPage = 1;
        if (mAdapter != null && mAdapter.getCount() > 0) {
            mAdapter.clearData();
        }
        searchNearBy();
    }

    /**
     * 地图加载完成回调
     */
    @Override
    public void onMapLoaded() {
        addMarkerInScreenCenter();
    }

    /**
     * 添加选点marker
     */
    private void addMarkerInScreenCenter() {
        LatLng latLng = aMap.getCameraPosition().target;
        Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
        locationMarker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f));
        //设置Marker在屏幕上,不跟随地图移动
        locationMarker.setPositionByPixels(screenPosition.x, screenPosition.y);
    }

    /**
     * 检查wifi，并提示用户开启wifi
     */
    private void checkWifiSetting() {
        if (mWifiManager.isWifiEnabled()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("开启WIFI模块会提升定位准确性"); //设置内容
        builder.setPositiveButton("去开启", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
                Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent); // 打开系统设置界面
            }
        });
        builder.setNegativeButton("不了", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }
}
