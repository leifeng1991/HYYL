package com.xxzlkj.shop.activity.shopOrder;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.adapter.MyCommonNavigatorAdapter;
import com.xxzlkj.shop.fragment.AfterSaleTabFragment;
import com.xxzlkj.zhaolinshare.base.base.BaseFragmentPagerAdapter;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.List;


/**
 * 描述:售后列表
 *
 * @author zhangrq
 *         2017/3/21 14:48
 */
public class AfterSaleListActivity extends MyBaseActivity {
    private ViewPager viewPager;
    private MagicIndicator magicIndicator;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private BaseFragmentPagerAdapter fragmentPagerAdapter;
    private MyCommonNavigatorAdapter indicatorAdapter;

    public static Intent newIntent(Context context ) {
        return new Intent(context, AfterSaleListActivity.class);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_after_sale_list);
    }

    @Override
    protected void findViewById() {
        magicIndicator = getView(R.id.magic_indicator);
        magicIndicator.setBackgroundResource(R.color.white_fafbfd);
        viewPager = getView(R.id.viewPager);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void processLogic() {
        setTitleLeftBack();
        setTitleName("售后/退款");
//        setTitleRightImage(R.mipmap.ic_order_message);

        titleList.add("申请售后");
        fragmentList.add(AfterSaleTabFragment.newInstance(0));

        titleList.add("退款详情");
        fragmentList.add(AfterSaleTabFragment.newInstance(1));

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
