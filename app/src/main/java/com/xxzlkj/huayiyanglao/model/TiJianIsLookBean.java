package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

public class TiJianIsLookBean extends BaseBean {
    private DataBean data = new DataBean();

    public static class DataBean {
        private String state;//1未查看 2已查看

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public DataBean getData() {
        return data;
    }
}
