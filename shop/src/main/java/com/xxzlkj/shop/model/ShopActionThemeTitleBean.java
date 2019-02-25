package com.xxzlkj.shop.model;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/5/24 17:59
 */
public class ShopActionThemeTitleBean {
    private String groupTitle;
    private boolean isClose;

    public ShopActionThemeTitleBean(String groupTitle, boolean isClose) {
        this.groupTitle = groupTitle;
        this.isClose = isClose;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean close) {
        isClose = close;
    }
}
