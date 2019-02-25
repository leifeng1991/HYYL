package com.xxzlkj.huayiyanglao.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import static com.xxzlkj.huayiyanglao.config.ZLConstants.Strings.WECHAT_APPID;

/**
 * Created by Administrator on 2017/4/13.
 */

public class AppRegister extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, WECHAT_APPID);

        // 将该app注册到微信
        msgApi.registerApp(WECHAT_APPID);
    }
}
