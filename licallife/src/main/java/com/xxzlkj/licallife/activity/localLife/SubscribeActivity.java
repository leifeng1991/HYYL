package com.xxzlkj.licallife.activity.localLife;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.adapter.SubscribeCommonNavigatorAdapter;
import com.xxzlkj.licallife.fragment.SingleOrCycleSubscribeFragment;
import com.xxzlkj.licallife.model.CleanerDetailBean;
import com.xxzlkj.licallife.model.ProjectDetail;
import com.xxzlkj.shop.adapter.TabFragmentPagerAdapter;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.util.SystemBarUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 预约
 */
public class SubscribeActivity extends BaseActivity {
    private MagicIndicator mMagicIndicator;
    private ViewPager mViewPager;
    private ImageView mLeftImageView;
    private List<Fragment> fragments;
    // 保洁产品/保洁师id
    private String id;
    // 保洁项目
    private ProjectDetail.DataBean mProjectDataBean;
    // 保洁师
    private CleanerDetailBean.DataBean mCleanerDataBean;
    // 1:项目详情 2：保洁师详情 3:小时工
    private int mProductType;

    /**
     * @param context 上下文 （必传）
     * @param id      保洁项目id （必传）
     * @param data    产品详情 数据 （必传）
     * @return
     */
    public static Intent newIntent(Context context, String id, int type, ProjectDetail.DataBean data) {
        Intent intent = new Intent(context, SubscribeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(StringConstants.INTENT_PARAM_PROJECTDETAIL_DATABEAN, data);
        bundle.putString(StringConstants.INTENT_PARAM_ID, id);
        bundle.putInt(StringConstants.INTENT_PARAM_TYPE, type);
        intent.putExtras(bundle);
        return intent;
    }

    /**
     * @param context 上下文 （必传）
     * @param id      保洁师/小时工id （必传）
     * @param data    保洁师详情 数据 （必传）
     * @return
     */
    public static Intent newIntent(Context context, String id, int type, CleanerDetailBean.DataBean data) {
        Intent intent = new Intent(context, SubscribeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(StringConstants.INTENT_PARAM_PROJECTDETAIL_DATABEAN, data);
        bundle.putString(StringConstants.INTENT_PARAM_ID, id);
        bundle.putInt(StringConstants.INTENT_PARAM_TYPE, type);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_subscribe);
        // 解决使用Android-Picker控件时状态栏不变暗
        SystemBarUtils.setStatusBarTranslucent(this);
        SystemBarUtils.setStatusBarLightMode(this, true);
        SystemBarUtils.setPaddingTopStatusBarHeight(this, getView(R.id.id_parent_layout));
    }

    @Override
    protected void findViewById() {
        mMagicIndicator = getView(R.id.id_subscribe_indicator);
        mViewPager = getView(R.id.id_subscribe_vp);
        mLeftImageView = getView(R.id.id_title_left);
    }

    @Override
    protected void setListener() {
        mLeftImageView.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        init();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.id_title_left) {
            finish();

        }
    }

    private void init() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString(StringConstants.INTENT_PARAM_ID);
            mProductType = bundle.getInt(StringConstants.INTENT_PARAM_TYPE);
            switch (mProductType) {
                case 1:// 1:保洁项目
                    mProjectDataBean = (ProjectDetail.DataBean) bundle.getSerializable(StringConstants.INTENT_PARAM_PROJECTDETAIL_DATABEAN);
                    break;
                case 2://  2：保洁师
                case 3://  3：小时工
                    mCleanerDataBean = (CleanerDetailBean.DataBean) bundle.getSerializable(StringConstants.INTENT_PARAM_PROJECTDETAIL_DATABEAN);
                    break;
            }

        }

        //标题数组
        String[] titles = new String[1];
        //将fragment装进列表中
        fragments = new ArrayList<>();
        switch (mProductType) {
            case 1:// 1:保洁项目
                fragments.add(SingleOrCycleSubscribeFragment.newInstance(1, id, mProductType, mProjectDataBean));
                titles[0] = "保洁项目预约";
                break;
            case 2://  2：保洁师
                titles[0] = "保洁师预约";
                fragments.add(SingleOrCycleSubscribeFragment.newInstance(1, mProductType, mCleanerDataBean));
                break;
            case 3://  3：小时工
                titles[0] = "小时工预约";
                fragments.add(SingleOrCycleSubscribeFragment.newInstance(1, mProductType, mCleanerDataBean));
                break;
        }

        TabFragmentPagerAdapter mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), this, fragments, Arrays.asList(titles));

        //viewpager加载adapter
        mViewPager.setAdapter(mAdapter);
        //TabLayout加载viewpager
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new SubscribeCommonNavigatorAdapter(mViewPager, Arrays.asList(titles)));

        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }
}
