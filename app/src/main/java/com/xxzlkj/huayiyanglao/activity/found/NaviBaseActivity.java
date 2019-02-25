package com.xxzlkj.huayiyanglao.activity.found;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.xxzlkj.shop.activity.address.CheckPermissionsActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * 语音导航基础类
 * Created by Administrator on 2017/4/26.
 */

public class NaviBaseActivity extends CheckPermissionsActivity implements AMapNaviListener, AMapNaviViewListener {

    protected AMapNaviView mAMapNaviView;
    protected AMapNavi mAMapNavi;
    protected TTSController mTtsManager;
    protected NaviLatLng mEndLatlng;
    protected NaviLatLng mStartLatlng;
    protected final List<NaviLatLng> sList = new ArrayList<NaviLatLng>();
    protected final List<NaviLatLng> eList = new ArrayList<NaviLatLng>();
    protected List<NaviLatLng> mWayPointList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //实例化语音引擎
        mTtsManager = TTSController.getInstance(getApplicationContext());
        mTtsManager.init();

        //
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);
        mAMapNavi.addAMapNaviListener(mTtsManager);

        //设置模拟导航的行车速度
        mAMapNavi.setEmulatorNaviSpeed(75);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
    }

    @Override
    public void permissonsRequestSuccess() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        mAMapNaviView.onPause();

//        仅仅是停止你当前在说的这句话，一会到新的路口还是会再说的
        mTtsManager.stopSpeaking();
//
//        停止导航之后，会触及底层stop，然后就不会再有回调了，但是讯飞当前还是没有说完的半句话还是会说完
//        mAMapNavi.stopNavi();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAMapNaviView.onDestroy();
        //since 1.6.0 不再在naviview destroy的时候自动执行AMapNavi.stopNavi();请自行执行
        mAMapNavi.stopNavi();
        mAMapNavi.destroy();
        mTtsManager.destroy();
    }

    @Override
    public void onInitNaviFailure() {
        Toast.makeText(this, "init navi Failed", Toast.LENGTH_SHORT).show();
    }

    /**
     * 初始化成功
     */
    @Override
    public void onInitNaviSuccess() {
    }

    /**
     * 开始导航回调
     * @param type
     */
    @Override
    public void onStartNavi(int type) {
    }

    @Override
    public void onTrafficStatusUpdate() {
        //
    }

    /**
     * 当前位置回调
     * @param location
     */
    @Override
    public void onLocationChange(AMapNaviLocation location) {
    }

    /**
     * 播报类型和播报文字回调
     * @param type
     * @param text
     */
    @Override
    public void onGetNavigationText(int type, String text) {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    /**
     * 结束模拟导航
     */
    @Override
    public void onEndEmulatorNavi() {

    }

    /**
     * 到达目的地
     */
    @Override
    public void onArriveDestination() {

    }


    /**
     * 路线计算失败
     * @param errorInfo
     */
    @Override
    public void onCalculateRouteFailure(int errorInfo) {
        Log.e("dm", "--------------------------------------------");
        Log.i("dm", "路线计算失败：错误码=" + errorInfo + ",Error Message= " + ErrorInfo.getError(errorInfo));
        Log.i("dm", "错误码详细链接见：http://lbs.amap.com/api/android-navi-sdk/guide/tools/errorcode/");
        Log.e("dm", "--------------------------------------------");
        Toast.makeText(this, "errorInfo：" + errorInfo + ",Message：" + ErrorInfo.getError(errorInfo), Toast.LENGTH_LONG).show();
    }

    /**
     * 偏航后重新计算路线回调
     */
    @Override
    public void onReCalculateRouteForYaw() {

    }

    /**
     * 拥堵后重新计算路线回调
     */
    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    /**
     * 到达途径点
     * @param wayID
     */
    @Override
    public void onArrivedWayPoint(int wayID) {

    }

    /**
     * GPS开关状态回调
     * @param enabled
     */
    @Override
    public void onGpsOpenStatus(boolean enabled) {

    }

    /**
     * 底部导航设置点击回调
     */
    @Override
    public void onNaviSetting() {

    }

    /**
     * 地图的模式，锁屏或锁车
     * @param isLock
     */
    @Override
    public void onNaviMapMode(int isLock) {

    }

    @Override
    public void onNaviCancel() {
        finish();
    }

    /**
     * 转弯view的点击回调
     */
    @Override
    public void onNaviTurnClick() {

    }

    /**
     * 下一个道路View点击回调
     */
    @Override
    public void onNextRoadClick() {

    }

    /**
     * 全览按钮点击回调
     */
    @Override
    public void onScanViewButtonClick() {

    }

    @Deprecated
    @Override
    public void onNaviInfoUpdated(AMapNaviInfo naviInfo) {
        //过时
    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapCameraInfos) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] amapServiceAreaInfos) {

    }

    /**
     * 导航过程中的信息更新，请看NaviInfo的具体说明
     * @param naviinfo
     */
    @Override
    public void onNaviInfoUpdate(NaviInfo naviinfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {
        //已过时
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
        //已过时
    }

    /**
     * 显示转弯回调
     * @param aMapNaviCross
     */
    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    /**
     * 隐藏转弯回调
     */
    @Override
    public void hideCross() {

    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    /**
     * 显示车道信息
     * @param laneInfos
     * @param laneBackgroundInfo
     * @param laneRecommendedInfo
     */
    @Override
    public void showLaneInfo(AMapLaneInfo[] laneInfos, byte[] laneBackgroundInfo, byte[] laneRecommendedInfo) {

    }

    /**
     * 隐藏车道信息
     */
    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void notifyParallelRoad(int i) {
        if (i == 0) {
//            Toast.makeText(this, "当前在主辅路过渡", Toast.LENGTH_SHORT).show();
            Log.d("wlx", "当前在主辅路过渡");
            return;
        }
        if (i == 1) {
//            Toast.makeText(this, "当前在主路", Toast.LENGTH_SHORT).show();

            Log.d("wlx", "当前在主路");
            return;
        }
        if (i == 2) {
            Toast.makeText(this, "当前在辅路", Toast.LENGTH_SHORT).show();

            Log.d("wlx", "当前在辅路");
        }
    }

    /**
     * 更新交通设施信息
     * @param aMapNaviTrafficFacilityInfos
     */
    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    /**
     * 更新巡航模式的统计信息
     * @param aimLessModeStat
     */
    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    /**
     * 更新巡航模式的拥堵信息
     * @param aimLessModeCongestionInfo
     */
    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    /**
     * 锁地图状态发生变化时回调
     * @param isLock
     */
    @Override
    public void onLockMap(boolean isLock) {

    }

    @Override
    public void onNaviViewLoaded() {
        Log.d("wlx", "导航页面加载成功");
        Log.d("wlx", "请不要使用AMapNaviView.getMap().setOnMapLoadedListener();会overwrite导航SDK内部画线逻辑");
    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }


}