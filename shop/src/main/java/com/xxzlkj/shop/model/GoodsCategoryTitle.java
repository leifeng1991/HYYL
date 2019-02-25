package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类名称（一级分类）
 * Created by Administrator on 2017/3/16.
 */

public class GoodsCategoryTitle extends BaseBean{

    private List<ItemBean> data = new ArrayList<>();

    public List<ItemBean> getData() {
        return data;
    }

    public void setData(List<ItemBean> data) {
        this.data = data;
    }

    public static class ItemBean {
        //分类id
        private String id;
        //分类名称
        private String title;

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

    }

}
