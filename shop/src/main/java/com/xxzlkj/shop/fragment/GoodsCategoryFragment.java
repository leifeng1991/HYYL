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
import com.xxzlkj.shop.activity.shop.GoodsDetailActivity;
import com.xxzlkj.shop.activity.shop.SearchGoodsListActivity;
import com.xxzlkj.shop.adapter.GoodsCategoryAdapter;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.interfac.ShopLibraryInterface;
import com.xxzlkj.shop.model.Goods;
import com.xxzlkj.shop.model.GoodsCategoryDate;
import com.xxzlkj.shop.model.GoodsGroupBean;
import com.xxzlkj.shop.weight.HeaderRecyclerView;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.ReuseViewFragment;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 商品分类fragment
 * create an instance of this fragment.
 */
public class GoodsCategoryFragment extends ReuseViewFragment {
    private HeaderRecyclerView mRecyclerView;
    //广告图片
    private ImageView mBannerImage;
    // 分类id
    private String mId;
    private List<String> mLists = new ArrayList<>();
    private List<List<Goods>> mGLists = new ArrayList<>();
    //广告图片布局
    private View mHeaderView;
    private GoodsCategoryAdapter mAdapter;

    /**
     * @param id 分类ID （必传）
     * @return A new instance of fragment GoodsCategoryFragment.
     */
    public static GoodsCategoryFragment newInstance(String id) {
        GoodsCategoryFragment fragment = new GoodsCategoryFragment();
        Bundle args = new Bundle();
        args.putString(StringConstants.INTENT_PARAM_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getString(StringConstants.INTENT_PARAM_ID);
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
        mRecyclerView = getView(R.id.id_gc_recyclerview);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

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
        map.put(URLConstants.REQUEST_PARAM_ID, mId);
        RequestManager.createRequest(URLConstants.REQUEST_GROUP_RIGHT, map,
                new OnMyActivityRequestListener<GoodsCategoryDate>((BaseActivity) getContext()) {
                    @Override
                    public void onSuccess(GoodsCategoryDate bean) {
                        GoodsCategoryDate.DataBean mDataBean = bean.getData();
                        final GoodsCategoryDate.DataBean.BannerBean mBannerBean = mDataBean.getBanner();

                        if (!TextUtils.isEmpty(mBannerBean.getSimg())) {
                            mHeaderView.setVisibility(View.VISIBLE);
                            mRecyclerView.addHeaderView(mHeaderView);
                            PicassoUtils.setImageBig(getContext(), mBannerBean.getSimg(), mBannerImage);
                            mBannerImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!TextUtils.isEmpty(mBannerBean.getTo())) {
                                        switch (NumberFormatUtils.toInt(mBannerBean.getType())) {
                                            case StringConstants.INTENT_TO_NO:// 不跳转
                                                break;
                                            case StringConstants.INTENT_TO_H5:// 跳转h5
                                                if (BaseApplication.getInstance() instanceof ShopLibraryInterface) {
                                                    // 让主项目处理
                                                    ((ShopLibraryInterface) BaseApplication.getInstance()).jumpToHasTitleWebView(mActivity, mBannerBean.getTo(), "详情");
                                                }
                                                break;
                                            case StringConstants.INTENT_TO_GOODS:// 跳转商品详情
                                                mActivity.startActivity(GoodsDetailActivity.newIntent(getContext(), mBannerBean.getTo()));
                                                break;
                                            case StringConstants.INTENT_TO_GOODSTYPE:// 跳转搜索商品列表
                                                mActivity.startActivity(SearchGoodsListActivity.newIntent(getContext(), -1, mBannerBean.getTo(), "", ""));
                                                break;
                                        }
                                    }
                                }
                            });
                        } else {
                            mHeaderView.setVisibility(View.GONE);
                        }

                        GoodsCategoryDate.DataBean.TopGoodsBean top_goods = mDataBean.getTop_goods();
                        if (top_goods != null) {
                            List<Goods> mTopGoods = top_goods.getList();
                            if (mTopGoods != null && mTopGoods.size() > 0) {
                                mLists.add("热卖单品");
                                mGLists.add(mTopGoods);
                            }
                        }

                        List<Goods> mTopGroup = mDataBean.getTop_group();
                        if (mTopGroup != null && mTopGroup.size() > 0) {
                            mLists.add("热门分类");
                            mGLists.add(mTopGroup);
                        }

                        List<GoodsGroupBean> mGroup = mDataBean.getGroup();
                        if (mGroup != null && mGroup.size() > 0) {
                            for (int i = 0; i < mGroup.size(); i++) {
                                mLists.add(mGroup.get(i).getTitle());
                                mGLists.add(mGroup.get(i).getChild());
                            }
                        }

                        mAdapter = new GoodsCategoryAdapter(getContext(), mActivity, R.layout.adapter_goods_category_item, mGLists, mDataBean);
                        mAdapter.addList(mLists);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                });
    }

}
