package com.xxzlkj.shop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.model.ShopNavBean;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;

import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeAnchor;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeRule;

import java.util.List;


/**
 * 描述:
 *
 * @author zhangrq
 *         2016/12/12 20:41
 */

public class ShopHomeCommonNavigatorAdapter extends CommonNavigatorAdapter {

    private final ViewPager viewPager;
    private final List<ShopNavBean.DataBean> mDataList;

    public ShopHomeCommonNavigatorAdapter(ViewPager viewPager, List<ShopNavBean.DataBean> mDataList) {
        this.viewPager = viewPager;
        this.mDataList = mDataList;
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {

        final BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);

        SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
        ShopNavBean.DataBean dataBean = mDataList.get(index);
        simplePagerTitleView.setText(dataBean == null ? "" : dataBean.getTitle());
        simplePagerTitleView.setTextSize(14);
        simplePagerTitleView.setNormalColor(ContextCompat.getColor(context, R.color.black_777777));
        simplePagerTitleView.setSelectedColor(ContextCompat.getColor(context, R.color.orange_ff725c));
        simplePagerTitleView.setPadding(getCount() == 4 ? 0 : PixelUtil.dip2px(context, 18), 0, getCount() == 4 ? 0 : PixelUtil.dip2px(context, 18), 0);
        simplePagerTitleView.setOnClickListener(v -> viewPager.setCurrentItem(index));
        badgePagerTitleView.setInnerPagerTitleView(simplePagerTitleView);
        // 设置标记view
        if (dataBean != null && !TextUtils.isEmpty(dataBean.getSimg())) {
            // 有图片地址，设置标记
            View badgeView = LayoutInflater.from(context).inflate(R.layout.view_shop_home_badge_layout, null);
            ImageView badgeImageView = (ImageView) badgeView.findViewById(R.id.iv_badge_title);
            ViewGroup.LayoutParams layoutParams = badgeImageView.getLayoutParams();
            int h = NumberFormatUtils.toInt(dataBean.getH());
            int w = NumberFormatUtils.toInt(dataBean.getW());
            layoutParams.width = w * layoutParams.height / h;
            badgeImageView.setLayoutParams(layoutParams);
            PicassoUtils.setImageBig(context, dataBean.getSimg(), badgeImageView);
            badgePagerTitleView.setBadgeView(badgeView);
            badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CENTER_X, 0));
            badgePagerTitleView.setYBadgeRule(new BadgeRule(BadgeAnchor.TOP, UIUtil.dip2px(context, 2)));
        } else
            badgePagerTitleView.setBadgeView(null);

        // don't cancel badge when tab selected
        badgePagerTitleView.setAutoCancelBadge(false);
        return badgePagerTitleView;
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
        indicator.setColors(Color.parseColor("#ff0000"));
        return indicator;
    }
}
