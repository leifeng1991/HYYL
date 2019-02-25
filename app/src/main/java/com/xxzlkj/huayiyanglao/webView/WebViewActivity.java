package com.xxzlkj.huayiyanglao.webView;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;


/**
 * 网页跳转
 *
 * @author zhangrq
 */
public class WebViewActivity extends MyBaseActivity implements WebViewFragment.OnWebViewListener {

    public static final String WEB_URL = "webUrl";
    public static final String TITLE_NAME = "titleName";
    public static final String IS_SHOW_TITLE = "isShowTitle";
    public static final String ENTER_ANIM = "enterAnim";
    public static final String EXIT_ANIM = "exitAnim";
    private WebViewFragment webViewFragment;
    private int enterAnim;
    private int exitAnim;

    /**
     * @param webUrl      加载的url地址
     * @param titleName   标题头名字
     * @param isShowTitle 是否展示标题头
     */
    public static Intent newIntent(Context context, String webUrl, String titleName, boolean isShowTitle) {
        return newIntent(context, webUrl, titleName, isShowTitle, -1, -1);
    }

    /**
     * @param webUrl      加载的url地址
     * @param titleName   标题头名字
     * @param isShowTitle 是否展示标题头
     * @param enterAnim   WebViewActivity的进入动画
     * @param exitAnim    WebViewActivity的退出动画
     */
    public static Intent newIntent(Context context, String webUrl, String titleName, boolean isShowTitle, int enterAnim, int exitAnim) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(WEB_URL, webUrl);
        intent.putExtra(TITLE_NAME, titleName);
        intent.putExtra(IS_SHOW_TITLE, isShowTitle);
        intent.putExtra(ENTER_ANIM, enterAnim);
        intent.putExtra(EXIT_ANIM, exitAnim);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        // 设置此页面的样式
        enterAnim = getIntent().getIntExtra("enterAnim", -1);
        exitAnim = getIntent().getIntExtra("exitAnim", -1);
        if (enterAnim != -1 && exitAnim != -1)
            overridePendingTransition(enterAnim, exitAnim);
        setContentView(R.layout.activity_webview);
    }

    @Override
    protected void findViewById() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        webViewFragment = new WebViewFragment();
        // 网址
        String webUrl = getIntent().getStringExtra(WEB_URL);
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
    protected void processLogic() {
//        // 标题头名字
        String titleName = getIntent().getStringExtra(TITLE_NAME);
        if (!TextUtils.isEmpty(titleName)) {
            setTitleName(titleName);
        } else {
            setTitleName(getString(R.string.app_name));
        }
        // 是否展示标题头
        boolean isShowTitle = getIntent().getBooleanExtra(IS_SHOW_TITLE, true);
        View view = getView(R.id.titleBar);
        if (view != null)
            view.setVisibility(isShowTitle ? View.VISIBLE : View.GONE);

        // 设置返回按钮
        setTitleLeftBack();
    }


    @Override
    public void finish() {
        super.finish();
        if (enterAnim != -1 && exitAnim != -1)
            overridePendingTransition(enterAnim, exitAnim);
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
