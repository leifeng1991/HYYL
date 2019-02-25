package com.xxzlkj.huayiyanglao.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.model.HomeIndexBean;
import com.xxzlkj.huayiyanglao.util.ZLActivityUtils;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;

import java.util.List;

/**
 * 描述:首页右边列表和悬浮标题适配器
 *
 * @author leifeng
 *         2017/11/21 17:08
 */


public class HomeContentAdapter extends AnimalsAdapter<RecyclerView.ViewHolder>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    private Context context;
    private Activity activity;

    public HomeContentAdapter(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_home_content_list, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecyclerView mRecyclerView = holder.itemView.findViewById(R.id.id_content_recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        HomeContentItemAdapter homeContentItemAdapter = new HomeContentItemAdapter(context, R.layout.adapter_home_content_list_item);
        mRecyclerView.setAdapter(homeContentItemAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);

        List<HomeIndexBean.DataBean.IndexBean.ChildBean> child = getItem(position).getChild();
        if (child != null && child.size() > 0)
            homeContentItemAdapter.addList(child);
        // item点击事件
        homeContentItemAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<HomeIndexBean.DataBean.IndexBean.ChildBean>() {
            @Override
            public void onClick(int position, HomeIndexBean.DataBean.IndexBean.ChildBean item) {
                // 跳转
                ZLActivityUtils.jumpToActivityByType(activity, item.getType(), item.getTo(), item.getTitle());
            }
        });
    }

    @Override
    public long getHeaderId(int position) {
       /* if (position == 0) {
            return -1;
        } else {
            return getItem(position).getTitle().charAt(0);
        }*/

        return getItem(position).getTitle().charAt(0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_sticky_header_list_item, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView.findViewById(R.id.id_title);
        textView.setText(getItem(position).getTitle());
        holder.itemView.setBackgroundColor(0xffffffff);
    }


}
