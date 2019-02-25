package com.xxzlkj.huayiyanglao.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/11/3 15:57
 */
public class ZLActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    private int appCount = 0;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        appCount++;
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        appCount--;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public int getAppCount() {
        return appCount;
    }

}
