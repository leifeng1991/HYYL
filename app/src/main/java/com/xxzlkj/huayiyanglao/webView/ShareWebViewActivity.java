package com.xxzlkj.huayiyanglao.webView;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.umeng.socialize.UMShareAPI;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;

/**
 * 网页跳转
 *
 * @author zhangrq
 */
public class ShareWebViewActivity extends MyBaseActivity implements WebViewFragment.OnWebViewListener {

    public static final String WEB_URL = "webUrl";
    public static final String TITLE_NAME = "titleName";
    public static final String IS_SHARE = "is_share";
    public static final String DESC = "desc";
    public static final String SIMG = "simg";
    private WebViewFragment webViewFragment;
    private String is_share;
    private String title;
    private String desc;
    private String simgUrl;
    private String webUrl;


    /**
     * @param webUrl   加载的url地址
     * @param is_share 是否可以分享
     * @param title    分享标题
     * @param is_share 分享描述
     */
    public static Intent newIntent(Context context, String webUrl, String is_share, String simg, String title, String desc) {
        Intent intent = new Intent(context, ShareWebViewActivity.class);
        intent.putExtra(WEB_URL, webUrl);
        intent.putExtra(IS_SHARE, is_share);
        intent.putExtra(SIMG, simg);
        intent.putExtra(TITLE_NAME, title);
        intent.putExtra(DESC, desc);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        // 设置此页面的样式
        setContentView(R.layout.activity_webview_share);
    }

    @Override
    protected void findViewById() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        webViewFragment = new WebViewFragment();
        // 网址
        webUrl = getIntent().getStringExtra(WEB_URL);
        webViewFragment.setArguments(WebViewFragment.newBundle(webUrl));// 传递值
        fragmentTransaction.add(R.id.ll_webview, webViewFragment);// 将fragment设置到布局上
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void setListener() {
        // 设置webView的监听
        webViewFragment.setOnWebViewListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.iv_title_right:
                // 分享
//                ShareUtil.shareImageText(ShareWebViewActivity.this, mContext, R.id.id_parent, simgUrl, webUrl, title, desc);
                break;
        }
    }

    @Override
    protected void processLogic() {
        setTitleName("详情");
        title = getIntent().getStringExtra(TITLE_NAME);
        is_share = getIntent().getStringExtra(IS_SHARE);
        desc = getIntent().getStringExtra(DESC);
        simgUrl = getIntent().getStringExtra(SIMG);

        // 可以分享
        if ("2".equals(is_share))
//            setTitleRightImage(R.mipmap.share_icon);

        // 设置返回按钮
        setTitleLeftBack();
    }

    @Override
    public void onBackPressed() {
        webViewFragment.goBackUntilFinishActivity();
    }

    @Override
    public void onPageFinished(boolean isCanGoBack, String title, String url) {
//        setTitleName(title);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
