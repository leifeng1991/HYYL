package com.xxzlkj.shop.weight;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xxzlkj.shop.R;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 轮播控件
 * Created by Administrator on 2017/3/13.
 */

public class BannerView extends FrameLayout implements ViewPager.OnPageChangeListener {

    private Context mContext;
    private ViewPager mViewPager;
    private LinearLayout mDotLl;
    private List<String> mUrlList;
    private String mLabellingUrl;// 标签的地址

    private List<ImageView> dotList = null;
    private MyAdapter mAdapter = null;
    private Handler mHandler = null;
    private AutoRollRunnable mAutoRollRunnable = null;

    private int prePosition = 0;

    private HeaderViewClickListener headerViewClickListener;

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        initView();
        initData();
    }

    //初始化view
    private void initView() {
        View.inflate(mContext, R.layout.view_hr_banner_layout, this);
        mViewPager = (ViewPager) findViewById(R.id.id_hbl_viewpager);
        mDotLl = (LinearLayout) findViewById(R.id.id_hbl_dot);
        ViewGroup.LayoutParams vParams = mViewPager.getLayoutParams();
        vParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        mViewPager.setLayoutParams(vParams);
    }

    //初始化数据
    private void initData() {
        dotList = new ArrayList<>();
        mAutoRollRunnable = new AutoRollRunnable();
        mHandler = new Handler();
        mAdapter = new MyAdapter();
    }

    private void initListener() {
        mViewPager.addOnPageChangeListener(this);
    }

    /**
     * 设置图片宽高
     *
     * @param width  bannder宽
     * @param height bannder高
     */
    public void setWidthAndHeight(int width, int height) {
        this.getLayoutParams().width = width;
        this.getLayoutParams().height = height;
    }

    /**
     * 设置指示器的位置
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setPointPadding(int left, int top, int right, int bottom) {
        if (mDotLl != null) {
            mDotLl.setPadding(left, top, right, bottom);
        }
    }


    /**
     * 设置标签，始终做成多个标签为一张图片的模式;
     */
    public void setImgUrlData(String labellingUrl, List<String> urlList) {
        this.mLabellingUrl = labellingUrl;
        setImgUrlData(urlList);
    }

    /**
     * 设置数据
     *
     * @param urlList
     */
    public void setImgUrlData(List<String> urlList) {
        this.mUrlList = urlList;
        if (mUrlList != null && !mUrlList.isEmpty() && mUrlList.size() > 1) {
            //清空数据
            dotList.clear();
            mDotLl.removeAllViews();
            ImageView dotIv;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(PixelUtil.dip2px(mContext, 8), PixelUtil.dip2px(mContext, 8));
            for (int i = 0; i < mUrlList.size(); i++) {
                dotIv = new ImageView(mContext);
                if (i == 0) {
                    dotIv.setBackgroundResource(R.mipmap.ic_page_indicator_focused);
                } else {
                    dotIv.setBackgroundResource(R.mipmap.ic_page_indicator);
                }
                //设置点的间距
                params.setMargins(0, 0, PixelUtil.dip2px(mContext, 5), 0);
                dotIv.setLayoutParams(params);

                //添加点到view上
                mDotLl.addView(dotIv);
                //添加到集合中, 以便控制其切换
                dotList.add(dotIv);
            }
        }

        mAdapter = new MyAdapter();
        mViewPager.setAdapter(mAdapter);

        if (mUrlList != null && !mUrlList.isEmpty() && mUrlList.size() > 1) {
            initListener();
            //设置viewpager初始位置
            mViewPager.setCurrentItem(urlList.size() * 1000);
        }

        //单张图片不轮播
        startRoll();
    }


    /**
     * 设置点击事件
     *
     * @param headerViewClickListener
     */
    public void setOnHeaderViewClickListener(HeaderViewClickListener headerViewClickListener) {
        this.headerViewClickListener = headerViewClickListener;
    }


    /**
     * 开始轮播
     */
    public void startRoll() {
        if (mUrlList != null && mUrlList.size() > 1) {
            mAutoRollRunnable.start();
        }
    }

    /**
     * 停止轮播
     */
    public void stopRoll() {
        if (mUrlList != null && mUrlList.size() > 1) {
            mAutoRollRunnable.stop();
        }
    }

    private class AutoRollRunnable implements Runnable {

        //是否在轮播的标志
        boolean isRunning = false;

        /**
         * 开始轮播
         */
        public void start() {
            if (!isRunning) {
                isRunning = true;
                mHandler.removeCallbacks(this);
                mHandler.postDelayed(this, 3000);
            }
        }

        /**
         * 停止轮播
         */
        private void stop() {
            if (isRunning) {
                mHandler.removeCallbacks(this);
                isRunning = false;
            }
        }

        @Override
        public void run() {
            if (isRunning) {
                if (mUrlList != null && mUrlList.size() > 1) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                }
                mHandler.postDelayed(this, 3000);
            }
        }
    }

    /**
     * 图片点击事件
     */
    public interface HeaderViewClickListener {
        void HeaderViewClick(int position);
    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            //不能滑动
            if (mUrlList == null) {
                return 0;
            } else {
                if (mUrlList.size() > 1) {
                    //无限滑动
                    return Integer.MAX_VALUE;
                } else {
                    //不能滑动
                    return mUrlList.size();
                }
            }

        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View rootView = View.inflate(container.getContext(), R.layout.item_banner_view, null);
            ImageView iv = (ImageView) rootView.findViewById(R.id.iv_image);
            ImageView iv_image_labelling = (ImageView) rootView.findViewById(R.id.iv_image_labelling);

            // 轮播图小于等于1时不能手动滑动
            if (mUrlList != null && !mUrlList.isEmpty() && mUrlList.size() > 0) {
                iv.setOnTouchListener(new OnTouchListener() {
                    private int downX = 0;
                    private long downTime = 0;

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                stopRoll();
                                //获取按下的x坐标
                                downX = (int) v.getX();
                                downTime = System.currentTimeMillis();
                                break;
                            case MotionEvent.ACTION_UP:
                                startRoll();
                                int moveX = (int) v.getX();
                                long moveTime = System.currentTimeMillis();
                                if (downX == moveX && (moveTime - downTime < 500)) {//点击的条件
                                    //轮播图回调点击事件
                                    if (headerViewClickListener != null)
                                        headerViewClickListener.HeaderViewClick(position % mUrlList.size());
                                }
                                break;
                            case MotionEvent.ACTION_CANCEL:
                                startRoll();
                                break;
                        }
                        return true;
                    }
                });
            }

            //加载图片
            if (mUrlList != null && mUrlList.size() > 0) {
                iv.setVisibility(VISIBLE);
                PicassoUtils.setImageBig(mContext, mUrlList.get(position % mUrlList.size()), iv);
            } else {
                iv.setVisibility(INVISIBLE);
            }
            //加载标签
            if (mLabellingUrl != null && mLabellingUrl.length() > 0 && position % mUrlList.size() == 0) {
                // 只有有标签，并且是第一张图片，才会显示标签
                iv_image_labelling.setVisibility(VISIBLE);
                PicassoUtils.setImageBig(mContext, mLabellingUrl, iv_image_labelling);
            } else {
                iv_image_labelling.setVisibility(INVISIBLE);
            }
            // 增加布局
            container.addView(rootView);

            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (dotList.size() > prePosition)
            dotList.get(prePosition).setBackgroundResource(R.mipmap.ic_page_indicator);
        if (dotList.size() > position % dotList.size())
            dotList.get(position % dotList.size()).setBackgroundResource(R.mipmap.ic_page_indicator_focused);
        prePosition = position % dotList.size();
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    /**
     * View销毁时停止轮播
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopRoll();
    }

    /**
     * View创建时开始轮播
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startRoll();
    }
}
