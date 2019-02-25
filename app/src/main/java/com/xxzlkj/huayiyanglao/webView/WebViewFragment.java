package com.xxzlkj.huayiyanglao.webView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.zhaolinshare.base.base.ReuseViewFragment;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;


/**
 * Fragment的webView
 *
 * @author zhangrq
 */
public class WebViewFragment extends ReuseViewFragment {
    private final static String TAG = "WebView";
    public static final String WEB_URL = "webUrl";
    protected WebView webView;
    public MyWebChromeClient webChromeClient;
    private LinearLayout ll_fragment_webView_error;
    private RelativeLayout rl_load_data;
    private TextView tv_fragment_webView_reset;
    private AnimationDrawable animationDrawable;
    protected boolean isError;

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup root) {
        return inflater.inflate(R.layout.fragment_webview, root, false);
    }

    /**
     * 获取给WebViewFragment 传值的Bundle
     */
    public static Bundle newBundle(String webUrl) {
        Bundle bundle = new Bundle();
        bundle.putString(WEB_URL, webUrl);
        return bundle;
    }

    /**
     * 获取给WebViewFragment 传值的Bundle
     */
    public static WebViewFragment newInstance(String webUrl) {
        WebViewFragment webViewFragment = new WebViewFragment();
        webViewFragment.setArguments(newBundle(webUrl));
        return webViewFragment;
    }

    @Override
    protected void findViewById() {
        webView = getView(R.id.webView_fragment);// webView
        ll_fragment_webView_error = getView(R.id.ll_fragment_webView_error);// 错误页面
        rl_load_data = getView(R.id.rl_load_data);// 加载进度页面
        LayoutParams params = rl_load_data.getLayoutParams();
        params.height = (int) getResources().getDimension(R.dimen.load_data_width);
        //noinspection SuspiciousNameCombination
        params.width = params.height;
        rl_load_data.setLayoutParams(params);
        ImageView imageView = (ImageView) rl_load_data.findViewById(R.id.loadingImageView);
        animationDrawable = (AnimationDrawable) imageView.getBackground();// 加载进度动画

        tv_fragment_webView_reset = getView(R.id.tv_fragment_webView_reset);// 按钮，重置

    }

    @Override
    public void setListener() {
        tv_fragment_webView_reset.setOnClickListener(this);
    }

    @Override
    public void processLogic() {

        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (webView != null)
            webView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (webView != null)
            webView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webView != null)
            webView.destroy();
    }

    /**
     * 内嵌网页向上返回一页，如果返回到头，则会销毁此activity页面
     */
    public void goBackUntilFinishActivity() {
        if (canGoBack()) {
            goBack();
        } else {
            mActivity.finish();
        }
    }

    /**
     * 设置滚动到顶部
     */
    public void setScrollTop() {
        if (webView != null)
            webView.setScrollY(0);
    }

    /**
     * 内部页面返回上一级
     */
    public void goBack() {
        webView.goBack();
    }

    /**
     * 内部页面是否有上一级，是否返回到头
     */
    public boolean canGoBack() {
        return webView.canGoBack();
    }

    /**
     * 返回到顶部地址
     */
    public void goTopUrl() {
        while (canGoBack()) {
            goBack();
        }
    }

    public void reload() {
        if (webView != null) {
            webView.reload();
        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    protected void initData() {
        Bundle bundle = getArguments();
        // 网址
        String url = bundle.getString(WEB_URL);

//        Map<String, String> httpRequestHeaders = QGRequestUtils.getHttpRequestHeaders();
//        webView.loadUrl(url, httpRequestHeaders);
        webView.loadUrl(url);
        LogUtil.i(TAG, url);
//        LogUtil.i(TAG, "请求头：" + httpRequestHeaders.toString());

        // webView的设置
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        // 在安卓5.0之前默认允许其加载混合网络协议内容，
        // 在安卓5.0之后，默认不允许加载http与https混合内容，需要设置webView允许其加载混合网络协议内容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        // Horizontal水平方向，Vertical竖直方向
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);
        webChromeClient = new MyWebChromeClient();
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // 展示webView
                super.onPageStarted(view, url, favicon);
                showView(true, false, false);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // 展示错误布局
                super.onReceivedError(view, errorCode, description, failingUrl);
                isError = true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 隐藏加载进度框
                if (isError) {
                    // 来自错误页面，显示错误布局
                    showView(false, false, true);
                    // 归原
                    isError = false;
                } else {
                    // 来自成功页面，显示webview
                    showView(false, true, false);
                    // 设置页面加载完成的监听
                    if (onWebViewListener != null)
                        onWebViewListener.onPageFinished(canGoBack(), view.getTitle(), url);
                }

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view,
                                                    String insideUrl) {
                LogUtil.i(TAG, insideUrl);
                return super.shouldOverrideUrlLoading(view, insideUrl);
            }

        });
        // js交互
//        webView.addJavascriptInterface(new WebAppInterface(), "obj");

        // 开启localStorage
        webSettings.setDomStorageEnabled(true);

        // 开启数据缓存 DOM Storage
        webSettings.setDatabaseEnabled(true);
        String databasePath = webView.getContext()
                .getDir("databases", Context.MODE_PRIVATE).getPath();
        webSettings.setDatabasePath(databasePath);

        // 开启App缓存 AppCache
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = mActivity.getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);

        // 设置缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // 设置WebView是否通过手势触发播放媒体，默认是true，需要手势触发。
            webSettings.setMediaPlaybackRequiresUserGesture(false);
        }
    }

    public void loadUrl(String urlString) {
        if (webView != null) {
            LogUtil.i(TAG, urlString);
            webView.loadUrl(urlString);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_fragment_webView_reset:
                // 按钮重新加载(失败页面)
                webView.reload();// 会重新调用WebViewClient的生命周期
                break;
        }
    }

    // 是否展示WebView
    protected void showView(boolean isShowLoadView, boolean isShowWebView,
                            boolean isShowErrorView) {
        if (isShowLoadView) {
            rl_load_data.setVisibility(View.VISIBLE);
            animationDrawable.start();
        } else {
            rl_load_data.setVisibility(View.INVISIBLE);
            animationDrawable.stop();
        }

        webView.setVisibility(isShowWebView ? View.VISIBLE : View.INVISIBLE);
        ll_fragment_webView_error.setVisibility(isShowErrorView ? View.VISIBLE
                : View.INVISIBLE);

    }

    class MyWebChromeClient extends WebChromeClient {

        ValueCallback<Uri> mUploadMessage;

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, "");
        }

        // 3.0 + 调用这个方法
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType) {
            openFileChooser(uploadMsg, acceptType, "");
        }

        // For Android > 4.1.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), 100);
        }

    }

    private OnWebViewListener onWebViewListener;

    public OnWebViewListener getOnWebViewListener() {
        return onWebViewListener;
    }

    public void setOnWebViewListener(OnWebViewListener onWebViewListener) {
        this.onWebViewListener = onWebViewListener;
    }

    public interface OnWebViewListener {
        void onPageFinished(boolean isCanGoBack, String title, String url);
    }
}
