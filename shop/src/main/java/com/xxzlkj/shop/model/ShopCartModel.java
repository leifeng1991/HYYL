package com.xxzlkj.shop.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/23.
 */

public class ShopCartModel implements Serializable{
    private String name;

    private boolean isChecked;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean checked) {
        isChecked = checked;
    }
}
