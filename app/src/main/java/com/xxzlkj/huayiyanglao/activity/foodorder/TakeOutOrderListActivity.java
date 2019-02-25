package com.xxzlkj.huayiyanglao.activity.foodorder;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.adapter.MyCommonNavigatorAdapter;
import com.xxzlkj.huayiyanglao.fragment.OrderListTabFragment;
import com.xxzlkj.zhaolinshare.base.base.BaseFragmentPagerAdapter;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.List;


/**
 * 描述:外卖订单列表
 *
 * @author zhangrq
 *         2017/3/21 14:48
 */
public class TakeOutOrderListActivity extends MyBaseActivity {
    public static final String INDEX = "index";
    private ViewPager viewPager;
    private MagicIndicator magicIndicator;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_take_out_order_list);
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
        setTitleName("订单列表");

        // 0.外卖订单
        titleList.add("外卖订单");
        fragmentList.add(OrderListTabFragment.newInstance("1"));
        // 1.到店订单
        titleList.add("到店订单");
        fragmentList.add(OrderListTabFragment.newInstance("2"));

        // 初始化viewPager和指示器
        BaseFragmentPagerAdapter fragmentPagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setOffscreenPageLimit(fragmentList.size());// 设置预加载的数量
        viewPager.setAdapter(fragmentPagerAdapter);
        // 指示器，稳健盈、灵活投
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setScrollPivotX(1f);
        MyCommonNavigatorAdapter indicatorAdapter = new MyCommonNavigatorAdapter(viewPager, titleList);
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
        Intent intent = new Intent(context, TakeOutOrderListActivity.class);
        intent.putExtra(INDEX, index);
        return intent;
    }
}
