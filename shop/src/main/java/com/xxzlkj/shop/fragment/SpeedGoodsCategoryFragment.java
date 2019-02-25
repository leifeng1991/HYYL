package com.xxzlkj.shop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.adapter.SpeedGoodsCategoryAdapter;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.model.SpeedGoodsCategoryBean;
import com.xxzlkj.shop.weight.HeaderRecyclerView;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.ReuseViewFragment;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * 时速达商品分类fragment
 * create an instance of this fragment.
 */
public class SpeedGoodsCategoryFragment extends ReuseViewFragment {
    private HeaderRecyclerView mRecyclerView;
    //广告图片
    private ImageView mBannerImage;
    // 分类id
    private String mId;
    //广告图片布局
    private View mHeaderView;
    private SpeedGoodsCategoryAdapter mAdapter;
    // 商店id
    private String mStoreId;

    /**
     * @param id      分类id （必传）
     * @param storeId 商店id （必传）
     * @return
     */
    public static SpeedGoodsCategoryFragment newInstance(String id, String storeId) {
        SpeedGoodsCategoryFragment fragment = new SpeedGoodsCategoryFragment();
        Bundle args = new Bundle();
        args.putString(StringConstants.INTENT_PARAM_ID, id);
        args.putString(StringConstants.INTENT_PARAM_STOREID, storeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getString(StringConstants.INTENT_PARAM_ID);
            mStoreId = getArguments().getString(StringConstants.INTENT_PARAM_STOREID);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter == null) {
            getData();
        }
    }

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_goods_category, container, false);
    }

    @Override
    protected void findViewById() {
        // 分类
        mRecyclerView = getView(R.id.id_gc_recyclerview);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 头
        mHeaderView = View.inflate(getContext(), R.layout.view_gcf_header, null);
        mBannerImage = (ImageView) mHeaderView.findViewById(R.id.id_gc_header_image);
    }

    @Override
    public void setListener() {
    }

    @Override
    public void processLogic() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    /**
     * 分类左面数据
     */
    private void getData() {
        final Map<String, String> map = new HashMap<>();
        String url;
        if ("-1".equals(mId)) {
            // 预售分类右侧 主分类id等于-1时调用
            url = URLConstants.ADVANCE_GROUP_RIGHT_URL;
            map.put(StringConstants.INTENT_PARAM_STORE_ID, mStoreId);
        } else if ("-2".equals(mId)) {
            // 团购分类右侧 主分类id等于-2时调用
            url = URLConstants.GROUPON_GROUP_RIGHT_URL;
            map.put(StringConstants.INTENT_PARAM_STORE_ID, mStoreId);
        } else {
            // 分类id
            map.put(URLConstants.REQUEST_PARAM_ID, mId);
            // 时速达分类右侧页面
            url = URLConstants.SHOP_GROUP_RIGHT_URL;
        }
        RequestManager.createRequest(url, map,
                new OnMyActivityRequestListener<SpeedGoodsCategoryBean>((BaseActivity) getContext()) {
                    @Override
                    public void onSuccess(SpeedGoodsCategoryBean bean) {
                        SpeedGoodsCategoryBean.DataBean data = bean.getData();
                        // 设置图片
                        String banner = data.getBanner();
                        if (!TextUtils.isEmpty(banner)) {
                            mHeaderView.setVisibility(View.VISIBLE);
                            mRecyclerView.addHeaderView(mHeaderView);
                            PicassoUtils.setImageBig(getContext(), banner, mBannerImage);
                        } else {
                            mHeaderView.setVisibility(View.GONE);
                        }
                        mAdapter = new SpeedGoodsCategoryAdapter(getContext(), mActivity, bean.getData(), mId, mStoreId, R.layout.adapter_goods_category_item);
                        mRecyclerView.setAdapter(mAdapter);
                        mAdapter.addList(data.getGroup());

                    }
                });
    }

}
