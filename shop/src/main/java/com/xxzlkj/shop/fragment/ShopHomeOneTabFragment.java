package com.xxzlkj.shop.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sunfusheng.marqueeview.DisplayUtil;
import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.ShopHomeActivity;
import com.xxzlkj.shop.activity.shop.GoodsDetailActivity;
import com.xxzlkj.shop.adapter.HotRecommentAdapter;
import com.xxzlkj.shop.adapter.ShopGridAdapter;
import com.xxzlkj.shop.adapter.ShopHomeAdapter;
import com.xxzlkj.shop.adapter.ShopHomeOneTopAdapter;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.interfac.ShopLibraryInterface;
import com.xxzlkj.shop.model.GoodsList;
import com.xxzlkj.shop.model.ShopIndexBean;
import com.xxzlkj.shop.weight.BannerView;
import com.xxzlkj.shop.weight.HeaderRecyclerView;
import com.xxzlkj.shop.weight.SpacesItemDecoration;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.ReuseViewFragment;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;


/**
 * 描述:商城首页栏目
 *
 * @author zhangrq
 *         2017/6/26 10:17
 */
public class ShopHomeOneTabFragment extends ReuseViewFragment {

    private MyRecyclerView mRecyclerView;
    private HeaderRecyclerView mHeaderRecyclerView;
    private RecyclerView mRecyclerViewTop;

    private ShopHomeAdapter adapter;
    private ShopHomeOneTopAdapter adapterTop;
    private int pageNo = 1;
    private BannerView mBannerView;
    private List<ShopIndexBean.DataBean.BannerBean> banner;
    private ShopGridAdapter gridAdapter;
    private List<ShopIndexBean.DataBean.AppBean> app;
    private View headerView;
    private int scrollYAll;
    private ShopHomeTabFragment parentFragment;
    private HotRecommentAdapter mHotRecommentAdapter;
    private boolean isRefreshScroll;
    private String type;
    private RecyclerView mGirdRecyclerView;

    /**
     * 商城首页列表、时速达列表
     *
     * @param parentArgs 父类里面的参数，详细请看父类 ShopHomeTabFragment 接收的值
     */
    public static ShopHomeOneTabFragment newInstance(ShopHomeTabFragment parentFragment, Bundle parentArgs) {
        ShopHomeOneTabFragment fragment = new ShopHomeOneTabFragment();
        fragment.parentFragment = parentFragment;
        fragment.setArguments(parentArgs);
        return fragment;
    }

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_tab_shop_home_one, container, false);
    }

    @Override
    protected void findViewById() {
        // 定位成功布局
        mRecyclerView = getView(R.id.recyclerView);// 内容
        mRecyclerViewTop = getView(R.id.recyclerView_top);
        // 给内容增加头、初始化
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setPullRefreshAndLoadingMoreEnabled(true, true);
        mHotRecommentAdapter = new HotRecommentAdapter(getContext(), mActivity, R.layout.adapter_shop_home_style_4_item);
        mRecyclerView.setAdapter(mHotRecommentAdapter);
        // 增加头布局
        View mHeaderView = LayoutInflater.from(mContext).inflate(R.layout.adapter_shop_home_style_list, null);
        mHeaderRecyclerView = (HeaderRecyclerView) mHeaderView.findViewById(R.id.id_shop_home_list);
        mHeaderRecyclerView.setNestedScrollingEnabled(false);
        mHeaderRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addHeaderView(mHeaderView);


        // 初始化头、增加头
        initViewAndAddHeaderView();
    }

    @SuppressWarnings("RedundantCast")
    private void initViewAndAddHeaderView() {
        headerView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_shop_home_style_header, null);
        // 轮播图
        mBannerView = (BannerView) headerView.findViewById(R.id.id_header_bannerView);
        // 根据手机屏幕重新设置banner宽高
        int mWidth = DisplayUtil.getWindowWidth(getActivity());
        int mHeight = (int) (1.0f * 420 * mWidth / 750);
        mBannerView.setWidthAndHeight(mWidth, mHeight);
        // 轮播图下面宫格菜单
        mGirdRecyclerView = (RecyclerView) headerView.findViewById(R.id.id_header_list);
        mGirdRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 5));
        mGirdRecyclerView.setNestedScrollingEnabled(false);
        SpacesItemDecoration decoration = new SpacesItemDecoration(22);
        mGirdRecyclerView.addItemDecoration(decoration);
        gridAdapter = new ShopGridAdapter(mContext, R.layout.adapter_grid_list_item);
        mGirdRecyclerView.setAdapter(gridAdapter);
        // 头布局RecyclerView增加头
        mHeaderRecyclerView.addHeaderView(headerView);

        // 内容的RecyclerView
        mHeaderRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new ShopHomeAdapter(mContext, getActivity(), 0);
        mHeaderRecyclerView.setAdapter(adapter);
        // 顶部的RecyclerView
        mRecyclerViewTop.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewTop.setNestedScrollingEnabled(false);
        mRecyclerViewTop.setItemAnimator(new SlideInDownAnimator());
        mRecyclerViewTop.getItemAnimator().setAddDuration(300);
        mRecyclerViewTop.setAlpha(0);// 默认隐藏
        adapterTop = new ShopHomeOneTopAdapter(mContext, R.layout.item_shop_home_top);
        mRecyclerViewTop.setAdapter(adapterTop);
    }

    @Override
    public void setListener() {

        // 轮播图点击事件
        mBannerView.setOnHeaderViewClickListener(position -> {
            if (banner != null && banner.get(position) != null) {
                // 轮播图跳转
                if (BaseApplication.getInstance() instanceof ShopLibraryInterface) {
                    // 让主项目处理
                    ShopIndexBean.DataBean.BannerBean bannerBean = banner.get(position);
                    ((ShopLibraryInterface) BaseApplication.getInstance()).jumpToActivityByType(mActivity, bannerBean.getType(), bannerBean.getTo());
                }
            }
        });
        // 宫格菜单的点击事件
        gridAdapter.setOnItemClickListener((position, item) -> jumpToNext(item));
        // 顶部水平滚动的列表
        adapterTop.setOnItemClickListener((position, item) -> jumpToNext(item));
        // 跳转到商品详情
        mHotRecommentAdapter.setOnItemClickListener((position, item) -> mActivity.startActivity(GoodsDetailActivity.newIntent(mContext, item.getId())));

        // 设置加载监听
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                scrollYAll = 0;
                pageNo = 1;
                getTopDataAfterBottomData();
            }

            @Override
            public void onLoadMore() {
                pageNo = mHotRecommentAdapter.getItemCount() / mRecyclerView.loadSize + 1;
                getRecommendBottom();
            }
        });
        // 设置滚动监听
        mRecyclerView.getxRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 设置滚动隐藏
                ((ShopHomeActivity) mActivity).setOneFragmentButtonAnimation(newState == RecyclerView.SCROLL_STATE_IDLE);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollYAll += dy;
                int height = headerView.getHeight();//mBottom - mTop
                // 设置标题头
                if (scrollYAll >= height) {
                    // 滚出了布局,设置显示，只执行一次
                    if (mRecyclerViewTop.getAlpha() != 1) {
                        mRecyclerViewTop.setAlpha(1);
                        if (app != null) {
                            startTopAnimation(app);
                        }
                    }
                } else {
                    // 设置隐藏，只执行一次
                    if (mRecyclerViewTop.getAlpha() == 1) {
                        mRecyclerViewTop.setAlpha(0);
                        stopTopAnimation();
                    }
                }

                // 滚动高于100，隐藏
                if (!isRefreshScroll) {
                    ((ShopHomeActivity) mActivity).setAddressHintVisibility(false);
                }
            }
        });
    }

    private void startTopAnimation(List<ShopIndexBean.DataBean.AppBean> app) {
        int startPosition = 0;
        List<ShopIndexBean.DataBean.AppBean> list = adapterTop.getList();
        list.addAll(app);
        adapterTop.notifyItemRangeInserted(startPosition, app.size());
    }

    private void stopTopAnimation() {
        int startPosition = 0;
        List<ShopIndexBean.DataBean.AppBean> list = adapterTop.getList();
        int preSize = list.size();
        if (preSize > 0) {
            list.clear();
            adapterTop.notifyItemRangeRemoved(startPosition, preSize);
        }
    }

    @Override
    public void processLogic() {
        // 设置按钮显示
        ((ShopHomeActivity) mActivity).setCurrentFragment(true);
        // 获取参数
        type = getArguments().getString(ShopHomeTabFragment.TYPE);//  5商城首页 2 火爆预售;3 兆邻团购 ;
        // 刷新数据
        setRefresh();
    }

    public void setRefresh() {
        if (mRecyclerView != null) {
            isRefreshScroll = true;
            mRecyclerView.getxRecyclerView().scrollToPosition(0);
            // 刷新数据
            mRecyclerView.getLoadingListener().onRefresh();
            mRecyclerView.postDelayed(() -> isRefreshScroll = false, 500);
        }
//        getTopDataAfterBottomData();
    }

    /**
     * 跳转到下一个页面，即TwoFragment
     */
    private void jumpToNext(ShopIndexBean.DataBean.AppBean item) {
        // 隐藏地址提示
        ((ShopHomeActivity) mActivity).setAddressHintVisibility(false);

        if (TextUtils.equals("0", item.getGroupid())) {
            // Groupid为0跳转到全部分类,根据TitleType跳到对应全部分类
            ShopHomeActivity.jumpToSpeedGoodsCategoryActivityByType(mActivity, type, GlobalParams.storeId);
        } else {
            parentFragment.jumpToTwoFragment(item.getId());
        }
    }

    /**
     * 先获取顶部数据，获取成功之后获取底部数据
     */
    private void getTopDataAfterBottomData() {
        HashMap<String, String> map = new HashMap<>();
        String urlStr = "";
//        5商城首页 2 火爆预售;3 兆邻团购 ;
        if ("5".equals(type)) {
            // 1.商城首页（时速达）
            urlStr = URLConstants.SHOP_INDEX_STORE_URL;
            map.put("store_id", GlobalParams.storeId);
        } else if ("2".equals(type)) {
            // 2 火爆预售
            urlStr = URLConstants.ADVANCE_INDEX_URL;
            map.put("store_id", GlobalParams.storeId);
        } else if ("3".equals(type)) {
            // 3 兆邻团购
            urlStr = URLConstants.GROUPON_INDEX_URL;
            // 店铺id (必传)
            map.put("store_id", GlobalParams.storeId);
        }
        RequestManager.createRequest(urlStr, map, new OnMyActivityRequestListener<ShopIndexBean>((BaseActivity) mActivity) {

            @Override
            public void onSuccess(ShopIndexBean bean) {
                ShopIndexBean.DataBean data = bean.getData();
                // 设置轮播图
                banner = data.getBanner();
                if (banner != null && banner.size() > 0) {
                    // 有数据，显示
                    mBannerView.setVisibility(View.VISIBLE);
                    ArrayList<String> urlList = new ArrayList<>();
                    for (ShopIndexBean.DataBean.BannerBean bannerBean : banner) {
                        urlList.add(bannerBean.getSimg());
                    }
                    mBannerView.setImgUrlData(urlList);
                } else {
                    // 没有数据，隐藏
                    mBannerView.setVisibility(View.GONE);
                }

                // 设置分类
                app = data.getApp();
                if (app != null && app.size() > 0) {
                    // 有数据，显示
                    mRecyclerViewTop.setVisibility(View.VISIBLE);
                    mGirdRecyclerView.setVisibility(View.VISIBLE);
                    gridAdapter.clear();
                    gridAdapter.addList(app);
                } else {
                    // 没有数据，隐藏
                    mRecyclerViewTop.setVisibility(View.GONE);
                    mGirdRecyclerView.setVisibility(View.GONE);
                }

                // 设置水平滑动的栏目列表，在这不添加了，显示的时候添加
//                adapterTop.clear();
//                adapterTop.addList(app);
                // 设置各种样式,不控制是否显示没有数据的view
                adapter.clearAndAddList(data.getAds());
//                MyRecyclerView.handlerSuccessHasRefreshAndLoad(mRecyclerView.getxRecyclerView(), adapter, mRecyclerView.loadSize, true, data.getAds());
                mHeaderRecyclerView.setAdapter(adapter);

                if ("5".equals(type)) {
                    // 1.商城首页（时速达），有加载，所以又重新的获取了一个列表
                    // 获取底部数据
                    getRecommendBottom();
                } else {
                    // 其它，刷新完成
                    MyRecyclerView.handlerComplete(mRecyclerView.getxRecyclerView());
                }

            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                super.onFailed(isError, code, message);
                // 不控制是否显示没有数据的view
                MyRecyclerView.handlerComplete(mRecyclerView.getxRecyclerView());
            }

        });
    }

    /**
     * 下部数据
     */
    private void getRecommendBottom() {
        Map<String, String> map = new HashMap<>();
        String urlStr;
//        5商城首页 2 火爆预售;3 兆邻团购 ;
        if ("5".equals(type)) {
            // 1.商城首页（时速达）
            urlStr = URLConstants.SHOP_INDEX_DOWN_LIST_URL;
            map.put("store_id", GlobalParams.storeId);
            map.put(URLConstants.REQUEST_PARAM_PAGE, String.valueOf(pageNo));
        } else
            // 只有5的时候，才有底部的数据
            return;

        RequestManager.createRequest(urlStr, map, new OnMyActivityRequestListener<GoodsList>((BaseActivity) mActivity) {
            @Override
            public void onSuccess(GoodsList bean) {
                mRecyclerView.handlerSuccessHasRefreshAndLoad(mHotRecommentAdapter, pageNo == 1, bean.getData());
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                super.onFailed(isError, code, message);
                // 不控制是否显示没有数据的view
                MyRecyclerView.handlerComplete(mRecyclerView.getxRecyclerView());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.cancelAllTimers();
            adapter.cancelTimer();
        }
    }

}
