package com.xxzlkj.shop.weight;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxzlkj.shop.R;

import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IMeasurablePagerTitleView;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/6/30 9:24
 */
public class MyPagerTitleView extends RelativeLayout implements IMeasurablePagerTitleView {

    private ImageView imageView;
    private TextView textView;
    protected int mSelectedColor;
    protected int mNormalColor;

    public MyPagerTitleView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        View rootView = View.inflate(context, R.layout.item_shop_home_top, null);
        imageView = (ImageView) rootView.findViewById(R.id.iv_icon);
        textView = (TextView) rootView.findViewById(R.id.tv_title);
        addView(rootView, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

        int padding = UIUtil.dip2px(context, 10.0D);
        this.setPadding(padding, 0, padding, 0);
    }

    @Override
    public int getContentLeft() {
        return this.getLeft();
    }

    @Override
    public int getContentTop() {
        return getTop();
    }

    @Override
    public int getContentRight() {
        return getRight();
    }

    @Override
    public int getContentBottom() {
        return getHeight();
    }

    @Override
    public void onSelected(int i, int i1) {
        textView.setTextColor(this.mSelectedColor);
    }

    @Override
    public void onDeselected(int i, int i1) {
        textView.setTextColor(this.mNormalColor);
    }

    @Override
    public void onLeave(int i, int i1, float v, boolean b) {

    }

    @Override
    public void onEnter(int i, int i1, float v, boolean b) {

    }

    public int getSelectedColor() {
        return mSelectedColor;
    }

    public void setSelectedColor(int mSelectedColor) {
        this.mSelectedColor = mSelectedColor;
    }

    public int getNormalColor() {
        return mNormalColor;
    }

    public void setNormalColor(int mNormalColor) {
        this.mNormalColor = mNormalColor;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
}
