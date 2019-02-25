package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 时速达分类右侧页面Bean（二级分类）
 * Created by Administrator on 2017/3/16.
 */

public class SpeedGoodsCategoryBean extends BaseBean implements Serializable{


    /**
     * data : {"banner":"","group":[{"id":"439","title":"饮料","simg":"","child":[{"id":"678","title":"饮料冲调","pid":"439","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492324260434492","btitle":"饮料"},{"id":"662","title":"奶茶","pid":"439","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1491903556940917","btitle":"饮料"}]},{"id":"481","title":"奶品","simg":"","child":[{"id":"671","title":"牛奶饮品","pid":"481","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1491977645408271","btitle":"奶品"}]}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * banner :
         * group : [{"id":"439","title":"饮料","simg":"","child":[{"id":"678","title":"饮料冲调","pid":"439","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492324260434492","btitle":"饮料"},{"id":"662","title":"奶茶","pid":"439","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1491903556940917","btitle":"饮料"}]},{"id":"481","title":"奶品","simg":"","child":[{"id":"671","title":"牛奶饮品","pid":"481","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1491977645408271","btitle":"奶品"}]}]
         */

        private String banner;
        private List<GoodsGroupBean> group = new ArrayList<>();

        public String getBanner() {
            return banner;
        }

        public void setBanner(String banner) {
            this.banner = banner;
        }

        public List<GoodsGroupBean> getGroup() {
            return group;
        }

        public void setGroup(List<GoodsGroupBean> group) {
            this.group = group;
        }

    }
}
