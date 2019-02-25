package com.xxzlkj.licallife.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

public class NannyAndMaternityMatronDesTabBean extends BaseBean {
    private String leftTitle;
    private String leftValue;
    private String rightTitle;
    private String rightValue;

    public NannyAndMaternityMatronDesTabBean(String leftTitle, String leftValue, String rightTitle, String rightValue) {
        this.leftTitle = leftTitle;
        this.leftValue = leftValue;
        this.rightTitle = rightTitle;
        this.rightValue = rightValue;
    }

    public String getLeftTitle() {
        return leftTitle;
    }

    public void setLeftTitle(String leftTitle) {
        this.leftTitle = leftTitle;
    }

    public String getLeftValue() {
        return leftValue;
    }

    public void setLeftValue(String leftValue) {
        this.leftValue = leftValue;
    }

    public String getRightTitle() {
        return rightTitle;
    }

    public void setRightTitle(String rightTitle) {
        this.rightTitle = rightTitle;
    }

    public String getRightValue() {
        return rightValue;
    }

    public void setRightValue(String rightValue) {
        this.rightValue = rightValue;
    }
}
