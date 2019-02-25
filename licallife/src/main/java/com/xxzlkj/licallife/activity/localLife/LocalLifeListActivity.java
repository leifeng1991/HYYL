package com.xxzlkj.licallife.activity.localLife;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.event.LocalLifeListCurrentTabEvent;
import com.xxzlkj.licallife.event.LocalLifeListTabRefreshEvent;
import com.xxzlkj.licallife.fragment.LocalLifeListTabFragment;
import com.xxzlkj.shop.adapter.MyCommonNavigatorAdapter;
import com.xxzlkj.shop.config.ShopConstants;
import com.xxzlkj.zhaolinshare.base.base.BaseFragmentPagerAdapter;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * 描述:本地生活列表
 *
 * @author zhangrq
 *         2017/3/21 14:48
 */
public class LocalLifeListActivity extends MyBaseActivity {
    private ViewPager viewPager;
    private MagicIndicator magicIndicator;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private BaseFragmentPagerAdapter fragmentPagerAdapter;
    private MyCommonNavigatorAdapter indicatorAdapter;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_local_life_list);
    }

    @Override
    protected void findViewById() {
        magicIndicator = getView(R.id.magic_indicator);
        magicIndicator.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white_fafbfd));
        viewPager = getView(R.id.viewPager);
    }

    @Override
    public void setListener() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void processLogic() {
        setTitleLeftBack();
        setTitleName("全部订单");

//        0或不传 全部订单 1待付款 2待服务 3服务中

        titleList.add("全部");
        fragmentList.add(LocalLifeListTabFragment.newInstance("0"));
        // 1待付款
        titleList.add("待付款");
        fragmentList.add(LocalLifeListTabFragment.newInstance("1"));
        // 2待发货
        titleList.add("待服务");
        fragmentList.add(LocalLifeListTabFragment.newInstance("2"));
        // 3配送中
        titleList.add("服务中");
        fragmentList.add(LocalLifeListTabFragment.newInstance("3"));

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

        // 设置当前的位置
        int intIndex = getIntent().getIntExtra(ShopConstants.Strings.INDEX, 0);
        viewPager.setCurrentItem(intIndex);
    }

    public static Intent newIntent(Context context, int index) {
        Intent intent = new Intent(context, LocalLifeListActivity.class);
        intent.putExtra(ShopConstants.Strings.INDEX, index);
        return intent;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void setItemRefresh(LocalLifeListTabRefreshEvent orderListTabEvent) {
        int position = orderListTabEvent.getPosition();
        if (viewPager == null || position >= viewPager.getChildCount())
            return;
        if (position == -1) {
            //代表当前页面刷新
            ((LocalLifeListTabFragment) fragmentList.get(viewPager.getCurrentItem())).refreshOnceData();// 会调用refreshOnceData方法刷新
        } else
            ((LocalLifeListTabFragment) fragmentList.get(position)).refreshOnceData();// 会调用refreshOnceData方法刷新
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void setCurrentItem(LocalLifeListCurrentTabEvent orderListCurrentTabEvent) {
        int position = orderListCurrentTabEvent.getPosition();
        if (viewPager == null || position >= viewPager.getChildCount())
            return;
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
