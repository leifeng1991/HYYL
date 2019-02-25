package com.xxzlkj.shop.event;


public class DataChangEvent {
    private int count;
    private boolean isLogin;

    public DataChangEvent(int count, boolean isLogin) {
        this.count = count;
        this.isLogin = isLogin;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
