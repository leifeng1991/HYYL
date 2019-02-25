package com.xxzlkj.shop.activity.shopOrder;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.shop.HotSearchActivity;
import com.xxzlkj.shop.adapter.MyCommonNavigatorAdapter;
import com.xxzlkj.shop.event.OrderListCurrentTabEvent;
import com.xxzlkj.shop.event.OrderListTabRefreshEvent;
import com.xxzlkj.shop.fragment.OrderListTabFragment;
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
 * 描述:订单列表
 *
 * @author zhangrq
 *         2017/3/21 14:48
 */
public class OrderListActivity extends MyBaseActivity {
    public static final String INDEX = "index";
    private ViewPager viewPager;
    private MagicIndicator magicIndicator;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private BaseFragmentPagerAdapter fragmentPagerAdapter;
    private MyCommonNavigatorAdapter indicatorAdapter;
    private ImageView iv_title_right_search, iv_title_right_message;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_order_list);
    }

    @Override
    protected void findViewById() {
        magicIndicator = getView(R.id.magic_indicator);
        magicIndicator.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white_fafbfd));
        viewPager = getView(R.id.viewPager);
        iv_title_right_search = getView(R.id.iv_title_right_search);
        iv_title_right_message = getView(R.id.iv_title_right_message);
    }

    @Override
    public void setListener() {
        EventBus.getDefault().register(this);
        iv_title_right_search.setOnClickListener(this);
        iv_title_right_message.setOnClickListener(this);
    }

    @Override
    public void processLogic() {
        setTitleLeftBack();
        setTitleName("全部订单");

        // 0全部
        // 全部则不传 2待发货 3配送中（细分分为2种：配送中，待确认）  1待付款 6订单退款（细分分为3种：退款中，系统退款，已退款） 4已完成 5已取消
//        0或不传 全部订单 1待付款 2待发货 3待收货

        titleList.add("全部");
        fragmentList.add(OrderListTabFragment.newInstance("0"));
        // 1待付款
        titleList.add("待付款");
        fragmentList.add(OrderListTabFragment.newInstance("1"));
        // 2待发货
        titleList.add("待发货");
        fragmentList.add(OrderListTabFragment.newInstance("2"));
        // 3配送中
        titleList.add("待收货");
        fragmentList.add(OrderListTabFragment.newInstance("3"));

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
        int intIndex = getIntent().getIntExtra(INDEX, 0);
        viewPager.setCurrentItem(intIndex);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.iv_title_right_search) {// 搜索
            startActivity(HotSearchActivity.newIntent(mContext));
        }
    }

    public static Intent newIntent(Context context, int index) {
        Intent intent = new Intent(context, OrderListActivity.class);
        intent.putExtra(INDEX, index);
        return intent;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void setItemRefresh(OrderListTabRefreshEvent orderListTabEvent) {
        int position = orderListTabEvent.getPosition();
        if (viewPager == null || position >= viewPager.getChildCount())
            return;
        ((OrderListTabFragment) fragmentList.get(position)).refreshOnceData();// 会调用refreshOnceData方法刷新
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void setCurrentItem(OrderListCurrentTabEvent orderListCurrentTabEvent) {
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
