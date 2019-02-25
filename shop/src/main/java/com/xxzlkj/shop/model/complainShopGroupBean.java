package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/4/24 14:22
 */
public class complainShopGroupBean extends BaseBean {

    /**
     * data : {"group":["配送人员问题","产品问题","配送时间问题","其它"]}
     */

    private DataBean data=new DataBean();

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<String> group=new ArrayList<>();

        public List<String> getGroup() {
            return group;
        }

        public void setGroup(List<String> group) {
            this.group = group;
        }
    }
}
