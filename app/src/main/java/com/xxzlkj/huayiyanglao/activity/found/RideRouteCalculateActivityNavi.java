package com.xxzlkj.huayiyanglao.activity.found;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.model.NaviLatLng;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.util.GaodeLocationUtil;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;

/**
 * 骑行导航
 */
public class RideRouteCalculateActivityNavi extends NaviBaseActivity {
    public static final String END_LATITUDE = "end_latitude";
    public static final String END_LONGITUDE = "end_longitude";
    private double endLatitude;
    private double endLongitude;
    //是否是第一次定位
    private boolean isFirstLocation = true;
    private GaodeLocationUtil gaodeLocationUtil;

    /**
     *
     * @param context 上下文 （必传）
     * @param endLatitude 结束纬度 （必传）
     * @param endLongitude 结束经度 （必传）
     * @return
     */
    public static Intent newIntent(Context context, double endLatitude, double endLongitude) {
        Intent intent = new Intent(context, RideRouteCalculateActivityNavi.class);
        Bundle bundle = new Bundle();
        bundle.putDouble(END_LATITUDE, endLatitude);
        bundle.putDouble(END_LONGITUDE, endLongitude);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_navi);
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            endLatitude = bundle.getDouble(END_LATITUDE);
            endLongitude = bundle.getDouble(END_LONGITUDE);
            mEndLatlng = new NaviLatLng(endLatitude, endLongitude);
        }

        gaodeLocationUtil = new GaodeLocationUtil();
        gaodeLocationUtil.initLocation(this, new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation location) {
                LogUtil.e("===============定位成功");
                if (isFirstLocation){
                    mStartLatlng = new NaviLatLng(location.getLatitude(), location.getLongitude());
                    sList.add(mStartLatlng);
                    eList.add(mEndLatlng);

                    mAMapNavi.calculateRideRoute(new NaviLatLng(location.getLatitude(), location.getLongitude()), new NaviLatLng(endLatitude, endLongitude));
                    isFirstLocation = false;
                }
            }
        });

        /*final AMap aMap = mAMapNaviView.getMap();
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LogUtil.e("===============定位成功");
                if (isFirstLocation){
                    mStartLatlng = new NaviLatLng(location.getLatitude(), location.getLongitude());
                    sList.add(mStartLatlng);
                    eList.add(mEndLatlng);

                    mAMapNavi.calculateRideRoute(new NaviLatLng(location.getLatitude(), location.getLongitude()), new NaviLatLng(endLatitude, endLongitude));
                    isFirstLocation = false;
                }
            }
        });*/
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gaodeLocationUtil != null){
            gaodeLocationUtil.stopLocation();
            gaodeLocationUtil.destroyLocation();
        }
    }
}
