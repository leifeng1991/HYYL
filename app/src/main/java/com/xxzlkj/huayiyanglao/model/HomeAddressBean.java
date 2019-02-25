package com.xxzlkj.huayiyanglao.model;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/8/22 16:14
 */
public class HomeAddressBean {
    private String title;
    // 1表示成功：提示：（当前服务小区:XXX）；2表示失败：提示：（超出配送范围,已展示XXX服务）；3展示定位地址：4展示定位失败
    private int titleStyle;

    public HomeAddressBean(String title, int titleStyle) {
        this.title = title;
        this.titleStyle = titleStyle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTitleStyle() {
        return titleStyle;
    }

    public void setTitleStyle(int titleStyle) {
        this.titleStyle = titleStyle;
    }
}
