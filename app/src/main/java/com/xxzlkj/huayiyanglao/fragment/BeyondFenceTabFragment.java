package com.xxzlkj.huayiyanglao.fragment;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps.model.LatLng;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.activity.equipment.LocationHistoryActivity;
import com.xxzlkj.huayiyanglao.adapter.BeyondFenceAdapter;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.BeyondFenceBean;
import com.xxzlkj.huayiyanglao.model.LocationRecordListBean;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.ReuseViewFragment;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 描述：超出围栏
 *
 * @author zhangrq
 *         2017/9/2 10:58
 */
public class BeyondFenceTabFragment extends ReuseViewFragment {
    private MyRecyclerView mRecyclerView;
    private BeyondFenceAdapter beyondFenceAdapter;
    private int page = 1;
    private LocationHistoryActivity activity;
    public List<LatLng> latLngs = new ArrayList<>();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (LocationHistoryActivity) activity;
    }

    public static BeyondFenceTabFragment newInstance() {
        BeyondFenceTabFragment locationHistoryTabFragment = new BeyondFenceTabFragment();
        return locationHistoryTabFragment;
    }

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_beyond_fence_tab, container, false);
    }

    @Override
    protected void findViewById() {
        mRecyclerView = getView(R.id.recyclerView);// 内容列表
        // 初始化
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setPullRefreshAndLoadingMoreEnabled(true, true);
        beyondFenceAdapter = new BeyondFenceAdapter(mContext, R.layout.adapter_beyond_fence_item);
        mRecyclerView.setAdapter(beyondFenceAdapter);
    }

    @Override
    public void setListener() {
        // 刷新和加载
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getNetData();
            }

            @Override
            public void onLoadMore() {
                page = beyondFenceAdapter.getItemCount() / 20 + 1;
                getNetData();
            }
        });
        // 列表点击
        beyondFenceAdapter.setOnItemClickListener((position, item) -> {
        });
        // 选中监听
        beyondFenceAdapter.setOnCheckedListener(new BeyondFenceAdapter.OnCheckedListener() {
            @Override
            public void checked(String day) {
                if (beyondFenceAdapter.selectPosition == -1) {
                    activity.addMoreMarker(new ArrayList<>());
                } else {
                    getAllLocation(day);
                }

            }
        });
    }

    @Override
    public void processLogic() {
        // 获取列表数据
        getNetData();
    }

    private void getNetData() {
        HashMap<String, String> map = new HashMap<>();
        User user = ZhaoLinApplication.getInstance().getLoginUserDoLogin(mActivity);
        if (user == null) {
            mActivity.finish();
            return;
        }
        // 用户id
        map.put("uid", user.getData().getId());
        map.put("page", page + "");
        RequestManager.createRequest(ZLURLConstants.WATCH_CAVEAT_DAY_URL, map, new OnMyActivityRequestListener<BeyondFenceBean>((BaseActivity) mActivity) {
            @Override
            public void onSuccess(BeyondFenceBean bean) {
                // 设置数据
                mRecyclerView.handlerSuccessHasRefreshAndLoad(beyondFenceAdapter, page == 1, bean.getData());
                for (int i = 0; i < 19; i++) {
                    beyondFenceAdapter.addList(bean.getData());
                }
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                super.onFailed(isError, code, message);
                mRecyclerView.getxRecyclerView().refreshComplete();
                mRecyclerView.getxRecyclerView().loadMoreComplete();
            }
        });
    }

    private void getAllLocation(String day) {
        HashMap<String, String> map = new HashMap<>();
        User user = ZhaoLinApplication.getInstance().getLoginUserDoLogin(mActivity);
        if (user == null) {
            mActivity.finish();
            return;
        }
        // 用户id
        map.put("uid", user.getData().getId());
        // 天
        map.put("day", day);
        RequestManager.createRequest(ZLURLConstants.WATCH_CAVEAT_DAY_LIST_URL, map, new OnMyActivityRequestListener<LocationRecordListBean>((BaseActivity) mActivity) {
            @Override
            public void onSuccess(LocationRecordListBean bean) {
                List<LocationRecordListBean.DataBean> data = bean.getData();
                if (latLngs.size() > 0) {
                    // 先清除列表内容
                    latLngs.clear();
                }
                for (int i = 0; i < data.size(); i++) {
                    LocationRecordListBean.DataBean item = data.get(i);
                    latLngs.add(new LatLng(NumberFormatUtils.toDouble(item.getLatitude()), NumberFormatUtils.toDouble(item.getLongitude())));
                }
                activity.addMoreMarker(latLngs);
            }
        });
    }

    /**
     * 展示所有定位点
     */
    public void showAllLocation() {
        if (beyondFenceAdapter.selectPosition != -1 && latLngs.size() > 0) {
            activity.addMoreMarker(latLngs);
        }
    }

    public void clear(){
        page = 1;
        beyondFenceAdapter.clear();
        latLngs.clear();
        activity.addMoreMarker(latLngs);
    }
}
