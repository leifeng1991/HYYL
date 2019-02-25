package com.xxzlkj.shop.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.shop.GoodsDetailActivity;
import com.xxzlkj.shop.adapter.MyTabTabGridViewAdapter;
import com.xxzlkj.shop.adapter.ShopHomeStyleAdapter11;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.model.GoodsList;
import com.xxzlkj.shop.model.ShopAppBean;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.ViewPageFragment;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:商城首页栏目
 *
 * @author zhangrq
 *         2017/6/26 10:17
 */
public class ShopHomeTwoTabTabFragment extends ViewPageFragment {

    private MyRecyclerView mRecyclerView;
    private RecyclerView mGridRecyclerView;
    private ShopHomeStyleAdapter11 adapter;
    private int pageNo = 1;
    private ImageView mImageView;
    private ShopAppBean.DataBean appBean;
    private List<ShopAppBean.DataBean.GroupBean> rawGroupList = new ArrayList<>();
    private MyTabTabGridViewAdapter mGridViewAdapter;
    private int oldClickPosition;
    private String type;

    /**
     * @param appBean    二级标题的信息
     * @param parentArgs 父类里面的参数，详细请看父类 ShopHomeTabFragment 接收的值
     */
    public static ShopHomeTwoTabTabFragment newInstance(ShopAppBean.DataBean appBean, Bundle parentArgs) {
        ShopHomeTwoTabTabFragment fragment = new ShopHomeTwoTabTabFragment();
        fragment.appBean = appBean;
        fragment.setArguments(parentArgs);
        return fragment;
    }

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_tab_shop_home_two_tab_tab, container, false);
    }

    @Override
    protected void findViewById() {
        mRecyclerView = getView(R.id.recyclerView);
        // 初始化头、增加头
        View headerView = View.inflate(mContext, R.layout.layout_tab_shop_home_two_tab_tab_header, null);
        mImageView = (ImageView) headerView.findViewById(R.id.iv_top);
        mGridRecyclerView = (RecyclerView) headerView.findViewById(R.id.gridView);
        mRecyclerView.addHeaderView(headerView);

        //  初始化头内容的RecyclerView
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRecyclerView.setPullRefreshAndLoadingMoreEnabled(true, true);
        adapter = new ShopHomeStyleAdapter11(mContext, mActivity, false, R.layout.adapter_shop_home_style_42_item);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void setListener() {
        // 三级菜单Item点击监听
        mGridViewAdapter = new MyTabTabGridViewAdapter(mContext, R.layout.adapter_shop_home_tab_tab_gridview);
        mGridRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        mGridRecyclerView.setNestedScrollingEnabled(false);
        mGridRecyclerView.setAdapter(mGridViewAdapter);
        mGridViewAdapter.setOnItemClickListener((position, groupBean) -> {
            if (groupBean.isMore()) {
                // 更多按钮
                // 设置选中位置
                if (oldClickPosition != 7) {
                    mGridViewAdapter.setSelectedPosition(oldClickPosition);
                }

                mGridViewAdapter.clearAndAddList(getShowButtonList(rawGroupList, true));

            } else if (groupBean.isClose()) {
                // 关闭按钮

                // 设置选中位置
                if (oldClickPosition >= 7) {
                    mGridViewAdapter.setSelectedPosition(6);
                } else {
                    mGridViewAdapter.setSelectedPosition(oldClickPosition);
                }

                List<ShopAppBean.DataBean.GroupBean> showButtonList = getShowButtonList(rawGroupList, false);
                mGridViewAdapter.clearAndAddList(showButtonList);
            } else {
                // 普通按钮
                pageNo = 1;
                oldClickPosition = position;
                // 设置选中位置
                mGridViewAdapter.setSelectedPosition(oldClickPosition);
                mGridViewAdapter.notifyDataSetChanged();

                getNetData(groupBean.getGroupid());
            }
        });
        // 设置加载监听
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                setData();
            }

            @Override
            public void onLoadMore() {
                pageNo = adapter.getItemCount() / mRecyclerView.loadSize + 1;
                setData();
            }
        });
        // Item的点击跳转商品详情
        adapter.setOnItemClickListener((position, item) -> mActivity.startActivity(GoodsDetailActivity.newIntent(mContext, item.getId())));
    }

    @Override
    public void processLogic() {
        // 获取参数
        type = getArguments().getString(ShopHomeTabFragment.TYPE);//  1商城首页 2 火爆预售;3 兆邻团购 ;
        // 初始化标题栏
        if (appBean == null) {
            return;
        }
        // 设置头图片
        PicassoUtils.setImageBig(mContext, appBean.getBgimg(), mImageView);
        // 设置三级菜单
        rawGroupList = appBean.getGroup();
        mGridViewAdapter.setSelectedPosition(0); // 设置默认选中菜单0
        mGridViewAdapter.clearAndAddList(getShowButtonList(rawGroupList, false));// 设置adapter，集合已经在getShowButtonList方法中处理了
    }

    @Override
    public void refreshOnceData() {
        // 获取网络数据
        setData();
    }

    private void setData() {
        // 设置内容数据
        if (rawGroupList != null && rawGroupList.size() > mGridViewAdapter.getSelectedPosition()) {
            getNetData(rawGroupList.get(mGridViewAdapter.getSelectedPosition()).getGroupid());
        }

    }

    /**
     * 获取商品列表
     */
    private void getNetData(String threeTabId) {
        Map<String, String> map = new HashMap<>();
        String urlStr = "";
//        5商城首页 2 火爆预售;3 兆邻团购 ;
        if ("5".equals(type)) {
            // 1.商城首页（时速达）
            urlStr = URLConstants.SHOP_GOODS_LIST_URL;
            map.put("store_id", GlobalParams.storeId);
            map.put(URLConstants.REQUEST_PARAM_GROUPID, threeTabId);
            map.put(URLConstants.REQUEST_PARAM_PAGE, String.valueOf(pageNo));
        } else if ("2".equals(type)) {
            // 2 火爆预售
            urlStr = URLConstants.ADVANCE_GOODS_LIST_URL;
//            store_id	店铺id(必传)没有传0
//            keyword	搜索关键词
//            groupid	分类id
//            order	按什么排序 1销量 2价格 3活动 0和不传默认
//            ord	desc 高到低  asc低到高
//            page	分页 默认为1 一页20条
            map.put(URLConstants.REQUEST_PARAM_STORE_ID, TextUtils.isEmpty(GlobalParams.storeId) ? "0" : GlobalParams.storeId);
            map.put(URLConstants.REQUEST_PARAM_GROUPID, threeTabId);
            map.put(URLConstants.REQUEST_PARAM_PAGE, String.valueOf(pageNo));
        } else if ("3".equals(type)) {
            // 3 兆邻团购
            urlStr = URLConstants.GROUPON_GOODS_LIST_URL;
//            store_id	店铺id(必传)没有传0
//            keyword	搜索关键词
//            groupid	分类id
//            order	按什么排序 1销量 2价格 3活动 0和不传默认
//            ord	desc 高到低  asc低到高
//            page	分页 默认为1 一页20条
            map.put(URLConstants.REQUEST_PARAM_STORE_ID, TextUtils.isEmpty(GlobalParams.storeId) ? "0" : GlobalParams.storeId);
            map.put(URLConstants.REQUEST_PARAM_GROUPID, threeTabId);
            map.put(URLConstants.REQUEST_PARAM_PAGE, String.valueOf(pageNo));
        }

        RequestManager.createRequest(urlStr, map, new OnMyActivityRequestListener<GoodsList>((BaseActivity) mActivity) {
            @Override
            public void onSuccess(GoodsList bean) {
                mRecyclerView.handlerSuccessHasRefreshAndLoad(adapter, pageNo == 1, bean.getData());
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                // 不显示提示信息
//                super.onFailed(isError, code, message);
                if (pageNo == 1) {
                    // 刷新，删除全部
                    adapter.clear();
                }
                // 不显示没有数据的View，设置刷新完成
                MyRecyclerView.handlerComplete(mRecyclerView.getxRecyclerView());
            }
        });
    }

    /**
     * @param rawGroupList 未加工的列表
     * @param isOpenView   是否打开View，即全部展开View
     * @return 已经处理好的集合
     */
    private List<ShopAppBean.DataBean.GroupBean> getShowButtonList(List<ShopAppBean.DataBean.GroupBean> rawGroupList, boolean isOpenView) {
        if (rawGroupList == null) {
            return new ArrayList<>();
        }
        if (rawGroupList.size() > 8) {
            // 超过8个
            ArrayList<ShopAppBean.DataBean.GroupBean> groupBeen = new ArrayList<>();
            if (isOpenView) {
                // 要全部展开,显示全部+关闭
                groupBeen.addAll(rawGroupList);
                groupBeen.add(new ShopAppBean.DataBean.GroupBean(false, true));// 添加关闭按钮
            } else {
                // 不全部展开，显示7个+更多
                // 如果之前选中的位置大于等于7，获取当前位置的后七个，否则正常显示前七个
                if (oldClickPosition >= 7) {
                    // 获取当前位置的后七个
                    for (int i = oldClickPosition - 6; i <= oldClickPosition; i++) {
                        groupBeen.add(rawGroupList.get(i));
                    }
                } else {
                    // 否则正常显示前七个
                    for (int i = 0; i < 7; i++) {
                        groupBeen.add(rawGroupList.get(i));
                    }
                }
                groupBeen.add(new ShopAppBean.DataBean.GroupBean(true, false));// 添加更多按钮
            }
            return groupBeen;
        } else
            // 正常显示
            return rawGroupList;
    }


}
