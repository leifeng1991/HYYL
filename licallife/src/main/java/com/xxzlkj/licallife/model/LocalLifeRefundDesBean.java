package com.xxzlkj.licallife.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/3/31 15:36
 */
public class LocalLifeRefundDesBean extends BaseBean {


    /**
     * data : {"addtime":"1495780818","desc":"","id":"156484733","price":"0.02","shop_title":"本地生活店铺1","state":"1","statetime":"0","status":"1","title":"买错了"}
     */

    private DataBean data=new DataBean();

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * addtime : 1495780818
         * desc :
         * id : 156484733
         * price : 0.02
         * shop_title : 本地生活店铺1
         * state : 1
         * statetime : 0
         * status : 1
         * title : 买错了
         */

        private String addtime;
        private String desc;
        private String id;
        private String price;
        private String shop_title;
        private String state;
        private String statetime;
        private String status;
        private String title;
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getShop_title() {
            return shop_title;
        }

        public void setShop_title(String shop_title) {
            this.shop_title = shop_title;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getStatetime() {
            return statetime;
        }

        public void setStatetime(String statetime) {
            this.statetime = statetime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
