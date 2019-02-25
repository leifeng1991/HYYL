package com.xxzlkj.shop.activity.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.fragment.GoodsCategoryFragment;
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
 * 商品分类
 */
public class GoodsCategoryActivity extends BaseActivity {
    private ViewPager mViewPager;
    private List<Fragment> mFragments;
    private List<GoodsCategoryTitle.ItemBean> mTitles;
    private ListView mTitleListView;
    private CommBaseAdapter mCommBaseAdapter;
    // 分类标题
    private String title;

    /**
     * @param context 上下文 （必传）
     * @param title   分类标题 为""或者为null时默认为0 (必传)
     * @return
     */
    public static Intent newIntent(Context context, String title) {
        Intent intent = new Intent(context, GoodsCategoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(StringConstants.INTENT_PARAM_TITLE, title);
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
            title = bundle.getString(StringConstants.INTENT_PARAM_TITLE);
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
        getCategoryTitles();
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
    private void getCategoryTitles() {
        Map<String, String> map = new HashMap<>();
        RequestManager.createRequest(URLConstants.REQUEST_TOP_GROUP, map, new OnMyActivityRequestListener<GoodsCategoryTitle>(this) {
            @Override
            public void onSuccess(GoodsCategoryTitle bean) {
                mTitles = bean.getData();
                int currentIndex = getIndex();
                mCommBaseAdapter = new CommBaseAdapter<GoodsCategoryTitle.ItemBean>(
                        GoodsCategoryActivity.this, R.layout.activity_goods_category_item, bean.getData()) {

                    @Override
                    protected void convert(ViewHolder viewHolder, GoodsCategoryTitle.ItemBean item, int position) {
                        viewHolder.setText(R.id.id_left_title, item.getTitle());
                    }
                };
                mTitleListView.setAdapter(mCommBaseAdapter);
                mTitleListView.setItemChecked(currentIndex, true);

                for (int i = 0; i < mTitles.size(); i++) {
                    mFragments.add(GoodsCategoryFragment.newInstance(mTitles.get(i).getId()));
                }
                mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
                // 放在setAdapter之后 否则会失效
                mViewPager.setCurrentItem(currentIndex);
            }
        });
    }

    /**
     * 获得默认当前页下坐标
     *
     * @return
     */
    private int getIndex() {
        int index = 0;
        for (int i = 0; i < mTitles.size(); i++) {
            if (!TextUtils.isEmpty(title)) {
                GoodsCategoryTitle.ItemBean itemBean = mTitles.get(i);
                if (TextUtils.equals(title, itemBean.getTitle())) {
                    index = i;
                }
            }
        }
        return index;
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

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
