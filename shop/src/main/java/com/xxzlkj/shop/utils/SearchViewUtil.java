package com.xxzlkj.shop.utils;

import android.graphics.Color;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.LinearLayout;

import com.xxzlkj.shop.R;

import java.lang.reflect.Field;


public class SearchViewUtil {
    /**
     * 去掉下划线
     * @param mSearchView
     */
    public static void setNoLine(SearchView mSearchView){
        if (mSearchView != null) {
            try {
                Class<?> argClass = mSearchView.getClass();
                //--指定某个私有属性,mSearchPlate是搜索框父布局的名字
                Field ownField = argClass.getDeclaredField("mSearchPlate");
                //--暴力反射,只有暴力反射才能拿到私有属性
                ownField.setAccessible(true);
                View mView = (View) ownField.get(mSearchView);
                //--设置背景
                mView.setBackgroundColor(Color.TRANSPARENT);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 获取焦点
     * @param mSearchView
     */
    public static void setSearchView(SearchView mSearchView) {
        if (mSearchView != null){
            mSearchView.setIconified(false);
            mSearchView.setIconifiedByDefault(false);
            LinearLayout rootView = (LinearLayout) mSearchView.findViewById(R.id.search_bar);
            rootView.setClickable(true);
        }
    }
}
