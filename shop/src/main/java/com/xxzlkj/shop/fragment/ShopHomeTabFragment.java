package com.xxzlkj.shop.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.ShopHomeActivity;
import com.xxzlkj.zhaolinshare.base.base.ViewPageFragment;

import java.util.ArrayList;

/**
 * 描述:商城首页栏目，控制显示One还是Two Fragment
 *
 * @author zhangrq
 *         2017/6/26 10:17
 */
public class ShopHomeTabFragment extends ViewPageFragment implements IOnFragmentBackPressed {
    public static final String TYPE = "type";
    private ArrayList<IOnFragmentBackPressed> fragmentBackPressedList = new ArrayList<>();//初始化list
    private ShopHomeOneTabFragment oneFragment;
    private ShopHomeTwoTabFragment twoFragment;

    public static Bundle newBundle(String type) {
        Bundle extras = new Bundle();
        extras.putString(TYPE, type);
        return extras;
    }

    /**
     * @param type 5商城首页 2 火爆预售;3 兆邻团购 ;
     */
    public static ShopHomeTabFragment newInstance(String type) {
        ShopHomeTabFragment fragment = new ShopHomeTabFragment();
        fragment.setArguments(newBundle(type));
        return fragment;
    }

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_tab_shop_home, container, false);
    }

    @Override
    protected void findViewById() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void processLogic() {
        // 设置one Fragment
        oneFragment = ShopHomeOneTabFragment.newInstance(this, getArguments());// 把这个里面的全部参数传给子类，具体参数看本类传值
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.rl_root, oneFragment, "One").addToBackStack(null).commitAllowingStateLoss();
    }

    /**
     * 从 One 跳转到Two Fragment，隐藏One Fragment，后跳到Two Fragment
     */
    public void jumpToTwoFragment(String twoTabId) {
        // 为了动画效果隐藏One Fragment，再add显示Two Fragment
        // 隐藏One Fragment
        hideOneFragment();
        // 增加显示Two Fragment
        // 里面存有经度、纬度 或 门店id
        twoFragment = ShopHomeTwoTabFragment.newInstance(this, getArguments(), twoTabId);// 把这个里面的全部参数传给子类，具体参数看本类传值
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.translate_right2myself, R.anim.translate_myself2right,
                R.anim.translate_right2myself, R.anim.translate_myself2right);
        transaction.add(R.id.rl_root, twoFragment, "Two").addToBackStack(null).commitAllowingStateLoss();
    }

    /**
     * 隐藏One Fragment
     */
    private void hideOneFragment() {
        if (oneFragment != null) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.setCustomAnimations(
                    R.anim.translate_left2myself, R.anim.translate_myself2left,
                    R.anim.translate_left2myself, R.anim.translate_myself2left);
            transaction.hide(oneFragment).commitAllowingStateLoss();
        }
    }

    /**
     * 展示One Fragment
     */
    public void showOneFragment() {
        if (oneFragment != null && oneFragment.isHidden()) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.setCustomAnimations(
                    R.anim.translate_left2myself, R.anim.translate_myself2left,
                    R.anim.translate_left2myself, R.anim.translate_myself2left);
            transaction.show(oneFragment).commitAllowingStateLoss();
            // 展示按钮
            ((ShopHomeActivity) mActivity).setCurrentFragment(true);
        }
    }

    @Override
    public boolean onBackPressed() {
        if (fragmentBackPressedList != null && fragmentBackPressedList.size() > 0) {//判断一下当前是否有fragment
            int size = fragmentBackPressedList.size();
            if (fragmentBackPressedList.get(size - 1).onBackPressed()) {//调用fragment的回退事件（每个fragment的回退逻辑代码都放在里面）
                //这里true表示当前的fragment不做任何操做，把回退事件交给它的上一级
                fragmentBackPressedList.remove(size - 1);//将当前的fragment从集合中移除
                onBackPressed();//再次调用activity的回退
            } else {
                //这里表示回退事件已经被当前fragment给消费了
                fragmentBackPressedList.remove(size - 1);
            }
        } else if (mActivity != null) {
            mActivity.finish();
        }
        return true;
    }

    //重写fragment的setFragment方法，将fragment放入activity的集合中
    public void addFragmentBackPressed(IOnFragmentBackPressed fragment) {
        fragmentBackPressedList.add(fragment);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fragmentBackPressedList != null) {
            fragmentBackPressedList.clear();
            fragmentBackPressedList = null;
        }
    }

    /**
     * 是否此Fragment没有子布局已经到顶部了
     */
    public boolean isGoTop() {
        return fragmentBackPressedList.size() == 0;
    }

    /**
     * 5商城首页 2 火爆预售;3 兆邻团购 ;
     */
    public String getType() {
        Bundle arguments = getArguments();
        return arguments == null ? "" : arguments.getString(TYPE);
    }

    @Override
    public void refreshOnceData() {
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaa");
    }

    public void setRefresh() {
        if (twoFragment != null && twoFragment.isVisible())
            // twoFragment 显示、先刷 twoFragment
            twoFragment.setRefresh();
        else if (oneFragment != null && oneFragment.isVisible())
            // oneFragment 显示、再刷 oneFragment
            oneFragment.setRefresh();
    }
}
