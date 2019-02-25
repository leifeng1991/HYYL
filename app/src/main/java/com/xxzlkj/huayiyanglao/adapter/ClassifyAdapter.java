package com.xxzlkj.huayiyanglao.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.model.ClassifyItemBean;
import com.xxzlkj.huayiyanglao.model.ClassifyItemItemBean;
import com.xxzlkj.huayiyanglao.util.ZLActivityUtils;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;


public class ClassifyAdapter extends BaseAdapter<ClassifyItemBean> {
    private final Activity mActivity;

    public ClassifyAdapter(Activity mActivity, Context context, int itemId) {
        super(context, itemId);
        this.mActivity = mActivity;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, ClassifyItemBean itemBean) {
        TextView tv_title = holder.getView(R.id.tv_title);
        RecyclerView rv_content = holder.getView(R.id.rv_content);
        //设值
        tv_title.setText(itemBean.getTitle());

        rv_content.setLayoutManager(new ClassifyGridLayoutManager(mContext, 4));
        ClassifyItemAdapter itemAdapter = new ClassifyItemAdapter(mContext, R.layout.adapter_classify_my_app_item);
        rv_content.setAdapter(itemAdapter);
        itemAdapter.addList(itemBean.getList());
        itemAdapter.setOnItemClickListener(new OnItemClickListener<ClassifyItemItemBean>() {
            @Override
            public void onClick(int position, ClassifyItemItemBean item) {
                // 根据id，跳转
                ZLActivityUtils.jumpToActivityByType(mActivity, item.getType(), item.getTo(), item.getTitle());
            }
        });
    }
}
