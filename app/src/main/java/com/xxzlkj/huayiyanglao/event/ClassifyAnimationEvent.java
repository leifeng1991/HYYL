package com.xxzlkj.huayiyanglao.event;

import android.view.View;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/3/24 11:03
 */
public class ClassifyAnimationEvent {
    private View clickView;
    private boolean isUpDown;

    public ClassifyAnimationEvent(View clickView, boolean isUpDown) {
        this.clickView = clickView;
        this.isUpDown = isUpDown;
    }

    public boolean isUpDown() {
        return isUpDown;
    }

    public void setUpDown(boolean upDown) {
        isUpDown = upDown;
    }

    public View getClickView() {
        return clickView;
    }

    public void setClickView(View clickView) {
        this.clickView = clickView;
    }
}
