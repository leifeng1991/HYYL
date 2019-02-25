package com.xxzlkj.licallife.activity.localLife;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.adapter.SubscribeCommonNavigatorAdapter;
import com.xxzlkj.licallife.fragment.CleanProjectListFragment;
import com.xxzlkj.licallife.fragment.CleanerAndHourlyWorkerListFragment;
import com.xxzlkj.shop.adapter.BhCommonNavigatorAdapter;
import com.xxzlkj.shop.adapter.TabFragmentPagerAdapter;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 保洁项目、保洁师、小时工
 */
public class ProjectCleaningAndHourlyWorkerActivity extends MyBaseActivity {
    private MagicIndicator mMagicIndicator;
    private ViewPager mViewPager;
    private ImageView mLeftImageView;
    private List<Fragment> fragments;
    private int mJumpType;

    /**
     * @param context  上下文 （必传）
     * @param jumpType 1/3:保洁项目和保洁师 2：小时工 4:只有保洁项目（必传）
     * @return
     */
    public static Intent newIntent(Context context, int jumpType) {
        Intent intent = new Intent(context, ProjectCleaningAndHourlyWorkerActivity.class);
        intent.putExtra(StringConstants.INTENT_PARAM_TYPE, jumpType);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_project_clean);
    }

    @Override
    protected void findViewById() {
        mMagicIndicator = getView(R.id.id_jz_magic_indicator);
        mViewPager = getView(R.id.id_hh_vp);
        mLeftImageView = getView(R.id.id_title_left);

    }

    @Override
    protected void setListener() {
        mLeftImageView.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        if (TextUtils.isEmpty(GlobalParams.communityId) || !GlobalParams.isOpenLocalLife){
            ToastManager.showShortToast(mContext, getString(R.string.noLocalLifeServiceHint));
            finish();
        }else {
            mJumpType = getIntent().getIntExtra(StringConstants.INTENT_PARAM_TYPE, 1);
            String[] titles;
            switch (mJumpType){
                case 1:// 保洁项目和保洁师
                case 3:// 保洁项目和保洁师
                    //标题数组
                    titles = getResources().getStringArray(R.array.houserkeepingTitle);
                    initData(titles, new BhCommonNavigatorAdapter(mViewPager, Arrays.asList(titles)));
                    // 当为3时设置viewpager当前为保洁师列表
                    if (mJumpType == 3){
                        mViewPager.setCurrentItem(1);
                    }
                    break;
                case 2:// 小时工
                    //标题数组
                    titles = new String[]{"小时工"};
                    initData(titles, new SubscribeCommonNavigatorAdapter(mViewPager, Arrays.asList(titles)));
                    break;
                case 4:// 只有保洁项目
                    titles = new String[]{"保洁项目"};
                    initData(titles, new SubscribeCommonNavigatorAdapter(mViewPager, Arrays.asList(titles)));
                    break;
            }
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.id_title_left) {
            finish();

        }
    }

    private void initData(String[] titles, CommonNavigatorAdapter commonNavigatorAdapter) {
        //将fragment装进列表中
        fragments = new ArrayList<>();
        if (mJumpType == 1 || mJumpType == 3 || mJumpType == 4) {
            // 保洁项目
            fragments.add(CleanProjectListFragment.newInstance());
        }

        if (mJumpType != 4){
            // 保洁师/小时工
            fragments.add(CleanerAndHourlyWorkerListFragment.newInstance(mJumpType));
        }

        TabFragmentPagerAdapter mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), this, fragments, Arrays.asList(titles));

        //viewpager加载adapter
        mViewPager.setAdapter(mAdapter);
        //TabLayout加载viewpager
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(commonNavigatorAdapter);
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }
}
