package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

/**
 * 当前时间的bean
 *
 * @author zhangrq
 */
public class CurrentTimeBean extends BaseBean {

    private DataBean data = new DataBean();

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String time;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

}
