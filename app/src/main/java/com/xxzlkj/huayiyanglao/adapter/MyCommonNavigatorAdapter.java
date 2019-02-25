package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.weight.ColorFlipPagerTitleView;

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

public class MyCommonNavigatorAdapter extends CommonNavigatorAdapter {

    private final ViewPager viewPager;
    private final List<String> mDataList;

    public MyCommonNavigatorAdapter(ViewPager viewPager, List<String> mDataList) {
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
        simplePagerTitleView.setWidth(ZhaoLinApplication.getInstance().screenWidth / mDataList.size());
        simplePagerTitleView.setText(mDataList.get(index));
//        simplePagerTitleView.setPadding(UIUtil.dip2px(context, 17), 0, UIUtil.dip2px(context, 17), 0);
        simplePagerTitleView.setNormalColor(ContextCompat.getColor(context, R.color.black_888888));
        simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        simplePagerTitleView.setSelectedColor(ContextCompat.getColor(context, R.color.blue_54B1F8));
        simplePagerTitleView.setOnClickListener(v -> viewPager.setCurrentItem(index));
        return simplePagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
        indicator.setLineHeight(UIUtil.dip2px(context, 2));
//        indicator.setLineWidth(UIUtil.dip2px(context, 10));
        indicator.setRoundRadius(UIUtil.dip2px(context, 1));
        indicator.setStartInterpolator(new AccelerateInterpolator());
        indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
        indicator.setColors(Color.parseColor("#FF54B1F8"));
        return indicator;
    }
}
