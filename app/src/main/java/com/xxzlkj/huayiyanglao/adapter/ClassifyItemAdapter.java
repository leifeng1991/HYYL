package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.event.ClassifyAnimationEvent;
import com.xxzlkj.huayiyanglao.event.ClassifyMyAppAddEvent;
import com.xxzlkj.huayiyanglao.event.RecentAppChangeEvent;
import com.xxzlkj.huayiyanglao.model.ClassifyItemItemBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;

import org.greenrobot.eventbus.EventBus;


public class ClassifyItemAdapter extends BaseAdapter<ClassifyItemItemBean> {
    public ClassifyItemAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(final BaseViewHolder holder, int position, final ClassifyItemItemBean itemBean) {
        RelativeLayout rl_root = holder.getView(R.id.rl_root);
        ImageView iv_classify_top_item = holder.getView(R.id.iv_classify_top_item);
        TextView tv_classify_top_item = holder.getView(R.id.tv_classify_top_item);
        final ImageView iv_classify_top_item_right = holder.getView(R.id.iv_classify_top_item_right);
        iv_classify_top_item_right.setVisibility(View.VISIBLE);
        // 设置值
        tv_classify_top_item.setText(itemBean.getTitle());
        PicassoUtils.setImageSmall(mContext, itemBean.getSimg(), iv_classify_top_item);
        if (itemBean.isEdit()) {
            // 编辑页面
            // 1已添加，0，未添加
            rl_root.setBackgroundResource(R.drawable.shape_classify_item_item_bg);

            if (itemBean.getState() == 1) {
                // 已添加，显示对勾
                iv_classify_top_item_right.setClickable(false);
                iv_classify_top_item_right.setImageResource(R.mipmap.ic_classify_ok);
            } else if (itemBean.getState() == 0) {
                // 未添加，显示加好
                iv_classify_top_item_right.setImageResource(R.mipmap.ic_classify_add);
                iv_classify_top_item_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 设置改变
                        iv_classify_top_item_right.setImageResource(R.mipmap.ic_classify_ok);
                        itemBean.setState(1);
                        // 发送通知
                        EventBus.getDefault().postSticky(new ClassifyAnimationEvent(holder.itemView, true));
                        EventBus.getDefault().postSticky(new ClassifyMyAppAddEvent(itemBean));
                        EventBus.getDefault().postSticky(new RecentAppChangeEvent(itemBean));
                    }
                });
            }
        } else {
            // 不是编辑页面，不显示
            rl_root.setBackgroundDrawable(null);
            iv_classify_top_item_right.setVisibility(View.INVISIBLE);
        }
    }
}
