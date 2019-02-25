package com.xxzlkj.huayiyanglao.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.event.WeChatPayEvent;

import org.greenrobot.eventbus.EventBus;

import static com.xxzlkj.huayiyanglao.config.ZLConstants.Strings.WECHAT_APPID;

/**
 * 微信支付回调
 * Created by Administrator on 2017/4/13.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, WECHAT_APPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.d(TAG, "checkArgs=" + baseReq.checkArgs());
    }

    @Override
    public void onResp(BaseResp baseResp) {
        finish();
        EventBus.getDefault().postSticky(new WeChatPayEvent(baseResp.errCode));
        Log.d(TAG, "onPayFinish, errCode = " + baseResp.errCode);
    }
}
