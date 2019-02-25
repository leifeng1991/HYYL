package com.xxzlkj.licallife.activity.localLife;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.adapter.LocalLifeDesAdapter;
import com.xxzlkj.licallife.interfac.LocalLifeLibraryInterface;
import com.xxzlkj.licallife.model.AuditionSucceedBean;
import com.xxzlkj.licallife.model.LocalLifeDesBean;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.utils.OrderUtils;
import com.xxzlkj.shop.utils.ZLDateUtils;
import com.xxzlkj.shop.utils.ZLDialogUtil;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.StringUtil;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;
import com.zrq.spanbuilder.Spans;

import java.util.HashMap;
import java.util.List;


/**
 * 本地生活列表详情
 *
 * @author zhangrq
 */
public class LocalLifeDesActivity extends MyBaseActivity {

    public static final int BTN_STATE_REFUND_ING = 0;// 退款中
    public static final int BTN_STATE_CANCEL_ORDER = 1;// 取消订单
    public static final int BTN_STATE_GO_PAYMENT = 2;// 去付款
    public static final int BTN_STATE_APPLY_REFUND = 3;// 申请退款
    public static final int BTN_STATE_REFUND_CANCEL = 4;// 退款取消
    public static final int BTN_STATE_SERVICE_FINISH = 5;// 服务完成
    public static final int BTN_STATE_APPLY_AFTER_SALE = 6;// 申请售后
    public static final int BTN_STATE_DELETE_ORDER = 7;// 删除订单
    public static final int BTN_STATE_ALREADY_REFUND = 8;// 已退款
    public static final int BTN_STATE_AFTER_SALE_DES = 9;// 售后详情

    public static final int BTN_STATE_CANCEL_SUBSCRIBE = 10;// 取消预约
    public static final int BTN_STATE_AUDITION_SUCCEED = 11;// 面试通过


    private static final String ORDER_ID = "order_id";
    private static final String TABLES = "tables";
    private static final int RESULT_REFRESH = 100;
    private RecyclerView mRecyclerView;
    private LinearLayout ll_state_des, ll_is_edit_order, ll_order_des_bottom, ll_nanny_maternity_layout;
    private ImageView iv_state_des;
    private TextView tv_state_des, tv_nanny_maternity_1, tv_nanny_maternity_2, tv_nanny_maternity_3_title, tv_nanny_maternity_3, tv_nanny_maternity_4, tv_edit_order_new_time, tv_edit_order_raw_time, tv_edit_order_edit_time, tv_edit_order_des, tv_order_des_btn1, tv_order_des_btn2,
            tv_orderId, tv_addTime, tv_address_name, tv_address_phone, tv_address_address, tv_server_type, tv_payment, tv_price_title, tv_favorable_title, tv_price, tv_favorable, tv_payment_money;
    private LocalLifeDesBean.DataBean data;
    private String orderId;
    private boolean isNoApplyAfterSale;
    private boolean isNannyAndMaternityMatron;
    private String mTables;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_local_life_des);
    }

    @Override
    protected void findViewById() {
        // 头
        ll_state_des = getView(R.id.ll_state_des);
        iv_state_des = getView(R.id.iv_state_des);
        tv_state_des = getView(R.id.tv_state_des);
        // 订单、收货人信息
        tv_orderId = getView(R.id.tv_orderId);
        tv_addTime = getView(R.id.tv_addTime);

        tv_address_name = getView(R.id.tv_address_name);
        tv_address_phone = getView(R.id.tv_address_phone);
        tv_address_address = getView(R.id.tv_address_address);

        // 支付信息
        mRecyclerView = getView(R.id.recyclerView);
        tv_server_type = getView(R.id.tv_server_type);// 服务类型
        tv_payment = getView(R.id.tv_payment);
        ll_nanny_maternity_layout = getView(R.id.ll_nanny_maternity_layout);// 保姆月嫂布局
        tv_nanny_maternity_1 = getView(R.id.tv_nanny_maternity_1);// 保姆月嫂布局Item 1
        tv_nanny_maternity_2 = getView(R.id.tv_nanny_maternity_2);// 保姆月嫂布局Item 2
        tv_nanny_maternity_3_title = getView(R.id.tv_nanny_maternity_3_title);// 保姆月嫂布局Item 3
        tv_nanny_maternity_3 = getView(R.id.tv_nanny_maternity_3);// 保姆月嫂布局Item 3
        tv_nanny_maternity_4 = getView(R.id.tv_nanny_maternity_4);// 保姆月嫂布局Item 4


        tv_price_title = getView(R.id.tv_price_title);// 商品总额标题
        tv_price = getView(R.id.tv_price);// 商品总额
        tv_favorable_title = getView(R.id.tv_favorable_title);//优惠标题
        tv_favorable = getView(R.id.tv_favorable);//优惠
        tv_payment_money = getView(R.id.tv_payment_money);
        // 是否修改订单
        ll_is_edit_order = getView(R.id.ll_is_edit_order);
        tv_edit_order_new_time = getView(R.id.tv_edit_order_new_time);
        tv_edit_order_raw_time = getView(R.id.tv_edit_order_raw_time);
        tv_edit_order_des = getView(R.id.tv_edit_order_des);
        tv_edit_order_edit_time = getView(R.id.tv_edit_order_edit_time);
        // 底部按钮
        ll_order_des_bottom = getView(R.id.ll_order_des_bottom);
        tv_order_des_btn1 = getView(R.id.tv_order_des_btn1);
        tv_order_des_btn2 = getView(R.id.tv_order_des_btn2);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("订单详情");
        setTitleRightText("投诉");
        ZLDateUtils.initCurrentTime();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        orderId = getIntent().getStringExtra(ORDER_ID);
        mTables = getIntent().getStringExtra(TABLES);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNetData();
    }

    /**
     * @param tables 1为家政订单 2为医养医疗
     */
    public static Intent newIntent(Context context, String orderId, String tables) {
        Intent intent = new Intent(context, LocalLifeDesActivity.class);
        intent.putExtra(ORDER_ID, orderId);
        intent.putExtra(TABLES, tables);
        return intent;
    }

    private void getNetData() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("id", orderId);
        RequestManager.createRequest("1".equals(mTables) ? URLConstants.JZ_ORDER_INFO_URL : URLConstants.HEALTH_ORDER_INFO_URL, stringStringHashMap, new OnMyActivityRequestListener<LocalLifeDesBean>(this) {

            @Override
            public void onSuccess(LocalLifeDesBean bean) {
                setData(bean);
            }
        });
    }

    private void setData(LocalLifeDesBean bean) {

        data = bean.getData();
//        状态 1待付款 2待服务 3服务中  4待确认，已完成 5已取消 6已退款
//        uidtime	state等于4时判断此值 等于0时为服务中 不为0时为已完成
//        is_tui	为2显示申请退款中 为1时 如果state等于2或3时显示申请退款 等于3显示退款详情 等于4时只显示退款灰色按钮不可点
//        refund_id;// 为0则未申请售后,不为0则为申请售后

//        pid	（保姆，月嫂，type 3、4类型） 判断是否是预付订单 0预付订单(交定金) 非0余额订单(交剩余费用)
//        audition_time	面试时间
//        audition_type	面试类型 1到店面试 2到家面试
//        audition_address	面试地址
//        shoptime	（保姆，月嫂，type3、4类型）shoptime等于0为预约中 有值则为面试中
//        type	订单类型 1保洁产品 2 预约保洁师 3月嫂 4保姆 5小时工'
        String type = data.getType();
        String shoptime = data.getShoptime();
        String pid = data.getPid();// 0预付订单 非0余额订单
        isNoApplyAfterSale = "0".equals(data.getRefund_id());// 是否没有申请售后
        isNannyAndMaternityMatron = "3".equals(type) || "4".equals(type);// 是否是保姆月嫂详情
        String state = data.getState();
        // 归原
        ll_nanny_maternity_layout.setVisibility(View.GONE);// 保姆月嫂布局
        ll_order_des_bottom.setVisibility(View.VISIBLE);// 底部按钮
        if ("2".equals(data.getIs_tui())) {
            // is_tui	为2显示申请退款中
            ll_state_des.setBackgroundResource(R.mipmap.bg_order_des_dfh);
            iv_state_des.setImageResource(R.mipmap.ic_local_life_des_dfw);
            tv_state_des.setText("待服务");

            ll_order_des_bottom.setVisibility(View.VISIBLE);
            tv_order_des_btn1.setVisibility(View.GONE);
            setBtnStateVisibility(tv_order_des_btn2, "退款中", BTN_STATE_REFUND_ING);
        } else if ("1".equals(state)) {
            // 1待付款
            // 设置头部
            ll_state_des.setBackgroundResource(R.mipmap.bg_order_des_dfk);
            iv_state_des.setImageResource(R.mipmap.ic_order_des_dfk);
            tv_state_des.setText(isNannyAndMaternityMatron ? ("0".equals(pid) ? "待付款（未预约）" : "待付款（已签约）") : "待付款");
            // 设置底部按钮
            ll_order_des_bottom.setVisibility(View.VISIBLE);
            setBtnStateVisibility(tv_order_des_btn1, "取消订单", BTN_STATE_CANCEL_ORDER);
            setBtnStateVisibility(tv_order_des_btn2, "立即支付", BTN_STATE_GO_PAYMENT);
        } else if ("2".equals(state)) {
            // 2待服务
            if ("3".equals(type) || "4".equals(type)) {
                // 3月嫂 4保姆
                if ("0".equals(pid)) {
                    // 0预付订单
                    if (NumberFormatUtils.toDouble(shoptime) == 0) {
                        // 0为预约中
                        // 设置头部
                        ll_state_des.setBackgroundResource(R.mipmap.bg_order_des_jygb);
                        iv_state_des.setImageResource(R.mipmap.ic_order_des_dfh);
                        tv_state_des.setText("预约中");

                        ll_order_des_bottom.setVisibility(View.VISIBLE);
                        tv_order_des_btn1.setVisibility(View.GONE);
                        setBtnStateVisibility(tv_order_des_btn2, "取消预约", BTN_STATE_CANCEL_SUBSCRIBE);
                    } else {
                        //  有值则为面试中（两个按钮：取消预约、面试通过）
                        // 设置头部
                        ll_state_des.setBackgroundResource(R.mipmap.bg_order_des_dfh);
                        iv_state_des.setImageResource(R.mipmap.ic_order_des_msz);
                        tv_state_des.setText("面试中");

                        ll_order_des_bottom.setVisibility(View.VISIBLE);
                        tv_order_des_btn1.setVisibility(View.GONE);
                        setBtnStateVisibility(tv_order_des_btn1, "取消预约", BTN_STATE_CANCEL_SUBSCRIBE);
                        setBtnStateVisibility(tv_order_des_btn2, "面试通过", BTN_STATE_AUDITION_SUCCEED);
                    }
                } else {
                    // 非0余额订单
                    // 同 1保洁产品 2 预约保洁师 5小时工 逻辑
                    // 设置头部
                    ll_state_des.setBackgroundResource(R.mipmap.bg_order_des_dfh);
                    iv_state_des.setImageResource(R.mipmap.ic_local_life_des_dfw);
                    tv_state_des.setText("待服务");

                    ll_order_des_bottom.setVisibility(View.VISIBLE);
                    tv_order_des_btn1.setVisibility(View.GONE);
                    setBtnStateVisibility(tv_order_des_btn2, "申请退款", BTN_STATE_APPLY_REFUND);
                }

            } else {
                // 1保洁产品 2 预约保洁师 5小时工'
                // 设置头部
                ll_state_des.setBackgroundResource(R.mipmap.bg_order_des_dfh);
                iv_state_des.setImageResource(R.mipmap.ic_local_life_des_dfw);
                tv_state_des.setText("待服务");

                ll_order_des_bottom.setVisibility(View.VISIBLE);
                tv_order_des_btn1.setVisibility(View.GONE);
                setBtnStateVisibility(tv_order_des_btn2, "申请退款", BTN_STATE_APPLY_REFUND);
            }


        } else if ("3".equals(state)) {
            // 3服务中（未点击任何操作）
            // 设置头部
            ll_state_des.setBackgroundResource(R.mipmap.bg_local_life_des_fwz);
            iv_state_des.setImageResource(R.mipmap.ic_order_des_dfh);
            tv_state_des.setText("服务中");
            if ("3".equals(data.getIs_tui())) {
                // state=3,istui=3 退款取消
                ll_order_des_bottom.setVisibility(View.VISIBLE);
                setBtnStateVisibility(tv_order_des_btn1, "退款取消", BTN_STATE_REFUND_CANCEL);
                setBtnStateVisibility(tv_order_des_btn2, "服务完成", BTN_STATE_SERVICE_FINISH);
            } else {
                // 服务中
                ll_order_des_bottom.setVisibility(View.VISIBLE);
                setBtnStateVisibility(tv_order_des_btn1, isNoApplyAfterSale ? "申请售后" : "售后详情", isNoApplyAfterSale ? BTN_STATE_APPLY_AFTER_SALE : BTN_STATE_AFTER_SALE_DES);
                setBtnStateVisibility(tv_order_des_btn2, "服务完成", BTN_STATE_SERVICE_FINISH);
            }
        } else if ("4".equals(state)) {
            // 4待确认，已完成(判断)
            // uidtime	state等于4时判断此值 等于0时为服务中 不为0时为已完成
            if ("0".equals(data.getUidtime())) {
                // 服务中，和状态3一样
                ll_state_des.setBackgroundResource(R.mipmap.bg_local_life_des_fwz);
                iv_state_des.setImageResource(R.mipmap.ic_order_des_dfh);
                tv_state_des.setText("服务中");

                ll_order_des_bottom.setVisibility(View.VISIBLE);
                setBtnStateVisibility(tv_order_des_btn1, isNoApplyAfterSale ? "申请售后" : "售后详情", isNoApplyAfterSale ? BTN_STATE_APPLY_AFTER_SALE : BTN_STATE_AFTER_SALE_DES);
                setBtnStateVisibility(tv_order_des_btn2, "服务完成", BTN_STATE_SERVICE_FINISH);
            } else {
                // 已完成（两种显示）
                ll_state_des.setBackgroundResource(R.mipmap.bg_local_life_des_ywc);
                iv_state_des.setImageResource(R.mipmap.ic_local_life_des_ywc);
                tv_state_des.setText("已完成");

                ll_order_des_bottom.setVisibility(View.VISIBLE);
                setBtnStateVisibility(tv_order_des_btn1, isNoApplyAfterSale ? "申请售后" : "售后详情", isNoApplyAfterSale ? BTN_STATE_APPLY_AFTER_SALE : BTN_STATE_AFTER_SALE_DES);
                setBtnStateVisibility(tv_order_des_btn2, "删除订单", BTN_STATE_DELETE_ORDER);
            }

        } else if ("5".equals(state)) {
            // 5已取消
            ll_state_des.setBackgroundResource(R.mipmap.bg_order_des_yqx);
            iv_state_des.setImageResource(R.mipmap.ic_order_des_jygb);
            tv_state_des.setText("已取消");

            ll_order_des_bottom.setVisibility(View.VISIBLE);
            setBtnStateVisibility(tv_order_des_btn1, "删除订单", BTN_STATE_DELETE_ORDER);
            tv_order_des_btn2.setVisibility(View.GONE);
        } else if ("6".equals(state)) {
            // 6已退款
            ll_state_des.setBackgroundResource(R.mipmap.bg_order_des_dfh);
            iv_state_des.setImageResource(R.mipmap.ic_local_life_des_dfw);
            tv_state_des.setText("待服务");

            ll_order_des_bottom.setVisibility(View.VISIBLE);
            tv_order_des_btn1.setVisibility(View.GONE);
            setBtnStateVisibility(tv_order_des_btn2, "已退款", BTN_STATE_ALREADY_REFUND);

        }
        // 设置通用的配置
        // 订单、收货人信息
        TextViewUtils.setText(tv_orderId, "订单编号：", data.getId());
        TextViewUtils.setText(tv_addTime, "下单时间：", DateUtils.getYearMonthDayHourMinuteSeconds(NumberFormatUtils.toLong(data.getAddtime()) * 1000));

        TextViewUtils.setText(tv_address_name, data.getAddress_name());
        TextViewUtils.setText(tv_address_phone, data.getAddress_phone());
        TextViewUtils.setText(tv_address_address, data.getAddress_address());

        // 设置列表展示
        // type	订单类型 1保洁产品 2 预约保洁师 3月嫂 4保姆 5小时工'
        // timetype	1 月 2 天

        // 保姆、月嫂并且按按月计费 或者 小时工
        boolean isHalfMoney = (isNannyAndMaternityMatron && "1".equals(data.getTimetype())) || "5".equals(type);

        LocalLifeDesAdapter adapter = new LocalLifeDesAdapter(mContext, R.layout.item_local_life_des, isHalfMoney);
        mRecyclerView.setAdapter(adapter);
        adapter.clearAndAddList(data.getGoods());

        // 服务类型
        String typeStr = "- -";
        if ("1".equals(type))
            typeStr = "保洁产品";
        else if ("2".equals(type))
            typeStr = "保洁师";
        else if ("3".equals(type))
            typeStr = "月嫂";
        else if ("4".equals(type))
            typeStr = "保姆";
        else if ("5".equals(type))
            typeStr = "小时工";
        tv_server_type.setText(typeStr);
        // 支付方式
        String paymentStr = null;
        switch (data.getPayment()) {
            case "0":
                paymentStr = "未支付";
                break;
            case "1":
                paymentStr = "支付宝";
                break;
            case "2":
                paymentStr = "微信";
                break;
            case "3":
                paymentStr = "银联";
                break;
            case "4":
                paymentStr = "钱包";
                break;
            case "5":
                paymentStr = "货到付款";
                break;
        }
        TextViewUtils.setText(tv_payment, paymentStr);//支付方式 0未支付 1支付宝 2微信 3银联 4钱包 5货到付款

        // 保姆月嫂布局、及下面所有
        if (isNannyAndMaternityMatron) {
            // 保姆、月嫂
            if ("0".equals(pid)) {
                // 0预付订单(交定金)
                ll_nanny_maternity_layout.setVisibility(View.VISIBLE);
                // 设置数据
                setNannyAndMaternityLayoutPrePayment();
                // 设置底部金额数据
                String payMoneyStr = StringUtil.saveTwoDecimal(data.getPrice());// 支付金额
                tv_price_title.setText("定金");
                TextViewUtils.setText(tv_price, "¥ ", payMoneyStr);// 定金
                tv_favorable_title.setText("优惠");
                TextViewUtils.setText(tv_favorable, "- ¥ 0.00");// 优惠
                TextViewUtils.setText(tv_payment_money, "¥ ", payMoneyStr);// 实付金额
            } else {
                // 非0余额订单(交剩余费用)
                ll_nanny_maternity_layout.setVisibility(View.VISIBLE);
                // 设置数据
                setNannyAndMaternityLayoutSpareMoney();
                // 设置底部金额数据
                tv_price_title.setText("订单金额");
                TextViewUtils.setText(tv_price, "¥ ", StringUtil.saveTwoDecimal(data.getPrices()));// 订单金额
                tv_favorable_title.setText("定金");
                String preMoneyStr = StringUtil.saveTwoDecimal(String.valueOf(NumberFormatUtils.toDouble(data.getPrices()) - NumberFormatUtils.toDouble(data.getPrice())));// 支付金额
                TextViewUtils.setText(tv_favorable, "- ¥ ", preMoneyStr);// 定金
                TextViewUtils.setText(tv_payment_money, "¥ ", StringUtil.saveTwoDecimal(data.getPrice()));// 实付金额
            }
        } else {
            // 普通的
            ll_nanny_maternity_layout.setVisibility(View.GONE);
            // 设置底部金额数据
            String payMoneyStr = StringUtil.saveTwoDecimal(data.getPrice());// 支付金额

            tv_price_title.setText("商品总额");
            TextViewUtils.setText(tv_price, "¥ ", payMoneyStr);// 商品总额
            tv_favorable_title.setText("优惠");
            TextViewUtils.setText(tv_favorable, "- ¥ 0.00");// 优惠
            TextViewUtils.setText(tv_payment_money, "¥ ", payMoneyStr);// 实付金额
        }


        String edittime = data.getEdittime();
        if (!TextUtils.isEmpty(edittime) && NumberFormatUtils.toLong(edittime) != 0) {
            // 不为空并且不为0，代表修改过
            ll_is_edit_order.setVisibility(View.VISIBLE);

            setItemValue(tv_edit_order_new_time, "现服务时间：", DateUtils.getYearMonthDayHourMinute(NumberFormatUtils.toLong(data.getEndtime()) * 1000));
            setItemValue(tv_edit_order_raw_time, "原服务时间：", DateUtils.getYearMonthDayHourMinute(NumberFormatUtils.toLong(data.getFormertime()) * 1000));
            setItemValue(tv_edit_order_des, "备注：", data.getEdit_desc());
            tv_edit_order_edit_time.setText(DateUtils.getYearMonthDayHourMinute(NumberFormatUtils.toLong(edittime) * 1000));
        } else {
            // 没修改过
            ll_is_edit_order.setVisibility(View.GONE);
        }
    }

    /**
     * 设置保姆月嫂--交余款
     */
    private void setNannyAndMaternityLayoutSpareMoney() {
        tv_nanny_maternity_1.setVisibility(View.VISIBLE);
        tv_nanny_maternity_1.setText(Spans.builder()
                .text("签约方式：").color(ContextCompat.getColor(mContext, R.color.black_746f6e))
                .text("纸质合同").color(ContextCompat.getColor(mContext, R.color.black_100015))
                .build());
        String timeStr = DateUtils.getYearMonthDayHourMinute(NumberFormatUtils.toLong(data.getAddtime()) * 1000);
        tv_nanny_maternity_2.setVisibility(View.VISIBLE);
        tv_nanny_maternity_2.setText(Spans.builder()
                .text("签约时间：").color(ContextCompat.getColor(mContext, R.color.black_746f6e))
                .text(timeStr).color(ContextCompat.getColor(mContext, R.color.black_100015))
                .build());
        tv_nanny_maternity_3_title.setVisibility(View.GONE);
        tv_nanny_maternity_3.setVisibility(View.GONE);
        tv_nanny_maternity_4.setVisibility(View.GONE);
    }

    /**
     * 设置保姆月嫂--交定金
     */
    private void setNannyAndMaternityLayoutPrePayment() {
        String audition_type = data.getAudition_type();//  1到店面试 2到家面试
        String audition_time = data.getAudition_time();// 面试时间
        String price = data.getPrice();// 支付金额
        if (price == null)
            price = "- -";
        String prices = data.getPrices();// 全部金额
        if (prices == null)
            prices = "- -";
        String auditionTimeStr = DateUtils.getYearMonthDayHourMinute(NumberFormatUtils.toLong(audition_time) * 1000);
        tv_nanny_maternity_1.setVisibility(View.VISIBLE);
        tv_nanny_maternity_1.setText(Spans.builder()
                .text("定金：").color(ContextCompat.getColor(mContext, R.color.black_222833))
                .text("￥" + price).color(ContextCompat.getColor(mContext, R.color.orange_ff725c))
                .build());
        tv_nanny_maternity_2.setVisibility(View.VISIBLE);
        tv_nanny_maternity_2.setText(Spans.builder()
                .text("服务金额：").color(ContextCompat.getColor(mContext, R.color.black_222833))
                .text("￥" + prices).color(ContextCompat.getColor(mContext, R.color.orange_ff725c))
                .build());
        tv_nanny_maternity_3_title.setVisibility(View.VISIBLE);
        tv_nanny_maternity_3_title.setText(Spans.builder().text("面试方式：").color(ContextCompat.getColor(mContext, R.color.black_222833)).build());
        tv_nanny_maternity_3.setVisibility(View.VISIBLE);
        tv_nanny_maternity_3.setText(Spans.builder().text("1".equals(audition_type)
                ? "到店面试（" + data.getAudition_address() + "）"
                : "到家面试").color(ContextCompat.getColor(mContext, R.color.black_222833)).build());
        tv_nanny_maternity_4.setVisibility(View.VISIBLE);
        tv_nanny_maternity_4.setText(Spans.builder()
                .text("面试时间：").color(ContextCompat.getColor(mContext, R.color.black_222833))
                .text(auditionTimeStr).color(ContextCompat.getColor(mContext, R.color.black_222833))
                .build());
    }

    private void setItemValue(TextView textView, String title, String value) {
        if (TextUtils.isEmpty(value))
            value = "——";
        textView.setText(Spans.builder().text(title + "").color(ContextCompat.getColor(mContext, R.color.black_777777))
                .text(value + "").color(ContextCompat.getColor(mContext, R.color.black_3a3a3a)).build());
    }

    private void setBtnStateVisibility(TextView button, String text, int tag) {
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(this);
        button.setTag(tag);
        button.setText(text);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (data == null) {
            ToastManager.showShortToast(mContext, "未获取到网络数据，请检查网络或稍后再试");
            return;
        }

        int i = v.getId();
        if (i == R.id.tv_title_right) {// 投诉
            startActivity(LocalLifeApplyComplainActivity.newIntent(mContext, data.getId()));

        }

        if (v.getTag() == null)
            return;
        if (data == null) {
            ToastManager.showShortToast(mContext, "未获取到网络数据，请检查网络或稍后再试");
            return;
        }
        int btnState = (int) v.getTag();
        switch (btnState) {
            case BTN_STATE_REFUND_ING:
                // 退款中(跳到退款详情页)
                startActivityForResult(LocalLifeRefundDesActivity.newIntent(mContext, data.getId()), RESULT_REFRESH);
                break;

            case BTN_STATE_CANCEL_ORDER:
                // 取消订单
                // 通知list列表刷新
                OrderUtils.submitLocalLifeCancelOrderHasDialog(this, data.getId(), this::getNetData);
                break;
            case BTN_STATE_GO_PAYMENT:
                // 立即支付
                long currentTimeSeconds = ZLDateUtils.getCurrentTimeSeconds();// 当前时间
                long endTime = NumberFormatUtils.toLong(data.getEndtime());// 服务时间
//                1）	服务时间-当前时间<0，提示【服务时间已过，订单失效】，订单状态到【已取消】
//                2）	服务时间-当前时间<6小时，提示【距服务开始时间少于6小时，商家可能无法服务，请确认是否下单】，是-跳转支付
//                3）	服务时间-当前时间>6小时，正常支付
                if (endTime - currentTimeSeconds < 6 * 60 * 60) {
                    if (endTime - currentTimeSeconds < 0) {
                        // 服务时间-当前时间<0，提示【服务时间已过，订单失效】，订单状态到【已取消】
                        ZLDialogUtil.showRawDialogOneButton(this, "服务时间已过，订单失效", this::cancelOrder);
//                        ZLDialogUtil.showRawDialogTwoButton(this, "服务时间已过，请修改订单服务时间",
//                                "取消订单", (dialog, which) -> cancelOrder(),
//                                "修改时间", (dialog, which) -> {
//                                    // TODO 差
//                                    // 修改时间
//                                });
                    } else {
                        // 服务时间-当前时间<6小时，提示【距服务开始时间少于6小时，商家可能无法服务，请确认是否下单】，是-跳转支付
                        ZLDialogUtil.showRawDialog(this, "距服务开始时间少于6小时，商家可能无法服务，请确认是否下单", this::jumpToPayActivity);
                    }
                } else {
                    // 服务时间-当前时间>6小时，正常支付
                    jumpToPayActivity();
                }

                break;
            case BTN_STATE_APPLY_REFUND:
                // 申请退款(跳到申请退款)
                OrderUtils.submitLocalLifeApplyRefundHasDialog(this, data.getId(), new ZLDialogUtil.OnClickConfirmListener() {
                    @Override
                    public void onClickConfirm() {
                        startActivityForResult(LocalLifeApplyRefundActivity.newIntent(mContext, data.getId(), false), RESULT_REFRESH);
                    }
                });
                break;
            case BTN_STATE_REFUND_CANCEL:
                // 退款取消(跳到退款详情页)
                startActivityForResult(LocalLifeRefundDesActivity.newIntent(mContext, data.getId()), RESULT_REFRESH);
                break;
            case BTN_STATE_SERVICE_FINISH:
                // 服务完成(弹框提示)
                OrderUtils.submitLocalLifeServiceFinishHasDialog(this, data.getId(), this::getNetData);
                break;
            case BTN_STATE_APPLY_AFTER_SALE:
                // 申请售后(弹框提示后跳转-申请售后页面)
                ZLDialogUtil.showRawDialog(this, "订单尚在服务中，若申请售后，将锁定订单状态，执行售后流程。请确认是否申请售后？", new ZLDialogUtil.OnClickConfirmListener() {
                    @Override
                    public void onClickConfirm() {
                        // 跳到 申请页面
                        List<LocalLifeDesBean.DataBean.GoodsBean> goods = data.getGoods();
                        if (goods != null && goods.size() > 0) {
                            LocalLifeDesBean.DataBean.GoodsBean goodsBean = goods.get(0);
                            startActivityForResult(LocalLifeAddRefundActivity.newIntent(mContext, data.getId(), data.getPrice(), goodsBean.getGoods_id(), false), RESULT_REFRESH);
                        }
                    }
                });
                break;
            case BTN_STATE_AFTER_SALE_DES:
                // 申请售后(跳到售后详情页面)
                startActivity(LocalLifeRefundGoodsInfoActivity.newIntent(mContext, data.getRefund_id()));
                break;
            case BTN_STATE_DELETE_ORDER:
                // 删除订单
                OrderUtils.submitLocalLifeDelOrderHasDialog(this, data.getId(), this::getNetData);
                break;
            case BTN_STATE_ALREADY_REFUND:
                // 已退款(跳到退款详情)
                startActivityForResult(LocalLifeRefundDesActivity.newIntent(mContext, data.getId()), RESULT_REFRESH);
                break;
            case BTN_STATE_CANCEL_SUBSCRIBE:
                // 取消预约（调用申请退款接口）
                OrderUtils.submitLocalLifeApplyRefundHasDialog(this, data.getId(), new ZLDialogUtil.OnClickConfirmListener() {
                    @Override
                    public void onClickConfirm() {
                        startActivityForResult(LocalLifeApplyRefundActivity.newIntent(mContext, data.getId(), false), RESULT_REFRESH);
                    }
                });
                break;
            case BTN_STATE_AUDITION_SUCCEED:
                // 面试通过（刷新详情--交余款）
                ZLDialogUtil.showRawDialog(this, "确认面试成功，支付剩余尾款？\n请确认已面试通过，签订正式合约，\n并同意支付订单剩余尾款", this::auditionSucceed);
                break;

        }
    }

    /**
     * 面试通过
     */
    private void auditionSucceed() {
        String price = StringUtil.saveTwoDecimal(String.valueOf(NumberFormatUtils.toDouble(data.getPrices()) - NumberFormatUtils.toDouble(data.getPrice())));// 支付金额
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("id", data.getId());
        stringStringHashMap.put("price", price);
        RequestManager.createRequest(URLConstants.JZ_AUDITION_YES_URL, stringStringHashMap, new OnMyActivityRequestListener<AuditionSucceedBean>(this) {

            @Override
            public void onSuccess(AuditionSucceedBean bean) {
                // 刷新详情--后交余款
                orderId = bean.getData().getId();
                getNetData();
            }
        });
    }

    private void jumpToPayActivity() {
        if (BaseApplication.getInstance() instanceof LocalLifeLibraryInterface) {
            startActivityForResult(((LocalLifeLibraryInterface) BaseApplication.getInstance()).getPayActivityIntent(mContext, data.getOrderid(), data.getId(), 2, ""), RESULT_REFRESH);
        }
    }

    private void cancelOrder() {
        OrderUtils.submitLocalLifeCancelOrder(this, data.getId(), "服务时间已过，订单失效", this::getNetData);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZLDateUtils.cancelTimer();
    }
}
