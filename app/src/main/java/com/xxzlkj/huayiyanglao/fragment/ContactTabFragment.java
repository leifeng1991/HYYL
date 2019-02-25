package com.xxzlkj.huayiyanglao.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.adapter.ContactTabAdapter;
import com.xxzlkj.huayiyanglao.model.ContactItemBean;
import com.xxzlkj.huayiyanglao.model.UserFriendListBean;
import com.xxzlkj.huayiyanglao.webView.SideBar;
import com.xxzlkj.zhaolinshare.base.base.ReuseViewFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：通讯录栏目
 *
 * @author zhangrq
 *         2017/9/2 10:58
 */
public class ContactTabFragment extends ReuseViewFragment {
    private ContactTabAdapter mAdapter;
    private SideBar mSideBar;
    private TextView mUserDialog;
    private RecyclerView mRecyclerView;
    private int mIndex;
    private boolean move;
    private LinearLayoutManager linearLayoutManager;
    private boolean isSelectFriend;
    private List<UserFriendListBean.DataBean.GroupBean> groupBeanList;

    /**
     * @param isSelectFriend （必传）是否是选择好友
     */
    public static ContactTabFragment newInstance(boolean isSelectFriend) {
        ContactTabFragment fragment = new ContactTabFragment();
        fragment.isSelectFriend = isSelectFriend;
        return fragment;
    }

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_contact_tab, container, false);
    }

    @Override
    protected void findViewById() {
        mSideBar = getView(R.id.contact_sidebar);
        mUserDialog = getView(R.id.contact_dialog);
        mRecyclerView = getView(R.id.contact_member);
        // 关联
        mSideBar.setTextView(mUserDialog);
    }

    @Override
    public void setListener() {
        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                if (mAdapter != null && mAdapter.getItemCount() > 0) {
                    // 有数据
                    int position = mAdapter.getPositionForSection(s.charAt(0));
                    if (position != -1) {
                        smoothScrollToPosition(position);
                    }
                }
            }
        });


    }

    @Override
    public void processLogic() {
        // 初始化RecyclerView、Adapter
        mAdapter = new ContactTabAdapter(mActivity, mContext, R.layout.item_contact_item, isSelectFriend);
        linearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerViewListener());
        mRecyclerView.setAdapter(mAdapter);
        // 设置内容
        if (groupBeanList != null && groupBeanList.size() > 0) {
            mAdapter.clearAndAddList(groupBeanList);
        }
    }

    /**
     * 设置网络数据
     */
    public void setData(List<UserFriendListBean.DataBean.GroupBean> groupBeanList) {
        this.groupBeanList = groupBeanList;
        if (mAdapter != null) {
            mAdapter.clearAndAddList(groupBeanList);
        }
    }

    /**
     * 获取选中的联系人
     */
    public ArrayList<UserFriendListBean.DataBean.GroupBean> getSelectList() {
        ArrayList<UserFriendListBean.DataBean.GroupBean> selectList = new ArrayList<>();
        if (mAdapter != null && mAdapter.getItemCount() > 0) {
            List<UserFriendListBean.DataBean.GroupBean> list = mAdapter.getList();
            for (UserFriendListBean.DataBean.GroupBean groupBean : list) {
                List<ContactItemBean> contactList = groupBean.getList();
                if (contactList != null && contactList.size() > 0) {
                    // 字母组好友列表没有问题
                    if (isSelectFriend(contactList)) {
                        // 有选择的好友，添加字母好友表
                        UserFriendListBean.DataBean.GroupBean newGroupBean = new UserFriendListBean.DataBean.GroupBean(groupBean.getTitle());
                        ArrayList<ContactItemBean> newList = new ArrayList<>();
                        for (ContactItemBean listBean : contactList) {
                            if (listBean.isSelected()) {
                                newList.add(listBean);
                            }
                        }
                        newGroupBean.setList(newList);
                        selectList.add(newGroupBean);
                    }
                }
            }
        }
        return selectList;
    }

    private boolean isSelectFriend(List<ContactItemBean> contactList) {
        if (contactList == null || contactList.size() == 0) {
            return false;
        }
        for (ContactItemBean contactItemBean : contactList) {
            if (contactItemBean.isSelected()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 处理后的 smoothScrollToPosition()
     */
    public void smoothScrollToPosition(int position) {
        mIndex = position;
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = linearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = linearLayoutManager.findLastVisibleItemPosition();
        //然后区分情况
        if (position <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            int top = mRecyclerView.getChildAt(position - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            mRecyclerView.smoothScrollToPosition(position);
            // 之后判断滚动监听里面的 onScrollStateChanged()
            move = true;
        }
    }


    private class RecyclerViewListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (move && newState == RecyclerView.SCROLL_STATE_IDLE) {
                move = false;
                move = false;
                //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                int n = mIndex - linearLayoutManager.findFirstVisibleItemPosition();
                if (0 <= n && n < mRecyclerView.getChildCount()) {
                    //获取要置顶的项顶部离RecyclerView顶部的距离
                    int top = mRecyclerView.getChildAt(n).getTop();
                    //最后的移动
                    mRecyclerView.scrollBy(0, top);
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //在这里进行第二次滚动（最后的100米！）
            if (move) {
                move = false;
                //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                int n = mIndex - linearLayoutManager.findFirstVisibleItemPosition();
                if (0 <= n && n < mRecyclerView.getChildCount()) {
                    //获取要置顶的项顶部离RecyclerView顶部的距离
                    int top = mRecyclerView.getChildAt(n).getTop();
                    //最后的移动
                    mRecyclerView.scrollBy(0, top);
                }
            }
        }
    }

    public interface OnContactItemClickListener {
        void onContactItemClick();
    }

}
