package com.xxzlkj.huayiyanglao.event;


import com.xxzlkj.huayiyanglao.model.ClassifyItemItemBean;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/3/24 11:03
 */
public class ClassifyMyAppAddEvent {
    private ClassifyItemItemBean itemItemBean;

    public ClassifyMyAppAddEvent(ClassifyItemItemBean itemItemBean) {

        this.itemItemBean = itemItemBean;
    }

    public ClassifyItemItemBean getItemItemBean() {
        return itemItemBean;
    }

    public void setItemItemBean(ClassifyItemItemBean itemItemBean) {
        this.itemItemBean = itemItemBean;
    }
}
