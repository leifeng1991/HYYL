package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

/**
 * 描述:
 *
 * @author leifeng
 *         2018/1/29 16:47
 */


public class SimgBean extends BaseBean{

    /**
     * data : {"simg":"http://zhaolin-10029121.image.myqcloud.com/sample1504768836176"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * simg : http://zhaolin-10029121.image.myqcloud.com/sample1504768836176
         */

        private String simg;

        public String getSimg() {
            return simg;
        }

        public void setSimg(String simg) {
            this.simg = simg;
        }
    }
}
