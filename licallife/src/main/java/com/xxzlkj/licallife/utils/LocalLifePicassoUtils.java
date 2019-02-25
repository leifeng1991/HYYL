package com.xxzlkj.licallife.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestListener;
import com.xxzlkj.zhaolinshare.base.model.GlideApp;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/11/29 9:14
 */
public class LocalLifePicassoUtils {
    /**
     * 设置大图片
     */
    public static void setImageBigHasCallBack(Context context, String imgUrl, ImageView imageView, RequestListener<Drawable> listener) {
        if (!TextUtils.isEmpty(imgUrl)) {
            // 地址没有问题，获取图片
            PicassoUtils.getImageSignatureInMainThread(context, new PicassoUtils.OnGetImageSignatureListener() {
                @Override
                public void onSuccess(String sign) {
                    // 获取签名成功，下载图片
                    setImageRawHasCallBack(context, imgUrl + PicassoUtils.SIGN_BIG + sign, imageView, listener);
                }

                @Override
                public void onError(int errorCode, String errorMsg) {
                    // 获取签名失败，不处理
                    if (listener != null)
                        listener.onLoadFailed(null, null, null, false);
                }
            });
        }
    }
    /**
     * 设置图片，Picasso最原始的使用方法
     */
    public static void setImageRawHasCallBack(Context context, String imgUrl, ImageView imageView, RequestListener<Drawable> listener) {
        GlideApp.with(context).load(imgUrl).centerCrop().listener(listener).into(imageView);
    }
}
