package com.xxzlkj.huayiyanglao.activity.yiyangyiliao;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.adapter.YLCommonNavigatorAdapter;
import com.xxzlkj.huayiyanglao.fragment.YLOrderListTabFragment;
import com.xxzlkj.zhaolinshare.base.base.BaseFragmentPagerAdapter;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.List;


/**
 * 描述:医养医疗订单列表
 *
 * @author zhangrq
 *         2017/3/21 14:48
 */
public class YLOrderListActivity extends MyBaseActivity {
    public static final String INDEX = "index";
    private ViewPager viewPager;
    private MagicIndicator magicIndicator;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_yyyl_order_list);
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
        setTitleName("医疗预约订单");
        // 待付款
        titleList.add("待付款");
        fragmentList.add(YLOrderListTabFragment.newInstance(1, viewPager));
        // 待服务
        titleList.add("待服务");
        fragmentList.add(YLOrderListTabFragment.newInstance(2, viewPager));
        // 已失效
        titleList.add("已失效");
        fragmentList.add(YLOrderListTabFragment.newInstance(5, viewPager));
        // 已完成
        titleList.add("已完成");
        fragmentList.add(YLOrderListTabFragment.newInstance(4, viewPager));

        // 初始化viewPager和指示器
        BaseFragmentPagerAdapter fragmentPagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setOffscreenPageLimit(fragmentList.size());// 设置预加载的数量
        viewPager.setAdapter(fragmentPagerAdapter);
        // 指示器，稳健盈、灵活投
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setScrollPivotX(1f);
        YLCommonNavigatorAdapter indicatorAdapter = new YLCommonNavigatorAdapter(viewPager, titleList);
        commonNavigator.setAdapter(indicatorAdapter);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);

        // 设置当前的位置
        int intIndex = getIntent().getIntExtra(INDEX, 0);
        viewPager.setCurrentItem(intIndex);
    }

    /**
     * @param index 要跳到的位置
     */
    public static Intent newIntent(Context context, int index) {
        Intent intent = new Intent(context, YLOrderListActivity.class);
        intent.putExtra(INDEX, index);
        return intent;
    }
}
