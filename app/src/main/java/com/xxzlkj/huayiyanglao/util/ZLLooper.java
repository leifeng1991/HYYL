package com.xxzlkj.huayiyanglao.util;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLGlobalParams;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;
import com.xxzlkj.zhaolinshare.base.util.OkHttpUtils;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * 描述:
 *
 * @author zhangrq
 *         2017/4/26 16:28
 */
public class ZLLooper {
    private static int PERIOD = 5 * 60 * 1000;

    public static void startLooper(Context context) {
//        Timer timer = new Timer();
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                // 提交数据
//                summitData();
//            }
//        };
//        // 常用于轮询
//        timer.schedule(timerTask, 0, PERIOD);// 5000(5秒)后，开始执行第一次run方法，此后每隔2秒调用一次
        summitData(context);
    }

    /**
     * 提交数据
     */
    private static void summitData(Context context) {

        GaodeLocationUtil gaodeLocationUtil = new GaodeLocationUtil();
        AMapLocationClientOption defaultOption = gaodeLocationUtil.getDefaultOption();
        defaultOption.setInterval(PERIOD);
        gaodeLocationUtil.initLocation(context, defaultOption, new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                // 全局保存经纬度
                ZLGlobalParams.aMapLocation = aMapLocation;

                submitLocationRequest(aMapLocation);
            }
        });
        gaodeLocationUtil.startLocation();
    }

    public static void submitLocationRequest(AMapLocation aMapLocation){
        if (aMapLocation == null)
            return;

        double longitude = aMapLocation.getLongitude();
        double latitude = aMapLocation.getLatitude();

        if (longitude == 0 || latitude == 0)
            return;

        String areaStr = (aMapLocation.getProvince() == null ? "" : aMapLocation.getProvince())// 省
                + (aMapLocation.getCity() == null ? "" : aMapLocation.getCity())// 市
                + (aMapLocation.getDistrict() == null ? "" : aMapLocation.getDistrict());// 区
        User user = ZhaoLinApplication.getInstance().getLoginUser();
        if (user == null)
            return;
        //        uid	会员id
        //        longitude	经度
        //        latitude	维度
        HashMap<String, String> requestParams = new HashMap<>();
        requestParams.put("uid", user.getData().getId());
        requestParams.put("longitude", longitude + "");
        requestParams.put("latitude", latitude + "");
        requestParams.put("area_str", areaStr);

        try {
            LogUtil.i("请求数据：", ZLURLConstants.KEEP_SEND_URL);
            LogUtil.i("请求数据头：", requestParams.toString());
            OkHttpUtils.enqueue(OkHttpUtils.getRequestPostByForm(ZLURLConstants.KEEP_SEND_URL, null, requestParams), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
