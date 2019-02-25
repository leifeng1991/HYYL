package com.xxzlkj.licallife.interfac;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/11/28 15:45
 */
public interface LocalLifeLibraryInterface {
    /**
     * 跳到web页面
     */
    void jumpToHasTitleWebView(Activity activity, String url, String title);

    /**
     * 获取支付页面的Intent
     */
    Intent getPayActivityIntent(Context mContext, String orderid, String id, int i, String groupon_team_id);

    /**
     * 根据type跳到指定位置
     */
    void jumpToActivityByType(Activity mActivity, String type, String to);
}
