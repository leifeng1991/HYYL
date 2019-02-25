package com.xxzlkj.huayiyanglao.activity.equipment;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.fragment.HeathAllInOneDataFragment;
import com.xxzlkj.huayiyanglao.fragment.HeathWatchDataFragment;
import com.xxzlkj.huayiyanglao.model.TiJianBean;
import com.xxzlkj.zhaolinshare.base.base.BaseFragmentPagerAdapter;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.base.TitleFragmentPagerAdapter;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.zrq.spanbuilder.Spans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 描述:健康数据
 *
 * @author zhangrq
 *         2017/3/21 14:48
 */
public class HealthDataActivity extends MyBaseActivity {
    private LinearLayout mWatchLinearLayout, mAllInOneLinearLayout;
    private TextView mWatchTextView, mAllInOneTextView;
    private View mWatchView, mAllInOneView;
    private ViewPager mViewPager;
    private boolean isWatch = true;

    public static Intent newIntent(Context context) {
        return new Intent(context, HealthDataActivity.class);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_health_data);
    }

    @Override
    protected void findViewById() {
        mWatchLinearLayout = getView(R.id.id_watch_layout);// 智能手表tab布局
        mWatchTextView = getView(R.id.id_watch_text);// 智能手表
        mWatchView = getView(R.id.id_watch_line);// 智能手表选中线
        mAllInOneLinearLayout = getView(R.id.id_all_in_on_layout);// 一体机tab布局
        mAllInOneTextView = getView(R.id.id_all_in_on_text);// 一体机
        mAllInOneView = getView(R.id.id_all_in_on_line);// 一体机选中线
        mViewPager = getView(R.id.id_view_pager);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(HeathWatchDataFragment.newInstance());
        fragmentList.add(HeathAllInOneDataFragment.newInstance());
        TitleFragmentPagerAdapter mPagerAdapter = new TitleFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(0);

    }

    @Override
    protected void setListener() {
        // 手表
        mWatchLinearLayout.setOnClickListener(v -> {
            isWatch = true;
            setState();
        });
        // 一体机
        mAllInOneLinearLayout.setOnClickListener(v -> {
            isWatch = false;
            setState();
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setState();
                isWatch = position == 0;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("健康数据");

    }

    private void setState() {
        mWatchTextView.setTextColor(ContextCompat.getColor(mContext, isWatch ? R.color.blue_54B1F8 : R.color.text_4));
        mAllInOneTextView.setTextColor(ContextCompat.getColor(mContext, isWatch ? R.color.text_4 : R.color.blue_54B1F8));
        mWatchView.setVisibility(isWatch ? View.VISIBLE : View.INVISIBLE);
        mAllInOneView.setVisibility(isWatch ? View.INVISIBLE : View.VISIBLE);
        // 选中智能手表
        if (mViewPager.getCurrentItem() != 0 && isWatch) {
            mViewPager.setCurrentItem(0);
        }
        // 选中一体机
        if (mViewPager.getCurrentItem() != 1 && !isWatch) {
            mViewPager.setCurrentItem(1);
        }
    }
}
