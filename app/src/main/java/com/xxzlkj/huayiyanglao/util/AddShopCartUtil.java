package com.xxzlkj.huayiyanglao.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.model.FoodsAddCartBean;
import com.xxzlkj.huayiyanglao.model.FoodsBean;
import com.xxzlkj.huayiyanglao.weight.ShopCartTextView;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

/**
 * 加入购物车
 * Created by Administrator on 2017/7/5.
 */

public class AddShopCartUtil {
    //弹入动画时间
    public final static int ANIMATION_IN_TIME=400;
    //弹出动画时间
    public final static int ANIMATION_OUT_TIME=300;

    /**
     * 加入购物车
     *
     * @param view         点击view
     * @param shopCartView 动画目标view
     * @param id
     */
    public static void addShopCart(final BaseActivity activity, View view, View shopCartView, String id) {

        HYNetRequestUtil.foodsAddCart(activity, id, new HYNetRequestUtil.OnRequestSuccessListener() {
            @Override
            public void RequestSuccess(FoodsAddCartBean bean) {
                addShopCartAnimal(activity, view, shopCartView);
                ToastManager.showShortToast(activity.getApplicationContext(), "加入购物车成功");
            }
        });
    }


    /**
     * 加入购物车动画
     *
     * @param addView      点击view
     * @param shopCartView 目标view
     */
    public static void addShopCartAnimal(Activity activity, View addView, View shopCartView) {
        if (ZhaoLinApplication.getInstance().getLoginUserDoLogin(activity) != null) {
            ShopCartTextView mShopCartTextView = new ShopCartTextView(activity.getApplicationContext());
            int position[] = new int[2];
            addView.getLocationInWindow(position);
            mShopCartTextView.setStartPosition(new Point(position[0], position[1]));

            ViewGroup rootView = (ViewGroup) activity.getWindow().getDecorView();
            rootView.addView(mShopCartTextView);
            int endPosition[] = new int[2];
            shopCartView.getLocationInWindow(endPosition);
            mShopCartTextView.setEndPosition(new Point(endPosition[0], endPosition[1]));
            // 开始
            mShopCartTextView.startBeizerAnimation();
        }
    }



    /**
     * 创建弹入动画集合
     * @param context
     * @param fromYDelta
     * @return
     */
    public static Animation createInAnimation(Context context, int fromYDelta){
        //创建一个动画集合
        AnimationSet set=new AnimationSet(context,null);
        //设置动画结束之后是否保持动画的目标状态
        set.setFillAfter(true);
        //创建一个位移动画
        TranslateAnimation animation=new TranslateAnimation(0,0,fromYDelta,0);
        //设置过度时间
        animation.setDuration(ANIMATION_IN_TIME);
        //添加动画集合
        set.addAnimation(animation);
        //创建一个透明度动画效果  这里都是1到1
        AlphaAnimation alphaAnimation=new AlphaAnimation(1,1);
        //设置过度时间
        alphaAnimation.setDuration(ANIMATION_IN_TIME);
        //添加动画集合
        set.addAnimation(alphaAnimation);
        return set;
    }

    /**
     * 创建退出动画
     * @param context
     * @param toYDelta
     * @return
     */
    public static Animation createOutAnimation(Context context, int toYDelta){
        //创建一个动画集合
        AnimationSet set=new AnimationSet(context,null);
        //设置动画结束之后是否保持动画的目标状态
        set.setFillAfter(true);
        //创建一个位移动画
        TranslateAnimation animation=new TranslateAnimation(0,0,0,toYDelta);
        //设置过度时间
        animation.setDuration(ANIMATION_OUT_TIME);
        //添加到动画集合
        set.addAnimation(animation);
        //创建一个透明度动画效果  这里都是1到1
        AlphaAnimation alphaAnimation=new AlphaAnimation(1,1);
        //设置过度时间
        alphaAnimation.setDuration(ANIMATION_OUT_TIME);
        //添加动画集合
        set.addAnimation(alphaAnimation);
        return set;
    }
}
