package com.xxzlkj.huayiyanglao.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.ArrayList;

/**
 * 用户数据
 * Created by Administrator on 2017/4/15.
 */

public class UserInfo extends BaseBean {


    /**
     * data : {"id":"111339","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492496922223405650","username":"哈哈哈哈哈","sex":"1","birthday":"1271433600","constellation":"白羊座","desc":"","addtime":"1488441670","logintime":"1492678433","age":"7","img":[{"id":"6737","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492667557269376862"},{"id":"6738","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492667691423486630"},{"id":"6739","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492668355188413072"},{"id":"6740","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492677927018801708"},{"id":"6741","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492667683415628747"},{"id":"6742","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492667676085637030"}]}
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
         * id : 111339
         * simg : http://zhaolin-10029121.image.myqcloud.com/sample1492496922223405650
         * username : 哈哈哈哈哈
         * sex : 1
         * birthday : 1271433600
         * constellation : 白羊座
         * desc :
         * addtime : 1488441670
         * logintime : 1492678433
         * age : 7
         * img : [{"id":"6737","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492667557269376862"},{"id":"6738","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492667691423486630"},{"id":"6739","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492668355188413072"},{"id":"6740","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492677927018801708"},{"id":"6741","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492667683415628747"},{"id":"6742","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492667676085637030"}]
         * "age": "1",
         * "friend": "0",
         * "concern_num": "0",
         * "fans_num": "0",
         * "friend_num": "0",
         *
         */

        private String id;
        private String simg;
        private String username;
        private String sex;
        private String birthday;
        private String constellation;
        private String desc;
        private String addtime;
        private String logintime;
        private String age;
        // 1:正常 0：隐身 不显示距离和时间 显示隐身
        private String hide;
        //融云token
        private String token;
        //距离
        private String juli;
        private String friend;
        private String concern_num;
        private String fans_num;
        private String friend_num;
        private String store_id;

        public String getStore_id() {
            return store_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public String getFriend() {
            return friend;
        }

        public void setFriend(String friend) {
            this.friend = friend;
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

        public String getJuli() {
            return juli;
        }

        public void setJuli(String juli) {
            this.juli = juli;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        private ArrayList<ImgBean> img = new ArrayList<>();

        public String getHide() {
            return hide;
        }

        public void setHide(String hide) {
            this.hide = hide;
        }

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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getConstellation() {
            return constellation;
        }

        public void setConstellation(String constellation) {
            this.constellation = constellation;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getLogintime() {
            return logintime;
        }

        public void setLogintime(String logintime) {
            this.logintime = logintime;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public ArrayList<ImgBean> getImg() {
            return img;
        }

        public void setImg(ArrayList<ImgBean> img) {
            this.img = img;
        }

        public static class ImgBean implements Parcelable {
            /**
             * id : 6737
             * simg : http://zhaolin-10029121.image.myqcloud.com/sample1492667557269376862
             */

            private String id;
            private String simg;

            protected ImgBean(Parcel in) {
                id = in.readString();
                simg = in.readString();
            }

            public static final Creator<ImgBean> CREATOR = new Creator<ImgBean>() {
                @Override
                public ImgBean createFromParcel(Parcel in) {
                    return new ImgBean(in);
                }

                @Override
                public ImgBean[] newArray(int size) {
                    return new ImgBean[size];
                }
            };

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

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(id);
                dest.writeString(simg);
            }
        }
    }
}
