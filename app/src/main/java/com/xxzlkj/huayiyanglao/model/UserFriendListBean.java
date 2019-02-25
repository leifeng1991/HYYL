package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/10/9 10:00
 */
public class UserFriendListBean extends BaseBean {

    /**
     * data : {"concern":[{"list":[{"age":"18","birthday":"936806400","id":"111346","prefix":"L","sex":"2","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1493868934023","type":"0","username":"李奇不不不跨步不我额"}],"title":"L"},{"list":[{"age":"1","birthday":"1504713600","id":"111380","prefix":"W","sex":"1","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1504768836176","type":"0","username":"我的名字只有十个字唉"}],"title":"W"}],"concern_num":"2","fans":[{"list":[{"age":"1","birthday":"1504713600","id":"111380","prefix":"W","sex":"1","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1504768836176","type":"0","username":"我的名字只有十个字唉"}],"title":"W"}],"fans_num":"1","friend":[],"friend_num":"0"}
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
         * concern : [{"list":[{"age":"18","birthday":"936806400","id":"111346","prefix":"L","sex":"2","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1493868934023","type":"0","username":"李奇不不不跨步不我额"}],"title":"L"},{"list":[{"age":"1","birthday":"1504713600","id":"111380","prefix":"W","sex":"1","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1504768836176","type":"0","username":"我的名字只有十个字唉"}],"title":"W"}]
         * concern_num : 2
         * fans : [{"list":[{"age":"1","birthday":"1504713600","id":"111380","prefix":"W","sex":"1","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1504768836176","type":"0","username":"我的名字只有十个字唉"}],"title":"W"}]
         * fans_num : 1
         * friend : []
         * friend_num : 0
         */

        private String concern_num;
        private String fans_num;
        private String friend_num;
        private List<GroupBean> concern = new ArrayList<>();
        private List<GroupBean> fans = new ArrayList<>();
        private List<GroupBean> friend = new ArrayList<>();

        public static class GroupBean implements Serializable {

            /**
             * list : [{"age":"18","birthday":"936806400","id":"111346","prefix":"L","sex":"2","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1493868934023","type":"0","username":"李奇不不不跨步不我额"}]
             * title : L
             */

            private String title;
            private ArrayList<ContactItemBean> list = new ArrayList<>();

            public GroupBean(String title) {
                this.title = title;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public ArrayList<ContactItemBean> getList() {
                return list;
            }

            public void setList(ArrayList<ContactItemBean> list) {
                this.list = list;
            }

        }

        public String getConcern_num() {
            return concern_num;
        }

        public void setConcern_num(String concern_num) {
            this.concern_num = concern_num;
        }

        public String getFans_num() {
            return fans_num;
        }

        public void setFans_num(String fans_num) {
            this.fans_num = fans_num;
        }

        public String getFriend_num() {
            return friend_num;
        }

        public void setFriend_num(String friend_num) {
            this.friend_num = friend_num;
        }

        public List<GroupBean> getConcern() {
            return concern;
        }

        public void setConcern(List<GroupBean> concern) {
            this.concern = concern;
        }

        public List<GroupBean> getFans() {
            return fans;
        }

        public void setFans(List<GroupBean> fans) {
            this.fans = fans;
        }

        public List<GroupBean> getFriend() {
            return friend;
        }

        public void setFriend(List<GroupBean> friend) {
            this.friend = friend;
        }
    }
}
