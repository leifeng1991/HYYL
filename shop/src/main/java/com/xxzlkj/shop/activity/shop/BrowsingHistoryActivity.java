package com.xxzlkj.shop.activity.shop;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.adapter.BhCommonNavigatorAdapter;
import com.xxzlkj.shop.adapter.TabFragmentPagerAdapter;
import com.xxzlkj.shop.event.BrowserHistoryEvent;
import com.xxzlkj.shop.fragment.BrowsingHistoryFragment;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 浏览记录
 */
public class BrowsingHistoryActivity extends MyBaseActivity {
    private ImageView mLeftImageView;
    private TextView mRightTextView;
    private TextView mDeleteTextView;
    private MagicIndicator mMagicIndicator;
    private ViewPager mViewPager;
    private List<Fragment> fragments;
    private RelativeLayout mBottomRelativeLayout;
    private CheckBox mAllCheckBox;
    private static boolean isSateFlag = false;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_browsing_history);
    }

    @Override
    protected void findViewById() {
        mLeftImageView = getView(R.id.id_title_left);
        mRightTextView = getView(R.id.id_title_right);
        mDeleteTextView = getView(R.id.id_bh_delete);
        mMagicIndicator = getView(R.id.magic_indicator);
        mViewPager = getView(R.id.id_bh_vp);
        mBottomRelativeLayout = getView(R.id.id_bh_bottom_layout);
        mAllCheckBox = getView(R.id.id_bh_all_checkbox);

        //标题数组
        String[] titles = getResources().getStringArray(R.array.browsingHistory);
        //将fragment装进列表中
        fragments = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            fragments.add(BrowsingHistoryFragment.newInstance(i));
        }

        TabFragmentPagerAdapter mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), this, fragments, Arrays.asList(titles));

        //viewpager加载adapter
        mViewPager.setAdapter(mAdapter);
        //TabLayout加载viewpager
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new BhCommonNavigatorAdapter(mViewPager, Arrays.asList(titles)));
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

    @Override
    protected void setListener() {
        mLeftImageView.setOnClickListener(this);
        mRightTextView.setOnClickListener(this);
        mDeleteTextView.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        mAllCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isSateFlag){
                EventBus.getDefault().postSticky(new BrowserHistoryEvent(false,mAllCheckBox.isChecked(),"","ss",false));
            }
            isSateFlag = false;
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.id_title_left) {
            finish();
        } else if (i == R.id.id_title_right) {
            String mEditStr = mRightTextView.getText().toString();
            if ("编辑".equals(mEditStr)) {
                mRightTextView.setText("完成");
                mBottomRelativeLayout.setVisibility(View.VISIBLE);
                EventBus.getDefault().postSticky(new BrowserHistoryEvent(true, false, "ss", "", false));
            } else if ("完成".equals(mEditStr)) {
                mRightTextView.setText("编辑");
                mBottomRelativeLayout.setVisibility(View.GONE);
                EventBus.getDefault().postSticky(new BrowserHistoryEvent(false, false, "ss", "", false));
            }
        } else if (i == R.id.id_bh_delete) {
            EventBus.getDefault().postSticky(new BrowserHistoryEvent(false, false, "", "", true));

        }
    }

    public void setCheckboxSate(boolean state){
        isSateFlag = true;
        mAllCheckBox.setChecked(state);
        isSateFlag = false;
    }

    /**
     * 底部控件的显示和隐藏
     * @param isVisible
     */
    public void setBottomLayoutVisible(boolean isVisible){
        if (isVisible){
            mBottomRelativeLayout.setVisibility(View.VISIBLE);
        }else {
            mBottomRelativeLayout.setVisibility(View.GONE);
            mRightTextView.setText("编辑");
        }
    }
}
