package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;


public class CheckDistanceBean extends BaseBean {

    /**
     * data : {"juli":"80","id":"2222316","title":"店铺2"}
     */

    private DataBean data = new DataBean();

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * juli : 80
         * id : 2222316
         * title : 店铺2
         */

        private String juli;
        private String store_id;
        private String community_id;
        private String title = "";
        // 是否开通本地生活 1：开通 2：未开通
        private String is_cleaning;

        public String getJuli() {
            return juli;
        }

        public void setJuli(String juli) {
            this.juli = juli;
        }

        public String getStore_id() {
            return store_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public String getCommunity_id() {
            return community_id;
        }

        public void setCommunity_id(String community_id) {
            this.community_id = community_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIs_cleaning() {
            return is_cleaning;
        }

        public void setIs_cleaning(String is_cleaning) {
            this.is_cleaning = is_cleaning;
        }
    }
}
