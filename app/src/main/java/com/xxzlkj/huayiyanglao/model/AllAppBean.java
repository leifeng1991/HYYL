package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class AllAppBean extends BaseBean {

    private List<DataBean> data=new ArrayList<>();

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * app : []
         * id : -1
         * title : 最近使用
         */

        private String id;// 0为我的应用 -1为最近使用,其余没有id
        private String title;
        private ArrayList<ClassifyItemItemBean> app=new ArrayList<>();

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

        public ArrayList<ClassifyItemItemBean> getApp() {
            return app;
        }

        public void setApp(ArrayList<ClassifyItemItemBean> app) {
            this.app = app;
        }
    }
}
