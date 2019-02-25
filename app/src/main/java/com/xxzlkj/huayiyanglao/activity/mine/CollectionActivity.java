package com.xxzlkj.huayiyanglao.activity.mine;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.event.CollectEvent;
import com.xxzlkj.huayiyanglao.fragment.CollectFragment;
import com.xxzlkj.shop.adapter.BhCommonNavigatorAdapter;
import com.xxzlkj.shop.adapter.TabFragmentPagerAdapter;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 收藏
 */
public class CollectionActivity extends MyBaseActivity {
    private ImageView mLeftImageView;
    private TextView mRightTextView;
    private TextView mClearTextView;
    private TextView mCancelTextView;
    private ViewPager mViewPager;
    private RelativeLayout mBottomRelativeLayout;
    private CheckBox mAllCheckBox;
    private boolean isSateFlag;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_browsing_history);
    }

    @Override
    protected void findViewById() {
        mLeftImageView = getView(R.id.id_title_left);// 返回键
        mRightTextView = getView(R.id.id_title_right);// 编辑
        MagicIndicator mMagicIndicator = getView(R.id.magic_indicator);// tab
        mViewPager = getView(R.id.id_bh_vp);// viewpager
        mBottomRelativeLayout = getView(R.id.id_collect_bottom_layout);// 底部布局
        mClearTextView = getView(R.id.id_collect_clear);// 清除失效商品
        mCancelTextView = getView(R.id.id_collect_cancel);// 取消收藏
        mAllCheckBox = getView(R.id.id_collect_all_checkbox);// 全选

        //标题数组
        String[] titles = getResources().getStringArray(R.array.browsingHistory);
        //将fragment装进列表中
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            fragments.add(CollectFragment.newInstance(i));
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
        mClearTextView.setOnClickListener(this);
        mCancelTextView.setOnClickListener(this);
        // viewpager监听事件
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){// 商品收藏
                    mRightTextView.setVisibility(View.VISIBLE);
                    if ("完成".equals(mRightTextView.getText().toString())){
                        mBottomRelativeLayout.setVisibility(View.VISIBLE);
                    }else {
                        mBottomRelativeLayout.setVisibility(View.GONE);
                    }
                }else {
                    mRightTextView.setVisibility(View.GONE);
                    mBottomRelativeLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 全选
        mAllCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LogUtil.e("==============onCheckedChanged",isChecked+"");
                if (!isSateFlag){
                    EventBus.getDefault().postSticky(new CollectEvent(false,isChecked,false,"","ss"));
                }
                isSateFlag = false;
            }
        });
    }

    @Override
    protected void processLogic() {
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.id_title_left:
                finish();
                break;
            case R.id.id_title_right:
                String mEditStr = mRightTextView.getText().toString();
                if ("编辑".equals(mEditStr)){
                    mRightTextView.setText("完成");
                    mBottomRelativeLayout.setVisibility(View.VISIBLE);
                    EventBus.getDefault().postSticky(new CollectEvent(true,false,false,"ss",""));
                }else if ("完成".equals(mEditStr)){
                    mRightTextView.setText("编辑");
                    mBottomRelativeLayout.setVisibility(View.GONE);
                    EventBus.getDefault().postSticky(new CollectEvent(false,false,false,"ss",""));
                }
                break;
            case R.id.id_collect_clear://清除失效商品
                EventBus.getDefault().postSticky(new CollectEvent(true));
                break;
            case R.id.id_collect_cancel://取消收藏
                EventBus.getDefault().postSticky(new CollectEvent(false,false,true,"",""));
                break;
        }
    }

    /**
     * 设置总开关状态
     * @param state
     */
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
