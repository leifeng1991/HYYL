package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/11/15 16:06
 */
public class SpelledGroupDesBean extends BaseBean {

    /**
     * data : {"id":"6","uid":"1113034","num":"5","price":"0.01","number":"4","goods_id":"9508193","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1508920239757293","title":"千禧农柴鸡蛋15枚-袋装","prices":"9.90","ads":"千禧农柴鸡蛋15枚-袋装","starttime":"1510728900","stoptime":"1512024840","state":"2","team_count":"1","team_user":[{"id":"1113034","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1508898405587","is_top":"1"}]}
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
         * id : 6
         * uid : 1113034
         * num : 5
         * price : 0.01
         * number : 4
         * goods_id : 9508193
         * simg : http://zhaolin-10029121.image.myqcloud.com/sample1508920239757293
         * title : 千禧农柴鸡蛋15枚-袋装
         * prices : 9.90
         * ads : 千禧农柴鸡蛋15枚-袋装
         * starttime : 1510728900
         * stoptime : 1512024840
         * state : 2
         * team_count : 1
         * team_user : [{"id":"1113034","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1508898405587","is_top":"1"}]
         */

        private String id;
        private String uid;
        private String num;
        private String price;
        private String number;
        private String goods_id;
        private String simg;
        private String title;
        private String prices;
        private String ads;
        private String starttime;
        private String stoptime;
        private String state;
        private String team_count;
        private List<TeamUserBean> team_user = new ArrayList<>();

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getSimg() {
            return simg;
        }

        public void setSimg(String simg) {
            this.simg = simg;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPrices() {
            return prices;
        }

        public void setPrices(String prices) {
            this.prices = prices;
        }

        public String getAds() {
            return ads;
        }

        public void setAds(String ads) {
            this.ads = ads;
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

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getTeam_count() {
            return team_count;
        }

        public void setTeam_count(String team_count) {
            this.team_count = team_count;
        }

        public List<TeamUserBean> getTeam_user() {
            return team_user;
        }

        public void setTeam_user(List<TeamUserBean> team_user) {
            this.team_user = team_user;
        }

        public static class TeamUserBean {
            /**
             * id : 1113034
             * simg : http://zhaolin-10029121.image.myqcloud.com/sample1508898405587
             * is_top : 1
             */

            private String id;
            private String simg;
            private String is_top;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getSimg() {
                return simg;
            }

            public void setSimg(String simg) {
                this.simg = simg;
            }

            public String getIs_top() {
                return is_top;
            }

            public void setIs_top(String is_top) {
                this.is_top = is_top;
            }
        }
    }
}
