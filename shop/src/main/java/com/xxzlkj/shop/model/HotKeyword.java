package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.List;

/**
 * 热门搜索关键字
 * Created by Administrator on 2017/3/21.
 */

public class HotKeyword extends BaseBean {

    /**
     * data : {"word":["红牛","雪花","苹果"]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<String> word;

        public List<String> getWord() {
            return word;
        }

        public void setWord(List<String> word) {
            this.word = word;
        }
    }
}
