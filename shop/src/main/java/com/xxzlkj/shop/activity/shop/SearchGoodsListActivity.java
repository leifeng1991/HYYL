package com.xxzlkj.shop.activity.shop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.shop.R;
import com.xxzlkj.shop.adapter.GoodsListAdapter;
import com.xxzlkj.shop.adapter.RecommendGoodsAdapter;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.event.DataChangEvent;
import com.xxzlkj.shop.model.Goods;
import com.xxzlkj.shop.model.GoodsGroupBean;
import com.xxzlkj.shop.model.GoodsList;
import com.xxzlkj.shop.model.RecommendGoods;
import com.xxzlkj.shop.model.SpeedGoodsCategoryBean;
import com.xxzlkj.shop.utils.AddShopCartUtil;
import com.xxzlkj.shop.utils.ShopCartNumberUtils;
import com.xxzlkj.shop.utils.listview.CommBaseAdapter;
import com.xxzlkj.shop.utils.listview.ViewHolder;
import com.xxzlkj.shop.weight.CustomScrollView;
import com.xxzlkj.shop.weight.RecyclerViewDividerItemDecoration;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.BasePopupWindow;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索商品列表
 */
public class SearchGoodsListActivity extends MyBaseActivity {
    public static final String GOODSCATEGORYDATE_DATABEAN = "GoodsCategoryDate.DataBean";
    private TextView mTextView1, mShopCartNumTextView, mInputTipTextView;
    private LinearLayout mLinearLayout1, mLinearLayout2, mLinearLayout3, mLinearLayout4, mSearchView;
    private RelativeLayout mRelativeLayout, mAlphLayout;
    public ImageView mShopCartImageView, mBackImageView, mListOrGridImage;
    private BasePopupWindow mPopupWindow;
    private ListView mLeftListView, mRightListView;
    private MyRecyclerView mRecyclerView;
    private RecyclerView mRecomendRecyclerView;
    private CustomScrollView mScrollView;
    //是否是网格布局
    private boolean isGirdView = false;
    //页数
    private int page = 1;
    //desc 高到低  asc低到高
    private String mOrd = "asc";
    //按什么排序 1销量 2价格 3活动 0和不传默认
    private String mOrder = "0";
    private int mJumpType;
    private String mLeftTitle;
    private String mRightTitle;
    // 分类id
    private String mGroupId;
    // 关键字
    private String mKeyword;
    // 门店id
    private String mStoreId;

    private ImageView[] mImageViews = new ImageView[4];
    private Boolean[] isUpOrBottom = new Boolean[]{true, true, true, true};
    private int lastPosition = -1;

    private GoodsListAdapter mRecyclerAdapter;
    private SpeedGoodsCategoryBean.DataBean mDataBean;
    private CommBaseAdapter mRightAdapter;
    // 左右（二级和三级）分类选中下坐标
    private int indexLeftTempFlag = 0;
    private int indexLeftFlag = 0;
    private int indexRightFlag = 0;

    /**
     * @param context 上下文 （必传）
     * @param type    1:分类 2：果蔬生鲜 3：时速达分类 4:门店果蔬生鲜更多 5:预售 6:团购 不等于1时显示全部分类
     * @param groupId 分类id  当keyword为空时必传（必传）
     * @param keyword 可以为"" 关键字 用关键字进行搜索时必传
     * @param keyword 门店id 当groupId为空、type为3/5/6时必传(必传)
     * @return
     */
    public static Intent newIntent(Context context, int type, String groupId, String keyword, String mStoreId) {
        Intent intent = new Intent(context, SearchGoodsListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(StringConstants.INTENT_PARAM_TYPE, type);
        bundle.putString(StringConstants.INTENT_PARAM_GROUPID, groupId);
        bundle.putString(StringConstants.INTENT_PARAM_KEYWORD, keyword);
        bundle.putString(StringConstants.INTENT_PARAM_STOREID, mStoreId);
        intent.putExtras(bundle);
        return intent;
    }

    /**
     * 只用到从商品分类界面跳转来,其他地方跳转用上面的
     *
     * @param context    上下文 （必传）
     * @param dataBean   二级和三级分类数据 （必传）
     * @param leftTitle  左边（二级）分类title （必传）
     * @param rightTitle 右边（三级）分类title （必传）
     * @param groupId    分类ID （必传）
     * @param mStoreId   门店ID （必传）
     * @return
     */
    public static Intent newIntent(Context context, SpeedGoodsCategoryBean.DataBean dataBean, int type, String leftTitle, String rightTitle, String groupId, String mStoreId) {
        Intent intent = new Intent(context, SearchGoodsListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(StringConstants.INTENT_PARAM_TYPE, type);
        bundle.putString(StringConstants.INTENT_PARAM_GROUPID, groupId);
        bundle.putString(StringConstants.INTENT_PARAM_LEFT_TITLE, leftTitle);
        bundle.putString(StringConstants.INTENT_PARAM_RIGHT_TITLE, rightTitle);
        bundle.putString(StringConstants.INTENT_PARAM_STOREID, mStoreId);
        bundle.putSerializable(GOODSCATEGORYDATE_DATABEAN, dataBean);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_goods_list);
    }

    @Override
    protected void findViewById() {
        EventBus.getDefault().register(this);
        mBackImageView = getView(R.id.id_gl_back);// 返回键
        mSearchView = getView(R.id.id_gl_search);// 搜索框
        mInputTipTextView = getView(R.id.id_input_tip);// 搜索内容提示
        mShopCartImageView = getView(R.id.id_gl_gouwuche);// 购物车
        mListOrGridImage = getView(R.id.id_gl_lg);// 列表/宫格
        mShopCartNumTextView = getView(R.id.id_gl_number);// 购物车数量
        // tab
        mLinearLayout1 = getView(R.id.id_gl_layout_1);// 全部分类
        mImageViews[0] = getView(R.id.id_gl_image_1);
        mLinearLayout2 = getView(R.id.id_gl_layout_2);// 销量
        mImageViews[1] = getView(R.id.id_gl_image_2);
        mLinearLayout3 = getView(R.id.id_gl_layout_3);// 价格
        mImageViews[2] = getView(R.id.id_gl_image_3);
        mLinearLayout4 = getView(R.id.id_gl_layout_4);//活动
        mImageViews[3] = getView(R.id.id_gl_image_4);
        mTextView1 = getView(R.id.id_gl_text_1);
        //列表
        mRecyclerView = getView(R.id.id_gl_list);
        mRecyclerView.setPullRefreshAndLoadingMoreEnabled(true, true);
        // scroll
        mScrollView = getView(R.id.id_gl_scroll);
        mScrollView.setNestedScrollingEnabled(false);
        mScrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        mScrollView.setFocusable(true);
        mScrollView.setFocusableInTouchMode(true);// 无数据时的头，设置获取焦点
        mRelativeLayout = getView(R.id.id_gl_layout);
        mRelativeLayout.setFocusable(true);
        mRelativeLayout.setFocusableInTouchMode(true);
        mRelativeLayout.requestFocus();
        //推荐
        mRecomendRecyclerView = getView(R.id.id_gl_recommend_list);
        mRecomendRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecomendRecyclerView.setFocusable(false);
        // 分割线
        RecyclerViewDividerItemDecoration dividerItemDecoration = new RecyclerViewDividerItemDecoration(mContext, LinearLayout.VERTICAL, 2);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.shape_divide_3));
        mRecomendRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecomendRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // 背景变暗布局
        mAlphLayout = getView(R.id.id_gl_alph_layout);
        mAlphLayout.setAlpha(0);

        initPopWindow();
    }

    @Override
    protected void setListener() {
        mLinearLayout1.setOnClickListener(this);
        mLinearLayout2.setOnClickListener(this);
        mLinearLayout3.setOnClickListener(this);
        mLinearLayout4.setOnClickListener(this);
        mBackImageView.setOnClickListener(this);
        mListOrGridImage.setOnClickListener(this);
        mShopCartImageView.setOnClickListener(this);
        // 刷新和分页加载
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getNetData();
            }

            @Override
            public void onLoadMore() {
                page = mRecyclerAdapter.getItemCount() / mRecyclerView.loadSize + 1;
                getNetData();
            }
        });

        mSearchView.setOnClickListener(v -> startActivity(HotSearchActivity.newIntent(this)));

    }

    @Override
    protected void processLogic() {
        initData(getIntent());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.id_gl_layout_1) {
            if ("全部分类".equals(mTextView1.getText().toString())) {
                // 跳转到门店分类
                startActivity(SpeedGoodsCategoryActivity.newIntent(mContext, TextUtils.isEmpty(mStoreId) ? GlobalParams.storeId : mStoreId, "0"));

            } else {
                mPopupWindow.showAsDropDown(mLinearLayout1, 0, 1);
                mAlphLayout.setAlpha(0.5f);

                mOrder = "0";
                setArrowDirection(0, lastPosition);
                lastPosition = 0;

                mLeftListView.setItemChecked(indexLeftFlag, true);
                if (mRightAdapter != null && mDataBean != null) {
                    createAdapter(indexLeftFlag, true);
                }

            }

        } else if (i == R.id.id_gl_layout_2) {
            mOrder = "1";
            setArrowDirection(1, lastPosition);
            lastPosition = 1;

        } else if (i == R.id.id_gl_layout_3) {
            mOrder = "2";
            setArrowDirection(2, lastPosition);
            lastPosition = 2;

        } else if (i == R.id.id_gl_layout_4) {
            mOrder = "3";
            setArrowDirection(3, lastPosition);
            lastPosition = 3;

        } else if (i == R.id.id_gl_back) {
            finish();

        } else if (i == R.id.id_gl_search) {
            startActivity(new Intent(this, HotSearchActivity.class));

        } else if (i == R.id.id_gl_lg) {
            if (isGirdView) {
                GoodsListAdapter.isGirdView = true;
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                isGirdView = false;
                mListOrGridImage.setImageResource(R.mipmap.menu_list);
            } else {
                GoodsListAdapter.isGirdView = false;
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
                isGirdView = true;
                mListOrGridImage.setImageResource(R.mipmap.menu_gird);
            }

        } else if (i == R.id.id_gl_gouwuche) {
            User loginUserDoLogin = BaseApplication.getInstance().getLoginUserDoLogin(this);
            if (loginUserDoLogin != null) {
                startActivity(ShopCartActivity.newIntent(this));
            }

        }
    }

    private void initData(Intent intent){
        //初始为宫格模式
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        GoodsListAdapter.isGirdView = true;

        //商品列表适配器
        mRecyclerAdapter = new GoodsListAdapter(SearchGoodsListActivity.this, SearchGoodsListActivity.this,
                R.layout.adapter_hr_item_item_4, (view, goods) -> AddShopCartUtil.addShopCart(SearchGoodsListActivity.this, view, mShopCartNumTextView, goods.getId()));
        mRecyclerAdapter.setOnItemClickListener((position, item) ->
                startActivity(GoodsDetailActivity.newIntent(SearchGoodsListActivity.this, item.getId())));
        mRecyclerView.setAdapter(mRecyclerAdapter);

        if (intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            mLeftTitle = bundle.getString(StringConstants.INTENT_PARAM_LEFT_TITLE);
            mRightTitle = bundle.getString(StringConstants.INTENT_PARAM_RIGHT_TITLE);
            mJumpType = bundle.getInt(StringConstants.INTENT_PARAM_TYPE);
            mGroupId = bundle.getString(StringConstants.INTENT_PARAM_GROUPID);
            mKeyword = bundle.getString(StringConstants.INTENT_PARAM_KEYWORD);
            mStoreId = bundle.getString(StringConstants.INTENT_PARAM_STOREID);
            mDataBean = (SpeedGoodsCategoryBean.DataBean) bundle.getSerializable(GOODSCATEGORYDATE_DATABEAN);
            // 获取二级分类和三级分类初始化下坐标
            if (mDataBean != null) {
                for (int i = 0; i < mDataBean.getGroup().size(); i++) {
                    if (mLeftTitle.equals(mDataBean.getGroup().get(i).getTitle())) {
                        indexLeftFlag = i;
                        indexLeftTempFlag = i;
                        break;
                    }
                }
                List<Goods> child = mDataBean.getGroup().get(indexLeftFlag).getChild();
                for (int j = 0; j < child.size(); j++) {
                    if (mRightTitle.equals(child.get(j).getTitle())) {
                        indexRightFlag = j;
                        break;
                    }
                }

                setPopupWindowData();
            }

            if (!TextUtils.isEmpty(mLeftTitle)) {
                // 从分类跳转进来
                mTextView1.setText(mRightTitle);
            } else {
                // 其他地方跳转进来
                mTextView1.setText("全部分类");
                // 显示搜索关键字
                if (!TextUtils.isEmpty(mKeyword))
                    mInputTipTextView.setText(mKeyword);

            }

        }

        getNetData();
        ShopCartNumberUtils.getShopCartNumber(this, mShopCartNumTextView);
    }

    /**
     * @param positon      当前排序位置
     * @param lastPosition 上一次排序位置
     */
    private void setArrowDirection(int positon, int lastPosition) {
        for (int i = 0; i < mImageViews.length; i++) {
            if (positon == i) {
                if (isUpOrBottom[i]) {
                    mImageViews[i].setImageResource(R.mipmap.bottom_arrow_red);
                    isUpOrBottom[i] = false;
                    if (positon != 0) {
                        // 高到低
                        mOrd = "desc";
                    }
                } else {
                    mImageViews[i].setImageResource(R.mipmap.arrow_top_red);
                    isUpOrBottom[i] = true;
                    if (positon != 0) {
                        // 低到高
                        mOrd = "asc";
                    }
                }
            }
            // 归原
            if (positon != lastPosition && lastPosition == i) {
                mImageViews[i].setImageResource(R.mipmap.top_arrow_gray);
                isUpOrBottom[i] = false;
            }

        }
        // 加载第一页
        page = 1;
        getNetData();
    }

    /**
     * 初始化PopupWindow
     */
    private void initPopWindow() {
        final View inflate = View.inflate(mContext, R.layout.popup_wiondow_layout, null);
        //点击外层，销毁 popWindow
        inflate.setOnClickListener(v -> mImageViews[0].setImageResource(R.mipmap.arrow_top_red));
        int height = PixelUtil.dip2px(mContext, 260);
        mPopupWindow = new BasePopupWindow(inflate, LinearLayout.LayoutParams.MATCH_PARENT, height, true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.setOnDismissListener(() -> {
            mAlphLayout.setAlpha(0);
            mImageViews[0].setImageResource(R.mipmap.arrow_top_red);
        });

        mLeftListView = (ListView) inflate.findViewById(R.id.id_left_list);
        mRightListView = (ListView) inflate.findViewById(R.id.id_right_list);
    }

    /**
     * PopupWindow布局设置数据
     */
    private void setPopupWindowData() {
        if (mDataBean != null) {
            //左边 商品列表二级分类适配器
            CommBaseAdapter<GoodsGroupBean> mLeftAdapter =
                    new CommBaseAdapter<GoodsGroupBean>(this, R.layout.adapter_left_list_item, mDataBean.getGroup()) {
                        @Override
                        protected void convert(ViewHolder viewHolder, GoodsGroupBean item, int position) {
                            viewHolder.setText(R.id.id_lli_title, item.getTitle());
                            if (item.getChild() != null && item.getChild().size() > 0) {
                                viewHolder.setText(R.id.id_lli_number, item.getCount());
                            } else {
                                viewHolder.setText(R.id.id_lli_number, "0");
                            }
                        }
                    };
            mLeftListView.setAdapter(mLeftAdapter);
            // 以下两个必须放在setAdapter下面
            mLeftListView.setSelected(true);
            mLeftListView.setItemChecked(indexLeftFlag, true);
            mLeftListView.setOnItemClickListener((parent, view, position, id) -> createAdapter(position, false));
            //右边 商品列表三级分类适配器
            createAdapter(indexLeftFlag, true);
            mRightListView.setOnItemClickListener((parent, view, position, id) -> {
                mPopupWindow.dismiss();
                indexLeftFlag = indexLeftTempFlag;
                indexRightFlag = position;
                mImageViews[0].setImageResource(R.mipmap.arrow_top_red);
                Goods item = (Goods) mRightAdapter.getItem(position);
                if (mRecyclerAdapter != null && mRecyclerAdapter.getItemCount() > 0) {
                    mRecyclerAdapter.clear();
                }
                page = 1;
                mKeyword = "";
                mGroupId = item.getId();
                mTextView1.setText(item.getTitle());
                getNetData();
            });
        }
    }

    private void createAdapter(int position, boolean isFlag) {
        mRightAdapter = new CommBaseAdapter<Goods>(this, R.layout.adapter_right_list_item, mDataBean.getGroup().get(position).getChild()) {

            @Override
            protected void convert(ViewHolder viewHolder, Goods item, int position) {
                viewHolder.setText(R.id.id_rli_title, item.getTitle());
                viewHolder.setText(R.id.id_rli_number, item.getCount());
            }
        };
        mRightListView.setAdapter(mRightAdapter);
        if (isFlag) {
            mRightListView.setItemChecked(indexRightFlag, true);
        } else {
            if (indexLeftFlag == position) {
                mRightListView.setItemChecked(indexRightFlag, true);
            }
            indexLeftTempFlag = position;
        }

    }

    /**
     * 获取商品列表
     */
    private void getNetData() {
        Map<String, String> map = new HashMap<>();
        // 按什么排序 1销量 2价格 3活动 0和不传默认
        map.put(URLConstants.REQUEST_PARAM_ORDER, mOrder);
        // desc 高到低  asc低到高
        map.put(URLConstants.REQUEST_PARAM_ORD, mOrd);
        // 分页 默认为1 一页20条
        map.put(URLConstants.REQUEST_PARAM_PAGE, String.valueOf(page));

        User loginUser = BaseApplication.getInstance().getLoginUser();
        // 会员id
        map.put(URLConstants.REQUEST_PARAM_UID, loginUser == null ? "0" : loginUser.getData().getId());
        // 是否是果蔬生鲜
        boolean isMarket = mJumpType == 2 || mJumpType == 4;
        // 当为果蔬生鲜时参数是id 其他参数是分类id为groupId
        if (!TextUtils.isEmpty(mGroupId)) // groupid
            map.put(isMarket ? URLConstants.REQUEST_PARAM_ID : URLConstants.REQUEST_PARAM_GROUPID, mGroupId);
        if (!TextUtils.isEmpty(mKeyword))
            // 搜索关键字
            map.put(URLConstants.REQUEST_PARAM_KEYWORD, mKeyword);

        // 请求地址
        String url;
        if (isMarket) {
            // 果蔬生鲜更多/门店果蔬生鲜更多商品列表
            url = mJumpType == 2 ? URLConstants.REQUEST_MARKET_GOODS_LIST : URLConstants.MARKET_GOODS_LIST_STORE_URL;
        } else if (mJumpType == 5) {
            // 预售商品列表
            url = URLConstants.ADVANCE_GOODS_LIST_URL;
            // 店铺id （必传）没有传0
            map.put(URLConstants.REQUEST_PARAM_STORE_ID, TextUtils.isEmpty(mStoreId) ? "0" : mStoreId);
        } else if (mJumpType == 6) {
            // 团购商品列表
            url = URLConstants.GROUPON_GOODS_LIST_URL;
            // 店铺id （必传）没有传0
            map.put(URLConstants.REQUEST_PARAM_STORE_ID, TextUtils.isEmpty(mStoreId) ? "0" : mStoreId);
        } else {
            // 时速达商品列表
            url = URLConstants.SHOP_GOODS_LIST_URL;

            if (TextUtils.isEmpty(mStoreId) && TextUtils.isEmpty(GlobalParams.storeId)) {
                finish();
                return;
            }
            // 店铺id （必传）
            map.put(URLConstants.REQUEST_PARAM_STORE_ID, TextUtils.isEmpty(mStoreId) ? GlobalParams.storeId : mStoreId);


        }


        RequestManager.createRequest(url, map, new OnMyActivityRequestListener<GoodsList>(this) {
            @Override
            public void onSuccess(GoodsList bean) {
                if (bean == null || bean.getData() == null || bean.getData().size() <= 0) {
                    if (page == 1) {
                        mRecyclerView.setVisibility(View.GONE);
                        mScrollView.setVisibility(View.VISIBLE);
                        getRecommendList();
                    }
                } else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mScrollView.setVisibility(View.GONE);
                    mRecyclerView.handlerSuccessHasRefreshAndLoad(mRecyclerAdapter, page == 1, bean.getData());
                }
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                super.onFailed(isError, code, message);
                if (page == 1) {
                    mRecyclerView.setVisibility(View.GONE);
                    mScrollView.setVisibility(View.VISIBLE);
                    getRecommendList();
                }

            }
        });
    }

    /**
     * 获取推荐商品列表
     */
    private void getRecommendList() {
        mScrollView.scrollTo(0, 0);
        User loginUser = BaseApplication.getInstance().getLoginUser();
        String id;
        if (loginUser == null || TextUtils.isEmpty(loginUser.getData().getId())) {
            id = "0";
        } else
            id = loginUser.getData().getId();

        if (TextUtils.isEmpty(GlobalParams.storeId)) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        // 会员id (必传)
        map.put(URLConstants.REQUEST_PARAM_UID, id);
        // 店铺id (必传)
        map.put(URLConstants.REQUEST_PARAM_STORE_ID, GlobalParams.storeId);
        RequestManager.createRequest(URLConstants.REQUEST_RECOMMEND_GOODS, map,
                new OnMyActivityRequestListener<RecommendGoods>(this) {
                    @Override
                    public void onSuccess(RecommendGoods bean) {
                        mRelativeLayout.setFocusable(true);
                        mRelativeLayout.setFocusableInTouchMode(true);
                        mRelativeLayout.requestFocus();
                        RecommendGoodsAdapter mRecommendGoodsAdapter =
                                new RecommendGoodsAdapter(SearchGoodsListActivity.this, R.layout.adapter_recommend_item);
                        mRecommendGoodsAdapter.addList(bean.getData());
                        mRecomendRecyclerView.setAdapter(mRecommendGoodsAdapter);
                        mRecommendGoodsAdapter.setOnItemClickListener((position, item) -> startActivity(new GoodsDetailActivity().newIntent(mContext, item.getId())));
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true) //在ui线程执行
    public void updataShopCart(DataChangEvent event) {
        ShopCartNumberUtils.getShopCartNumber(this, mShopCartNumTextView);
    }

}
