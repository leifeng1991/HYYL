package com.xxzlkj.zhaolinshare.chat;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author zhangrq
 */
public class BaseCommonViewHolder {
    private final SparseArray<View> mViews;
    public View mConvertView;
    private int mPosition;

    private BaseCommonViewHolder(Context context,
                                 ViewGroup parent, int layoutId, int position) {
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(
                layoutId, parent, false);
        mPosition = position;
        mConvertView.setTag(this);
    }

    /**
     * 拿到一个ViewHolder对象
     *
     * @param context     上下文
     * @param convertView 复用的布局
     * @param parent      父布局
     * @param layoutId    布局id
     * @param position    第几个view
     * @return ViewHolder对象
     */
    public static BaseCommonViewHolder get(Context context, View convertView,
                                           ViewGroup parent, int layoutId, int position) {

        if (null == convertView) {
            return new BaseCommonViewHolder(context, parent,
                    layoutId, position);
        }
        return (BaseCommonViewHolder) convertView.getTag();
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId 控件id
     * @return view控件
     */
    public <T extends View> T getView(int viewId) {

        View view = mViews.get(viewId);
        if (null == view) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public TextView getTextView(int viewId) {
        return (TextView) getView(viewId);
    }

    public ImageView getImageView(int viewId) {
        return (ImageView) getView(viewId);
    }

    public Button getButton(int viewId) {
        return (Button) getView(viewId);
    }

    public ImageButton getImgButton(int viewId) {
        return (ImageButton) getView(viewId);
    }

    /**
     * 为TextView设置文字内容
     *
     * @param viewId 控件id
     * @param text   文字内容
     * @return BaseCommonViewHolder
     */
    public BaseCommonViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId 控件id
     * @param imgId  图片id
     * @return BaseCommonViewHolder
     */
    public BaseCommonViewHolder setImageResource(int viewId, int imgId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(imgId);
        return this;
    }

    public int getPosition() {
        return mPosition;
    }
}
