package com.xxzlkj.huayiyanglao.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.MessageCenterListBean;
import com.xxzlkj.huayiyanglao.weight.ShapeButton;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 描述:监护消息
 *
 * @author leifeng
 *         2018/2/26 14:23
 */


public class MonitoringMessageAdapter extends BaseAdapter<MessageCenterListBean.DataBean> {
    private Activity activity;

    public MonitoringMessageAdapter(Context context, Activity activity, int itemId) {
        super(context, itemId);
        this.activity = activity;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, MessageCenterListBean.DataBean itemBean) {
        CircleImageView mCircleImageView = holder.getView(R.id.id_user_avatar);// 用户头像
        TextView mUserNickTextView = holder.getView(R.id.id_nick);// 用户昵称
        TextView mUserRealNameTextView = holder.getView(R.id.id_real_name);// 用户真实姓名
        TextView mTimeTextView = holder.getView(R.id.id_time);// 时间
        TextView mStateTextView = holder.getView(R.id.id_state);// 状态
        ShapeButton mReceiverShapeButton = holder.getView(R.id.id_receiver_btn);// 接受按钮
        // 设置数据
        String simg = itemBean.getSimg();
        if (TextUtils.isEmpty(simg)) {
            // 用户头像为空显示默认
            mCircleImageView.setImageResource(R.mipmap.default_icon);
        } else {
            // 用户头像不为空加载用户头像
            PicassoUtils.setImageSmall(mContext, simg, mCircleImageView);
        }
        String username = itemBean.getUsername();
        mUserNickTextView.setText(TextUtils.isEmpty(username) ? "华颐用户" : username);
        mUserRealNameTextView.setText(String.format("真实姓名：%s", itemBean.getName()));
        mTimeTextView.setText(DateUtils.getMonthDayHourMinute(NumberFormatUtils.toLong(itemBean.getAddtime()) * 1000));
        // 1 申请中 2已通过 3申请失败
        String state = itemBean.getState();
        mStateTextView.setVisibility("1".equals(state) ? View.GONE : View.VISIBLE);
        mStateTextView.setText("2".equals(state) ? "已接受" : "3".equals(state) ? "已拒绝" : "");
        mReceiverShapeButton.setVisibility("1".equals(state) ? View.VISIBLE : View.GONE);

        mReceiverShapeButton.setOnClickListener(v -> {
            // 网络请求
            auditWatchGuardianship(itemBean);
        });
    }

    /**
     * 申请监护
     */
    private void auditWatchGuardianship(MessageCenterListBean.DataBean itemBean) {

        Map<String, String> map = new HashMap<>();
        User loginUserDoLogin = ZhaoLinApplication.getInstance().getLoginUser();
        // 用户id
        map.put(ZLConstants.Params.UID, loginUserDoLogin.getData().getId());
        // 数据id
        map.put(ZLConstants.Params.ID, itemBean.getId());
        // 2同意 3拒绝
        map.put(ZLConstants.Params.STATE, "2");
        RequestManager.createRequest(ZLURLConstants.AUDIT_WATCH_GUARDIANSHIP_URL, map, new OnMyActivityRequestListener<BaseBean>((BaseActivity) activity) {
            @Override
            public void onSuccess(BaseBean bean) {
                itemBean.setState("2");
                notifyDataSetChanged();
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {

            }
        });
    }
}
