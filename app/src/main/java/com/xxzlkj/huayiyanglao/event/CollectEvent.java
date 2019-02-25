package com.xxzlkj.huayiyanglao.event;

/**
 * 收藏
 * Created by Administrator on 2017/4/19.
 */

public class CollectEvent {
    private boolean isCheckboxVisible;
    private boolean isAllCheckbox;
    private boolean isDelete;
    private boolean isClear;
    private String isVisibleFlag;
    private String isAllFlag;

    public CollectEvent(boolean isCheckboxVisible, boolean isAllCheckbox, boolean isDelete, String isVisibleFlag, String isAllFlag) {
        this.isCheckboxVisible = isCheckboxVisible;
        this.isAllCheckbox = isAllCheckbox;
        this.isDelete = isDelete;
        this.isVisibleFlag = isVisibleFlag;
        this.isAllFlag = isAllFlag;
    }

    public CollectEvent(boolean isClear){
        this.isClear = isClear;
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

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
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

    public boolean isClear() {
        return isClear;
    }

    public void setClear(boolean clear) {
        isClear = clear;
    }
}
