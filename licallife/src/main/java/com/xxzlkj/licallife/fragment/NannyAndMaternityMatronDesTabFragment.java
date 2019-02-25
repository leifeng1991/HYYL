package com.xxzlkj.licallife.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.adapter.NannyAndMaternityMatronDesTabAdapter;
import com.xxzlkj.licallife.model.NannyAndMaternityMatronDesBean;
import com.xxzlkj.licallife.model.NannyAndMaternityMatronDesTabBean;
import com.xxzlkj.zhaolinshare.base.base.ReuseViewFragment;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:保姆或月嫂tab
 *
 * @author zhangrq
 *         2017/3/20 16:37
 */
public class NannyAndMaternityMatronDesTabFragment extends ReuseViewFragment {

    public static final String STATE = "state";
    private RecyclerView mRecyclerView;
    private LinearLayout ll_myself_des;
    private TextView tv_myself_des;
    private NannyAndMaternityMatronDesTabAdapter adapter;


    public static NannyAndMaternityMatronDesTabFragment newInstance() {
        return new NannyAndMaternityMatronDesTabFragment();
    }

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_tab_nanny_and_maternity_matron_des, container, false);
    }

    @Override
    protected void findViewById() {
        mRecyclerView = getView(R.id.recyclerView);// 时间轴内容
        ll_myself_des = getView(R.id.ll_myself_des);// 我自介绍布局
        tv_myself_des = getView(R.id.tv_myself_des);// 我自介绍
    }

    @Override
    public void setListener() {

    }

    @Override
    public void processLogic() {
        // 初始化RecyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setNestedScrollingEnabled(false);
        adapter = new NannyAndMaternityMatronDesTabAdapter(mContext, R.layout.item_nanny_and_maternity_matron_des_tab);
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * 设置数据，并设值
     */
    public void setData(String state, NannyAndMaternityMatronDesBean.DataBean data) {
        if (data == null) {
            return;
        }
        ArrayList<NannyAndMaternityMatronDesTabBean> list = new ArrayList<>();
        if ("0".equals(state)) {
            // 基本信息
            // 列表内容
            String education = data.getEducation();// 1 小学 2初中 3高中 4 中专 5大专 6本科
            String educationStr = null;
            if ("0".equals(education))
                educationStr = "保密";
            else if ("1".equals(education))
                educationStr = "小学";
            else if ("2".equals(education))
                educationStr = "初中";
            else if ("3".equals(education))
                educationStr = "高中";
            else if ("4".equals(education))
                educationStr = "中专";
            else if ("5".equals(education))
                educationStr = "大专";
            else if ("6".equals(education))
                educationStr = "本科";
            list.add(new NannyAndMaternityMatronDesTabBean("身份证号", data.getCardno(), "手机号", data.getShop() == null ? "" : data.getShop().getPhone()));
            list.add(new NannyAndMaternityMatronDesTabBean("类型", data.getTypes(), "籍贯", data.getPlace_title()));
            list.add(new NannyAndMaternityMatronDesTabBean("民族", data.getNation(), "婚姻", "1".equals(data.getMarriage()) ? "未婚" : ("2".equals(data.getMarriage()) ? "已婚" : "保密")));
            list.add(new NannyAndMaternityMatronDesTabBean("年龄", data.getAge(), "学历", educationStr));
            list.add(new NannyAndMaternityMatronDesTabBean("星座", data.getConstellation(), "属相", data.getGenus()));
            // Status 1 待岗 2 在岗
            list.add(new NannyAndMaternityMatronDesTabBean(
                    "当前状态", "1".equals(data.getStatus()) ? "待岗" : ("2".equals(data.getStatus()) ? "在岗" : ""),
                    "更新时间", DateUtils.getYearMonthDay(NumberFormatUtils.toLong(data.getEdittime()) * 1000, "yyyy/MM/dd HH:mm:ss")));
            // 自我介绍
            ll_myself_des.setVisibility(View.GONE);
        } else if ("1".equals(state)) {
            // 工作信息
            // 列表内容
            list.add(new NannyAndMaternityMatronDesTabBean("住址", data.getAddress(), "每月休息", data.getRest() + "天"));
            list.add(new NannyAndMaternityMatronDesTabBean("个人特长", data.getSpecialty(), "工作经验", data.getJobs()));
            list.add(new NannyAndMaternityMatronDesTabBean("工作能力", data.getWork(), null, null));
            // 自我介绍
            ll_myself_des.setVisibility(View.VISIBLE);
            tv_myself_des.setText(data.getIntroduction());
        } else if ("2".equals(state)) {
            // 培训信息
            // 列表内容
            List<NannyAndMaternityMatronDesBean.DataBean.TrainingBean> training = data.getTraining();
            for (NannyAndMaternityMatronDesBean.DataBean.TrainingBean trainingBean : training) {
                list.add(new NannyAndMaternityMatronDesTabBean("培训学校", trainingBean.getTitle(), "培训主题", trainingBean.getTheme()));
                list.add(new NannyAndMaternityMatronDesTabBean("类型", trainingBean.getTypes(), "培训日期", DateUtils.getYearMonthDay(NumberFormatUtils.toLong(trainingBean.getThemetime()) * 1000, "yyyy-MM-dd")));
            }

            // 自我介绍
            ll_myself_des.setVisibility(View.GONE);
        }
        adapter.clearAndAddList(list);
    }

}
