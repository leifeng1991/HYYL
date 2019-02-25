package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.xxzlkj.licallife.R;
import com.xxzlkj.shop.adapter.ColorFlipPagerTitleView;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;

import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.List;


/**
 * 描述:
 *
 * @author zhangrq
 *         2016/12/12 20:41
 */

public class YLSelectTimeCommonNavigatorAdapter extends CommonNavigatorAdapter {

    private final ViewPager viewPager;
    private final List<String> mDataList;

    public YLSelectTimeCommonNavigatorAdapter(ViewPager viewPager, List<String> mDataList) {
        this.viewPager = viewPager;
        this.mDataList = mDataList;
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        SimplePagerTitleView simplePagerTitleView = new ColorFlipPagerTitleView(context);
        simplePagerTitleView.setWidth(PixelUtil.getScreenWidth(context) / mDataList.size());
        simplePagerTitleView.setText(mDataList.get(index));
        simplePagerTitleView.setPadding(0, 0, 0, 0);
        simplePagerTitleView.setSingleLine(false);
        simplePagerTitleView.setNormalColor(ContextCompat.getColor(context, R.color.text_6));
        simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        simplePagerTitleView.setSelectedColor(ContextCompat.getColor(context, R.color.blue_54B1F8));
        // 设置点击
        simplePagerTitleView.setOnClickListener(v -> viewPager.setCurrentItem(index));
        return simplePagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
        indicator.setLineHeight(UIUtil.dip2px(context, 2));
        indicator.setLineWidth(UIUtil.dip2px(context, 45));
        indicator.setRoundRadius(UIUtil.dip2px(context, 1));
        indicator.setStartInterpolator(new AccelerateInterpolator());
        indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
        indicator.setColors(Color.parseColor("#54B1F8"));
        return indicator;
    }
}
