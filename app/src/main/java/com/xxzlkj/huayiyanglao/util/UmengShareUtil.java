package com.xxzlkj.huayiyanglao.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWeb;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

/**
 * 分享
 * Created by Administrator on 2017/5/8.
 */

public class UmengShareUtil {

    /**
     * 视频分享
     *
     * @param videoUrl 视频地址（必传）
     * @param title    主标题（必传）
     * @param subTitle 描述（必传）
     * @param platform 分享平台（必传）
     */
    public static void shareVideo(Activity activity, String videoUrl, String title, String subTitle, String simgUrl, SHARE_MEDIA platform) {
        UMImage image;
        Context context = activity.getApplicationContext();
        if (TextUtils.isEmpty(simgUrl)) {
            // 图片地址为空
            image = new UMImage(activity, R.mipmap.ic_launcher);
        } else {
            // 图片地址不为空
            image = new UMImage(activity, simgUrl);
        }

        // 对于微信QQ的那个平台，分享的图片需要设置缩略图
        UMVideo video = new UMVideo(videoUrl);
        video.setTitle(title);//视频的标题
        video.setThumb(image);//视频的缩略图
        video.setDescription(subTitle);//视频的描述
        if (UMShareAPI.get(context).isInstall(activity, platform)) {
            // 已安装该应用
            new ShareAction(activity).setPlatform(platform)
                    .withMedia(video)
                    .setCallback(initUMShareListener(context))
                    .share();
        } else {
            // 未安装微信
            ToastManager.showMsgToast(context, "你还没安装此应用，请安装后重试");
        }
    }

    /**
     * 图片分享
     */
    public static void shareImage(Activity activity, SHARE_MEDIA platform, Bitmap bitmap) {
        Context context = activity.getApplicationContext();
        UMImage image = new UMImage(context, bitmap);
        // 对于微信QQ的那个平台，分享的图片需要设置缩略图
        UMImage thumb = new UMImage(context, bitmap);
        image.setThumb(thumb);
        if (UMShareAPI.get(context).isInstall(activity, platform)) {
            // 已安装该应用
            new ShareAction(activity).setPlatform(platform)
                    .withMedia(image)
                    .setCallback(initUMShareListener(context))
                    .share();
        } else {
            // 未安装微信
            ToastManager.showMsgToast(context, "你还没安装此应用，请安装后重试");
        }
    }

    /**
     * 链接分享
     *
     * @param imageUrl 缩略图
     * @param webUrl   链接地址
     * @param title    标题
     * @param desc     描述
     */
    public static void shareImageText(Activity activity, SHARE_MEDIA platform, String imageUrl, String webUrl, String title, String desc) {
        // 对于微信QQ的那个平台，分享的图片需要设置缩略图
        Context context = activity.getApplicationContext();
        UMImage thumb;
        if (TextUtils.isEmpty(imageUrl)) {
            // 图片地址为空
            thumb = new UMImage(context, R.mipmap.ic_launcher);
        } else {
            // 图片地址为空
            thumb = new UMImage(context, imageUrl);
        }

        UMWeb web = new UMWeb(webUrl);
        web.setTitle(title);//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription(desc);//描述
        if (UMShareAPI.get(context).isInstall(activity, platform)) {
            // 已安装该应用
            new ShareAction(activity).setPlatform(platform)
                    .withMedia(web)
                    .setCallback(initUMShareListener(context))
                    .share();
        } else {
            // 未安装微信
            ToastManager.showMsgToast(context, "你还没安装此应用，请安装后重试");
        }
    }


    private static UMShareListener initUMShareListener(final Context context) {
        return new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
                //分享开始的回调
            }

            @Override
            public void onResult(SHARE_MEDIA platform) {
                LogUtil.d("plat", "platform" + platform);
                ToastManager.showShortToast(context, "分享成功");

            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                ToastManager.showShortToast(context, "分享失败");
                if (t != null) {
                    LogUtil.d("throw", "throw:" + t.getMessage());
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                ToastManager.showShortToast(context, "分享取消");
            }
        };
    }

}
