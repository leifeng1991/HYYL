package com.xxzlkj.licallife.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

public class AuditionSucceedBean extends BaseBean {


    /**
     * data : {"id":"1000000704","orderid":"201704211636493777069","is_refund":"1","uid":"111339","price":"0.02","addtime":"1492763809","endtime":"1492658710","payment":"0","uidtime":"0","state":"1","address_name":"毕珂","address_phone":"18623792320","address_address":"中弘北京像素你麻痹","content":"","is_tui":1,"sendtime":"0","edittime":"0","edit_desc":"","shop_title":"测试家政店铺1","goods":[{"id":"1","title":"测试商品1","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1489211525773340","price":"0.01","prices":"0.00","ads":"","num":"2","goods_id":"1","endtime":"1492658710","shop_title":"测试家政店铺1"}]}
     */

    private DataBean data = new DataBean();

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private String title;
        private String orderid;
        private String price;
        private String id;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
