package com.xxzlkj.licallife.utils;

import android.view.View;

import com.xxzlkj.licallife.interfac.LocalLifeLibraryInterface;
import com.xxzlkj.licallife.model.CleaningBannerBean;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.weight.BannerView;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/5/2 16:35
 */
public class ZLBannerUtils {
    /**
     * 获取到轮播图数据，并设置内容及点击跳转
     *
     * @param pid 2 保洁师 3月嫂 4 保姆 5 小时工
     */
    public static void getBannerDataAndSetData(BaseActivity activity, BannerView bannerView, String pid) {
        Map<String, String> map = new HashMap<>();
        // 小区id
        map.put(URLConstants.REQUEST_PARAM_COMMUNITY_ID, GlobalParams.communityId);
//        pid	 2 保洁师 3月嫂 4 保姆 5 小时工
        map.put("pid", pid);
        RequestManager.createRequest(URLConstants.JZ_CLEANING_BANNER_URL, map, new OnMyActivityRequestListener<CleaningBannerBean>(activity) {
            @Override
            public void onSuccess(CleaningBannerBean bean) {
//                type	跳转方式 0不跳转 1跳h5
//                        to
                List<CleaningBannerBean.DataBean> data = bean.getData();
                if (data == null || data.size() == 0) {
                    // 没有图片，控件不显示
                    bannerView.setVisibility(View.GONE);
                    return;
                }
                bannerView.setVisibility(View.VISIBLE);
                ArrayList<String> urlList = new ArrayList<>();
                for (CleaningBannerBean.DataBean dataBean : bean.getData()) {
                    urlList.add(dataBean.getSimg());
                }
                bannerView.setImgUrlData(urlList);
                bannerView.setOnHeaderViewClickListener(position -> {
                    if (data.size() > position) {
                        CleaningBannerBean.DataBean dataBean = data.get(position);
                        if (BaseApplication.getInstance() instanceof LocalLifeLibraryInterface) {
                            ((LocalLifeLibraryInterface) BaseApplication.getInstance()).jumpToActivityByType(activity, dataBean.getType(), dataBean.getTo());
                        }
                    }
                });
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                // 不提示
//                super.onFailed(isError, code, message);
            }
        });
    }
}
