package com.xxzlkj.huayiyanglao.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.activity.foodorder.TakeOutActivity;
import com.xxzlkj.huayiyanglao.adapter.TakeOutContentAdapter;
import com.xxzlkj.huayiyanglao.adapter.TakeOutTitleAdapter;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.event.FoodsSaleListEvent;
import com.xxzlkj.huayiyanglao.model.FoodsBean;
import com.xxzlkj.huayiyanglao.model.FoodsSaleListBean;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.ViewPageFragment;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 描述：今日/明日菜单
 *
 * @author zhangrq
 *         2017/9/2 10:58
 */
public class TodayByTomorrowMenuFragment extends ViewPageFragment {
    private RecyclerView mTitleRecyclerView, mContentRecyclerView;
    private TakeOutTitleAdapter mTakeOutTitleAdapter;
    private TakeOutContentAdapter mTakeOutContentAdapter;
    private LinearLayoutManager linearLayoutManager;
    private View mShopCartView;
    private String mShopId;
    private String mDate;
    private TakeOutActivity mTakeOutActivity;
    // 是否是点击左边item列表
    private boolean isClickLeftItem;

    /**
     * @param mShopCartView 购物车（必传）
     * @param shopId        店铺id（必传）
     * @param date          1为今日菜单 0为明日菜单(必传)
     * @return
     */
    public static TodayByTomorrowMenuFragment newInstance(TakeOutActivity mTakeOutActivity, View mShopCartView, String shopId, String date) {
        TodayByTomorrowMenuFragment fragment = new TodayByTomorrowMenuFragment();
        fragment.mShopCartView = mShopCartView;
        fragment.mTakeOutActivity = mTakeOutActivity;
        Bundle bundle = new Bundle();
        bundle.putString(ZLConstants.Strings.SHOPID, shopId);
        bundle.putString(ZLConstants.Strings.DATE, date);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_take_out_menu, container, false);
    }

    @Override
    protected void findViewById() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        mTitleRecyclerView = getView(R.id.id_title_recycler_view);// 左边标题栏列表
        mTitleRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mTakeOutTitleAdapter = new TakeOutTitleAdapter(mContext, R.layout.adapter_takeout_left_list_item);
        mTitleRecyclerView.setAdapter(mTakeOutTitleAdapter);

        mContentRecyclerView = getView(R.id.id_content_recycler_view);// 右边栏列表
        linearLayoutManager = new LinearLayoutManager(mContext);
        mContentRecyclerView.setLayoutManager(linearLayoutManager);
        mTakeOutContentAdapter = new TakeOutContentAdapter(mContext, mTakeOutActivity, mShopCartView, R.layout.adapter_takeout_right_list);
        mContentRecyclerView.setAdapter(mTakeOutContentAdapter);

    }

    @Override
    public void setListener() {
        mTakeOutTitleAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<FoodsSaleListBean.DataBean>() {
            @Override
            public void onClick(int position, FoodsSaleListBean.DataBean item) {
                isClickLeftItem = true;
                // 改变选中item
                if (mTakeOutTitleAdapter.selectPosition != position) {
                    mTakeOutTitleAdapter.selectPosition = position;
                    // 右侧滑到对应的item
                    mContentRecyclerView.scrollToPosition(position);
                    mTakeOutTitleAdapter.notifyDataSetChanged();
                }
            }
        });

        // 右边内容滚动监听事件
        mContentRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 找到第一个可见item
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                // 改变左侧选中item
                if (mTakeOutTitleAdapter.selectPosition != firstVisibleItemPosition && !isClickLeftItem) {
                    mTakeOutTitleAdapter.selectPosition = firstVisibleItemPosition;
                    mTitleRecyclerView.scrollToPosition(firstVisibleItemPosition);
                    mTakeOutTitleAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                isClickLeftItem = false;
            }
        });
    }

    @Override
    public void processLogic() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mShopId = bundle.getString(ZLConstants.Strings.SHOPID);
            mDate = bundle.getString(ZLConstants.Strings.DATE);
        }
    }

    @Override
    public void refreshOnceData() {
        getFoodsSaleList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 美食菜单列表
     */
    private void getFoodsSaleList() {
        Map<String, String> map = new HashMap<>();
        // 店铺id（必传）
        map.put(ZLConstants.Params.SHOPID, mShopId);
        User loginUser = ZhaoLinApplication.getInstance().getLoginUser();
        if (loginUser == null) {
            mActivity.finish();
            return;
        }

        // 会员id(会员id)
        map.put(ZLConstants.Params.UID, loginUser.getData().getId());
        // 1为今日菜单 0为明日菜单
        map.put(ZLConstants.Params.DATE, mDate);

        RequestManager.createRequest(ZLURLConstants.FOODS_SALE_LIST_URL, map,
                new OnMyActivityRequestListener<FoodsSaleListBean>((BaseActivity) mActivity) {
                    @Override
                    public void onSuccess(FoodsSaleListBean bean) {
                        List<FoodsSaleListBean.DataBean> data = bean.getData();
                        mTakeOutTitleAdapter.addList(data);
                        mTakeOutContentAdapter.addList(data);

                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updataFoodsCartList(FoodsSaleListEvent event) {
        // 更改菜单列表中单个商品加入购物车的数量
        if (mTakeOutContentAdapter != null && mTakeOutContentAdapter.getItemCount() > 0){
            // 商品id多个用英文逗号隔开
            String foodsIds = event.getFoodsIds();
            String[] splitIds = foodsIds.split(",");
            // 商品数量多个用英文逗号隔开,并且和id一一对应
            String foodsNumbers = event.getFoodsNumbers();
            String[] splitNumbers = foodsNumbers.split(",");
            for (int f = 0; f < splitIds.length; f++) {
                // 商品id
                String foodsId = splitIds[f];
                // 商品购物车数量
                String number = splitNumbers[f];
                List<FoodsSaleListBean.DataBean> list = mTakeOutContentAdapter.getList();
                for (int i = 0; i < list.size(); i++) {
                    FoodsSaleListBean.DataBean dataBean = list.get(i);
                    List<FoodsSaleListBean.DataBean.ListBeanX> list1 = dataBean.getList();
                    for (int j = 0; j < list1.size(); j++) {
                        FoodsSaleListBean.DataBean.ListBeanX listBeanX = list1.get(j);
                        List<FoodsBean> list2 = listBeanX.getList();
                        // 遍历找到对应的商品
                        for (int k = 0; k < list2.size(); k++) {
                            // 列表中商品
                            FoodsBean foodsBean = list2.get(k);
                            // 列表中商品id
                            String id = foodsBean.getId();
                            if (foodsId.equals(id)) {
                                // 改变加入购物车商品数量
                                foodsBean.setCount(number);
                                list2.set(k,foodsBean);
                                listBeanX.setList(list2);
                                list1.set(j,listBeanX);
                                dataBean.setList(list1);
                                list.set(i,dataBean);
                            }
                        }
                    }
                }
            }

            // 刷新列表
            mTakeOutContentAdapter.notifyDataSetChanged();

        }

    }

}
