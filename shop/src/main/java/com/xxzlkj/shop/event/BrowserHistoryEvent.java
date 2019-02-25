package com.xxzlkj.shop.event;

/**
 * 浏览记录
 * Created by Administrator on 2017/4/18.
 */

public class BrowserHistoryEvent {
    private boolean isCheckboxVisible;
    private boolean isAllCheckbox;
    private boolean isDelete;
    private String isVisibleFlag;
    private String isAllFlag;

    public BrowserHistoryEvent(boolean isCheckboxVisible, boolean isAllCheckbox, String isVisibleFlag, String isAllFlag, boolean isDelete) {
        this.isCheckboxVisible = isCheckboxVisible;
        this.isAllCheckbox = isAllCheckbox;
        this.isVisibleFlag = isVisibleFlag;
        this.isAllFlag = isAllFlag;
        this.isDelete = isDelete;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public boolean isCheckboxVisible() {
        return isCheckboxVisible;
    }

    public void setCheckboxVisible(boolean checkboxVisible) {
        isCheckboxVisible = checkboxVisible;
    }

    public boolean isAllCheckbox() {
        return isAllCheckbox;
    }

    public void setAllCheckbox(boolean allCheckbox) {
        isAllCheckbox = allCheckbox;
    }

    public String getIsVisibleFlag() {
        return isVisibleFlag;
    }

    public void setIsVisibleFlag(String isVisibleFlag) {
        this.isVisibleFlag = isVisibleFlag;
    }

    public String getIsAllFlag() {
        return isAllFlag;
    }

    public void setIsAllFlag(String isAllFlag) {
        this.isAllFlag = isAllFlag;
    }
}
