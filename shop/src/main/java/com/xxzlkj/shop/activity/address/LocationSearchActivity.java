package com.xxzlkj.shop.activity.address;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.xxzlkj.shop.R;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.event.EditHarvestAddressEvent;
import com.xxzlkj.shop.event.LocationEvent;
import com.xxzlkj.shop.event.ShopHomeLocationEvent;
import com.xxzlkj.shop.utils.SearchViewUtil;
import com.xxzlkj.shop.utils.listview.CommBaseAdapter;
import com.xxzlkj.shop.utils.listview.ViewHolder;
import com.xxzlkj.shop.weight.xlistview.XListView;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 地址搜索（根据关键字进行搜索）
 */
public class LocationSearchActivity extends MyBaseActivity implements XListView.IXListViewListener, PoiSearch.OnPoiSearchListener {
    public static final String CITY_CODE = "city_code";
    private SearchView mSearchView;
    private TextView mCancelTextView;
    private XListView mLocationListView;
    // 页数
    private int mPage = 1;
    // 地址
    private CommBaseAdapter<PoiItem> mAdapter;
    // 搜索关键字
    private String keyword;
    private String cityCode;
    // 地位界面传进来的 1:编辑地址界面 2：从商城首页跳进来
    private int fromJumpObjectType = 1;

    /**
     * @param cityCode 城市编码（必传）
     * @param type     1:编辑地址界面 2：从商城首页跳进来（必传）
     * @return
     */
    public static Intent newIntent(Context context, String cityCode, int type) {
        Intent intent = new Intent(context, LocationSearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(CITY_CODE, cityCode);
        bundle.putInt(StringConstants.INTENT_PARAM_TYPE, type);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_location_search);
    }

    @Override
    protected void findViewById() {
        mSearchView = getView(R.id.id_hs_search);// 搜索框
        SearchView.SearchAutoComplete textView = (SearchView.SearchAutoComplete) mSearchView.findViewById(R.id.search_src_text);
        textView.setTextColor(0xff808080);
        textView.setTextSize(13);
        SearchViewUtil.setNoLine(mSearchView);
        SearchViewUtil.setSearchView(mSearchView);
        mCancelTextView = getView(R.id.id_hs_cancel);// 取消
        // 列表
        mLocationListView = (XListView) findViewById(R.id.id_loction_search_list);
        mLocationListView.setPullRefreshEnable(false);
        mLocationListView.setPullLoadEnable(true);
        mLocationListView.setXListViewListener(this);
        // 上个页面传来的值
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            cityCode = bundle.getString(CITY_CODE);
            // 地位界面传进来的 1:编辑地址界面 2：从商城首页跳进来
            fromJumpObjectType = bundle.getInt(StringConstants.INTENT_PARAM_TYPE);
        }

    }

    @Override
    protected void setListener() {
        // 取消监听事件
        mCancelTextView.setOnClickListener(this);
        // 搜索框监听事件
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {//点击软键盘搜索键回调
                keyword = query;
                mPage = 1;
                if (mAdapter != null && mAdapter.getCount() > 0) {
                    mAdapter.clearData();
                }
                searchKeyword(keyword);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {//内容改变回调
                return false;
            }
        });
        // 列表item点击事件
        mLocationListView.setOnItemClickListener((parent, view, position, id) -> {
            PoiItem mPoiItem = mAdapter.getItem(position - 1);
            LatLonPoint latLonPoint = mPoiItem.getLatLonPoint();
            EventBus.getDefault().postSticky(mPoiItem);
            EventBus.getDefault().postSticky(new LocationEvent(true));// 定位界面
            switch (fromJumpObjectType) {
                case 1: // 编辑地址
                    EventBus.getDefault().postSticky(new EditHarvestAddressEvent(mPoiItem.getCityName() +
                            mPoiItem.getAdName() + mPoiItem.getSnippet(), latLonPoint.getLatitude(), latLonPoint.getLongitude()));// 添加/编辑收货地址界面
                    break;
                case 2: // 商城首页
                    EventBus.getDefault().postSticky(new ShopHomeLocationEvent(mPoiItem));// 商城首页定位
                    break;
            }

            finish();
        });
    }

    @Override
    protected void processLogic() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.id_hs_cancel) {
            finish();
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        mPage++;
        searchKeyword(keyword);
    }

    /**
     * 关键字搜索
     */
    private void searchKeyword(String keyword) {
        PoiSearch.Query query = new PoiSearch.Query(keyword, "", cityCode);
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(mPage - 1);//设置查询页码
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        //开始搜索
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (poiResult != null) {
            ArrayList<PoiItem> pois = poiResult.getPois();
            if (pois != null && pois.size() > 0) {
                if (pois.size() < 20) {// 当前数据少于20条
                    mLocationListView.setPullLoadEnable(false);
                }
                if (mAdapter == null) {
                    mAdapter = new CommBaseAdapter<PoiItem>(this, R.layout.adapter_location_item, pois) {
                        @Override
                        protected void convert(ViewHolder viewHolder, PoiItem item, int position) {
                            if (TextUtils.isEmpty(item.getTitle())) {
                                viewHolder.setVisible(R.id.id_li_location, false);
                            } else {
                                viewHolder.setVisible(R.id.id_li_location, true);
                                viewHolder.setText(R.id.id_li_location, item.getTitle());
                            }

                            if (TextUtils.isEmpty(item.getSnippet())) {
                                viewHolder.setVisible(R.id.id_li_detail_location, false);
                            } else {
                                viewHolder.setVisible(R.id.id_li_detail_location, true);
                                viewHolder.setText(R.id.id_li_detail_location, item.getCityName() + item.getAdName() + item.getSnippet());
                            }
                        }
                    };
                    mLocationListView.setAdapter(mAdapter);
                } else {
                    mAdapter.addList(pois);
                    mLocationListView.stopLoadMore();
                }
            } else {// 数据为空时
                LogUtil.e("=============kong===============");
                mLocationListView.stopLoadMore();
            }

        } else {
            mLocationListView.stopLoadMore();
            mLocationListView.setPullLoadEnable(false);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}
