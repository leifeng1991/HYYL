package com.xxzlkj.huayiyanglao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunfusheng.marqueeview.DisplayUtil;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.activity.localLife.SubscribeActivity;
import com.xxzlkj.licallife.adapter.ProjecImageAdapter;
import com.xxzlkj.licallife.model.ProjectDetail;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.CallPhoneUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;
import com.zrq.spanbuilder.Spans;

import java.util.HashMap;


/**
 * 医养医疗项目详情
 */
public class HealthProjectDetailActivity extends MyBaseActivity {
    private ImageView mBannerView;
    private TextView mPriceTextView;
    private TextView mNowBuyTextView;
    private LinearLayout mPhoneLinearLayout;
    private TextView mPreferentialTextView;
    private TextView mContentTextView;
    private LinearLayout ll_bottom_button;
    private RelativeLayout mRelativeLayout;
    private RecyclerView mImagesRecyclerView;
    private ProjectDetail.DataBean data;
    // 产品id
    private String id;
    // 防止执行多次
    private boolean isNowBottomShow;

    /**
     * @param context 上下文 （必传）
     * @param id      保洁产品id  （必传）
     * @return
     */
    public static Intent newIntent(Context context, String id) {
        Intent intent = new Intent(context, HealthProjectDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(StringConstants.INTENT_PARAM_ID, id);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_project_detail);
    }

    @Override
    protected void findViewById() {
        // 轮播
        mBannerView = getView(R.id.id_pd_banner);
        mPriceTextView = getView(R.id.id_pd_price);
        mNowBuyTextView = getView(R.id.id_pd_now);
        mPhoneLinearLayout = getView(R.id.id_pd_phone);
        mRelativeLayout = getView(R.id.id_relativelayout);
        mPreferentialTextView = getView(R.id.id_pd_preferential);
        mContentTextView = getView(R.id.id_pd_content);
        // 图片列表
        mImagesRecyclerView = getView(R.id.id_pd_bottom_image);
        mImagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 禁止RecyclerView滑动解决RecyclerView不显示问题
        mImagesRecyclerView.setNestedScrollingEnabled(false);
        // 底部按钮
        ll_bottom_button = getView(R.id.ll_bottom_button);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString(StringConstants.INTENT_PARAM_ID);
        }
    }

    @Override
    protected void setListener() {
        mPhoneLinearLayout.setOnClickListener(this);
        mNowBuyTextView.setOnClickListener(this);
        setOnClickListener(R.id.id_pd_phone_bottom, R.id.id_pd_now_bottom);
        //标题栏背景变化
        NestedScrollView mNestedScrollView = getView(R.id.id_gd_scroll);
        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY < (mBannerView.getHeight() + mRelativeLayout.getPaddingTop() + mPhoneLinearLayout.getHeight())) {// 此方法不会逻辑深入获取
                    // 隐藏
                    if (isNowBottomShow) {// 防止执行多次
                        hideBottom();
                        isNowBottomShow = false;
                    }
                } else {
                    // 打开
                    if (!isNowBottomShow) {// 防止执行多次
                        openBottom();
                        isNowBottomShow = true;
                    }
                }


            }

        });
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("服务详情");
        setTitleRightImage(R.mipmap.share_icon);
        getProjectDetail(id);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.id_pd_now || i == R.id.id_pd_now_bottom) {
            User loginUserDoLogin = BaseApplication.getInstance().getLoginUserDoLogin(this);
            if (loginUserDoLogin != null && data != null) {
                startActivity(HealthSubscribeActivity.newIntent(this, id, data));
            }

        } else if (i == R.id.id_pd_phone || i == R.id.id_pd_phone_bottom) {
            if (data != null) {
                CallPhoneUtils.callPhoneDialog(HealthProjectDetailActivity.this, data.getShop_phone());
            }

        } else if (i == R.id.iv_title_right) {
        }
    }

    private void openBottom() {
        ll_bottom_button.setVisibility(View.VISIBLE);
        ll_bottom_button.setTranslationY(ll_bottom_button.getHeight());
        ll_bottom_button.animate().translationY(0).setDuration(500).start();// animate 会先取消，后执行
    }

    private void hideBottom() {
        ll_bottom_button.setVisibility(View.VISIBLE);
        ll_bottom_button.setTranslationY(0);
        ll_bottom_button.animate().translationY(ll_bottom_button.getHeight()).setDuration(500).start();
    }

    /**
     * 医养医疗产品详情
     */
    private void getProjectDetail(String id) {
        HashMap<String, String> map = new HashMap<>();
        // 产品id
        map.put(URLConstants.REQUEST_PARAM_ID, id);
        RequestManager.createRequest(ZLURLConstants.HEALTH_GOODS_DETAILS_URL,
                map, new OnMyActivityRequestListener<ProjectDetail>(this) {
                    @Override
                    public void onSuccess(ProjectDetail bean) {
                        data = bean.getData();
                        ProjecImageAdapter mImageAdapter = new
                                ProjecImageAdapter(HealthProjectDetailActivity.this, R.layout.adapter_goods_detail_image);
                        mImageAdapter.addList(data.getImg());
                        mImagesRecyclerView.setAdapter(mImageAdapter);
                        mPriceTextView.setText(Spans.builder().text(data.getRealPrice()).text("/" + data.getUnit_desc(), 15, 0xffff725c).build());
                        int wh = DisplayUtil.getWindowWidth(HealthProjectDetailActivity.this);
                        mBannerView.getLayoutParams().width = wh;
                        mBannerView.getLayoutParams().height = wh;
                        PicassoUtils.setWithAndHeight(HealthProjectDetailActivity.this, data.getSimg(), wh, wh, mBannerView);
                        // 广告语
                        mPreferentialTextView.setText(data.getAds());

                    }

                });
    }
}
