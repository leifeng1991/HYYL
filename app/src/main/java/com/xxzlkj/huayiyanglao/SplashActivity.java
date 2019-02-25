package com.xxzlkj.huayiyanglao;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.ScreenBean;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.net.OnBaseRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.SystemBarUtils;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * 启动页
 *
 * @author zhangrq
 */
public class SplashActivity extends BaseActivity {

    public ArrayList<ScreenBean.DataBean> adList;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_splash);
        SystemBarUtils.setStatusBarTranslucent(this);
        SystemBarUtils.setStatusBarLightMode(this, true);
        // 创建静态handler，避免内存泄漏
        Handler handler = new SplashHandler(this);
        handler.sendEmptyMessageDelayed(1, 2000);
        // 获取广告数据
        getAdData();
    }

    private void getAdData() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        RequestManager.createRequest(URLConstants.SCREEN_URL, stringStringHashMap, new OnBaseRequestListener<ScreenBean>() {
            @Override
            public void handlerSuccess(ScreenBean bean) {
                if ("200".equals(bean.getCode())) {
                    // 200	表示成功
                    adList = bean.getData();
                    // 加载图片
                    for (ScreenBean.DataBean dataBean : adList) {
                        // TODO 优化
                        PicassoUtils.setImageBig(mContext, dataBean.getSimg(), new ImageView(mContext));
                    }
                }
            }

            @Override
            public void handlerError(int errorCode, String errorMessage) {

            }
        });
    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic() {
        TextView tv_version = getView(R.id.tv_version);
        tv_version.setText("Copyright©HuaYi  V " + BuildConfig.VERSION_NAME);

    }

    // 使用静态的，防止内存泄漏
    private static class SplashHandler extends Handler {
        private SoftReference<SplashActivity> splashActivitySoftReference;

        SplashHandler(SplashActivity splashActivity) {
            splashActivitySoftReference = new SoftReference<>(splashActivity);
        }

        public void handleMessage(android.os.Message msg) {
            SplashActivity splashActivity = splashActivitySoftReference.get();
            Context mContext = splashActivity.getApplicationContext();
            splashActivity.startActivity(new Intent(mContext, MainTabActivity.class));
            // 关闭掉当前的splash界面，避免用户按返回键时有看到此界面
            splashActivity.finish();
//            switch (msg.what) {
//                case 1:
//                    String versionName = PreferencesSaver.getStringAttr(mContext, ZLConstants.Strings.VERSION_NAME);
//                    if (TextUtils.isEmpty(versionName) || !BuildConfig.VERSION_NAME.equals(versionName)) {
//                        //之前没存过，或者现在的版本号和之前的不一样（说明升级了）
//                        // 第一次进入引导页
//                        splashActivity.startActivity(new Intent(mContext, GuideViewActivity.class));
//                    } else {
//                        // 开启主页
//                        ArrayList<ScreenBean.DataBean> adList = splashActivity.adList;
//                        if (adList != null && adList.size() > 0) {
//                            // 有广告，跳到广告页面
//                            splashActivity.startActivity(ADActivity.newIntent(mContext, adList));
//                        } else
//                            // 无广告
//                            splashActivity.startActivity(new Intent(mContext, MainTabActivity.class));
//                    }
//                    // 关闭掉当前的splash界面，避免用户按返回键时有看到此界面
//                    splashActivity.finish();
//                    break;
//            }
//        }
        }
    }

}
