package com.xxzlkj.huayiyanglao.event;

import com.xxzlkj.huayiyanglao.model.ClassifyItemItemBean;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/3/24 11:03
 */
public class RecentAppChangeEvent {
    private ClassifyItemItemBean itemItemBean;

    public RecentAppChangeEvent(ClassifyItemItemBean itemItemBean) {

        this.itemItemBean = itemItemBean;
    }

    public ClassifyItemItemBean getItemItemBean() {
        return itemItemBean;
    }

    public void setItemItemBean(ClassifyItemItemBean itemItemBean) {
        this.itemItemBean = itemItemBean;
    }
}
