package com.xxzlkj.shop.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/23.
 */

public class BrowsingHistoryModel implements Serializable{
    private String add_time;

    private boolean isChecked;

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean checked) {
        isChecked = checked;
    }
}
