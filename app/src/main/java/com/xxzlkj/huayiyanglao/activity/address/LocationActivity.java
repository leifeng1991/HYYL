package com.xxzlkj.huayiyanglao.activity.address;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.adapter.LocationAdapter;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;
import com.xxzlkj.zhaolinshare.base.util.PermissionHelper;
import com.xxzlkj.zhaolinshare.base.util.SystemBarUtils;

import java.util.ArrayList;

/**
 * 高德定位
 */
public class LocationActivity extends AppCompatActivity implements PoiSearch.OnPoiSearchListener, AMap.OnCameraChangeListener, AMap.OnMapLoadedListener {
    private MapView mMapView;
    private MyRecyclerView mMyRecyclerView;
    private LocationAdapter mLocationAdapter;
    private AMap aMap = null;
    private int mPage = 1;
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private AMapLocationClient mlocationClient = null;
    private Marker locationMarker;
    private LatLonPoint searchLatlonPoint;
    private LatLng checkinpoint, mlocation;
    private String cityCode = "";
    private PermissionHelper mHelper;

    /**
     * 使用startActivityForResult启动  data.getParcelableExtra(ZLConstants.Strings.POI_ITEM)获取PoiItem
     *
     * @return
     */
    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, LocationActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localtion);

        SystemBarUtils.setStatusBarColor(this, ContextCompat.getColor(this, R.color.title_bar_bg));
        SystemBarUtils.setStatusBarLightMode(this, true);
        findViewById(android.R.id.content).setBackgroundColor(ContextCompat.getColor(this, R.color.content_bg_zhaolin));

        mMapView = (MapView) findViewById(R.id.id_location_mapview);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setOnCameraChangeListener(this);
        aMap.setOnMapLoadedListener(this);

        // 此方法必须重写
        mMapView.onCreate(savedInstanceState);
        initMyView();
        setupLocationStyle();
        //初始化定位
        initLocation();

        mHelper = new PermissionHelper(this);
        mHelper.requestPermissions("请授予[位置]权限，否则无法获取附近小区", new PermissionHelper.PermissionListener() {
                    @Override
                    public void doAfterGrand(String... permission) {
                        //开始定位
                        startLocation();
                    }

                    @Override
                    public void doAfterDenied(String... permission) {
                    }
                }, Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.ACCESS_FINE_LOCATION);

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
        mMyRecyclerView = (MyRecyclerView) findViewById(R.id.id_recyclerview);
        mMyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMyRecyclerView.setPullRefreshAndLoadingMoreEnabled(false, true);
        mLocationAdapter = new LocationAdapter(this, R.layout.adapter_location_item);
        mMyRecyclerView.setAdapter(mLocationAdapter);

        ImageView leftImage = (ImageView) findViewById(R.id.iv_title_left);
        leftImage.setVisibility(View.VISIBLE);
        leftImage.setOnClickListener(v -> finish());
        TextView rightTextView = (TextView) findViewById(R.id.tv_title_right);
        rightTextView.setVisibility(View.VISIBLE);
        rightTextView.setText("确定 ");
        rightTextView.setOnClickListener(v -> {
            if (mLocationAdapter != null && mLocationAdapter.getItemCount() > 0) {
                Intent intent = new Intent();
                intent.putExtra(ZLConstants.Strings.POI_ITEM, mLocationAdapter.getPoiItem());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        mMyRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mPage++;
                searchNearBy();
            }
        });
        mLocationAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<PoiItem>() {
            @Override
            public void onClick(int position, PoiItem item) {
                int selection = mLocationAdapter.getSelection();
                if (selection != position) {
                    mLocationAdapter.setLastSelection(position);
                    mLocationAdapter.notifyDataSetChanged();
                }
            }
        });

        EditText mSearchEditText = (EditText) findViewById(R.id.id_location_search);
        mSearchEditText.setOnClickListener(v -> startActivityForResult(LocationSearchActivity
                .newIntent(LocationActivity.this, cityCode), ZLConstants.Integers.REQUEST_CODE_LOCATION_FINISH));
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
                mlocation = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                searchLatlonPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mlocation, 16f));
                checkinpoint = mlocation;
                cityCode = aMapLocation.getCityCode();
                if (searchLatlonPoint != null) {
//                    searchNearBy();
                }
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

    /**
     * 周边搜索
     */
    private void searchNearBy() {
        // 第一个参数表示搜索字符串，第二个参数表示POI搜索类型二选其一
        // 第三个参数表示POI搜索区域的编码，必设
        PoiSearch.Query query = new PoiSearch.Query("", "", "");
        query.setPageSize(20);
        query.setPageNum(mPage);
        PoiSearch poisearch = new PoiSearch(this, query);
        poisearch.setOnPoiSearchListener(this);
        poisearch.setBound(new PoiSearch.SearchBound(searchLatlonPoint, 2000, true));
        poisearch.searchPOIAsyn();
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

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (poiResult != null) {
            ArrayList<PoiItem> pois = poiResult.getPois();
            mMyRecyclerView.handlerSuccessHasRefreshAndLoad(mLocationAdapter, mPage == 1, pois);
            if (mPage == 1 && mLocationAdapter.getItemCount() > 0) {
                mLocationAdapter.setLastSelection(0);
                mLocationAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case ZLConstants.Integers.REQUEST_CODE_LOCATION_FINISH:
                    setResult(RESULT_OK, data);
                    finish();
                    break;
            }
        }
    }

    public static PoiItem getResult(Intent data) {
        if (data == null)
            return null;
        return (PoiItem) data.getParcelableExtra(ZLConstants.Strings.POI_ITEM);
    }
}
