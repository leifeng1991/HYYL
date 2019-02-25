package com.xxzlkj.shop.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.model.ShopAppBean;
import com.xxzlkj.shop.weight.MyPagerTitleView;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;

import java.util.List;


/**
 * 描述:
 *
 * @author zhangrq
 *         2016/12/12 20:41
 */

public class ShopHomeTwoTabFragmentNavigatorAdapter extends CommonNavigatorAdapter {

    private final ViewPager viewPager;
    private final List<ShopAppBean.DataBean> mDataList;

    public ShopHomeTwoTabFragmentNavigatorAdapter(ViewPager viewPager, List<ShopAppBean.DataBean> mDataList) {
        this.viewPager = viewPager;
        this.mDataList = mDataList;
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        MyPagerTitleView simplePagerTitleView = new MyPagerTitleView(context);
        TextView textView = simplePagerTitleView.getTextView();
        ImageView imageView = simplePagerTitleView.getImageView();
        // 初始化
        simplePagerTitleView.setNormalColor(ContextCompat.getColor(context, R.color.black_888888));
        simplePagerTitleView.setSelectedColor(ContextCompat.getColor(context, R.color.orange_ff725c));
        simplePagerTitleView.setOnClickListener(v -> viewPager.setCurrentItem(index));
        // 设置值
        textView.setText(mDataList.get(index).getTitle());
        PicassoUtils.setImageSmall(context, mDataList.get(index).getSimg(), imageView);
        return simplePagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        return null;
    }
}
