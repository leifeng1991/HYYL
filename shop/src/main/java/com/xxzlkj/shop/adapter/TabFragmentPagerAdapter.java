package com.xxzlkj.shop.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;
    private List<String> mFragmentsTitles;
    private Context context;

    public TabFragmentPagerAdapter(FragmentManager fm, List<Fragment> mFragments, List<String> mFragmentsTitles) {
        super(fm);
        this.mFragments = mFragments;
        this.mFragmentsTitles = mFragmentsTitles;
    }

    public TabFragmentPagerAdapter(FragmentManager fm, Context context, List<Fragment> mFragments, List<String> mFragmentsTitles) {
        super(fm);
        this.mFragments = mFragments;
        this.mFragmentsTitles = mFragmentsTitles;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        //得到对应position的Fragment
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        //返回Fragment的数量
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //得到对应position的Fragment的title
        return mFragmentsTitles.get(position);
    }
}
