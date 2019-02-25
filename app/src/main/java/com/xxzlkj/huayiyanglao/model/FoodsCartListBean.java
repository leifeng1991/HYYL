package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.List;

/**
 * 描述:
 *
 * @author leifeng
 *         2017/11/28 13:51
 */


public class FoodsCartListBean extends BaseBean{

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * starttime : 1511820000
         * stoptime : 1511829000
         * list : [{"id":"2711","pid":"9507236","starttime":"1511820000","stoptime":"1511829000","num":"10","price":"8.00","title":"八宝粥","ads":"香甜八宝粥","sale_num":"100","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1495608221311178"},{"id":"2710","pid":"9507237","starttime":"1511820000","stoptime":"1511829000","num":"8","price":"36.00","title":"鸡肉沙拉+寿司+果汁","ads":"鸡肉沙拉+寿司+果汁，营养快餐一波鲜","sale_num":"100","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1495678303220937"}]
         */

        private String starttime;
        private String stoptime;
        private List<ListBean> list;

        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public String getStoptime() {
            return stoptime;
        }

        public void setStoptime(String stoptime) {
            this.stoptime = stoptime;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 2711
             * pid : 9507236
             * starttime : 1511820000
             * stoptime : 1511829000
             * num : 10
             * price : 8.00
             * title : 八宝粥
             * ads : 香甜八宝粥
             * sale_num : 100
             * simg : http://zhaolin-10029121.image.myqcloud.com/sample1495608221311178
             */

            private String id;
            private String pid;
            private String starttime;
            private String stoptime;
            private String num;
            private String price;
            private String title;
            private String ads;
            private String sale_num;
            private String simg;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }

            public String getStarttime() {
                return starttime;
            }

            public void setStarttime(String starttime) {
                this.starttime = starttime;
            }

            public String getStoptime() {
                return stoptime;
            }

            public void setStoptime(String stoptime) {
                this.stoptime = stoptime;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getAds() {
                return ads;
            }

            public void setAds(String ads) {
                this.ads = ads;
            }

            public String getSale_num() {
                return sale_num;
            }

            public void setSale_num(String sale_num) {
                this.sale_num = sale_num;
            }

            public String getSimg() {
                return simg;
            }

            public void setSimg(String simg) {
                this.simg = simg;
            }
        }
    }
}
