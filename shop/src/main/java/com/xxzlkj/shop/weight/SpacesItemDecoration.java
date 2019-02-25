package com.xxzlkj.shop.weight;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by liu on 2016/1/25.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
//        outRect.left = space;
        outRect.top = space;
        outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
        /*if(parent.getChildPosition(view) == 0 || parent.getChildPosition(view) == 5) {
            Log.e("Main",parent.getChildAdapterPosition(view)+"");
            outRect.left = 66;
        }else {
            outRect.left = 0;
        }

        if(parent.getChildPosition(view) == 4 || parent.getChildPosition(view) == 9) {
            Log.e("Main",parent.getChildAdapterPosition(view)+"");
            outRect.right = 66;
        }else {
            outRect.right = 59;
        }*/
    }
}
