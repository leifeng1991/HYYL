package com.xxzlkj.huayiyanglao.event;

/**
 * 描述:
 *
 * @author leifeng
 *         2017/11/28 18:19
 */


public class FoodsSaleListEvent {
    // 多个id用英文逗号隔开
    private String foodsIds;
    // 多个用英文逗号隔开 一定要和id一一对应
    private String foodsNumbers;

    public FoodsSaleListEvent(String foodsIds, String foodsNumbers) {
        this.foodsIds = foodsIds;
        this.foodsNumbers = foodsNumbers;
    }

    public String getFoodsIds() {
        return foodsIds;
    }

    public void setFoodsIds(String foodsIds) {
        this.foodsIds = foodsIds;
    }

    public String getFoodsNumbers() {
        return foodsNumbers;
    }

    public void setFoodsNumbers(String foodsNumbers) {
        this.foodsNumbers = foodsNumbers;
    }
}
