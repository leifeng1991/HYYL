package com.xxzlkj.licallife.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.licallife.R;


public class ScheduleUtil {

    /**
     * 画刻度和值
     * @param context
     * @param linearLayout 父布局
     * @param type 1: 每天 2：每月
     */
    public static void drawingScale(Context context,LinearLayout linearLayout,int type){
        int number = 0;
        switch (type){
            case 1:// 每天
                number = 9;
                break;
            case 2:// 每月
                number = 11;
                break;
        }

        if (number > 0){
            for (int i = 0; i < number; i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.view_about_time_2,null);
                TextView mTimeTextView = (TextView) view.findViewById(R.id.id_time);

                int num = 0;
                switch (type){
                    case 1:// 每天
                        view.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 4));
                        num = i * 2 + 8;
                        break;
                    case 2:// 每月
                        if (i % 2 == 0){
                            // 二等分
                            view.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2));
                        }else {
                            // 四等分
                            view.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 4));
                        }
                        num = 1 + i * 3;
                        break;
                }

                if (num < 10){
                    mTimeTextView.setText("0" + num);
                }else {
                    mTimeTextView.setText(num + "");
                }

                linearLayout.addView(view);

            }
        }

    }


}
