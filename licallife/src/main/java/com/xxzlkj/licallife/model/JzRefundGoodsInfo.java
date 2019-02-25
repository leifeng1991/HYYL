package com.xxzlkj.licallife.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

/**
 * 售后详情
 * Created by Administrator on 2017/5/11.
 */

public class JzRefundGoodsInfo extends BaseBean{

    /**
     * data : {"id":"156484727","addtime":"1494410533","statetime":"0","state":"1","status":"1","title":"商品有损坏","price":"0.02","desc":"","shop_title":"测试家政店铺2"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 156484727
         * addtime : 1494410533
         * statetime : 0
         * state : 1
         * status : 1
         * title : 商品有损坏
         * price : 0.02
         * desc :
         * shop_title : 测试家政店铺2
         */

        private String id;
        private String addtime;
        private String statetime;
        private String state;
        private String status;
        private String title;
        private String price;
        private String desc;
        private String shop_title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getStatetime() {
            return statetime;
        }

        public void setStatetime(String statetime) {
            this.statetime = statetime;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getShop_title() {
            return shop_title;
        }

        public void setShop_title(String shop_title) {
            this.shop_title = shop_title;
        }
    }
}
