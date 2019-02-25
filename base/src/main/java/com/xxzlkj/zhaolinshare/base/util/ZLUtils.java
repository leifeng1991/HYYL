package com.xxzlkj.zhaolinshare.base.util;


import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.xxzlkj.zhaolinshare.base.BuildConfig;
import com.xxzlkj.zhaolinshare.base.R;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/5/2 16:35
 */
public class ZLUtils {
    /**
     * 给打电话添加电话图片
     */
    public static void initCallPhoneTextView(TextView textView) {
        initCallPhoneTextViewRaw(textView, R.mipmap.ic_call_phone_order);
    }

    /**
     * 给打电话添加电话图片
     */
    public static void initCallPhoneTextViewRaw(TextView textView, int drawableResId) {
        //获取Drawable资源
        Drawable d = textView.getContext().getResources().getDrawable(drawableResId);
        TextPaint paint = textView.getPaint();
        Paint.FontMetrics fm = paint.getFontMetrics();
        int fontHeight = (int) Math.abs(fm.ascent);
        d.setBounds(0, 0, fontHeight, fontHeight);//必设置
        SpannableString ss = new SpannableString("打 电话");
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
        //用ImageSpan替换文本
        ss.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
    }


    /**
     * 兆邻的请求头
     */
    public static Map<String, String> getZhaoLinRequestHeader() {
//        App-Key	zhaolin
//        Timestamp	当前时间戳
//        Signature	签名 签名算法 sha1(App-Key连接Nonce连接Timestamp)
//        Phone-Model	手机型号 苹果传iPhone  安卓传android
//        Phone-Version	App版本号

        Map<String, String> map = new HashMap<>();
        String appKeyStr = "zhaolin";
        map.put("App-Key", appKeyStr);
        String currentTimeStr = String.valueOf(System.currentTimeMillis() / 1000);
        map.put("Timestamp", currentTimeStr);
        map.put("Signature", DecriptUtil.encryptSHA1(appKeyStr + "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCpeLgK0vk5POU5PQ" + currentTimeStr));
        map.put("Phone-Model", "android");
        map.put("Phone-Version", BuildConfig.VERSION_NAME);// 当前app版本号
        map.put("Phone-System", Build.VERSION.RELEASE); // 手机系统版本
        map.put("Phone-Brand", Build.BRAND);// 手机品牌类型
        map.put("Phone-Series", Build.MODEL);// 手机品牌型号
        User user = UserUtils.getLoginUser(BaseApplication.getInstance());
        map.put("Uid", user == null ? "0" : user.getData().getId());
        return map;
    }
}
