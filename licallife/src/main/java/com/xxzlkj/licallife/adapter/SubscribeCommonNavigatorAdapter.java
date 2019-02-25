package com.xxzlkj.licallife.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.sunfusheng.marqueeview.DisplayUtil;
import com.xxzlkj.licallife.R;
import com.xxzlkj.shop.adapter.ColorFlipPagerTitleView;

import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.List;


/**
 * 浏览历史
 *
 */
public class SubscribeCommonNavigatorAdapter extends CommonNavigatorAdapter {

    private final ViewPager viewPager;
    private final List<String> mDataList;

    public SubscribeCommonNavigatorAdapter(ViewPager viewPager, List<String> mDataList) {
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
        simplePagerTitleView.setWidth(DisplayUtil.dip2px(context,200) / mDataList.size());
        simplePagerTitleView.setText(mDataList.get(index));
        simplePagerTitleView.setNormalColor(ContextCompat.getColor(context, R.color.black_3a3a3a));
        simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        simplePagerTitleView.setSelectedColor(ContextCompat.getColor(context, R.color.black_3a3a3a));
        simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(index);
            }
        });
        return simplePagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
        indicator.setLineHeight(UIUtil.dip2px(context, 2));
        indicator.setRoundRadius(UIUtil.dip2px(context, 1));
        indicator.setStartInterpolator(new AccelerateInterpolator());
        indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
        indicator.setColors(Color.TRANSPARENT);
        return indicator;
    }
}
