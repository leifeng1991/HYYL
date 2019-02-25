package com.xxzlkj.shop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PreferencesSaver;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;

import java.util.ArrayList;
import java.util.Collections;


/**
 * 搜索历史是适配器
 * Created by Administrator on 2017/3/21.
 */

public class HistoryKeywordAdapter extends BaseAdapter<String> {

    private OnIsHasKeywordListener listener;

    public HistoryKeywordAdapter(Context context, int itemId, OnIsHasKeywordListener listener) {
        super(context, itemId);
        this.listener = listener;
    }

    @Override
    public void convert(BaseViewHolder holder, final int position, String itemBean) {
        TextView mKeyword = holder.getView(R.id.id_hkl_keyword);
        TextViewUtils.setText(mKeyword,itemBean);
        ImageView mDeleteImage = holder.getView(R.id.id_hkl_delete);
        mDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = PreferencesSaver.getStrList(mContext, StringConstants.PREFERENCES_KEY_KEYWORD_LIST);
                Collections.reverse(list);
                if (list != null && list.size() > 0){
                    list.remove(position);
                    PreferencesSaver.putStrList(mContext,StringConstants.PREFERENCES_KEY_KEYWORD_LIST,list);
                }
                deleteItem(position);

                if (listener == null) return;
                if (getItemCount() > 0){
                    listener.setIsHasKeyword(false);
                }else {
                    listener.setIsHasKeyword(true);
                }
            }
        });
    }

    /**
     * item数量大于0
     */
    public interface OnIsHasKeywordListener{
        void setIsHasKeyword(boolean isHasKeyword);
    }
}
