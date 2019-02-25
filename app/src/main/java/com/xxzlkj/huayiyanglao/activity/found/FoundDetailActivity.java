package com.xxzlkj.huayiyanglao.activity.found;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunfusheng.marqueeview.DisplayUtil;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.activity.setting.PersonInfoActivity;
import com.xxzlkj.huayiyanglao.adapter.FoundSignUpUserAdapter;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.FoundDetail;
import com.xxzlkj.huayiyanglao.model.SignUpUser;
import com.xxzlkj.licallife.adapter.ProjecImageAdapter;
import com.xxzlkj.licallife.model.ProjectDetail;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.utils.ZLDialogUtil;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * 发现详情（活动详情）
 */
public class FoundDetailActivity extends MyBaseActivity {
    private ImageView mMainImageView;
    private CircleImageView mLogoImageView;
    private ImageView mLikeImageView;
    private TextView mTitleTextView;
    private TextView mCellTextView;
    private TextView mPriceTextView;
    private TextView mTimeTextView;
    private TextView mDayTextView;
    private TextView mAddressTextView;
    private TextView mNumberTextView;
    private TextView mNameTextView;
    private TextView mTelTextView;
    private TextView mSignUpTextView;
    private TextView mNavigationTextView;
    private RecyclerView mDetailRecyclerView;
    private RecyclerView mUserRecyclerView;
    // 报名按钮布局
    private LinearLayout mSignUpLayout;
    private LinearLayout mLikeLayout;
    private LinearLayout mDetailLinearLayout;
    // 价格布局
    private LinearLayout mPriceLinearLayout;
    // 已报名和主办方布局
    private LinearLayout mLinearLayout, mBaoMingLinearLayout, mAddressLayout;
    private FoundSignUpUserAdapter mFoundSignUpUserAdapter;
    // 活动id
    private String id;
    // 是否喜欢 1喜欢 0未喜欢
    private String isCell = "0";
    // 是否报名 1已报名 0未报名
    private String isSign = "0";
    // 1报名中 0已截止
    private String state = "1";
    //报名人数
    private int number;
    private String endLatitudeStr;
    private String endLongitudeStr;
    // 报名状态是否改变
    private boolean signIsChange = false;
    private double mPrice;

    /**
     * @param context 上下文 （必传）
     * @param id      活动id （必传）
     */
    public static Intent newIntent(Context context, String id) {
        Intent intent = new Intent(context, FoundDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(StringConstants.INTENT_PARAM_ID, id);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_found_detail);
    }

    @Override
    protected void findViewById() {
        mMainImageView = getView(R.id.id_fd_image);
        mLogoImageView = getView(R.id.id_fd_logo);
        mLikeImageView = getView(R.id.id_fd_like_image);
        mTitleTextView = getView(R.id.id_fd_title);
        mCellTextView = getView(R.id.id_fd_like_content);
        mPriceTextView = getView(R.id.id_fd_price);
        mTimeTextView = getView(R.id.id_fd_time);
        mDayTextView = getView(R.id.id_fd_day);
        mAddressTextView = getView(R.id.id_fd_address);
        mNumberTextView = getView(R.id.id_fd_num);
        mNameTextView = getView(R.id.id_fd_name);
        mTelTextView = getView(R.id.id_fd_like_tel);
        mSignUpTextView = getView(R.id.id_fd_sign_up);
        mSignUpLayout = getView(R.id.id_fd_more_user);
        mLikeLayout = getView(R.id.id_fd_like);
        mNavigationTextView = getView(R.id.id_fd_navigation);
        mDetailLinearLayout = getView(R.id.id_fd_detail_layout);
        mPriceLinearLayout = getView(R.id.id_fd_layout_price);
        mSignUpLayout = getView(R.id.id_fd_sign_up_layout);
        mLinearLayout = getView(R.id.id_fd_layout);
        mBaoMingLinearLayout = getView(R.id.id_baoming_layout);
        mAddressLayout = getView(R.id.id_address_layout);
        //详情
        mDetailRecyclerView = getView(R.id.id_fd_detail_list);
        mDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //用户
        mUserRecyclerView = getView(R.id.id_fd_user_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mUserRecyclerView.setLayoutManager(linearLayoutManager);
        mFoundSignUpUserAdapter = new FoundSignUpUserAdapter(this, R.layout.adapter_fsuu_list_item, false);
        mUserRecyclerView.setAdapter(mFoundSignUpUserAdapter);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString(StringConstants.INTENT_PARAM_ID);
        }

        setTitleLeftBack();
        setTitleName("活动详情");
    }

    @Override
    protected void setListener() {
        mSignUpTextView.setOnClickListener(this);
        mSignUpLayout.setOnClickListener(this);
        mLikeLayout.setOnClickListener(this);
        mNavigationTextView.setOnClickListener(this);

        mFoundSignUpUserAdapter.setOnItemClickListener((position, item) -> {
            if (!TextUtils.isEmpty(id)) {
                startActivity(FoundSignUpActivity.newIntent(FoundDetailActivity.this, id));
            }
        });
    }

    @Override
    protected void processLogic() {
        getFoundDetail(id);
    }

    @Override
    public void onBackPressed() {
        // 报名状态发生变化
        if (signIsChange) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.id_fd_sign_up:// 立即报名
                if (ZhaoLinApplication.getInstance().getLoginUserDoLogin(this) != null) {
                    if ("0".equals(state)) {
                        ToastManager.showShortToast(mContext, "报名已经截止");
                    } else {

                        setSignActivity();
                    }
                }
                break;
            case R.id.id_fd_more_user:// 报名列表
                if (!TextUtils.isEmpty(id)) {
                    startActivity(FoundSignUpActivity.newIntent(this, id));
                }
                break;
            case R.id.id_fd_like:// 喜欢活动/取消喜欢活动
                if (ZhaoLinApplication.getInstance().getLoginUserDoLogin(this) != null) {
                    setCellActivity();
                }
                break;
            case R.id.id_fd_navigation:// 导航
                ToastManager.showShortToast(mContext, "该功能暂未开放");
//                showNaviDialog();
                break;
        }
    }

    private void showBaoMingPayDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("报名需要支付一定的费用，确定要支付？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 获取活动详情数据
     */
    private void getFoundDetail(String id) {
        HashMap<String, String> map = new HashMap<>();
        // 活动id 必传
        map.put(URLConstants.REQUEST_PARAM_ID, id);
        // 会员id
        User loginUser = ZhaoLinApplication.getInstance().getLoginUser();
        map.put(URLConstants.REQUEST_PARAM_UID, loginUser != null ? loginUser.getData().getId() : "0");
        RequestManager.createRequest(ZLURLConstants.HEALTH_ACTIVITY_INFO_URL,
                map, new OnMyActivityRequestListener<FoundDetail>(this) {
                    @Override
                    public void onSuccess(FoundDetail bean) {
                        FoundDetail.DataBean data = bean.getData();
                        setData(data);
                    }
                });
    }

    /**
     * 设置数据
     */
    private void setData(FoundDetail.DataBean data) {
        endLatitudeStr = data.getLatitude();
        endLongitudeStr = data.getLongitude();

        int width = DisplayUtil.getWindowWidth(this) - DisplayUtil.dip2px(this, 22);
        PicassoUtils.setWithAndHeight(this, data.getSimg(), width, (int) (width * (1.0f * 377 / 706)), mMainImageView);
        mTitleTextView.setText(data.getTitle());
        String address = data.getAddress();
        // 地址为空不显示
        if (TextUtils.isEmpty(address)) {
            mAddressLayout.setVisibility(View.GONE);
        } else {
            mAddressLayout.setVisibility(View.VISIBLE);
            mAddressTextView.setText(address);
        }

        // 时间
        long startTime = NumberFormatUtils.toLong(data.getStarttime()) * 1000;
        mDayTextView.setVisibility(View.GONE);
        long stopTime = NumberFormatUtils.toLong(data.getStoptime()) * 1000;
        mTimeTextView.setText(String.format("%s—%s", DateUtils.getYearMonthDay(startTime, "yyyy.MM.dd HH:mm"), DateUtils.getYearMonthDay(stopTime, "yyyy.MM.dd HH:mm")));

        PicassoUtils.setWithAndHeight(this, data.getLogo(), DisplayUtil.dip2px(this, 36), DisplayUtil.dip2px(this, 36), mLogoImageView);
        number = NumberFormatUtils.toInt(data.getCount(), 1);
        mNumberTextView.setText(String.format("%s人", data.getCount()));
        mNameTextView.setText(data.getName());
        mTelTextView.setText(String.format("客服电话：%s", data.getPhone()));

        // 详情图片
        List<ProjectDetail.DataBean.ImgBean> img = data.getImg();
        if (img != null && img.size() > 0) {
            ProjecImageAdapter mImageAdapter = new ProjecImageAdapter(this, R.layout.adapter_goods_detail_image);
            mImageAdapter.addList(img);
            mDetailRecyclerView.setAdapter(mImageAdapter);
        } else {
            mDetailLinearLayout.setVisibility(View.GONE);
        }
        // 1预约模板 2报名付费模板 3报名不付费模板 4活动展示模板 5图文展示模板
        if ("1".equals(data.getType()) || "4".equals(data.getType()) || "5".equals(data.getType())) {
            mSignUpLayout.setVisibility(View.GONE);
            mPriceLinearLayout.setVisibility(View.GONE);
            mLinearLayout.setVisibility(View.GONE);
        } else {
            mSignUpLayout.setVisibility(View.VISIBLE);

            // 价格
            String price = data.getPrice();
            mPrice = NumberFormatUtils.toDouble(price, 0);
            if (TextUtils.isEmpty(price) || mPrice <= 0) {
                mPriceLinearLayout.setVisibility(View.GONE);
            } else {
                mPriceTextView.setText(String.format("￥%s", data.getPrice()));
            }
        }
        // 用户头像
        List<SignUpUser> userBeanList = data.getUser();
        if (userBeanList != null && userBeanList.size() > 0) {
            LogUtil.e("===========", userBeanList.size() + "");
            mFoundSignUpUserAdapter.addList(userBeanList);
            mBaoMingLinearLayout.setVisibility(View.VISIBLE);
        } else {
            mBaoMingLinearLayout.setVisibility(View.GONE);
        }

        if ("0".equals(data.getIs_cell())) { // 不喜欢
            setNotCell();
        } else {// 1 喜欢
            setCell();
        }

        if ("0".equals(data.getIs_sign())) {// 未报名
            mSignUpTextView.setText("立即报名");
            isSign = "0";
        } else {// 已报名
            mSignUpTextView.setText("已报名");
            isSign = "1";
        }

        if ("0".equals(data.getState())) {// 已截止
            state = "0";
        } else {
            state = "1";
        }

    }

    /**
     * 喜欢活动/取消喜欢活动
     */
    private void setCellActivity() {
        HashMap<String, String> map = new HashMap<>();
        User user = ZhaoLinApplication.getInstance().getLoginUser();
        if (user == null) {
            return;
        }
        map.put(URLConstants.REQUEST_PARAM_UID, user.getData().getId());
        map.put(URLConstants.REQUEST_PARAM_PID, id);
        RequestManager.createRequest(URLConstants.REQUEST_CELL_ACTIVITY,
                map, new OnMyActivityRequestListener<BaseBean>(this) {
                    @Override
                    public void onSuccess(BaseBean bean) {
                        if ("0".equals(isCell)) {
                            setNotCell();
                        } else {
                            setCell();
                        }
                    }
                });
    }

    /**
     * 活动报名/取消报名
     */
    private void setSignActivity() {
        HashMap<String, String> map = new HashMap<>();
        // 会员id
        User user = ZhaoLinApplication.getInstance().getLoginUser();
        if (user == null) {
            return;
        }
        map.put(URLConstants.REQUEST_PARAM_UID, user.getData().getId());
        // 活动id
        map.put(URLConstants.REQUEST_PARAM_PID, id);
        RequestManager.createRequest(ZLURLConstants.USER_HEALTH_ACTIVITY_SIGN_URL,
                map, new OnMyActivityRequestListener<BaseBean>(this) {
                    @Override
                    public void onSuccess(BaseBean bean) {
                        if ("0".equals(isSign)) {
                            mSignUpTextView.setText("已报名");
                            isSign = "1";
                            number++;
                            // 增加头像
                            SignUpUser signUpUser = new SignUpUser();
                            signUpUser.setSimg(user.getData().getSimg());
                            mFoundSignUpUserAdapter.addItem(0, signUpUser);
                        } else {
                            mSignUpTextView.setText("立即报名");
                            isSign = "0";
                            number--;
                            // 获得下坐标
                            List<SignUpUser> list = mFoundSignUpUserAdapter.getList();
                            int index = -1;
                            for (int i = 0; i < list.size(); i++) {
                                if (user.getData().getSimg().equals(list.get(i).getSimg())) {
                                    index = i;
                                    break;
                                }
                            }

                            if (index != -1) {
                                // 删除头像
                                mFoundSignUpUserAdapter.removeItem(list.get(index));
                            }

                        }
                        mNumberTextView.setText(String.format(Locale.CHINESE, "%d人", number));

                        signIsChange = !signIsChange;

                        if (mFoundSignUpUserAdapter != null && mFoundSignUpUserAdapter.getList().size() > 0) {
                            mBaoMingLinearLayout.setVisibility(View.VISIBLE);
                        } else {
                            mBaoMingLinearLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailed(boolean isError, String code, String message) {
                        if (isError) {
                            // 400	数据错误或者为空，直接打印message信息
                            // 404	没有接收到传值
                            if ("400".equals(code)) {
                                onError(code, message);
                            } else if ("401".equals(code)) {
                                showDialog();
                            } else {
                                onException(404, "没有接收到传值");
                            }

                        } else {
                            // 404	没有接收到传值
                            onException(Integer.parseInt(code), message);
                        }
                    }
                });
    }

    /**
     * 导航方式选择对话框
     */
    private void showNaviDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_nav_content, null);
        TextView mTextView1 = view.findViewById(R.id.id_nc_1);
        TextView mTextView2 = view.findViewById(R.id.id_nc_2);
        TextView mTextView3 = view.findViewById(R.id.id_nc_3);
        builder.setView(view);
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        mTextView1.setOnClickListener(v -> {// 驾车
            if (!TextUtils.isEmpty(endLatitudeStr) && !TextUtils.isEmpty(endLongitudeStr)) {
                alertDialog.dismiss();
                startActivity(SingleRouteCalculateActivityNavi.newIntent(
                        FoundDetailActivity.this, NumberFormatUtils.toDouble(endLatitudeStr), NumberFormatUtils.toDouble(endLongitudeStr)));
            }
        });
        mTextView2.setOnClickListener(v -> {// 骑行
            if (!TextUtils.isEmpty(endLatitudeStr) && !TextUtils.isEmpty(endLongitudeStr)) {
                alertDialog.dismiss();
                startActivity(RideRouteCalculateActivityNavi.newIntent(
                        FoundDetailActivity.this, NumberFormatUtils.toDouble(endLatitudeStr), NumberFormatUtils.toDouble(endLongitudeStr)));
            }
        });
        mTextView3.setOnClickListener(v -> {// 步行
            if (!TextUtils.isEmpty(endLatitudeStr) && !TextUtils.isEmpty(endLongitudeStr)) {
                alertDialog.dismiss();
                startActivity(WalkRouteCalculateActivityNavi.newIntent(
                        FoundDetailActivity.this, NumberFormatUtils.toDouble(endLatitudeStr), NumberFormatUtils.toDouble(endLongitudeStr)));
            }
        });
    }

    /**
     * 感兴趣
     */
    private void setCell() {
        mLikeLayout.setBackgroundResource(R.drawable.shape_buy_now);
        mCellTextView.setTextColor(0xffffffff);
        mLikeImageView.setImageResource(R.mipmap.like_white);
        isCell = "0";
    }

    /**
     * 不感兴趣
     */
    private void setNotCell() {
        mLikeLayout.setBackgroundResource(R.drawable.shape_orange_stroke);
        mCellTextView.setTextColor(0xffff725c);
        mLikeImageView.setImageResource(R.mipmap.like_red);
        isCell = "1";
    }

    private void showDialog() {
        ZLDialogUtil.showPerfectInfo(this, new ZLDialogUtil.OnDialogButtonClickListener() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {
                startActivity(new Intent(FoundDetailActivity.this, PersonInfoActivity.class));
            }
        });
    }
}
