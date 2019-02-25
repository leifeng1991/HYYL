package com.xxzlkj.shop.weight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.adapter.ImageSelectAdapter;

import java.util.ArrayList;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/4/6 14:34
 */
public class MyImageSelectView extends LinearLayout {
    private RecyclerView recyclerView;
    private ImageSelectAdapter imageSelectAdapter;

    public MyImageSelectView(Context context) {
        this(context, null);
    }

    public MyImageSelectView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyImageSelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View rootView = View.inflate(context, R.layout.view_my_image_select_view, null);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        // 设置
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        imageSelectAdapter = new ImageSelectAdapter(context, R.layout.item_image_select);
        recyclerView.setAdapter(imageSelectAdapter);
        imageSelectAdapter.addItem(0, "");
        addView(rootView);
    }

    public void setActivity(Activity activity) {
        imageSelectAdapter.setActivity(activity);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (imageSelectAdapter != null) {
            imageSelectAdapter.onActivityResult(requestCode, resultCode, data);
        }
    }
    //直接把参数交给mHelper就行了
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageSelectAdapter.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public ArrayList<String> getImageList() {
        if (imageSelectAdapter != null) {
            return imageSelectAdapter.getImageList();
        }
        return new ArrayList<>();
    }

}
