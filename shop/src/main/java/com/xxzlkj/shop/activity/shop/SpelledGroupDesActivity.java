package com.xxzlkj.shop.activity.shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.adapter.SpelledGroupPeopleAdapter;
import com.xxzlkj.shop.adapter.TuanGouStyleAdapter2;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.model.RecommendGoods;
import com.xxzlkj.shop.model.ShopCartList;
import com.xxzlkj.shop.model.SpelledGroupDesBean;
import com.xxzlkj.shop.utils.TextPriceUtil;
import com.xxzlkj.shop.utils.ZLDateUtils;
import com.xxzlkj.shop.weight.MyTextView;
import com.xxzlkj.shop.weight.RecyclerViewDividerItemDecoration;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;
import com.zrq.spanbuilder.Spans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 参与拼团(详情)
 */
public class SpelledGroupDesActivity extends MyBaseActivity {
    public static final String SPELLED_GROUP_ID = "spelledGroupId";
    private ImageView iv_goods_image;
    private ViewGroup vg_hot_layout;
    private TextView btn_submit_add, tv_goods_title, tv_second, tv_minute, tv_hour, tv_day, tv_remaining_places, tv_goods_money, tv_goods_des;
    private SpelledGroupPeopleAdapter spelledGroupPeopleAdapter;
    private TuanGouStyleAdapter2 spelledGroupHotAdapter;
    private CountDownTimer countDownTimer;
    private String spelledGroupId;
    private SpelledGroupDesBean.DataBean data;

    /**
     * @param spelledGroupId 必传，团购组id
     */
    public static Intent newIntent(Context context, String spelledGroupId) {
        Intent intent = new Intent(context, SpelledGroupDesActivity.class);
        intent.putExtra(SPELLED_GROUP_ID, spelledGroupId);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_spelled_group_des);
    }

    @Override
    protected void findViewById() {
        iv_goods_image = getView(R.id.iv_goods_image);// 商品图
        tv_goods_title = getView(R.id.tv_goods_title);// 商品标题
        tv_goods_des = getView(R.id.tv_goods_des);// 商品拼团描述
        tv_goods_money = getView(R.id.tv_goods_money);// 商品金额

        RecyclerView mRecyclerViewPeople = getView(R.id.recyclerView_people);
        tv_remaining_places = getView(R.id.tv_remaining_places);// 拼团剩余名额
        tv_day = getView(R.id.tv_day);// 剩余时间-天
        tv_hour = getView(R.id.tv_hour);// 剩余时间-小时
        tv_minute = getView(R.id.tv_minute);// 剩余时间-分钟
        tv_second = getView(R.id.tv_second);// 剩余时间-秒

        btn_submit_add = getView(R.id.btn_submit_add);// 一键参团按钮

        vg_hot_layout = getView(R.id.vg_hot_layout);// 热门推荐布局
        MyTextView mHotTitleTextView = getView(R.id.tv_hot_title);// 热门推荐标题
        RecyclerView mRecyclerViewHot = getView(R.id.recyclerView_hot);
        // 初始化拼团人员列表
        mRecyclerViewPeople.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewPeople.setNestedScrollingEnabled(false);
        spelledGroupPeopleAdapter = new SpelledGroupPeopleAdapter(mContext, R.layout.item_spelled_group_people);
        mRecyclerViewPeople.setAdapter(spelledGroupPeopleAdapter);

        // 初始化热门推荐
        mHotTitleTextView.setGradientColor(0xffFF3EE5, 0xffFF4C65);
        mHotTitleTextView.setText("一 热门推荐 一");

        mRecyclerViewHot.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerViewHot.setNestedScrollingEnabled(false);
        // 分割线
        RecyclerViewDividerItemDecoration dividerItemDecoration = new RecyclerViewDividerItemDecoration(mContext, LinearLayout.VERTICAL, 2);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.shape_divide_2));
        mRecyclerViewHot.addItemDecoration(dividerItemDecoration);
        mRecyclerViewHot.setItemAnimator(new DefaultItemAnimator());
        spelledGroupHotAdapter = new TuanGouStyleAdapter2(mContext, this, false, R.layout.item_spelled_group_hot);
        mRecyclerViewHot.setAdapter(spelledGroupHotAdapter);

    }

    @Override
    protected void setListener() {
        // 商品跳到详情页面
        getView(R.id.vg_goods_layout).setOnClickListener(v -> {
            if (data != null) {
                startActivity(GoodsDetailActivity.newIntent(mContext, data.getId()));
            } else
                ToastManager.showShortToast(mContext, "获取网络数据错误，请检查网络或稍后再试");
        });
        // 一键参团按钮
        btn_submit_add.setOnClickListener(v -> {
            if (data != null) {
                summitCheckData(data);
            } else
                ToastManager.showShortToast(mContext, "获取网络数据错误，请检查网络或稍后再试");
        });
        // 热门推荐item点击，跳到详情页面
        spelledGroupHotAdapter.setOnItemClickListener((position, item) -> {
            // 跳转到商品详情
            startActivity(GoodsDetailActivity.newIntent(mContext, item.getGoods_id()));
        });
    }


    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("参与拼团");
        ZLDateUtils.initCurrentTime();
        // 获取值
        spelledGroupId = getIntent().getStringExtra(SPELLED_GROUP_ID);
        // 获取数据
        getNetData();
        // 获取热门推荐信息
        getRecommendList();
    }

    /**
     * 获取数据
     */
    private void getNetData() {
        Map<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_ID, spelledGroupId);
        RequestManager.createRequest(URLConstants.GROUPON_TEAM_INFO_URL, map, new OnMyActivityRequestListener<SpelledGroupDesBean>(this) {
            @Override
            public void onSuccess(SpelledGroupDesBean bean) {
                // 设置数据
                setData(bean.getData());
            }
        });
    }

    /**
     * 设置数据
     */
    private void setData(SpelledGroupDesBean.DataBean data) {
        this.data = data;
        // 设置商品信息
        PicassoUtils.setImageSmall(mContext, data.getSimg(), iv_goods_image);// 商品图
        tv_goods_title.setText(data.getTitle());// 商品标题
        String numStr = data.getNum();// 几人团
        TextViewUtils.setTextHasValue(tv_goods_des, numStr, "人拼团·已团", data.getTeam_count(), "件");// 商品拼团描述
        String price = data.getPrice();
        if (price == null) price = "";
        String pricesStr = TextPriceUtil.getTextPrices(price, data.getPrices());// 返回内容为""
        tv_goods_money.setText(Spans.builder()
                .text(price + " ")
                .text(pricesStr).size(12).color(ContextCompat.getColor(mContext, R.color.gray_9c9c9c)).deleteLine().build());// 商品金额
        // 设置参与人
        List<SpelledGroupDesBean.DataBean.TeamUserBean> team_user = data.getTeam_user();
        if (team_user == null)
            team_user = new ArrayList<>();
        int haveGroupNum = team_user.size();// 参与人数
        int allGroupNum = NumberFormatUtils.toInt(numStr);// 所有人数
        int unHaveGroupNum = allGroupNum - haveGroupNum;// 未参与人数
        if (unHaveGroupNum > 0) {
            // 参与人数小于团购总人数，添加 （allGroupNum - haveGroupNum）个未参加状态
            for (int i = 0; i < unHaveGroupNum; i++) {
                team_user.add(new SpelledGroupDesBean.DataBean.TeamUserBean());
            }
        }
        spelledGroupPeopleAdapter.clearAndAddList(team_user);
        // 设置剩余名额
        // --仅剩2个名额，赶快来一起拼团吧！
        tv_remaining_places.setText(Spans.builder()
                .text("仅剩")
                .text(unHaveGroupNum + "个").color(ContextCompat.getColor(mContext, R.color.orange_ff725c))
                .text("名额，赶快来一起拼团吧！")
                .build());
        // 设置剩余时间
        long currentTime = ZLDateUtils.getCurrentTimeSeconds();// 开始时间
        long stopTime = NumberFormatUtils.toLong(data.getStoptime());// 结束时间
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (stopTime >= currentTime)
            countDownTimer = new CountDownTimer((stopTime - currentTime) * 1000, 1000) {//倒计时时间
                public void onTick(long millisUntilFinished) {
                    int days = (int) (millisUntilFinished / (24 * 60 * 60 * 1000));// 天
                    millisUntilFinished = (int) (millisUntilFinished % (24 * 60 * 60 * 1000));// 剩余时间
                    int hours = (int) (millisUntilFinished / (60 * 60 * 1000));// 小时
                    millisUntilFinished = (int) (millisUntilFinished % (60 * 60 * 1000));// 剩余时间
                    int minutes = (int) (millisUntilFinished / (60 * 1000));// 分钟
                    millisUntilFinished = (int) (millisUntilFinished % (60 * 1000));// 剩余时间
                    int seconds = (int) (millisUntilFinished / (1000));// 秒

                    tv_day.setText(String.format(Locale.CHINA, "%02d", days));// 剩余时间-天
                    tv_hour.setText(String.format(Locale.CHINA, "%02d", hours));// 剩余时间-小时
                    tv_minute.setText(String.format(Locale.CHINA, "%02d", minutes));// 剩余时间-分钟
                    tv_second.setText(String.format(Locale.CHINA, "%02d", seconds));// 剩余时间-秒
                }

                @SuppressLint("SetTextI18n")
                public void onFinish() {//倒计时结束
                    tv_day.setText("00");
                    tv_hour.setText("00");
                    tv_minute.setText("00");
                    tv_second.setText("00");
                }
            }.start();


    }

    /**
     * 提交检测
     */
    private void summitCheckData(SpelledGroupDesBean.DataBean data) {
        User user = BaseApplication.getInstance().getLoginUserDoLogin(this);
        if (user == null) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_ID, spelledGroupId);
        map.put(URLConstants.REQUEST_PARAM_UID, user.getData().getId());
        RequestManager.createRequest(URLConstants.GROUPON_ITEM_CHECK_URL, map, new OnMyActivityRequestListener<BaseBean>(this) {
            @Override
            public void onSuccess(BaseBean bean) {
                // 检测成功，跳到确认订单页面
                // 拼接参数
                ShopCartList.DataBean dataBean = new ShopCartList.DataBean();
                List<ShopCartList.DataBean.GBean> goodses = new ArrayList<>();
                ShopCartList.DataBean.GBean gBean = new ShopCartList.DataBean.GBean();
                gBean.setId(data.getGoods_id());
                gBean.setGoods_id(data.getGoods_id());
                gBean.setSimg(data.getSimg());
                gBean.setNum("1");
                gBean.setPrice(data.getPrice());
                gBean.setPrices(data.getPrices());
                gBean.setAds(data.getAds());
                gBean.setTitle(data.getTitle());
                goodses.add(gBean);
                dataBean.setGoods(goodses);
                // 跳到确认订单页面
                startActivity(MakeSureOrderActivity.newIntent(mContext, dataBean, null,
                        NumberFormatUtils.toDouble(data.getPrice()), spelledGroupId));
            }
        });
    }

    /**
     * 获取推荐商品列表
     */
    private void getRecommendList() {
        Map<String, String> map = new HashMap<>();
        if (TextUtils.isEmpty(GlobalParams.storeId)) {
            return;
        }
        // 店铺id (必传)
        map.put(URLConstants.REQUEST_PARAM_STORE_ID, GlobalParams.storeId);

        RequestManager.createRequest(URLConstants.REQUEST_GROUPON_RECOMMEND, map, new OnMyActivityRequestListener<RecommendGoods>(this) {
            @Override
            public void onSuccess(RecommendGoods bean) {
                // 设置热门推荐
                vg_hot_layout.setVisibility(View.VISIBLE);
                spelledGroupHotAdapter.clearAndAddList(bean.getData());
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                vg_hot_layout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZLDateUtils.cancelTimer();
    }
}
