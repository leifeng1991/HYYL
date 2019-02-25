package com.xxzlkj.huayiyanglao.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.activity.found.FoundDetailActivity;
import com.xxzlkj.huayiyanglao.activity.mine.CollectionActivity;
import com.xxzlkj.huayiyanglao.adapter.ActivityCollectAdapter;
import com.xxzlkj.huayiyanglao.adapter.GoodsCollectAdapter;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.event.CollectEvent;
import com.xxzlkj.huayiyanglao.model.ActivityCollect;
import com.xxzlkj.huayiyanglao.model.Collect;
import com.xxzlkj.licallife.activity.localLife.NannyAndMaternityMatronDesActivity;
import com.xxzlkj.licallife.adapter.NannyAndMaternityMatronListAdapter;
import com.xxzlkj.licallife.model.NannyAndMaternityMatronListBean;
import com.xxzlkj.shop.activity.shop.GoodsDetailActivity;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.ViewPageFragment;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 收藏
 * create an instance of this fragment.
 */
public class CollectFragment extends ViewPageFragment {
    // 收藏商品
    public static final int COLLECT_GOODS = 0;
    // 收藏商铺
    public static final int COLLECT_STORE = 1;
    // 收藏活动
    public static final int COLLECT_ACTIVITY = 2;
    private MyRecyclerView myRecyclerView;
    private GoodsCollectAdapter mGoodsCollectAdapter;
    private ActivityCollectAdapter mActivityCollectAdapter;
    private int page = 1;
    private int type;
    private CollectionActivity activity;
    private NannyAndMaternityMatronListAdapter mNannyAndMaternityMatronListAdapter;

    /**
     * @param type 0:收藏商品列表 1：收藏服务列表 2：收藏活动列表 （必传）
     * @return
     */
    public static CollectFragment newInstance(int type) {
        CollectFragment fragment = new CollectFragment();
        Bundle args = new Bundle();
        args.putInt(StringConstants.INTENT_PARAM_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (CollectionActivity) activity;
    }

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_collect, container, false);
    }

    @Override
    protected void findViewById() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt(StringConstants.INTENT_PARAM_TYPE);
        }

        myRecyclerView = getView(R.id.id_collect_list);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        switch (type) {
            case COLLECT_GOODS:// 商品
                myRecyclerView.setPullRefreshAndLoadingMoreEnabled(true, false);
                mGoodsCollectAdapter = new GoodsCollectAdapter(getContext(), getActivity(), R.layout.adapter_collect_goods_list_item);
                myRecyclerView.setAdapter(mGoodsCollectAdapter);
                break;
            case COLLECT_STORE:// 服务
                myRecyclerView.setPullRefreshAndLoadingMoreEnabled(true, true);
                mNannyAndMaternityMatronListAdapter = new NannyAndMaternityMatronListAdapter(mContext, R.layout.item_nanny_and_maternity_matron_list);
                myRecyclerView.setAdapter(mNannyAndMaternityMatronListAdapter);
                break;
            case COLLECT_ACTIVITY:// 活动
                myRecyclerView.setPullRefreshAndLoadingMoreEnabled(true, true);
                mActivityCollectAdapter = new ActivityCollectAdapter(getContext(), R.layout.adapter_collect_activity_list_item);
                myRecyclerView.setAdapter(mActivityCollectAdapter);
                break;
        }

    }

    @Override
    public void setListener() {
        myRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                setRefresh();
            }

            @Override
            public void onLoadMore() {
                setLoadMore();
            }
        });

        switch (type) {
            case COLLECT_GOODS:// 商品->跳到商品详情
                mGoodsCollectAdapter.setOnItemClickListener((position, item) ->
                        startActivityForResult(GoodsDetailActivity.newIntent(mContext, item.getGoods_id()), 123));
                mGoodsCollectAdapter.setOnIsAllChecked(isAllChecked -> activity.setCheckboxSate(isAllChecked));
                break;
            case COLLECT_STORE:// 服务->跳到保姆月嫂详情
                mNannyAndMaternityMatronListAdapter.setOnItemClickListener((position, item) ->
                        startActivityForResult(NannyAndMaternityMatronDesActivity.newIntent(mContext, "baomu".equals(item.getType()), item.getPeopleid()), 123));
                break;
            case COLLECT_ACTIVITY:// 活动->跳到发现详情
                mActivityCollectAdapter.setOnItemClickListener((position, item) ->
                        startActivityForResult(FoundDetailActivity.newIntent(mContext, item.getAct_id()), 123));
                break;
        }

    }

    @Override
    public void processLogic() {

    }

    @Override
    public void refreshOnceData() {
        setRefresh();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 进入详情页面返回的，重新刷新当前页面
        setRefresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 设置刷新
     */
    private void setRefresh() {
        switch (type) {
            case COLLECT_GOODS:// 商品
                getGoodsCollectionList();
                break;
            case COLLECT_STORE:// 服务
                getNannyAndMaternityMatronList();
                break;
            case COLLECT_ACTIVITY:// 活动
                getActivityCollectionList();
                break;
        }
    }

    private void getNannyAndMaternityMatronList() {
        HashMap<String, String> map = new HashMap<>();
        User user = ZhaoLinApplication.getInstance().getLoginUser();
        if (user == null) {
            return;
        }
        map.put(URLConstants.REQUEST_PARAM_UID, user.getData().getId());
        RequestManager.createRequest(URLConstants.REQUEST_CELL_LIST,
                map, new OnMyActivityRequestListener<NannyAndMaternityMatronListBean>((BaseActivity) getActivity()) {
                    @Override
                    public void onSuccess(NannyAndMaternityMatronListBean bean) {
                        myRecyclerView.handlerSuccessHasRefreshAndLoad(mNannyAndMaternityMatronListAdapter, page == 1, bean.getData());
                    }

                    @Override
                    public void onFailed(boolean isError, String code, String message) {
                        super.onFailed(isError, code, message);
                        myRecyclerView.handlerError(mNannyAndMaternityMatronListAdapter, page == 1);
                    }
                });
    }

    /**
     * 设置加载更多
     */
    private void setLoadMore() {
        switch (type) {
            case COLLECT_GOODS:// 商品
                page = mGoodsCollectAdapter.getItemCount() / myRecyclerView.loadSize + 1;
                getGoodsCollectionList();
                break;
            case COLLECT_STORE:// 服务
                page = mGoodsCollectAdapter.getItemCount() / myRecyclerView.loadSize + 1;
                getNannyAndMaternityMatronList();
                break;
            case COLLECT_ACTIVITY:// 活动
                page = mActivityCollectAdapter.getItemCount() / myRecyclerView.loadSize + 1;
                getActivityCollectionList();
                break;
        }
    }

    /**
     * 获取商品收藏列表
     */
    private void getGoodsCollectionList() {
        HashMap<String, String> map = new HashMap<>();
        User user = ZhaoLinApplication.getInstance().getLoginUser();
        if (user == null) {
            return;
        }
        map.put(URLConstants.REQUEST_PARAM_UID, user.getData().getId());
        RequestManager.createRequest(URLConstants.REQUEST_CELL_GOODS,
                map, new OnMyActivityRequestListener<Collect>((BaseActivity) getActivity()) {
                    @Override
                    public void onSuccess(Collect bean) {
                        myRecyclerView.handlerSuccessHasRefreshAndLoad(mGoodsCollectAdapter, page == 1, bean.getData());
                    }

                    @Override
                    public void onFailed(boolean isError, String code, String message) {
                        super.onFailed(isError, code, message);
                        myRecyclerView.handlerError(mGoodsCollectAdapter, page == 1);
                    }
                });
    }

    /**
     * 获取活动收藏列表
     */
    private void getActivityCollectionList() {
        HashMap<String, String> map = new HashMap<>();
        User user = ZhaoLinApplication.getInstance().getLoginUser();
        if (user == null) {
            return;
        }
        map.put(URLConstants.REQUEST_PARAM_UID, user.getData().getId());
        map.put(URLConstants.REQUEST_PARAM_PAGE, page + "");
        RequestManager.createRequest(URLConstants.MY_CELL_ACTIVITY,
                map, new OnMyActivityRequestListener<ActivityCollect>((BaseActivity) getActivity()) {
                    @Override
                    public void onSuccess(ActivityCollect bean) {
                        myRecyclerView.handlerSuccessHasRefreshAndLoad(mActivityCollectAdapter, page == 1, bean.getData());
                    }

                    @Override
                    public void onFailed(boolean isError, String code, String message) {
                        super.onFailed(isError, code, message);
                        myRecyclerView.handlerError(mActivityCollectAdapter, page == 1);
                    }
                });
    }

    /**
     * 批量删除商品收藏
     */
    private void deleteRequest(String ids) {
        HashMap<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_IDS, ids.substring(0, ids.length() - 1).trim());
        RequestManager.createRequest(URLConstants.REQUEST_CELL_DEL,
                map, new OnMyActivityRequestListener<BaseBean>((BaseActivity) getActivity()) {
                    @Override
                    public void onSuccess(BaseBean bean) {
                    }

                });
    }

    /**
     * 设置选择框的显示和隐藏
     *
     * @param isVisibelChekbox
     */
    private void setIsVisibelChekbox(boolean isVisibelChekbox) {
        if (mGoodsCollectAdapter != null && mGoodsCollectAdapter.getItemCount() > 0) {
            GoodsCollectAdapter.isVisibelCheckbox = isVisibelChekbox;
            mGoodsCollectAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 取消收藏（选中的)
     */
    private void delete() {
        if (mGoodsCollectAdapter != null && mGoodsCollectAdapter.getItemCount() > 0) {
            String ids = "";
            //当前集合
            List<Collect.DataBean> list = mGoodsCollectAdapter.getList();
            //删除算法最复杂,拿到checkBox选择寄存map
            Map<Integer, Boolean> map = mGoodsCollectAdapter.getCheckMap();
            // 获取当前的数据数量
            int count = mGoodsCollectAdapter.getItemCount();
            // 进行遍历
            for (int i = 0; i < count; i++) {
                // 因为List的特性,删除了2个item,则3变成2,所以这里要进行这样的换算,才能拿到删除后真正的position
                int position = i - (count - mGoodsCollectAdapter.getItemCount());
                if (map.get(i) != null && map.get(i)) {
                    if (map.get(i)) {
                        mGoodsCollectAdapter.getCheckMap().remove(i);
                        LogUtil.e("id", list.get(position).getId());
                        ids = ids + list.get(position).getId() + ",";
                        mGoodsCollectAdapter.delete(position);
                    } else {
                        map.put(position, false);
                    }
                }
            }
            if (!TextUtils.isEmpty(ids)) {
                deleteRequest(ids);
                mGoodsCollectAdapter.notifyDataSetChanged();
                if (mGoodsCollectAdapter != null && mGoodsCollectAdapter.getItemCount() > 0) {
                    activity.setBottomLayoutVisible(true);
                } else {
                    activity.setBottomLayoutVisible(false);
                }
            } else {
                ToastManager.showShortToast(mContext, "请选择要取消收藏的商品");
            }
        }
    }

    /**
     * 清除下架商品
     */
    private void clear() {
        if (mGoodsCollectAdapter != null && mGoodsCollectAdapter.getItemCount() > 0) {
            String ids = "";
            //删除算法最复杂,拿到checkBox选择寄存map
            List<Collect.DataBean> list = mGoodsCollectAdapter.getList();
            // 进行遍历
            for (int i = 0; i < list.size(); i++) {
                // 因为List的特性,删除了2个item,则3变成2,所以这里要进行这样的换算,才能拿到删除后真正的position
                int position = i - (list.size() - mGoodsCollectAdapter.getItemCount());
                if (list.get(i) != null && !"2".equals(list.get(i).getState())) {
                    if (!"2".equals(list.get(i).getState())) {
                        LogUtil.e("id", list.get(position).getId());
                        ids = list.get(position).getId() + ",";
                        mGoodsCollectAdapter.delete(position);
                    }
                }
            }
            if (!TextUtils.isEmpty(ids)) {
                deleteRequest(ids);
            } else {
                ToastManager.showShortToast(mContext, "暂无失效商品");
            }
            mGoodsCollectAdapter.notifyDataSetChanged();
            if (mGoodsCollectAdapter != null && mGoodsCollectAdapter.getItemCount() > 0) {
                activity.setBottomLayoutVisible(true);
            } else {
                activity.setBottomLayoutVisible(false);
            }
        }
    }

    /**
     * 全选/取消全选
     *
     * @param isAllCheck
     */
    private void setIsAllCheck(boolean isAllCheck) {
        if (isAllCheck) {
            mGoodsCollectAdapter.selectAll();
        } else {
            mGoodsCollectAdapter.cancelSelected();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    public void updataCollect(CollectEvent event) {
        switch (type) {
            case COLLECT_GOODS:
                if (!TextUtils.isEmpty(event.getIsAllFlag())) {
                    setIsAllCheck(event.isAllCheckbox());
                }
                if (!TextUtils.isEmpty(event.getIsVisibleFlag())) {
                    setIsVisibelChekbox(event.isCheckboxVisible());
                }
                if (event.isDelete()) {
                    delete();
                }
                if (event.isClear()) {
                    clear();
                }
                break;
        }
    }


}
