package com.xxzlkj.licallife.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.activity.localLife.ProjectDetailActivity;
import com.xxzlkj.licallife.adapter.CleanGoodsAdapter;
import com.xxzlkj.licallife.adapter.JzProjectTypeAdapter;
import com.xxzlkj.licallife.interfac.LocalLifeLibraryInterface;
import com.xxzlkj.licallife.model.CleanGoods;
import com.xxzlkj.licallife.model.CleanIndex;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.weight.BannerView;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.ViewPageFragment;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 保洁项目
 * create an instance of this fragment.
 */
public class CleanProjectListFragment extends ViewPageFragment {
    private RecyclerView mTypeRecyclerView;
    private MyRecyclerView mCleanGoodsRecyclerView;
    private BannerView mBannerView;
    private JzProjectTypeAdapter mTypeAdapter;
    private CleanGoodsAdapter mCleanGoodsAdapter;
    private int mPage = 1;
    //分组id
    private String mGroupId;

    public static CleanProjectListFragment newInstance() {
        CleanProjectListFragment fragment = new CleanProjectListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_jz_project, container, false);
    }

    @Override
    protected void findViewById() {
        //商品列表
        mCleanGoodsRecyclerView = getView(R.id.id_jz_project_list);
        mCleanGoodsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mCleanGoodsAdapter = new CleanGoodsAdapter(getContext(), R.layout.adapter_jz_project_goods_item);
        mCleanGoodsRecyclerView.setAdapter(mCleanGoodsAdapter);
        mCleanGoodsRecyclerView.setPullRefreshAndLoadingMoreEnabled(true, true);
        //保洁类型列表
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.view_jp_header, null);
        mTypeRecyclerView = (RecyclerView) headerView.findViewById(R.id.id_jz_project_type_list);
        mTypeRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
        mTypeAdapter = new JzProjectTypeAdapter(getContext(), R.layout.adapter_jz_project_type_item);
        mTypeRecyclerView.setAdapter(mTypeAdapter);
        mCleanGoodsRecyclerView.addHeaderView(headerView);
        //轮播
        mBannerView = (BannerView) headerView.findViewById(R.id.id_jz_project_banner);
    }

    @Override
    public void setListener() {
        mTypeAdapter.setOnItemClickListener((position, item) -> {
            JzProjectTypeAdapter.indexChecked = position;
            mTypeAdapter.notifyDataSetChanged();
            if (!mGroupId.equals(item.getId())) {// 点击的不是当前类型
                mPage = 1;
                mGroupId = item.getId();
                getCleanGoods(mGroupId);
            }
        });
        mCleanGoodsAdapter.setOnItemClickListener((position, item) -> getContext().startActivity(ProjectDetailActivity.newIntent(getContext(), item.getId())));
        mCleanGoodsRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                JzProjectTypeAdapter.indexChecked = 0;
                getProjectTop();
            }

            @Override
            public void onLoadMore() {
                if (!TextUtils.isEmpty(mGroupId)) {
                    mPage = mCleanGoodsAdapter.getItemCount() / mCleanGoodsRecyclerView.loadSize + 1;
                    getCleanGoods(mGroupId);
                }
            }
        });
    }

    @Override
    public void processLogic() {

    }

    @Override
    public void refreshOnceData() {
        getProjectTop();
    }

    /**
     * 获取上部分数据
     */
    private void getProjectTop() {
        HashMap<String, String> map = new HashMap<>();
        // 小区id
        map.put(URLConstants.REQUEST_PARAM_COMMUNITY_ID, GlobalParams.communityId);
        RequestManager.createRequest(URLConstants.JZ_CLEANING_INDEX,
                map, new OnMyActivityRequestListener<CleanIndex>((BaseActivity) getActivity()) {
                    @Override
                    public void onSuccess(CleanIndex bean) {
                        final List<CleanIndex.DataBean.BannerBean> banner = bean.getData().getBanner();
                        if (banner != null && banner.size() > 0) {
                            mBannerView.setVisibility(View.VISIBLE);
                            List<String> mBannerUrls = new ArrayList<>();
                            for (int i = 0; i < banner.size(); i++) {
                                mBannerUrls.add(banner.get(i).getSimg());
                            }
                            mBannerView.setImgUrlData(mBannerUrls);
                            mBannerView.setOnHeaderViewClickListener(position -> {
                                CleanIndex.DataBean.BannerBean bannerBean = banner.get(position);
                                int type = NumberFormatUtils.toInt(bannerBean.getType());
                                switch (type) {
                                    case 0:// 不跳转
                                        break;
                                    case 1:// 跳转到h5
                                        if (!TextUtils.isEmpty(bannerBean.getTo()))
                                            if (BaseApplication.getInstance() instanceof LocalLifeLibraryInterface) {
                                                ((LocalLifeLibraryInterface) BaseApplication.getInstance()).jumpToHasTitleWebView(mActivity, bannerBean.getTo(), "详情");
                                            }
                                        break;
                                }
                            });

                        } else {
                            mBannerView.setVisibility(View.GONE);
                        }
                        List<CleanIndex.DataBean.GroupBean> group = bean.getData().getGroup();
                        if (group != null) {
                            // 刷新时清除
                            if (mTypeAdapter != null && mTypeAdapter.getItemCount() > 0) {
                                mTypeAdapter.clear();
                            }
                            mTypeAdapter.addList(group);
                            if (group.size() >= 1) {// 加载保洁产品数据
                                mGroupId = group.get(0).getId(); // 第一次进来加载第一个
                                getCleanGoods(mGroupId);
                            }
                        }

                    }

                });
    }

    /**
     * 获取保洁产品集合
     */
    private void getCleanGoods(String groupid) {
        HashMap<String, String> map = new HashMap<>();
        // 小区id
        map.put(URLConstants.REQUEST_PARAM_COMMUNITY_ID, GlobalParams.communityId);
        map.put(URLConstants.REQUEST_PARAM_GROUPID, groupid);
        map.put(URLConstants.REQUEST_PARAM_PAGE, String.valueOf(mPage));
        RequestManager.createRequest(URLConstants.JZ_CLEANING_GOODS,
                map, new OnMyActivityRequestListener<CleanGoods>((BaseActivity) getActivity()) {
                    @Override
                    public void onSuccess(CleanGoods bean) {
                        MyRecyclerView.handlerSuccessHasRefreshAndLoad(mCleanGoodsRecyclerView.getxRecyclerView(), mCleanGoodsAdapter, mCleanGoodsRecyclerView.loadSize, mPage == 1, bean.getData());
                    }

                    @Override
                    public void onFailed(boolean isError, String code, String message) {
                        super.onFailed(isError, code, message);
                        mCleanGoodsRecyclerView.handlerComplete(mCleanGoodsRecyclerView.getxRecyclerView());

                        if (mPage == 1 && mCleanGoodsAdapter.getItemCount() > 0) {
                            mCleanGoodsAdapter.clear();
                        }

                    }

                });
    }


}
