package com.xxzlkj.zhaolinshare.base.app;

import android.app.Activity;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.xxzlkj.zhaolinshare.base.config.BaseConstants;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.util.PreferencesSaver;


/**
 * 描述:
 *
 * @author zhangrq
 *         2017/9/21 10:21
 */
public class BaseApplication extends MultiDexApplication {
    private static BaseApplication baseApplication;
    protected User loginUser;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
    }

    public static BaseApplication getInstance() {
        return baseApplication;
    }

    public void setInstance(BaseApplication instance) {
        baseApplication = instance;
    }

    // 获取登录用户
    public User getLoginUser() {
        String userString = PreferencesSaver.getStringAttr(baseApplication, BaseConstants.Strings.USER);
        if (loginUser == null && !TextUtils.isEmpty(userString)) {
            // 用户没登录，保存本地保存了，用本地的
            loginUser = new Gson().fromJson(userString, User.class);
        }
        return loginUser;
    }

    public User getLoginUserDoLogin(Activity activity) {
        return loginUser;
    }
}
