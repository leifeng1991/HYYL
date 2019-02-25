package com.xxzlkj.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunfusheng.marqueeview.DisplayUtil;
import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.shop.GoodsDetailActivity;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.interfac.ShopLibraryInterface;
import com.xxzlkj.shop.model.AdsBean;
import com.xxzlkj.shop.model.Goods;
import com.xxzlkj.shop.model.Market;
import com.xxzlkj.shop.model.TimeBean;
import com.xxzlkj.shop.utils.RecyclerViewHelperListener;
import com.xxzlkj.shop.utils.TextPriceUtil;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.net.OnBaseRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;
import com.xxzlkj.zhaolinshare.base.util.TimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 描述: 商城首页adapter
 *
 * @author zhangrq
 *         2016/12/12 11:06
 */

public class ShopHomeAdapter extends BaseAdapter<AdsBean> {
    private Activity mActivity;
    private int mWidth;
    private float mRatioValue;
    /*******************倒计时*********************/
    //用于退出activity,避免countdown，造成资源浪费。
    private SparseArray<CountDownTimer> mCountDownTimerSparseArray;
    private long startTime = 0;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private Handler mHandler;
    private boolean onScrollListenerEnable = true;

    public ShopHomeAdapter(Context context, Activity mActivity, int itemId) {
        super(context, itemId);
        this.mActivity = mActivity;
        mWidth = DisplayUtil.getWindowWidth(mActivity);
        mRatioValue = (float) (1.0 * mWidth / 750);
        mCountDownTimerSparseArray = new SparseArray<>();
        startTime();
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final AdsBean itemBean) {
        int style = NumberFormatUtils.toInt(itemBean.getStyle());
        setViewAndDataByStyle(holder, itemBean, style);
        LogUtil.e("===" + position, itemBean.getTitle() + "-----" + itemBean.getSimg());


    }

    /**
     * 设置view显示
     */
    public void setViewAndDataByStyle(BaseViewHolder holder, AdsBean itemBean, int style) {
        switch (style) {
            case 1:
                // 类型1 // 1.2.3广告位样式
                setDataStyle1(holder, itemBean);
                setTitleAndImage(holder, itemBean, 1);
                break;
            case 2:
                // 类型2 // 1.2.3广告位样式
                setDataStyle2(holder, itemBean);
                setTitleAndImage(holder, itemBean, 2);
                break;
            case 3:
                // 类型3 // 1.2.3广告位样式
                setDataStyle3(holder, itemBean);
                setTitleAndImage(holder, itemBean, 3);
                break;
            case 4:
                // 类型4：分类产品
                setDataStyle4(holder, itemBean);
                break;
            case 5:
                // 类型5：预售产品
                setDataStyle5(holder, itemBean);
                setTitleAndImage(holder, itemBean, 5);
                break;
            case 6:
                // 类型6：团购产品
                setDataStyle6(holder, itemBean);
                setTitleAndImage(holder, itemBean, 6);
                break;
            case 7:
                // 类型7： 活动产品（果蔬生鲜）
                setDataStyle7(holder, itemBean);
                break;
            case 8:
                // 类型8：
                setDataStyle8(holder, itemBean);
                setTitleAndImage(holder, itemBean, 8);
                break;
            case 9:
                // 类型9：
                setDataStyle9(holder, itemBean);
                setTitleAndImage(holder, itemBean, 9);
                break;
            case 10:
                // 类型10：
                setDataStyle10(holder, itemBean);
                setTitleAndImage(holder, itemBean, 10);
                break;
            case 11:
                // 类型11：
                setDataStyle11(holder, itemBean);
                setTitleAndImage(holder, itemBean, 11);
                break;
            case 12:
                // 类型12：
                setDataStyle12(holder, itemBean);
                setTitleAndImage(holder, itemBean, 12);
                break;
            default:
                // 服务器返回错误的默认处理
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        // 此处换为其他判断依据
        AdsBean adsBean = getList().get(position);
        String style = adsBean.getStyle();
        return getItemViewTypeByStyle(NumberFormatUtils.toInt(style));
    }

    /**
     * 获取item布局
     */
    public static int getItemViewTypeByStyle(int style) {
        switch (style) {
            case 1:
                //  类型1: 1.2.3广告位样式
                return R.layout.adapter_shop_home_style_1;
            case 2:
                // 类型2: 1.2.3广告位样式
                return R.layout.adapter_shop_home_style_2;
            case 3:
                // 类型3: 1.2.3广告位样式
                return R.layout.adapter_shop_home_style_3;
            case 4:
                // 类型：分类产品
                return R.layout.adapter_shop_home_style_4;
            case 5:
                // 类型：预售产品
                return R.layout.adapter_shop_home_style_5;
            case 6:
                // 类型：团购产品
                return R.layout.adapter_shop_home_style_6;
            case 7:
                // 类型： 活动产品（果蔬生鲜）
                return R.layout.adapter_shop_home_style_7;
            case 8:
                // 类型8：
                return R.layout.adapter_shop_home_style_8;
            case 9:
                // 类型9：
                return R.layout.adapter_shop_home_style_8;
            case 10:
                // 类型10：
                return R.layout.adapter_shop_home_style_10;
            case 11:
                // 类型11：
                return R.layout.adapter_shop_home_style_11;
            case 12:
                // 类型12：
                return R.layout.adapter_shop_home_style_8;
            default:
                // 服务器返回错误的默认处理
                return R.layout.adapter_shop_home_style_list;
        }
    }

    /**
     * 样式一
     */
    private void setDataStyle1(BaseViewHolder holder, final AdsBean itemBean) {
        // 固定五张图片
        ImageView mImageView1 = holder.getView(R.id.id_shs7_image_1);
        ImageView mImageView2 = holder.getView(R.id.id_shs7_image_2);
        ImageView mImageView3 = holder.getView(R.id.id_shs7_image_3);
        ImageView mImageView4 = holder.getView(R.id.id_shs7_image_4);
        ImageView mImageView5 = holder.getView(R.id.id_shs7_image_5);
        // 图片集合 图片 为空不显示
        List<Goods> img = itemBean.getImg();
        // 图片数量
        int size = img.size();
        if (img != null && size > 0) {
            if (mWidth > 0 && mRatioValue > 0) {
                int mHeight1 = (int) (240 * mRatioValue);
                if (size >= 1)
                    setImageAndIntent(mImageView1, img.get(0), mHeight1);

                int mHeight2 = (int) (117.5 * mRatioValue);
                if (size >= 2)
                    setImageAndIntent(mImageView2, img.get(1), mHeight2);
                if (size >= 3)
                    setImageAndIntent(mImageView3, img.get(2), mHeight2);
                if (size >= 4)
                    setImageAndIntent(mImageView4, img.get(3), mHeight2);
                if (size == 5)
                    setImageAndIntent(mImageView5, img.get(4), mHeight2);
            }

        }

    }

    /**
     * 样式二
     */
    private void setDataStyle2(BaseViewHolder holder, final AdsBean itemBean) {
        // 固定五张图片
        ImageView mLeftImageView1 = holder.getView(R.id.id_shs9_left_image_1);
        ImageView mLeftImageView2 = holder.getView(R.id.id_shs9_left_image_2);
        ImageView mLeftImageView3 = holder.getView(R.id.id_shs9_left_image_3);
        ImageView mRightImageView1 = holder.getView(R.id.id_shs9_right_image_1);
        ImageView mRightImageView2 = holder.getView(R.id.id_shs9_right_image_2);
        // 图片集合 图片 为空不显示
        List<Goods> img = itemBean.getImg();
        // 图片数量
        int size = img.size();
        if (img != null && size > 0) {
            if (mWidth > 0 && mRatioValue > 0) {
                int mWidth1 = (int) (175 * mRatioValue);
                int mHeight1 = (int) (80 * mRatioValue);
                int mHeight2 = (int) (75 * mRatioValue);
                int mHeight3 = (int) (165 * mRatioValue);
                if (size >= 1)
                    setImageAndIntent(mLeftImageView1, img.get(0), mHeight1);
                if (size >= 2)
                    setImageAndIntent(mRightImageView1, img.get(1), mHeight3);
                if (size >= 3)
                    setImageAndIntent(mLeftImageView2, img.get(2), mHeight1);
                if (size >= 4)
                    setImageAndIntent(mLeftImageView3, img.get(3), mHeight2);
                if (size == 5)
                    setImageAndIntent(mRightImageView2, img.get(4), mHeight2);
            }
        }
    }

    /**
     * 样式三
     */
    private void setDataStyle3(BaseViewHolder holder, final AdsBean itemBean) {
        // 从上到下 图片数量 2-4-4
        LinearLayout mLinearLayout1 = holder.getView(R.id.id_shs10_layout_1);
        LinearLayout mLinearLayout2 = holder.getView(R.id.id_shs10_layout_2);
        LinearLayout mLinearLayout3 = holder.getView(R.id.id_shs10_layout_3);
        // 第一个布局图片
        ImageView mImageView1 = holder.getView(R.id.id_shs10_image_1);
        ImageView mImageView2 = holder.getView(R.id.id_shs10_image_2);
        // 第二个布局图片
        ImageView mImageView3 = holder.getView(R.id.id_shs10_image_3);
        ImageView mImageView4 = holder.getView(R.id.id_shs10_image_4);
        ImageView mImageView5 = holder.getView(R.id.id_shs10_image_5);
        ImageView mImageView6 = holder.getView(R.id.id_shs10_image_6);
        // 第三个布局图片
        ImageView mImageView7 = holder.getView(R.id.id_shs10_image_7);
        ImageView mImageView8 = holder.getView(R.id.id_shs10_image_8);
        ImageView mImageView9 = holder.getView(R.id.id_shs10_image_9);
        ImageView mImageView10 = holder.getView(R.id.id_shs10_image_10);
        // 图片集合 图片 为空不显示
        List<Goods> img = itemBean.getImg();
        // 图片数量
        int size = img.size();
        if (img != null && size > 0) {
            int mWidth1 = mWidth / 2;
            int mHeight1 = (int) (100 * mRatioValue);
            if (size >= 1)
                setImageAndIntent(mImageView1, img.get(0), mHeight1);
            if (size >= 2)
                setImageAndIntent(mImageView2, img.get(1), mHeight1);

            int mWidth2 = mWidth / 4;
            if (size >= 3)
                setImageAndIntent(mImageView3, img.get(2), mHeight1);
            if (size >= 4)
                setImageAndIntent(mImageView4, img.get(3), mHeight1);
            if (size >= 5)
                setImageAndIntent(mImageView5, img.get(4), mHeight1);
            if (size >= 6)
                setImageAndIntent(mImageView6, img.get(5), mHeight1);
            if (size >= 7)
                setImageAndIntent(mImageView7, img.get(6), mHeight1);
            if (size >= 8)
                setImageAndIntent(mImageView8, img.get(7), mHeight1);
            if (size >= 9)
                setImageAndIntent(mImageView9, img.get(8), mHeight1);
            if (size >= 10)
                setImageAndIntent(mImageView10, img.get(9), mHeight1);

            if (size <= 2) {// 图片数量小于/等于2
                mLinearLayout1.setVisibility(View.VISIBLE);
                mLinearLayout2.setVisibility(View.GONE);
                mLinearLayout3.setVisibility(View.GONE);
            } else if (size <= 6) {// 图片数量小于/等于6
                mLinearLayout1.setVisibility(View.VISIBLE);
                mLinearLayout2.setVisibility(View.VISIBLE);
                mLinearLayout3.setVisibility(View.GONE);
            } else {// 图片数量大于6
                mLinearLayout1.setVisibility(View.VISIBLE);
                mLinearLayout2.setVisibility(View.VISIBLE);
                mLinearLayout3.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 样式四
     */
    private void setDataStyle4(BaseViewHolder holder, final AdsBean itemBean) {
        List<AdsBean.GroupBean> group = itemBean.getGroup();
        // 轮播
        ImageView mImageView = holder.getView(R.id.id_shs11_image);
        int mHeight = (int) (110 * mRatioValue);
        // 重新设置高度
        ViewGroup.LayoutParams layoutParams = mImageView.getLayoutParams();
        layoutParams.height = mHeight;
        PicassoUtils.setImageBig(mContext, itemBean.getSimg(), mImageView);

        // 菜单
        GridView mGridView = holder.getView(R.id.id_shs11_gridview);
        mGridView.setNumColumns(5);
        List<String> mTitles = new ArrayList<>();
        for (int i = 0; i < group.size(); i++) {
            mTitles.add(group.get(i).getTitle());
        }
        ShopHomeSelectedGridAdapter adapter = new ShopHomeSelectedGridAdapter(mContext, R.layout.adapter_shop_home_style_41_item, mTitles);
        mGridView.setAdapter(adapter);
        // 设置默认选中
        mGridView.setItemChecked(0, true);
        // 每种菜单类型对应数据
        RecyclerView mRecyclerView = holder.getView(R.id.id_shs11_list);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(layoutManager);
        ShopHomeStyleAdapter11 adapter11 = new ShopHomeStyleAdapter11(mContext, mActivity, true, R.layout.adapter_shop_home_style_42_item);
        // 设置默认为第一菜单对应的数据
        adapter11.addList(group.get(0).getGoods());
        mRecyclerView.setAdapter(adapter11);
        // 菜单监听事件
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 设置adapter选中
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetChanged();
                // 设置内容数据
                adapter11.getList().clear();
                adapter11.addList(group.get(position).getGoods());
            }
        });

        adapter11.setOnItemClickListener(new OnItemClickListener<Goods>() {
            @Override
            public void onClick(int position, Goods item) {
                mActivity.startActivity(GoodsDetailActivity.newIntent(mContext, item.getId()));
            }
        });
    }

    /**
     * 样式五
     */
    private void setDataStyle5(BaseViewHolder holder, final AdsBean bean) {
        LinearLayout mParentLayout = holder.getView(R.id.id_style_5_parent);
        ImageView mFireImageView = holder.getView(R.id.id_fire_image);
        TextView mFireTitle = holder.getView(R.id.id_fire_title);
        TextView mFireGuiGe = holder.getView(R.id.id_fire_guige);
        TextView mFireBuyNow = holder.getView(R.id.id_fire_buy_now);
        TextView mPrice = holder.getView(R.id.id_fire_price);
        TextView mPrices = holder.getView(R.id.id_fire_prices);
        View mLine = holder.getView(R.id.id_fli_line);
        final TextView mFireHour = holder.getView(R.id.id_fire_hour);
        final TextView mFireMinute = holder.getView(R.id.id_fire_minute);
        final TextView mFireSenconds = holder.getView(R.id.id_fire_senconds);
        final TextView mFireMillisencond = holder.getView(R.id.id_fire_millisencond);

        AdsBean.AdvanceBean itemBean = bean.getAdvance();

        // 价格
        String price = itemBean.getPrice();
        String prices = itemBean.getPrices();
        mPrice.setText("￥" + price);
        TextPriceUtil.setTextPrices(price, prices, mPrices);

        mLine.setVisibility(View.GONE);

        if (startTime == 0) {
            startTime = System.currentTimeMillis();
            LogUtil.e("---------" + DateUtils.getYearMonthDayHourMinuteSeconds(NumberFormatUtils.toLong(itemBean.getStoptime()) * 1000) + "=" + DateUtils.getYearMonthDayHourMinuteSeconds(startTime));
        }
        long time = NumberFormatUtils.toLong(itemBean.getStoptime()) * 1000 - startTime;
        // 抢购 跳转到商品详情
        jumpToGoodDetail(mFireBuyNow, itemBean.getGoods_id());
        jumpToGoodDetail(mParentLayout, itemBean.getGoods_id());

        CountDownTimer countDownTimer = mCountDownTimerSparseArray.get(mFireHour.hashCode());
        //将前一个缓存清除
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (time > 0) {
            countDownTimer = new CountDownTimer(time, 100) {//倒计时时间
                public void onTick(long millisUntilFinished) {
                    String[] split = TimeUtil.getCountTimeByLong(millisUntilFinished).split(":");
                    mFireHour.setText(split[0]);
                    mFireMinute.setText(split[1]);
                    mFireSenconds.setText(split[2]);
                    mFireMillisencond.setText(split[3]);
                }

                public void onFinish() {//倒计时结束
                    mFireHour.setText("00");
                    mFireMinute.setText("00");
                    mFireSenconds.setText("00");
                    mFireMillisencond.setText("00");
                }
            }.start();

            mCountDownTimerSparseArray.put(mFireHour.hashCode(), countDownTimer);
        } else {//倒计时结束
            mFireHour.setText("00");
            mFireMinute.setText("00");
            mFireSenconds.setText("00");
            mFireMillisencond.setText("00");
        }

        PicassoUtils.setImageBig(mContext, itemBean.getSimg(), mFireImageView);
        // 标题
        String title = itemBean.getTitle();
        if (TextUtils.isEmpty(title)) {
            mFireTitle.setVisibility(View.GONE);
        } else {
            mFireTitle.setVisibility(View.VISIBLE);
            mFireTitle.setText(title);
        }
        // 规格
//        String ads = itemBean.getAds();
        String ads = "";
        if (TextUtils.isEmpty(ads)) {
            mFireGuiGe.setVisibility(View.GONE);
        } else {
            mFireGuiGe.setVisibility(View.VISIBLE);
            mFireGuiGe.setText(ads);
        }
    }

    /**
     * 清空资源
     */
    public void cancelAllTimers() {
        if (mCountDownTimerSparseArray == null) {
            return;
        }
        Log.e("TAG", "size :  " + mCountDownTimerSparseArray.size());
        for (int i = 0, length = mCountDownTimerSparseArray.size(); i < length; i++) {
            CountDownTimer mCountDownTimer = mCountDownTimerSparseArray.get(mCountDownTimerSparseArray.keyAt(i));
            if (mCountDownTimer != null) {
                mCountDownTimer.cancel();
            }
        }
    }

    /**
     * 获取网络时间戳
     */
    private void startTime() {
        Map<String, String> map = new HashMap<>();
        RequestManager.createRequest(URLConstants.REQUEST_GET_TIME, map, new OnBaseRequestListener<TimeBean>() {
            @Override
            public void handlerSuccess(TimeBean bean) {
                startTime = bean.getData().getTime() * 1000;
                LogUtil.e("---------" + DateUtils.getYearMonthDayHourMinuteSeconds(startTime));
                startTimer();
            }

            @Override
            public void handlerError(int errorCode, String errorMessage) {
                startTime = System.currentTimeMillis();
                startTimer();
            }
        });
    }

    private void startTimer() {
        if (mHandler == null) {
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    startTime += 100;
                }
            };
        }
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessage(message);
            }
        };
        mTimer.schedule(mTimerTask, 100, 100);
    }

    public void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if (mHandler != null) {
            mHandler = null;
        }
    }

    /**
     * 样式六
     */
    private void setDataStyle6(BaseViewHolder holder, final AdsBean bean) {
        LinearLayout mParentLayout = holder.getView(R.id.id_style_6_parent);
        ImageView mTuanImageView = holder.getView(R.id.id_tuan_image);
        TextView mTuanTitle = holder.getView(R.id.id_tuan_title);
        TextView mTuanGuiGe = holder.getView(R.id.id_tuan_guige);
        TextView mTuanBuyNow = holder.getView(R.id.id_tuan_buy_now);
        TextView mTuanPrice = holder.getView(R.id.id_tuan_price);
        TextView mTuanPrices = holder.getView(R.id.id_tuan_prices);
//        TextView mTuanPeples = holder.getView(R.id.id_tuan_peples);
        View mLine = holder.getView(R.id.id_tuan_line);
        // 团购商品
        AdsBean.GrouponBean itemBean = bean.getGroupon();
        // 设置数据
        PicassoUtils.setImageBig(mContext, itemBean.getSimg(), mTuanImageView);
        TextViewUtils.setText(mTuanTitle, itemBean.getTitle());
        TextViewUtils.setText(mTuanGuiGe, itemBean.getAds());
        String price = itemBean.getPrice();
        String prices = itemBean.getPrices();
        TextViewUtils.setText(mTuanPrice, "￥" + price);
        TextPriceUtil.setTextPrices(price, prices, mTuanPrices);

        mLine.setVisibility(View.GONE);
        // 跳转到商品详情
        jumpToGoodDetail(mTuanBuyNow, itemBean.getGoods_id());
        jumpToGoodDetail(mParentLayout, itemBean.getGoods_id());
    }

    /**
     * 样式七
     */
    private void setDataStyle7(BaseViewHolder holder, final AdsBean bean) {
        RecyclerView mRecyclerView = holder.getView(R.id.id_shs3_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setNestedScrollingEnabled(false);
        MarketAdapter adapter = new MarketAdapter(mContext, mActivity, R.layout.adapter_shop_home_style_7_item);
        List<Market> market = new ArrayList<>();
        market.add(bean.getMarket());
        adapter.addList(market);
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * 样式八
     */
    private void setDataStyle8(BaseViewHolder holder, final AdsBean itemBean) {
        RecyclerView mRecyclerView = holder.getView(R.id.id_shs8_list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        YuShouStyleAdapter1 mYuShouStyleAdapter1 = new YuShouStyleAdapter1(mContext, R.layout.adapter_shop_home_style_8_item);
        mRecyclerView.setAdapter(mYuShouStyleAdapter1);
        mYuShouStyleAdapter1.addList(itemBean.getImg());
        // 列表item点击事件
        mYuShouStyleAdapter1.setOnItemClickListener(new OnItemClickListener<Goods>() {
            @Override
            public void onClick(int position, Goods item) {
                // 跳转到主题活动详情页
                if (BaseApplication.getInstance() instanceof ShopLibraryInterface) {
                    // 让主项目处理
                    ((ShopLibraryInterface) BaseApplication.getInstance()).jumpToActivityByType(mActivity, item.getType(), item.getTo());
                }
            }
        });

    }

    /**
     * 样式九
     */
    private void setDataStyle9(BaseViewHolder holder, final AdsBean itemBean) {
        RecyclerView mRecyclerView = holder.getView(R.id.id_shs8_list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        YuShouStyleAdapter2 mYuShouStyleAdapter2 = new YuShouStyleAdapter2(mContext, mActivity, R.layout.adapter_shop_home_style_5_item);
        mRecyclerView.setAdapter(mYuShouStyleAdapter2);
        mYuShouStyleAdapter2.addList(itemBean.getImg());
        // 列表item点击事件
        mYuShouStyleAdapter2.setOnItemClickListener(new OnItemClickListener<Goods>() {
            @Override
            public void onClick(int position, Goods item) {
                // 跳转到商品详情
                mActivity.startActivity(GoodsDetailActivity.newIntent(mContext, item.getTo()));
            }
        });
    }

    /**
     * 样式十
     */
    private void setDataStyle10(BaseViewHolder holder, final AdsBean itemBean) {
        // 初始化标题
        RecyclerView mTitleRecyclerView = holder.getView(R.id.id_title_recycler);// 标题列表
        LinearLayoutManager mTitleLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mTitleRecyclerView.setLayoutManager(mTitleLayoutManager);
        mTitleRecyclerView.setNestedScrollingEnabled(false);
        YuShouTitleAdapter mTitleAdapter = new YuShouTitleAdapter(mContext, R.layout.adapter_yu_shou_title_item);
        mTitleRecyclerView.setAdapter(mTitleAdapter);

        // 初始化商品列表
        RecyclerView mContentRecyclerView = holder.getView(R.id.id_content_recycler);// 商品列表
        LinearLayoutManager mContentLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mContentRecyclerView.setLayoutManager(mContentLinearLayoutManager);
        RecyclerViewHelperListener recyclerViewHelperListener = new RecyclerViewHelperListener(mContentRecyclerView, mContentLinearLayoutManager);
        mContentRecyclerView.addOnScrollListener(recyclerViewHelperListener);
        ShopHomeStyleAdapter10 mContentAdapter = new ShopHomeStyleAdapter10(mContext, mActivity, R.layout.adapter_shop_home_style_10_item);
        mContentRecyclerView.setAdapter(mContentAdapter);

        // 设置title的点击
        mTitleAdapter.setOnItemClickListener((position, item) -> {
            // 滚动到此位置
            mTitleRecyclerView.scrollToPosition(position > mTitleAdapter.getSelectedPosition() ? position + 1 : (position == 0 ? 0 : position - 1));// 下滑预览一，上滑动预览一
            // 设置选中
            mTitleAdapter.setSelectedPosition(position);
            mTitleAdapter.notifyDataSetChanged();
            // 设置滚动，点击滚动的时候，不再监听滚动
            onScrollListenerEnable = false;
            recyclerViewHelperListener.scrollToPosition(position);
            System.out.println("点击监听--内容滚动到" + position);

            mTitleRecyclerView.postDelayed(() -> onScrollListenerEnable = true, 500);
        });
        // 内容滚动监听
        mContentRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstItem = mContentLinearLayoutManager.findFirstVisibleItemPosition();
                if (mTitleAdapter.getSelectedPosition() != firstItem && onScrollListenerEnable) {
                    // 滚动到此位置
                    mTitleRecyclerView.scrollToPosition(dx > 0 ? firstItem + 1 : (firstItem == 0 ? 0 : firstItem - 1));// 下滑预览一，上滑动预览一
                    // 设置选中
                    mTitleAdapter.setSelectedPosition(firstItem);
                    mTitleAdapter.notifyDataSetChanged();
                }
            }
        });
        // 设置内容
        // 设置头
        mTitleAdapter.addList(itemBean.getImg());
        // 设置内容
        mContentAdapter.addList(itemBean.getImg());
    }


    /**
     * 样式十一
     */
    private void setDataStyle11(BaseViewHolder holder, final AdsBean itemBean) {
        RecyclerView mRecyclerView = holder.getView(R.id.id_shs8_list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        TuanGouStyleAdapter2 mTuanGouStyleAdapter2 = new TuanGouStyleAdapter2(mContext, mActivity, true, R.layout.adapter_shop_home_style_11_item);
        mRecyclerView.setAdapter(mTuanGouStyleAdapter2);
        mTuanGouStyleAdapter2.addList(itemBean.getImg());
        // item点击事件
        mTuanGouStyleAdapter2.setOnItemClickListener(new OnItemClickListener<Goods>() {
            @Override
            public void onClick(int position, Goods item) {
                // 跳转到商品详情
                mActivity.startActivity(GoodsDetailActivity.newIntent(mContext, item.getGoods_id()));
            }
        });
    }

    /**
     * 样式十二
     */
    private void setDataStyle12(BaseViewHolder holder, final AdsBean itemBean) {
        RecyclerView mRecyclerView = holder.getView(R.id.id_shs8_list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        TuanGouStyleAdapter3 mTuanGouStyleAdapter3 = new TuanGouStyleAdapter3(mContext, mActivity, R.layout.adapter_shop_home_style_6_item);
        mRecyclerView.setAdapter(mTuanGouStyleAdapter3);
        mTuanGouStyleAdapter3.addList(itemBean.getImg());
        // item点击事件
        mTuanGouStyleAdapter3.setOnItemClickListener(new OnItemClickListener<Goods>() {
            @Override
            public void onClick(int position, Goods item) {
                // 跳转到商品详情
                mActivity.startActivity(GoodsDetailActivity.newIntent(mContext, item.getGoods_id()));
            }
        });

    }


    /**
     * 设置每个样式的标题和图片
     */
    private void setTitleAndImage(BaseViewHolder holder, AdsBean itemBean, int type) {
        RelativeLayout mTitleLayout = holder.getView(R.id.id_title_layout);
        ImageView mTitleImageView = holder.getView(R.id.id_sht_image);// 左边图片
        TextView mTitlteTextView = holder.getView(R.id.id_sht_title);// 标题

        // 标题小图片
        String simg = itemBean.getSimg();
        if (!TextUtils.isEmpty(simg)) {
            PicassoUtils.setImageSmall(mContext, simg, mTitleImageView);
        }

        switch (type) {
            case 1:
            case 2:
            case 3:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
                mTitlteTextView.setVisibility(View.GONE);
                mTitleLayout.setVisibility(TextUtils.isEmpty(simg) ? View.GONE : View.VISIBLE);
                break;
            case 5:
            case 6:
                mTitlteTextView.setVisibility(View.VISIBLE);
                mTitleLayout.setVisibility(View.VISIBLE);
                // 标题名
                String title = itemBean.getTitle();
                if (!TextUtils.isEmpty(title)) {
                    mTitlteTextView.setText(title);
                }
                break;
        }


    }

    private void setImageAndIntent(ImageView imageView, Goods imgBean, int height) {
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.height = height * 2;
        PicassoUtils.setImageBig(mContext, imgBean.getSimg(), imageView);
        imageView.setOnClickListener(v ->{
            if (BaseApplication.getInstance() instanceof ShopLibraryInterface) {
                // 让主项目处理
                ((ShopLibraryInterface) BaseApplication.getInstance()).jumpToActivityByType(mActivity, imgBean.getType(), imgBean.getTo());
            }
        });

    }

    /**
     * 跳转到商品详情
     */
    private void jumpToGoodDetail(View view, String id) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BaseApplication.getInstance().getLoginUserDoLogin(mActivity) != null) {
                    mActivity.startActivity(GoodsDetailActivity.newIntent(mContext, id));
                }
            }
        });
    }
}


