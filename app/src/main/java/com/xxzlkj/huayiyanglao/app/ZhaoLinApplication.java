package com.xxzlkj.huayiyanglao.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.iflytek.cloud.SpeechUtility;
import com.lqr.emoji.IImageLoader;
import com.lqr.emoji.LQREmotionKit;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.xxzlkj.huayiyanglao.BuildConfig;
import com.xxzlkj.huayiyanglao.MainTabActivity;
import com.xxzlkj.huayiyanglao.activity.LoginActivity;
import com.xxzlkj.huayiyanglao.activity.PayActivity;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.listener.MyIRongReceivedCallListener;
import com.xxzlkj.huayiyanglao.listener.MyOnReceiveMessageListener;
import com.xxzlkj.huayiyanglao.listener.OnLoginSuccessListener;
import com.xxzlkj.huayiyanglao.util.JumpToWebView;
import com.xxzlkj.huayiyanglao.util.MyConnectionStatusListener;
import com.xxzlkj.huayiyanglao.util.ZLActivityUtils;
import com.xxzlkj.licallife.interfac.LocalLifeLibraryInterface;
import com.xxzlkj.shop.interfac.ShopLibraryInterface;
import com.xxzlkj.shop.utils.ZLPreferencesUtils;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.config.BaseConstants;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.util.CrashHandler;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.PreferencesSaver;
import com.xxzlkj.zhaolinshare.chat.event.RongConnectSuccessEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import io.rong.calllib.RongCallClient;
import io.rong.imlib.RongIMClient;
import io.rong.push.RongPushClient;


/**
 * 描述:application
 *
 * @author zhangrq
 *         2017/3/6 14:53
 */

public class ZhaoLinApplication extends BaseApplication implements ShopLibraryInterface, LocalLifeLibraryInterface {
    private static ZhaoLinApplication instance;

    public int screenWidth;
    public int screenHeight;
    private ZLActivityLifecycleCallbacks activityLifecycleCallbacks;
    private OnLoginSuccessListener onLoginSuccessListener;

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(instance = this);
        // 注册
        activityLifecycleCallbacks = new ZLActivityLifecycleCallbacks();
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);


        // 初始化一次
        initOnce();
        // 初始化融云
        initRongYun();
        // 初始化讯飞语音
        SpeechUtility.createUtility(getApplicationContext(), "appid=5a1622d6");

    }


    private void initOnce() {
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    measureInit();// 测量屏幕宽高
                    new CrashHandler(getApplicationContext(), "zhaoLinLog");// 设置捕获日志
//                    FontsUtils.setMyAppTypeface(getApplicationContext());// 设置字体
                    // 初始化微信
                    IWXAPI api = WXAPIFactory.createWXAPI(getApplicationContext(), ZLConstants.Strings.WECHAT_APPID, true);
                    api.registerApp(ZLConstants.Strings.WECHAT_APPID);
//                    // 初始化极光
                    initJPush();
//                    // 初始化友盟
                    initUmeng();
                    // 初始化表情控件
                    initEmotion();

//                    BlurKit.init(getApplicationContext());

                }
            }.start();
        }
    }

    private void initEmotion() {
        LQREmotionKit.init(this, new IImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                PicassoUtils.setImageRaw(context, path, imageView);
            }
        });
    }

    private void initRongYun() {
        String curProcessName = getCurProcessName(getApplicationContext());
        boolean isCurrentProcess = getApplicationInfo().packageName.equals(curProcessName);
        if (isCurrentProcess) {
            // 小米推送
            RongPushClient.registerMiPush(this, BuildConfig.xiaomi_appId, BuildConfig.xiaomi_appKey);
            // 华为推送
            RongPushClient.registerHWPush(this);
        }

        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIMClient 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (isCurrentProcess || "io.rong.push".equals(curProcessName)) {
            RongIMClient.init(this);
            // 监听接收到的消息
            RongIMClient.setOnReceiveMessageListener(new MyOnReceiveMessageListener(getApplicationContext()));
            // 监听链接状态变化
            RongIMClient.setConnectionStatusListener(new MyConnectionStatusListener(getApplicationContext()));
            // 监听通话来电
            RongCallClient.setReceivedCallListener(new MyIRongReceivedCallListener(getApplicationContext()));

        }

    }

    /**
     * 连接融云
     */
    public void connectRongIM() {
        // 链接融云
        User loginUser = getLoginUser();
        if (loginUser != null && !TextUtils.isEmpty(loginUser.getData().getToken())) {
            // 用户不为空，并且融云token不为空，链接
            connectRongIM(loginUser.getData().getToken());
        }
    }

    /**
     * umeng和umeng社会化分享三方账号初始化
     */
    @SuppressWarnings("ConstantConditions")
    private void initUmeng() {
        // 友盟分享
        Config.isJumptoAppStore = true;
        Config.DEBUG = true;
        UMShareAPI.get(this);
        PlatformConfig.setWeixin(ZLConstants.Strings.WECHAT_APPID, ZLConstants.Strings.WECHAT_APPSECRET);
        PlatformConfig.setSinaWeibo("612203548", "e765639ac4ad71b9ca0c95a11ca0b996", "http://sns.whalecloud.com");
        // 友盟统计
        // 正式的app并且是打包的，统计，设为false
        MobclickAgent.setDebugMode(!(!BuildConfig.isTest && !BuildConfig.DEBUG));// 是否打印Log错误日志
        MobclickAgent.setCatchUncaughtExceptions(!BuildConfig.isTest && !BuildConfig.DEBUG);// 是否上传错误报告
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);//设置场景模式为普通
    }

    /**
     * 初始化极光推送
     */
    private void initJPush() {
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
        //设置别名为用户手机号
        User loginUser = getLoginUser();
        setJPushAlias(loginUser == null ? null : loginUser.getData().getPhone());
    }

    //消息推送--设置别名
    private void setJPushAlias(String alias) {
//        alias
//        null 此次调用不设置此值。（注：不是指的字符串"null"）
//        "" （空字符串）表示取消之前的设置。
//        每次调用设置有效的别名，覆盖之前的设置。
//        有效的别名组成：字母（区分大小写）、数字、下划线、汉字、特殊字符(v2.1.6支持)@!#$&*+=.|￥。
//        限制：alias 命名长度限制为 40 字节。（判断长度需采用UTF-8编码）
//
//        callback;
//        在TagAliasCallback 的 gotResult 方法，返回对应的参数 alias, tags。并返回对应的状态码：0为成功，其他返回码请参考错误码定义。
        JPushInterface.setAlias(getApplicationContext(), alias, new TagAliasCallback() {
            @Override
            public void gotResult(int responseCode, String alias, Set<String> tags) {
//                responseCode     0 表示调用成功。   其他返回码请参考错误码定义。
//                alias            原设置的别名
//                tags             原设置的标签

            }
        });
    }


    public static ZhaoLinApplication getInstance() {
        return instance;
    }


    public User getLoginUserDoLogin(Activity activity) {
        if (loginUser == null) {
            String userString = PreferencesSaver.getStringAttr(instance, BaseConstants.Strings.USER);
            if (TextUtils.isEmpty(userString)) {
                // 用户没登录，并且未保存本地（退出登录效果）,重新登录
                Intent intent = new Intent(activity, LoginActivity.class);
                activity.startActivity(intent);
            } else {
                // 用户没登录，保存本地保存了，用本地的
                loginUser = new Gson().fromJson(userString, User.class);
            }
        }
        return loginUser;
    }

    /**
     * 设置成功登录用户(本地保存用户名和密码)
     */
    public void setSuccessLoginUser(User loginUser, String userName) {
        // 保存用户名，密码
        PreferencesSaver.setStringAttr(this, ZLConstants.Strings.LOGIN_USER_NAME, userName);
        // PreferencesSaver.setStringAttr(this,
        // PreferencesSaver.KEY_LAST_LOGIN_PSWD, userPassword);
        // 设置登录成功
        setSuccessLoginUser(loginUser);
    }

    /**
     * 设置成功登录用户
     */
    public void setSuccessLoginUser(User loginUser) {
        // user保存
        this.loginUser = loginUser;
        if (loginUser == null)
            PreferencesSaver.setStringAttr(this, BaseConstants.Strings.USER, "");
        else
            PreferencesSaver.setStringAttr(this, BaseConstants.Strings.USER, new Gson().toJson(loginUser));
        // 设置已经登录
        PreferencesSaver.setBooleanAttr(this, BaseConstants.Strings.ALREADY_LOGIN, true);
        // 消息推送--设置别名
        setJPushAlias(loginUser == null ? "" : loginUser.getData().getPhone());
        // 链接融云
        if (loginUser != null && !TextUtils.isEmpty(loginUser.getData().getToken())) {
            // 用户不为空，并且融云token不为空，链接
            ZhaoLinApplication.getInstance().connectRongIM(loginUser.getData().getToken());
        }

        if (onLoginSuccessListener != null) {
            onLoginSuccessListener.onLoginSuccess(loginUser);
            onLoginSuccessListener = null;
        }
    }

    /**
     * 设置退出登录
     */
    public void setExitLoginUser() {
        // 清空手机号
        PreferencesSaver.setStringAttr(getBaseContext(), BaseConstants.Strings.LOGIN_USER_NAME, "");
        // user清空
        this.loginUser = null;
        PreferencesSaver.setStringAttr(this, BaseConstants.Strings.USER, "");
        // 登录状态为：未登录
        PreferencesSaver.setBooleanAttr(this, BaseConstants.Strings.ALREADY_LOGIN, false);
        // 消息推送--取消之前设置的别名
//        setJPushAlias("");
        // 断开融云链接
        RongIMClient.getInstance().logout();
        // 清除本地保存数据
        ZLPreferencesUtils.removeAddressInfo(getApplicationContext());
        PreferencesSaver.setStringAttr(this, ZLConstants.Strings.COMMUNITY_ID, "");// 保存到本地
        PreferencesSaver.setStringAttr(this, ZLConstants.Strings.COMMUNITY_NAME, "");// 保存到本地
        onLoginSuccessListener = null;
    }

    /**
     * 获取屏幕宽度并设置
     */
    private void measureInit() {
        WindowManager wm = (WindowManager) getBaseContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    public void connectRongIM(String token) {
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {
            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIMClient.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {
                    // IOS 说token永久生效的，所以不用处理
                    LogUtil.i("UserLoginActivity", "--onTokenIncorrect");
                    System.out.println("===========onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    LogUtil.i("UserLoginActivity", "--onSuccess---" + userid);
                    System.out.println("===========onSuccess====" + userid);
                    EventBus.getDefault().postSticky(new RongConnectSuccessEvent());
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    LogUtil.i("UserLoginActivity", "--onError" + errorCode);
                    System.out.println("===========onError====" + errorCode);

                }
            });
        }
    }

    /**
     * app是否在前台
     */
    public boolean isForeground() {
        return activityLifecycleCallbacks != null && activityLifecycleCallbacks.getAppCount() > 0;
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    // 商场的接口
    @Override
    public void jumpToHasTitleWebView(Activity activity, String url, String title) {
        JumpToWebView.getInstance(activity).jumpToHasTitleWebView(url, title);
    }

    @Override
    public Intent getPayActivityIntent(Context mContext, String orderid, String id, int i, String groupon_team_id) {
        return PayActivity.newIntent(mContext, orderid, id, i, groupon_team_id);
    }

    @Override
    public Intent getMainTabActivityIntent(Context mContext, int tabIndex) {
        return MainTabActivity.newIntent(mContext, tabIndex);
    }

    @Override
    public void jumpToActivityByType(Activity mActivity, String type, String to) {
        ZLActivityUtils.jumpToActivityByType(mActivity, type, to);
    }

    public void setOnLoginSuccessListener(OnLoginSuccessListener onLoginSuccessListener) {
        this.onLoginSuccessListener = onLoginSuccessListener;
    }
}