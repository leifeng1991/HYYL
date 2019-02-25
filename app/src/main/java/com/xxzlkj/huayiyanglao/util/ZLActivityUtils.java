package com.xxzlkj.huayiyanglao.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.activity.HYClassifyActivity;
import com.xxzlkj.huayiyanglao.activity.OneLevelListActivity;
import com.xxzlkj.huayiyanglao.activity.TwoLevelListActivity;
import com.xxzlkj.huayiyanglao.activity.equipment.EquipmentHomeActivity;
import com.xxzlkj.huayiyanglao.activity.equipment.HealthDataActivity;
import com.xxzlkj.huayiyanglao.activity.foodorder.SelectAddressModeActivity;
import com.xxzlkj.huayiyanglao.activity.found.FoundDetailActivity;
import com.xxzlkj.huayiyanglao.event.JumpToCustomerServiceEvent;
import com.xxzlkj.licallife.activity.localLife.CleanersOrHourlyWorkerDetailsActivity;
import com.xxzlkj.licallife.activity.localLife.LocalLifeHomeActivity;
import com.xxzlkj.licallife.activity.localLife.NannyAndMaternityMatronDesActivity;
import com.xxzlkj.licallife.activity.localLife.NannyAndMaternityMatronListActivity;
import com.xxzlkj.licallife.activity.localLife.ProjectCleaningAndHourlyWorkerActivity;
import com.xxzlkj.licallife.activity.localLife.ProjectDetailActivity;
import com.xxzlkj.shop.activity.ShopHomeActivity;
import com.xxzlkj.shop.activity.shop.GoodsCategoryActivity;
import com.xxzlkj.shop.activity.shop.GoodsDetailActivity;
import com.xxzlkj.shop.activity.shop.SearchGoodsListActivity;
import com.xxzlkj.shop.activity.shop.ShopActionThemeActivity;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.utils.ShopActivityUtils;
import com.xxzlkj.zhaolinshare.base.util.CallPhoneUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import org.greenrobot.eventbus.EventBus;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/4/26 11:49
 */
public class ZLActivityUtils {

    public static void jumpToActivityByType(Activity mActivity, String type, String to) {
        if (TextUtils.isEmpty(type)) return;
        // type	跳转方式 0不跳转 1跳h5 2商品 3分类 4火爆预售 5兆邻团购 6果蔬生鲜 7活动 8兆邻推荐 9天天特价 10时速达(如果为4,5,6,8,9,10不用判断to)
        // to	跳转目标 为0不跳转
        switch (type) {
            case "0"://不跳转
                break;
            case "1"://跳转到H5
                if (!TextUtils.isEmpty(to) && !"0".equals(to)) {
                    JumpToWebView.getInstance(mActivity).jumpToHasTitleWebView(to, "详情");
                }
                break;
            case "2"://跳转到商品详情
                if (!TextUtils.isEmpty(to) && !"0".equals(to)) {
                    mActivity.startActivity(GoodsDetailActivity.newIntent(mActivity.getApplicationContext(), to));
                }
                break;
            case "3"://跳转到商品分类
                if (!TextUtils.isEmpty(to) && !"0".equals(to)) {
                    mActivity.startActivity(SearchGoodsListActivity.newIntent(mActivity.getApplicationContext(), -1, to, "", ""));
                }
                break;
            case "4"://跳转到火爆预售
                ShopActivityUtils.jumpToShopHomeActivity(mActivity, "2");
                break;
            case "5"://跳转到兆邻团购
                ShopActivityUtils.jumpToShopHomeActivity(mActivity, "3");
                break;
            case "6"://跳转到生鲜
                ShopActivityUtils.jumpToShopHomeActivity(mActivity, "4");
                break;
            case "7":// 活动
                if (!TextUtils.isEmpty(to) && !"0".equals(to)) {
                    mActivity.startActivity(FoundDetailActivity.newIntent(mActivity.getApplicationContext(), to));
                }
                break;
            case "8":// 兆邻推荐
                mActivity.startActivity(GoodsCategoryActivity.newIntent(mActivity.getApplicationContext(), "兆邻推荐"));
                break;
            case "9":// 天天特价
                mActivity.startActivity(GoodsCategoryActivity.newIntent(mActivity.getApplicationContext(), "天天特价"));
                break;
            case "10":// 时速达
                mActivity.startActivity(GoodsCategoryActivity.newIntent(mActivity.getApplicationContext(), "时速达"));
                break;
            case "11":// 商城果蔬生鲜活动
                if (!TextUtils.isEmpty(to))
                    mActivity.startActivity(SearchGoodsListActivity.newIntent(mActivity.getApplicationContext(), 2, to, "", ""));
                break;
            case "12":// 商品活动
                if (!TextUtils.isEmpty(to))
                    mActivity.startActivity(ShopActionThemeActivity.newIntent(mActivity.getApplicationContext(), 1, to));
                break;
            case "13":// 门店果蔬详情
                if (!TextUtils.isEmpty(to))
                    mActivity.startActivity(SearchGoodsListActivity.newIntent(mActivity.getApplicationContext(), 4, to, "", ""));
                break;
            case "14":// 门店品牌活动详情
                if (!TextUtils.isEmpty(to))
                    mActivity.startActivity(ShopActionThemeActivity.newIntent(mActivity.getApplicationContext(), 2, to));
                break;
            case "15":// 门店分类
                if (!TextUtils.isEmpty(to))
                    mActivity.startActivity(SearchGoodsListActivity.newIntent(mActivity.getApplicationContext(), 3, to, "", GlobalParams.storeId));
                break;
            case "16":// 跳转到应用
                mActivity.startActivity(HYClassifyActivity.newIntent(mActivity.getApplicationContext()));
                break;
            case "17":// 17保洁项目
                if (!TextUtils.isEmpty(to))
                    mActivity.startActivity(ProjectDetailActivity.newIntent(mActivity.getApplicationContext(), to));
                break;

            case "18":// 18 保洁师
                if (!TextUtils.isEmpty(to))
                    mActivity.startActivity(CleanersOrHourlyWorkerDetailsActivity.newIntent(mActivity.getApplicationContext(), to, true));
                break;
            case "19":// 19 小时工
                if (!TextUtils.isEmpty(to))
                    mActivity.startActivity(CleanersOrHourlyWorkerDetailsActivity.newIntent(mActivity.getApplicationContext(), to, false));
                break;
            case "20":// 20 月嫂
                if (!TextUtils.isEmpty(to))
                    mActivity.startActivity(NannyAndMaternityMatronDesActivity.newIntent(mActivity.getApplicationContext(), false, to));
                break;
            case "21":// 21 保姆
                if (!TextUtils.isEmpty(to))
                    mActivity.startActivity(NannyAndMaternityMatronDesActivity.newIntent(mActivity.getApplicationContext(), true, to));
                break;
            case "22":// 条H5，同1
                if (!TextUtils.isEmpty(to) && !"0".equals(to)) {
                    JumpToWebView.getInstance(mActivity).jumpToHasTitleWebView(to, "详情");
                }
                break;
            case "23":// 客服电话
                if (!TextUtils.isEmpty(to)) {
                    CallPhoneUtils.callPhoneDialog(mActivity, to);
                }
                break;
            case "24":// 主题活动详情
                if (!TextUtils.isEmpty(to)) {
                    mActivity.startActivity(ShopActionThemeActivity.newIntent(mActivity.getApplicationContext(), 3, to));
                }
                break;
            case "31":// 商城首页
                mActivity.startActivity(ShopHomeActivity.newIntent(mActivity.getApplicationContext(), null));
                break;
            case "32":// 订餐首页
                mActivity.startActivity(SelectAddressModeActivity.newIntent(mActivity.getApplicationContext()));
                break;
            case "33":// 本地生活首页
                mActivity.startActivity(LocalLifeHomeActivity.newIntent(mActivity.getApplicationContext()));
                break;
            case "34":// 只能设备首页
                mActivity.startActivity(EquipmentHomeActivity.newIntent(mActivity.getApplicationContext()));
                break;
            case "35":// 健康数据
                mActivity.startActivity(HealthDataActivity.newIntent(mActivity.getApplicationContext()));
                break;
            case "36":// 紧急救护
                EventBus.getDefault().postSticky(new JumpToCustomerServiceEvent());
                break;
            case "50":// 家政保洁列表
                mActivity.startActivity(ProjectCleaningAndHourlyWorkerActivity.newIntent(mActivity.getApplicationContext(), 4));
                break;
            case "51":// 保洁师列表
                mActivity.startActivity(ProjectCleaningAndHourlyWorkerActivity.newIntent(mActivity.getApplicationContext(), 1));
                break;
            case "52":// 小时工列表
                mActivity.startActivity(ProjectCleaningAndHourlyWorkerActivity.newIntent(mActivity.getApplicationContext(), 2));
                break;
            case "53":// 月嫂列表
                mActivity.startActivity(NannyAndMaternityMatronListActivity.newIntent(mActivity.getApplicationContext(), false));
                break;
            case "54":// 保姆列表
                mActivity.startActivity(NannyAndMaternityMatronListActivity.newIntent(mActivity.getApplicationContext(), true));
                break;
            case "117":// 跳转到2级分类
                if (!TextUtils.isEmpty(to) && !TextUtils.isEmpty(GlobalParams.communityId)) {
                    mActivity.startActivity(OneLevelListActivity.newIntent(mActivity.getApplicationContext(), to, ""));
                }
                break;
            case "118":// 家政三级分类
                if (!TextUtils.isEmpty(to)) {
                }
                break;
            case "119":// 医养医疗三级分类
                if (!TextUtils.isEmpty(to)) {
                    mActivity.startActivity(TwoLevelListActivity.newIntent(mActivity, to, ""));
                }
                break;
            default:
                ToastManager.showShortToast(mActivity.getApplicationContext(), "当前版本不支持此活动，请升级最新版本");
                break;
        }
    }

    /**
     * 有标题
     *
     * @param title 标题
     */
    public static void jumpToActivityByType(Activity mActivity, String type, String to, String title) {
        if ("117".equals(type)) {
            if (!TextUtils.isEmpty(to) && !TextUtils.isEmpty(GlobalParams.communityId)) {
                mActivity.startActivity(OneLevelListActivity.newIntent(mActivity.getApplicationContext(), to, title));
            }
        } else if ("119".equals(type)) {
            if (!TextUtils.isEmpty(to)) {
                mActivity.startActivity(TwoLevelListActivity.newIntent(mActivity, to, title));
            }
        } else {
            jumpToActivityByType(mActivity, type, to);
        }
    }


    /**
     * 分类的跳转
     */
    public static void jumpToActivityByClassId(Activity mActivity, String id) {
        if (TextUtils.isEmpty(id)) return;
        Context mContext = mActivity.getApplicationContext();
        switch (id) {
            case "8"://本地生活
                if (GlobalParams.isOpenLocalLife) {
                    // 开通
                    mActivity.startActivity(new Intent(mContext, LocalLifeHomeActivity.class));
                } else {
                    // 未开通
                    ToastManager.showShortToast(mContext, mContext.getString(R.string.noLocalLifeServiceHint));
                }
                break;
            case "9"://快递
//                mActivity.startActivity(new Intent(mContext, SearchExpressActivity.class));
                break;
            case "10"://养老
//                mActivity.startActivity(new Intent(mContext, YLMainActivity.class));
                break;
            case "15"://兆邻取件
                ToastManager.showShortToast(mContext, "暂未开放");
                break;
            case "16"://查快递
//                mActivity.startActivity(new Intent(mContext, SearchExpressActivity.class));
                break;
            case "17"://找快递员
                ToastManager.showShortToast(mContext, "暂未开放");
                break;
            case "18"://代收快递
                ToastManager.showShortToast(mContext, "暂未开放");
                break;
            case "19"://预约体检
                ToastManager.showShortToast(mContext, "暂未开放");
                break;
            case "20"://健康数据
                mActivity.startActivity(new Intent(mContext, HealthDataActivity.class));
                break;
            case "21"://步行榜
                ToastManager.showShortToast(mContext, "暂未开放");
                break;
            case "22"://健康超市
                ToastManager.showShortToast(mContext, "暂未开放");
                break;
            case "23"://上门服务
                ToastManager.showShortToast(mContext, "暂未开放");
                break;
            case "24"://保姆
                if (GlobalParams.isOpenLocalLife) {
                    // 开通
                    mActivity.startActivity(NannyAndMaternityMatronListActivity.newIntent(mContext, true));
                } else {
                    // 未开通
                    ToastManager.showShortToast(mContext, mContext.getString(R.string.noLocalLifeServiceHint));
                }
                break;
            case "25"://月嫂
                if (GlobalParams.isOpenLocalLife) {
                    // 开通
                    mActivity.startActivity(NannyAndMaternityMatronListActivity.newIntent(mContext, false));
                } else {
                    // 未开通
                    ToastManager.showShortToast(mContext, mContext.getString(R.string.noLocalLifeServiceHint));
                }
                break;
            case "26"://保洁项目
                if (GlobalParams.isOpenLocalLife) {
                    // 开通
                    mActivity.startActivity(ProjectCleaningAndHourlyWorkerActivity.newIntent(mContext, 1));
                } else {
                    // 未开通
                    ToastManager.showShortToast(mContext, mContext.getString(R.string.noLocalLifeServiceHint));
                }
                break;
            case "27"://小时工
                if (GlobalParams.isOpenLocalLife) {
                    // 开通
                    mActivity.startActivity(ProjectCleaningAndHourlyWorkerActivity.newIntent(mContext, 2));
                } else {
                    // 未开通
                    ToastManager.showShortToast(mContext, mContext.getString(R.string.noLocalLifeServiceHint));
                }
                break;
            case "28"://保洁师
                if (GlobalParams.isOpenLocalLife) {
                    // 开通
                    mActivity.startActivity(ProjectCleaningAndHourlyWorkerActivity.newIntent(mContext, 3));
                } else {
                    // 未开通
                    ToastManager.showShortToast(mContext, mContext.getString(R.string.noLocalLifeServiceHint));
                }
                break;
            case "29"://兆邻物业
//                mActivity.startActivity(PropertyHomeActivity.newIntent(mActivity.getApplicationContext()));
                break;
            case "30"://开通服务
//                mActivity.startActivity(PropertyLocationActivity.newIntent(mActivity.getApplicationContext()));
                break;
            case "31"://我的房屋
                if (TextUtils.isEmpty(GlobalParams.houseId)) {
                    // 物业首页
//                    mActivity.startActivity(PropertyHomeActivity.newIntent(mContext));
                } else {
                    // 我的房屋
//                    mActivity.startActivity(MyHouseActivity.newIntent(mContext, GlobalParams.houseId));
                }
                break;
            case "32"://生活缴费
                if (TextUtils.isEmpty(GlobalParams.houseId)) {
                    // 物业首页
//                    mActivity.startActivity(PropertyHomeActivity.newIntent(mContext));
                } else {
                    //生活缴费
//                    mActivity.startActivity(LivingPaymentHomeActivity.newIntent(mActivity.getApplicationContext(), GlobalParams.houseId));
                }
                break;
            case "33"://我的资产
//                mActivity.startActivity(MyAssetsActivity.newIntent(mActivity.getApplicationContext()));
                break;
            case "34"://报检报修
                if (TextUtils.isEmpty(GlobalParams.houseId)) {
                    // 物业首页
//                    mActivity.startActivity(PropertyHomeActivity.newIntent(mContext));
                } else {
                    //报检报修
//                    mActivity.startActivity(SubmitInspectionFixActivity.newIntent(mActivity.getApplicationContext(), GlobalParams.houseId));
                }
                break;
            default:
                ToastManager.showShortToast(mContext, "暂未开放");
                break;
        }
    }
}
