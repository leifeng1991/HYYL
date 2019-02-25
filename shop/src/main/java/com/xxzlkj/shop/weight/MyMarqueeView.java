package com.xxzlkj.shop.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.sunfusheng.marqueeview.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 对三方MarqueeView进行修改后的
 */
public class MyMarqueeView extends ViewFlipper {

    private Context mContext;
    private List<? extends CharSequence> notices;
    private boolean isSetAnimDuration = false;
    private OnItemClickListener onItemClickListener;
    private OnItemChangeListener onItemChangeListener;

    private int interval = 2000;
    private int animDuration = 500;
    private int textSize = 14;
    private int textColor = 0xffffffff;

    private boolean singleLine = false;
    private int gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
    private static final int TEXT_GRAVITY_LEFT = 0, TEXT_GRAVITY_CENTER = 1, TEXT_GRAVITY_RIGHT = 2;
    private List<Integer> imageIds;

    public MyMarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        if (notices == null) {
            notices = new ArrayList<>();
        }

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, com.sunfusheng.marqueeview.R.styleable.MarqueeViewStyle, defStyleAttr, 0);
        interval = typedArray.getInteger(com.sunfusheng.marqueeview.R.styleable.MarqueeViewStyle_mvInterval, interval);
        isSetAnimDuration = typedArray.hasValue(com.sunfusheng.marqueeview.R.styleable.MarqueeViewStyle_mvAnimDuration);
        singleLine = typedArray.getBoolean(com.sunfusheng.marqueeview.R.styleable.MarqueeViewStyle_mvSingleLine, false);
        animDuration = typedArray.getInteger(com.sunfusheng.marqueeview.R.styleable.MarqueeViewStyle_mvAnimDuration, animDuration);
        if (typedArray.hasValue(com.sunfusheng.marqueeview.R.styleable.MarqueeViewStyle_mvTextSize)) {
            textSize = (int) typedArray.getDimension(com.sunfusheng.marqueeview.R.styleable.MarqueeViewStyle_mvTextSize, textSize);
            textSize = DisplayUtil.px2sp(mContext, textSize);
        }
        textColor = typedArray.getColor(com.sunfusheng.marqueeview.R.styleable.MarqueeViewStyle_mvTextColor, textColor);
        int gravityType = typedArray.getInt(com.sunfusheng.marqueeview.R.styleable.MarqueeViewStyle_mvGravity, TEXT_GRAVITY_LEFT);
        switch (gravityType) {
            case TEXT_GRAVITY_CENTER:
                gravity = Gravity.CENTER;
                break;
            case TEXT_GRAVITY_RIGHT:
                gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                break;
        }
        typedArray.recycle();

        setFlipInterval(interval);
    }

    // 根据公告字符串启动轮播
    public void startWithText(final String notice) {
        if (TextUtils.isEmpty(notice)) return;
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                startWithFixedWidth(notice, getWidth());
            }
        });
    }

    // 根据公告字符串列表启动轮播
    public void startWithList(List<? extends CharSequence> notices) {
        setNotices(notices);
        start();
    }

    // 根据宽度和公告字符串启动轮播
    private void startWithFixedWidth(String notice, int width) {
        int noticeLength = notice.length();
        int dpW = DisplayUtil.px2dip(mContext, width);
        int limit = dpW / textSize;
        if (dpW == 0) {
            throw new RuntimeException("Please set MarqueeView width !");
        }
        List list = new ArrayList();
        if (noticeLength <= limit) {
            list.add(notice);
        } else {
            int size = noticeLength / limit + (noticeLength % limit != 0 ? 1 : 0);
            for (int i = 0; i < size; i++) {
                int startIndex = i * limit;
                int endIndex = ((i + 1) * limit >= noticeLength ? noticeLength : (i + 1) * limit);
                list.add(notice.substring(startIndex, endIndex));
            }
        }
        notices.addAll(list);
        start();
    }

    // 启动轮播
    public boolean start() {
        if (notices == null || notices.size() == 0) return false;
        removeAllViews();
        resetAnimation();

        for (int i = 0; i < notices.size(); i++) {
            final TextView textView = createTextView(notices.get(i), i);
            final int finalI = i;
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(finalI, textView);
                    }
                }
            });
            addView(textView);
        }

        if (notices.size() > 1) {
            startFlipping();
        } else {
            stopFlipping();
        }
        return true;
    }

    @Override
    public void showNext() {
        if (onItemChangeListener != null) {
            onItemChangeListener.onItemChanged(getDisplayedChild() + 1);
        }
        super.showNext();
    }

    private void resetAnimation() {
        clearAnimation();

        Animation animIn = AnimationUtils.loadAnimation(mContext, com.sunfusheng.marqueeview.R.anim.anim_marquee_in);
        if (isSetAnimDuration) animIn.setDuration(animDuration);
        setInAnimation(animIn);

        Animation animOut = AnimationUtils.loadAnimation(mContext, com.sunfusheng.marqueeview.R.anim.anim_marquee_out);
        if (isSetAnimDuration) animOut.setDuration(animDuration);
        setOutAnimation(animOut);
    }

    // 创建ViewFlipper下的TextView
    private TextView createTextView(CharSequence text, int position) {
        TextView tv = new TextView(mContext);
        tv.setGravity(gravity);
        tv.setText(text);
        tv.setTextColor(textColor);
        tv.setTextSize(textSize);
        tv.setSingleLine(singleLine);
        tv.setTag(position);

        tv.setCompoundDrawablePadding(DisplayUtil.dip2px(mContext, 7));
        tv.setPadding(DisplayUtil.dip2px(mContext, 2), DisplayUtil.dip2px(mContext, 2), DisplayUtil.dip2px(mContext, 7), DisplayUtil.dip2px(mContext, 2));
        if (imageIds != null && imageIds.size() > position) {
            Drawable left = mContext.getResources().getDrawable(imageIds.get(position));
            left.setBounds(0, 0, left.getIntrinsicWidth(), left.getIntrinsicHeight());
            tv.setCompoundDrawables(left, null, null, null);
        }
        return tv;
    }

    public int getPosition() {
        return (int) getCurrentView().getTag();
    }

    public List<? extends CharSequence> getNotices() {
        return notices;
    }

    public void setNotices(List<? extends CharSequence> notices) {
        this.notices = notices;
    }

    public void setDrawableIds(List<Integer> imageIds) {
        this.imageIds = imageIds;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemChangeListener(OnItemChangeListener onItemChangeListener) {
        this.onItemChangeListener = onItemChangeListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, TextView textView);
    }

    public interface OnItemChangeListener {
        void onItemChanged(int position);
    }

}
