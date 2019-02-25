package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.List;

/**
 * 描述:
 *
 * @author leifeng
 *         2017/11/28 9:47
 */


public class FoodsSaleListBean extends BaseBean{

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * title : 午餐
         * list : [{"title":"午餐","list":[{"id":"9507233","title":"鸡肉沙拉+寿司+果汁","ads":"鸡肉沙拉+寿司+果汁，营养快餐一波鲜","price":"36.00","num":"100","groupid":"775","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1495678303220937","istop":"1","stoptime":"1","groupname":"午餐","count":"0"},{"id":"9507228","title":"皮蛋瘦肉粥","ads":"口感顺滑皮蛋瘦肉粥","price":"8.00","num":"100","groupid":"775","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1495607895268172","istop":"1","stoptime":"1","groupname":"午餐","count":"0"},{"id":"9507232","title":"八宝粥","ads":"香甜八宝粥","price":"8.00","num":"100","groupid":"775","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1495608221311178","istop":"1","stoptime":"1","groupname":"午餐","count":"0"},{"id":"9507227","title":"西葫芦鸡蛋","ads":"素菜美味","price":"15.00","num":"100","groupid":"775","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1495607830703485","istop":"1","stoptime":"1","groupname":"午餐","count":"0"},{"id":"9507231","title":"咸鸭蛋","ads":"油多","price":"4.00","num":"100","groupid":"775","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1495608180450258","istop":"1","stoptime":"1","groupname":"午餐","count":"0"},{"id":"9507226","title":"小胖肉夹馍","ads":"没有香菜，香","price":"10.00","num":"100","groupid":"775","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1495607782404325","istop":"1","stoptime":"1","groupname":"午餐","count":"0"},{"id":"9507230","title":"茶叶蛋","ads":"土豪的象征","price":"3.00","num":"100","groupid":"775","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1495608147747255","istop":"1","stoptime":"1","groupname":"午餐","count":"0"},{"id":"9507229","title":"南瓜小米粥","ads":"养胃健脾南瓜粥","price":"8.00","num":"100","groupid":"775","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1495608056901538","istop":"1","stoptime":"1","groupname":"午餐","count":"0"}]}]
         */

        private String title;
        private List<ListBeanX> list;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<ListBeanX> getList() {
            return list;
        }

        public void setList(List<ListBeanX> list) {
            this.list = list;
        }

        public static class ListBeanX {
            /**
             * title : 午餐
             * list : [{"id":"9507233","title":"鸡肉沙拉+寿司+果汁","ads":"鸡肉沙拉+寿司+果汁，营养快餐一波鲜","price":"36.00","num":"100","groupid":"775","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1495678303220937","istop":"1","stoptime":"1","groupname":"午餐","count":"0"},{"id":"9507228","title":"皮蛋瘦肉粥","ads":"口感顺滑皮蛋瘦肉粥","price":"8.00","num":"100","groupid":"775","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1495607895268172","istop":"1","stoptime":"1","groupname":"午餐","count":"0"},{"id":"9507232","title":"八宝粥","ads":"香甜八宝粥","price":"8.00","num":"100","groupid":"775","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1495608221311178","istop":"1","stoptime":"1","groupname":"午餐","count":"0"},{"id":"9507227","title":"西葫芦鸡蛋","ads":"素菜美味","price":"15.00","num":"100","groupid":"775","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1495607830703485","istop":"1","stoptime":"1","groupname":"午餐","count":"0"},{"id":"9507231","title":"咸鸭蛋","ads":"油多","price":"4.00","num":"100","groupid":"775","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1495608180450258","istop":"1","stoptime":"1","groupname":"午餐","count":"0"},{"id":"9507226","title":"小胖肉夹馍","ads":"没有香菜，香","price":"10.00","num":"100","groupid":"775","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1495607782404325","istop":"1","stoptime":"1","groupname":"午餐","count":"0"},{"id":"9507230","title":"茶叶蛋","ads":"土豪的象征","price":"3.00","num":"100","groupid":"775","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1495608147747255","istop":"1","stoptime":"1","groupname":"午餐","count":"0"},{"id":"9507229","title":"南瓜小米粥","ads":"养胃健脾南瓜粥","price":"8.00","num":"100","groupid":"775","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1495608056901538","istop":"1","stoptime":"1","groupname":"午餐","count":"0"}]
             */

            private String title;
            private List<FoodsBean> list;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public List<FoodsBean> getList() {
                return list;
            }

            public void setList(List<FoodsBean> list) {
                this.list = list;
            }

        }
    }
}
