package com.xxzlkj.licallife.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;


public class LocalLifeHomeBean extends BaseBean{


    /**
     * data : {"banner":[{"simg":"http://zhaolin-10029121.image.myqcloud.com/sample150096543611156","type":"0","to":"0"},{"simg":"http://zhaolin-10029121.image.myqcloud.com/sample1500965256506549","type":"0","to":"0"},{"simg":"http://zhaolin-10029121.image.myqcloud.com/sample1500964578800286","type":"0","to":"0"}],"type":[{"id":"1","title":"保洁","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1500965472301644","ord":"99","state":"2","addtime":"1488792467","label":"1,2,2","type":"1","desc":"简介"},{"id":"2","title":"月嫂","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1500965576307030","ord":"99","state":"2","addtime":"1488792467","label":"深度保洁,日常保洁,深度保洁,家电清洗,衣物洗护","type":"2","desc":""},{"id":"3","title":"保姆","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1500966371177104","ord":"99","state":"2","addtime":"1488792467","label":"","type":"3","desc":""},{"id":"4","title":"小时工","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1500966250573945","ord":"99","state":"2","addtime":"1488792467","label":"","type":"4","desc":""}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<BannerBean> banner = new ArrayList<>();
        private List<TypeBean> type = new ArrayList<>();

        public List<BannerBean> getBanner() {
            return banner;
        }

        public void setBanner(List<BannerBean> banner) {
            this.banner = banner;
        }

        public List<TypeBean> getType() {
            return type;
        }

        public void setType(List<TypeBean> type) {
            this.type = type;
        }

        public static class BannerBean {
            /**
             * simg : http://zhaolin-10029121.image.myqcloud.com/sample150096543611156
             * type : 0
             * to : 0
             */

            private String simg;
            private String type;
            private String to;

            public String getSimg() {
                return simg;
            }

            public void setSimg(String simg) {
                this.simg = simg;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getTo() {
                return to;
            }

            public void setTo(String to) {
                this.to = to;
            }
        }

        public static class TypeBean {
            /**
             * id : 1
             * title : 保洁
             * simg : http://zhaolin-10029121.image.myqcloud.com/sample1500965472301644
             * ord : 99
             * state : 2
             * addtime : 1488792467
             * label : 1,2,2
             * type : 1
             * desc : 简介
             */

            private String id;
            private String title;
            private String simg;
            private String ord;
            private String state;
            private String addtime;
            private String label;
            private String type;
            private String desc;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getSimg() {
                return simg;
            }

            public void setSimg(String simg) {
                this.simg = simg;
            }

            public String getOrd() {
                return ord;
            }

            public void setOrd(String ord) {
                this.ord = ord;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }
        }
    }
}
