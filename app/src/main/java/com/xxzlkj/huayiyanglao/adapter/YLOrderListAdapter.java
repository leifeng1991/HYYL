package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.model.HealthOrderListBean;
import com.xxzlkj.huayiyanglao.weight.ShapeButton;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;


/**
 * 描述: 医养医疗订单adapter
 *
 * @author zhangrq
 *         2016/12/12 11:06
 */

public class YLOrderListAdapter extends BaseAdapter<HealthOrderListBean.DataBean> {
    private OnLeftButtonListener leftButtonListener;
    private OnRightButtonListener rightButtonListener;

    public YLOrderListAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final HealthOrderListBean.DataBean itemBean) {
        TextView mTimeTextView = holder.getView(R.id.id_time);// 时间
        TextView mStateTextView = holder.getView(R.id.id_state);// 状态
        ImageView mImageView = holder.getView(R.id.id_image);// 产品图片
        TextView mTitleTextView = holder.getView(R.id.id_title);// 产品名称
        TextView mZhuTextView = holder.getView(R.id.id_zhu);// 注
        TextView mPriceTextView = holder.getView(R.id.id_price);// 价格
        ShapeButton mLeftShapeButton = holder.getView(R.id.id_left_button);// 左按钮
        ShapeButton mRightShapeButton = holder.getView(R.id.id_right_button);// 右按钮
        // 设置数据
        mTimeTextView.setText(String.format("时间：%s-%s", DateUtils.getYearMonthDay(
                NumberFormatUtils.toLong(itemBean.getService_starttime()) * 1000, "yyyy.MM.dd HH:mm"),
                DateUtils.getYearMonthDay(NumberFormatUtils.toLong(itemBean.getService_endtime()) * 1000, "HH:mm")));
        // 状态 1待付款 2待服务 3,4服务中 4已完成 5已取消 6已退款
        String state = itemBean.getState();
        if ("1".equals(state)) {
            // 待付款
            mStateTextView.setText("待付款");
            setLeftRightButton(mLeftShapeButton, mRightShapeButton, "取消预约", "立即支付");
        } else if ("2".equals(state)) {
            //待服务
            mStateTextView.setText("待服务");
            setLeftRightButton(mLeftShapeButton, mRightShapeButton, "取消预约", "");
        } else if ("5".equals(state) || "6".equals(state)) {
            // 已失效
            mStateTextView.setText("已失效");
            setLeftRightButton(mLeftShapeButton, mRightShapeButton, "删除订单", "再次预约");
        } else {
            // 已完成
            mStateTextView.setText("已完成");
            setLeftRightButton(mLeftShapeButton, mRightShapeButton, "删除订单", "再次预约");
        }
        HealthOrderListBean.DataBean.GoodsBean goodsBean = itemBean.getGoods().get(0);
        PicassoUtils.setImageBig(mContext, goodsBean.getSimg(), mImageView);
        mTitleTextView.setText(goodsBean.getTitle());
        mPriceTextView.setText(String.format("¥%s", goodsBean.getPrice()));
        String cancel_desc = itemBean.getCancel_desc();
        mZhuTextView.setVisibility(TextUtils.isEmpty(cancel_desc) ? View.GONE : View.VISIBLE);
        mZhuTextView.setText(TextUtils.isEmpty(cancel_desc) ? "" : cancel_desc);
        mLeftShapeButton.setOnClickListener(v -> {
            if (leftButtonListener != null)
                leftButtonListener.onLeftButtonClick(mLeftShapeButton.getText().toString(), position, itemBean);
        });
        mRightShapeButton.setOnClickListener(v -> {
            if (rightButtonListener != null)
                rightButtonListener.onRightButtonClick(mRightShapeButton.getText().toString(), position, itemBean);
        });
    }

    private void setLeftRightButton(ShapeButton mLeftShapeButton, ShapeButton mRightShapeButton, String leftText, String rightText) {
        mLeftShapeButton.setVisibility(TextUtils.isEmpty(leftText) ? View.GONE : View.VISIBLE);
        mRightShapeButton.setVisibility(TextUtils.isEmpty(rightText) ? View.GONE : View.VISIBLE);
        mLeftShapeButton.setText(leftText);
        mRightShapeButton.setText(rightText);
    }

    /**
     * 左侧按钮点击监听事件
     */
    public void setOnLeftButtonListener(OnLeftButtonListener leftButtonListener) {
        this.leftButtonListener = leftButtonListener;
    }

    /**
     * 右侧按钮点击监听事件
     */
    public void setOnRightButtonListener(OnRightButtonListener rightButtonListener) {
        this.rightButtonListener = rightButtonListener;
    }

    public interface OnLeftButtonListener {
        void onLeftButtonClick(String leftText, int position, HealthOrderListBean.DataBean itemBean);
    }

    public interface OnRightButtonListener {
        void onRightButtonClick(String rightText, int position, HealthOrderListBean.DataBean itemBean);
    }
}


