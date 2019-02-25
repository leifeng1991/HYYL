package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;


public class AddGrouponItemBean extends BaseBean {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private String groupon_team_id;

        public String getGroupon_team_id() {
            return groupon_team_id;
        }

        public void setGroupon_team_id(String groupon_team_id) {
            this.groupon_team_id = groupon_team_id;
        }
    }
}
