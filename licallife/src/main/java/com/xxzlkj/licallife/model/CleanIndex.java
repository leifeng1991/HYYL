package com.xxzlkj.licallife.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 保洁首页上
 * Created by Administrator on 2017/4/20.
 */

public class CleanIndex extends BaseBean{

    /**
     * data : {"banner":[{"simg":"http://zhaolin-10029121.image.myqcloud.com/sample1491811130526333","type":"2","to":"1"},{"simg":"http://zhaolin-10029121.image.myqcloud.com/sample1491811220925613","type":"2","to":"1"},{"simg":"http://zhaolin-10029121.image.myqcloud.com/sample1491811157265076","type":"2","to":"1"},{"simg":"http://zhaolin-10029121.image.myqcloud.com/sample1491811107339442","type":"2","to":"1"},{"simg":"http://zhaolin-10029121.image.myqcloud.com/sample1490159820437951","type":"2","to":"1"}],"group":[{"id":"1","title":"热门服务","pid":"0","simg":"http://zhaolin-10029121.image.myqcloud.com/sample149259024380656","bg_simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492590212814226","ord":"99","state":"2","addtime":"1488792467"},{"id":"2","title":"日常保洁","pid":"0","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492590281455225","bg_simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492590282830874","ord":"99","state":"2","addtime":"1488792467"},{"id":"3","title":"深度保洁","pid":"0","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492590333365772","bg_simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492590334999155","ord":"99","state":"2","addtime":"1488792467"},{"id":"4","title":"家电清洗","pid":"0","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492590419360264","bg_simg":"http://zhaolin-10029121.image.myqcloud.com/sample149259041936199","ord":"99","state":"2","addtime":"1488792467"},{"id":"5","title":"衣物洗护","pid":"0","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492590438701057","bg_simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492590438563303","ord":"99","state":"2","addtime":"1488792467"}]}
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
        private List<GroupBean> group = new ArrayList<>();

        public List<BannerBean> getBanner() {
            return banner;
        }

        public void setBanner(List<BannerBean> banner) {
            this.banner = banner;
        }

        public List<GroupBean> getGroup() {
            return group;
        }

        public void setGroup(List<GroupBean> group) {
            this.group = group;
        }

        public static class BannerBean {
            /**
             * simg : http://zhaolin-10029121.image.myqcloud.com/sample1491811130526333
             * type : 2
             * to : 1
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

        public static class GroupBean {
            /**
             * id : 1
             * title : 热门服务
             * pid : 0
             * simg : http://zhaolin-10029121.image.myqcloud.com/sample149259024380656
             * bg_simg : http://zhaolin-10029121.image.myqcloud.com/sample1492590212814226
             * ord : 99
             * state : 2
             * addtime : 1488792467
             */

            private String id;
            private String title;
            private String pid;
            private String simg;
            private String bg_simg;
            private String ord;
            private String state;
            private String addtime;

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

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }

            public String getSimg() {
                return simg;
            }

            public void setSimg(String simg) {
                this.simg = simg;
            }

            public String getBg_simg() {
                return bg_simg;
            }

            public void setBg_simg(String bg_simg) {
                this.bg_simg = bg_simg;
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
        }
    }
}
