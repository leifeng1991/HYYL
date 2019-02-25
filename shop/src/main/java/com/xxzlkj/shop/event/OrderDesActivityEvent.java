package com.xxzlkj.shop.event;


import com.xxzlkj.shop.model.OrderListBean;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/3/31 15:58
 */
public class OrderDesActivityEvent {
    private int state;
    private OrderListBean.DataBean bean;

    public OrderDesActivityEvent(int state, OrderListBean.DataBean bean) {
        this.state = state;
        this.bean = bean;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public OrderListBean.DataBean getBean() {
        return bean;
    }

    public void setBean(OrderListBean.DataBean bean) {
        this.bean = bean;
    }
}
