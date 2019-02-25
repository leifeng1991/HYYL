package com.xxzlkj.zhaolinshare.chat;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/10/11 17:14
 */
public class ScrollRecyclerView extends XRecyclerView {
    private boolean move;
    private int mIndex;
    private LinearLayoutManager linearLayoutManagerContent;

    public ScrollRecyclerView(Context context) {
        super(context);
    }

    public ScrollRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        linearLayoutManagerContent = (LinearLayoutManager) layout;
        addOnScrollListener(new RecyclerViewListener());
    }

    /**
     * 处理后的 smoothScrollToPosition()
     */
    @Override
    public void smoothScrollToPosition(int position) {
        mIndex = position;
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = linearLayoutManagerContent.findFirstVisibleItemPosition();
        int lastItem = linearLayoutManagerContent.findLastVisibleItemPosition();
        //然后区分情况
        if (position <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            super.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            int top = getChildAt(position - firstItem).getTop();
            scrollBy(0, top);
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            super.smoothScrollToPosition(position);
            // 之后判断滚动监听里面的 onScrollStateChanged()
            move = true;
        }
    }

    @Override
    public void scrollToPosition(int position) {
        mIndex = position;
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = linearLayoutManagerContent.findFirstVisibleItemPosition();
        int lastItem = linearLayoutManagerContent.findLastVisibleItemPosition();
        //然后区分情况
        if (position <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            super.scrollToPosition(position);
        } else if (position <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            int top = getChildAt(position - firstItem).getTop();
            scrollBy(0, top);
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            super.scrollToPosition(position);
            // 之后判断滚动监听里面的 onScrollStateChanged()
            move = true;
        }
    }

    private class RecyclerViewListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (move && newState == RecyclerView.SCROLL_STATE_IDLE) {
                move = false;
                //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                int n = mIndex - linearLayoutManagerContent.findFirstVisibleItemPosition();
                if (0 <= n && n < getChildCount()) {
                    //获取要置顶的项顶部离RecyclerView顶部的距离
                    int top = getChildAt(n).getTop();
                    //最后的移动
                    scrollBy(0, top);
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //在这里进行第二次滚动（最后的100米！）
            if (move) {
                move = false;
                //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                int n = mIndex - linearLayoutManagerContent.findFirstVisibleItemPosition();
                if (0 <= n && n < getChildCount()) {
                    //获取要置顶的项顶部离RecyclerView顶部的距离
                    int top = getChildAt(n).getTop();
                    //最后的移动
                    scrollBy(0, top);
                }
            }
        }
    }
}
