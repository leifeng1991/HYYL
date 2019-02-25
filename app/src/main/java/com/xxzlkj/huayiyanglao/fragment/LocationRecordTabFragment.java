package com.xxzlkj.huayiyanglao.fragment;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps.model.LatLng;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.activity.equipment.LocationHistoryActivity;
import com.xxzlkj.huayiyanglao.adapter.BeyondFenceAdapter;
import com.xxzlkj.huayiyanglao.adapter.LocationRecordAdapter;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.LocationHistoryTabBean;
import com.xxzlkj.huayiyanglao.model.LocationRecordListBean;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.ReuseViewFragment;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 描述：位置记录
 *
 * @author zhangrq
 *         2017/9/2 10:58
 */
public class LocationRecordTabFragment extends ReuseViewFragment {
    private MyRecyclerView mRecyclerView;
    private LocationRecordAdapter locationRecordAdapter;
    private LocationHistoryActivity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (LocationHistoryActivity) activity;
    }

    public static LocationRecordTabFragment newInstance() {
        LocationRecordTabFragment locationHistoryTabFragment = new LocationRecordTabFragment();
        return locationHistoryTabFragment;
    }

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_location_history_tab, container, false);
    }

    @Override
    protected void findViewById() {
        mRecyclerView = getView(R.id.recyclerView);// 内容列表
        mRecyclerView.setPullRefreshAndLoadingMoreEnabled(true, true);
        // 初始化
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        locationRecordAdapter = new LocationRecordAdapter(mContext, R.layout.adapter_location_record_item);
        mRecyclerView.setAdapter(locationRecordAdapter);
    }

    @Override
    public void setListener() {
        // 列表点击事件
        locationRecordAdapter.setOnItemClickListener((position, item) -> {
            List<LatLng> latLngs = new ArrayList<>();
            latLngs.add(new LatLng(NumberFormatUtils.toDouble(item.getLatitude()), NumberFormatUtils.toDouble(item.getLongitude())));
            activity.addMoreMarker(latLngs);
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
        /*if (TextUtils.isEmpty(activity.day)){
            return;
        }*/
        // 天
        map.put("day", "2018-02-01");
//        map.put("day", activity.day);
        RequestManager.createRequest(ZLURLConstants.WATCH_CAVEAT_DAY_LIST_URL, map, new OnMyActivityRequestListener<LocationRecordListBean>((BaseActivity) mActivity) {
            @Override
            public void onSuccess(LocationRecordListBean bean) {
                List<LocationRecordListBean.DataBean> data = bean.getData();
                List<LatLng> latLngs = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    LocationRecordListBean.DataBean item = data.get(i);
                    latLngs.add(new LatLng(NumberFormatUtils.toDouble(item.getLatitude()), NumberFormatUtils.toDouble(item.getLongitude())));
                }
                activity.addMoreMarker(latLngs);
                // 设置数据
                mRecyclerView.handlerSuccessHasRefreshAndLoad(locationRecordAdapter, true, bean.getData());
            }
        });
    }

    public void clear(){
        locationRecordAdapter.clear();
        activity.addMoreMarker(new ArrayList<>());
    }

}
