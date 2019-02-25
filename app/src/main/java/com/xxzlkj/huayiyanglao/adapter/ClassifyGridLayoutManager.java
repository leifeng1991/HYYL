package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/4/12 14:55
 */
public class ClassifyGridLayoutManager extends GridLayoutManager {

    public ClassifyGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ClassifyGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public ClassifyGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
