package com.xxzlkj.huayiyanglao;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.TitleFragmentPagerAdapter;
import com.xxzlkj.zhaolinshare.base.util.PreferencesSaver;
import com.xxzlkj.zhaolinshare.base.util.SystemBarUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 引导页
 *
 * @author zhangrq
 */
public class GuideViewActivity extends BaseActivity {
    public static final String IMAGE_ID = "image_id";//引导页面的图片id
    public static final String IS_LAST_PAGE = "is_last_page";//引导页面的，是否是最后一页
    public static final String IS_FIRST_PAGE = "is_first_page";//引导页面的，是否是第一页

    private int pointWidthAndHeight;
    private int pointLeftMargin;
    private ViewPager viewPager;
    private LinearLayout ll_guide_point_group;
    private View v_guide_point_selected;
    // TODO 没有引导图
    int[] bgImageIds = new int[]{};

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_guideview);
        SystemBarUtils.setStatusBarTranslucent(this);
        SystemBarUtils.setStatusBarLightMode(this, true);
    }

    @Override
    protected void findViewById() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ll_guide_point_group = (LinearLayout) findViewById(R.id.ll_guide_point_group);
        v_guide_point_selected = findViewById(R.id.v_guide_point_selected);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic() {
        //viewPager加Fragment
        pointWidthAndHeight = (int) getResources().getDimension(R.dimen.guide_point_width);
        pointLeftMargin = (int) getResources().getDimension(R.dimen.guide_point_left_margin);
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < bgImageIds.length; i++) {
            GuideViewFragment guideViewFragment = new GuideViewFragment();
            Bundle args = new Bundle();
            args.putInt(IMAGE_ID, bgImageIds[i]);
            args.putBoolean(IS_LAST_PAGE, i == bgImageIds.length - 1);//是否是最后一页
            args.putBoolean(IS_FIRST_PAGE, i == 0);// 是否是第一页
            guideViewFragment.setArguments(args);
            fragments.add(guideViewFragment);
            //添加点
            View view = new View(this);
            view.setBackgroundResource(R.drawable.shape_point_normal);
            // view的宽高
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pointWidthAndHeight, pointWidthAndHeight);
            // 让每个点之间有间隙
            if (i != 0) {
                params.leftMargin = pointLeftMargin;
            }
            view.setLayoutParams(params);
            ll_guide_point_group.addView(view);
        }
        viewPager.setAdapter(new TitleFragmentPagerAdapter(getSupportFragmentManager(), fragments));
//        viewPager.setOffscreenPageLimit(5);//预加载多少个
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 移动的距离
                int leftMargin = (int) ((pointWidthAndHeight + pointLeftMargin) * (position + positionOffset));
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v_guide_point_selected
                        .getLayoutParams();
                params.leftMargin = leftMargin;
                v_guide_point_selected.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public static class GuideViewFragment extends Fragment implements View.OnClickListener {


        private TextView tv_gudie_view_btn;
        private ImageView iv_gudie_view_show;
        private View view;

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            if (view == null) {
                initView();
            }
            return view;
        }

        private void initView() {
            int imageId = getArguments().getInt(IMAGE_ID);
            boolean isLastPage = getArguments().getBoolean(IS_LAST_PAGE);
            boolean isFirstPage = getArguments().getBoolean(IS_FIRST_PAGE);
            view = View.inflate(getContext(), R.layout.item_guide_view, null);
            iv_gudie_view_show = (ImageView) view.findViewById(R.id.iv_gudie_view_show);//上面显示
            tv_gudie_view_btn = (TextView) view.findViewById(R.id.tv_gudie_view_btn);//按钮
            //上面显示
            iv_gudie_view_show.setBackgroundResource(imageId);

            //按钮
            if (!isLastPage) {
                //前几页
                tv_gudie_view_btn.setVisibility(View.INVISIBLE);
            } else {
                //最后一页
                tv_gudie_view_btn.setVisibility(View.VISIBLE);
                tv_gudie_view_btn.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            //进入到首页
            Intent intent = new Intent(getContext(), MainTabActivity.class);
            getActivity().startActivity(intent);
            PreferencesSaver.setStringAttr(getContext(), ZLConstants.Strings.VERSION_NAME, BuildConfig.VERSION_NAME);//保存版本信息
            getActivity().finish();
        }
    }
}
