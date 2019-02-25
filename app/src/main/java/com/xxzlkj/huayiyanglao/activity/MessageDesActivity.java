package com.xxzlkj.huayiyanglao.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.webView.WebViewFragment;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;


/**
 * 消息详情
 *
 * @author zhangrq
 */
public class MessageDesActivity extends MyBaseActivity implements WebViewFragment.OnWebViewListener {
    public static final String WEB_URL = "webUrl";
    public static final String STATE = "state";
    private WebViewFragment webViewFragment;
    private LinearLayout ll_message_des_btn;
    private TextView tv_message_des_btn1;
    private TextView tv_message_des_btn2;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_message_des);
    }

    @Override
    protected void findViewById() {
        ll_message_des_btn = getView(R.id.ll_message_des_btn);// 底部按钮
        tv_message_des_btn1 = getView(R.id.tv_message_des_btn1); // 按钮1
        tv_message_des_btn2 = getView(R.id.tv_message_des_btn2);  // 按钮2

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        webViewFragment = new WebViewFragment();
        // 网址
        String webUrl = getIntent().getStringExtra(WEB_URL);
        webViewFragment.setArguments(WebViewFragment.newBundle(webUrl));// 传递值
        fragmentTransaction.add(R.id.ll_webView, webViewFragment);// 将fragment设置到布局上
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void setListener() {
        // 设置webView的监听
        webViewFragment.setOnWebViewListener(this);
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("消息详情");
        // 判断布局显示
        // 0普通消息，1优惠券，2 h5
        String state = getIntent().getStringExtra(STATE);
        if ("1".equals(state)) {
            // 优惠券
            // 设置底部按钮
            ll_message_des_btn.setVisibility(View.VISIBLE);
            /*tv_message_des_btn1.setText("查看优惠券");
            tv_message_des_btn1.setOnClickListener(v -> startActivity(CouponsActivity.newIntent(mContext)));
            tv_message_des_btn2.setText("立即使用");
            tv_message_des_btn2.setOnClickListener(v -> ZLActivityUtils.jumpToShopHomeActivity(this));*/
        } else {
            // 普通消息
            ll_message_des_btn.setVisibility(View.GONE);
        }
    }


    /**
     * @param webUrl 加载的url地址
     * @param state  状态
     */
    public static Intent newIntent(Context context, String webUrl, String state) {
        Intent intent = new Intent(context, MessageDesActivity.class);
        intent.putExtra(WEB_URL, webUrl);
        intent.putExtra(STATE, state);
        return intent;
    }

    @Override
    public void onBackPressed() {
        webViewFragment.goBackUntilFinishActivity();
    }

    @Override
    public void onPageFinished(boolean isCanGoBack, String title, String url) {
//        setTitleName(title);
    }

}
