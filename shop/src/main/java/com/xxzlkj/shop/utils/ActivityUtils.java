package com.xxzlkj.shop.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.xxzlkj.shop.interfac.ShopLibraryInterface;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;


/**
 * 描述:activity的工具类
 *
 * @author zhangrq
 *         2016/11/24 16:41
 */
public class ActivityUtils {
    /**
     * 页面销毁到MainTabActivity并跳转到tabIndex栏目
     *
     * @param activity 当前activity
     * @param tabIndex 要跳转的栏目位置
     */
    public static void finishToMainTabActivityTab(Activity activity, int tabIndex) {
        if (activity == null)
            return;
        if (BaseApplication.getInstance() instanceof ShopLibraryInterface) {
            // 让主项目处理
            Intent intent = ((ShopLibraryInterface) BaseApplication.getInstance()).getMainTabActivityIntent(activity.getApplicationContext(), tabIndex);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
        }

    }

    /**
     * 从fromActivity销毁到clazz Activity
     */
    public static void finishToActivity(Activity fromActivity, Class clazz) {
        if (fromActivity == null)
            return;
        Intent intent = new Intent(fromActivity.getApplicationContext(), clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        fromActivity.startActivity(intent);
    }

    /**
     * 跳到clazz Activity
     */
    public static void jumpToActivity(Context context, Class clazz) {
        if (context == null)
            return;
        Intent intent = new Intent(context.getApplicationContext(), clazz);
        if (!(context instanceof Activity))
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 跳到clazz Activity
     */
    public static void jumpToActivity(Context context, Intent intent) {
        if (context == null)
            return;
        if (!(context instanceof Activity))
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
