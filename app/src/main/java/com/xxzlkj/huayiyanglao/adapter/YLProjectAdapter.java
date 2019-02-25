package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.model.ProjectDetailBean1;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;

/**
 * 描述:医养医疗项目
 *
 * @author leifeng
 *         2018/3/8 14:20
 */


public class YLProjectAdapter extends BaseAdapter<ProjectDetailBean1.DataBean.ProjectBean> {
    public YLProjectAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, ProjectDetailBean1.DataBean.ProjectBean itemBean) {
        TextView mTitleTextView = holder.getView(R.id.id_title);// 标题
        TextView mContentTextView = holder.getView(R.id.id_content);// 内容
        // 设置数据
        mTitleTextView.setText(itemBean.getTitle());
        String content = itemBean.getContent();
        mContentTextView.setText(content);
    }
}
