package com.xxzlkj.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.xxzlkj.shop.R;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.BitmapUtils;
import com.xxzlkj.zhaolinshare.base.util.PermissionHelper;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import static android.app.Activity.RESULT_OK;


/**
 * 描述: 选择图片的adapter
 *
 * @author zhangrq
 *         2016/12/12 11:06
 */

public class ImageSelectAdapter extends BaseAdapter<String> {
    public static final int REQUEST_IMAGE = 123;
    public static final int REQUEST_VIEW_PAGE = 100;

    private Activity activity;
    private PermissionHelper mHelper;

    public ImageSelectAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, final int position, final String itemBean) {
        ImageView iv_show = holder.getView(R.id.iv_show);
        ImageView iv_delete = holder.getView(R.id.iv_delete);
        if (TextUtils.isEmpty(itemBean)) {
            // 最后一个
            iv_delete.setVisibility(View.GONE);
            iv_show.setImageResource(R.mipmap.ic_upload_image);
            iv_show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectImage();

                }
            });
        } else {
            // 普通的图片
            iv_show.setImageBitmap(BitmapUtils.decodeSampledBitmap(itemBean, 2));
            iv_show.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ArrayList<String> imageList = getImageList();
//                    activity.startActivityForResult(ImageViewPageActivity.newIntent(mContext, imageList, imageList.indexOf(itemBean)), REQUEST_VIEW_PAGE);
                }
            });
            iv_delete.setVisibility(View.VISIBLE);
            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 删除
                    deleteItem(position);
                }
            });
        }

    }

    private void selectImage() {
        // 选择图片
        mHelper.requestPermissions("请授予[读写][拍照]权限，否则无法拍照和读取本地图片", new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                // 请求权限成功
                MultiImageSelector.create(mContext)
                        .showCamera(true) // show camera or not. true by default
                        .count(4) // max select image size, 9 by default. used width #.multi()
                        .multi() // multi mode, default mode;
                        .origin(getImageList()) // original select data set, used width #.multi()
                        .start(activity, REQUEST_IMAGE);
            }

            @Override
            public void doAfterDenied(String... permission) {

            }
        }, "android.permission.READ_EXTERNAL_STORAGE", android.Manifest.permission.CAMERA);

    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
        mHelper = new PermissionHelper(activity);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImageSelectAdapter.REQUEST_IMAGE) {
            // 选择的图片
            if (resultCode == RESULT_OK) {
                // Get the result list of select image paths
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                path.add("");
                clear();
                addList(path);
            }
        }
//        if (requestCode == ImageSelectAdapter.REQUEST_VIEW_PAGE) {
//            // viewpager的选择后的图片
//            if (resultCode == RESULT_OK) {
//                // Get the result list of select image paths
//                List<String> path = data.getStringArrayListExtra(ImageViewPageActivity.SELECT_RESULT_LIST);
//                path.add("");
//                clear();
//                addList(path);
//            }
//        }
    }

    public void handleRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        mHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public ArrayList<String> getImageList() {
        ArrayList<String> list = (ArrayList<String>) getList();
        ArrayList<String> imageList = new ArrayList<>();
        for (String s : list) {
            if (TextUtils.isEmpty(s)) {
                continue;
            }
            imageList.add(s);
        }
        return imageList;
    }

}


