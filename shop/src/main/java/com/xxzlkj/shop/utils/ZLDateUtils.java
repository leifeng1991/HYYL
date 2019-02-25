package com.xxzlkj.shop.utils;

import android.text.TextUtils;

import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.model.CurrentTimeBean;
import com.xxzlkj.zhaolinshare.base.net.OnBaseRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/5/2 9:28
 */
public class ZLDateUtils {
    private static long currentTimeSeconds;
    private static Timer timer;
    private static TimerTask timerTask;

    public static void initCurrentTime() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        RequestManager.createRequest(URLConstants.GET_TIME_URL, stringStringHashMap, new OnBaseRequestListener<CurrentTimeBean>() {

            @Override
            public void handlerSuccess(CurrentTimeBean bean) {
                String time = bean.getData().getTime();
                if (!TextUtils.isEmpty(time) && !"0".equals(time)) {
                    currentTimeSeconds = NumberFormatUtils.toLong(time);
                    startTimer();
                } else
                    handlerError(0, "");
            }

            @Override
            public void handlerError(int errorCode, String errorMessage) {
                currentTimeSeconds = System.currentTimeMillis() / 1000;
                startTimer();
            }
        });
    }

    private static void startTimer() {
        // 归原
        cancelTimer();

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                currentTimeSeconds++;
            }
        };
        timer.schedule(timerTask, 1000, 1000);
    }

    public static void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    /**
     * 获取到秒
     */
    public static long getCurrentTimeSeconds() {
        if (currentTimeSeconds == 0)
            currentTimeSeconds = System.currentTimeMillis() / 1000;
        return currentTimeSeconds;
    }
}
