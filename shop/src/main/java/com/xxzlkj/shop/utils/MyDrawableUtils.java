package com.xxzlkj.shop.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.ViewTreeObserver;

import com.xxzlkj.shop.R;
import com.xxzlkj.zhaolinshare.base.util.DrawableUtils;


/**
 * 聪明投样式的背景图片
 *
 * @author zhangrq
 */
public class MyDrawableUtils {
    /**
     * 设置聪明投，大button按钮选择器
     *
     * @param context
     * @param view
     */
    public static void setCMTBigButtonSelector(final Context context,
                                               final View view) {
        // 设置bitmap的边角弧度
        final float cornerRadius = context.getResources().getDimension(
                R.dimen.corner_radius);
        // 设置阴影的边距
        final float shadowRadius = context.getResources().getDimension(
                R.dimen.shadow_radius);
        // 设置阴影的边距
        final float shadowDy = context.getResources().getDimension(
                R.dimen.shadow_dy);

        final ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        viewTreeObserver
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @SuppressLint("NewApi")
                    public boolean onPreDraw() {
                        // 按下的图片（enable 为true 并且按下，的图片样式（必须，先添加，优先处理））
                        BitmapDrawable enablePressedDrawable = DrawableUtils
                                .getBitmapDrawable(
                                        context,
                                        getShadowBitmapStyle1(
                                                view.getMeasuredWidth(),
                                                view.getMeasuredHeight(),
                                                context.getResources()
                                                        .getColor(
                                                                R.color.orange_e55638),
                                                cornerRadius,
                                                context.getResources()
                                                        .getColor(
                                                                R.color.orange_80e55638),
                                                shadowRadius, shadowDy));

                        // 可用的图片
                        // orange_FDBEB1 enable为false颜色
                        // orange_ff603e 其它状态颜色（enable为true颜色）
                        BitmapDrawable enableDrawable = DrawableUtils
                                .getBitmapDrawable(
                                        context,
                                        getShadowBitmapStyle1(
                                                view.getMeasuredWidth(),
                                                view.getMeasuredHeight(),
                                                context.getResources()
                                                        .getColor(
                                                                R.color.orange_ff603e),
                                                cornerRadius,
                                                context.getResources()
                                                        .getColor(
                                                                R.color.orange_80ff603e),
                                                shadowRadius, shadowDy));
                        // 其它状态的图片（必须最后添加）
                        BitmapDrawable normalDrawable = DrawableUtils
                                .getBitmapDrawable(
                                        context,
                                        getShadowBitmapStyle1(
                                                view.getMeasuredWidth(),
                                                view.getMeasuredHeight(),
                                                context.getResources()
                                                        .getColor(
                                                                R.color.orange_FDBEB1),
                                                cornerRadius,
                                                context.getResources()
                                                        .getColor(
                                                                R.color.orange_80FDBEB1),
                                                shadowRadius, shadowDy));


                        StateListDrawable stateListDrawable = new StateListDrawable();
                        stateListDrawable.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed}, enablePressedDrawable);// 参数1，stateSet全部为true，才会显示参数2图片
                        stateListDrawable.addState(new int[]{android.R.attr.state_enabled}, enableDrawable);
                        stateListDrawable.addState(new int[]{}, normalDrawable);


                        // 设置view的padding值，因为此图片给人的错觉是内容不居中，此修正内容位置
                        int bottom = (int) (view.getPaddingBottom()
                                + shadowRadius + shadowDy);
                        view.setPadding(view.getPaddingLeft(),
                                view.getPaddingTop(), view.getPaddingRight(),
                                bottom);

                        view.setBackgroundDrawable(stateListDrawable);
                        // // 移除监听
                        view.getViewTreeObserver()
                                .removeOnPreDrawListener(this);
                        return true;
                    }
                });

    }

    /**
     * 设置聪明投，小按钮图片
     */
    public static void setMyButtonShape(final Context context,
                                        final View view, int fillColorId, int shadowColorId) {
        // 设置bitmap的边角弧度
        final float cornerRadius = context.getResources().getDimension(
                R.dimen.corner_radius);
        // 设置阴影的边距
        final float shadowRadius = context.getResources().getDimension(
                R.dimen.shadow_radius);
        // 设置阴影的边距
        final float shadowDy = context.getResources().getDimension(
                R.dimen.shadow_dy);
        final int fillColor = context.getResources().getColor(fillColorId);
        final int shadowColor = context.getResources().getColor(shadowColorId);
        final ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        viewTreeObserver
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @SuppressLint("NewApi")
                    public boolean onPreDraw() {
                        // 可用的图片
                        BitmapDrawable shapeDrawable = DrawableUtils
                                .getBitmapDrawable(
                                        context,
                                        getShadowBitmapStyle1(
                                                view.getMeasuredWidth(),
                                                view.getMeasuredHeight(),
                                                fillColor, cornerRadius,
                                                shadowColor, shadowRadius,
                                                shadowDy));
                        // 设置view的padding值，因为此图片给人的错觉是内容不居中，此修正内容位置
                        int bottom = (int) (view.getPaddingBottom()
                                + shadowRadius + shadowDy);
                        view.setPadding(view.getPaddingLeft(),
                                view.getPaddingTop(), view.getPaddingRight(),
                                bottom);
                        // 设置背景
                        view.setBackgroundDrawable(shapeDrawable);
                        // 移除监听
                        view.getViewTreeObserver()
                                .removeOnPreDrawListener(this);
                        return true;
                    }
                });
    }

    /**
     * 设置聪明投，小按钮图片
     */
    public static void setCMTButtonSelector(final Context context, final View view,
                                            int pressedFillColorId, int pressedShapeColorId,
                                            int normalFillColorId, int normalShapeColorId) {
        // 设置bitmap的边角弧度
        final float cornerRadius = context.getResources().getDimension(
                R.dimen.corner_radius);
        // 设置阴影的边距
        final float shadowRadius = context.getResources().getDimension(
                R.dimen.shadow_radius);
        // 设置阴影的边距
        final float shadowDy = context.getResources().getDimension(
                R.dimen.shadow_dy);
        // 按下的颜色
        final int pressedFillColor = context.getResources().getColor(pressedFillColorId);
        final int pressedShapeColor = context.getResources().getColor(pressedShapeColorId);
        // 正常的颜色
        final int normalFillColor = context.getResources().getColor(normalFillColorId);
        final int normalShapeColor = context.getResources().getColor(normalShapeColorId);
        final ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        viewTreeObserver
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @SuppressLint("NewApi")
                    public boolean onPreDraw() {
                        // 按下的图片
                        BitmapDrawable pressedDrawable = DrawableUtils
                                .getBitmapDrawable(
                                        context,
                                        getShadowBitmapStyle1(
                                                view.getMeasuredWidth(),
                                                view.getMeasuredHeight(),
                                                pressedFillColor, cornerRadius,
                                                pressedShapeColor, shadowRadius,
                                                shadowDy));
                        // 正常的图片
                        BitmapDrawable normalDrawable = DrawableUtils
                                .getBitmapDrawable(
                                        context,
                                        getShadowBitmapStyle1(
                                                view.getMeasuredWidth(),
                                                view.getMeasuredHeight(),
                                                normalFillColor, cornerRadius,
                                                normalShapeColor, shadowRadius,
                                                shadowDy));

                        // 设置view的padding值，因为此图片给人的错觉是内容不居中，此修正内容位置
                        int bottom = (int) (view.getPaddingBottom()
                                + shadowRadius + shadowDy);
                        view.setPadding(view.getPaddingLeft(),
                                view.getPaddingTop(), view.getPaddingRight(),
                                bottom);
                        // 设置背景
                        StateListDrawable stateListDrawable = DrawableUtils.getStateListDrawable(
                                new int[]{android.R.attr.state_pressed}, pressedDrawable, normalDrawable);
                        view.setBackgroundDrawable(stateListDrawable);
                        // 移除监听
                        view.getViewTreeObserver()
                                .removeOnPreDrawListener(this);
                        return true;
                    }
                });
    }

    /**
     * 创建带阴影效果的图片，在图片周围有模糊阴影
     *
     * @param shadowBitmapWidth  创建bitmap的宽
     * @param shadowBitmapHeight 创建bitmap的高
     * @param fillColor          填充颜色
     * @param cornerRadius       设置bitmap的边角弧度
     * @param shadowRadius       设置阴影的边距
     */
    public static Bitmap getShadowBitmapStyle1(int shadowBitmapWidth,
                                               int shadowBitmapHeight, int fillColor, float cornerRadius,
                                               int shadowColor, float shadowRadius, float shadowDy) {

        return DrawableUtils.getShadowBitmap(shadowBitmapWidth,
                shadowBitmapHeight, fillColor, cornerRadius, shadowColor,
                shadowRadius, 0, shadowDy);
    }

    /**
     * 设置聪明投蓝色shape背景图片
     */
    public static void setCMTButtonShapeBlue(Context context, View view) {
//        setMyButtonShape(context, view, R.color.title_bar,R.color.blue_8046cbfe);
//        充值按钮	3fb6e4
        setCMTButtonSelector(context, view, R.color.blue_3fb6e4, R.color.blue_8046cbfe,
                R.color.title_bar, R.color.blue_8046cbfe);


    }

    /**
     * 设置聪明投白色shape背景图片
     *
     */
    public static void setButtonShapeOrange(Context context, View view) {
        setMyButtonShape(context, view, R.color.orange_ff725c, R.color.orange_ff725c);
        //        提现按钮	e6ecf0
//        setCMTButtonSelector(context, view, R.color.white_e6ecf0, R.color.blue_2646cbfe
//                , R.color.white, R.color.blue_2646cbfe);
    }

    public static void setButtonShape(Context context, View view,int colorResId) {
        setMyButtonShape(context, view, colorResId, colorResId);
        //        提现按钮	e6ecf0
//        setCMTButtonSelector(context, view, R.color.white_e6ecf0, R.color.blue_2646cbfe
//                , R.color.white, R.color.blue_2646cbfe);
    }
}
