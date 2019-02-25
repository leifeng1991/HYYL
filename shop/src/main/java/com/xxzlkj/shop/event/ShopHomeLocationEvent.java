package com.xxzlkj.shop.event;

import com.amap.api.services.core.PoiItem;

/**
 * 商城首页定位
 * Created by Administrator on 2017/6/28.
 */

public class ShopHomeLocationEvent {
    private PoiItem poiItem;

    public ShopHomeLocationEvent(PoiItem poiItem) {
        this.poiItem = poiItem;
    }

    public PoiItem getPoiItem() {
        return poiItem;
    }

    public void setPoiItem(PoiItem poiItem) {
        this.poiItem = poiItem;
    }
}
