package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.model.BeyondFenceBean;
import com.xxzlkj.huayiyanglao.model.LocationHistoryTabBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;


/**
 * 描述:
 *
 * @author zhangrq
 *         2016/12/12 11:06
 */

public class BeyondFenceAdapter extends BaseAdapter<BeyondFenceBean.DataBean> {
    public int selectPosition = -1;
    private OnCheckedListener listener;

    public BeyondFenceAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final BeyondFenceBean.DataBean itemBean) {
        ImageView mCheckedImageView = holder.getView(R.id.id_checked_image);
        TextView tv_left = holder.getView(R.id.tv_left);
        TextView tv_right = holder.getView(R.id.tv_right);
        View view_line = holder.getView(R.id.view_line);
        // 设置值
        tv_left.setText(itemBean.getDay());
        tv_right.setText(String.format("%s次", itemBean.getCount()));
        // 设置线
        view_line.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
        mCheckedImageView.setImageResource(selectPosition == position ? R.mipmap.ic_blue_checked : R.mipmap.ic_blue_normal_checked);
        mCheckedImageView.setOnClickListener(v -> {
            selectPosition = selectPosition != position ? position : -1;
            notifyDataSetChanged();
            if (listener != null)
                listener.checked(itemBean.getDay());
        });
    }

    public void setOnCheckedListener(OnCheckedListener listener) {
        this.listener = listener;
    }

    public interface OnCheckedListener {
        void checked(String day);
    }
}


