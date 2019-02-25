package com.xxzlkj.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.model.Goods;
import com.xxzlkj.shop.model.TimeBean;
import com.xxzlkj.shop.utils.TextPriceUtil;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.net.OnBaseRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.TimeUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 *  by Administrator on 2017/6/26.
 */

public class YuShouStyleAdapter2 extends BaseAdapter<Goods> {

    //用于退出activity,避免countdown，造成资源浪费。
    private SparseArray<CountDownTimer> mCountDownTimerSparseArray;
    private long startTime = 0;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            startTime += 100;
        }
    };
    private Activity activity;

    public YuShouStyleAdapter2(Context context, Activity activity, int itemId) {
        super(context, itemId);
        this.activity = activity;
        startTime();
        mCountDownTimerSparseArray = new SparseArray<>();
    }

    @Override
    public void convert(final BaseViewHolder holder, int position, final Goods itemBean) {
        ImageView mFireImageView = holder.getView(R.id.id_fire_image);// 图片
        TextView mFireTitle = holder.getView(R.id.id_fire_title);// 标题
        TextView mFireGuiGe = holder.getView(R.id.id_fire_guige);// 规格
        mFireGuiGe.setVisibility(View.GONE);// 规格隐藏
        TextView mPrice = holder.getView(R.id.id_fire_price);// 现价
        TextView mPrices = holder.getView(R.id.id_fire_prices);// 原价
        View mLine = holder.getView(R.id.id_fli_line);// 分割线
        final TextView mFireHour = holder.getView(R.id.id_fire_hour);// 时
        final TextView mFireMinute = holder.getView(R.id.id_fire_minute);// 分
        final TextView mFireSenconds = holder.getView(R.id.id_fire_senconds);// 秒
        final TextView mFireMillisencond = holder.getView(R.id.id_fire_millisencond);// 毫秒
        // 价格
        String price = itemBean.getPrice();
        String prices = itemBean.getPrices();
        mPrice.setText("￥" + price);
        TextPriceUtil.setTextPrices(price,prices,mPrices);

        // 控制分割线的显示和隐藏
        if (position == getItemCount() - 1){
            // 隐藏
            mLine.setVisibility(View.GONE);
        }else {
            // 显示
            mLine.setVisibility(View.VISIBLE);
        }

        if (startTime == 0){
            startTime = System.currentTimeMillis();
            LogUtil.e("---------"+ DateUtils.getYearMonthDayHourMinuteSeconds(NumberFormatUtils.toLong(itemBean.getStoptime()) * 1000)+"="+DateUtils.getYearMonthDayHourMinuteSeconds(startTime));
        }
        long time = NumberFormatUtils.toLong(itemBean.getStoptime()) * 1000 - startTime;

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

        PicassoUtils.setImageBig(mContext,itemBean.getSimg(),mFireImageView);
        // 标题
        mFireTitle.setText(itemBean.getTitle());


    }

    /**
     * 清空资源
     */
    public void cancelAllTimers() {
        if (mCountDownTimerSparseArray == null) {
            return;
        }
        Log.e("TAG",  "size :  " + mCountDownTimerSparseArray.size());
        for (int i = 0,length = mCountDownTimerSparseArray.size(); i < length; i++) {
            CountDownTimer mCountDownTimer = mCountDownTimerSparseArray.get(mCountDownTimerSparseArray.keyAt(i));
            if (mCountDownTimer != null) {
                mCountDownTimer.cancel();
            }
        }
    }

    /**
     * 获取网络时间戳
     */
    private void startTime(){
        Map<String,String> map = new HashMap<>();
        RequestManager.createRequest(URLConstants.REQUEST_GET_TIME, map, new OnBaseRequestListener<TimeBean>() {
            @Override
            public void handlerSuccess(TimeBean bean) {
                startTime = bean.getData().getTime() * 1000;
                LogUtil.e("---------"+ DateUtils.getYearMonthDayHourMinuteSeconds(startTime));
                startTimer();
            }

            @Override
            public void handlerError(int errorCode, String errorMessage) {
                startTime = System.currentTimeMillis();
                startTimer();
            }
        });
    }

    private void startTimer(){
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessage(message);
            }
        };
        mTimer.schedule(mTimerTask,100,100);
    }

    public void cancelTimer(){
        if (mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null){
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if (mHandler != null){
            mHandler = null;
        }
    }
}
