package com.xxzlkj.licallife.activity.localLife;


import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.fragment.JzAfterSaleTabFragment;
import com.xxzlkj.shop.adapter.MyCommonNavigatorAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseFragmentPagerAdapter;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.List;


/**
 * 售后/退款（本地生活）
 */
public class LocalLifeAfterSaleListActivity extends MyBaseActivity {
    private ViewPager viewPager;
    private MagicIndicator magicIndicator;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private BaseFragmentPagerAdapter fragmentPagerAdapter;
    private MyCommonNavigatorAdapter indicatorAdapter;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_after_sale_list);
    }

    @Override
    protected void findViewById() {
        magicIndicator = getView(R.id.magic_indicator);
        viewPager = getView(R.id.viewPager);
    }

    @Override
    public void setListener() {
    }

    @Override
    public void processLogic() {
        setTitleLeftBack();
        setTitleName("售后/退款");

        titleList.add("申请售后");
        fragmentList.add(JzAfterSaleTabFragment.newInstance(0));

        titleList.add("退款详情");
        fragmentList.add(JzAfterSaleTabFragment.newInstance(1));

        // 初始化viewPager和指示器
        fragmentPagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setOffscreenPageLimit(fragmentList.size());// 设置预加载的数量
        viewPager.setAdapter(fragmentPagerAdapter);
        // 指示器，稳健盈、灵活投
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setScrollPivotX(1f);
        indicatorAdapter = new MyCommonNavigatorAdapter(viewPager, titleList);
        commonNavigator.setAdapter(indicatorAdapter);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.iv_title_right) {// 消息
            ToastManager.showShortToast(mContext, "消息");

        }
    }
}
