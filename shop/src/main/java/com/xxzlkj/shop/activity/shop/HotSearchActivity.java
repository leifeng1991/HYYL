package com.xxzlkj.shop.activity.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.adapter.HistoryKeywordAdapter;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.model.HotKeyword;
import com.xxzlkj.shop.utils.SearchViewUtil;
import com.xxzlkj.shop.weight.HeaderRecyclerView;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;
import com.xxzlkj.zhaolinshare.base.util.PreferencesSaver;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 热搜
 */
public class HotSearchActivity extends MyBaseActivity {
    private HeaderRecyclerView mRecyclerViewHistory;
    private SearchView mSearchView;
    private TextView mCancelTextView;
    private LinearLayout mClearHistoryLayout;
    // 流式布局
    private TagFlowLayout mFlowLayout;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, HotSearchActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setHistoryData();
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_hot_search);
    }

    @Override
    protected void findViewById() {
        mRecyclerViewHistory = getView(R.id.id_hs_history_list);
        mRecyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
        mClearHistoryLayout = getView(R.id.id_clear_history);
        mCancelTextView = getView(R.id.id_hs_cancel);
        mFlowLayout = getView(R.id.id_hs_flowlayout);

        mSearchView = getView(R.id.id_hs_search);
        SearchView.SearchAutoComplete textView = (SearchView.SearchAutoComplete) mSearchView.findViewById(R.id.search_src_text);
        textView.setTextColor(0xff808080);
        textView.setTextSize(13);
        SearchViewUtil.setNoLine(mSearchView);
        SearchViewUtil.setSearchView(mSearchView);
        mSearchView.setQueryHint("请输入关键字…");
    }

    @Override
    protected void setListener() {
        mCancelTextView.setOnClickListener(this);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {//点击软键盘搜索键回调
                LogUtil.e(TAG, "======onQueryTextSubmit============" + query);
                addKeyword(query);
                startActivity(SearchGoodsListActivity.newIntent(HotSearchActivity.this, -1, "", query, ""));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {//内容改变回调
                LogUtil.e(TAG, "======onQueryTextChange============");
                return false;
            }
        });
        // 清除搜索历史
        mClearHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = PreferencesSaver.getStrList(mContext, StringConstants.PREFERENCES_KEY_KEYWORD_LIST);
                if (list != null && list.size() > 0) {
                    PreferencesSaver.removeAttr(mContext, StringConstants.PREFERENCES_KEY_KEYWORD_LIST);
                    ArrayList<String> lists = new ArrayList<>();
                    HistoryKeywordAdapter mAdapter = new HistoryKeywordAdapter(HotSearchActivity.this, R.layout.adapter_history_keyword_layout, new HistoryKeywordAdapter.OnIsHasKeywordListener() {
                        @Override
                        public void setIsHasKeyword(boolean isHasKeyword) {
                            if (isHasKeyword) {
                                mClearHistoryLayout.setVisibility(View.GONE);
                            } else {
                                mClearHistoryLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    mAdapter.addList(lists);
                    mRecyclerViewHistory.setAdapter(mAdapter);
                    mClearHistoryLayout.setVisibility(View.GONE);
                }
            }
        });

    }

    /**
     * 添加搜索历史关键字
     *
     * @param query
     */
    private void addKeyword(String query) {
        // 判断搜索历史里是否有盖关键字
        boolean flag = false;
        ArrayList<String> list = PreferencesSaver.getStrList(HotSearchActivity.this, StringConstants.PREFERENCES_KEY_KEYWORD_LIST);
        if (list == null) {
            list = new ArrayList<>();
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (query.equals(list.get(i))) {
                    flag = true;
                    break;
                }
            }
        }

        if (!flag) {// 搜索历史列表没有 加进去
            list.add(query);
            PreferencesSaver.putStrList(HotSearchActivity.this, StringConstants.PREFERENCES_KEY_KEYWORD_LIST, list);
        }
    }

    @Override
    protected void processLogic() {
        if (TextUtils.isEmpty(GlobalParams.storeId)) {
            ToastManager.showShortToast(mContext, getString(R.string.noShopServiceHint));
            finish();
        } else {
            getHotKeyword();
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.id_hs_cancel) {
            finish();

        }
    }

    /**
     * 设置搜索历史数据
     */
    private void setHistoryData() {
        ArrayList<String> list = PreferencesSaver.getStrList(HotSearchActivity.this, StringConstants.PREFERENCES_KEY_KEYWORD_LIST);
        if (list != null && list.size() > 0) {
            Collections.reverse(list);
            HistoryKeywordAdapter mAdapter = new HistoryKeywordAdapter(this, R.layout.adapter_history_keyword_layout, new HistoryKeywordAdapter.OnIsHasKeywordListener() {
                @Override
                public void setIsHasKeyword(boolean isHasKeyword) {
                    if (isHasKeyword) {
                        mClearHistoryLayout.setVisibility(View.GONE);
                    } else {
                        mClearHistoryLayout.setVisibility(View.VISIBLE);
                    }
                }
            });
            mAdapter.addList(list);
            mRecyclerViewHistory.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<String>() {
                @Override
                public void onClick(int position, String item) {
                    startActivity(SearchGoodsListActivity.newIntent(HotSearchActivity.this, -1, "", item, ""));
                }
            });
            mClearHistoryLayout.setVisibility(View.VISIBLE);
        } else {
            mClearHistoryLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 获取热门搜索关键字数据
     */
    private void getHotKeyword() {
        Map<String, String> map = new HashMap<>();
        RequestManager.createRequest(URLConstants.REQUEST_HOT_WORD, map, new OnMyActivityRequestListener<HotKeyword>(this) {
            @Override
            public void onSuccess(HotKeyword bean) {
                final List<String> words = bean.getData().getWord();
                if (words != null && words.size() > 0) {
                    // 设置数据
                    mFlowLayout.setAdapter(new TagAdapter<String>(words) {
                        @Override
                        public View getView(FlowLayout parent, int position, String o) {
                            TextView textView = (TextView) LayoutInflater.
                                    from(HotSearchActivity.this).inflate(R.layout.adapter_keyword_layout, mFlowLayout, false);
                            if (TextUtils.isEmpty(o)) {// 关键字为空
                                textView.setVisibility(View.GONE);
                            } else {// 关键字不为空
                                textView.setVisibility(View.VISIBLE);
                            }
                            textView.setText(o);
                            return textView;
                        }
                    });
                    // 点击事件
                    mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                        @Override
                        public boolean onTagClick(View view, int position, FlowLayout parent) {
                            addKeyword(words.get(position));
                            startActivity(SearchGoodsListActivity.newIntent(HotSearchActivity.this, -1, "", words.get(position), ""));
                            return true;
                        }
                    });
                }

            }
        });
    }
}
