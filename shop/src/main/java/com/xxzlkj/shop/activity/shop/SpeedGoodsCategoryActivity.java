package com.xxzlkj.shop.activity.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.fragment.SpeedGoodsCategoryFragment;
import com.xxzlkj.shop.model.GoodsCategoryTitle;
import com.xxzlkj.shop.utils.listview.CommBaseAdapter;
import com.xxzlkj.shop.utils.listview.ViewHolder;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.SystemBarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 时速达商品分类
 */
public class SpeedGoodsCategoryActivity extends BaseActivity {
    public static final String CURRENT_TITLE_ID = "currentTitleId";
    private ViewPager mViewPager;
    private List<Fragment> mFragments;
    private List<GoodsCategoryTitle.ItemBean> mTitles;
    private ListView mTitleListView;
    private CommBaseAdapter mCommBaseAdapter;
    private String mStoreId;
    private String currentTabId;

    /**
     * @param context        上下文  （必传）
     * @param storeId        选择地址或者定位后获取到的店铺id  （必传）
     * @param currentTitleId -1:预售，-2:团购，传0跳到兆邻商场第一个
     */
    public static Intent newIntent(Context context, String storeId, String currentTitleId) {
        Intent intent = new Intent(context, SpeedGoodsCategoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(StringConstants.INTENT_PARAM_STOREID, storeId);
        bundle.putString(CURRENT_TITLE_ID, currentTitleId);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_goods_category);
    }

    @Override
    protected void findViewById() {
        SystemBarUtils.setStatusBarLightMode(this, true);
        mFragments = new ArrayList<>();

        mViewPager = getView(R.id.id_gc_viewpager);
        mTitleListView = getView(R.id.id_title_list);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mStoreId = bundle.getString(StringConstants.INTENT_PARAM_STOREID);
            currentTabId = bundle.getString(CURRENT_TITLE_ID);
        }
    }

    @Override
    protected void setListener() {
        mTitleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mViewPager.setCurrentItem(position);
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTitleListView.setItemChecked(position, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("分类");
        setTitleRightImage(R.mipmap.search_icon);
        // 获取数据
        getCategoryTitles(mStoreId);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.iv_title_right) {
            startActivity(HotSearchActivity.newIntent(this));

        }
    }

    /**
     * 获取顶级分类Title
     */
    private void getCategoryTitles(String storeId) {
        Map<String, String> map = new HashMap<>();
        // 选择地址或者定位后获取到的店铺id
        map.put(URLConstants.REQUEST_PARAM_STORE_ID, storeId);
        RequestManager.createRequest(URLConstants.SHOP_TOP_GROUP_URL, map, new OnMyActivityRequestListener<GoodsCategoryTitle>(this) {
            @Override
            public void onSuccess(GoodsCategoryTitle bean) {
                // 时速达分类左边标题
                mTitles = bean.getData();
                mCommBaseAdapter = new CommBaseAdapter<GoodsCategoryTitle.ItemBean>(
                        SpeedGoodsCategoryActivity.this, R.layout.activity_goods_category_item, bean.getData()) {

                    @Override
                    protected void convert(ViewHolder viewHolder, GoodsCategoryTitle.ItemBean item, int position) {
                        viewHolder.setText(R.id.id_left_title, item.getTitle());
                    }
                };
                mTitleListView.setAdapter(mCommBaseAdapter);


                for (int i = 0; i < mTitles.size(); i++) {
                    mFragments.add(SpeedGoodsCategoryFragment.newInstance(mTitles.get(i).getId(), mStoreId));
                }
                mViewPager.setAdapter(new SpeedGoodsCategoryActivity.MyFragmentPagerAdapter(getSupportFragmentManager()));
                // 指定选中,-1:预售，-2:团购，传0跳到兆邻商场第一个
                setSelectByTabId(currentTabId);
            }
        });
    }

    /**
     * 设置位置选中
     *
     * @param currentTitleId -1:预售，-2:团购，传0跳到兆邻商场第一个
     */
    private void setSelectByTabId(String currentTitleId) {
        if (mTitles != null && mTitles.size() > 0) {
            int position = -1;
            for (int i = 0; i < mTitles.size(); i++) {
                GoodsCategoryTitle.ItemBean itemBean = mTitles.get(i);
                String titleId = itemBean.getId();
                if (TextUtils.isEmpty(titleId)) {
                    continue;
                }
                if (titleId.equals(currentTitleId)) {
                    // 有相同的id，跳到指定位置
                    position = i;
                    break;
                }
            }
            if (position == -1) {
                // 没有找到相同的id，跳到非团购、预售的第1个位置
                for (int i = 0; i < mTitles.size(); i++) {
                    GoodsCategoryTitle.ItemBean itemBean = mTitles.get(i);
                    String titleId = itemBean.getId();

                    if (!"-1".equals(titleId) && !"-2".equals(titleId)) {
                        // 有相同的id，跳到指定位置
                        position = i;
                        break;
                    }
                }
            }
            // 设置选中
            mTitleListView.setItemChecked(position, true);
            mViewPager.setCurrentItem(position);
        }

    }

    private class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

        MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
