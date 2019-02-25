package com.xxzlkj.huayiyanglao.activity.address;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.adapter.LocationAdapter;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.util.SearchViewUtil;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;

import java.util.ArrayList;


/**
 * 地址搜索（根据关键字进行搜索）
 */
public class LocationSearchActivity extends MyBaseActivity implements PoiSearch.OnPoiSearchListener {
    public static final String CITY_CODE = "city_code";
    private SearchView mSearchView;
    private TextView mCancelTextView;
    private MyRecyclerView mMyRecyclerView;
    private LocationAdapter mLocationAdapter;
    // 页数
    private int mPage = 1;
    // 搜索关键字
    private String keyword;
    private String cityCode;


    /**
     * @param cityCode 城市编码（必传）
     * @return
     */
    public static Intent newIntent(Context context, String cityCode) {
        Intent intent = new Intent(context, LocationSearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(CITY_CODE, cityCode);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_local_search);
    }

    @Override
    protected void findViewById() {
        mSearchView = getView(R.id.id_hs_search);// 搜索框
        SearchView.SearchAutoComplete textView = mSearchView.findViewById(R.id.search_src_text);
        textView.setTextColor(0xff808080);
        textView.setTextSize(13);
        SearchViewUtil.setNoLine(mSearchView);
        SearchViewUtil.setSearchView(mSearchView);
        mCancelTextView = getView(R.id.id_hs_cancel);// 取消
        // 列表
        mMyRecyclerView = (MyRecyclerView) findViewById(R.id.id_recyclerview);
        mMyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMyRecyclerView.setPullRefreshAndLoadingMoreEnabled(false, true);
        mLocationAdapter = new LocationAdapter(this, R.layout.adapter_location_item);
        mMyRecyclerView.setAdapter(mLocationAdapter);
        mLocationAdapter.setLastSelection(-1);

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
                if (mLocationAdapter != null && mLocationAdapter.getItemCount() > 0) {
                    mLocationAdapter.clear();
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
        mLocationAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<PoiItem>() {
            @Override
            public void onClick(int position, PoiItem item) {
                Intent intent = new Intent();
                intent.putExtra(ZLConstants.Strings.POI_ITEM, item);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        // 分页和刷新
        mMyRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mPage++;
                searchKeyword(keyword);
            }
        });
    }

    @Override
    protected void processLogic() {
        // 上个页面传来的值
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            cityCode = bundle.getString(CITY_CODE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.id_hs_cancel:
                finish();
                break;
        }
    }

    /**
     * @param keyword 关键字搜索
     */
    private void searchKeyword(String keyword) {
        PoiSearch.Query query = new PoiSearch.Query(keyword, "", cityCode);
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(mPage);//设置查询页码
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        //开始搜索
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (poiResult != null) {
            ArrayList<PoiItem> pois = poiResult.getPois();
            mMyRecyclerView.handlerSuccessHasRefreshAndLoad(mLocationAdapter, mPage == 1, pois);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

}
