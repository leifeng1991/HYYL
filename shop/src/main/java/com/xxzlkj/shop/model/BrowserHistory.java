package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 浏览记录
 * Created by Administrator on 2017/4/18.
 */

public class BrowserHistory extends BaseBean {


    private List<DataBean> data = new ArrayList<>();

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * time : 2017-04-18
         * list : [{"id":"325","addtime":"1492511163","time":"2017-04-18","goods_id":"9503972","title":"卡夫奥利奥饼干奥利奥 轻甜","price":"5.50","prices":"5.50","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1491817763518417","ads":"卡夫奥利奥饼干"},{"id":"324","addtime":"1492511153","time":"2017-04-18","goods_id":"9503962","title":"可口可乐","price":"2.80","prices":"2.80","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1491816593779719","ads":"可口可乐"},{"id":"294","addtime":"1492511123","time":"2017-04-18","goods_id":"9505108","title":"预售规则","price":"0.00","prices":"0.00","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492398082628832","ads":"预售规则"},{"id":"322","addtime":"1492507168","time":"2017-04-18","goods_id":"9505353","title":"杏鲍菇","price":"10.91","prices":"10.91","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492505996214731","ads":"杏鲍菇"},{"id":"321","addtime":"1492507144","time":"2017-04-18","goods_id":"9505357","title":"绿化玖桃","price":"8.00","prices":"8.00","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492506469867705","ads":"绿化玖桃"},{"id":"320","addtime":"1492507130","time":"2017-04-18","goods_id":"9505359","title":"柠檬","price":"2.50","prices":"2.50","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492506537708524","ads":"柠檬"},{"id":"319","addtime":"1492507121","time":"2017-04-18","goods_id":"9505365","title":"秋香苹果","price":"17.84","prices":"17.84","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492506992352838","ads":"秋香苹果"},{"id":"295","addtime":"1492490900","time":"2017-04-18","goods_id":"9504812","title":"200g厨小丫绿豆宽粉","price":"4.95","prices":"4.95","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492077915143718","ads":"200g厨小丫绿豆宽粉"}]
         */

        private String time;
        private List<ListBean> list;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 325
             * addtime : 1492511163
             * time : 2017-04-18
             * goods_id : 9503972
             * title : 卡夫奥利奥饼干奥利奥 轻甜
             * price : 5.50
             * prices : 5.50
             * simg : http://zhaolin-10029121.image.myqcloud.com/sample1491817763518417
             * ads : 卡夫奥利奥饼干
             *
             */

            private String id;
            private String addtime;
            private String time;
            private String goods_id;
            private String title;
            private String price;
            private String prices;
            private String simg;
            private String ads;
            private String  activity_icon_img;
            private boolean isChecked;

            public String getActivity_icon_img() {
                return activity_icon_img;
            }

            public void setActivity_icon_img(String activity_icon_img) {
                this.activity_icon_img = activity_icon_img;
            }

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

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
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

            public String getPrices() {
                return prices;
            }

            public void setPrices(String prices) {
                this.prices = prices;
            }

            public String getSimg() {
                return simg;
            }

            public void setSimg(String simg) {
                this.simg = simg;
            }

            public String getAds() {
                return ads;
            }

            public void setAds(String ads) {
                this.ads = ads;
            }

            public boolean isChecked() {
                return isChecked;
            }

            public void setIsChecked(boolean checked) {
                isChecked = checked;
            }
        }
    }
}
