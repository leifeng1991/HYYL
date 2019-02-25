package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class ShopAppBean extends BaseBean {


    private List<DataBean> data = new ArrayList<>();

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * title : 蔬果
         * simg :
         * bgimg :
         * groupid : 447
         * pid : 0
         * group : [{"id":"7","title":"","simg":"","bgimg":"","groupid":"678","pid":"2"},{"id":"8","title":"","simg":"","bgimg":"","groupid":"671","pid":"2"},{"id":"9","title":"","simg":"","bgimg":"","groupid":"662","pid":"2"},{"id":"10","title":"","simg":"","bgimg":"","groupid":"498","pid":"2"},{"id":"11","title":"","simg":"","bgimg":"","groupid":"497","pid":"2"},{"id":"12","title":"","simg":"","bgimg":"","groupid":"496","pid":"2"},{"id":"13","title":"","simg":"","bgimg":"","groupid":"495","pid":"2"},{"id":"14","title":"","simg":"","bgimg":"","groupid":"493","pid":"2"}]
         */

        private String id;
        private String title;
        private String simg;
        private String bgimg;
        private String groupid;
        private String pid;
        private List<GroupBean> group = new ArrayList<>();
        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

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

        public String getBgimg() {
            return bgimg;
        }

        public void setBgimg(String bgimg) {
            this.bgimg = bgimg;
        }

        public String getGroupid() {
            return groupid;
        }

        public void setGroupid(String groupid) {
            this.groupid = groupid;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public List<GroupBean> getGroup() {
            return group;
        }

        public void setGroup(List<GroupBean> group) {
            this.group = group;
        }



        public static class GroupBean {
            /**
             * id : 7
             * title :
             * simg :
             * bgimg :
             * groupid : 678
             * pid : 2
             */

            private String id;
            private String group_title;
            private String simg;
            private String bgimg;
            private String groupid;
            private String pid;
            private boolean isMore;
            private boolean isClose;

            public GroupBean(boolean isMore, boolean isClose) {
                this.isMore = isMore;
                this.isClose = isClose;
            }

            public boolean isMore() {
                return isMore;
            }

            public void setMore(boolean more) {
                isMore = more;
            }

            public boolean isClose() {
                return isClose;
            }

            public void setClose(boolean close) {
                isClose = close;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getGroup_title() {
                return group_title;
            }

            public void setGroup_title(String group_title) {
                this.group_title = group_title;
            }

            public String getSimg() {
                return simg;
            }

            public void setSimg(String simg) {
                this.simg = simg;
            }

            public String getBgimg() {
                return bgimg;
            }

            public void setBgimg(String bgimg) {
                this.bgimg = bgimg;
            }

            public String getGroupid() {
                return groupid;
            }

            public void setGroupid(String groupid) {
                this.groupid = groupid;
            }

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }
        }
    }
}
