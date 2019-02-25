package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.event.OtherServersChangeEvent;
import com.xxzlkj.huayiyanglao.event.RecentAppChangeEvent;
import com.xxzlkj.huayiyanglao.model.ClassifyItemItemBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;

import org.greenrobot.eventbus.EventBus;


public class ClassifyMyAppAdapter extends BaseAdapter<ClassifyItemItemBean> {
    public ClassifyMyAppAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final ClassifyItemItemBean bean) {
        RelativeLayout rl_root = holder.getView(R.id.rl_root);
        ImageView iv_classify_top_item = holder.getView(R.id.iv_classify_top_item);
        TextView tv_classify_top_item = holder.getView(R.id.tv_classify_top_item);
        final ImageView iv_classify_top_item_right = holder.getView(R.id.iv_classify_top_item_right);
        iv_classify_top_item_right.setVisibility(View.VISIBLE);
        // 设置值
        TextViewUtils.setText(tv_classify_top_item, bean.getTitle());
        if (!TextUtils.isEmpty(bean.getSimg())) {
            PicassoUtils.setImageSmall(mContext, bean.getSimg(), iv_classify_top_item);
        } else {
            iv_classify_top_item.setImageDrawable(null);
        }
        if (bean.isEdit()) {
            // 编辑页面
            // 已添加，显示减号
            rl_root.setBackgroundResource(R.drawable.shape_classify_item_item_bg);
            iv_classify_top_item_right.setImageResource(R.mipmap.ic_classify_delete);
            iv_classify_top_item_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 通知状态改变
                    bean.setState(0);// 1已在我的应用，0，未在我的应用里面
                    EventBus.getDefault().postSticky(new RecentAppChangeEvent(bean));
                    EventBus.getDefault().postSticky(new OtherServersChangeEvent(bean));
                    removeItem(bean);

                }
            });
        } else {
            // 不编辑，不显示
            rl_root.setBackgroundDrawable(null);
            iv_classify_top_item_right.setVisibility(View.INVISIBLE);
        }
    }
}
