package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息
 * Created by Administrator on 2017/6/15.
 */

public class MessageTypeBean extends BaseBean {


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
         * title : 通知消息
         * simg : http://zhaolin-10029121.image.myqcloud.com/sample1501059193501615
         * message_id : 248
         * message_title : 测试消息
         * message_state : 2
         * message_addtime : 1501061217
         * house : 1
         * house_id : 43
         */

        private String id;
        private String title;
        private String simg;
        private String message_id;
        private String message_title;
        private String message_state;
        private String message_addtime;
        private int house;
        private String house_id;

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

        public String getMessage_id() {
            return message_id;
        }

        public void setMessage_id(String message_id) {
            this.message_id = message_id;
        }

        public String getMessage_title() {
            return message_title;
        }

        public void setMessage_title(String message_title) {
            this.message_title = message_title;
        }

        public String getMessage_state() {
            return message_state;
        }

        public void setMessage_state(String message_state) {
            this.message_state = message_state;
        }

        public String getMessage_addtime() {
            return message_addtime;
        }

        public void setMessage_addtime(String message_addtime) {
            this.message_addtime = message_addtime;
        }

        public int getHouse() {
            return house;
        }

        public void setHouse(int house) {
            this.house = house;
        }

        public String getHouse_id() {
            return house_id;
        }

        public void setHouse_id(String house_id) {
            this.house_id = house_id;
        }
    }
}
