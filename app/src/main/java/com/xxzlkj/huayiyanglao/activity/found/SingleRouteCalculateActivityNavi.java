package com.xxzlkj.huayiyanglao.activity.found;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviLatLng;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.util.GaodeLocationUtil;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;

/**
 * 单路径驾车导航
 */
public class SingleRouteCalculateActivityNavi extends NaviBaseActivity {
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
        Intent intent = new Intent(context, SingleRouteCalculateActivityNavi.class);
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
                    /**
                     * 方法: int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute); 参数:
                     *
                     * @congestion 躲避拥堵
                     * @avoidhightspeed 不走高速
                     * @cost 避免收费
                     * @hightspeed 高速优先
                     * @multipleroute 多路径
                     *
                     *  说明: 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
                     *  注意: 不走高速与高速优先不能同时为true 高速优先与避免收费不能同时为true
                     */
                    int strategy = 0;

                    mStartLatlng = new NaviLatLng(location.getLatitude(), location.getLongitude());
                    sList.add(mStartLatlng);
                    eList.add(mEndLatlng);

                    try {
                        //再次强调，最后一个参数为true时代表多路径，否则代表单路径
                        strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);

                    isFirstLocation = false;
                }
            }
        });
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
