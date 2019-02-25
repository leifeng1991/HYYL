package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

/**
 * 描述:
 *
 * @author leifeng
 *         2017/11/28 11:33
 */


public class FoodsAddCartBean extends BaseBean{

    /**
     * data : {"id":"2711","num":"1","pid":"9507236","starttime":"1511820000","stoptime":"1511829000","title":"八宝粥","price":"8.00","ads":"香甜八宝粥","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1495608221311178","sale_num":"100","count":"2"}
     */

    private FoodsBean data;

    public FoodsBean getData() {
        return data;
    }

    public void setData(FoodsBean data) {
        this.data = data;
    }

}
