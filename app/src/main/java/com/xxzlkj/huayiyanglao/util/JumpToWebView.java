package com.xxzlkj.huayiyanglao.util;

import android.app.Activity;

import com.xxzlkj.huayiyanglao.webView.WebViewActivity;
import com.xxzlkj.shop.config.URLConstants;


/**
 * 统一的处理跳转到网页
 *
 * @author zhangrq
 */
public class JumpToWebView {
    private Activity mActivity;

    private JumpToWebView() {

    }

    public static JumpToWebView getInstance(Activity activity) {
        JumpToWebView jumpToWebView = new JumpToWebView();
        jumpToWebView.mActivity = activity;
        return jumpToWebView;
    }

    /**
     * 跳到有标题头的网页
     *
     * @param webViewUrl 网址
     * @param titleName  标题头名字，为空则为默认应用名在WebView已处理
     */
    public void jumpToHasTitleWebView(String webViewUrl, String titleName) {
        mActivity.startActivity(WebViewActivity.newIntent(mActivity.getApplicationContext(), webViewUrl, titleName, true));
    }

    /**
     * 跳到没标题头的网页
     *
     * @param webViewUrl 网址
     */
    public void jumpToNoTitleWebView(String webViewUrl) {
        mActivity.startActivity(WebViewActivity.newIntent(mActivity.getApplicationContext(), webViewUrl, "", false));
    }

    // 仿照这个格式写
    public void jumpToXX() {
        String webViewUrl = "http://www.baidu.com";
        jumpToHasTitleWebView(webViewUrl, "百度");
    }
    /**
     * 关于兆邻
     */
    public void jumpToAboutZhaoLin() {
        jumpToHasTitleWebView(URLConstants.ABOUT_H5_URL, "关于华颐");
    }


}
