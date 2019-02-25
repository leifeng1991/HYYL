package com.xxzlkj.huayiyanglao.model;

import java.util.ArrayList;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/4/12 14:27
 */
public class ClassifyItemBean {
    private String title;
    private ArrayList<ClassifyItemItemBean> list=new ArrayList<>();

    public ClassifyItemBean(String title, ArrayList<ClassifyItemItemBean> list) {
        this.title = title;
        this.list = list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<ClassifyItemItemBean> getList() {
        return list;
    }

    public void setList(ArrayList<ClassifyItemItemBean> list) {
        this.list = list;
    }
}
