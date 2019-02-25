package com.xxzlkj.licallife.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.model.SortBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;


/**
 * 描述:
 *
 * @author zhangrq
 *         2017/3/11 17:24
 */
public class SortAdapter extends BaseAdapter<SortBean> {
    private int selectPosition = 0;


    public SortAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, SortBean itemBean) {
        TextView tv_title = holder.getView(R.id.tv_title);
        View view_line = holder.getView(R.id.view_line);
        // 设置值
        tv_title.setText(String.valueOf(itemBean.getTitle() + "（降序）"));
        tv_title.setTextColor(selectPosition == position ? ContextCompat.getColor(mContext, R.color.orange_ff735d)
                : ContextCompat.getColor(mContext, R.color.black_746f6e));
        // 通用设置
        view_line.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);

    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    public SortBean getSelectItemBean() {
        return getList().get(selectPosition);
    }
}
