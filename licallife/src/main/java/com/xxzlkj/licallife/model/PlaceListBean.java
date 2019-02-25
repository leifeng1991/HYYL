package com.xxzlkj.licallife.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class PlaceListBean extends BaseBean {

    /**
     * data : {"place":[{"title":"不限","id":"97","pid":"0"},{"id":"97","pid":"0","title":"华东地区"},{"id":"100","pid":"0","title":"华北地区"}],"place2":[{"id":"97","pid":"0","place":[{"id":"98","pid":"97","title":"山东1"},{"id":"99","pid":"97","title":"江苏"}],"title":"华东地区"},{"id":"100","pid":"0","title":"华北地区"}]}
     */

    private DataBean data = new DataBean();

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<PlaceBean> place = new ArrayList<>();
        private List<Place2Bean> place2 = new ArrayList<>();

        public List<PlaceBean> getPlace() {
            return place;
        }

        public void setPlace(List<PlaceBean> place) {
            this.place = place;
        }

        public List<Place2Bean> getPlace2() {
            return place2;
        }

        public void setPlace2(List<Place2Bean> place2) {
            this.place2 = place2;
        }

        public static class PlaceBean {
            /**
             * title : 不限
             * id : 97
             * pid : 0
             */

            private String title;
            private String id;
            private String pid;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

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
        }

        public static class Place2Bean {
            /**
             * id : 97
             * pid : 0
             * place : [{"id":"98","pid":"97","title":"山东1"},{"id":"99","pid":"97","title":"江苏"}]
             * title : 华东地区
             */

            private String id;
            private String pid;
            private String title;
            private List<PlaceBean> place = new ArrayList<>();

            public Place2Bean(String title, List<PlaceBean> place) {
                this.title = title;
                this.place = place;
            }

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

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public List<PlaceBean> getPlace() {
                return place;
            }

            public void setPlace(List<PlaceBean> place) {
                this.place = place;
            }
        }
    }
}
