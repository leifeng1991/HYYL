package com.xxzlkj.huayiyanglao.activity.equipment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.suke.widget.SwitchButton;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.activity.address.LocationSearchActivity;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.model.UserWatchBean;
import com.xxzlkj.huayiyanglao.util.Const;
import com.xxzlkj.huayiyanglao.util.HYNetRequestUtil;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PermissionHelper;

import static com.xxzlkj.huayiyanglao.config.ZLConstants.Strings.DATA_BEAN;


/**
 * 描述:电子围栏
 *
 * @author zhangrq
 *         2017/3/21 14:48
 */
public class ElectronicFenceActivity extends MyBaseActivity implements GeocodeSearch.OnGeocodeSearchListener {

    private MapView mMapView;
    private AMap aMap;
    private PermissionHelper permissionHelper;
    private TextView mLocationTextView, mLongitudeTextView, mLatitudeTextView, mFenceRadiusTextView;
    private SwitchButton mSettingSwitchButton;
    private SeekBar mSeekBar;
    private GeocodeSearch geocodeSearch;
    private boolean isFirst;
    private AMapLocation mAmapLocation;

    /**
     * 用于显示当前的位置
     * <p>
     * 示例中是为了显示当前的位置，在实际使用中，单独的地理围栏可以不使用定位接口
     * </p>
     */
    private AMapLocationClient mlocationClient;
    private double latitude;
    private double longitude;
    private int radius;
    private UserWatchBean.DataBean dataBean;
    private String cityCode;

    // 中心点坐标
    private LatLng centerLatLng = null;
    private BitmapDescriptor ICON_RED = BitmapDescriptorFactory
            .defaultMarker(BitmapDescriptorFactory.HUE_RED);


    public static Intent newIntent(Context context) {
        return new Intent(context, ElectronicFenceActivity.class);
    }


    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_electronic_fence);
    }

    @Override
    protected void findViewById() {
        mMapView = (MapView) findViewById(R.id.id_location_mapview);// 地图布局
        mLocationTextView = getView(R.id.id_location);// 位置
        mLongitudeTextView = getView(R.id.id_longitude);// 当前经度
        mLatitudeTextView = getView(R.id.id_latitude);// 当前纬度
        mFenceRadiusTextView = getView(R.id.id_fence_radius);// 围栏半径
        mSettingSwitchButton = getView(R.id.id_setting_switch);// 开启围栏开关
        mSeekBar = getView(R.id.id_seek_bar);
    }

    @Override
    protected void setListener() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                LogUtil.e("progress=" + seekBar.getProgress());
                radius = seekBar.getProgress() * 50;
                drawCircle();
            }
        });
        EditText mSearchEditText = (EditText) findViewById(R.id.id_location_search);
        mSearchEditText.setOnClickListener(v -> startActivityForResult(LocationSearchActivity
                .newIntent(this, cityCode), ZLConstants.Integers.REQUEST_CODE_LOCATION_FINISH));
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        mSeekBar.setProgress(2);
        setTitleName("电子围栏");
        setTitleRightText("保存");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化地图
        if (aMap == null) {
            aMap = mMapView.getMap();
            aMap.getUiSettings().setRotateGesturesEnabled(false);
            // 是否允许显示缩放按钮
            aMap.getUiSettings().setZoomControlsEnabled(false);
            aMap.moveCamera(CameraUpdateFactory.zoomBy(16f));
            initLocation();
            setUpMap();
        }

        // 此方法必须重写
        mMapView.onCreate(savedInstanceState);
        // 请求权限
        permissionHelper = new PermissionHelper(this);

    }

    @Override
    public void onTitleRightClick(View view) {
        super.onTitleRightClick(view);
        if (dataBean != null) {
            dataBean.setFence_latitude(mLatitudeTextView.getText().toString());
            dataBean.setFence_longitude(mLongitudeTextView.getText().toString());
            dataBean.setFence_m(radius + "");
            dataBean.setFence_state(mSettingSwitchButton.isChecked() ? "2" : "1");
        }
        userWatchEdit();
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                latitude = cameraPosition.target.latitude;
                longitude = cameraPosition.target.longitude;

                drawCircle();
            }
        });
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(
                BitmapDescriptorFactory.fromResource(R.drawable.gps_point));
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

        //地理搜索类
        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);

        addCenterMarker();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
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
     * 初始化定位，设置回调监听
     */
    private void initLocation() {
        //初始化client
        mlocationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位参数
        mlocationClient.setLocationOption(getOption());
        // 设置定位监听
        mlocationClient.setLocationListener(locationListener);
    }

    /**
     * 开始定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void startLocation() {
        // 启动定位
        mlocationClient.startLocation();
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
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();

        mlocationClient.stopLocation();

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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    /**
     * 画圆和Market
     */
    private void drawCircle() {
        mLongitudeTextView.setText(String.format("%s", longitude));
        mLatitudeTextView.setText(String.format("%s", latitude));
        mFenceRadiusTextView.setText(String.format("%s米", radius));
        aMap.clear(true);
        centerLatLng = new LatLng(latitude, longitude);
        getAddressByLatlng(centerLatLng);
        // 绘制一个圆形
        aMap.addCircle(new CircleOptions().center(centerLatLng)
                .radius(radius).strokeColor(Const.STROKE_COLOR)
                .fillColor(Const.FILL_COLOR).strokeWidth(Const.STROKE_WIDTH));
        addCenterMarker();
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (null != aMapLocation) {
                mAmapLocation = aMapLocation;
                cityCode = aMapLocation.getCityCode();
            } else {
                LogUtil.e("location", "定位失败，loc is null");
            }

            if (dataBean == null) {
                getNetData();
            }
        }
    };

    /**
     * 添加Marker
     */
    private void addCenterMarker() {
        MarkerOptions markerOption = new MarkerOptions().draggable(true);
        markerOption.icon(ICON_RED);
        Marker centerMarker = aMap.addMarker(markerOption);
        centerMarker.setPosition(centerLatLng);
    }

    private void getAddressByLatlng(LatLng latLng) {
        //逆地理编码查询条件：逆地理编码查询的地理坐标点、查询范围、坐标类型。
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 500f, GeocodeSearch.AMAP);
        //异步查询
        geocodeSearch.getFromLocationAsyn(query);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        mLocationTextView.setText(String.format("位置：%s", regeocodeResult.getRegeocodeAddress().getFormatAddress()));
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    private void getNetData() {
        // 当dataBean为空时从网络获取
        HYNetRequestUtil.getUserWatch(ElectronicFenceActivity.this, userWatchBean -> {
            dataBean = userWatchBean.getData();
            // 设置数据
            setData(dataBean);
        });
    }

    /**
     * 修改绑定信息
     */
    private void userWatchEdit() {
        if (dataBean != null)
            HYNetRequestUtil.userWatchEdit(this, dataBean, new OnMyActivityRequestListener<BaseBean>(this) {
                @Override
                public void onSuccess(BaseBean bean) {
                    finish();
                }

                @Override
                public void onFailed(boolean isError, String code, String message) {
                    finish();
                }
            });
    }

    /**
     * 设置数据
     */
    private void setData(UserWatchBean.DataBean data) {
        // 中心纬度
        String fence_latitude = data.getFence_latitude();
        latitude = !TextUtils.isEmpty(fence_latitude) ? NumberFormatUtils.toDouble(fence_latitude) : mAmapLocation.getLatitude();
        // 中心经度
        String fence_longitude = data.getFence_longitude();
        longitude = !TextUtils.isEmpty(fence_longitude) ? NumberFormatUtils.toDouble(fence_longitude) : mAmapLocation.getLongitude();
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16f));
        // 半径
        String fence_m = data.getFence_m();
        radius = TextUtils.isEmpty(fence_m) ? 0 : NumberFormatUtils.toInt(fence_m, 0);
        mSeekBar.setProgress(radius / 50);
        mSettingSwitchButton.setChecked("2".equals(data.getFence_state()));
        drawCircle();
    }

    public static UserWatchBean.DataBean getIntentResult(Intent intent) {
        return (UserWatchBean.DataBean) intent.getSerializableExtra(DATA_BEAN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case ZLConstants.Integers.REQUEST_CODE_LOCATION_FINISH:
                    PoiItem poiItem = data.getParcelableExtra(ZLConstants.Strings.POI_ITEM);
                    latitude = poiItem.getLatLonPoint().getLatitude();
                    longitude = poiItem.getLatLonPoint().getLongitude();
                    mLocationTextView.setText(String.format("位置：%s", poiItem.getCityName() + poiItem.getAdName() + poiItem.getSnippet()));
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16f));
                    break;
            }
        }
    }
}
