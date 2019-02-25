package com.xxzlkj.huayiyanglao.activity.yiyangyiliao;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.adapter.YLProjectAdapter;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.ProjectDetailBean1;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.StringUtil;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;
import com.zrq.spanbuilder.Spans;

import java.util.HashMap;


/**
 * 医养医疗详情
 */
public class YLDetailActivity extends MyBaseActivity {
    private ImageView mImageView;
    private TextView mYyTimeTextView, mYyLocationTextView, mContentTextView, mPriceTextView, mRightNowTextView;
    private RecyclerView mRecyclerView;
    private YLProjectAdapter mYlProjectAdapter;
    private ProjectDetailBean1.DataBean dataBean;
    private String mId;

    /**
     * @param context 上下文 （必传）
     * @param id      产品id  （必传）
     */
    public static Intent newIntent(Context context, String id) {
        Intent intent = new Intent(context, YLDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(StringConstants.INTENT_PARAM_ID, id);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_yyyl_detail);
    }

    @Override
    protected void findViewById() {
        mImageView = getView(R.id.id_image);// 图片
        mYyTimeTextView = getView(R.id.id_yyyl_time);// 时间
        mYyLocationTextView = getView(R.id.id_yyyl_location);// 地址
        mContentTextView = getView(R.id.id_content);// 内容
        mRecyclerView = getView(R.id.id_recyclerview);// 项目列表
        mPriceTextView = getView(R.id.id_price);// 价格
        mRightNowTextView = getView(R.id.id_right_now_subscribe);// 立即预约
    }

    @Override
    protected void setListener() {
        // 立即预约
        mRightNowTextView.setOnClickListener(v -> {
            // 判断用户登录 没登录先让其进行等路然后再进行预约
            User loginUserDoLogin = ZhaoLinApplication.getInstance().getLoginUserDoLogin(YLDetailActivity.this);
            if (loginUserDoLogin == null)
                return;

            if (dataBean != null) {
                // 跳转到时间选择界面
                startActivity(YLSelectServiceTimeActivity.newIntent(mContext, dataBean, dataBean.getRes_type()));
            } else {
                // 提示
                ToastManager.showShortToast(mContext, "网络加载出错");
            }
        });
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        mId = getIntent().getStringExtra(StringConstants.INTENT_PARAM_ID);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setNestedScrollingEnabled(false);
        mYlProjectAdapter = new YLProjectAdapter(mContext, R.layout.adapter_yyyl_detail_list_item);
        mRecyclerView.setAdapter(mYlProjectAdapter);
        getHealthGoodsDetails1();
    }


    /**
     * 医养医疗产品详情
     */
    private void getHealthGoodsDetails1() {
        HashMap<String, String> map = new HashMap<>();
        // 医养医疗产品id
        map.put(URLConstants.REQUEST_PARAM_ID, mId);
        RequestManager.createRequest(ZLURLConstants.HEALTH_GOODS_DETAILS1_URL,
                map, new OnMyActivityRequestListener<ProjectDetailBean1>(this) {
                    @Override
                    public void onSuccess(ProjectDetailBean1 bean) {
                        ProjectDetailBean1.DataBean data = bean.getData();
                        setData(data);
                    }

                });
    }

    /**
     * 设置数据
     */
    public void setData(ProjectDetailBean1.DataBean data) {
        this.dataBean = data;
        setTitleName(data.getTitle());
        // 顶部图片
        PicassoUtils.setImageBig(mContext, data.getSimg(), mImageView);
        // 时长
        mYyTimeTextView.setText(String.format("时长：%s", data.getShow_time()));
        // 地点
        mYyLocationTextView.setText(String.format("地点：%s", data.getAddress()));
        // 详细内容
        mContentTextView.setText(data.getAds());
        // 项目列表
        mYlProjectAdapter.clearAndAddList(data.getProject());
        // 价格
        String price = data.getPrice();
        String priceStr = StringUtil.saveTwoDecimal(price);
        String[] split = priceStr.split("\\.");
        mPriceTextView.setText(Spans.builder().text("¥").size(14).text(TextUtils.isEmpty(price) ? "0" : split[0]).size(20).text("." + (TextUtils.isEmpty(price) ? "00" : split[1])).size(14).build())
        ;
    }
}
